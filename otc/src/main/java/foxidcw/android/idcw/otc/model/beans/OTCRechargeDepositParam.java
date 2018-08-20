package foxidcw.android.idcw.otc.model.beans;

/**
 * Created by hpz on 2018/5/9.
 */

public class OTCRechargeDepositParam {
    private int CoinId;
    private double Amount;
    private String SysWalletAddress;
    private String PIN;
    private String DeviceId;

    public OTCRechargeDepositParam(int CoinId, double Amount, String SysWalletAddress, String PIN, String DeviceId) {
        this.CoinId = CoinId;
        this.Amount = Amount;
        this.SysWalletAddress = SysWalletAddress;
        this.PIN = PIN;
        this.DeviceId = DeviceId;
    }

    public int getCoinId() {
        return CoinId;
    }

    public void setCoinId(int coinId) {
        CoinId = coinId;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getSysWalletAddress() {
        return SysWalletAddress;
    }

    public void setSysWalletAddress(String sysWalletAddress) {
        SysWalletAddress = sysWalletAddress;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }
}
