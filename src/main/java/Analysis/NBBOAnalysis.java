package Analysis; /**
 * Created by Anjana on 5/29/2017.
 */

import DataFieldType.IFieldType;
import DataFieldType.StockExchanges;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NBBOAnalysis {
    private ZipFile zf;
    private PrintStream outputStream;
    private File outputFile;
    private IFieldType[] fieldType;
    private int startOffset;
    private BufferedReader br;
    private long bufferSize;
    private String startTime = null, endTime = null;

    NBBOAnalysis(String zipfile, String outputFileName, String startTime, String endTime) {
        try {
            this.zf = new ZipFile(zipfile);
            this.outputFile = new File(outputFileName);
            this.outputStream = new PrintStream(outputFile);
            this.startTime = startTime;
            this.endTime = endTime;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    NBBOAnalysis(String zipfile, String outputFileName) {
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

    private int getIndexSec(String time) {
        int index = (Integer.parseInt(time.substring(0, 2))) * 3600 + (Integer.parseInt(time.substring(2, 4))) * 60 + (Integer.parseInt(time.substring(4, 6)));
        return index - 1;
    }

    public int getIndexMin(String time) {
        int index = (Integer.parseInt(time.substring(0, 2))) * 3600 + (Integer.parseInt(time.substring(2, 4))) * 60;
        return index - 1;
    }

    public void printBigInt(BigInteger[][] list_arr, int start, int end) {
        for (int m = 0; m < list_arr.length; m++) {
            for (int n = 0; n < list_arr[m].length; n++) {
                if (list_arr[m][0].equals(BigInteger.ZERO))
                    break;
                System.out.print(list_arr[m][n] + " ");
            }
            System.out.print("\n");
        }
    }

    private void printBigDecimal(BigDecimal[][] list_arr, int start, int end) {
        for (int m = 0; m < list_arr.length; m++) {
            for (int n = 0; n < list_arr[m].length; n++) {
                if (list_arr[m][0].equals(BigDecimal.ZERO))
                    break;
                System.out.print(list_arr[m][n] + " ");
            }
            System.out.print("\n");
        }
    }

    public BigInteger[][] initArr(BigInteger[][] list) {
        for (int m = 0; m < list.length; m++) {
            for (int n = 0; n < list[m].length; n++) {
                list[m][n] = BigInteger.ZERO;
            }
        }
        return list;
    }

    private BigDecimal[][] initArr(BigDecimal[][] list) {
        for (int m = 0; m < list.length; m++) {
            for (int n = 0; n < list[m].length; n++) {
                list[m][n] = BigDecimal.ZERO;
            }
        }
        return list;
    }

    private void writeHeaderNBBO() {
        String[] headers = {"Bid Time", "Ask Time", "Bid Exchange", "Ask Exchange", "Bid Price", "Bid Volume", "Ask Price", "Ask Volume"};
        StringBuilder tempLine = new StringBuilder();
        outputStream.print("Time,");
        for (int m = 0; m < headers.length; m++) {
            tempLine.append(headers[m]);
            tempLine.append(",");
        }
        tempLine.append("\n");
        outputStream.print(tempLine);
        outputStream.flush();
        tempLine.setLength(0);
    }

    public void writeFileBigInteger(BigInteger[][] time_series_arr, StockExchanges exchangesObj) {
        writeHeaderNBBO();
        StringBuilder tempLine = new StringBuilder();
        for (int m = 0; m < time_series_arr.length; m++) {
            for (int n = 0; n < time_series_arr[m].length; n++) {
                tempLine.append(time_series_arr[m][n]);
                tempLine.append(",");
            }
            tempLine.append("\n");
            outputStream.print(tempLine);
            outputStream.flush();
            tempLine.setLength(0);

        }
        outputStream.close();
    }

    private void writeFileBigDecimal(BigDecimal[][] time_series_arr, StockExchanges exchangesObj) {
        writeHeaderNBBO();
        StringBuilder tempLine = new StringBuilder();
        for (int m = 0; m < time_series_arr.length; m++) {
            for (int n = 0; n < time_series_arr[m].length; n++) {
                tempLine.append(time_series_arr[m][n]);
                tempLine.append(",");
            }
            tempLine.append("\n");
            outputStream.print(tempLine);
            outputStream.flush();
            tempLine.setLength(0);

        }
        outputStream.close();
    }

    public void NBBOAnalyzer() {
        try {
            Enumeration entries = zf.entries();
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            StockExchanges exchangesObj = new StockExchanges();

            HashMap<String, Integer> exchanges = exchangesObj.getExchanges_map();
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                long size = ze.getSize();
                if (size > 0) {
                    System.out.println("Length is " + size);
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    br.readLine();
                    line = br.readLine();
                    int k = 0;
                    int start_ind = 0, end_ind = 0;
                    BigDecimal[][] time_series_arr = new BigDecimal[86400][8];
                    time_series_arr = initArr(time_series_arr);
                    int time_index = -1;
                    BigDecimal time = BigDecimal.ZERO;
                    BigDecimal bid_price = BigDecimal.ZERO;
                    BigDecimal bid_size = BigDecimal.ZERO;
                    BigDecimal ask_price = BigDecimal.ZERO;
                    BigDecimal ask_size = BigDecimal.ZERO;
                    int bid_exchange = -1;
                    int ask_exchange = -1;

                    while (line != null) {
                        int start = 0;
                        for (int i = 0; i < fieldType.length - 1; i++) {
                            String tempStr = fieldType[i].convertFromBinary(line, start);
                            if (i == 0) {
                                time = new BigDecimal(tempStr);
                                time_index = getIndexSec(tempStr);
                            }
                            if (i == 3) {
                                bid_price = BigDecimal.valueOf(Double.parseDouble(String.valueOf(tempStr)));
                            }
                            if (i == 4) {
                                bid_size = BigDecimal.valueOf(Long.parseLong(String.valueOf(tempStr)));
                            }
                            if (i == 5) {
                                ask_price = BigDecimal.valueOf(Double.parseDouble(String.valueOf(tempStr)));
                            }
                            if (i == 6) {
                                ask_size = BigDecimal.valueOf(Long.parseLong(tempStr));
                            }
                            if (i == 9) {
                                bid_exchange = exchanges.get(tempStr);
                                time_series_arr[time_index][0] = time;
                                time_series_arr[time_index][2] = new BigDecimal(String.valueOf(bid_exchange));
                                time_series_arr[time_index][4] = time_series_arr[time_index][4].add(bid_price.multiply(bid_size));
                                time_series_arr[time_index][5] = time_series_arr[time_index][5].add(bid_size);
                            }
                            if (i == 10) {

                                ask_exchange = exchanges.get(tempStr);
                                time_series_arr[time_index][1] = new BigDecimal(String.valueOf(time));
                                time_series_arr[time_index][3] = new BigDecimal(String.valueOf(ask_exchange));
                                time_series_arr[time_index][6] = time_series_arr[time_index][6].add(ask_price.multiply(ask_size));
                                time_series_arr[time_index][7] = time_series_arr[time_index][7].add(ask_size);
                            }

                            start = start + fieldType[i].getLength();
                        }
                        line = br.readLine();
                        k++;
//                        if (k>10000) {
//                            break;
//                        }
                    }
                    printBigDecimal(time_series_arr, start_ind, end_ind);
                    writeFileBigDecimal(time_series_arr, exchangesObj);
                }
            }
            closeStream();
        } catch (Exception e) {
            System.out.println("Error: " + e);
            closeStream();
        }
    }

    private void closeStream() {
        try {
            br.close();
            zf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
