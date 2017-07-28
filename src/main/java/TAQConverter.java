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
import java.util.Collections;
import java.util.List;

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
    private TAQAttributes TAQAttributesObject=null;
    public IFieldType[] fieldTypes;
    public ITAQSpec ITAQSpecObject;
    private String startTime;
    private String endTime;
    private List<String> tickerSymbols;
    private List<Integer> columnList;
    private boolean filterTickers = false;
    private boolean filterTime = false;
    private boolean filterColumns = false;
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
            this.inputFileUnzippedAbsolutePath = unzipDirectoryName+"/"+inputFileNameUnzipped;
            this.sizeStr = unZip(inputFileAbsolutePath, inputFileUnzippedAbsolutePath);
            print("Unzipping Completed for + " + inputFileName + " Size : " +this.sizeStr);
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
    public void setFileProperty(){
        this.inputFileName = getInputFileName(inputFileAbsolutePath);
//        print(" this.inputFileName: "+ this.inputFileName);
        this.inputFileType = getInputFileType(inputFileName);
//        print(" this.inputFileType: "+ this.inputFileType);
        this.inputDirectoryName = getParentDir(inputFileAbsolutePath);
//        print(" this.inputDirectoryName: "+ this.inputDirectoryName);
        this.TAQFileType = getFileType(inputFileAbsolutePath);
//        print(" this.TAQFileType: "+ this.TAQFileType);
        this.fileYear = extractYear(inputFileAbsolutePath);
//        print(" this.fileYear: "+ this.fileYear);
        this.unzipDirectoryAbsolutePath = TAQAttributesObject.getUnzipDirectoryAbsolutePath();
//        print(" this.unzipDirectoryAbsolutePath: "+ this.unzipDirectoryAbsolutePath);
        this.batchConversion = TAQAttributesObject.getBatchConversion();
//        print(" this.batchConversion: "+ this.batchConversion);
        this.outputDirectoryName = TAQAttributesObject.getOutputDirectoryAbsolutePath();
//        print(" this.outputDirectoryName: "+ this.outputDirectoryName);
        this.unzipDirectoryName = TAQAttributesObject.getUnzipDirectoryAbsolutePath();
//        print(" this.unzipDirectoryName: "+ this.unzipDirectoryName);
        this.outputFileName = getConvertedFileName(inputFileName);
//        print(" this.outputFileName: "+ this.outputFileName);
        this.OutputFileAbsolutePath = this.outputDirectoryName+"/"+outputFileName;
//        print(" this.OutputFileAbsolutePath: "+ this.OutputFileAbsolutePath);
    }
    public void setTAQConverterObject(){
        this.startTime = TAQAttributesObject.getStartTime();
        this.endTime = TAQAttributesObject.getEndTime();
        this.filterTime = TAQAttributesObject.getfilterTime();
        this.tickerSymbols = TAQAttributesObject.getTickerSymbols();
        this.columnList = TAQAttributesObject.getColumnList();
        this.filterTickers = TAQAttributesObject.getFilterTickers();
        this.filterColumns = TAQAttributesObject.getFilterColumn();
        this.clusterSize = TAQAttributesObject.getClusterSize();

    }

    private void convertFile() {

        JavaRDD<String> text_file = sc.textFile(finalInputFileAbsolutePath);
        JavaRDD<String> convertedObject;

        convertedObject = text_file.map(line -> convertLine(line));

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
        print("OutputFileAbsolutePath :"+OutputFileAbsolutePath);

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
        }
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
        if(str.trim().length()<getlength()/5)
            return "\r";
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
