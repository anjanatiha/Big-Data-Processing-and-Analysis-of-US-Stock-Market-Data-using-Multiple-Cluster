//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class TAQDataConverterUnzipedMain {
//    public static void main(String[] args) throws IOException {
//        long startTime = System.currentTimeMillis();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");
//        String formattedDate = dateFormat.format(new Date()).toString();
//        System.out.println(formattedDate);
//
//        TAQConverterUnziped taqObj = new TAQConverterUnziped(args[0], args[1]);
//        TAQJune2015Spec fieldSpec = new TAQJune2015Spec();
//        int fieldLength = fieldSpec.getNBBOFieldsLength();
//        int start = fieldLength;
//        int bufferSize = 1024 * 1024 * 28 * fieldLength;
//        taqObj.setAttributes(start, fieldSpec.getNBBOFields(), bufferSize);
//        taqObj.convertFile();
//
//        long endTime   = System.currentTimeMillis();
//        long totalTime = endTime - startTime;
//        int seconds = (int) ((totalTime / 1000) % 60);
//        int minutes = (int) ((totalTime / 1000) / 60);
//        System.out.println("\n\nTotal Time: "+ minutes+":"+seconds);
//
//        dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");
//        formattedDate = dateFormat.format(new Date()).toString();
//        System.out.println(formattedDate);
//    }
//}
