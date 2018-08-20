package com.idcg.idcw.model.bean;

import java.util.List;

/**
 * Created by hpz on 2018/4/8.
 */

public class TradeNewConfigBean {

    /**
     * Data : {"TradeVarietyList":[{"TradingConfigID":"_DSQ3BmslE-cS-HP3POlnA","BuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","SellerCoinID":"1xx3-k3olkGgIwEiPqwXMw","Symbol":"BTC/USD","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Bitcoin","SellerCoinCode":"BTC","BuyerCoinName":"US Dollar","BuyerCoinCode":"USD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":1.0E-4,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP4POlnA","BuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","SellerCoinID":"APqFnATs2U6oYPFxJjAooQ","Symbol":"ETH/USD","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Ethereum","SellerCoinCode":"ETH","BuyerCoinName":"US Dollar","BuyerCoinCode":"USD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP5POlnA","BuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","SellerCoinID":"CPqFnATs2U6oYPFxJjAooQ","Symbol":"LTC/USD","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Litecoin","SellerCoinCode":"LTC","BuyerCoinName":"US Dollar","BuyerCoinCode":"USD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP1POlnA","BuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","SellerCoinID":"8PqFnATs2U6oYPFxJjAooQ","Symbol":"BCH/USD","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Bitcoin Cash","SellerCoinCode":"BCH","BuyerCoinName":"US Dollar","BuyerCoinCode":"USD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP6POlnA","BuyerCoinID":"9PqFnATs2U6oYPFxJjAooQ","SellerCoinID":"1xx3-k3olkGgIwEiPqwXMw","Symbol":"BTC/VHKD","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Bitcoin","SellerCoinCode":"BTC","BuyerCoinName":"Virtual HKD","BuyerCoinCode":"VHKD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP7POlnA","BuyerCoinID":"9PqFnATs2U6oYPFxJjAooQ","SellerCoinID":"APqFnATs2U6oYPFxJjAooQ","Symbol":"ETH/VHKD","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Ethereum","SellerCoinCode":"ETH","BuyerCoinName":"Virtual HKD","BuyerCoinCode":"VHKD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP8POlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"APqFnATs2U6oYPFxJjAooQ","Symbol":"ETH/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Ethereum","SellerCoinCode":"ETH","BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-6,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPAPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"CPqFnATs2U6oYPFxJjAooQ","Symbol":"LTC/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Litecoin","SellerCoinCode":"LTC","BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-6,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP0POlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"8PqFnATs2U6oYPFxJjAooQ","Symbol":"BCH/BTC","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Bitcoin Cash","SellerCoinCode":"BCH","BuyerCoinName":null,"BuyerCoinCode":null,"MinBuyerCoinPrecision":1.0E-6,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPBPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"6LTTBZwKYUS7xbpaNjR2pg","Symbol":"TRX/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":null,"SellerCoinCode":null,"BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-8,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPCPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"vJPX2D8cWUGZYZJxctUfLw","Symbol":"OMG/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":null,"SellerCoinCode":null,"BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-6,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPDPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"LYP6LLotbkW2kjpSli7OrA","Symbol":"SNT/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":null,"SellerCoinCode":null,"BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-8,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPEPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"aBznMJxkA0SGHwzpyhBB8A","Symbol":"BAT/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":null,"SellerCoinCode":null,"BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-8,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0}],"Signalr":"http://192.168.1.251:8308","DefaultBuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","DefaultSellerCoinID":"1xx3-k3olkGgIwEiPqwXMw","DefaultTradeConfigID":"_DSQ3BmslE-cS-HP3POlnA","DefaultCurrencyID":"M716wsg8Q0eMmw-ksWkyjA","CNYExchangeRate":6.3044,"HKDExchangeRate":7.84842}
     * Status : true
     * Msg : null
     * Url : null
     * StatusCode : 200
     * Extra : null
     */

    private DataBean Data;
    private boolean Status;
    private Object Msg;
    private Object Url;
    private String StatusCode;
    private Object Extra;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    public Object getMsg() {
        return Msg;
    }

    public void setMsg(Object Msg) {
        this.Msg = Msg;
    }

    public Object getUrl() {
        return Url;
    }

