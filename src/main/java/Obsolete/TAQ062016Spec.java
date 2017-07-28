//package Obsolete;
//
//import DataFieldType.*;
//
//import java.io.Serializable;
//
///**
// * Created by anjana on 7/17/17.
// */
//public class TAQ062016Spec implements ITAQSpec, Serializable {
//    private int tradeFileEntryLength = 130;
//    private int QuoteFileEntryLength = 133;
//    private int NBBOFileEntryLength = 182;
//    private String activeStartDate = "06062016";
//    private String activeEndDate = "99999999";
//
//    public TAQ062016Spec() {
//    }
//
//    public int getTradeFieldsLength() {
//        return tradeFileEntryLength;
//    }
//
//    public int getQuoteFieldsLength() {
//        return QuoteFileEntryLength;
//    }
//
//    public int getNBBOFieldsLength() {
//        return NBBOFileEntryLength;
//    }
//
//    public IFieldType[] getTradeFields() {
//        IFieldType[] tradeFileFields = {new TimeField(15), new TextField(1), new TextField(16),
//                new TextField(4), new TextField(4), new NumericField(10), new NumericField(21, 6),
//                new TextField(1), new NumericField(2, 'p'), new NumericField(16, 'p'),
//                new NumericField(20, 'p'), new TextField(1), new TextField(1), new TimeField(15), new TimeField(15),
//                new NumericField(1)};
//        return tradeFileFields;
//    }
//
//    public IFieldType[] getNBBOFields() {
//        IFieldType[] NBBOFileFields = {new TimeField(15), new TextField(1), new TextField(16),
//                new NumericField(21, 1), new NumericField(10),
//                new NumericField(21, 1), new NumericField(10), new TextField(1),
//                new NumericField(20, 'p'), new NumericField(1), new NumericField(1), new NumericField(1),
//                new TextField(1), new TextField(1), new TextField(1), new TextField(1),
//                new NumericField(21, 1), new NumericField(10),
//                new TextField(4), new TextField(1), new NumericField(21, 1), new NumericField(10), new TextField(4), new TextField(1),
//                new TextField(1), new TextField(1), new TimeField(15), new TimeField(15)};
//        return NBBOFileFields;
//    }
//
//    public IFieldType[] getQuoteFields() {
//        IFieldType[] quoteFileFields = {new TimeField(15), new TextField(1),
//                new TextField(16), new NumericField(21, 1), new NumericField(10),
//                new NumericField(21, 1), new NumericField(10), new TextField(1),
//                new TextField(1), new NumericField(20, 'p'), new NumericField(1),
//                new NumericField(1), new NumericField(1), new TextField(1), new TextField(1), new TextField(1),
//                new TextField(1), new TextField(1),
//                new TextField(1), new TextField(1), new TextField(1),
//                new TextField(1),
//                new TimeField(15), new TimeField(15), new TextField(1)};
//        return quoteFileFields;
//    }
//}
