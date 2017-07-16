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

public class TradeAnalysis {
    private ZipFile zf;
    private PrintStream outputStream;
    private File outputFile;
    private IFieldType[] fieldType;
    private int startOffset;
    private BufferedReader br;
    private long bufferSize;
    private String startTime=null, endTime=null;

    TradeAnalysis(String zipfile, String outputFileName, String startTime, String endTime){
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
    TradeAnalysis(String zipfile, String outputFileName){
        try {
            this.zf = new ZipFile(zipfile);
            this.outputFile = new File(outputFileName);
            this.outputStream = new PrintStream(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAttributes(int startOffset, IFieldType[] fieldType, long bufferSize){
        this.startOffset=startOffset;
        this.fieldType = fieldType;
        this.bufferSize = bufferSize;
    }
    private int getIndexSec(String time){
        int index = (Integer.parseInt(time.substring(0,2)))*3600+(Integer.parseInt(time.substring(2,4)))*60 + (Integer.parseInt(time.substring(4,6)));
        return index-1;
    }
    private int getIndexMin(String time){
        int index = (Integer.parseInt(time.substring(0,2)))*3600+(Integer.parseInt(time.substring(2,4)))*60;
        return index-1;
    }

    private BigInteger[][] initArr(BigInteger[][] list){
        for(int m=0; m<list.length;m++){
            for(int n=0; n<list[m].length;n++){
                list[m][n]= BigInteger.ZERO;
            }
        }
        return list;
    }

    public BigDecimal[][] initArr(BigDecimal[][] list){
        for(int m=0; m<list.length;m++){
            for(int n=0; n<list[m].length;n++){
                list[m][n]= BigDecimal.ZERO;
            }
        }
        return list;
    }


    private void printBigInt(BigInteger[][] list_arr, int start, int end){
        for(int m=0; m<list_arr.length;m++){
            for(int n=0; n<list_arr[m].length;n++){
                if (list_arr[m][0].equals(BigInteger.ZERO))
                    break;
                System.out.print(list_arr[m][n] + " ");
            }
            System.out.print("\n");
        }
    }
    public void printBigDecimal(BigDecimal[][] list_arr){
        for(int m=0; m<list_arr.length;m++){
            for(int n=0; n<list_arr[m].length;n++){
                if (list_arr[m][0].equals(BigDecimal.ZERO))
                    break;
                System.out.print(list_arr[m][n] + " ");
            }
            System.out.print("\n");
        }
    }


    private void writeHeader(StockExchanges exchangesObj){
        String[] headers = exchangesObj.getExchangeNames();
        StringBuilder tempLine = new StringBuilder();
        outputStream.print("Time,");
        for(int m=0; m<headers.length;m++){
            tempLine.append(headers[m]);
            tempLine.append(",");
        }
        tempLine.append("\n");
        outputStream.print(tempLine);
        outputStream.flush();
        tempLine.setLength(0);
    }

    private void writeFile(BigInteger[][] time_series_arr, StockExchanges exchangesObj){
        writeHeader(exchangesObj);
        StringBuilder tempLine = new StringBuilder();
        for(int m=0; m<time_series_arr.length;m++){
            for(int n=0; n<time_series_arr[m].length;n++){
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

    public void TradeAnalyzer() {
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
                    int start_ind=0, end_ind=0;
                    BigInteger[][] time_series_arr = new BigInteger[86400][20];
                    time_series_arr = initArr(time_series_arr);
                    String time_s = "";
                    int exchange=-1;
                    String exchange_s="";
                    int index_i=-1;
                    while (line != null) {
                        int start=0;
                        for (int i = 0; i < fieldType.length - 1; i++) {
                            String tempStr = fieldType[i].convertFromBinary(line, start);
                            if (i == 0) {
                                time_s = tempStr;
                                index_i = getIndexSec(time_s);
                            }
                            if(i==1){
                                exchange_s = tempStr;
                                exchange = exchanges.get(exchange_s);
                            }
                            if (i == 4) {
                                time_series_arr[index_i][0] = new BigInteger(time_s);
                                time_series_arr[index_i][exchange]= time_series_arr[index_i][exchange].add(BigInteger.valueOf(Long.parseLong(tempStr)));
                            }

                            start = start + fieldType[i].getLength();
                        }
                        line = br.readLine();
                        k++;
//                            if (k>10) {
//                                break;
//                            }
                    }
                    printBigInt(time_series_arr, start_ind, end_ind);
                    writeFile(time_series_arr, exchangesObj);
                }
            }
            closeStream();
        }catch (Exception e){
            System.out.println("Error: "+e);
            closeStream();
        }
    }

    private void closeStream(){
        try {
            br.close();
            zf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
