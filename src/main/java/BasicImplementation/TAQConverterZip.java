package BasicImplementation; /**
 * Created by Anjana on 5/29/2017.
 */

import DataFieldType.IFieldType;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TAQConverterZip {
    private ZipFile zf;
    private PrintStream outputStream;
    private String outputFileName;
    private File outputFile;
    private IFieldType[] fieldType;
    private int startOffset;
    private BufferedReader br;

    TAQConverterZip(String zipfile, IFieldType[] fieldType, int startOffset) {
        try {
            this.zf = new ZipFile(zipfile);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            outputFileName = zipfile.substring(0, zipfile.length() - 4) + "_SparkConverted_" + timeStamp + ".txt";
            outputFile = new File(outputFileName);
            outputStream = new PrintStream(outputFile);
            this.startOffset = startOffset;
            this.fieldType = fieldType;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void convertFile() throws IOException {
        try {
            int start = 0;
            StringBuilder strBuilder = new StringBuilder();
            Enumeration entries = zf.entries();
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
//                System.out.println("Read " + ze.getName() + "?");
//                String inputLine = input.readLine();
//                if (inputLine.equalsIgnoreCase("yes")) {
                long size = ze.getSize();
                if (size > 0) {
                    System.out.println("Length is " + size);
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    br.readLine();
                    line = br.readLine();
                    int k = 0;
                    while (line != null) {
                        System.out.println("ew");
                        for (int i = 0; i < fieldType.length - 1; i++) {
                            String tempStr = fieldType[i].convertFromBinary(line, start);
                            strBuilder.append(tempStr);
                            start = start + fieldType[i].getLength();
                            if (i < fieldType.length - 1) {
                                strBuilder.append(',');
                            }
                            if (i == fieldType.length - 2) {
                                strBuilder.append('.');
                            }
                        }

                        strBuilder.append("\r\n");
                        if (k > 100) {
                            outputStream.print(strBuilder);
                            outputStream.flush();
                            strBuilder.setLength(0);
                        }
                        line = br.readLine();
                        start = 0;
                        k++;
                    }
                }
//                }
                closeStream();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            closeStream();
        }
    }

    private void closeStream() throws IOException {
        br.close();
        zf.close();
        outputStream.close();
    }
}
