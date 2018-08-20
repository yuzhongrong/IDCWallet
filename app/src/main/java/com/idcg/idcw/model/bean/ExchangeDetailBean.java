package com.idcg.idcw.model.bean;


import android.text.TextUtils;

import com.idcg.idcw.utils.Utils;

import java.util.Locale;

/**
 *
 * @author yiyang
 */
public class ExchangeDetailBean {

    /**
     * Id : 0
     * CreateTime : 2018-03-27T09:38:53.907Z
     * SendConfirmTime : 2018-03-27T09:38:53.907Z
     * ReceiveConfirmTime : 2018-03-27T09:38:53.907Z
     * CompleteTime : 2018-03-27T09:38:53.907Z
     * UserId : 0
     * Currency : string
     * ToCurrency : string
     * Fee : 0.0
     * ToFee : 0.0
     * MinCount : 0
     * ConfirmCount : 0
     * ToMinCount : 0
     * ToConfirmCount : 0
     * ExchangeRate : 0.0
     * Amount : 0.0
     * ToAmount : 0.0
     * Logo : string
     * TxId : string
     * ToTxId : string
     * ToLogo : string
     * BlockViewUrl : string
     * ToBlockViewUrl : string
     * Status : 0
     * Comment : string
     * StatusDescription : string
     * SearchStartTime : 2018-03-27T09:38:53.907Z
     * SearchEndTime : 2018-03-27T09:38:53.907Z
     */

    private int Id;
    private String CreateTime;
    private String SendConfirmTime;
    private String ReceiveConfirmTime;
    private String CompleteTime;
    private int UserId;
    private String Currency;
    private String ToCurrency;
    private double Fee;
    private double ToFee;
    private int MinCount;
    private int ConfirmCount;
    private int ToMinCount;
    private int ToConfirmCount;
    private double ExchangeRate;
    private int RateDigit;
    private double Amount;
    private double ToAmount;
    private String Logo;
    private String TxId;
    private String ToTxId;
    private String ToLogo;
    private String BlockViewUrl;
    private String ToBlockViewUrl;
    /**
     * 0是进行中，兑入中。
     * 1是兑入成功，也是兑出中
     * 2是兑出成功。
     * 3是推送交易所成功，就是整个交易完成
     * 4失败
     * */
    private int Status;
    private int InStatus;
    private int OutStatus;
    private String Comment;
    private String StatusDescription;
    private String SearchStartTime;
    private String SearchEndTime;
    /**Direction (boolean, optional): 方向（true汇率不变，false汇率取倒数） ,*/
    private boolean Direction;
    private String Unit;

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUpperCaseUnit(){
        return TextUtils.isEmpty(Unit)?"":Unit.toUpperCase(Locale.ENGLISH);
    }
    public boolean isDirection() {
        return Direction;
    }

    public void setDirection(boolean direction) {
        Direction = direction;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getSendConfirmTime() {
        return SendConfirmTime;
    }

    public void setSendConfirmTime(String SendConfirmTime) {
        this.SendConfirmTime = SendConfirmTime;
    }

    public String getReceiveConfirmTime() {
        return ReceiveConfirmTime;
    }

    public void setReceiveConfirmTime(String ReceiveConfirmTime) {
        this.ReceiveConfirmTime = ReceiveConfirmTime;
    }

    public String getCompleteTime() {
        return CompleteTime;
    }

    public void setCompleteTime(String CompleteTime) {
        this.CompleteTime = CompleteTime;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public String getCurrency() {
        return Currency;
    }
    public String getUpperCaseCurrency() {
        return TextUtils.isEmpty(Currency)?"":Currency.toUpperCase();
    }
    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public String getToCurrency() {
        return ToCurrency;
    }
    public String getUpperCaseToCurrency() {
        return TextUtils.isEmpty(ToCurrency)?"":ToCurrency.toUpperCase();
    }
    public void setToCurrency(String ToCurrency) {
        this.ToCurrency = ToCurrency;
    }

    public double getFee() {
        return Fee;
    }

    public void setFee(double Fee) {
        this.Fee = Fee;
    }

    public double getToFee() {
        return ToFee;
    }

    public void setToFee(double ToFee) {
        this.ToFee = ToFee;
    }

    public int getMinCount() {
        return MinCount;
    }

    public void setMinCount(int MinCount) {
        this.MinCount = MinCount;
    }

    public int getConfirmCount() {
        return ConfirmCount;
    }

    public void setConfirmCount(int ConfirmCount) {
        this.ConfirmCount = ConfirmCount;
    }

    public int getToMinCount() {
        return ToMinCount;
    }

    public void setToMinCount(int ToMinCount) {
        this.ToMinCount = ToMinCount;
    }

    public int getToConfirmCount() {
        return ToConfirmCount;
    }

    public void setToConfirmCount(int ToConfirmCount) {
        this.ToConfirmCount = ToConfirmCount;
    }
    /**
     * 如果是btc-bch,则真实汇率为汇率倒数
     * @return
     */
    public double getRightExchangeRate() {
        return ExchangeRate==0?0:isDirection()?ExchangeRate: Utils.div(1, ExchangeRate,8);
    }
    public double getExchangeRate() {
        return ExchangeRate;
    }

    public int getRateDigit() {
        return RateDigit;
    }

    public void setRateDigit(int rateDigit) {
        RateDigit = rateDigit;
    }

    public void setExchangeRate(double ExchangeRate) {
        this.ExchangeRate = ExchangeRate;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public double getToAmount() {
        return ToAmount;
    }

    public void setToAmount(double ToAmount) {
        this.ToAmount = ToAmount;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public String getTxId() {
        return TxId;
    }

    public void setTxId(String TxId) {
        this.TxId = TxId;
    }

    public String getToTxId() {
        return ToTxId;
    }

    public void setToTxId(String ToTxId) {
        this.ToTxId = ToTxId;
    }

    public String getToLogo() {
        return ToLogo;
    }

    public void setToLogo(String ToLogo) {
        this.ToLogo = ToLogo;
    }

    public String getBlockViewUrl() {
        return BlockViewUrl;
    }

    public void setBlockViewUrl(String BlockViewUrl) {
        this.BlockViewUrl = BlockViewUrl;
    }

    public String getToBlockViewUrl() {
        return ToBlockViewUrl;
    }

    public void setToBlockViewUrl(String ToBlockViewUrl) {
        this.ToBlockViewUrl = ToBlockViewUrl;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getInStatus() {
        return InStatus;
    }

    public void setInStatus(int inStatus) {
        InStatus = inStatus;
    }

    public int getOutStatus() {
        return OutStatus;
    }

    public void setOutStatus(int outStatus) {
        OutStatus = outStatus;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }

    public void setStatusDescription(String StatusDescription) {
        this.StatusDescription = StatusDescription;
    }

    public String getSearchStartTime() {
        return SearchStartTime;
    }

    public void setSearchStartTime(String SearchStartTime) {
        this.SearchStartTime = SearchStartTime;
    }

    public String getSearchEndTime() {
        return SearchEndTime;
    }

    public void setSearchEndTime(String SearchEndTime) {
        this.SearchEndTime = SearchEndTime;
    }
}
