import DataFieldType.*;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;

import static DataFieldType.TickerSymbols.getTickers;
import static Misc.FileClass.*;
import static Misc.FileProperties.*;
import static Misc.Print.print;


public class TAQConverterMainFN3 {
    private TAQConverterSparkFN2 TAQConverterSparkFNObject;
    private String inputFileName;
    private String outputFileName;
    private ITAQSpec ITAQSpecObject;
    private IFieldType[] fieldTypes;
    private List<String> tickerSymbols;
    private String startTime="";
    private String endTime="";
    private String TAQFileType="";
    private String inputFileType="";
    private boolean tickerFilter=false;
    private boolean timeFilter=false;
    private String fileYear;
    private int start=0;
    public static JavaSparkContext sc;


    TAQConverterMainFN3(JavaSparkContext sc, String[] args, String inputFileName) {
        this.TAQFileType = args[0];
        this.inputFileName = inputFileName;
        this.fileYear = extractYear(inputFileName);
        if (!args[3].equals("n")) {
            this.timeFilter = true;
            this.startTime = args[4];
            this.endTime = args[5];
        }
        if (!args[5].equals("n")){
            this.tickerSymbols = getTickers();
            this.tickerFilter=true;
        }
        this.start = 1;
        this.inputFileType = getInputFileType(inputFileName);
        this.outputFileName = getOutputFileName(inputFileName);
        this.sc = sc;
        print("fileYear: "+fileYear);
        switch (fileYear){
            case "2010":
                print("hi 2010 "+ inputFileName);
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
        if(inputFileType.equals("zip")) {
            unZip(inputFileName, outputFileName);
            print("unzipping complete");
            inputFileName = outputFileName;
            outputFileName = getOutputFileName(inputFileName);
        }
        if (!timeFilter) {
            if(!tickerFilter)
                TAQConverterSparkFNObject = new TAQConverterSparkFN2(sc, inputFileName, outputFileName, fieldTypes, start);
            else
                TAQConverterSparkFNObject = new TAQConverterSparkFN2(sc, inputFileName, outputFileName, fieldTypes, tickerSymbols, start);
        }
        else
        {
            if (!tickerFilter)
                TAQConverterSparkFNObject = new TAQConverterSparkFN2(sc, inputFileName, outputFileName, fieldTypes, startTime, endTime, start);
            else
                TAQConverterSparkFNObject = new TAQConverterSparkFN2(sc, inputFileName, outputFileName, fieldTypes,startTime,endTime, tickerSymbols, start);
        }

    }

}