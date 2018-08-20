package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

public class OTCLocalCurrencyResBean implements Serializable {

    private int id; // 法币ID
    private String LocalCurrencyCode;//法币

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalCurrencyCode() {
        return LocalCurrencyCode;
    }

    public void setLocalCurrencyCode(String localCurrencyCode) {
        LocalCurrencyCode = localCurrencyCode;
    }

    @Override
    public String toString() {
        return "OTCLocalCurrencyResBean{" +
                "id=" + id +
                ", LocalCurrencyCode='" + LocalCurrencyCode + '\'' +
                '}';
    }
}
