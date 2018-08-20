package com.idcg.idcw.model.bean;

/**
 *
 * @author yiyang
 */
public class ExchangeResultBean {

    /**
     * statusCode : 0
     * statusMessage : string
     * txId : string
     */

    private int statusCode;
    private String statusMessage;
    private String txId;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
