package Misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileProperties {
    public static String getOutputFileName(String inputFileName){
        String outputFileName;
        if (inputFileName.substring(inputFileName.length()-3,inputFileName.length()).equals("zip")) {
            outputFileName = inputFileName.substring(0,inputFileName.length()-4) + "_extracted";
        } else {
            outputFileName = inputFileName + "_converted";
        }
        return outputFileName;
    }
    public static String getInputFileType(String inputFileName) {
        if (inputFileName.substring(inputFileName.length() - 3, inputFileName.length()).equals("zip")){
            return "zip";
        }else
            return "txt";
    }
    public static String getDate(String inputZipFileName){
        String date = "";
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
                    String line = br.readLine();
                    date = line.substring(2,10);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            if(br!=null)
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

        }
        return date;
    }
    public static String extractYear(String inputFileName){
        String date = getDate(inputFileName);
        String year = date.substring(4,8);
        String month = date.substring(2,4);
        int dateNumber = Integer.parseInt(year+month);
        return analyzeYear(dateNumber);
    }
    public static String analyzeYear(int date){
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
}
