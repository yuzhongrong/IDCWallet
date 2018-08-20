package com.idcg.idcw.model.bean;

import android.text.TextUtils;

/**
 *
 * @author yiyang
 */
public class ExchangeDataBean {

    /**
     * Exchange : string
     * Currency : string
     * ToCurrency : string
     * UserId : 0
     * Amount : 0.0
     * ToAmount : 0.0
     * InTxId : string
     * OutTxId : string
     * CreateTime : 2018-03-26T11:50:45.267Z
     * DateType : string
     * Date : 0
     * Status : 0
     * StatusDescription : string
     * Comment : string
     * MinCount : 0.0
     * ConfirmCount : 0.0
     * ToMinCount : 0.0
     * ToConfirmCount : 0.0
     * id : 0
     */

    private String Exchange;
    private String Currency;
    private String ToCurrency;
    private int UserId;
    private double Amount;
    private double ToAmount;
    private String InTxId;
    private String OutTxId;
    private String CreateTime;
    private String DateType;
    private int Date;
    private int Status;
    private int InStatus;
    private int OutStatus;
    private String StatusDescription;
    private String Comment;
    private int MinCount;
    private int ConfirmCount;
    private int ToMinCount;
    private int ToConfirmCount;
    private int id;

    public String getExchange() {
        return Exchange;
    }

    public void setExchange(String Exchange) {
        this.Exchange = Exchange;
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

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
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

    public String getInTxId() {
        return InTxId;
    }

    public void setInTxId(String InTxId) {
        this.InTxId = InTxId;
    }

    public String getOutTxId() {
        return OutTxId;
    }

    public void setOutTxId(String OutTxId) {
        this.OutTxId = OutTxId;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getDateType() {
        return DateType;
    }

    public void setDateType(String DateType) {
        this.DateType = DateType;
    }

    public int getDate() {
        return Date;
    }

    public void setDate(int Date) {
        this.Date = Date;
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

    public String getStatusDescription() {
        return StatusDescription;
    }

    public void setStatusDescription(String StatusDescription) {
        this.StatusDescription = StatusDescription;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
