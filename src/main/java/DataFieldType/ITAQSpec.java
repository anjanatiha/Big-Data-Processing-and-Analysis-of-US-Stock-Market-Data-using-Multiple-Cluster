package DataFieldType;

import java.io.Serializable;

public interface ITAQSpec extends Serializable {
    public int tradeFileEntryLength = 0;
    public int QuoteFileEntryLength = 0;
    public int NBBOFileEntryLength = 0;
    public String activeStartDate = "None";
    public String activeEndDate = "None";

    public int getTradeFieldsLength();

    public int getQuoteFieldsLength();

    public int getNBBOFieldsLength();
    public IFieldType[] getTradeFields();

    public IFieldType[] getNBBOFields();

    public IFieldType[] getQuoteFields();
}
