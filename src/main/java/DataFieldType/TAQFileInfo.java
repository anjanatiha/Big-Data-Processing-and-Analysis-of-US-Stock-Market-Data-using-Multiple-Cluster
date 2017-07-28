package DataFieldType;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static FilePackage.FileName.getInputFileType;
import static Misc.Print.print;

public class TAQFileInfo {
    public static String getDateUnzip(String inputFileName) {
        FileInputStream fstream = null;
        String date = "";
        BufferedReader br = null;
        try {
            fstream = new FileInputStream(inputFileName);
            br = new BufferedReader(new InputStreamReader(fstream));
            String line = br.readLine();
            date = line.substring(2, 10);
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
        print(date);
        return date;

    }

    public static String getDate(String inputZipFileName) {
        String date = "";
        BufferedReader br = null;
        try {
            ZipFile zf = new ZipFile(inputZipFileName);
            Enumeration entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                print(ze.getName());
                long size = ze.getSize();
                if (size > 0) {
                    System.out.println("Length is " + size);
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line = br.readLine();
                    date = line.substring(2, 10);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (br != null)
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

        }
        return date;
    }

    public static String extractYear(String inputFileName) {
        String date = "";
        if (getInputFileType(inputFileName).equals("zip"))
            date = getDate(inputFileName);
        else
            date = getDateUnzip(inputFileName);
        String year = date.substring(4, 8);
        String month = date.substring(2, 4);
        int dateNumber = Integer.parseInt(year + month);
        return analyzeYear(dateNumber);
    }

    public static String analyzeYear(int date) {
        if (date >= 201006 && date <= 201207)
            return "2010";
        if (date >= 201208 && date <= 201302)
            return "2012";
        if (date >= 201305 && date <= 201312)
            return "2013";
        if (date >= 201506 && date <= 201607)
            return "2015";
        if (date >= 201607 && date <= 201707)
            return "2016";
        else
            return "None";
    }

    public static String getFileType(String directory) {
        if (directory.toLowerCase().contains("nbbo"))
            return "nbbo";
        else if (directory.toLowerCase().contains("quote"))
            return "quote";
        else if (directory.toLowerCase().contains("trade"))
            return "trade";
        else return "None";
    }
}
