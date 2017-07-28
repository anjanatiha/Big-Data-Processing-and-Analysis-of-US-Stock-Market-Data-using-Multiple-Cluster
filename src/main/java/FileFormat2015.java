//public class FileFormat2015
//{
//        private static IFieldType Time = new TimeType(12);
//        private static IFieldType Exchange = new TextType(1);
//        private static IFieldType Symbol = new TextType(16);
//        private static IFieldType ParticipantTimestamp = new TimeType(12);
//        private static IFieldType RRN = new TextType(8);
//        private static IFieldType TRF = new TimeType(12);
//        private static IFieldType LineChange = new TextType(2);
//
//        private static IFieldType SaleCondition = new TextType(4);
//        private static IFieldType TradeVolume = new DecimalNumberType(9);
//        private static IFieldType TradePrice = new DecimalNumberType(11,4);
//        private static IFieldType TradeStopStockIndicator = new TextType(1);
//        private static IFieldType TradeCorrectionIndicator = new DecimalNumberType(2);
//        private static IFieldType TradeSequenceNumber = new DecimalNumberType(16);
//        private static IFieldType SourceOfTrade = new TextType(1);
//        private static IFieldType TradeReportingFacility = new TextType(1);
//
//
//        private static IFieldType BidPrice = new DecimalNumberType(11,4);
//        private static IFieldType BidSize = new DecimalNumberType(7);
//        private static IFieldType AskPrice = new DecimalNumberType(11,4);
//        private static IFieldType AskSize = new DecimalNumberType(7);
//        private static IFieldType QuoteCondition = new TextType(1);
//        private static IFieldType MarketMaker = new DecimalNumberType(4);
//        private static IFieldType BidExchange = new TextType(1);
//        private static IFieldType AskExchange = new TextType(1);
//        private static IFieldType SequenceNumber = new DecimalNumberType(16);
//        private static IFieldType NationalBBOInd = new DecimalNumberType(1);
//        private static IFieldType NASDAQBBOInd = new DecimalNumberType(1);
//        private static IFieldType QuoteCancelCorrection = new TextType(1);
//        private static IFieldType SourceOfQuote = new TextType(1);
//        private static IFieldType NBBOQuoteCondition = new TextType(1);
//        private static IFieldType BestBidExchange = new TextType(1);
//        private static IFieldType BestBidPrice = new DecimalNumberType(11,4);
//        private static IFieldType BestBidSize = new DecimalNumberType(7);
//        private static IFieldType BestBidMarketMaker = new TextType(4);
//        private static IFieldType BestBidMMLocation = new TextType(2);
//        private static IFieldType BestBidMMDeskLocation = new TextType(1);
//        private static IFieldType BestOfferExchange = new TextType(1);
//        private static IFieldType BestOfferPrice = new DecimalNumberType(11,4);
//        private static IFieldType BestOfferSize = new DecimalNumberType(7);
//        private static IFieldType BestOfferMarketMaker = new TextType(4);
//        private static IFieldType BestOfferMMLocation = new TextType(2);
//        private static IFieldType BestOfferMMDeskLocation = new TextType(1);
//
//        private static IFieldType LULDIndicator = new TextType(1);
//        private static IFieldType LULDNBBOIndicator = new TextType(1);
//        private static IFieldType SIPGeneratedMessageIdentifier = new TextType(1);
//
//
//        public static IFieldType[] NBBO ={Time,Exchange,Symbol,BidPrice,BidSize,AskPrice,AskSize,QuoteCondition,MarketMaker,
//                BidExchange,AskExchange,SequenceNumber,NationalBBOInd,NASDAQBBOInd,QuoteCancelCorrection,SourceOfQuote,NBBOQuoteCondition,
//                BestBidExchange,BestBidPrice,BestBidSize,BestBidMarketMaker,BestBidMMLocation,BestBidMMDeskLocation,BestOfferExchange,
//                BestOfferPrice,BestOfferSize,BestOfferMarketMaker,BestOfferMMLocation,BestOfferMMDeskLocation,LULDIndicator,LULDNBBOIndicator,
//                SIPGeneratedMessageIdentifier,ParticipantTimestamp,RRN,TRF,LineChange
//        };
//
//        public static IFieldType[] TradeFormat ={Time,Exchange,Symbol,SaleCondition,TradeVolume,TradePrice,
//                TradeStopStockIndicator,TradeCorrectionIndicator,TradeSequenceNumber,
//                SourceOfTrade,TradeReportingFacility,ParticipantTimestamp,RRN,TRF,LineChange
//        };
//}
