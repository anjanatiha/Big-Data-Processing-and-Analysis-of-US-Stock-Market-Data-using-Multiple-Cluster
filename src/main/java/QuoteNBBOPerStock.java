/**
 * Created by Anjana on 5/29/2017.
 */
import java.io.*;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class QuoteNBBOPerStock {
    private String inputFileName;
    private String outputFileName;
    private ZipFile zf;
    private PrintStream outputStream;
    private File outputFile;
    private IFieldType[] fieldType;
    private int startOffset;
    private BufferedReader br;
    private long bufferSize;


    QuoteNBBOPerStock(String zipfile, String outputFileName){
        try {
            this.zf = new ZipFile(zipfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAttributes(int startOffset, IFieldType[] fieldType, long bufferSize){
        this.startOffset=startOffset;
        this.fieldType = fieldType;
        this.bufferSize = bufferSize;
    }
    public int getIndex(String time){
        int index = (Integer.parseInt(time.substring(0,2)))*3600+(Integer.parseInt(time.substring(2,4)))*60 + (Integer.parseInt(time.substring(4,6)));
        return index-1;
    }

    public int[][] initArr(int[][] list){
        for(int m=0; m<list.length;m++){
            for(int n=0; n<list[m].length;n++){
                list[m][n]=0;
            }
        }
        return list;
    }

    public void printMap(HashMap<String, BigDecimal[][]> mapObject){
        for (String name: mapObject.keySet()){
            String key = name.toString();
            BigDecimal[][] arr = mapObject.get(name);
            System.out.println(key);
            for(int m=0; m<arr.length;m++) {
                for (int n = 0; n < arr[m].length; n++) {
                    System.out.print(arr[m][n] + " ");
                }
                System.out.print("\n");
            }
            System.out.println("\n\n\n");
        }
    }

    public void writeFile(HashMap<String, BigDecimal[][]> mapObject){
        for (String name: mapObject.keySet()){
            String key = name.toString();
//            String outputFileName = inputFileName.substring(0,inputFileName.length()-4)+key+".txt";
            String outputFileName ="E:\\FinanceData\\"+key+".txt";
            this.outputFile = new File(outputFileName);
            try {
                this.outputStream = new PrintStream(outputFile);
                BigDecimal[][] arr = mapObject.get(name);
                StringBuilder tempLine = new StringBuilder();
                for(int m=0; m<arr.length;m++) {
                    for (int n = 0; n < arr[m].length; n++) {
                        tempLine.append(arr[m][n] + ",");
                    }
                    tempLine.append("\n");
                }
                tempLine.append("\n\n\n\n");
                outputStream.print(tempLine);
                outputStream.flush();
                tempLine.setLength(0);
                outputStream.close();
            } catch (FileNotFoundException e) {
                outputStream.close();
                e.printStackTrace();
            }

        }
    }

    public BigDecimal[][] initArr(BigDecimal[][] list){
        for(int m=0; m<list.length;m++){
            for(int n=0; n<list[m].length;n++){
                list[m][n]= BigDecimal.ZERO;
            }
        }
        return list;
    }
    public void QuoteNBBOPerStockAnalyzer(){
        try {
            Enumeration entries = zf.entries();
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            StockExchanges exchangesObj = new StockExchanges();
            HashMap<String, Integer> exchanges = exchangesObj.getExchanges_map();
            HashMap<String, BigDecimal[][]> QuoteNBBOPerStockObject = new HashMap<String, BigDecimal[][]>();

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
                    BigDecimal time = BigDecimal.ZERO;
                    int time_index = -1;
                    BigDecimal bid_price = BigDecimal.ZERO;
                    BigDecimal ask_price = BigDecimal.ZERO;
                    BigDecimal best_bid_price = BigDecimal.ZERO;
                    BigDecimal best_ask_price = BigDecimal.ZERO;
                    BigDecimal best_bid_exchange =  BigDecimal.ZERO;
                    BigDecimal best_ask_exchange =  BigDecimal.ZERO;
                    BigDecimal[][] time_series_arr = new BigDecimal[86400][40];;
                    int bid_exchange = -1;
                    int ask_exchange = -1;
                    String stock="";
                    while (line != null) {
                        int start=0;
                        for (int i = 0; i < fieldType.length - 1; i++) {
                            String tempStr = fieldType[i].convertFromBinary(line, start);
                            if (i == 0) {
                                time = BigDecimal.valueOf(Long.parseLong(tempStr));
                                time_index = getIndex(tempStr);
                            }
                            if (i == 2) {
                                stock = tempStr;
                            }
                            if(i==3){
                                bid_price = new BigDecimal(tempStr);
                            }
                            if (i == 5) {
                                ask_price = new BigDecimal(tempStr);
                            }
                            if(i == 9){
                                bid_exchange = exchanges.get(tempStr);
                            }
                            if (i == 10) {
                                ask_exchange = exchanges.get(tempStr);
                                if(!(QuoteNBBOPerStockObject.containsKey(stock))) {
                                    time_series_arr = new BigDecimal[86400][40];
                                    time_series_arr = initArr(time_series_arr);
                                    QuoteNBBOPerStockObject.put(stock, time_series_arr);
                                }
                                else {
                                    time_series_arr = QuoteNBBOPerStockObject.get(stock);
                                    time_series_arr[time_index][0] = time;
                                    time_series_arr[time_index][1] = best_bid_exchange;
                                    time_series_arr[time_index][2] = best_ask_exchange;
                                    time_series_arr[time_index][3] = best_bid_price;
                                    time_series_arr[time_index][4] = best_ask_price;
                                    time_series_arr[time_index][bid_exchange+4] = bid_price;
                                    time_series_arr[time_index][ask_exchange*2+4] = ask_price;
                                }
                            }
                            start = start + fieldType[i].getLength();
                        }

                        line = br.readLine();
                        k++;
                        if (k>100000) {
                            break;
                        }

                    }

//                    printMap(QuoteNBBOPerStockObject);
                    writeFile(QuoteNBBOPerStockObject);
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
