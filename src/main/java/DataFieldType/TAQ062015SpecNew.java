package DataFieldType;

public class TAQ062015SpecNew {
    private int tradeFileEntryLength = 108;
    private int QuoteFileEntryLength = 133;
    private int NBBOFileEntryLength = 182;
    private String activeStartDate = "01062015";
    private String activeEndDate = "None";

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
    private IFieldType ParticipantTimestamp;
    private IFieldType RegionalReferenceNumber;
    private IFieldType TradeReportingFacilitTimestamp;
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
    private IFieldType BestOfferExchange;
    private IFieldType BestOfferPrice;
    private IFieldType BestOfferSize;
    private IFieldType BestOfferMarketMaker;
    private IFieldType BestOfferMMLocation;
    private IFieldType BestOfferMMDeskLocation;

    private IFieldType LULDIndicator;
    private IFieldType LULDNBBOIndicator;
    private IFieldType RetailInterestIndicator;
    private IFieldType ShortSaleRestrictionIndicator;
    private IFieldType LULDBBOIndicatorCQS;
    private IFieldType LULDBBOIndicatorUTP;
    private IFieldType FINRAADFMPIDIndicator;
    private IFieldType NationalBBOLULDIndicator;
    private IFieldType SIPGeneratedMessageIdentifier;

    TAQ062015SpecNew() {

    }


    private void setTradeFileSpec() {
        this.Time = new TimeField(12);
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
        this.ParticipantTimestamp = new TextField(12);
        this.RegionalReferenceNumber = new TextField(8);
        this.TradeReportingFacilitTimestamp = new TimeField(12);
        this.LineChange = new TextField(2);
    }

    private void setNBBOFileSpec() {
        this.Time = new TimeField(12);
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
        this.BestOfferExchange = new TextField(1);
        this.BestOfferPrice = new NumericField(11, 4);
        this.BestOfferSize = new NumericField(7);
        this.BestOfferMarketMaker = new TextField(4);
        this.BestOfferMMLocation = new TextField(2);
        this.BestOfferMMDeskLocation = new TextField(1);
        this.LULDIndicator = new TextField(1);
        this.LULDNBBOIndicator = new TextField(1);
        this.SIPGeneratedMessageIdentifier = new TextField(1);
        this.ParticipantTimestamp = new TimeField(12);
        this.RegionalReferenceNumber = new TextField(8);
        this.TradeReportingFacilitTimestamp = new TimeField(12);
        this.LineChange = new TextField(2);

    }

    private void setQuoteFileSpec() {
        this.Time = new TimeField(12);
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
        this.RetailInterestIndicator = new TextField(1);
        this.ShortSaleRestrictionIndicator = new TextField(1);
        this.LULDBBOIndicatorCQS = new TextField(1);
        this.LULDBBOIndicatorUTP = new TextField(1);
        this.FINRAADFMPIDIndicator = new TextField(1);
        this.SIPGeneratedMessageIdentifier = new TextField(1);
        this.NationalBBOLULDIndicator = new TextField(1);
        this.RegionalReferenceNumber = new TextField(8);
        this.TradeReportingFacilitTimestamp = new TimeField(12);
        this.LineChange = new TextField(2);

    }

    public IFieldType[] getTradeFields() {
        setTradeFileSpec();
        this.tradeFileFields = new IFieldType[]{Time, Exchange, Symbol, SaleCondition, TradeVolume, TradePrice,
                TradeStopStockIndicator, TradeCorrectionIndicator, TradeSequenceNumber,
                SourceOfTrade, TradeReportingFacility, ParticipantTimestamp, RegionalReferenceNumber
                , TradeReportingFacilitTimestamp, LineChange};
        return this.tradeFileFields;
    }

    public IFieldType[] getQuoteFields() {
        setQuoteFileSpec();
        this.quoteFileFields = new IFieldType[]{Time, Exchange, Symbol, BidPrice, BidSize, AskPrice, AskSize, QuoteCondition, MarketMaker,
                BidExchange, AskExchange, SequenceNumber, NationalBBOInd, NASDAQBBOInd, QuoteCancelCorrection, SourceOfQuote, RetailInterestIndicator,
                ShortSaleRestrictionIndicator, LULDBBOIndicatorCQS, LULDBBOIndicatorUTP, FINRAADFMPIDIndicator, SIPGeneratedMessageIdentifier, NationalBBOLULDIndicator, ParticipantTimestamp,
                RegionalReferenceNumber, TradeReportingFacilitTimestamp, LineChange};
        return this.quoteFileFields;
    }

    public IFieldType[] getNBBOFields() {
        setNBBOFileSpec();
        this.NBBOFileFields = new IFieldType[]{Time, Exchange, Symbol, BidPrice, BidSize, AskPrice, AskSize, QuoteCondition, MarketMaker,
                BidExchange, AskExchange, SequenceNumber, NationalBBOInd, NASDAQBBOInd, QuoteCancelCorrection, SourceOfQuote, NBBOQuoteCondition,
                BestBidExchange, BestBidPrice, BestBidSize, BestBidMarketMaker, BestBidMMLocation, BestBidMMDeskLocation, BestOfferExchange,
                BestOfferPrice, BestOfferSize, BestOfferMarketMaker, BestOfferMMLocation, BestOfferMMDeskLocation, LULDIndicator, LULDNBBOIndicator,
                SIPGeneratedMessageIdentifier, ParticipantTimestamp, RegionalReferenceNumber, TradeReportingFacilitTimestamp, LineChange};
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
