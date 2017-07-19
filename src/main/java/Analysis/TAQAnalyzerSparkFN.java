package Analysis; /**
 * Created by Anjana on 5/29/2017.
 */

import DataFieldType.IFieldType;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import static Misc.Debug.debug;
import static Misc.Print.print;


public class TAQAnalyzerSparkFN implements Serializable {
    private int startOffset;
    private IFieldType[] fieldType;
    private int recordLength;
    private String inputFileName;
    private File inputFile;
    private String outputFileName;
    private String startTime;
    private String endTime;
    private List<String> tickerSymbols;
    private int[] fieldset;
    private String fileType;
    private boolean tickerListExists=false;
    private boolean timeRange=false;


    TAQAnalyzerSparkFN(String inputFileName, String outputFileName, IFieldType[] fieldType, int startOffset) {
        setMainObjects(inputFileName, outputFileName,fieldType, startOffset);
        convertFile();
    }

    TAQAnalyzerSparkFN(String inputFileName, String outputFileName, IFieldType[] fieldType, int startOffset, List<String> tickerSymbols) {
        setMainObjects(inputFileName, outputFileName, fieldType, startOffset);
        this.tickerSymbols = tickerSymbols;
        this.tickerListExists=true;
        convertFile();

    }

    TAQAnalyzerSparkFN(String inputFileName, String outputFileName, IFieldType[] fieldType, String startTime, String endTime, int startOffset) {
        setMainObjects(inputFileName, outputFileName, fieldType, startOffset);
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeRange =true;
        convertFile();
    }

    TAQAnalyzerSparkFN(String inputFileName, String outputFileName, IFieldType[] fieldType, String startTime, String endTime, int startOffset, List<String> tickerSymbols) {
        setMainObjects(inputFileName, outputFileName, fieldType, startOffset);
        this.tickerSymbols =tickerSymbols;
        this.tickerListExists=true;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeRange =true;
        System.out.println(tickerSymbols);
        convertFile();
    }

    private void setMainObjects(String inputFileName, String outputFileName, IFieldType[] fieldType, int startOffset) {
        this.inputFileName = inputFileName;
        this.fieldType = fieldType;
        this.startOffset = startOffset;
        this.recordLength = getlength();
        this.outputFileName = outputFileName;
        this.outputFileName = outputFileName;
    }

    private void convertFile() {
        SparkConf conf = new SparkConf().setAppName("Financial Data Processor").setMaster("local[2]").set("spark.executor.memory", "1g");
        JavaSparkContext sc = new JavaSparkContext(conf);
        System.out.println("Input File: " +inputFileName);
        JavaRDD<String> text_file = sc.textFile(inputFileName);
        JavaRDD<String> convertedObject;
        convertedObject = text_file.map(line -> convertLine(line));
        convertedObject = convertedObject.filter(line -> !line.equals("\r"));

        print("Output File: "+ outputFileName);
        convertedObject.saveAsTextFile(outputFileName);
    }


    private String convertLine(String line) {
        String str = "";
        int start = 0;
        int time;
        boolean inTime = false;
        boolean inTicker = false;
        if (line.trim().length() < 15)
            return "\r";

        for (int i = 0; i < fieldType.length - 1; i++) {
            String tempStr = fieldType[i].convertFromBinary(line, start);
            if (timeRange==true) {
                if (i == 0) {
                    time = Integer.parseInt(tempStr);
                    if ((time >= Integer.parseInt(startTime)) && (time <= Integer.parseInt(endTime))) {
                        inTime = true;
                    } else
                        return "\r";
                }
            }
            if (tickerListExists==true) {
                if (i == 2) {
                    if (tickerSymbols.contains(tempStr)) {
                        inTicker = true;
                    } else
                        return "\r";
                }
            }
            str = str + tempStr;
            start = start + fieldType[i].getLength();
            if (i < fieldType.length - 2)
                str = str + ",";
        }
        str = str + "\n";
        if (!timeRange && !tickerListExists) {
            return str;
        } else {

            if (timeRange && tickerListExists) {
                if (inTime && inTicker) {
                    return str;
                }

            } else if (timeRange && !tickerListExists) {
                if (inTime)
                    return str;

            } else if (!timeRange && tickerListExists) {
                if (inTicker)
                    return str;
            }
        }
        return "\r";
    }

    public int getlength() {
        int recordLength = 0;
        for (int i = 0; i < fieldType.length; i++) {
            recordLength += fieldType[i].getLength();
        }
        return recordLength;
    }

    //work on
    private void convertFileZip() {

        SparkConf conf = new SparkConf().setAppName("Financial Data Processor").setMaster("local[2]").set("spark.executor.memory", "1g");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaPairRDD<String, PortableDataStream> text_file = sc.binaryFiles(inputFileName);
        JavaRDD<String> convertedObject = text_file.map(line -> convertLineZip(String.valueOf(line), fieldType));
        convertedObject.saveAsTextFile(outputFileName);

    }

    //work on
    private String convertLineZip(String line, IFieldType[] fieldType) {
        String str = "";
        int start = 0;
        debug(line);
        if (line.trim().length() < 15)
            return "";
        for (int i = 0; i < fieldType.length - 1; i++) {
            String tempStr = fieldType[i].convertFromBinary(line, start);
            str = str + tempStr;
            start = start + fieldType[i].getLength();
            if (i < fieldType.length - 2)
                str = str + ",";
        }
        str = str + "\n";
        return str;
    }
}
