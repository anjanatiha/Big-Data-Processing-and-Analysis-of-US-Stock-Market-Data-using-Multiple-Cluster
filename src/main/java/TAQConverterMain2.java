import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Misc.Print.print;
import static Misc.Time.printElapsedTime;

//argument 1 : file or direcotory location of file to convert( /home/user/Downloads/file.zip)
//argument 2 : start time (please follow number of digits for each file spec for 9 digit 090000000)
//argument 3 : end time please follow number of digits for each file spec for 9 digit 093000000)
//argument 4 : file location of file containing stock symbols seperated by space, comma( /home/user/Downloads/stock).
//argument 5 : type file location containing column index seperated by space, comma(/home/user/Downloads/columns).
//argument 6 : cluster size(leave with -1 to as cluster size is set automatically based on available worker nodes)

public class TAQConverterMain2 {
    private String fileOrDirectoryName;
    private File fileOrDirectory;
    public static JavaSparkContext sc;
    private FileAttributes fileAttributesObject;

    TAQConverterMain2(String[] args, JavaSparkContext sc, FileAttributes fileAttributesObject) {
        fileOrDirectoryName = args[0];
        fileOrDirectory = new File(fileOrDirectoryName);
        this.sc = sc;
        int fileOrDirectoryNameLength = fileOrDirectoryName.length();
        this.fileAttributesObject = fileAttributesObject;

        if (fileOrDirectoryName.substring(fileOrDirectoryNameLength - 1, fileOrDirectoryNameLength).equals("/"))
            convertDirectory(sc, args);
        else
            convertSingleFile(sc, args);

    }

    public void convertDirectory(JavaSparkContext sc, String[] args) {

        File directory = new File(fileOrDirectoryName);

        File[] listOfFiles = directory.listFiles();
        List<String> strTimeList = new ArrayList<>();
        int i;
        for (i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                long startTime = System.currentTimeMillis();
                System.out.println("Converting File : " + listOfFiles[i].getName());
                String inputFileName = fileOrDirectoryName + listOfFiles[i].getName();
                TAQConverter2 TAQConverterObject = new TAQConverter2(sc, args, inputFileName, fileAttributesObject);
                long endTime = System.currentTimeMillis();
                print("Conversion completed for file : " + inputFileName);
                String tempStr = printElapsedTime(startTime, endTime, "converting single file");
//                tempStr = "Total Size is : " + TAQConverterSingleObject.getSize() + "\nConvertion Time : " + tempStr;
//                strTimeList.add(tempStr);

            }
        }
        for (String p : strTimeList)
            System.out.println(p);
    }

    private void convertSingleFile(JavaSparkContext sc, String[] args) {
        String inputFileName = args[0];
        long startTime = System.currentTimeMillis();
        TAQConverter2 TAQConverterObject = new TAQConverter2(sc, args, inputFileName, fileAttributesObject);
        long endTime = System.currentTimeMillis();
        print("Conversion completed for file : " + inputFileName);
        String tempStr = printElapsedTime(startTime, endTime, "converting single file");
//        tempStr = "Total Size is : " + TAQConverterObject.getSize() + "\nConvertsion Time : " + tempStr;
//        print(tempStr);
    }

    public static void main(String[] args) throws IOException {
        FileAttributes fileAttributesObject = new FileAttributes();
        SparkConf conf = new SparkConf().setAppName("Financial Data Processor").setMaster("local[2]").set("spark.executor.memory", fileAttributesObject.getMemorySize());
        JavaSparkContext sc = new JavaSparkContext(conf);
        long startTime = System.currentTimeMillis();
        TAQConverterMain2 TAQConverterMainObject = new TAQConverterMain2(args, sc, fileAttributesObject);
        long endTime = System.currentTimeMillis();
        printElapsedTime(startTime, endTime, "complete conversion");
        System.gc();
    }

}
