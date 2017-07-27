/**
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
import java.util.Scanner;

import static Misc.Debug.debug;
import static Misc.FileClass.*;
import static Misc.FileProperties.*;
import static Misc.Print.print;
import static Misc.Time.printElapsedTime;

public class TAQConverter2 implements Serializable {
    private String inputFileName;
    private String outputFileName;
    public IFieldType[] fieldTypes;
    public ITAQSpec ITAQSpecObject;
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
    private int clusterSize = -1;
    FileAttributes fileAttributesObject=null;

    TAQConverter2(JavaSparkContext sc, String[] args, String inputFileName, FileAttributes fileAttributesObject) {
        this.TAQFileType = getFileType(inputFileName);
        this.inputFileName = args[0];
        this.fileYear = extractYear(inputFileName);
        print("this.fileYear " + this.fileYear);
        this.fileAttributesObject = fileAttributesObject;
        setTAQConverterObject();
        this.inputFileType = getInputFileType(inputFileName);
        this.outputFileName = getOutputFileName(inputFileName);
        this.sc = sc;
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
            this.outputFileName = getOutputFileName(this.inputFileName);
        }

        if (filterTime)
            setTime();
        print("File Year : " + this.fileYear);
        print("TAQ File Type : " + this.TAQFileType);
        print("startTime :" + this.startTime);
        print("endTime :" + this.endTime);
        convertFile();
        Scanner scan = new Scanner(System.in);
        print("Dou you want to delete unzipped file : " + outputFileName_unzip +"??");
        String dStr = scan.next();
        if (dStr.equals("y"))
            deleteFileorDir(outputFileName_unzip);

    }
    public void setTAQConverterObject(){
        this.startTime = fileAttributesObject.getStartTime();
        this.endTime = fileAttributesObject.getEndTime();
        this.filterTime = fileAttributesObject.getfilterTime();
        this.tickerSymbols = fileAttributesObject.getTickerSymbols();
        this.columnList = fileAttributesObject.getColumnList();
        this.filterTickers = fileAttributesObject.getFilterTickers();
        this.filterColumns = fileAttributesObject.getFilterColumn();
        this.clusterSize = fileAttributesObject.getClusterSize();

    }

    private void convertFile() {
        JavaRDD<String> text_file = sc.textFile(inputFileName);
        JavaRDD<String> convertedObject;
        convertedObject = text_file.map(line -> convertLine(line));
        if (this.clusterSize == -1)
            convertedObject = convertedObject.filter(line -> !line.equals("\r"));
        else
            convertedObject = convertedObject.filter(line -> !line.equals("\r")).coalesce(this.clusterSize);
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
        String str = "";
        int start = 0;
        BigInteger time;
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
                    time = new BigInteger(tempStr);
                    int c1 = time.compareTo(new BigInteger(startTime));
                    int c2 = time.compareTo(new BigInteger(endTime));
                    if (((c1==1)||(c2==0)) && ((c2==-1)||(c2==0))) {
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
                if (inTime)
                    return str;
            } else if (!filterTime && filterTickers) {
                if (inTicker)
                    return str;
            }
        }
        return "\r";
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
                debug("2015");
                this.ITAQSpecObject = new TAQ062015Spec();
                break;
            case "2016":
                this.ITAQSpecObject = new TAQ062016Spec();
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

}
