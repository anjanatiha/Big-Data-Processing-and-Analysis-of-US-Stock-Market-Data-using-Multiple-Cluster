package DataFieldType;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Anjana on 6/27/2017.
 */
public class StockExchanges implements Serializable {
    private static HashMap<String, Integer> exchanges_map = new HashMap<>();
    private static String[] exchange_names = {"NYSE AMEX(A)","NASDAQ OMX BX(B)", "National(C)",
            "FINRA(D)", "International Securities(I)", "Direct Edge A(J)","Direct Edge X(K)",
            "Chicago(M)", "New York(N)","NASDAQ OMX(T)", "NYSE Arca SM(P)",
            "Consolidated Tape(S)","NASDAQ(Q)", "CBOE(W)", "NASDAQ OMX PSX(X)",
            "BATS Y(Y)", "BATS Z(Z)"};
    public StockExchanges(){
        exchanges_map.put("A", 1);
        exchanges_map.put("B", 2);
        exchanges_map.put("C", 3);
        exchanges_map.put("D", 4);
        exchanges_map.put("I", 5);
        exchanges_map.put("J", 6);
        exchanges_map.put("K", 7);
        exchanges_map.put("M", 8);
        exchanges_map.put("N", 9);
        exchanges_map.put("T", 10);
        exchanges_map.put("P", 11);
        exchanges_map.put("S", 12);
        exchanges_map.put("Q", 13);
        exchanges_map.put("W", 14);
        exchanges_map.put("X", 15);
        exchanges_map.put("Y", 16);
        exchanges_map.put("Z", 17);


//        exchanges_map = {'A': 1, 'B': 2, 'C': 3, 'D': 4, 'I': 5, 'J': 6, 'K': 7, 'M': 8, 'N':9, 'T':10, 'P':11, 'S':12,
//                'Q':13, 'W':14, 'X':15, 'Y':16, 'Z':17}
//
//        exchange_names = {'A' : "NYSE AMEX Stock Exchange(A)", 'B': "NASDAQ OMX BX Stock Exchange(B)",
//                'C' : "National Stock Exchange(C)", 'D': "FINRA(D)", 'I' : "International Securities Exchange(I)",
//                'J' : "Direct Edge A Stock Exchange(J)", 'K' : "Direct Edge X Stock Exchange(K)",
//                'M' : "Chicago Stock Exchange(M)", 'N' : "New York Stock Exchange(N)",
//                'T' : "NASDAQ OMX Stock Exchange(T)", 'P' : "NYSE Arca SM(P)",'S' : "Consolidated Tape System(S)",
//                "Q" : "NASDAQ Stock Exchange(Q)", 'W' : "CBOE Stock Exchange(W)",
//                'X' : "NASDAQ OMX PSX Stock Exchange(X)", 'Y' : "BATS Y-Exchange(Y)", 'Z' : "BATS Exchange(Z)"}
    }

    public HashMap<String, Integer> getExchanges_map(){
        return exchanges_map;
    }
    public String[]  getExchangeNames(){
        return exchange_names;
    }


}
