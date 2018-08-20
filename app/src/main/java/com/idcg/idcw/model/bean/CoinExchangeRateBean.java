package com.idcg.idcw.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.idcg.idcw.utils.Utils;

/**
 *
 * @author yiyang
 */
public class CoinExchangeRateBean implements Parcelable{

    /**
     * FromCoinId : 0
     * FromCoin : string
     * ToCoinId : 0
     * ToCoin : string
     * ExchangeRate : 0
     * ExchangeDate : 2018-03-23T04:49:50.282Z
     * ExchangeMin : 0
     * ExchangeMax : 0
     * CreateTime : 2018-03-23T04:49:50.282Z
     * CreateUserId : 0
     * UpdateTime : 2018-03-23T04:49:50.282Z
     * UpdateUserId : 0
     * id : 0
     */

    private int FromCoinId;
    private String FromCoin;
    private int ToCoinId;
    private String ToCoin;
    private double ExchangeRate;
    private String ExchangeDate;
    private double ExchangeMin;
    private double ExchangeMax;
    private String CreateTime;
    private int CreateUserId;
    private String UpdateTime;
    private int UpdateUserId;
    private int id;
    private String Digit;
    private int ToDigit;
    private int RateDigit = 8;
    /**Direction (boolean, optional): 方向（true汇率不变，false汇率取倒数） ,*/
    private boolean Direction;

    public boolean isDirection() {
        return Direction;
    }

    public void setDirection(boolean direction) {
        Direction = direction;
    }

    public String getDigit() {
        return Digit;
    }
    public int safeGetDigit(){
        int digest;
        try {
            digest = Integer.parseInt(getDigit());
        } catch (NumberFormatException e) {
            //限制位数，如果为0则设置为无穷大
            digest = 4;
            e.printStackTrace();
        }
        return digest;
    }

    public void setDigit(String digit) {
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

    public int getFromCoinId() {
        return FromCoinId;
    }

    public void setFromCoinId(int FromCoinId) {
        this.FromCoinId = FromCoinId;
    }

    public String getFromCoin() {
        return FromCoin;
    }

    public void setFromCoin(String FromCoin) {
        this.FromCoin = FromCoin;
    }

    public int getToCoinId() {
        return ToCoinId;
    }

    public void setToCoinId(int ToCoinId) {
        this.ToCoinId = ToCoinId;
    }

    public String getToCoin() {
        return ToCoin;
    }

    public void setToCoin(String ToCoin) {
        this.ToCoin = ToCoin;
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

    public void setExchangeRate(double ExchangeRate) {
        this.ExchangeRate = ExchangeRate;
    }

    public String getExchangeDate() {
        return ExchangeDate;
    }

    public void setExchangeDate(String ExchangeDate) {
        this.ExchangeDate = ExchangeDate;
    }

    public double getExchangeMin() {
        return ExchangeMin;
    }

    public void setExchangeMin(double ExchangeMin) {
        this.ExchangeMin = ExchangeMin;
    }

    public double getExchangeMax() {
        return ExchangeMax;
    }

    public void setExchangeMax(double ExchangeMax) {
        this.ExchangeMax = ExchangeMax;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.FromCoinId);
        dest.writeString(this.FromCoin);
        dest.writeInt(this.ToCoinId);
        dest.writeString(this.ToCoin);
        dest.writeDouble(this.ExchangeRate);
        dest.writeString(this.ExchangeDate);
        dest.writeDouble(this.ExchangeMin);
        dest.writeDouble(this.ExchangeMax);
        dest.writeString(this.CreateTime);
        dest.writeInt(this.CreateUserId);
        dest.writeString(this.UpdateTime);
        dest.writeInt(this.UpdateUserId);
        dest.writeInt(this.id);
        dest.writeString(this.Digit);
        dest.writeInt(this.ToDigit);
        dest.writeInt(this.RateDigit);
        dest.writeByte(this.Direction ? (byte) 1 : (byte) 0);
    }

    public CoinExchangeRateBean() {
    }

    protected CoinExchangeRateBean(Parcel in) {
        this.FromCoinId = in.readInt();
        this.FromCoin = in.readString();
        this.ToCoinId = in.readInt();
        this.ToCoin = in.readString();
        this.ExchangeRate = in.readDouble();
        this.ExchangeDate = in.readString();
        this.ExchangeMin = in.readDouble();
        this.ExchangeMax = in.readDouble();
        this.CreateTime = in.readString();
        this.CreateUserId = in.readInt();
        this.UpdateTime = in.readString();
        this.UpdateUserId = in.readInt();
        this.id = in.readInt();
        this.Digit = in.readString();
        this.ToDigit = in.readInt();
        this.RateDigit = in.readInt();
        this.Direction = in.readByte() != 0;
    }

    public static final Creator<CoinExchangeRateBean> CREATOR = new Creator<CoinExchangeRateBean>() {
        @Override
        public CoinExchangeRateBean createFromParcel(Parcel source) {
            return new CoinExchangeRateBean(source);
        }

        @Override
        public CoinExchangeRateBean[] newArray(int size) {
            return new CoinExchangeRateBean[size];
        }
    };
}
