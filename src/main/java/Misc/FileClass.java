package Misc;

import java.io.*;

import static Misc.Debug.debug;
import static Misc.Print.print;

public class FileClass {
    private static String inputFileName;

    private int count;

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
                        if (k%bufferSize==0)
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


    public static void printFile(int start, int end) throws IOException {
        File inputFile = new File(inputFileName);
        byte[] inputBuffer = new byte[end + 1];
        InputStream inputStream = new FileInputStream(inputFile);
        int bytesRead = inputStream.read(inputBuffer);
        String str = "";
        for (int i = start; i <= end; i++) {
            System.out.println(inputBuffer[i]);
        }
        System.out.println(str);

    }

    public static void printFileLine(int lineCount) throws IOException {
        FileInputStream fstream = new FileInputStream(inputFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        int k = 0;
        StringBuilder str = new StringBuilder();
        while ((strLine = br.readLine()) != null) {
            if (k > 0) {
                System.out.println(strLine);
                str.append(strLine + "\n");
                lineCount--;
                if (lineCount < 0)
                    break;
            }
            k++;
        }

        br.close();

    }

}
