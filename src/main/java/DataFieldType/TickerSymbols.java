package DataFieldType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by anjana on 7/16/17.
 */
public class TickerSymbols {
    public static String[] tickerSymbolsArr = {"AAPL", "AMZN","FB", "GOOGL", "MSFT"};
    public static List<String> tickerSymbolsObj = Arrays.asList(tickerSymbolsArr);
    public static List<String> getTickers(){return tickerSymbolsObj;}
}
