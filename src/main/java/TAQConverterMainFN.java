import BasicImplementation.TAQConverterUnziped;
import BasicImplementation.TAQConverterZipExtractFN;
import DataFieldType.IFieldType;
import DataFieldType.TAQ102010Spec;
import DataFieldType.TAQ062015Spec;
import Misc.FileClass;
import Misc.UnZip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static DataFieldType.TickerSymbols.getTickers;
import static Misc.Debug.debug;
import static Misc.Print.print;
import static Misc.Time.printElapsedTime;
import static Misc.Time.printTime;


public class TAQConverterMainFN {
    private String inputFileName;
    private TAQ102010Spec fieldObject2010;
    private TAQ062015Spec fieldObject2015;
    private TAQConverterSparkFN TAQConverterSparkFNObject;
    private TAQConverterZipExtractFN TAQConverterZipObject;
    private TAQConverterUnziped TAQConverterUnzipedObject;
    private FileClass fileReadObject;
    private IFieldType[] fieldTypes;
    private String outputFileName;
    private List<String> tickerSymbols;


    TAQConverterMainFN(String[] args) {
        this.inputFileName = args[2];
        String type = args[0];
        int year = Integer.parseInt(args[1]);
        int start = 1;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        if (args[2].substring(args[2].length()-3,args[2].length()).equals("zip")) {
            outputFileName = args[2].substring(0,args[2].length()-4) + "_converted";
        } else {
            outputFileName = args[2] + "_converted";
        }

        tickerSymbols = getTickers();

        if (year == 2015) {
            TAQ062015Spec fieldObject2015 = new TAQ062015Spec();
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
            TAQ102010Spec fieldObject2010 = new TAQ102010Spec();
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
                        TAQConverterSparkFNObject = new TAQConverterSparkFN(args[2], outputFileName, fieldTypes, start);
                    } else {

                        TAQConverterSparkFNObject = new TAQConverterSparkFN(args[2], outputFileName, fieldTypes, start, tickerSymbols);
                    }
                } else {
                    if (args[6].equals("n")) {
                        TAQConverterSparkFNObject = new TAQConverterSparkFN(args[2], outputFileName, fieldTypes, args[4], args[5], start);
                    } else {
                        TAQConverterSparkFNObject = new TAQConverterSparkFN(args[2], outputFileName, fieldTypes, args[4], args[5], start, tickerSymbols);
                    }
                }
                break;
            case "z":
                if (args[4].equals("n"))
                    TAQConverterZipObject = new TAQConverterZipExtractFN(args[2], outputFileName, fieldTypes, start);
                else
                    TAQConverterZipObject = new TAQConverterZipExtractFN(args[2], outputFileName, fieldTypes, args[4], args[5], start);
                break;
            case "zu":
                UnZip unZip = new UnZip();
                String outputFileName = inputFileName.substring(0, inputFileName.length() - 4) + "converted";
                unZip.unZipIt(inputFileName, outputFileName);
                print("unzipping complete");
                inputFileName = outputFileName;
                outputFileName = inputFileName.substring(0, inputFileName.length()-4)+"_spark";
                if (args[4].equals("n")) {
                    debug(5);
                    TAQConverterSparkFNObject = new TAQConverterSparkFN(inputFileName, outputFileName, fieldTypes, start);
                }else {
                    TAQConverterSparkFNObject = new TAQConverterSparkFN(inputFileName, outputFileName, fieldTypes, args[4], args[5], start);
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
                break;
            case "u":
                outputFileName = inputFileName+"_converted.txt";
                TAQConverterUnzipedObject = new TAQConverterUnziped(args[2], outputFileName, 73, fieldTypes, 1024 * 73);
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        printTime();
        TAQConverterMainFN TAQAnalysisObject = new TAQConverterMainFN(args);
        printTime();
        long endTime = System.currentTimeMillis();
        printElapsedTime(startTime, endTime);
    }
}