package foxidcw.android.idcw.otc.model.beans;

/**
 * Created by hpz on 2018/5/9.
 */

public class OTCDepositMgListBean {

    private int id;
    private int CoinId;
    private String CoinCode;
    private String CoinName;
    private String Logo;
    private double UseNum;
    private double Precision;
    private int Sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoinId() {
        return CoinId;
    }

    public void setCoinId(int coinId) {
        CoinId = coinId;
    }

    public String getCoinCode() {
        return CoinCode;
    }

    public void setCoinCode(String coinCode) {
        CoinCode = coinCode;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getCoinName() {
        return CoinName;
    }

    public void setCoinName(String coinName) {
        CoinName = coinName;
    }

    public double getUseNum() {
        return UseNum;
    }

    public void setUseNum(double useNum) {
        UseNum = useNum;
    }

    public double getPrecision() {
        return Precision;
    }

    public void setPrecision(double precision) {
        Precision = precision;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }
}
