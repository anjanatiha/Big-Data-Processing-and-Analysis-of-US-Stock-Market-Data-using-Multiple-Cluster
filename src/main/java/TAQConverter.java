/**
 * Created by Anjana on 5/29/2017.
 */

import DataFieldType.*;
import org.apache.commons.io.FileUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static DataFieldType.StockExchanges.getExchangesMap;
import static DataFieldType.TAQFileInfo.extractYear;
import static DataFieldType.TAQFileInfo.getFileType;
import static FilePackage.FileClass.getParentDir;
import static FilePackage.FileClass.isDirectory;
import static FilePackage.FileName.*;
import static FilePackage.ZipExtract.unZip;
import static Misc.Debug.debug;
import static Misc.Print.print;
import static Misc.Time.printElapsedTime;


public class TAQConverter implements Serializable {
    private static JavaSparkContext sc;
    private String inputFileName;
    private String inputFileAbsolutePath;
    private String inputFileType = "";
    private String inputFileNameUnzipped;
    private String inputFileUnzippedAbsolutePath;
    private String OutputFileAbsolutePath;
    private String outputFileName;
    private String inputDirectoryName;
    private String inputDirectoryAbsolutePath;
    private String finalInputFileAbsolutePath;
    private boolean batchConversion = false;
    private String outputDirectoryName;
    private String outputDirectoryAbsolutePath;
    private String unzipDirectoryName;
    private String unzipDirectoryAbsolutePath;
    private TAQAttributes TAQAttributesObject = null;
    private IFieldType[] fieldTypes;
    private ITAQSpec ITAQSpecObject;
    private String startTime;
    private String endTime;
    private List<String> tickerSymbols;
    private List<Integer> columnList;
    private Integer function = -1;
    private int selectColumn = -1;
    private boolean filterTickers = false;
    private boolean filterTime = false;
    private boolean filterColumns = false;
    private HashMap<String, Integer> exchageMap;
    private String TAQFileType = "";
    private String fileYear;
    private int clusterSize = -1;
    private String sizeStr = "";

    TAQConverter(JavaSparkContext sc, String[] args, String inputFileName, TAQAttributes TAQAttributesObject) {
        this.sc = sc;
        this.inputFileAbsolutePath = inputFileName;
        this.TAQAttributesObject = TAQAttributesObject;
        this.finalInputFileAbsolutePath = inputFileName;
        setFileProperty();
        setTAQConverterObject();
        if (inputFileType.equals("zip")) {
            print("Unzipping  file : " + this.inputFileName);
            long startTime = System.currentTimeMillis();
            this.inputFileNameUnzipped = getUnzippedFileName(this.inputFileName);
            this.inputFileUnzippedAbsolutePath = unzipDirectoryName + "/" + inputFileNameUnzipped;
            this.sizeStr = unZip(inputFileAbsolutePath, inputFileUnzippedAbsolutePath);
            print("Unzipping Completed for + " + inputFileName + " Size : " + this.sizeStr);
            long endTime = System.currentTimeMillis();
            printElapsedTime(startTime, endTime, " unzipping ");
            this.finalInputFileAbsolutePath = inputFileUnzippedAbsolutePath;

        }
        setFieldTypes();

        if (filterTime)
            setTime();
        print("File Year : " + this.fileYear);
        print("TAQ File Type : " + this.TAQFileType);
        print("startTime :" + this.startTime);
        print("endTime :" + this.endTime);
        convertFile();
//        Scanner scan = new Scanner(System.in);
//        print("Dou you want to delete unzipped file : " + outputFileName_unzip +"??");
//        String dStr = scan.next();
//        if (dStr.equals("y"))
//            deleteFileorDir(outputFileName_unzip);

    }

