import DataFieldType.*;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

import static Misc.FileClass.deleteFileorDir;
import static Misc.FileClass.unZip;
import static Misc.FileProperties.*;
import static Misc.Print.print;
import static Misc.Time.printElapsedTime;


public class TAQConverterSingle {
    private TAQConverter TAQConverterSingleObject;
    private String inputFileName;
    private String outputFileName;
    private ITAQSpec ITAQSpecObject;
    private IFieldType[] fieldTypes;
    private List<String> tickerSymbols;
    private String startTime = "";
    private String endTime = "";
    private String TAQFileType = "";
    private String inputFileType = "";
    private boolean tickerFilter = false;
    private boolean timeFilter = false;
    private String fileYear;
    private int start = 0;
    public static JavaSparkContext sc;
    private String sizeStr = "";
    private List<Integer> columnList= new ArrayList<>();



    TAQConverterSingle(JavaSparkContext sc, String[] args, String inputFileName) {
        this.TAQFileType = args[0];
        this.inputFileName = inputFileName;
        this.fileYear = extractYear(inputFileName);
        if (!args[3].equals("n")) {
            this.timeFilter = true;
            this.startTime = args[3];
            this.endTime = args[4];
            print("Start Time: "+startTime+ " End Time : " +endTime);
        }
        if (!args[5].equals("n")) {
            this.tickerSymbols = wordCollect(sc, args[5]);
            this.tickerFilter = true;
        }
        if (!args[6].equals("n")) {
            this.columnList = columnSelect(sc, args[6]);
        }
        this.start = 1;
        this.inputFileType = getInputFileType(inputFileName);
        this.outputFileName = getOutputFileName(inputFileName);
        String outputFileName_unzip = outputFileName;
        this.sc = sc;
        print("fileYear: " + fileYear);
        switch (fileYear) {
            case "2010":
                ITAQSpecObject = new TAQ102010Spec();
                break;
            case "2012":
                ITAQSpecObject = new TAQ072012Spec();
                break;
            case "2013":
                ITAQSpecObject = new TAQ082013Spec();
                break;
            case "2015":
                ITAQSpecObject = new TAQ062015Spec();
                break;
            case "2016":
                ITAQSpecObject = new TAQ062016Spec();
                break;
        }
        switch (TAQFileType) {
            case "trade":
                fieldTypes = ITAQSpecObject.getTradeFields();
                break;
            case "nbbo":
                fieldTypes = ITAQSpecObject.getNBBOFields();
                break;
            case "quote":
                fieldTypes = ITAQSpecObject.getQuoteFields();
                break;
        }

        if (inputFileType.equals("zip")) {
            print("Unzipping Started for + " + inputFileName);
            long startTime = System.currentTimeMillis();
            this.sizeStr = unZip(inputFileName, outputFileName);
            print("Unzipping Completed for + " + inputFileName);
            long endTime = System.currentTimeMillis();
            printElapsedTime(startTime, endTime, " unzipping ");
            inputFileName = outputFileName;
            outputFileName = getOutputFileName(inputFileName);
        }
        if (!timeFilter) {
            if (!tickerFilter)
                TAQConverterSingleObject = new TAQConverter(sc, inputFileName, outputFileName, fieldTypes, start, columnList);
            else
                TAQConverterSingleObject = new TAQConverter(sc, inputFileName, outputFileName, fieldTypes, tickerSymbols, start, columnList);
        } else {
            if (!tickerFilter)
                TAQConverterSingleObject = new TAQConverter(sc, inputFileName, outputFileName, fieldTypes, startTime, endTime, start, columnList);
            else
                TAQConverterSingleObject = new TAQConverter(sc, inputFileName, outputFileName, fieldTypes, startTime, endTime, tickerSymbols, start, columnList);
        }
        deleteFileorDir(outputFileName_unzip);

    }

    public String getSize() {
        return sizeStr;
    }

}