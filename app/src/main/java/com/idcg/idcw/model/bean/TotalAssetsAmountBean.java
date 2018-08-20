package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 17:18
 **/

public class TotalAssetsAmountBean implements Serializable {
    /**
     * date : 26
     * money : 0.0
     * localCurrency : USD
     * currencySymbol : $
     */

    private String date;
    private double money;
    private String localCurrency;
    private String currencySymbol;
    private String detailDate;

    public String getDetailDate() {
        return detailDate;
    }

    public void setDetailDate(String detailDate) {
        this.detailDate = detailDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
