package Misc;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static Misc.Print.print;

public class FileProperties {
    public static String getOutputFileName(String inputFileName) {
        String outputFileName;
        if (inputFileName.substring(inputFileName.length() - 3, inputFileName.length()).equals("zip")) {
            outputFileName = inputFileName.substring(0, inputFileName.length() - 4) + "_extracted";
        } else {
            outputFileName = inputFileName + "_converted";
        }
        return outputFileName;
    }

    public static String getInputFileType(String inputFileName) {
        if (inputFileName.substring(inputFileName.length() - 3, inputFileName.length()).equals("zip")) {
            return "zip";
        } else
            return "txt";
    }

    public static String getDate(String inputZipFileName) {
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
        String date = getDate(inputFileName);
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
    public static List<String> wordCollect (JavaSparkContext sc, String inputFileName){
        JavaRDD<String> textFile = sc.textFile(inputFileName);
        List<String[]> wordListArr = textFile.map(e -> e.split("[\\s,;.]+")).collect();
        List<String> wordList = new ArrayList<>();

        for (String[] word: wordListArr) {
            for (int i=0; i<word.length;i++){
                wordList.add(word[i]);
            }
        }
        int k=0;
        for (String word: wordList) {
            k++;
        }
        print("Number of Tickers selected : " +k);
        print("Finding the following Tickers");
        for (String word: wordList) {
            System.out.print(word + " ");

        }
        print("\n");
        return wordList;
    }

    public static List<Integer> columnSelect(JavaSparkContext sc, String inputFileName){
        JavaRDD<String> textFile = sc.textFile(inputFileName);
        List<String[]> columnListArr = textFile.map(e -> e.split("[\\s,;.]+")).collect();
        List<Integer> columnList = new ArrayList<>();

        for (String[] col: columnListArr) {
            for (int i=0; i<col.length;i++){
                try{
                    columnList.add(Integer.parseInt(col[i]));
                }catch(Exception e ){
                    System.out.println(e);
                }
            }
        }
        int k=0;
        for (int column: columnList) {
            k++;
        }
        print("Number of columns selected : " +k);
        print("Finding the following columns");
        for (int column: columnList) {
            System.out.print(column + " ");

        }
        print("\n");
        return columnList;
    }
}
