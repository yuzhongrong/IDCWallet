package com.idcg.idcw.model.bean;

import java.util.List;

/**
 * Created by hpz on 2017/12/27.
 */

public class CurrencyDetailInfo {


    /**
     * page : {"index":1,"size":5,"count":42}
     * status : 1
     * msg : null
     * ex : null
     * data : [{"id":764,"amount":22000,"tx_fee":5.0E-4,"description":"","send_address":"VKSKmpuy16yN4qJpcezjexz25EYQL6EHsr","receiver_address":"VTN7J1rcoRs6f6nRg3YZLdvq7aMEBF8Kj5","is_confirm":true,"create_time":"2018-01-17T15:55:01","confirmtime":"2018-01-17T15:55:01","trade_type":0,"modify_date":"0001-01-01T00:00:00","confirmations":10060,"total_confirmations":3,"currency":"btc","timeInterval":6,"intervalUnit":"day","confirmation_des":"10060/3","txhash":"59b283f1ec6f052f9ab24b82a30fc9ea0a572f297491109a3453bdc607056106"},{"id":763,"amount":0.04181352,"tx_fee":5.0E-4,"description":"","send_address":"VG3pf4MV5YmRHbAvkgKcpseX2QzFQnLi4T","receiver_address":"VTN7J1rcoRs6f6nRg3YZLdvq7aMEBF8Kj5","is_confirm":true,"create_time":"2018-01-17T15:51:03","confirmtime":"2018-01-17T15:51:03","trade_type":0,"modify_date":"0001-01-01T00:00:00","confirmations":10064,"total_confirmations":3,"currency":"btc","timeInterval":6,"intervalUnit":"day","confirmation_des":"10064/3","txhash":"088768cce0705dae3d9703e584ca6ca1803f5d72af5d94ed7acf75116a96f91b"},{"id":916,"amount":0.1911,"tx_fee":5.0E-4,"description":"","send_address":"VQy4bao9ktm5oCzQ5Ccj5pTA3GkfcyH5e2","receiver_address":"VWjCeMYaaFyxfaAXRhktaghxsjoy731yU2","is_confirm":true,"create_time":"2018-01-17T15:49:37","confirmtime":"2018-01-17T15:49:38","trade_type":1,"modify_date":"0001-01-01T00:00:00","confirmations":10066,"total_confirmations":3,"currency":"btc","timeInterval":6,"intervalUnit":"day","confirmation_des":"10066/3","txhash":"8272f8ca4c1a157b50839d3a9dec44caeddad6bb9dc9b6d61c9a0af44ab8ad25"},{"id":748,"amount":0.1916,"tx_fee":5.0E-4,"description":"","send_address":"VH5b29yCcH678in17bedVGKhx8A7ug1jQX","receiver_address":"VQy4bao9ktm5oCzQ5Ccj5pTA3GkfcyH5e2","is_confirm":true,"create_time":"2018-01-17T12:46:59","confirmtime":"2018-01-17T12:46:59","trade_type":0,"modify_date":"0001-01-01T00:00:00","confirmations":10249,"total_confirmations":3,"currency":"btc","timeInterval":1,"intervalUnit":"week","confirmation_des":"10249/3","txhash":"79c29effd5758321f560d62e486f851d976c869edfcfc0e584b440123b14a72a"},{"id":899,"amount":0.1921,"tx_fee":5.0E-4,"description":"","send_address":"VDyraSUvuCqFKNVYKVKkw5tjoUfL8Ma3Zk;VRPAv9fyfT11ynESKFyLgFwGWJVT3VrrS7","receiver_address":"VH5b29yCcH678in17bedVGKhx8A7ug1jQX","is_confirm":true,"create_time":"2018-01-17T12:42:52","confirmtime":"2018-01-17T12:42:52","trade_type":1,"modify_date":"0001-01-01T00:00:00","confirmations":10253,"total_confirmations":3,"currency":"btc","timeInterval":1,"intervalUnit":"week","confirmation_des":"10253/3","txhash":"9e72628ff22694074fa0e7b1fb04331e041e600ac8034576f15adbe88f44eeab"}]
     */

