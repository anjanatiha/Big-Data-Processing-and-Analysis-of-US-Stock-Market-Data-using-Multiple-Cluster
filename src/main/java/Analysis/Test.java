package Analysis;

import static Misc.FileClass.getFirstLine;
import static Misc.Print.print;

public class Test{
    public static void main(String[] args){
        String firstLine = getFirstLine("/home/anjana/Downloads/DATA/EQY_US_ALL_TRADE_20160506.zip");
        print(firstLine);
    }
}
