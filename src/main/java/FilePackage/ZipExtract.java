package FilePackage;

import java.io.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static FilePackage.FileClass.readableFileSize;
import static Misc.Print.print;

public class ZipExtract {
    public static String unZip(String zipFile, String outputFileName) {
        print("zipFile : " + zipFile);
        String sizeStr = "";
        try {
            ZipFile zf = new ZipFile(zipFile);
            File outputFile = new File(outputFileName);
            if (outputFile.exists()) {
                System.out.println("File already unzipped\nDo you want to unzip again?");
                Scanner scan = new Scanner(System.in);
                String s = scan.next();
                if (s.equals("y"))
                    outputFile.delete();
                else
                    return "0";
            }
            Enumeration entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                long size = ze.getSize();
                sizeStr = readableFileSize(size);
                BufferedReader br = null;
                PrintWriter outputStream = new PrintWriter(outputFile);
                StringBuilder strBuilder = new StringBuilder();
                if (size > 0) {
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    int k = 1;
                    line = br.readLine();
                    print("Extracting zip file...");
                    while (line != null) {
                        strBuilder.append(line);
                        strBuilder.append("\r\n");
                        if (k % 1000000 == 0) {
                            outputStream.print(strBuilder);
                            outputStream.flush();
                            strBuilder.setLength(0);
                            print("On Line: " + k);
                            System.gc();
                        }
                        k++;
                        line = br.readLine();
                    }
                    outputStream.print(strBuilder);
                    outputStream.flush();
                    strBuilder.setLength(0);
                    print("Total Lines : " + k);
                    System.gc();

                }
                outputStream.close();
                br.close();

            }
            zf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
        return sizeStr;
    }

}
