package DataFieldType;

public class TAQ2015newA {
        private static IFieldType Time = new TimeField(12);
        private static IFieldType Exchange = new TextField(1);
        private static IFieldType Symbol = new TextField(16);
        private static IFieldType ParticipantTimestamp = new TextField(12);
        private static IFieldType RRN = new TextField(8);
        private static IFieldType TRF = new TimeField(12);
        private static IFieldType LineChange = new TextField(2);

        private static IFieldType SaleCondition = new TextField(4);
        private static IFieldType TradeVolume = new NumericField(9);
        private static IFieldType TradePrice = new NumericField(11,4);
        private static IFieldType TradeStopStockIndicator = new TextField(1);
        private static IFieldType TradeCorrectionIndicator = new NumericField(2);
        private static IFieldType TradeSequenceNumber = new NumericField(16);
        private static IFieldType SourceOfTrade = new TextField(1);
        private static IFieldType TradeReportingFacility = new TextField(1);


        private static IFieldType BidPrice = new NumericField(11,4);
        private static IFieldType BidSize = new NumericField(7);
        private static IFieldType AskPrice = new NumericField(11,4);
        private static IFieldType AskSize = new NumericField(7);
        private static IFieldType QuoteCondition = new TextField(1);
        private static IFieldType MarketMaker = new NumericField(4);
        private static IFieldType BidExchange = new TextField(1);
        private static IFieldType AskExchange = new TextField(1);
        private static IFieldType SequenceNumber = new NumericField(16);
        private static IFieldType NationalBBOInd = new NumericField(1);
        private static IFieldType NASDAQBBOInd = new NumericField(1);
        private static IFieldType QuoteCancelCorrection = new TextField(1);
        private static IFieldType SourceOfQuote = new TextField(1);
        private static IFieldType NBBOQuoteCondition = new TextField(1);
        private static IFieldType BestBidExchange = new TextField(1);
        private static IFieldType BestBidPrice = new NumericField(11,4);
        private static IFieldType BestBidSize = new NumericField(7);
        private static IFieldType BestBidMarketMaker = new TextField(4);
        private static IFieldType BestBidMMLocation = new TextField(2);
        private static IFieldType BestBidMMDeskLocation = new TextField(1);
        private static IFieldType BestOfferExchange = new TextField(1);
        private static IFieldType BestOfferPrice = new NumericField(11,4);
        private static IFieldType BestOfferSize = new NumericField(7);
        private static IFieldType BestOfferMarketMaker = new TextField(4);
        private static IFieldType BestOfferMMLocation = new TextField(2);
        private static IFieldType BestOfferMMDeskLocation = new TextField(1);

        private static IFieldType LULDIndicator = new TextField(1);
        private static IFieldType LULDNBBOIndicator = new TextField(1);
        private static IFieldType SIPGeneratedMessageIdentifier = new TextField(1);


        public static IFieldType[] NBBO ={Time,Exchange,Symbol,BidPrice,BidSize,AskPrice,AskSize,QuoteCondition,MarketMaker,
                BidExchange,AskExchange,SequenceNumber,NationalBBOInd,NASDAQBBOInd,QuoteCancelCorrection,SourceOfQuote,NBBOQuoteCondition,
                BestBidExchange,BestBidPrice,BestBidSize,BestBidMarketMaker,BestBidMMLocation,BestBidMMDeskLocation,BestOfferExchange,
                BestOfferPrice,BestOfferSize,BestOfferMarketMaker,BestOfferMMLocation,BestOfferMMDeskLocation,LULDIndicator,LULDNBBOIndicator,
                SIPGeneratedMessageIdentifier,ParticipantTimestamp,RRN,TRF,LineChange
        };

        public static IFieldType[] TradeFormat ={Time,Exchange,Symbol,SaleCondition,TradeVolume,TradePrice,
                TradeStopStockIndicator,TradeCorrectionIndicator,TradeSequenceNumber,
                SourceOfTrade,TradeReportingFacility,ParticipantTimestamp,RRN,TRF,LineChange
        };
}
