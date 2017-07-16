import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TAQConverterMainFN {
    private TAQ2010Spec fieldObject2010;
    private TAQJune2015Spec fieldObject2015;
    private TAQConverterSparkFN TAQConverterSparkFNObject;
    private TAQConverterZipExtractFN TAQConverterZipObject;
    private TAQConverterUnziped TAQConverterUnzipedObject;
    private IFieldType[] fieldTypes;
    private String outputFileName;


    TAQConverterMainFN(String[] args) {
        String type = args[0];
        int year = Integer.parseInt(args[1]);
        int start = 1;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        outputFileName =args[2]+"_SparkConverted_"+timeStamp+".txt";
        if (year == 2015) {
            TAQJune2015Spec fieldObject2015 = new TAQJune2015Spec();
            switch (type) {
                case "trade":
                    fieldTypes = fieldObject2015.getTradeFields();
                    break;

                case "nbbo":
                    fieldTypes = fieldObject2015.getNBBOFields();
                    break;
                case "quote":
                    fieldTypes = fieldObject2015.getQuoteFields();
                    break;
            }
        }
        else if (year == 2010) {

            TAQ2010Spec fieldObject2010 = new TAQ2010Spec();
            switch (type) {
                case "trade":
                    fieldTypes = fieldObject2010.getTradeFields();
                    break;
                case "nbbo":
                    fieldTypes = fieldObject2010.getNBBOFields();
                    break;
                case "quote":
                    fieldTypes = fieldObject2010.getQuoteFields();
                    break;
            }
        }
        switch (args[3]) {
            case "sparku":
                TAQConverterSparkFNObject = new TAQConverterSparkFN(args[2], fieldTypes, start);
                break;
            case "z":
                if (args[4].equals("n"))
                    TAQConverterZipObject = new TAQConverterZipExtractFN(args[2], outputFileName, fieldTypes, start);
                else
                    TAQConverterZipObject = new TAQConverterZipExtractFN(args[2], outputFileName, fieldTypes, args[4],args[5], start);
                break;
            case "u":
                TAQConverterUnzipedObject = new TAQConverterUnziped(args[2], outputFileName,73, fieldTypes,1024*73);
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
        TAQConverterMainFN TAQAnalysisObject = new TAQConverterMainFN(args);
        printTime();
        long endTime   = System.currentTimeMillis();
        printElapsedTime(startTime, endTime);
    }
}