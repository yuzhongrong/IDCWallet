package com.idcg.idcw.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 18:08
 **/

public class RunChartDataBean implements Serializable {
    /**
     * id : 0
     * showType : 0
     * currencyList : [{"id":10000520,"currency":"etc","currencyName":"Ethereum Classic","logo":"/upload/coin/ico_etc.png","sortIndex":1,"isShow":true,"isDefault":false,"logo_url":"http://file.idcw.io//upload/coin/ico_etc.png"},{"id":2202,"currency":"bch","currencyName":"Bitcoin Cash","logo":"/upload/coin/ico_bch.png","sortIndex":2,"isShow":true,"isDefault":false,"logo_url":"http://file.idcw.io//upload/coin/ico_bch.png"},{"id":2203,"currency":"btg","currencyName":"Bitcoin Gold","logo":"/upload/coin/ico_btg.png","sortIndex":3,"isShow":true,"isDefault":false,"logo_url":"http://file.idcw.io//upload/coin/ico_btg.png"},{"id":2204,"currency":"vhkd","currencyName":"VHKD","logo":"/upload/coin/ico_vhkd.png","sortIndex":4,"isShow":true,"isDefault":false,"logo_url":"http://file.idcw.io//upload/coin/ico_vhkd.png"},{"id":2513,"currency":"ltc","currencyName":"Litecoin","logo":"/upload/coin/ico_ltc.png","sortIndex":5,"isShow":true,"isDefault":false,"logo_url":"http://file.idcw.io//upload/coin/ico_ltc.png"},{"id":10000370,"currency":"eth","currencyName":"Ethereum","logo":"/upload/coin/ico_eth.png","sortIndex":6,"isShow":true,"isDefault":false,"logo_url":"http://file.idcw.io//upload/coin/ico_eth.png"},{"id":2201,"currency":"btc","currencyName":"Bitcoin","logo":"/upload/coin/ico_btc.png","sortIndex":7,"isShow":true,"isDefault":false,"logo_url":"http://file.idcw.io//upload/coin/ico_btc.png"},{"id":2205,"currency":"btl","currencyName":"Bitcoin Link","logo":"/upload/coin/ico_btl.png","sortIndex":8,"isShow":true,"isDefault":false,"logo_url":"http://file.idcw.io//upload/coin/ico_btl.png"}]
     */

    private int id;
    private int showType;
    private List<CurrencyListBean> currencyList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public List<CurrencyListBean> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<CurrencyListBean> currencyList) {
        this.currencyList = currencyList;
    }

    public static class CurrencyListBean {
        /**
         * id : 10000520
         * currency : etc
         * currencyName : Ethereum Classic
         * logo : /upload/coin/ico_etc.png
         * sortIndex : 1
         * isShow : true
         * isDefault : false
         * logo_url : http://file.idcw.io//upload/coin/ico_etc.png
         */

        private int id;
        private String currency;
        private String currencyName;
        private String logo;
        private int sortIndex;
        private boolean isShow;
        private boolean isDefault;
        private String logo_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getSortIndex() {
            return sortIndex;
        }

        public void setSortIndex(int sortIndex) {
            this.sortIndex = sortIndex;
        }

        public boolean isIsShow() {
            return isShow;
        }

        public void setIsShow(boolean isShow) {
            this.isShow = isShow;
        }

        public boolean isIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }
    }
}
