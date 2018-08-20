package com.idcg.idcw.model.params;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 *
 * @author yiyang
 */
public class ExchangeInParam implements Serializable, Parcelable{

    /**
     * toCurrency : string
     * toAmount : 0.0
     * rate : 0.0
     * payPassword : string
     * toAddress : string
     * amount : 0.0
     * fee : 0.0
     * currency : string
     * comment : string
     */

    private String toCurrency;
    private double toAmount;
    private double rate;
    private String payPassword;
    private String toAddress;
    private double amount;
    private String fee;
    private String currency;
    private String comment;
    private String device_id;
    private boolean Direction;
    private int Digit;
    private int ToDigit;
    private int RateDigit;


    public boolean isDirection() {
        return Direction;
    }

    public void setDirection(boolean direction) {
        Direction = direction;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(double toAmount) {
        this.toAmount = toAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getDigit() {
        return Digit;
    }

    public void setDigit(int digit) {
        Digit = digit;
    }

    public int getToDigit() {
        return ToDigit;
    }

    public void setToDigit(int toDigit) {
        ToDigit = toDigit;
    }

    public int getRateDigit() {
        return RateDigit;
    }

    public void setRateDigit(int rateDigit) {
        RateDigit = rateDigit;
    }

    public ExchangeInParam(String toCurrency, double toAmount, double rate, String payPassword, String toAddress,
                           double amount, String fee, String currency, String comment, String device_id, boolean
                                   direction, int digit, int toDigit, int rateDigit) {
        this.toCurrency = toCurrency;
        this.toAmount = toAmount;
        this.rate = rate;
        this.payPassword = payPassword;
        this.toAddress = toAddress;
        this.amount = amount;
        this.fee = fee;
        this.currency = currency;
        this.comment = comment;
        this.device_id = device_id;
        Direction = direction;
        Digit = digit;
        ToDigit = toDigit;
        RateDigit = rateDigit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.toCurrency);
        dest.writeDouble(this.toAmount);
        dest.writeDouble(this.rate);
        dest.writeString(this.payPassword);
        dest.writeString(this.toAddress);
        dest.writeDouble(this.amount);
        dest.writeString(this.fee);
        dest.writeString(this.currency);
        dest.writeString(this.comment);
        dest.writeString(this.device_id);
        dest.writeByte(this.Direction ? (byte) 1 : (byte) 0);
        dest.writeInt(this.Digit);
        dest.writeInt(this.ToDigit);
        dest.writeInt(this.RateDigit);
    }

    protected ExchangeInParam(Parcel in) {
        this.toCurrency = in.readString();
        this.toAmount = in.readDouble();
        this.rate = in.readDouble();
        this.payPassword = in.readString();
        this.toAddress = in.readString();
        this.amount = in.readDouble();
        this.fee = in.readString();
        this.currency = in.readString();
        this.comment = in.readString();
        this.device_id = in.readString();
        this.Direction = in.readByte() != 0;
        this.Digit = in.readInt();
        this.ToDigit = in.readInt();
        this.RateDigit = in.readInt();
    }

    public static final Creator<ExchangeInParam> CREATOR = new Creator<ExchangeInParam>() {
        @Override
        public ExchangeInParam createFromParcel(Parcel source) {
            return new ExchangeInParam(source);
        }

        @Override
        public ExchangeInParam[] newArray(int size) {
            return new ExchangeInParam[size];
        }
    };
}
