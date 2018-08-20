package com.idcg.idcw.model.params;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class RequestCommonParam {

    String id;
    String tradeType;
    String currency;
    String content;
    //for txid
    String txid;

    public RequestCommonParam(String tradeType, String currency, String txid) {//for txid
        this.tradeType = tradeType;
        this.currency = currency;
        this.txid = txid;
    }

    public RequestCommonParam(String id, String tradeType, String currency, String content) {
        this.id = id;
        this.tradeType = tradeType;
        this.currency = currency;
        this.content = content;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
