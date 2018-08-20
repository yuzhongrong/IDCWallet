package com.idcg.idcw.model.bean;

/**
 *
 * @author yiyang
 */
public class CoinPairBean {

    /**
     * FromCoin : string
     * ToCoin : string
     * FromLogo : string
     * ToLogo : string
     * ExchangeRate : 0.0
     * FromExchangeMin : 0.0
     * ToExchangeMax : 0.0
     * FromDigit : 0
     * RateDigit : 0
     * ToDigit : 0
     * ToExchangeMin : 0.0
     * FromExchangeMax : 0.0
     * FromIsSupportExchange : true
     * ToIsSupportExchange : true
     * Default : true
     * CreateTime : 2018-04-21T02:57:25.250Z
     * CreateUserId : 0
     * UpdateTime : 2018-04-21T02:57:25.250Z
     * UpdateUserId : 0
     * FromIsMarket : true
     * ToIsMarket : true
     * id : 0
     */

    private String FromCoin;
    private String ToCoin;
    private String FromLogo;
    private String ToLogo;
    private double ExchangeRate;
    private double FromExchangeMin;
    private double ToExchangeMax;
    private int FromDigit;
    private int RateDigit;
    private int ToDigit;
    private double ToExchangeMin;
    private double FromExchangeMax;
    private boolean FromIsSupportExchange;
    private boolean ToIsSupportExchange;
    private boolean Default;
    private String CreateTime;
    private int CreateUserId;
    private String UpdateTime;
    private int UpdateUserId;
    private boolean FromIsMarket;
    private boolean ToIsMarket;
    private String id;

    private double ToExchangeRate;

    public double getToExchangeRate() {
        return ToExchangeRate;
    }

    public void setToExchangeRate(double toExchangeRate) {
        ToExchangeRate = toExchangeRate;
    }

    public String getFromCoin() {
        return FromCoin;
    }

    public void setFromCoin(String FromCoin) {
        this.FromCoin = FromCoin;
    }

    public String getToCoin() {
        return ToCoin;
    }

    public void setToCoin(String ToCoin) {
        this.ToCoin = ToCoin;
    }

    public String getFromLogo() {
        return FromLogo;
    }

    public void setFromLogo(String FromLogo) {
        this.FromLogo = FromLogo;
    }

    public String getToLogo() {
        return ToLogo;
    }

    public void setToLogo(String ToLogo) {
        this.ToLogo = ToLogo;
    }

    public double getExchangeRate() {
        return ExchangeRate;
    }

    public void setExchangeRate(double ExchangeRate) {
        this.ExchangeRate = ExchangeRate;
    }

    public double getFromExchangeMin() {
        return FromExchangeMin;
    }

    public void setFromExchangeMin(double FromExchangeMin) {
        this.FromExchangeMin = FromExchangeMin;
    }

    public double getToExchangeMax() {
        return ToExchangeMax;
    }

    public void setToExchangeMax(double ToExchangeMax) {
        this.ToExchangeMax = ToExchangeMax;
    }

    public int getFromDigit() {
        return FromDigit;
    }

    public void setFromDigit(int FromDigit) {
        this.FromDigit = FromDigit;
    }

    public int getRateDigit() {
        return RateDigit;
    }

    public void setRateDigit(int RateDigit) {
        this.RateDigit = RateDigit;
    }

    public int getToDigit() {
        return ToDigit;
    }

    public void setToDigit(int ToDigit) {
        this.ToDigit = ToDigit;
    }

    public double getToExchangeMin() {
        return ToExchangeMin;
    }

    public void setToExchangeMin(double ToExchangeMin) {
        this.ToExchangeMin = ToExchangeMin;
    }

    public double getFromExchangeMax() {
        return FromExchangeMax;
    }

    public void setFromExchangeMax(double FromExchangeMax) {
        this.FromExchangeMax = FromExchangeMax;
    }

    public boolean isFromIsSupportExchange() {
        return FromIsSupportExchange;
    }

    public void setFromIsSupportExchange(boolean FromIsSupportExchange) {
        this.FromIsSupportExchange = FromIsSupportExchange;
    }

    public boolean isToIsSupportExchange() {
        return ToIsSupportExchange;
    }

    public void setToIsSupportExchange(boolean ToIsSupportExchange) {
        this.ToIsSupportExchange = ToIsSupportExchange;
    }

    public boolean isDefault() {
        return Default;
    }

    public void setDefault(boolean Default) {
        this.Default = Default;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public int getCreateUserId() {
        return CreateUserId;
    }

    public void setCreateUserId(int CreateUserId) {
        this.CreateUserId = CreateUserId;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public int getUpdateUserId() {
        return UpdateUserId;
    }

    public void setUpdateUserId(int UpdateUserId) {
        this.UpdateUserId = UpdateUserId;
    }

    public boolean isFromIsMarket() {
        return FromIsMarket;
    }

    public void setFromIsMarket(boolean FromIsMarket) {
        this.FromIsMarket = FromIsMarket;
    }

    public boolean isToIsMarket() {
        return ToIsMarket;
    }

    public void setToIsMarket(boolean ToIsMarket) {
        this.ToIsMarket = ToIsMarket;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
