//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class TAQConverterUnzippedMain {
//    private int recordLength;
//    private TAQ2010Spec fieldObject2010;
//    private TAQJune2015Spec fieldObject2015;
//    private TAQConverterUnziped TAQConverterUnzipedObject;
//    private IFieldType[] fieldTypes;
//    private int bufferSize;
//
//    TAQConverterUnzippedMain(String[] args) throws IOException {
//        String type = args[0];
//        int year = Integer.parseInt(args[1]);
//        int start = 1;
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
//        String outputFileName = args[2]+"_Converted_"+timeStamp+".txt";
//        if (year == 2015) {
//            TAQJune2015Spec fieldObject2015 = new TAQJune2015Spec();
//            switch (type) {
//                case "trade":
//                    recordLength = fieldObject2015.getTradeFieldsLength();
//                    fieldTypes = fieldObject2015.getTradeFields();
//                    break;
//                case "nbbo":
//                    recordLength = fieldObject2015.getNBBOFieldsLength();
//                    fieldTypes = fieldObject2015.getNBBOFields();
//                    break;
//                case "quote":
//                    recordLength = fieldObject2015.getQuoteFieldsLength();
//                    fieldTypes =fieldObject2015.getQuoteFields();
//                    break;
//            }
//        }
//        else if (year == 2010) {
//
//            TAQ2010Spec fieldObject2010 = new TAQ2010Spec();
//            switch (type) {
//                case "trade":
//                    recordLength = fieldObject2010.getTradeFieldsLength();
//                    fieldTypes =fieldObject2010.getTradeFields();
//                    break;
//                case "nbbo":
//                    recordLength = fieldObject2010.getNBBOFieldsLength();
//                    fieldTypes = fieldObject2010.getNBBOFields();
//                    break;
//                case "quote":
//                    recordLength = fieldObject2010.getQuoteFieldsLength();
//                    fieldTypes = fieldObject2010.getQuoteFields();
//                    break;
//            }
//        }
//        TAQConverterUnzipedObject = new TAQConverterUnziped(args[2], outputFileName);
//        bufferSize = 1024 * recordLength;
//        TAQConverterUnzipedObject.setAttributes(start, fieldTypes, bufferSize);
//        TAQConverterUnzipedObject.convertFile();
//
//    }
//    public static void printTime(){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");
//        String formattedDate = dateFormat.format(new Date()).toString();
//        System.out.println(formattedDate);
//    }
//    public static void printElapsedTime(long startTime, long endTime){
//        long totalTime = endTime - startTime;
//        int seconds = (int) ((totalTime / 1000) % 60);
//        int minutes = (int) ((totalTime / 1000) / 60);
//        System.out.println("\n\nTotal Time: "+ minutes+":"+seconds);
//    }
//
//    public static void main(String[] args) throws IOException {
//        long startTime = System.currentTimeMillis();
//        printTime();
//        TAQConverterUnzippedMain TAQAnalysisObject = new TAQConverterUnzippedMain(args);
//        printTime();
//        long endTime   = System.currentTimeMillis();
//        printElapsedTime(startTime, endTime);
//    }
//}
