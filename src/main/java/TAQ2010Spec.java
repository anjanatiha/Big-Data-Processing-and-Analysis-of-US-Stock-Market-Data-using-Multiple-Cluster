import java.io.Serializable;

/**
 * Created by Anjana on 6/21/2017.
 */
public class TAQ2010Spec implements Serializable {
    private int tradeFileEntryLength = 73;
    private int QuoteFileEntryLength = 91;
    private int NBBOFileEntryLength = 144;
    TAQ2010Spec(){}

    public int getTradeFieldsLength(){return tradeFileEntryLength;}
    public int getQuoteFieldsLength(){
        return QuoteFileEntryLength;
    }
    public int getNBBOFieldsLength(){
        return NBBOFileEntryLength;
    }

    public IFieldType[] getTradeFields(){
        IFieldType[] tradeFileFields = {new TimeField(9), new TextField(1), new TextField(16),
                new TextField(4), new NumericField(9), new NumericField(11, 4),
                new TextField(1), new NumericField(2, 'p'), new NumericField(16, 'p'),
                new TextField(1), new TextField(1), new TextField(2)};
        return tradeFileFields;
    }

    public IFieldType[] getQuoteFields(){
        IFieldType[] quoteFileFields = {new TimeField(9), new TextField(1),
                new TextField(16), new NumericField(11, 4), new NumericField(7),
                new NumericField(11, 4), new NumericField(7), new TextField(1),
                new NumericField(4, 'p'), new TextField(1), new TextField(1),
                new TextField(4), new NumericField(16, 'p'), new NumericField(1),
                new TextField(1), new TextField(1), new TextField(2)};
        return quoteFileFields;
    }

    public IFieldType[] getNBBOFields(){
        IFieldType[] NBBOFileFields = {new TimeField(9), new TextField(1), new TextField(16),
                new NumericField(11,4), new NumericField(7),
                new NumericField(11,4), new NumericField(7),new TextField(1),
                new NumericField(4), new TextField(1), new TextField(1),new NumericField(16),
                new NumericField(1),new NumericField(1), new TextField(1), new TextField(1),
                new TextField(1),new TextField(1), new NumericField(11, 4),
                new NumericField(7), new TextField(4), new TextField(2), new TextField(1),
                new TextField(1), new NumericField(11, 4),new NumericField(7),
                new TextField(4), new TextField(2),new TextField(1), new TextField(2)};
        return NBBOFileFields;
    }

}
