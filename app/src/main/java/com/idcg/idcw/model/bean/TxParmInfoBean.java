package com.idcg.idcw.model.bean;

/**
 * Created by admin-2 on 2018/3/21.
 */

public class TxParmInfoBean {

    /**
     * id : 2285
     * user_id : 4284
     * description :
     * txhash : 08a65afd7e079592c29b6bb88c85411740514339c79f128e12ba3777cccf9f9b
     * send_address : VJ8CpuNLoAyRUvZJiYUoBhKfEuz3JtB42t
     * amount : 0.01
     * receiver_address : VRvXtyNDPCC8Mf5DG9LJR92mgoyizug3qR
     * is_confirm : true
     * tx_fee : 3.38E-4
     * create_time : 2018-02-10T13:51:59
     * currency : btc
     * confirmations : 2588
     * total_confirmations : 3
     * trade_type : 1
     * blockhash : 000c6d76a72a693515ea26a99a6cec1cbaa0e1f33d85ca4e63ade810dfefa981
     * url : https://www.blocktrail.com/BTC/tx/{idcw_txid}
     * isJump : true
     */

    private int id;
    private int user_id;
    private String description;
    private String txhash;
    private String send_address;
    private double amount;
    private String receiver_address;
    private boolean is_confirm;
    private double tx_fee;
    private String create_time;
    private String currency;
    private int confirmations;
    private int total_confirmations;
    private int trade_type;
    private String blockhash;
    private String url;
    private boolean isJump;
    private boolean txReceiptStatus;
    private boolean isToken;
    private String tokenUnit;
    private String categoryUnit;

    public boolean isTxReceiptStatus() {
        return txReceiptStatus;
    }

    public void setTxReceiptStatus(boolean txReceiptStatus) {
        this.txReceiptStatus = txReceiptStatus;
    }

    public boolean isIsToken() {
        return isToken;
    }

    public void setIsToken(boolean isToken) {
        this.isToken = isToken;
    }

    public String getTokenUnit() {
        return tokenUnit;
    }

    public void setTokenUnit(String tokenUnit) {
        this.tokenUnit = tokenUnit;
    }

    public String getCategoryUnit() {
        return categoryUnit;
    }

    public void setCategoryUnit(String categoryUnit) {
        this.categoryUnit = categoryUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash;
    }

    public String getSend_address() {
        return send_address;
    }

    public void setSend_address(String send_address) {
        this.send_address = send_address;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public double getTx_fee() {
        return tx_fee;
    }

    public void setTx_fee(double tx_fee) {
        this.tx_fee = tx_fee;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public int getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(int trade_type) {
        this.trade_type = trade_type;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isIsJump() {
        return isJump;
    }

    public void setIsJump(boolean isJump) {
        this.isJump = isJump;
    }

}
