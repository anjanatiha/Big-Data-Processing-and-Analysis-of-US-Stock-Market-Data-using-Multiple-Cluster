package DataFieldType;

import java.io.Serializable;

/**
 * Created by anjana on 7/17/17.
 */
public class TAQ062016SpecNew implements ITAQSpec, Serializable {
    private int tradeFileEntryLength = 130;
    private int QuoteFileEntryLength = 133;
    private int NBBOFileEntryLength = 182;
    private String activeStartDate = "06062016";
    private String activeEndDate = "99999999";
    private String version = "2.2a";


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
    private IFieldType TradeThroughExemptIndicator;
    private IFieldType TradeID;
    private IFieldType TradeReportingFacilityTimestamp;
    private IFieldType OfferPrice;
    private IFieldType OfferSize;
    private IFieldType FINRABBOIndicator;
    private IFieldType BestBidFINRAMarketMakerID;
    private IFieldType BestOfferFINRAMarketMakerID;
    private IFieldType FINRAADFTimestamp;

    private IFieldType NationalBBOIndicator;
    private IFieldType FINRAADFMPIDAppendageIndicator;
    private IFieldType LULDBBOIndicator;
    private IFieldType FINRAADFMarketParticipantQuoteIndicator;


    public TAQ062016SpecNew() {
    }

    private void setTradeFileSpec() {
        this.Time = new TimeField(15);
        this.Exchange = new TextField(1);
        this.Symbol = new TextField(16);
        this.SaleCondition = new TextField(4);
        this.TradeVolume = new NumericField(10);
        this.TradePrice = new NumericField(21, 6);//decimal point there can cause confusion
        this.TradeStopStockIndicator = new TextField(1);
        this.TradeCorrectionIndicator = new NumericField(2, 'p');
        this.TradeSequenceNumber = new NumericField(16, 'p');
        this.TradeID = new TextField(20);
        this.SourceOfTrade = new TextField(1);
        this.TradeReportingFacility = new TextField(1);
        this.ParticipantTimestamp = new TimeField(15);//change errors
        this.TradeReportingFacilityTimestamp = new TimeField(15);
        this.TradeThroughExemptIndicator = new NumericField(1);
    }

    private void setNBBOFileSpec() {
        this.Time = new TimeField(15);
        this.Exchange = new TextField(1);
        this.Symbol = new TextField(16);
        this.BidPrice = new NumericField(21, 6);
        this.BidSize = new NumericField(10);
        this.OfferPrice = new NumericField(21, 6);
        this.OfferSize = new NumericField(7);
        this.QuoteCondition = new TextField(1);
        this.SequenceNumber = new NumericField(20, 'p');
        this.NationalBBOInd = new NumericField(1);

        this.FINRABBOIndicator = new NumericField(1);
        this.FINRAADFMPIDIndicator = new NumericField(1);
//        this.NationalBBOInd = new NumericField(1); //please check

        this.QuoteCancelCorrection = new TextField(1);
        this.SourceOfQuote = new TextField(1);
        this.NBBOQuoteCondition = new TextField(1);
        this.BestBidExchange = new TextField(1);
        this.BestBidPrice = new NumericField(21, 6);
        this.BestBidSize = new NumericField(10);
        this.BestBidFINRAMarketMakerID = new TextField(4);
        this.BestOfferExchange = new TextField(1);
        this.BestOfferPrice = new NumericField(21, 6);
        this.BestOfferSize = new NumericField(10);
        this.BestOfferFINRAMarketMakerID = new TextField(4);
        this.LULDIndicator = new TextField(1);
        this.LULDNBBOIndicator = new TextField(1);
        this.SIPGeneratedMessageIdentifier = new TextField(1);
        this.ParticipantTimestamp = new TimeField(15);
        this.FINRAADFTimestamp = new TimeField(15);
    }

    private void setQuoteFileSpec() {
        this.Time = new TimeField(15);
        this.Exchange = new TextField(1);
        this.Symbol = new TextField(16);
        this.BidPrice = new NumericField(211, 6);
        this.BidSize = new NumericField(10);
        this.OfferPrice = new NumericField(21, 6);
        this.OfferSize = new NumericField(10);
        this.SequenceNumber = new NumericField(20);
        this.NationalBBOIndicator = new NumericField(1);
        this.FINRABBOIndicator = new NumericField(1);
        this.FINRAADFMPIDAppendageIndicator = new NumericField(1);
        this.QuoteCancelCorrection = new TextField(1);
        this.SourceOfQuote = new TextField(1);
        this.RetailInterestIndicator = new TextField(1);
        this.ShortSaleRestrictionIndicator = new TextField(1);
        this.LULDBBOIndicator = new TextField(1);
        this.SIPGeneratedMessageIdentifier = new TextField(1);
        this.NationalBBOLULDIndicator = new TextField(1);
        this.ParticipantTimestamp = new TimeField(15);
        this.FINRAADFTimestamp = new TimeField(12);
        this.FINRAADFMarketParticipantQuoteIndicator = new TextField(1);
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

    public IFieldType[] getTradeFields() {
        setTradeFileSpec();
        this.tradeFileFields = new IFieldType[]{Time, Exchange, Symbol, SaleCondition, TradeVolume, TradePrice,
                TradeStopStockIndicator, TradeCorrectionIndicator, TradeSequenceNumber, TradeID,
                SourceOfTrade, TradeReportingFacility, ParticipantTimestamp, TradeReportingFacilityTimestamp
                , TradeThroughExemptIndicator};
        return this.tradeFileFields;
    }

    public IFieldType[] getNBBOFields() {
        setNBBOFileSpec();
        this.NBBOFileFields = new IFieldType[]{Time, Exchange, Symbol, BidPrice, BidSize, OfferPrice, OfferSize, QuoteCondition,
                BidExchange, AskExchange, SequenceNumber, NationalBBOInd, FINRABBOIndicator, FINRAADFMPIDIndicator,
                QuoteCancelCorrection, SourceOfQuote, NBBOQuoteCondition,
                BestBidExchange, BestBidPrice, BestBidSize, BestBidFINRAMarketMakerID, BestOfferExchange,
                BestBidMMDeskLocation, BestOfferExchange,
                BestOfferPrice, BestOfferSize, BestOfferFINRAMarketMakerID, LULDIndicator, LULDNBBOIndicator,
                SIPGeneratedMessageIdentifier, ParticipantTimestamp, FINRAADFTimestamp};
        return this.NBBOFileFields;
    }

    public IFieldType[] getQuoteFields() {
        setQuoteFileSpec();
        this.quoteFileFields = new IFieldType[]{Time, Exchange, Symbol, BidPrice, BidSize,
                OfferPrice, OfferSize, QuoteCondition, SequenceNumber, NationalBBOIndicator,
                FINRABBOIndicator, FINRAADFMPIDAppendageIndicator, QuoteCancelCorrection, SourceOfQuote,
                RetailInterestIndicator, ShortSaleRestrictionIndicator, LULDBBOIndicator,
                SIPGeneratedMessageIdentifier, NationalBBOLULDIndicator, ParticipantTimestamp,
                FINRAADFTimestamp, FINRAADFMarketParticipantQuoteIndicator};
        return this.quoteFileFields;
    }
}
