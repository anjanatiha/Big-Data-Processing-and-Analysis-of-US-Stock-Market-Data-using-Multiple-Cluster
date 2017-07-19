import DataFieldType.*;
import Misc.UnZip;

import java.io.IOException;
import java.util.List;

import static DataFieldType.TickerSymbols.getTickers;
import static Misc.Print.print;
import static Misc.Time.printElapsedTime;
import static Misc.Time.printTime;


public class TAQConverterMainFN2 {
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
    private int fileYear;
    private int start=0;

    public String getOutputFileName(String inputFileName){
        String outputFileName;
        if (inputFileName.substring(inputFileName.length()-3,inputFileName.length()).equals("zip")) {
            outputFileName = inputFileName.substring(0,inputFileName.length()-4) + "_extracted";
        } else {
            outputFileName = inputFileName + "_converted";
        }
        return outputFileName;
    }
    public String inputFileType(String inputFileName){
        if(inputFileName.substring(inputFileName.length()-3, inputFileName.length())=="zip")
            return "zip";
        else
            return "txt";
    }
    TAQConverterMainFN2(String[] args) {
        this.TAQFileType = args[0];
        this.fileYear = Integer.parseInt(args[1]);
        this.inputFileName = args[2];
        if (!args[4].equals("n"))
            this.timeFilter = true;
            this.startTime = args[4];
            this.endTime = args[5];
        if (!args[6].equals("n"))
            this.tickerFilter=true;
        this.start = 1;
        this.tickerSymbols = getTickers();
        this.outputFileName = getOutputFileName(inputFileName);
        switch (fileYear){
            case 2010:
                ITAQSpecObject = new TAQ102010Spec();
            case 2012:
                ITAQSpecObject = new TAQ072012Spec();
            case 2013:
                ITAQSpecObject = new TAQ082013Spec();
            case 2015:
                ITAQSpecObject = new TAQ062015Spec();
            case 2016:
                ITAQSpecObject = new TAQ062016Spec();
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
        if(inputFileType.equals("zip")){
            UnZip unZip = new UnZip();
            unZip.unZipIt(inputFileName, outputFileName);
            print("unzipping complete");
            inputFileName = outputFileName;
            outputFileName = getOutputFileName(inputFileName);
        if (!timeFilter) {
            if(!tickerFilter)
                TAQConverterSparkFNObject = new TAQConverterSparkFN2(inputFileName, outputFileName, fieldTypes, start);
            else
                TAQConverterSparkFNObject = new TAQConverterSparkFN2(inputFileName, outputFileName, fieldTypes, tickerSymbols, start);
        }
        else
        {
            if (!tickerFilter)
                TAQConverterSparkFNObject = new TAQConverterSparkFN2(inputFileName, outputFileName, fieldTypes, startTime, endTime, start);
            else
                TAQConverterSparkFNObject = new TAQConverterSparkFN2(inputFileName, outputFileName, fieldTypes,startTime,endTime, tickerSymbols, start);
        }
//                try {
//                    TimeUnit.MINUTES.sleep(1);
//
//                    Path path = Paths.get(folderName);
//                    deleteFileOrFolder(path);
//                    print("folder deleted");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

}
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        printTime();
        TAQConverterMainFN2 TAQAnalysisObject = new TAQConverterMainFN2(args);
        printTime();
        long endTime = System.currentTimeMillis();
        printElapsedTime(startTime, endTime);
    }
}