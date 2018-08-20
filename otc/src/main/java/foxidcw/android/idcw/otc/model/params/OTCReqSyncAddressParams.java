package foxidcw.android.idcw.otc.model.params;

/**
 * Created by hpz on 2018/5/16.
 */

public class OTCReqSyncAddressParams {
    private String  address;
    private String currency;

    public OTCReqSyncAddressParams(String address, String currency) {
        this.address = address;
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
