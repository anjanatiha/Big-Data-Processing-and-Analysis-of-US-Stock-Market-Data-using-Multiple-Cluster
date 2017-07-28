package FilePackage;

import java.io.*;

import static Misc.Debug.debug;
import static Misc.Print.print;

public class FilePrintWriteLine {
    public static void printFileLine(String inputFileName, int lineCount) {
        FileInputStream fstream = null;
        BufferedReader br = null;
        try {
            fstream = new FileInputStream(inputFileName);
            br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            int k = 0;
            StringBuilder str = new StringBuilder();
            while ((strLine = br.readLine()) != null) {
                if (k >= 0) {
                    System.out.println(strLine);
                    str.append(strLine + "\n");
                    lineCount--;
                    if (lineCount < 0)
                        break;
                }
                k++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }
    public static void FileSampleWrite(String inputFileName, String count) {
        FileInputStream inputStream;
        String outputFileName;
        File outputFile;
        PrintStream outputStream;
        BufferedReader br;
        try {
            inputStream = new FileInputStream(inputFileName);
            outputFileName = inputFileName + "_sample";
            outputFile = new File(outputFileName);
            outputStream = new PrintStream(outputFile);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            int lastindex = Integer.parseInt(count);
            int k = 0;
            StringBuilder stringBuilder = new StringBuilder();
            int bufferSize = 100000;
            while ((strLine = br.readLine()) != null) {

                if (k > lastindex)
                    break;
                else {
                    stringBuilder.append(strLine);
                    if (k < lastindex)
                        stringBuilder.append('\n');
                    k++;
                    if (k > bufferSize) {
                        outputStream.print(stringBuilder);
                        outputStream.flush();
                        stringBuilder.setLength(0);
                        if (k % bufferSize == 0)
                            print(k);
                    }
                }
            }
            outputStream.print(stringBuilder);
            outputStream.flush();
            stringBuilder.setLength(0);
            inputStream.close();
            br.close();
            outputStream.close();
            debug("end");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
