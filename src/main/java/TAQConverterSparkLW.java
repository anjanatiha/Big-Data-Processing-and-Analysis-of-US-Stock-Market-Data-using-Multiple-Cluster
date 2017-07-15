/**
 * Created by Anjana on 5/29/2017.
 */

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TAQConverterSparkLW implements Serializable {
    private int startOffset;
    private IFieldType[] fieldType;
    private int recordLength;

//    private String inputFileName;
//    private File inputFile;
    private String inputFileName, outputFileName;
    private File inputFile, outputFile;
    PrintStream outputStream;

    TAQConverterSparkLW(String inputFileName, IFieldType[] fieldType, int startOffset){
        this.inputFileName = inputFileName;
        this.startOffset=startOffset;
        this.fieldType = fieldType;
        this.recordLength = getlength();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        outputFileName = inputFileName+"_converted_"+timeStamp+".txt";
//        outputFile= new File(outputFileName);
//        try {
//            outputStream = new PrintStream(outputFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }


    public void convertFile(){
        SparkConf conf = new SparkConf().setAppName("SOME APP NAME").setMaster("local[2]").set("spark.executor.memory","1g");;
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> text_file = sc.textFile(inputFileName);
//        List<String> convertedObject = text_file.map(line -> convertLine(line, fieldType)).collect();
        JavaRDD<String> convertedObject = text_file.map(line -> convertLine(line, fieldType));

        convertedObject.saveAsTextFile ("/home/anjana/Downloads/outputspark");
//        System.out.println("\n\najhsjhadjhajn\n\n");
//        for (String a:convertedObject) {
//            System.out.println(a);
//        }
    }
    public String convertLine(String line, IFieldType[] fieldType){
        String str = "";
        int start = 0;
        if (line.trim().length()<recordLength-2)
            return "";
        for(int i=0; i<fieldType.length-1; i++) {
            System.out.println();
            String tempStr=fieldType[i].convertFromBinary(line, start);
            str = str+tempStr;
            start = start +fieldType[i].getLength();
//            System.out.println("original -> " +line+ "length filed -> "+fieldType[i].getLength()+"\nfull -> "+ str+ " <- field ->"+tempStr+ "\nstart -> "+start+"||||||\n\n\n");
            if (i < fieldType.length-2)
                str =str+",";
        }
        str = str+"\n";
        return str;
    }
    public int getlength(){
        int recordLength=0;
        for(int i=0;i<fieldType.length;i++){
            recordLength +=fieldType[i].getLength();
        }
        return recordLength;
    }
//    public void writeFile(List<String> convertedObject){
//
//        try {
//            outputStream = new PrintStream(outputFile);
//            outputStream = new PrintStream(outputFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        for (String str:convertedObject) {
//            outputStream.print(str);
//            outputStream.flush();
//        }
//        outputStream.close();
//    }

}
