import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static Misc.FileProperties.columnSelect;
import static Misc.FileProperties.wordCollect;
import static Misc.Print.print;
import static Misc.SystemProperties.getMaxMemorySize;

public class FileAttributes implements Serializable {
    private String startTime;
    private String endTime;
    private List<String> tickerSymbols;
    private List<Integer> columnList;
    private boolean filterTickers = false;
    private boolean filterTime = false;
    private boolean filterColumns = false;
    private static JavaSparkContext sc;
    private int clusterSize = -1;
    private String calcType = "";
    private Integer calcTypeInt = -1;
    private HashMap<String, Integer> exchageMap;
    private int selectColumn=-1;
    private String memorySize= "1g";

    FileAttributes() {
        setMemory();
        setConversionAttribute();
    }
    public void setConversionAttribute(){
        Scanner scan = new Scanner(System.in);
        print("Enter start time in HHMM format");
        this.startTime = scan.next();
        if (!this.startTime.equals("n")) {
            this.filterTime = true;
            print("Enter end time in HHMM format");
            this.endTime = scan.next();
        }
        print("Enter file containing stock symbols");
        String stockFile = scan.next();
        if (!stockFile.equals("n")) {
            this.tickerSymbols = wordCollect(sc, stockFile);
            this.filterTickers = true;
        }
        print("Enter file containing selected columns");
        String columnFile = scan.next();
        if (!columnFile.equals("n")) {
            this.columnList = columnSelect(sc, columnFile);
            this.filterColumns = true;
        }
        print("Select partition size(-1 for default or any other positive number)");
        int clusterSize = scan.nextInt();
        if (clusterSize!=-1)
            this.clusterSize = clusterSize;
        this.exchageMap = getExchageMap();
    }

    public void setMemory(){
        int memMin = 1;
        int memMax = getMaxMemorySize();
        System.out.println("\nPlease specify Memory Size for spark in GB (-1 to ignore):\n(Please consider leaving enough memory space for operating system to function properly which is usually 4-6GB)");
        Scanner scan = new Scanner(System.in);
        int memSizeNum = scan.nextInt();
        String memorySize;
        if (memSizeNum != -1 && memSizeNum <= memMax)
            this.memorySize = String.valueOf(memSizeNum) + "g";
        else
            memorySize = "1g";
    }
    public String getMemorySize(){
        return memorySize;
    }
    public String getStartTime(){
        return startTime;
    }
    public String getEndTime(){
        return endTime;
    }
    public boolean getfilterTime(){
        return filterTime;
    }
    public List<String> getTickerSymbols(){
        return tickerSymbols;
    }
    public List<Integer> getColumnList(){
        return columnList;
    }
    public boolean getFilterTickers(){
        return filterTickers;
    }
    public boolean getFilterColumn(){
        return filterColumns;
    }
    public String getCalcType(){ return calcType; }
    public int getCalcTypeInt(){
        return calcTypeInt;
    }
    public int getSelectColumn(){
        return selectColumn;
    }
    public HashMap<String, Integer> getExchageMap() {return exchageMap; }
    public int getClusterSize() {return clusterSize; }
}
