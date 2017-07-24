/**
 * Created by Anjana on 5/29/2017.
 */

import DataFieldType.IFieldType;
import org.apache.commons.io.FileUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static Misc.Debug.debug;
import static Misc.FileClass.deleteFileorDir;
import static Misc.Print.*;

public class TAQConverter implements Serializable {
    private String inputFileName;
    private String outputFileName;
    private IFieldType[] fieldType;
    private String startTime;
    private String endTime;
    private List<String> tickerSymbols;
    private boolean tickerListExists = false;
    private boolean timeRange = false;
    private int startOffset;
    private int recordLength;
    private static JavaSparkContext sc;
    private boolean columnListExists = false;
    private List<Integer> columnList =new ArrayList<>();

    TAQConverter(JavaSparkContext sc, String inputFileName, String outputFileName, IFieldType[] fieldType, int startOffset, List<Integer> columnSelectList) {
        setMainObjects(sc, inputFileName, outputFileName, fieldType, startOffset, columnList);
        convertFile();
    }

    TAQConverter(JavaSparkContext sc, String inputFileName, String outputFileName, IFieldType[] fieldType, List<String> tickerSymbols, int startOffset, List<Integer> columnSelectList) {
        setMainObjects(sc, inputFileName, outputFileName, fieldType, startOffset,  columnList);
        this.tickerSymbols = tickerSymbols;
        this.tickerListExists = true;
        convertFile();
    }

    TAQConverter(JavaSparkContext sc, String inputFileName, String outputFileName, IFieldType[] fieldType, String startTime, String endTime, int startOffset, List<Integer> columnSelectList) {
        setMainObjects(sc, inputFileName, outputFileName, fieldType, startOffset, columnList);
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeRange = true;
        convertFile();
    }

    TAQConverter(JavaSparkContext sc, String inputFileName, String outputFileName, IFieldType[] fieldType, String startTime, String endTime, List<String> tickerSymbols, int startOffset, List<Integer> columnSelectList) {
        setMainObjects(sc, inputFileName, outputFileName, fieldType, startOffset,  columnList);
        this.timeRange = true;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tickerListExists = true;
        this.tickerSymbols = tickerSymbols;
        convertFile();
    }

    private void setMainObjects(JavaSparkContext sc, String inputFileName, String outputFileName, IFieldType[] fieldType, int startOffset, List<Integer> columnList) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.fieldType = fieldType;
        this.startOffset = startOffset;
        this.recordLength = getlength();
        this.columnList = columnList;
        this.sc = sc;
    }

    private void convertFile() {
        JavaRDD<String> text_file = sc.textFile(inputFileName);
        JavaRDD<String> convertedObject;
        convertedObject = text_file.map(line -> convertLine(line));
        convertedObject = convertedObject.filter(line -> !line.equals("\r"));
        Path path = Paths.get(outputFileName);
        if (Files.exists(path)) {
            try {
                FileUtils.deleteDirectory(new File((outputFileName + "/")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            if (timeRange == true) {
                if (i == 0) {
                    time = Integer.parseInt(tempStr);
                    if ((time >= Integer.parseInt(startTime)) && (time <= Integer.parseInt(endTime))) {
                        inTime = true;
                    } else
                        return "\r";
                }
            }
            if (tickerListExists == true) {
                if (i == 2) {
                    if (tickerSymbols.contains(tempStr)) {
                        inTicker = true;
                    } else
                        return "\r";
                }
            }
            if(columnList.isEmpty()){
                str = str + tempStr;
            }
            else if (!columnList.isEmpty()){
                print("aewr");
                if (columnList.contains(i+1)){
                    str = str + tempStr;
                }
            }


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
        JavaPairRDD<String, PortableDataStream> text_file = sc.binaryFiles(inputFileName);
        JavaRDD<String> convertedObject = text_file.map(line -> convertLineZip(String.valueOf(line), fieldType));
        if (new File(outputFileName).exists())
            deleteFileorDir(outputFileName);
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
