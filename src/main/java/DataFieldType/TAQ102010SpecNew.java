package DataFieldType;

import java.io.Serializable;

/**
 * Created by Anjana on 6/21/2017.
 */
public class TAQ102010SpecNew implements ITAQSpec, Serializable {
    private int tradeFileEntryLength = 73;
    private int QuoteFileEntryLength = 91;
    private int NBBOFileEntryLength = 144;
    private String activeStartDate = "02102006";
    private String activeEndDate = "31072012";
    private String Version = "1.0";

    private IFieldType[] tradeFileFields;
    private IFieldType[] NBBOFileFields;
    private IFieldType[] quoteFileFields;


    private IFieldType Time;
    private IFieldType Exchange;
    private IFieldType Symbol;
    private IFieldType SaleCondition;
    private IFieldType TradeVolume;
    private IFieldType TradePrice;
    private IFieldType TradeStopStockIndicator;
    private IFieldType TradeCorrectionIndicator;
    private IFieldType TradeSequenceNumber;
    private IFieldType SourceOfTrade;
    private IFieldType TradeReportingFacility;
    private IFieldType LineChange;

    private IFieldType BidPrice;
    private IFieldType BidSize;
    private IFieldType AskPrice;
    private IFieldType AskSize;
    private IFieldType QuoteCondition;
    private IFieldType MarketMaker;
    private IFieldType BidExchange;
    private IFieldType AskExchange;
    private IFieldType SequenceNumber;
    private IFieldType NationalBBOInd;
    private IFieldType NASDAQBBOInd;
    private IFieldType QuoteCancelCorrection;
    private IFieldType SourceOfQuote;
    private IFieldType NBBOQuoteCondition;
    private IFieldType BestBidExchange;
    private IFieldType BestBidPrice;
    private IFieldType BestBidSize;
    private IFieldType BestBidMarketMaker;
    private IFieldType BestBidMMLocation;
    private IFieldType BestBidMMDeskLocation;
    private IFieldType BestAskExchange;
    private IFieldType BestAskPrice;
    private IFieldType BestAskSize;
    private IFieldType BestAskMarketMaker;
    private IFieldType BestAskMMLocation;
    private IFieldType BestAskMMDeskLocation;


    public TAQ102010SpecNew() {
    }

    private void setTradeFileSpec() {
        this.Time = new TimeField(9);
        this.Exchange = new TextField(1);
        this.Symbol = new TextField(16);
        this.SaleCondition = new TextField(4);
        this.TradeVolume = new NumericField(9);
        this.TradePrice = new NumericField(11, 4);
        this.TradeStopStockIndicator = new TextField(1);
        this.TradeCorrectionIndicator = new NumericField(2, 'p');
        this.TradeSequenceNumber = new NumericField(16, 'p');
        this.SourceOfTrade = new TextField(1);
        this.TradeReportingFacility = new TextField(1);
        this.LineChange = new TextField(2);
    }

    private void setNBBOFileSpec() {
        this.Time = new TimeField(9);
        this.Exchange = new TextField(1);
        this.Symbol = new TextField(16);
        this.BidPrice = new NumericField(11, 4);
        this.BidSize = new NumericField(7);
        this.AskPrice = new NumericField(11, 4);
        this.AskSize = new NumericField(7);
        this.QuoteCondition = new TextField(1);
        this.MarketMaker = new NumericField(4);
        this.BidExchange = new TextField(1);
        this.AskExchange = new TextField(1);
        this.SequenceNumber = new NumericField(16, 'p');
        this.NationalBBOInd = new NumericField(1);
        this.NASDAQBBOInd = new NumericField(1);
        this.QuoteCancelCorrection = new TextField(1);
        this.SourceOfQuote = new TextField(1);
        this.NBBOQuoteCondition = new TextField(1);
        this.BestBidExchange = new TextField(1);
        this.BestBidPrice = new NumericField(11, 4);
        this.BestBidSize = new NumericField(7);
        this.BestBidMarketMaker = new TextField(4);
        this.BestBidMMLocation = new TextField(2);
        this.BestBidMMDeskLocation = new TextField(1);
        this.BestAskExchange = new TextField(1);
        this.BestAskPrice = new NumericField(11, 4);
        this.BestAskSize = new NumericField(7);
        this.BestAskMarketMaker = new TextField(4);
        this.BestAskMMLocation = new TextField(2);
        this.BestAskMMDeskLocation = new TextField(1);
        this.LineChange = new TextField(2);

    }

    private void setQuoteFileSpec() {
        this.Time = new TimeField(9);
        this.Exchange = new TextField(1);
        this.Symbol = new TextField(16);
        this.BidPrice = new NumericField(11, 4);
        this.BidSize = new NumericField(7);
        this.AskPrice = new NumericField(11, 4);
        this.AskSize = new NumericField(7);
        this.QuoteCondition = new TextField(1);
        this.MarketMaker = new NumericField(4);
        this.BidExchange = new TextField(1);
        this.AskExchange = new TextField(1);
        this.SequenceNumber = new NumericField(16, 'p');
        this.NationalBBOInd = new NumericField(1);
        this.NASDAQBBOInd = new NumericField(1);
        this.QuoteCancelCorrection = new TextField(1);
        this.SourceOfQuote = new TextField(1);
        this.LineChange = new TextField(2);

    }

    public IFieldType[] getTradeFields() {
        setTradeFileSpec();
        this.tradeFileFields = new IFieldType[]{Time, Exchange, Symbol, SaleCondition, TradeVolume, TradePrice,
                TradeStopStockIndicator, TradeCorrectionIndicator, TradeSequenceNumber,
                SourceOfTrade, TradeReportingFacility, LineChange};
        return this.tradeFileFields;
    }

    public IFieldType[] getQuoteFields() {
        setQuoteFileSpec();
        this.quoteFileFields = new IFieldType[]{Time, Exchange, Symbol, BidPrice, BidSize, AskPrice, AskSize, QuoteCondition, MarketMaker,
                BidExchange, AskExchange, SequenceNumber, NationalBBOInd, NASDAQBBOInd, QuoteCancelCorrection, SourceOfQuote, LineChange};
        return this.quoteFileFields;
    }

    public IFieldType[] getNBBOFields() {
        setNBBOFileSpec();
        this.NBBOFileFields = new IFieldType[]{Time, Exchange, Symbol, BidPrice, BidSize, AskPrice, AskSize, QuoteCondition, MarketMaker,
                BidExchange, AskExchange, SequenceNumber, NationalBBOInd, NASDAQBBOInd, QuoteCancelCorrection, SourceOfQuote, NBBOQuoteCondition,
                BestBidExchange, BestBidPrice, BestBidSize, BestBidMarketMaker, BestBidMMLocation, BestBidMMDeskLocation, BestAskExchange,
                BestAskPrice, BestAskSize, BestAskMarketMaker, BestAskMMLocation, BestAskMMDeskLocation, LineChange};
        return this.NBBOFileFields;
    }

    public int getTradeFieldsLength() {
        return this.tradeFileEntryLength;
    }

    public int getQuoteFieldsLength() {
        return this.QuoteFileEntryLength;
    }

    public int getNBBOFieldsLength() {
        return this.NBBOFileEntryLength;
    }

}
