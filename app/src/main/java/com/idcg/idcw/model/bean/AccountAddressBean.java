package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 15:02
 **/

public class AccountAddressBean implements Serializable {
    /**
     * currency : btc
     * address : VH5b29yCcH678in17bedVGKhx8A7ug1jQX
     * qrUrl : https://api.idcw.io/QRImage/VH5b29yCcH678in17bedVGKhx8A7ug1jQX.png
     */

    private String currency;
    private String address;
    private String qrUrl;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }
}
