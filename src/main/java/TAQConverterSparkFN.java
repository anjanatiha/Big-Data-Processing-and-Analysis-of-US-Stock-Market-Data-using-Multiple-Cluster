/**
 * Created by Anjana on 5/29/2017.
 */

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static Misc.Debug.debug;


public class TAQConverterSparkFN implements Serializable {
    private int startOffset;
    private IFieldType[] fieldType;
    private int recordLength;
    private String inputFileName;
    private File inputFile;
    private String outputFileName;
    private String startTime;
    private String endTime;
    private String[] ticker;
    private int[] fieldset;
    private String fileType;

    TAQConverterSparkFN(String inputFileName, IFieldType[] fieldType, int startOffset){
        setMainObjects(inputFileName, fieldType, startOffset );
        if (!fileType.trim().equals("zip")) {
            convertFile();
        }
        else {
            convertFileZip();
        }
    }
    TAQConverterSparkFN(String inputFileName, IFieldType[] fieldType, String startTime, String endTime, int startOffset){
        setMainObjects(inputFileName, fieldType, startOffset );
        this.startTime = startTime;
        this.endTime = endTime;
        convertFile();

    }
    public void setMainObjects(String inputFileName, IFieldType[] fieldType, int startOffset ){
        this.inputFileName = inputFileName;
        this.fieldType = fieldType;
        this.startOffset=startOffset;
        this.recordLength = getlength();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.outputFileName =inputFileName+"_SparkConverted_"+timeStamp+".txt";
        this.fileType = inputFileName.substring(inputFileName.length()-3,inputFileName.length());

    }

    public void convertFile(){
        SparkConf conf = new SparkConf().setAppName("SOME APP NAME").setMaster("local[2]").set("spark.executor.memory","1g");;
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> text_file = sc.textFile(inputFileName);
//        JavaRDD<String> convertedObject = text_file.map(line -> convertLine(line, fieldType));
        JavaRDD<String> convertedObject = text_file.map(line -> convertLineExtract(line, fieldType, startTime, endTime));

        convertedObject.saveAsTextFile (outputFileName);

    }
    public String convertLine(String line, IFieldType[] fieldType){
        String str = "";
        int start = 0;
        if (line.trim().length()<15)
            return "";
        for(int i=0; i<fieldType.length-1; i++) {
            String tempStr=fieldType[i].convertFromBinary(line, start);
            str = str+tempStr;
            start = start +fieldType[i].getLength();
            if (i < fieldType.length-2)
                str =str+",";
        }
        str = str+"\n";
        return str;
    }
    public String convertLineExtract(String line, IFieldType[] fieldType, String startTime, String endTime){
        String str = "";
        int start = 0;
        int time;
        int inRange = -1;
        if (line.trim().length()<15)
            return "";
        for(int i=0; i<fieldType.length-1; i++) {
            String tempStr=fieldType[i].convertFromBinary(line, start);
            if(startTime!=null & endTime!=null){
                if(i==0){
                    time = Integer.parseInt(tempStr);
                    if((time>=Integer.parseInt(startTime)) && (time<=Integer.parseInt(endTime))) {
                        inRange = 1;
                    }
                    else {
                        return "";
                    }
                }
            }
            str = str+tempStr;
            start = start +fieldType[i].getLength();
            if (i < fieldType.length-2)
                str =str+",";
        }
        str = str+"\n";

        if(startTime!=null & endTime!=null) {
            if (inRange == 1) {
                System.out.println(str);
                return str;
            }
            else {
                return "";
            }
        }
        else {
            return str;
        }

    }
    public int getlength(){
        int recordLength=0;
        for(int i=0;i<fieldType.length;i++){
            recordLength +=fieldType[i].getLength();
        }
        return recordLength;
    }

    //work on
    public void convertFileZip(){
        SparkConf conf = new SparkConf().setAppName("SOME APP NAME").setMaster("local[2]").set("spark.executor.memory","1g");;
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaPairRDD<String, PortableDataStream> text_file = sc.binaryFiles(inputFileName);
        debug(5);
        JavaRDD<String> convertedObject = text_file.map(line -> convertLineZip(String.valueOf(line), fieldType));
        convertedObject.saveAsTextFile (outputFileName);

    }
    //work on
    public String convertLineZip(String line, IFieldType[] fieldType){
        String str = "";
        int start = 0;
        if (line.trim().length()<15)
            return "";
        for(int i=0; i<fieldType.length-1; i++) {
            String tempStr=fieldType[i].convertFromBinary(line, start);
            str = str+tempStr;
            start = start +fieldType[i].getLength();
            if (i < fieldType.length-2)
                str =str+",";
        }
        str = str+"\n";
        return str;
    }
}
