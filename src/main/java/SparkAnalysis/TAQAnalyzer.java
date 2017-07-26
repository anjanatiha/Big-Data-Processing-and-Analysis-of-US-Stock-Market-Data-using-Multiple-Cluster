package SparkAnalysis; /**
 * Created by Anjana on 5/29/2017.
 */

import DataFieldType.*;
import org.apache.commons.io.FileUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static Misc.FileClass.unZip;
import static Misc.FileProperties.*;
import static Misc.Print.print;
import static Misc.Time.printElapsedTime;

public class TAQAnalyzer implements Serializable {
    private String inputFileName;
    private String outputFileName;
    private IFieldType[] fieldTypes;
    private ITAQSpec ITAQSpecObject;
    private String startTime;
    private String endTime;
    private List<String> tickerSymbols;
    private List<Integer> columnList;
    private boolean filterTickers = false;
    private boolean filterTime = false;
    private boolean filterColumns = false;
    private static JavaSparkContext sc;
    private String TAQFileType = "";
    private String inputFileType = "";
    private String fileYear;
    private String sizeStr = "";
    private int partionSize = 0;

    TAQAnalyzer(JavaSparkContext sc, String[] args, String inputFileName) {
        this.TAQFileType = args[0];
        this.inputFileName = args[2];
        this.fileYear = extractYear(inputFileName);
        if (!args[3].equals("n")) {
            this.filterTime = true;
            this.startTime = args[3];
            this.endTime = args[4];
            print("Start Time: " + startTime + " End Time : " + endTime);
        }
        if (!args[5].equals("n")) {
            this.tickerSymbols = wordCollect(sc, args[5]);
            this.filterTickers = true;
        }
        if (!args[6].equals("n")) {
            this.columnList = columnSelect(sc, args[6]);
            this.filterColumns = true;
        }
        this.inputFileType = getInputFileType(inputFileName);
        this.outputFileName = getOutputFileName(inputFileName, "Spread");
        this.sc = sc;
        this.partionSize = Integer.parseInt(args[7]);

        print("File Year: " + fileYear);
        switch (fileYear) {
            case "2010":
                ITAQSpecObject = new TAQ102010Spec();
                break;
            case "2012":
                ITAQSpecObject = new TAQ072012Spec();
                break;
            case "2013":
                ITAQSpecObject = new TAQ082013Spec();
                break;
            case "2015":
                ITAQSpecObject = new TAQ062015Spec();
                break;
            case "2016":
                ITAQSpecObject = new TAQ062016Spec();
                break;
        }
        switch (TAQFileType) {
            case "trade":
                fieldTypes = ITAQSpecObject.getTradeFields();
                break;
            case "nbbo":
                fieldTypes = ITAQSpecObject.getNBBOFields();
                break;
            case "quote":
                fieldTypes = ITAQSpecObject.getQuoteFields();
                break;
        }

        String outputFileName_unzip = outputFileName;
        if (inputFileType.equals("zip")) {
            print("Unzipping Started for + " + inputFileName);
            long startTime = System.currentTimeMillis();
            this.sizeStr = unZip(inputFileName, outputFileName);
            print("Unzipping Completed for + " + inputFileName);
            long endTime = System.currentTimeMillis();
            printElapsedTime(startTime, endTime, " unzipping ");
            this.inputFileName = outputFileName;
            this.outputFileName = getOutputFileName(this.inputFileName, "Spread");

        }

        setTime();
        convertFile();
//        deleteFileorDir(outputFileName_unzip);
    }

