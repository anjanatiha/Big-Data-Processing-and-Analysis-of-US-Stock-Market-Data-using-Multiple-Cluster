package FilePackage;

import java.io.File;

import static Misc.Print.print;

public class FileName {
    public static String getInputFileName(String inputFileName) {
        File inputFile = new File(inputFileName);
        return inputFile.getName();
    }

    public static String getFileTypeRemoved(String inputFileName) {
        String inputFileNameOnly = inputFileName.substring(0, inputFileName.length() - 4);
        return inputFileNameOnly;
    }

    public static String getUnzippedFileName(String inputFileName) {
        String unzippedFileName = "";
        if (getInputFileType(inputFileName).equals("zip"))
            unzippedFileName = getFileTypeRemoved(inputFileName) + "_Unzipped";
        else {
            print("Already Unzipped");
        }
        return unzippedFileName;

    }

    public static String getConvertedFileName(String inputFileName) {
        String outputFileName = "";
        if (getInputFileType(inputFileName).equals("zip"))
            outputFileName = getFileTypeRemoved(inputFileName) + "_Converted";
        return outputFileName;

    }

    public static String getOutputFileName(String inputFileName, String subStr) {
        String outputFileName;
        if (getInputFileType(inputFileName).equals("zip")) {

            outputFileName = getInputFileName(inputFileName) + "_unzipped";
        } else {
            outputFileName = inputFileName + "_" + subStr;
        }
        return outputFileName;
    }

    public static String getInputFileType(String inputFileName) {
        print(inputFileName);
        if (inputFileName.substring(inputFileName.length() - 3, inputFileName.length()).equals("zip")) {
            return "zip";
        } else
            return "txt";
    }
}
