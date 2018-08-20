package foxidcw.android.idcw.otc.model.beans;

/**
 * Created by hpz on 2018/5/5.
 */

public class OTCAddCurrParam {


    /**
     * id : 0
     * CoinId : 0
     * Max : 0
     * Min : 0
     * Direction : 1
     */

    private String id;
    private int CoinId;
    private double Max;
    private double Min;
    private int Direction;
    private double Premium;

    public OTCAddCurrParam(String id, int CoinId, double Max, double Min, int Direction,double Premium) {
        this.id = id;
        this.CoinId = CoinId;
        this.Max = Max;
        this.Min = Min;
        this.Direction = Direction;
        this.Premium = Premium;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCoinId() {
        return CoinId;
    }

    public void setCoinId(int CoinId) {
        this.CoinId = CoinId;
    }

    public double getMax() {
        return Max;
    }

    public void setMax(double Max) {
        this.Max = Max;
    }

    public double getMin() {
        return Min;
    }

    public void setMin(double Min) {
        this.Min = Min;
    }

    public int getDirection() {
        return Direction;
    }

    public void setDirection(int Direction) {
        this.Direction = Direction;
    }

    public double getPremium() {
        return Premium;
    }

    public void setPremium(double premium) {
        Premium = premium;
    }
}
