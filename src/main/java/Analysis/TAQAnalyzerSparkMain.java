package Analysis;

import DataFieldType.IFieldType;
import DataFieldType.TAQ2010Spec;
import DataFieldType.TAQJune2015Spec;
import Misc.FileClass;
import Misc.UnZip;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static DataFieldType.TickerSymbols.getTickers;
import static Misc.FileClass.deleteFileOrFolder;
import static Misc.Print.print;
import static Misc.Time.printElapsedTime;
import static Misc.Time.printTime;


public class TAQAnalyzerSparkMain {
    private TAQ2010Spec fieldObject2010;
    private TAQJune2015Spec fieldObject2015;
    private TAQAnalyzerSpark TAQConverterSparkFNObject;
    private FileClass fileReadObject;
    private IFieldType[] fieldTypes;
    private String outputFileName;
    private List<String> tickerSymbols;


    TAQAnalyzerSparkMain(String[] args) {
        String type = args[0];
        int year = Integer.parseInt(args[1]);
        int start = 1;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        outputFileName = args[2] + "_SparkConverted_" + timeStamp + ".txt";
        tickerSymbols = getTickers();

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
        } else if (year == 2010) {
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
            case "su":
                if (args[4].equals("n")) {
                    if (args[5].equals("n")) {
                        TAQConverterSparkFNObject = new TAQAnalyzerSpark(args[2], fieldTypes, start);
                    } else {

                        TAQConverterSparkFNObject = new TAQAnalyzerSpark(args[2], fieldTypes, start, tickerSymbols);
                    }
                } else {
                    if (args[6].equals("n")) {
                        TAQConverterSparkFNObject = new TAQAnalyzerSpark(args[2], fieldTypes, args[4], args[5], start);
                    } else {
                        TAQConverterSparkFNObject = new TAQAnalyzerSpark(args[2], fieldTypes, args[4], args[5], start, tickerSymbols);
                    }
                }
                break;
            case "zu":
                UnZip unZip = new UnZip();
                String folderName = args[2].substring( 0, args[2].length()-4);
                print("asd"+folderName);
                unZip.unZipIt(args[2],folderName);
                File dir = new File(folderName);
                String[] inputFileName =  dir.list();
                    if (args[4].equals("n"))
                        TAQConverterSparkFNObject = new TAQAnalyzerSpark(inputFileName[0], fieldTypes, start);
                    else
                        TAQConverterSparkFNObject = new TAQAnalyzerSpark(inputFileName[0], fieldTypes, args[4], args[5], start);
                try {
                    TimeUnit.MINUTES.sleep(5);
                    Path path = Paths.get(folderName);
                    deleteFileOrFolder(path);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        printTime();
        TAQAnalyzerSparkMain TAQAnalysisObject = new TAQAnalyzerSparkMain(args);
        printTime();
        long endTime = System.currentTimeMillis();
        printElapsedTime(startTime, endTime);
    }
}