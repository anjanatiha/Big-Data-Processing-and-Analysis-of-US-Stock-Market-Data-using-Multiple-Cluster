import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Misc.Print.print;
import static Misc.Time.printElapsedTime;


//argument 1= filetype (taq file type trade, nbbo or quote)
//argument 2= type "n"
//argument 3= file or direcotory location
//argument 4= start time
//argument 5= end time
//argument 6= stock symbols

public class TAQConverter {
    private String fileOrDirectoryName;
    private File fileOrDirectory;
    public static JavaSparkContext sc;
    TAQConverter(String[] args,JavaSparkContext sc){
        fileOrDirectoryName = args[2];
        fileOrDirectory = new File(fileOrDirectoryName);
        this.sc = sc;
        int fileOrDirectoryNameLengh = fileOrDirectoryName.length();
        if (fileOrDirectoryName.substring(fileOrDirectoryNameLengh-1, fileOrDirectoryNameLengh).equals("/"))
            convertDirectory(sc, args);
        else
            convertSingleFile(sc, args);

    }
    public void convertDirectory(JavaSparkContext sc, String[] args){
        File[] listOfFiles = fileOrDirectory.listFiles();
        List<String> strTimeList = new ArrayList<String>();
        int i=0;
        for (i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                long startTime = System.currentTimeMillis();
                System.out.println("Converting File : " + listOfFiles[i].getName());
                String inputFileName = fileOrDirectoryName+listOfFiles[i].getName();
                TAQConverterSingle TAQConverterSingleObject = new TAQConverterSingle(sc, args, inputFileName);
                long endTime = System.currentTimeMillis();
                print("Conversion completed for file : "+ inputFileName);
                strTimeList.add(printElapsedTime(startTime, endTime , "converting single file"));

            }
            for (String p : strTimeList)
                System.out.println(p);
        }
    }

    public void convertSingleFile(JavaSparkContext sc, String[] args){
        String inputFileName = args[2];
        TAQConverterSingle TAQConverterSingleObject = new TAQConverterSingle(sc, args, inputFileName);
    }
    public static void main(String[] args) throws IOException {
        SparkConf conf = new SparkConf().setAppName("Financial Data Processor").setMaster("local[2]").set("spark.executor.memory", "1g");
        JavaSparkContext sc = new JavaSparkContext(conf);
        long startTime = System.currentTimeMillis();
        TAQConverter DirectoryConverterObject = new TAQConverter(args, sc);
        long endTime = System.currentTimeMillis();
        printElapsedTime(startTime, endTime, "complete conversion");
    }
}
