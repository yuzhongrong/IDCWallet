package com.idcg.idcw.model.bean;

/**
 * Created by hpz on 2018/3/6.
 */

public class AssetNewBean {

    /**
     * status : 1
     * msg : success
     * ex : null
     * data : [{"id":2180,"wallet_type":"btc","label":"My Bitcoin Wallet","balance":22000.04183752,"realityBalance":22000.04183752,"currency_unit":"btc","currency":"btc","currencyName":"Bitcoin","logo":"/upload/coin/ico_btc.png","localCurrencyName":"USD","currencySymbol":"$","localCurrencyMarket":10573,"sortIndex":0},{"id":2181,"wallet_type":"bch","label":"My Bitcoin Cash Wallet","balance":0.72184167,"realityBalance":0.72184167,"currency_unit":"bch","currency":"bch","currencyName":"Bitcoin Cash","logo":"/upload/coin/ico_bch.png","localCurrencyName":"USD","currencySymbol":"$","localCurrencyMarket":1713.5,"sortIndex":0},{"id":2182,"wallet_type":"btg","label":"My Bitcoin Gold Wallet","balance":0.12491567,"realityBalance":0.12491567,"currency_unit":"btg","currency":"btg","currencyName":"Bitcoin Gold","logo":"/upload/coin/ico_btg.png","localCurrencyName":"USD","currencySymbol":"$","localCurrencyMarket":180.92,"sortIndex":0},{"id":2183,"wallet_type":"vhkd","label":"My VHKD Wallet","balance":0.22186241,"realityBalance":0.22186241,"currency_unit":"vhkd","currency":"vhkd","currencyName":"VHKD","logo":"/upload/coin/ico_vhkd.png","localCurrencyName":"USD","currencySymbol":"$","localCurrencyMarket":0.127871,"sortIndex":0},{"id":2184,"wallet_type":"btl","label":"My Bitcoin Link Wallet","balance":1.04879155,"realityBalance":1.04879155,"currency_unit":"btl","currency":"btl","currencyName":"Bitcoin Link","logo":"/upload/coin/ico_btl.png","localCurrencyName":"USD","currencySymbol":"$","localCurrencyMarket":100,"sortIndex":0}]
     */


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
    private String localCurrencyName;
    private String currencySymbol;
    private double localCurrencyMarket;
    private int sortIndex;
    private boolean isSelect = false;
    private int currencyLayoutType;
    private boolean isToken;
    private String tokenCategory;
    private String coinUnit;
    private double ethBalanceForToken;

    public boolean isIsToken() {
        return isToken;
    }

    public void setIsToken(boolean isToken) {
        this.isToken = isToken;
    }

    public String getTokenCategory() {
        return tokenCategory;
    }

    public void setTokenCategory(String tokenCategory) {
        this.tokenCategory = tokenCategory;
    }

    public String getCoinUnit() {
        return coinUnit;
    }

    public void setCoinUnit(String coinUnit) {
        this.coinUnit = coinUnit;
    }

    public double getEthBalanceForToken() {
        return ethBalanceForToken;
    }

    public void setEthBalanceForToken(double ethBalanceForToken) {
        this.ethBalanceForToken = ethBalanceForToken;
    }

    public int getCurrencyLayoutType() {
        return currencyLayoutType;
    }

    public void setCurrencyLayoutType(int currencyLayoutType) {
        this.currencyLayoutType = currencyLayoutType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
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

}
