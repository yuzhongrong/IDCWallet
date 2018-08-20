package com.idcg.idcw.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 14:18
 **/

public class HistoryAmountBean implements Serializable {
    /**
     * showType : 0
     * localCurrency : USD
     * currencySymbol : $
     * totalAssetMoney : 1.8555399592E8
     * persent : +4.56%
     * dValue : +$8,097,248.46
     * assetChangeType : 1
     * historyMarketData : null
     * historyAssetData : [{"currency":"total","sortIndex":0,"localCurrency":"USD","currencySymbol":"$","isDefault":false,"dateList":[{"date":"2018-02-18","amount":17891.98995682,"marketMoney":1.8555399592E8},{"date":"2018-02-19","amount":17891.98995682,"marketMoney":1.8555399592E8},{"date":"2018-02-20","amount":17891.98995682,"marketMoney":1.8555399592E8},{"date":"2018-02-21","amount":17891.98995682,"marketMoney":1.8555399592E8},{"date":"2018-02-22","amount":17891.98995682,"marketMoney":1.8358042378E8},{"date":"2018-02-23","amount":17891.98995682,"marketMoney":1.7745674743E8},{"date":"2018-02-24","amount":17891.98995682,"marketMoney":1.8555399592E8}]},{"currency":"btc","sortIndex":1,"localCurrency":"USD","currencySymbol":"$","isDefault":true,"dateList":[{"date":"2018-02-18","amount":17775.99666652,"marketMoney":1.855458532E8},{"date":"2018-02-19","amount":17775.99666652,"marketMoney":1.855458532E8},{"date":"2018-02-20","amount":17775.99666652,"marketMoney":1.855458532E8},{"date":"2018-02-21","amount":17775.99666652,"marketMoney":1.855458532E8},{"date":"2018-02-22","amount":17775.99666652,"marketMoney":1.8357271757E8},{"date":"2018-02-23","amount":17775.99666652,"marketMoney":1.7744888672E8},{"date":"2018-02-24","amount":17775.99666652,"marketMoney":1.855458532E8}]}]
     */
    private int showType;
    private String localCurrency;
    private String currencySymbol;
    private double totalAssetMoney;
    private String persent;
    private String dValue;
    private int assetChangeType;
    private List<HistoryMarketData> historyMarketData;
    private List<HistoryAssetDataBean> historyAssetData;

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public double getTotalAssetMoney() {
        return totalAssetMoney;
    }

    public void setTotalAssetMoney(double totalAssetMoney) {
        this.totalAssetMoney = totalAssetMoney;
    }

    public String getPersent() {
        return persent;
    }

    public void setPersent(String persent) {
        this.persent = persent;
    }

    public String getDValue() {
        return dValue;
    }

    public void setDValue(String dValue) {
        this.dValue = dValue;
    }

    public int getAssetChangeType() {
        return assetChangeType;
    }

    public void setAssetChangeType(int assetChangeType) {
        this.assetChangeType = assetChangeType;
    }

    public List<HistoryMarketData> getHistoryMarketData() {
        return historyMarketData;
    }

    public void setHistoryMarketData(List<HistoryMarketData> historyMarketData) {
        this.historyMarketData = historyMarketData;
    }

    public List<HistoryAssetDataBean> getHistoryAssetData() {
        return historyAssetData;
    }

    public void setHistoryAssetData(List<HistoryAssetDataBean> historyAssetData) {
        this.historyAssetData = historyAssetData;
    }

    public static class HistoryAssetDataBean {
        /**
         * currency : total
         * sortIndex : 0
         * localCurrency : USD
         * currencySymbol : $
         * isDefault : false
         * dateList : [{"date":"2018-02-18","amount":17891.98995682,"marketMoney":1.8555399592E8},{"date":"2018-02-19","amount":17891.98995682,"marketMoney":1.8555399592E8},{"date":"2018-02-20","amount":17891.98995682,"marketMoney":1.8555399592E8},{"date":"2018-02-21","amount":17891.98995682,"marketMoney":1.8555399592E8},{"date":"2018-02-22","amount":17891.98995682,"marketMoney":1.8358042378E8},{"date":"2018-02-23","amount":17891.98995682,"marketMoney":1.7745674743E8},{"date":"2018-02-24","amount":17891.98995682,"marketMoney":1.8555399592E8}]
         */

        private String currency;
        private int sortIndex;
        private String localCurrency;
        private String currencySymbol;
        private boolean isDefault;
        private List<DateListBean> dateList;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getSortIndex() {
            return sortIndex;
        }

        public void setSortIndex(int sortIndex) {
            this.sortIndex = sortIndex;
        }

        public String getLocalCurrency() {
            return localCurrency;
        }

        public void setLocalCurrency(String localCurrency) {
            this.localCurrency = localCurrency;
        }

        public String getCurrencySymbol() {
            return currencySymbol;
        }

        public void setCurrencySymbol(String currencySymbol) {
            this.currencySymbol = currencySymbol;
        }

        public boolean isIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public List<DateListBean> getDateList() {
            return dateList;
        }

        public void setDateList(List<DateListBean> dateList) {
            this.dateList = dateList;
        }

        public static class DateListBean {
            /**
             * date : 2018-02-18
             * amount : 17891.98995682
             * marketMoney : 1.8555399592E8
             */

            private String date;
            private double amount;
            private double marketMoney;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public double getMarketMoney() {
                return marketMoney;
            }

            public void setMarketMoney(double marketMoney) {
                this.marketMoney = marketMoney;
            }
        }
    }

    public static class HistoryMarketData {
        private String currency;
        private int sortIndex;
        private String localCurrency;
        private boolean isDefault;
        private List<DateListBean> dateList;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getSortIndex() {
            return sortIndex;
        }

        public void setSortIndex(int sortIndex) {
            this.sortIndex = sortIndex;
        }

        public String getLocalCurrency() {
            return localCurrency;
        }

        public void setLocalCurrency(String localCurrency) {
            this.localCurrency = localCurrency;
        }

        public boolean isIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public List<DateListBean> getDateList() {
            return dateList;
        }

        public void setDateList(List<DateListBean> dateList) {
            this.dateList = dateList;
        }

        public static class DateListBean {
            /**
             * price : 10400
             */

            private float price;

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }
        }
    }
}
