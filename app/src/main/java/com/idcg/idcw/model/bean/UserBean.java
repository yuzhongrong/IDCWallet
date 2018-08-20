package com.idcg.idcw.model.bean;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class UserBean {
    private int msgId;
    private String origCurrency;
    private String receiveCurrency;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getOrigCurrency() {
        return origCurrency;
    }

    public void setOrigCurrency(String origCurrency) {
        this.origCurrency = origCurrency;
    }

    public String getReceiveCurrency() {
        return receiveCurrency;
    }

    public void setReceiveCurrency(String receiveCurrency) {
        this.receiveCurrency = receiveCurrency;
    }

}
