package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.params ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 10:45
 **/

public class WalletAssetBean implements Serializable {
    /**
     * id : 2180
     * wallet_type : btc
     * label : My Bitcoin Wallet
     * balance : 22000.04183752
     * realityBalance : 22000.04183752
     * currency_unit : btc
     * currency : btc
     * currencyName : Bitcoin
     * logo : /upload/coin/ico_btc.png
     * localCurrencyName : USD
     * currencySymbol : $
     * localCurrencyMarket : 10573.0
     * sortIndex : 0
     */
    private int id;
    private String wallet_type;
    private String label;
    private double balance;
    private double realityBalance;
    private String currency_unit;
    private String currency;
    private String currencyName;
    private String logo;
    private String logo_url;
    private String localCurrencyName;
    private String currencySymbol;
    private double localCurrencyMarket;
    private int sortIndex;
    private boolean isSelect = false;
    private int currencyLayoutType;
    private boolean is_enable_ransceiver;

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getRealityBalance() {
        return realityBalance;
    }

    public void setRealityBalance(double realityBalance) {
        this.realityBalance = realityBalance;
    }

    public String getCurrency_unit() {
        return currency_unit;
    }

    public void setCurrency_unit(String currency_unit) {
        this.currency_unit = currency_unit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLocalCurrencyName() {
        return localCurrencyName;
    }

    public void setLocalCurrencyName(String localCurrencyName) {
        this.localCurrencyName = localCurrencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public double getLocalCurrencyMarket() {
        return localCurrencyMarket;
    }

    public void setLocalCurrencyMarket(double localCurrencyMarket) {
        this.localCurrencyMarket = localCurrencyMarket;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getCurrencyLayoutType() {
        return currencyLayoutType;
    }

    public void setCurrencyLayoutType(int currencyLayoutType) {
        this.currencyLayoutType = currencyLayoutType;
    }

    public boolean isIs_enable_ransceiver() {
        return is_enable_ransceiver;
    }

    public void setIs_enable_ransceiver(boolean is_enable_ransceiver) {
        this.is_enable_ransceiver = is_enable_ransceiver;
    }
}