    private PageBean page;
    private int status;
    private Object msg;
    private Object ex;
    private List<DataBean> data;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getEx() {
        return ex;
    }

    public void setEx(Object ex) {
        this.ex = ex;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PageBean {
        /**
         * index : 1
         * size : 5
         * count : 42
         */

        private int index;
        private int size;
        private int count;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public static class DataBean {
        /**
         * id : 764
         * amount : 22000.0
         * tx_fee : 5.0E-4
         * description :
         * send_address : VKSKmpuy16yN4qJpcezjexz25EYQL6EHsr
         * receiver_address : VTN7J1rcoRs6f6nRg3YZLdvq7aMEBF8Kj5
         * is_confirm : true
         * create_time : 2018-01-17T15:55:01
         * confirmtime : 2018-01-17T15:55:01
         * trade_type : 0
         * modify_date : 0001-01-01T00:00:00
         * confirmations : 10060
         * total_confirmations : 3
         * currency : btc
         * timeInterval : 6
         * intervalUnit : day
         * confirmation_des : 10060/3
         * txhash : 59b283f1ec6f052f9ab24b82a30fc9ea0a572f297491109a3453bdc607056106
         */

        private int id;
        private double amount;
        private double tx_fee;
        private String description;
        private String send_address;
        private String receiver_address;
        private boolean is_confirm;
        private String create_time;
        private String confirmtime;
        private int trade_type;
        private String modify_date;
        private int confirmations;
        private int total_confirmations;
        private String currency;
        private int timeInterval;
        private String intervalUnit;
        private String confirmation_des;
        private String txhash;
        private Object url;
        private boolean isJump;
        private String input;
        private boolean txReceiptStatus;
        private boolean isToken;

        public boolean isToken() {
            return isToken;
        }

        public void setToken(boolean token) {
            isToken = token;
        }

        public Object getUrl() {
            return url;
        }

        public void setUrl(Object url) {
            this.url = url;
        }

        public boolean isIsJump() {
            return isJump;
        }

        public void setIsJump(boolean isJump) {
            this.isJump = isJump;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public boolean isTxReceiptStatus() {
            return txReceiptStatus;
        }

        public void setTxReceiptStatus(boolean txReceiptStatus) {
            this.txReceiptStatus = txReceiptStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getTx_fee() {
            return tx_fee;
        }

        public void setTx_fee(double tx_fee) {
            this.tx_fee = tx_fee;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSend_address() {
            return send_address;
        }

        public void setSend_address(String send_address) {
            this.send_address = send_address;
        }

        public String getReceiver_address() {
            return receiver_address;
        }

        public void setReceiver_address(String receiver_address) {
            this.receiver_address = receiver_address;
        }

        public boolean isIs_confirm() {
            return is_confirm;
        }

        public void setIs_confirm(boolean is_confirm) {
            this.is_confirm = is_confirm;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getConfirmtime() {
            return confirmtime;
        }

        public void setConfirmtime(String confirmtime) {
            this.confirmtime = confirmtime;
        }

        public int getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(int trade_type) {
            this.trade_type = trade_type;
        }

        public String getModify_date() {
            return modify_date;
        }

        public void setModify_date(String modify_date) {
            this.modify_date = modify_date;
        }

        public int getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(int confirmations) {
            this.confirmations = confirmations;
        }

        public int getTotal_confirmations() {
            return total_confirmations;
        }

        public void setTotal_confirmations(int total_confirmations) {
            this.total_confirmations = total_confirmations;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getTimeInterval() {
            return timeInterval;
        }

        public void setTimeInterval(int timeInterval) {
            this.timeInterval = timeInterval;
        }

        public String getIntervalUnit() {
            return intervalUnit;
        }

        public void setIntervalUnit(String intervalUnit) {
            this.intervalUnit = intervalUnit;
        }

        public String getConfirmation_des() {
            return confirmation_des;
        }

        public void setConfirmation_des(String confirmation_des) {
            this.confirmation_des = confirmation_des;
        }

        public String getTxhash() {
            return txhash;
        }

        public void setTxhash(String txhash) {
            this.txhash = txhash;
        }
    }
}
