import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TAQAnalysis {
    private int recordLength;
    private TAQ2010Spec fieldObject2010;
    private TAQJune2015Spec fieldObject2015;
    private NBBOAnalysis NBBOAnalysisObject;
    private TradeAnalysis TradeAnalysisObject;
    private QuoteAnalysis QuoteAnalysisObject;
    private QuoteNBBOPerStock QuoteNBBOPerStockObject;
    private long bufferSize;

    TAQAnalysis(String[] args) {
        String type = args[0];
        int year = Integer.parseInt(args[1]);
        int start = 1;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String outputFileName = args[2].substring(0,args[2].length()-4)+"_Analysis_"+args[0]+"_"+timeStamp+".txt";
        if (year == 2015) {
            TAQJune2015Spec fieldObject2015 = new TAQJune2015Spec();
            switch (type) {
                case "trade":
                    recordLength = fieldObject2015.getTradeFieldsLength();
                    TradeAnalysisObject = new TradeAnalysis(args[2], outputFileName);
                    bufferSize = 1024 * recordLength;
                    TradeAnalysisObject.setAttributes(start, fieldObject2015.getTradeFields(), bufferSize);
                    TradeAnalysisObject.TradeAnalyzer();
                    break;

                case "nbbo":
                    recordLength = fieldObject2015.getNBBOFieldsLength();
                    NBBOAnalysisObject = new NBBOAnalysis(args[2], outputFileName);
                    bufferSize = 1024 * recordLength;
                    NBBOAnalysisObject.setAttributes(start, fieldObject2015.getNBBOFields(), bufferSize);
                    NBBOAnalysisObject.NBBOAnalyzer();
                    break;
                case "quote":
                    recordLength = fieldObject2015.getQuoteFieldsLength();
                    QuoteAnalysisObject = new QuoteAnalysis(args[2], outputFileName);
                    bufferSize = 1024 * recordLength;
                    QuoteAnalysisObject.setAttributes(start, fieldObject2015.getQuoteFields(), bufferSize);
                    QuoteAnalysisObject.QuoteAnalyzer();
                    break;
                case "nbboquote":
                    recordLength = fieldObject2015.getQuoteFieldsLength();
                    QuoteNBBOPerStockObject = new QuoteNBBOPerStock(args[2], outputFileName);
                    bufferSize = 1024 * recordLength;
                    QuoteNBBOPerStockObject.setAttributes(start, fieldObject2015.getQuoteFields(), bufferSize);
                    QuoteNBBOPerStockObject.QuoteNBBOPerStockAnalyzer();
                    break;
            }
        }
        else if (year == 2010) {

            TAQ2010Spec fieldObject2010 = new TAQ2010Spec();
            switch (type) {
                case "trade":
                    recordLength = fieldObject2010.getTradeFieldsLength();
                    TradeAnalysisObject = new TradeAnalysis(args[2], outputFileName);
                    bufferSize = 1024 * recordLength;
                    TradeAnalysisObject.setAttributes(start, fieldObject2010.getTradeFields(), bufferSize);
                    TradeAnalysisObject.TradeAnalyzer();
                    break;
                case "nbbo":
                    recordLength = fieldObject2010.getNBBOFieldsLength();
                    NBBOAnalysisObject = new NBBOAnalysis(args[2], outputFileName);
                    bufferSize = 1024 * recordLength;
                    NBBOAnalysisObject.setAttributes(start, fieldObject2010.getNBBOFields(), bufferSize);
                    NBBOAnalysisObject.NBBOAnalyzer();
                    break;
                case "quote":
                    recordLength = fieldObject2010.getQuoteFieldsLength();
                    QuoteAnalysisObject = new QuoteAnalysis(args[2], outputFileName);
                    bufferSize = 1024 * recordLength;
                    QuoteAnalysisObject.setAttributes(start, fieldObject2010.getQuoteFields(), bufferSize);
                    QuoteAnalysisObject.QuoteAnalyzer();
                    break;
                case "nbboquote":
                    recordLength = fieldObject2010.getQuoteFieldsLength();
                    QuoteNBBOPerStockObject = new QuoteNBBOPerStock(args[2], outputFileName);
                    bufferSize = 1024 * recordLength;
                    QuoteNBBOPerStockObject.setAttributes(start, fieldObject2010.getQuoteFields(), bufferSize);
                    QuoteNBBOPerStockObject.QuoteNBBOPerStockAnalyzer();
                    break;
            }
        }
    }
    public static void printTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");
        String formattedDate = dateFormat.format(new Date()).toString();
        System.out.println(formattedDate);
    }
    public static void printElapsedTime(long startTime, long endTime){
        long totalTime = endTime - startTime;
        int seconds = (int) ((totalTime / 1000) % 60);
        int minutes = (int) ((totalTime / 1000) / 60);
        System.out.println("\n\nTotal Time: "+ minutes+":"+seconds);
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        printTime();
        TAQAnalysis TAQAnalysisObject = new TAQAnalysis(args);
        printTime();
        long endTime   = System.currentTimeMillis();
        printElapsedTime(startTime, endTime);
    }
}