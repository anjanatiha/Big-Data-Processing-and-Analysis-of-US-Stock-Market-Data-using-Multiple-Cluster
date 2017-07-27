package SparkAnalysis;

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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static DataFieldType.StockExchanges.getExchangesMap;
import static Misc.FileClass.getFileType;
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
    private String calcType="";
    private Integer calcTypeInt=-1;
    private HashMap<String, Integer> exchageMap;
    private int selectColumn=-1;

    TAQAnalyzer(JavaSparkContext sc, String[] args, String inputFileName) {
        this.TAQFileType = getFileType(inputFileName);
        this.inputFileName = args[0];
        this.exchageMap = getExchangesMap();
        this.fileYear = extractYear(inputFileName);
        if (!args[1].equals("n")) {
            this.filterTime = true;
            this.startTime = args[1];
            this.endTime = args[2];
            print("Start Time: " + startTime + " End Time : " + endTime);
        }
        if (!args[3].equals("n")) {
            this.tickerSymbols = wordCollect(sc, args[3]);
            this.filterTickers = true;
        }
        if (!args[4].equals("n")) {
            this.columnList = columnSelect(sc, args[4]);
            this.filterColumns = true;
        }
        this.inputFileType = getInputFileType(inputFileName);
        print("Type Calculation type: (convert:1, spread:2, volatility:3, exchangesComp:4)");
        Scanner scan = new Scanner(System.in);
        this.calcType = scan.next();
        if(calcType.equals("4") || calcType.equals("exchangesComp")){
            print("Select single column to compare across exchanges: ");
            this.selectColumn = scan.nextInt();
        }
        this.outputFileName = getOutputFileName(inputFileName, this.calcType);
        this.sc = sc;
        this.partionSize = Integer.parseInt(args[5]);

        print("File Year: " + fileYear);
        setFieldTypes();

        String outputFileName_unzip = outputFileName;
        if (inputFileType.equals("zip")) {
            print("Unzipping Started for + " + inputFileName);
            long startTime = System.currentTimeMillis();
            this.sizeStr = unZip(inputFileName, outputFileName);
            print("Unzipping Completed for + " + inputFileName);
            long endTime = System.currentTimeMillis();
            printElapsedTime(startTime, endTime, " unzipping ");
            this.inputFileName = outputFileName;
            this.outputFileName = getOutputFileName(this.inputFileName, this.calcType);

        }
        if (filterTime)
            setTime();
        convertFile();
//        deleteFileorDir(outputFileName_unzip);
    }


    public void setFieldTypes(){
        switch (this.fileYear) {
            case "2010":
                this.ITAQSpecObject = new TAQ102010Spec();
                break;
            case "2012":
                this.ITAQSpecObject = new TAQ072012Spec();
                break;
            case "2013":
                this.ITAQSpecObject = new TAQ082013Spec();
                break;
            case "2015":
                this.ITAQSpecObject = new TAQ062015Spec();
                break;
            case "2016":
                this.ITAQSpecObject = new TAQ062016Spec();
                break;
        }
        switch (this.TAQFileType) {
            case "trade":
                this.fieldTypes = ITAQSpecObject.getTradeFields();
                break;
            case "nbbo":
                this.fieldTypes = ITAQSpecObject.getNBBOFields();
                break;
            case "quote":
                this.fieldTypes = ITAQSpecObject.getQuoteFields();
                break;
        }
    }

    private void convertFile() {
        JavaRDD<String> text_file = sc.textFile(inputFileName);
        JavaRDD<String> convertedObject = null;
        switch (calcType) {
            case "convert":
                convertedObject = text_file.map(line -> convertLine(line));
                break;
            case "1":
                convertedObject = text_file.map(line -> convertLine(line));
                break;
            case "spread":
                convertedObject = text_file.map(line -> spread(line));
                break;
            case "2":
                convertedObject = text_file.map(line -> spread(line));
                break;
            case "volatility":
                convertedObject = text_file.map(line -> volatlity(line));
                break;
            case "3":
                convertedObject = text_file.map(line -> volatlity(line));
                break;
            case "exchangeComp":
                convertedObject = text_file.map(line -> exchangeComp(line, this.selectColumn));
                break;
            case "4":
                convertedObject = text_file.map(line -> exchangeComp(line, this.selectColumn));
                break;
            default:
                print("no matching option found");
                break;
        }
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
        if ((line.trim()).length()<getTotalRecordLength()/4)
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
        if (line.trim().length()<getTotalRecordLength()/4)
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
                    if (i < colMax)
                        str = str + ",";
                }
            }

            start = start + fieldTypes[i].getLength();
        }
        spread = (bestAsk - bestBid) / ((bestAsk + bestBid) / 2);
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

    private String volatlity(String line) {
        if ((line.trim()).length()<getTotalRecordLength()/4)
            return "\r";
        String str = "";
        int start = 0;
        BigInteger time = BigInteger.ZERO;
        int priceI = 5;
        double price = -1.0;
        String exchange = "";
        String stock = "";
        boolean inTime = false;
        boolean inTicker = false;
        int colMax;
        if (filterColumns)
            colMax = Collections.max(columnList);
        else
            colMax = fieldTypes.length - 1;
        for (int i = 0; i <= colMax; i++) {
            String tempStr = fieldTypes[i].convertFromBinary(line, start);
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
            if (i==0)
                time = new BigInteger(tempStr);
            else if (i==1)
                exchange = tempStr;
            else if (i==2)
                stock= tempStr;
            else if (i==priceI)
                price = Double.parseDouble(tempStr);

            if (filterTickers) {
                if (i == 2) {
                    if (tickerSymbols.contains(tempStr)) {
                        inTicker = true;
                    } else
                        return "\r";
                }
            }

            start = start + fieldTypes[i].getLength();
        }
        str = time + ", "+ exchange + ", " + stock + ", "+price;
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
    private String exchangeComp(String line, int colI) {
        if ((line.trim()).length()<getTotalRecordLength()/4)
            return "\r";
        String str = "";
        int start = 0;
        int time;
        String timeStr = "";
        String stock="";
        boolean inTime = false;
        boolean inTicker = false;
        int colMax;
        String exchange="";
        String colVal="";
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

            if (i==0)
                timeStr = tempStr;
            if (i==1){
                exchange = tempStr;
            }
            if (i==2)
                stock=tempStr;
            if (i==colI)
                colVal =tempStr;
            start = start + fieldTypes[i].getLength();
        }

        int exchangeIndex = exchageMap.get(exchange);

        str = str+stock+","+timeStr;

        if (!filterTime && !filterTickers) {
            return str;
        } else {

            if (filterTime && filterTickers) {
                if (inTime && inTicker) {
                    str = stockCompLine(str, colVal, exchangeIndex);
                    print(str);
                    return str;
                }

            } else if (filterTime && !filterTickers) {
                if (inTime) {
                    str = stockCompLine(str, colVal, exchangeIndex);
                    print(str);
                    return str;
                }
            } else if (!filterTime && filterTickers) {
                if (inTicker) {
                    str = stockCompLine(str, colVal, exchangeIndex);
                    print(str);
                    return str;
                }
            }
        }
        return "\r";
    }

    public String stockCompLine(String str, String colVal, int exchangeIndex){
        for(int i =0; i<getExchangesMap().size();i++){
            if (i==exchangeIndex)
                str = str + colVal;
            else if (i<getExchangesMap().size()-1)
                str = str+",";
        }
        return str;

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
    public int getTotalRecordLength() {
        int recordLength = 0;
        for (int i = 0; i < fieldTypes.length; i++) {
            recordLength += fieldTypes[i].getLength();
        }
        return recordLength;
    }

}
