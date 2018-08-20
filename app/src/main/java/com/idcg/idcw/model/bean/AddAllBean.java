package com.idcg.idcw.model.bean;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class AddAllBean {

    /**
     * id : 2180
     * currency : btc
     * currencyName : Bitcoin
     * logo : /upload/coin/ico_btc.png
     * sortIndex : 0
     * isShow : true
     */

    private int id;
    private String currency;
    private String currencyName;
    private String logo;
    private int sortIndex;
    private boolean isShow;
    private boolean isSelect = false;

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

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public boolean isIsShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

}
