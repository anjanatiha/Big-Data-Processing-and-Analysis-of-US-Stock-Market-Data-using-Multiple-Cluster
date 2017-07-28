package FilePackage;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ReadFirstLine {

    public static String getFirstLineUnzipped(String inputFileName) {
        FileInputStream fstream = null;
        String firstLine = "";
        BufferedReader br = null;
        try {
            fstream = new FileInputStream(inputFileName);
            br = new BufferedReader(new InputStreamReader(fstream));
            firstLine = br.readLine();
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
        return firstLine;
    }

    public static String getFirstLineZip(String inputZipFileName) {
        String line = "";
        BufferedReader br = null;
        try {
            ZipFile zf = new ZipFile(inputZipFileName);
            Enumeration entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                long size = ze.getSize();
                if (size > 0) {
                    System.out.println("Length is " + size);
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    line = br.readLine();

                }
            }
            br.close();
        } catch (IOException e) {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();

        }
        return line;

    }}
