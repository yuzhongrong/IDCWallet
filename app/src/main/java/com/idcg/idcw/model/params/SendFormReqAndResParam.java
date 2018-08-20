package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.params ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 10:25
 **/

public class SendFormReqAndResParam implements Serializable{
    private String toAddress;
    private String amount;
    private String currency;
    private String fee;
    private String tag;
    private int statusCode;

    public SendFormReqAndResParam(String toAddress,String amount ,String currency, String fee,int statusCode) {

        this.toAddress=toAddress;
        this.amount=amount;
        this.currency = currency;
        this.fee = fee;
        this.statusCode = statusCode;

    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
