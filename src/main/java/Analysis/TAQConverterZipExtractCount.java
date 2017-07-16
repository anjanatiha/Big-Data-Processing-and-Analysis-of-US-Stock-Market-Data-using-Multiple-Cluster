package Analysis; /**
 * Created by Anjana on 5/29/2017.
 */

import DataFieldType.IFieldType;

import java.io.*;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TAQConverterZipExtractCount {
    private String inputFileName;
    private ZipFile zf;
    private PrintStream outputStream;
    private File outputFile;
    private IFieldType[] fieldType;
    private int startOffset;
    private BufferedReader br;
    private long bufferSize;
    private String startTime = null, endTime = null;
    private BigInteger tradeVolumeActual;

    TAQConverterZipExtractCount(String zipfile, String outputFileName, String startTime, String endTime) {
        try {
            this.inputFileName = zipfile;
            this.zf = new ZipFile(zipfile);
            this.outputFile = new File(outputFileName);
            this.outputStream = new PrintStream(outputFile);
            this.startTime = startTime;
            this.endTime = endTime;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    TAQConverterZipExtractCount(String zipfile, String outputFileName) {
        try {
            this.zf = new ZipFile(zipfile);
            this.outputFile = new File(outputFileName);
            this.outputStream = new PrintStream(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setAttributes(int startOffset, IFieldType[] fieldType, long bufferSize) {
        this.startOffset = startOffset;
        this.fieldType = fieldType;
        this.bufferSize = bufferSize;
    }

    public void print(String str) {
        System.out.print(str);
    }

    public void print(String[] str) {
        for (int i = 0; i < str.length; i++) {
            System.out.print(str[i] + " ");
        }
    }

    public void print(Integer str) {
        System.out.print(str);
    }

    public void print(Integer[] val) {
        for (int i = 0; i < val.length; i++) {
            System.out.print(val[i] + " ");
        }
    }

    private int toInt(String str) {
        return Integer.parseInt(str);
    }

    private String inRange(String time, String startTime, String endTime) {
        int hr = toInt(time.substring(0, 2));
        int min = toInt(time.substring(2, 4));
        int sec = toInt(time.substring(4, 6));
        int shr = toInt(startTime.substring(0, 2));
        int smin = toInt(startTime.substring(2, 4));
        int ssec = toInt(startTime.substring(4, 6));
        int ehr = toInt(endTime.substring(0, 2));
        int emin = toInt(endTime.substring(2, 4));
        int esec = toInt(endTime.substring(4, 6));
        if (hr > ehr)
            return "None";
        else if ((hr == ehr) & (min > emin))
            return "None";
        else {
            if (hr < shr)
                return "Continue";
            else if ((hr == shr) & (min < smin))
                return "Continue";
            else if ((hr == shr) & (min >= smin))
                return "Append";
            else
                return "Append";
        }
    }

    public Integer[] toInt(String[] str) {
        Integer[] newInt = new Integer[str.length];
        for (int i = 0; i < str.length; i++) {
            newInt[i] = Integer.parseInt(str[i]);
        }
        return newInt;
    }

    public void convertFile() throws IOException {
        try {
            HashMap<String, Integer> countMap = new HashMap<>();
            int start = 0;
            StringBuilder strBuilder = new StringBuilder();
            Enumeration entries = zf.entries();
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            int l = 0;
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                System.out.println("Read " + ze.getName() + "?");
                String inputLine = input.readLine();
                if (inputLine.equalsIgnoreCase("y")) {
                    long size = ze.getSize();
                    if (size > 0) {
                        System.out.println("Length is " + size);
                        br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        String time;
                        String inRange = "";
                        br.readLine();
                        line = br.readLine();

                        while (line != null) {
                            StringBuilder tempLine = new StringBuilder();
                            for (int i = 0; i < fieldType.length - 1; i++) {
                                String tempStr = fieldType[i].convertFromBinary(line, start);
                                if (startTime != null & endTime != null) {
                                    if (i == 0) {
                                        time = tempStr;
                                        inRange = inRange(time, startTime, endTime);
                                    }
                                }
//                                tempLine.append(tempStr);
                                if (i == 4) {
                                    BigInteger tradeVolumeNew = new BigInteger(tempStr);
                                    if (tradeVolumeActual == null)
                                        tradeVolumeActual = tradeVolumeNew;
                                    else
                                        tradeVolumeActual = tradeVolumeActual.add(tradeVolumeNew);

                                }
                                start = start + fieldType[i].getLength();
//                                if(i==2){
//
//                                    if (countMap.containsKey(tempStr)){
//                                        countMap.put(tempStr, countMap.get(tempStr)+1);
//                                    }
//                                    else
//                                        countMap.put(tempStr, 1);
//                                }


                            }
                            if (startTime != null & endTime != null) {
                                if (inRange.equals("None")) {
                                    break;
                                } else if (inRange.equals("Append")) {
//                                    outputStream.print(tempLine);
//                                    outputStream.flush();
                                    tempLine.setLength(0);
                                    line = br.readLine();
                                    start = 0;
                                } else if (inRange.equals("Continue")) {
                                    tempLine.setLength(0);
                                    line = br.readLine();
                                    start = 0;
                                    continue;
                                }
                            } else {
//                                    outputStream.print(tempLine);
//                                    outputStream.flush();
                                tempLine.setLength(0);
                                line = br.readLine();
                                start = 0;
                            }
                            l++;

                        }
                        outputStream.print(inputFileName + ": " + tradeVolumeActual);
                        outputStream.flush();
                        System.out.println(tradeVolumeActual);
                    }
                }

            }
//            for (String name: countMap.keySet()){
//
//                String key =name.toString();
//                String value = countMap.get(name).toString();
//                System.out.println(key + " " + value);
//
//
//            }
            closeStream();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            closeStream();
        }
    }

    private void closeStream() throws IOException {
        br.close();
        zf.close();
//        outputStream.close();
    }
}