    public void setUrl(Object Url) {
        this.Url = Url;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public Object getExtra() {
        return Extra;
    }

    public void setExtra(Object Extra) {
        this.Extra = Extra;
    }

    public static class DataBean {
        /**
         * TradeVarietyList : [{"TradingConfigID":"_DSQ3BmslE-cS-HP3POlnA","BuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","SellerCoinID":"1xx3-k3olkGgIwEiPqwXMw","Symbol":"BTC/USD","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Bitcoin","SellerCoinCode":"BTC","BuyerCoinName":"US Dollar","BuyerCoinCode":"USD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":1.0E-4,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP4POlnA","BuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","SellerCoinID":"APqFnATs2U6oYPFxJjAooQ","Symbol":"ETH/USD","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Ethereum","SellerCoinCode":"ETH","BuyerCoinName":"US Dollar","BuyerCoinCode":"USD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP5POlnA","BuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","SellerCoinID":"CPqFnATs2U6oYPFxJjAooQ","Symbol":"LTC/USD","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Litecoin","SellerCoinCode":"LTC","BuyerCoinName":"US Dollar","BuyerCoinCode":"USD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP1POlnA","BuyerCoinID":"M716wsg8Q0eMmw-ksWkyjA","SellerCoinID":"8PqFnATs2U6oYPFxJjAooQ","Symbol":"BCH/USD","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Bitcoin Cash","SellerCoinCode":"BCH","BuyerCoinName":"US Dollar","BuyerCoinCode":"USD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP6POlnA","BuyerCoinID":"9PqFnATs2U6oYPFxJjAooQ","SellerCoinID":"1xx3-k3olkGgIwEiPqwXMw","Symbol":"BTC/VHKD","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Bitcoin","SellerCoinCode":"BTC","BuyerCoinName":"Virtual HKD","BuyerCoinCode":"VHKD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP7POlnA","BuyerCoinID":"9PqFnATs2U6oYPFxJjAooQ","SellerCoinID":"APqFnATs2U6oYPFxJjAooQ","Symbol":"ETH/VHKD","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Ethereum","SellerCoinCode":"ETH","BuyerCoinName":"Virtual HKD","BuyerCoinCode":"VHKD","MinBuyerCoinPrecision":0.01,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP8POlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"APqFnATs2U6oYPFxJjAooQ","Symbol":"ETH/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Ethereum","SellerCoinCode":"ETH","BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-6,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPAPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"CPqFnATs2U6oYPFxJjAooQ","Symbol":"LTC/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Litecoin","SellerCoinCode":"LTC","BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-6,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HP0POlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"8PqFnATs2U6oYPFxJjAooQ","Symbol":"BCH/BTC","TradingType":0,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":"Bitcoin Cash","SellerCoinCode":"BCH","BuyerCoinName":null,"BuyerCoinCode":null,"MinBuyerCoinPrecision":1.0E-6,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPBPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"6LTTBZwKYUS7xbpaNjR2pg","Symbol":"TRX/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":null,"SellerCoinCode":null,"BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-8,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPCPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"vJPX2D8cWUGZYZJxctUfLw","Symbol":"OMG/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":null,"SellerCoinCode":null,"BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-6,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPDPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"LYP6LLotbkW2kjpSli7OrA","Symbol":"SNT/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":null,"SellerCoinCode":null,"BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-8,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0},{"TradingConfigID":"_DSQ3BmslE-cS-HPEPOlnA","BuyerCoinID":"1xx3-k3olkGgIwEiPqwXMw","SellerCoinID":"aBznMJxkA0SGHwzpyhBB8A","Symbol":"BAT/BTC","TradingType":2,"TradingStatus":1,"Newest":20,"BuyPrice":0,"BuyNum":0,"SellPrice":0,"SellNum":0,"SellerCoinName":null,"SellerCoinCode":null,"BuyerCoinName":"Bitcoin","BuyerCoinCode":"BTC","MinBuyerCoinPrecision":1.0E-8,"MinSellerCoinPrecision":0.001,"AvgPrice":0,"FastRose":0,"DisplayPrecision":0,"Rise":0,"Rose":0,"Open":0,"Close":0,"High":0,"Low":0,"Last24TradeQuantity":0}]
         * Signalr : http://192.168.1.251:8308
         * DefaultBuyerCoinID : M716wsg8Q0eMmw-ksWkyjA
         * DefaultSellerCoinID : 1xx3-k3olkGgIwEiPqwXMw
         * DefaultTradeConfigID : _DSQ3BmslE-cS-HP3POlnA
         * DefaultCurrencyID : M716wsg8Q0eMmw-ksWkyjA
         * CNYExchangeRate : 6.3044
         * HKDExchangeRate : 7.84842
         */

        private String DefaultBuyerCoinID;
        private String DefaultSellerCoinID;
        private String DefaultTradeConfigID;
        private String Signalr;
        private List<TradeVarietyListBean> TradeVarietyList;

        public String getDefaultBuyerCoinID() {
            return DefaultBuyerCoinID;
        }

        public void setDefaultBuyerCoinID(String DefaultBuyerCoinID) {
            this.DefaultBuyerCoinID = DefaultBuyerCoinID;
        }

        public String getDefaultSellerCoinID() {
            return DefaultSellerCoinID;
        }

        public void setDefaultSellerCoinID(String DefaultSellerCoinID) {
            this.DefaultSellerCoinID = DefaultSellerCoinID;
        }

        public String getDefaultTradeConfigID() {
            return DefaultTradeConfigID;
        }

        public void setDefaultTradeConfigID(String DefaultTradeConfigID) {
            this.DefaultTradeConfigID = DefaultTradeConfigID;
        }

        public String getSignalr() {
            return Signalr;
        }

        public void setSignalr(String Signalr) {
            this.Signalr = Signalr;
        }

        public List<TradeVarietyListBean> getTradeVarietyList() {
            return TradeVarietyList;
        }

        public void setTradeVarietyList(List<TradeVarietyListBean> TradeVarietyList) {
            this.TradeVarietyList = TradeVarietyList;
        }

        public static class TradeVarietyListBean {
            /**
             * TradingConfigID : _DSQ3BmslE-cS-HP3POlnA
             * BuyerCoinID : M716wsg8Q0eMmw-ksWkyjA
             * SellerCoinID : 1xx3-k3olkGgIwEiPqwXMw
             * Symbol : BTC/USD
             * TradingType : 0
             * TradingStatus : 1
             * Newest : 20
             * BuyPrice : 0
             * BuyNum : 0
             * SellPrice : 0
             * SellNum : 0
             * SellerCoinName : Bitcoin
             * SellerCoinCode : BTC
             * BuyerCoinName : US Dollar
             * BuyerCoinCode : USD
             * MinBuyerCoinPrecision : 0.01
             * MinSellerCoinPrecision : 1.0E-4
             * AvgPrice : 0
             * FastRose : 0
             * DisplayPrecision : 0
             * Rise : 0
             * Rose : 0
             * Open : 0
             * Close : 0
             * High : 0
             * Low : 0
             * Last24TradeQuantity : 0
             */

            private double AvgPrice;
            private double BuyNum;
            private double BuyPrice;
            private String BuyerCoinCode;
            private String BuyerCoinID;
            private String BuyerCoinName;
            private double Close;
            private double DisplayPrecision;
            private double FastRose;
            private double High;
            private double Low;
            private double MinBuyerCoinPrecision;
            private double MinSellerCoinPrecision;
            private double Newest;
            private double Open;
            private double Rise;
            private double Rose;
            private double SellNum;
            private double SellPrice;
            private double Last24TradeQuantity;
            private String SellerCoinCode;
            private String SellerCoinID;
            private String SellerCoinName;
            private String Symbol;
            private String TradingConfigID;
            private int TradingStatus;
            private int TradingType;

            private boolean isAddOptional;

            public double getLast24TradeQuantity() {
                return Last24TradeQuantity;
            }

            public void setLast24TradeQuantity(double last24TradeQuantity) {
                Last24TradeQuantity = last24TradeQuantity;
            }

            public boolean isAddOptional() {
                return isAddOptional;
            }

            public void setAddOptional(boolean addOptional) {
                isAddOptional = addOptional;
            }

            public double getAvgPrice() {
                return AvgPrice;
            }

            public void setAvgPrice(double AvgPrice) {
                this.AvgPrice = AvgPrice;
            }

            public double getBuyNum() {
                return BuyNum;
            }

            public void setBuyNum(double BuyNum) {
                this.BuyNum = BuyNum;
            }

            public double getBuyPrice() {
                return BuyPrice;
            }

            public void setBuyPrice(double BuyPrice) {
                this.BuyPrice = BuyPrice;
            }

            public String getBuyerCoinCode() {
                return BuyerCoinCode;
            }

            public void setBuyerCoinCode(String BuyerCoinCode) {
                this.BuyerCoinCode = BuyerCoinCode;
            }

            public String getBuyerCoinID() {
                return BuyerCoinID;
            }

            public void setBuyerCoinID(String BuyerCoinID) {
                this.BuyerCoinID = BuyerCoinID;
            }

            public String getBuyerCoinName() {
                return BuyerCoinName;
            }

            public void setBuyerCoinName(String BuyerCoinName) {
                this.BuyerCoinName = BuyerCoinName;
            }

            public double getClose() {
                return Close;
            }

            public void setClose(double Close) {
                this.Close = Close;
            }

            public double getDisplayPrecision() {
                return DisplayPrecision;
            }

            public void setDisplayPrecision(double DisplayPrecision) {
                this.DisplayPrecision = DisplayPrecision;
            }

            public double getFastRose() {
                return FastRose;
            }

            public void setFastRose(double FastRose) {
                this.FastRose = FastRose;
            }

            public double getHigh() {
                return High;
            }

            public void setHigh(double High) {
                this.High = High;
            }

            public double getLow() {
                return Low;
            }

            public void setLow(double Low) {
                this.Low = Low;
            }

            public double getMinBuyerCoinPrecision() {
                return MinBuyerCoinPrecision;
            }

            public void setMinBuyerCoinPrecision(double MinBuyerCoinPrecision) {
                this.MinBuyerCoinPrecision = MinBuyerCoinPrecision;
            }

            public double getMinSellerCoinPrecision() {
                return MinSellerCoinPrecision;
            }

            public void setMinSellerCoinPrecision(double MinSellerCoinPrecision) {
                this.MinSellerCoinPrecision = MinSellerCoinPrecision;
            }

            public double getNewest() {
                return Newest;
            }

            public void setNewest(double Newest) {
                this.Newest = Newest;
            }

            public double getOpen() {
                return Open;
            }

            public void setOpen(double Open) {
                this.Open = Open;
            }

            public double getRise() {
                return Rise;
            }

            public void setRise(double Rise) {
                this.Rise = Rise;
            }

            public double getRose() {
                return Rose;
            }

            public void setRose(double Rose) {
                this.Rose = Rose;
            }

            public double getSellNum() {
                return SellNum;
            }

            public void setSellNum(double SellNum) {
                this.SellNum = SellNum;
            }

            public double getSellPrice() {
                return SellPrice;
            }

            public void setSellPrice(double SellPrice) {
                this.SellPrice = SellPrice;
            }

            public String getSellerCoinCode() {
                return SellerCoinCode;
            }

            public void setSellerCoinCode(String SellerCoinCode) {
                this.SellerCoinCode = SellerCoinCode;
            }

            public String getSellerCoinID() {
                return SellerCoinID;
            }

            public void setSellerCoinID(String SellerCoinID) {
                this.SellerCoinID = SellerCoinID;
            }

            public String getSellerCoinName() {
                return SellerCoinName;
            }

            public void setSellerCoinName(String SellerCoinName) {
                this.SellerCoinName = SellerCoinName;
            }

            public String getSymbol() {
                return Symbol;
            }

            public void setSymbol(String Symbol) {
                this.Symbol = Symbol;
            }

            public String getTradingConfigID() {
                return TradingConfigID;
            }

            public void setTradingConfigID(String TradingConfigID) {
                this.TradingConfigID = TradingConfigID;
            }

            public int getTradingStatus() {
                return TradingStatus;
            }

            public void setTradingStatus(int TradingStatus) {
                this.TradingStatus = TradingStatus;
            }

            public int getTradingType() {
                return TradingType;
            }

            public void setTradingType(int TradingType) {
                this.TradingType = TradingType;
            }

        }
    }
}
