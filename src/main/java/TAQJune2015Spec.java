import java.io.Serializable;

/**
 * Created by Anjana on 6/21/2017.
 */
public class TAQJune2015Spec implements Serializable {
    private int tradeFileEntryLength = 108;
    private int QuoteFileEntryLength = 133;
    private int NBBOFileEntryLength = 182;
    TAQJune2015Spec(){}

    public int getTradeFieldsLength(){
        return tradeFileEntryLength;
    }
    public int getQuoteFieldsLength(){
        return QuoteFileEntryLength;
    }
    public int getNBBOFieldsLength(){
        return NBBOFileEntryLength;
    }

    public IFieldType[] getTradeFields(){
        IFieldType[] tradeFileFields = {new TimeField(12), new TextField(1), new TextField(16),
                new TextField(4), new NumericField(9), new NumericField(11, 4),
                new TextField(1), new NumericField(2, 'p'), new NumericField(16, 'p'),
                new TextField(1), new TextField(1), new TimeField(12), new TextField(8),
                new TimeField(12), new TextField(2)};
        return tradeFileFields;
    }

    public IFieldType[] getNBBOFields(){
        IFieldType[] NBBOFileFields = {new TimeField(12), new TextField(1), new TextField(16),
                new NumericField(11,4), new NumericField(7),
                new NumericField(11,4), new NumericField(7),new TextField(1),
                new NumericField(4, 'b'), new TextField(1), new TextField(1), new NumericField(16, 'p'),
                new NumericField(1),new NumericField(1), new TextField(1), new TextField(1),
                new TextField(1),new TextField(1), new NumericField(11, 4),
                new NumericField(7), new TextField(4), new TextField(2), new TextField(1),
                new TextField(1), new NumericField(11, 4),new NumericField(7),
                new TextField(4), new TextField(2),new TextField(1), new TextField(1),
                new TextField(1),new TextField(1),new TimeField(12), new TextField(8),
                new TimeField(12), new TextField(2)};
        return NBBOFileFields;
    }

    public IFieldType[] getQuoteFields(){
        IFieldType[] quoteFileFields = {new TimeField(12), new TextField(1),
                new TextField(16), new NumericField(11, 4), new NumericField(7),
                new NumericField(11, 4), new NumericField(7), new TextField(1),
                new NumericField(4),
                new TextField(1),new TextField(1),new NumericField(16,'p'),new NumericField(1),
                new NumericField(1), new TextField(1),  new TextField(1), new TextField(1),
                new TextField(1), new TextField(1),
                new TextField(1),new TextField(1),new TextField(1),
                new TextField(1),
                new TimeField(12), new TextField(8), new TimeField(12),
                new TextField(2)};
        return quoteFileFields;
    }

}
