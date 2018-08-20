package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

/**
 * Created by hpz on 2018/5/5.
 */

public class OTCCoinListBean implements Serializable {
    /**
     * id : 4
     * CoinCode : vhkd
     */

    private int id;
    //@SerializedName("LocalCurrencyCode")
    private String CoinCode;
    private String LocalCurrencyCode;
    private String Logo;
    private double minBuyQuantity;
    private double maxBuyQuantity;
    private int Digit ;
    private double minSellQuantity;
    private double maxSellQuantity;
    private String SysWalletAddress;
    private double DepositBanlance;
    private double MinAmount;

    public OTCCoinListBean(){}

    public OTCCoinListBean(int id, String coinCode, String localCurrencyCode, String Logo, int type) {
        this.id = id;
        this.Logo = Logo;
        if(type == 1){ //虚拟币
            CoinCode = coinCode;
        }else if(type == 2){ //法币
            LocalCurrencyCode = localCurrencyCode;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoinCode() {
        return CoinCode;
    }

    public void setCoinCode(String CoinCode) {
        this.CoinCode = CoinCode;
    }

    public String getLocalCurrencyCode() {
        return LocalCurrencyCode;
    }

    public void setLocalCurrencyCode(String localCurrencyCode) {
        LocalCurrencyCode = localCurrencyCode;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public double getMinBuyQuantity() {
        return minBuyQuantity;
    }

    public void setMinBuyQuantity(double minBuyQuantity) {
        this.minBuyQuantity = minBuyQuantity;
    }

    public double getMaxBuyQuantity() {
        return maxBuyQuantity;
    }

    public void setMaxBuyQuantity(double maxBuyQuantity) {
        this.maxBuyQuantity = maxBuyQuantity;
    }

    public int getDigit() {
        return Digit;
    }

    public void setDigit(int digit) {
        Digit = digit;
    }

    public double getMinSellQuantity() {
        return minSellQuantity;
    }

    public void setMinSellQuantity(double minSellQuantity) {
        this.minSellQuantity = minSellQuantity;
    }

    public double getMaxSellQuantity() {
        return maxSellQuantity;
    }

    public void setMaxSellQuantity(double maxSellQuantity) {
        this.maxSellQuantity = maxSellQuantity;
    }

    public String getSysWalletAddress() {
        return SysWalletAddress;
    }

    public void setSysWalletAddress(String sysWalletAddress) {
        SysWalletAddress = sysWalletAddress;
    }

    public double getDepositBanlance() {
        return DepositBanlance;
    }

    public void setDepositBanlance(double depositBanlance) {
        DepositBanlance = depositBanlance;
    }

    public double getMinAmount() {
        return MinAmount;
    }

    public void setMinAmount(double minAmount) {
        MinAmount = minAmount;
    }

    @Override
    public String toString() {
        return "OTCCoinListBean{" + "id=" + id + ", CoinCode='" + CoinCode + '\'' + ", LocalCurrencyCode='" + LocalCurrencyCode + '\'' + ", Logo='" + Logo + '\'' + ", minBuyQuantity=" + minBuyQuantity + ", maxBuyQuantity=" + maxBuyQuantity + ", Digit=" + Digit + ", minSellQuantity=" + minSellQuantity + ", maxSellQuantity=" + maxSellQuantity + '}';
    }
}