    private void convertFile() {
        JavaRDD<String> text_file = sc.textFile(inputFileName);
        JavaRDD<String> convertedObject;
        convertedObject = text_file.map(line -> spread(line));
        if (this.partionSize == -1)
            convertedObject = convertedObject.filter(line -> !line.equals("\r"));
        else
            convertedObject = convertedObject.filter(line -> !line.equals("\r")).coalesce(this.partionSize);
        Path path = Paths.get(outputFileName);
        if (Files.exists(path)) {
            try {
                FileUtils.deleteDirectory(new File((outputFileName + "/")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        convertedObject.saveAsTextFile(outputFileName);
        System.gc();
    }

    private String convertLine(String line) {
        if ((line.trim()).length()<37)
            return "\r";
        String str = "";
        int start = 0;
        int time;
        boolean inTime = false;
        boolean inTicker = false;
        int colMax;
        if (filterColumns)
            colMax = Collections.max(columnList);
        else
            colMax = fieldTypes.length - 1;
        for (int i = 0; i < colMax; i++) {
            String tempStr = fieldTypes[i].convertFromBinary(line, start);
            if (filterTime) {
                if (i == 0) {
                    time = Integer.parseInt(tempStr);
                    if ((time >= Integer.parseInt(startTime)) && (time <= Integer.parseInt(endTime))) {
                        inTime = true;
                    } else
                        return "\r";
                }
            }
            if (filterTickers) {
                if (i == 2) {
                    if (tickerSymbols.contains(tempStr)) {
                        inTicker = true;
                    } else
                        return "\r";
                }
            }
            if (!filterColumns) {
                str = str + tempStr;
                if (i < fieldTypes.length - 2)
                    str = str + ",";
            } else if (filterColumns) {
                if (columnList.contains(i)) {
                    str = str + tempStr;
                    if (i < colMax - 1)
                        str = str + ",";
                }
            }

            start = start + fieldTypes[i].getLength();

        }
        str = str + "\n";
        if (!filterTime && !filterTickers) {
            return str;
        } else {

            if (filterTime && filterTickers) {
                if (inTime && inTicker) {
                    return str;
                }

            } else if (filterTime && !filterTickers) {
                if (inTime)
                    return str;
            } else if (!filterTime && filterTickers) {
                if (inTicker)
                    return str;
            }
        }
        return "\r";
    }

    private String spread(String line) {
        if (line.trim().length()<40)
            return "\r";
        String str = "";
        int start = 0;
        BigInteger time = BigInteger.ZERO;
        double bestBid = 0;
        double bestAsk = 0;
        double spread = 0;
        int bestBidI = 3;
        int bestAskI = 5;
        boolean inTime = false;
        boolean inTicker = false;
        int colMax;
        if (filterColumns) {
            colMax = Collections.max(columnList);
        }
        else
            colMax = fieldTypes.length - 1;
        for (int i = 0; i <= colMax; i++) {
            String tempStr = fieldTypes[i].convertFromBinary(line, start);
            if (i==bestBidI) {
                bestBid = Double.parseDouble(tempStr);
            }
            if (i==bestAskI) {
                bestAsk = Double.parseDouble(tempStr);
            }

            if (filterTime) {
                if (i == 0) {
                    time = new BigInteger(tempStr);
                    int c1 = time.compareTo(new BigInteger(startTime));
                    int c2 = time.compareTo(new BigInteger(endTime));
                    if (((c1==1)||(c1==0)) && ((c2==-1)||(c2==0))) {
                        inTime = true;
                    } else
                        return "\r";
                }
            }
            if (filterTickers) {
                if (i == 2) {
                    if (tickerSymbols.contains(tempStr)) {
                        inTicker = true;
                    } else
                        return "\r";
                }
            }
            if (!filterColumns) {
                str = str + tempStr;
                    if (i < fieldTypes.length - 2)
                    str = str + ",";
            } else if (filterColumns) {
                if (columnList.contains(i)) {
                    str = str + tempStr;
                    if (i < colMax - 1)
                        str = str + ",";
                }
            }
            spread = (bestAsk - bestBid) / ((bestAsk + bestBid) / 2);
            start = start + fieldTypes[i].getLength();
        }
        str = str +" , "+ spread+"\n";
        if (!filterTime && !filterTickers) {
            return str;
        } else {
            if (filterTime && filterTickers) {
                if (inTime && inTicker) {
                    return str;
                }

            } else if (filterTime && !filterTickers) {
                if (inTime)
                    return str;
            } else if (!filterTime && filterTickers) {
                if (inTicker)
                    return str;
            }
        }
        return "\r";
    }
    public void setTime(){
        int timeLen= this.fieldTypes[0].getLength();
        int s=(this.startTime).length();
        int e=(this.endTime).length();
        if ((this.startTime).length()<timeLen){
            for (int i = 0; i < timeLen - s; i++) {
                this.startTime = this.startTime + "0";
            }
        }
        if ((this.endTime).length()<timeLen) {
            for (int i = 0; i < timeLen - e; i++) {
                this.endTime = this.endTime + "0";
            }
        }

    }
    public int getlength() {
        int recordLength = 0;
        for (int i = 0; i < fieldTypes.length; i++) {
            recordLength += fieldTypes[i].getLength();
        }
        return recordLength;
    }
    private String volatlity(String line) {
        if ((line.trim()).length()<37)
            return "\r";
        String str = "";
        int start = 0;
        int time;
        int priceI = 5;
        boolean inTime = false;
        boolean inTicker = false;
        int colMax;
        if (filterColumns)
            colMax = Collections.max(columnList);
        else
            colMax = fieldTypes.length - 1;
        for (int i = 0; i < colMax; i++) {
            String tempStr = fieldTypes[i].convertFromBinary(line, start);
            if (filterTime) {
                if (i == 0) {
                    time = Integer.parseInt(tempStr);
                    if ((time >= Integer.parseInt(startTime)) && (time <= Integer.parseInt(endTime))) {
                        inTime = true;
                    } else
                        return "\r";
                }
            }
            if (filterTickers) {
                if (i == 2) {
                    if (tickerSymbols.contains(tempStr)) {
                        inTicker = true;
                    } else
                        return "\r";
                }
            }
            if (i==priceI){
                str = tempStr;
            }
            start = start + fieldTypes[i].getLength();
        }
        if (!filterTime && !filterTickers) {
            return str;
        } else {

            if (filterTime && filterTickers) {
                if (inTime && inTicker) {
                    return str;
                }

            } else if (filterTime && !filterTickers) {
                if (inTime)
                    return str;
            } else if (!filterTime && filterTickers) {
                if (inTicker)
                    return str;
            }
        }
        return "\r";
    }

}
