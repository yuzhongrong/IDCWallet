package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 15:08
 **/

public class TransMsgBean implements Serializable {
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

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean jump) {
        isJump = jump;
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

    public boolean isToken() {
        return isToken;
    }

    public void setToken(boolean token) {
        isToken = token;
    }

}
