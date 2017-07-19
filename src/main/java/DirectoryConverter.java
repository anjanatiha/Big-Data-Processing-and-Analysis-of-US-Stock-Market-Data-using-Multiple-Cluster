import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.IOException;

import static Misc.Print.print;
import static Misc.Time.printElapsedTime;
import static Misc.Time.printTime;

public class DirectoryConverter {
    private String directory;
    private File folder;
    public static JavaSparkContext sc;
    DirectoryConverter(String[] args,JavaSparkContext sc){
        directory = args[2];
        folder = new File(directory);
        this.sc = sc;
        convertDirectory(sc, args);

    }
    public void convertDirectory(JavaSparkContext sc, String[] args){
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("Converting File : " + listOfFiles[i].getName());
                String inputFileName = directory+listOfFiles[i].getName();
                TAQConverterMainFN3 TAQAnalysisObject = new TAQConverterMainFN3(sc, args, inputFileName);
                print("Conversion completed of : "+ inputFileName);
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }
    public static void main(String[] args) throws IOException {
        SparkConf conf = new SparkConf().setAppName("Financial Data Processor").setMaster("local[2]").set("spark.executor.memory", "1g");
        JavaSparkContext sc = new JavaSparkContext(conf);
        long startTime = System.currentTimeMillis();
        printTime();
        DirectoryConverter TAQAnalysisObject = new DirectoryConverter(args, sc);
        printTime();
        long endTime = System.currentTimeMillis();
        printElapsedTime(startTime, endTime);
    }
}
