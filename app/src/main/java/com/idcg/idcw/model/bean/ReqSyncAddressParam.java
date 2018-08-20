package com.idcg.idcw.model.bean;

/**
 * Created by admin-2 on 2018/3/19.
 */

public class ReqSyncAddressParam {

 private String  address;
    private String currency;

    public ReqSyncAddressParam(String address, String currency) {
        this.address = address;
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
