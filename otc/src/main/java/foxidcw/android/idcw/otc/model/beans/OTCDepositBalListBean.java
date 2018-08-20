package foxidcw.android.idcw.otc.model.beans;

/**
 * Created by hpz on 2018/5/11.
 */

public class OTCDepositBalListBean {
    /**
     * id : 4
     * CoinCode : vhkd
     * Logo : http://192.168.1.36:8888//group1/M00/00/00/wKgBJFrwBxeARSFTAAAPgZGgQdY783.png
     * MinAmount : 0
     * SysWalletAddress : null
     * DepositBanlance : 4011
     */

    private int id;
    private String CoinCode;
    private String Logo;
    private int MinAmount;
    private String SysWalletAddress;
    private int DepositBanlance;

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

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public int getMinAmount() {
        return MinAmount;
    }

    public void setMinAmount(int MinAmount) {
        this.MinAmount = MinAmount;
    }

    public String getSysWalletAddress() {
        return SysWalletAddress;
    }

    public void setSysWalletAddress(String SysWalletAddress) {
        this.SysWalletAddress = SysWalletAddress;
    }

    public int getDepositBanlance() {
        return DepositBanlance;
    }

    public void setDepositBanlance(int DepositBanlance) {
        this.DepositBanlance = DepositBanlance;
    }
}
