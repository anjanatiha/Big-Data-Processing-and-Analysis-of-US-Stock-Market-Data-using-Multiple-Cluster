package BasicImplementation; /**
 * Created by Anjana on 5/29/2017.
 */

import DataFieldType.IFieldType;

import java.io.*;

public class TAQDataConverterUnzipedSpark implements Serializable {
    private File inputFile, outputFile;
    private InputStream inputStream;
    private PrintStream outputStream;
    private byte[] inputBuffer;
    private int startOffset;
    private IFieldType[] fieldType;
    private int recordLength;
    private int bufferSize;
    private static final long serialVersionUID = 7526472295622776147L;

    TAQDataConverterUnzipedSpark(String inputFileName, String outputFileName) {
        inputFile = new File(inputFileName);
        outputFile = new File(outputFileName);
        try {
            inputStream = new FileInputStream(inputFile);
            outputStream = new PrintStream(outputFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setAttributes(int startOffset, IFieldType[] fieldType, int bufferSize) {
        this.startOffset = startOffset;
        this.fieldType = fieldType;
        recordLength = getRecordLength();
        this.bufferSize = bufferSize;
    }


    public File convertFile() throws IOException {

        try {
            byte[] inputBuffer = new byte[bufferSize];
            StringBuilder strBuilder = new StringBuilder();
            inputStream.skip(startOffset);

            int start = 0;
            long bytesRead = 0;
            long totalByteRead = 0;
            int k = 0;
            while (true) {
                System.out.println("hi");
                bytesRead = inputStream.read(inputBuffer);
                if (bytesRead < recordLength) {
                    System.out.println("bytesRead<recordLength: \n" + "bytesRead:" + bytesRead + "recordLength: " + recordLength);
                    break;
                }
                totalByteRead += bytesRead;
//                System.out.println("Total byte read: "+totalByteRead);
                start = 0;

                while (bytesRead > start) {
                    for (int i = 0; i < fieldType.length; i++) {
                        String tempStr = fieldType[i].convertFromBinary(inputBuffer, start);
                        strBuilder.append(tempStr);
                        start = start + fieldType[i].getLength();
                        if (i < fieldType.length - 2)
                            strBuilder.append(',');
                    }
                    strBuilder.append("\r\n");
                    outputStream.print(strBuilder);
                    outputStream.flush();
                    strBuilder.setLength(0);
                }
                k++;
                if (k > 2)
                    break;
            }
            System.out.println("Total byte read: " + totalByteRead);

        } catch (Exception e) {
            closeStream();
        }
        return outputFile;
    }

    private int getRecordLength() {
        int length = 0;
        for (int i = 0; i < fieldType.length; i++) {
            length = length + fieldType[i].getLength();
        }
        return length;
    }

    private void closeStream() throws IOException {
        inputStream.close();
        outputStream.close();
    }


}
