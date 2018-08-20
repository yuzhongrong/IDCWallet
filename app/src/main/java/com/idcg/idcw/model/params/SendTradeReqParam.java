package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.params ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 10:51
 **/

public class SendTradeReqParam implements Serializable {
    private String toAddress;
    private String amount;
    private String comment;
    private String fee;
    private String currency;
    private String payPassword;
    private String device_id;
    private boolean newVersion;


    public SendTradeReqParam(String toAddress, String amount, String comment, String fee, String currency, String payPassword,String device_id,boolean newVersion) {
        this.toAddress = toAddress;
        this.amount = amount;
        this.comment = comment;
        this.fee = fee;
        this.currency = currency;
        this.payPassword = payPassword;
        this.device_id = device_id;
        this.newVersion = newVersion;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public boolean isNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }
}
