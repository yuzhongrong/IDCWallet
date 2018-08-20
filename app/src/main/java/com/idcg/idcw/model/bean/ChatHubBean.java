package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * Created by admin-2 on 2018/3/29.
 */

public class ChatHubBean implements Serializable{


    /**
     * Rise : -1.44E-4
     * Rose : -0.001533121819304559
     * Open : 0.093842
     * Close : 0.093782
     * Highest : 0.097769
     * Low : 0.09302
     * Newest : 0.093782
     * Date : 0001-01-01T00:00:00
     * TradingConfigID : _DSQ3BmslE-cS-HP0POlnA
     * Last24TradeQuantity : 4617.297
     * ChangeVolume : 0.004749
     */

    private double Rise;
    private double Rose;
    private double Open;
    private double Close;
    private double Highest;
    private double Low;
    private double Newest;
    private String Date;
    private String TradingConfigID;
    private double Last24TradeQuantity;
    private double ChangeVolume;

    public double getRise() {
        return Rise;
    }

    public void setRise(double Rise) {
        this.Rise = Rise;
    }

    public double getRose() {
        return Rose;
    }

    public void setRose(double Rose) {
        this.Rose = Rose;
    }

    public double getOpen() {
        return Open;
    }

    public void setOpen(double Open) {
        this.Open = Open;
    }

    public double getClose() {
        return Close;
    }

    public void setClose(double Close) {
        this.Close = Close;
    }

    public double getHighest() {
        return Highest;
    }

    public void setHighest(double Highest) {
        this.Highest = Highest;
    }

    public double getLow() {
        return Low;
    }

    public void setLow(double Low) {
        this.Low = Low;
    }

    public double getNewest() {
        return Newest;
    }

    public void setNewest(double Newest) {
        this.Newest = Newest;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTradingConfigID() {
        return TradingConfigID;
    }

    public void setTradingConfigID(String TradingConfigID) {
        this.TradingConfigID = TradingConfigID;
    }

    public double getLast24TradeQuantity() {
        return Last24TradeQuantity;
    }

    public void setLast24TradeQuantity(double Last24TradeQuantity) {
        this.Last24TradeQuantity = Last24TradeQuantity;
    }

    public double getChangeVolume() {
        return ChangeVolume;
    }

    public void setChangeVolume(double ChangeVolume) {
        this.ChangeVolume = ChangeVolume;
    }
}
