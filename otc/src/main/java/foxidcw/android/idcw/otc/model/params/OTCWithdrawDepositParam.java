package foxidcw.android.idcw.otc.model.params;

/**
 * Created by hpz on 2018/5/9.
 */

public class OTCWithdrawDepositParam {
    private int CoinId;
    private double Amount;
    private String Address;
    private String PIN;
    private String DeviceId;

    public OTCWithdrawDepositParam(int CoinId, double Amount, String Address,String PIN,String DeviceId) {
        this.CoinId = CoinId;
        this.Amount = Amount;
        this.Address = Address;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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