    private void setFileProperty() {
        this.inputFileName = getInputFileName(inputFileAbsolutePath);
        this.inputFileType = getInputFileType(inputFileName);
        this.inputDirectoryName = getParentDir(inputFileAbsolutePath);
        this.TAQFileType = getFileType(inputFileAbsolutePath);
        this.fileYear = extractYear(inputFileAbsolutePath);
        this.unzipDirectoryAbsolutePath = TAQAttributesObject.getUnzipDirectoryAbsolutePath();
        this.batchConversion = TAQAttributesObject.getBatchConversion();
        this.outputDirectoryName = TAQAttributesObject.getOutputDirectoryAbsolutePath();
        this.unzipDirectoryName = TAQAttributesObject.getUnzipDirectoryAbsolutePath();
        this.outputFileName = getConvertedFileName(inputFileName);
        this.OutputFileAbsolutePath = this.outputDirectoryName + "/" + outputFileName;

    }

    private void setTAQConverterObject() {
        this.startTime = TAQAttributesObject.getStartTime();
        this.endTime = TAQAttributesObject.getEndTime();
        this.filterTime = TAQAttributesObject.getfilterTime();
        this.tickerSymbols = TAQAttributesObject.getTickerSymbols();
        this.columnList = TAQAttributesObject.getColumnList();
        this.filterTickers = TAQAttributesObject.getFilterTickers();
        this.filterColumns = TAQAttributesObject.getFilterColumn();
        this.function = TAQAttributesObject.getFunction();
        this.selectColumn = TAQAttributesObject.getSelectColumn();
        this.exchageMap = TAQAttributesObject.getExchageMap();
        this.clusterSize = TAQAttributesObject.getClusterSize();

    }

