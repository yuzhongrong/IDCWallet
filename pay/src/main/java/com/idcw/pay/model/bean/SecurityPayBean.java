package com.idcw.pay.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.bean
 * 备注消息：
 * 修改时间：2018/3/26 18:08
 **/

public class SecurityPayBean implements Serializable {
    /**
     * statusCode : 0
     * statusMessage : string
     * txId : string
     */

    private int statusCode;
    private String statusMessage;
    private String txId;

    public int getStatusCode() { return statusCode;}

    public void setStatusCode(int statusCode) { this.statusCode = statusCode;}

    public String getStatusMessage() { return statusMessage;}

    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage;}

    public String getTxId() { return txId;}

    public void setTxId(String txId) { this.txId = txId;}

    @Override
    public String toString() {
        return "SendFromBean{" +
                "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                ", txId='" + txId + '\'' +
                '}';
    }
}
