package foxidcw.android.idcw.otc.model.beans;

/**
 * Created by hpz on 2018/5/5.
 */

public class OTCAddAmountParam {

    /**
     * id : 0
     * CoinId : 0
     * Max : 0
     * Min : 0
     * Direction : 1
     */

    private String id;
    private int LocalCurrencyId;
    private double Amount;

    public OTCAddAmountParam(String id, int LocalCurrencyId, double Amount) {
        this.id = id;
        this.LocalCurrencyId = LocalCurrencyId;
        this.Amount = Amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLocalCurrencyId() {
        return LocalCurrencyId;
    }

    public void setLocalCurrencyId(int localCurrencyId) {
        LocalCurrencyId = localCurrencyId;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }
}
