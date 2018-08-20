package com.idcw.pay.model.param;

import java.io.Serializable;

public class PayValidAddressReqParam implements Serializable {
    private String address; // 地址
    private String currency; // 币种

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
