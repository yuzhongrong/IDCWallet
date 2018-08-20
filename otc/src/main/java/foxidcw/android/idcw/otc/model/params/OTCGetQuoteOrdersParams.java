package foxidcw.android.idcw.otc.model.params;

import java.io.Serializable;

public class OTCGetQuoteOrdersParams implements Serializable {
    private String orderId;

    public OTCGetQuoteOrdersParams(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
