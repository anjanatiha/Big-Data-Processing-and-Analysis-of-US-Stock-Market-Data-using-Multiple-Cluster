import java.io.File;
import java.io.IOException;

import static Misc.FileClass.getYear;
import static Misc.Time.printElapsedTime;
import static Misc.Time.printTime;
public class DirectoryConverter {
    DirectoryConverter(String[] args){
        String directory = args[2];
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                String fileYear = getYear(directory+listOfFiles[i].getName());
                TAQConverterMainFN3 TAQAnalysisObject = new TAQConverterMainFN3(args, fileYear);
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        printTime();
        DirectoryConverter TAQAnalysisObject = new DirectoryConverter(args);
        printTime();
        long endTime = System.currentTimeMillis();
        printElapsedTime(startTime, endTime);
    }
}
