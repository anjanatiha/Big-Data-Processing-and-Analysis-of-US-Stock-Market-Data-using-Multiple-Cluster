package Analysis;

import static Misc.FileClass.printFirstLineZip;
import static Misc.Print.print;

public class Test{
    public static void main(String[] args){
        String firstLine = printFirstLineZip("/home/anjana/Downloads/DATA/EQY_US_ALL_TRADE_20160506.zip");
        print(firstLine);
    }
}