    private void convertFile() {

        JavaRDD<String> text_file = sc.textFile(finalInputFileAbsolutePath);
        JavaRDD<String> convertedObject;

        Function2 removeHeader = new Function2<Integer, Iterator<String>, Iterator<String>>() {
            @Override
            public Iterator<String> call(Integer ind, Iterator<String> iterator) throws Exception {
                if (ind == 0 && iterator.hasNext()) {
                    iterator.next();
                    return iterator;
                } else
                    return iterator;
            }
        };
        switch (this.function) {
            case 1:
                convertedObject = text_file.map(line -> convertLine(line));
                break;

            case 2:
                convertedObject = text_file.map(line -> spread(line));
                break;
            case 3:
                convertedObject = text_file.map(line -> volatlity(line));
                break;

            case 4:
                convertedObject = text_file.map(line -> exchangeComp(line, this.selectColumn));
                break;
            default:
                print("no matching option found");
                break;
        }

        convertedObject = text_file.mapPartitionsWithIndex(removeHeader, false);
        convertedObject = convertedObject.map(line -> convertLine(line));

        if (this.clusterSize == -1)

            convertedObject = convertedObject.filter(line -> !line.equals("\r"));
        else
            convertedObject = convertedObject.filter(line -> !line.equals("\r")).coalesce(this.clusterSize);

        if (isDirectory(OutputFileAbsolutePath)) {
            try {
                FileUtils.deleteDirectory(new File((OutputFileAbsolutePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        print("OutputFileAbsolutePath :" + OutputFileAbsolutePath);

        convertedObject.saveAsTextFile(OutputFileAbsolutePath);

        System.gc();
    }

    private String convertLine(String line) {
        String str = "";
        int start = 0;
        BigInteger time;
        boolean inTime = false;
        boolean inTicker = false;
        int colMax;
        if (filterColumns) {
            colMax = Collections.max(columnList);
        } else
            colMax = fieldTypes.length - 1;

        for (int i = 0; i < colMax; i++) {
            String tempStr = fieldTypes[i].convertFromBinary(line, start);
            if (filterTime) {
                if (i == 0) {
                    time = new BigInteger(tempStr);
                    int c1 = time.compareTo(new BigInteger(startTime));
                    int c2 = time.compareTo(new BigInteger(endTime));
                    if (((c1 == 1) || (c2 == 0)) && ((c2 == -1) || (c2 == 0))) {
                        inTime = true;
                    } else {
                        return "\r";
                    }
                }
            }
            if (filterTickers) {
                if (i == 2) {
                    if (tickerSymbols.contains(tempStr)) {
                        inTicker = true;
                    } else {
                        return "\r";
                    }
                }
            }
            if (!filterColumns) {
                str = str + tempStr;
                if (i < fieldTypes.length - 2)
                    str = str + ",";
            } else if (filterColumns) {
                if (columnList.contains(i + 1)) {
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
                if (inTime) {
                    return str;
                }
            } else if (!filterTime && filterTickers) {
                if (inTicker) {
                    return str;
                }
            }
        }
        return "\r";
    }

    private void setFieldTypes() {
        switch (this.fileYear) {
            case "2010":
//                this.ITAQSpecObject = new TAQ102010Spec();
                this.ITAQSpecObject = new TAQ102010SpecNew();
                break;
            case "2012":
                this.ITAQSpecObject = new TAQ072012Spec();
                break;
            case "2013":
                this.ITAQSpecObject = new TAQ082013Spec();
                break;
            case "2015":
                debug("2015");
//                this.ITAQSpecObject = new TAQ062015Spec();
                this.ITAQSpecObject = new TAQ062015SpecNew();
                break;
            case "2016":
//                this.ITAQSpecObject = new TAQ062016Spec();
                this.ITAQSpecObject = new TAQ062016SpecNew();
                break;
            default:
                print("No year found");
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
            default:
                print("No matiching TAQ file type found");
                break;
        }
    }

    public int getlength() {
        int recordLength = 0;
        for (int i = 0; i < fieldTypes.length; i++) {
            recordLength += fieldTypes[i].getLength();
        }
        return recordLength;
    }

    private void setTime() {
        int timeLen = this.fieldTypes[0].getLength();
        int s = (this.startTime).length();
        int e = (this.endTime).length();
        if ((this.startTime).length() < timeLen) {
            for (int i = 0; i < timeLen - s; i++) {
                this.startTime = this.startTime + "0";
            }
        }
        if ((this.endTime).length() < timeLen) {
            for (int i = 0; i < timeLen - e; i++) {
                this.endTime = this.endTime + "0";
            }
        }

    }

    //
//
//
//
    private String spread(String line) {
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
        } else
            colMax = fieldTypes.length - 1;
        for (int i = 0; i <= colMax; i++) {
            String tempStr = fieldTypes[i].convertFromBinary(line, start);
            if (i == bestBidI) {
                bestBid = Double.parseDouble(tempStr);
            }
            if (i == bestAskI) {
                bestAsk = Double.parseDouble(tempStr);
            }

            if (filterTime) {
                if (i == 0) {
                    time = new BigInteger(tempStr);
                    int c1 = time.compareTo(new BigInteger(startTime));
                    int c2 = time.compareTo(new BigInteger(endTime));
                    if (((c1 == 1) || (c1 == 0)) && ((c2 == -1) || (c2 == 0))) {
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
        str = str + " , " + spread + "\n";
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
                    if (((c1 == 1) || (c1 == 0)) && ((c2 == -1) || (c2 == 0))) {
                        inTime = true;
                    } else
                        return "\r";
                }
            }
            if (i == 0)
                time = new BigInteger(tempStr);
            else if (i == 1)
                exchange = tempStr;
            else if (i == 2)
                stock = tempStr;
            else if (i == priceI)
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
        str = time + ", " + exchange + ", " + stock + ", " + price;
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
        String str = "";
        int start = 0;
        int time;
        String timeStr = "";
        String stock = "";
        boolean inTime = false;
        boolean inTicker = false;
        int colMax;
        String exchange = "";
        String colVal = "";
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

            if (i == 0)
                timeStr = tempStr;
            if (i == 1) {
                exchange = tempStr;
            }
            if (i == 2)
                stock = tempStr;
            if (i == colI)
                colVal = tempStr;
            start = start + fieldTypes[i].getLength();
        }

        int exchangeIndex = this.exchageMap.get(exchange);

        str = str + stock + "," + timeStr;

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

    private String stockCompLine(String str, String colVal, int exchangeIndex) {
        for (int i = 0; i < getExchangesMap().size(); i++) {
            if (i == exchangeIndex)
                str = str + colVal;
            else if (i < getExchangesMap().size() - 1)
                str = str + ",";
        }
        return str;

    }
}
