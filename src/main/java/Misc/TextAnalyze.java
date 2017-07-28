package Misc;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static FilePackage.FileClass.isDirectory;
import static Misc.Print.print;

public class TextAnalyze {
    public static List<String> wordCollect(JavaSparkContext sc, String inputFileName) {
        JavaRDD<String> textFile = sc.textFile(inputFileName);
        List<String[]> wordListArr = textFile.map(e -> e.split("[\\s,;.]+")).collect();
        List<String> wordList = new ArrayList<>();

        for (String[] word : wordListArr) {
            for (int i = 0; i < word.length; i++) {
                wordList.add(word[i]);
            }
        }
        int k = 0;
        for (String word : wordList) {
            k++;
        }
        print("Number of Tickers selected : " + k);
        print("Finding the following Tickers");
        for (String word : wordList) {
            System.out.print(word + " ");

        }
        print("\n");
        return wordList;
    }

    public static List<Integer> columnSelect(JavaSparkContext sc, String inputFileName) {
        JavaRDD<String> textFile = sc.textFile(inputFileName);
        List<String[]> columnListArr = textFile.map(e -> e.split("[\\s,;.]+")).collect();
        List<Integer> columnList = new ArrayList<>();

        for (String[] col : columnListArr) {
            for (int i = 0; i < col.length; i++) {
                try {
                    columnList.add(Integer.parseInt(col[i]));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        int k = 0;
        for (int column : columnList) {
            k++;
        }
        print("Number of columns selected : " + k);
        print("Finding the following columns");
        for (int column : columnList) {
            System.out.print(column + " ");

        }
        print("\n");
        return columnList;
    }
    public static List<String> wordCollect(String wordListStr) {
        List<String> wordList = new ArrayList<>(Arrays.asList(wordListStr.split("[\\s,;.]+")));
        print("Number of Tickers selected : " + wordList.size());
        print("Finding the following Tickers");
        for (String word : wordList) {
            System.out.print(word + " ");

        }
        print("\n");
        return wordList;
    }
    public static List<Integer> columnSelect(String columns) {
        List<String> columnListStr = new ArrayList<>(Arrays.asList(columns.split("[\\s,;.]+")));
        List<Integer> columnList = new ArrayList<>();

        for (String column : columnListStr) {
            columnList.add(Integer.valueOf(column));
        }
        print("Number of columns selected : " + columnList.size());
        print("Finding the following columns");
        for (int column : columnList) {
            System.out.print(column + " ");

        }
        print("\n");
        return columnList;
    }
    public static void main(String[] args){
        boolean a =isDirectory("/home/anjana/Downloads/DATA/bulk/");
        print(String.valueOf(a));
    }

}
