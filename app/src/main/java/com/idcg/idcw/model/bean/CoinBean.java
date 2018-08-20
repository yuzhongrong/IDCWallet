package com.idcg.idcw.model.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Locale;

/**
 *
 * @author yiyang
 */
public class CoinBean implements Comparable<CoinBean>{

    private String Coin;
    private boolean IsSupportExchange;
    private String Logo;
    /**
     * 是否市场币
     * 兑换过程中至少需要一个市场币
     * */
    private boolean IsMarket;
    private double ExchangeMin;
    private double ExchangeMax;
    private int digit;
    private int Sort;

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public CoinBean() {
    }

    public CoinBean(String coin, boolean isSupportExchange, String logo, boolean isMarket,
                    double exchangeMin, double exchangeMax, int digit) {
        Coin = coin;
        IsSupportExchange = isSupportExchange;
        Logo = logo;
        IsMarket = isMarket;
        ExchangeMin = exchangeMin;
        ExchangeMax = exchangeMax;
        this.digit = digit;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public String getCoin() {
        return Coin;
    }

    public String getUpperCaseCoin(){
        return TextUtils.isEmpty(Coin)?"":Coin.toUpperCase(Locale.ENGLISH);
    }

    public void setCoin(String coin) {
        Coin = coin;
    }

    public boolean isSupportExchange() {
        return IsSupportExchange;
    }

    public void setSupportExchange(boolean supportExchange) {
        IsSupportExchange = supportExchange;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public boolean isMarket() {
        return IsMarket;
    }

    public void setMarket(boolean market) {
        IsMarket = market;
    }

    public double getExchangeMin() {
        return ExchangeMin;
    }

    public void setExchangeMin(double exchangeMin) {
        ExchangeMin = exchangeMin;
    }

    public double getExchangeMax() {
        return ExchangeMax;
    }

    public void setExchangeMax(double exchangeMax) {
        ExchangeMax = exchangeMax;
    }

    @Override
    public String toString() {
        return "OTCCoinBean{" +
                "Coin='" + Coin + '\'' +
                ", IsSupportExchange=" + IsSupportExchange +
                ", Logo='" + Logo + '\'' +
                ", IsMarket=" + IsMarket +
                ", ExchangeMin=" + ExchangeMin +
                ", ExchangeMax=" + ExchangeMax +
                ", digit=" + digit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CoinBean coinBean = (CoinBean) o;

        return Coin != null ? Coin.equals(coinBean.Coin) : coinBean.Coin == null;
    }

    @Override
    public int hashCode() {
        return Coin != null ? Coin.hashCode() : 0;
    }

    @Override
    public int compareTo(@NonNull CoinBean o) {
        return Sort-o.Sort;
    }
}
