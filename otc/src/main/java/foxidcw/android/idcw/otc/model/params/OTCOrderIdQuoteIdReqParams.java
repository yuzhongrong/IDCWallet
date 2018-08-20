package foxidcw.android.idcw.otc.model.params;

import java.io.Serializable;

public class OTCOrderIdQuoteIdReqParams implements Serializable {
    private int OrderId;
    private int QuoteOrderId;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getQuoteOrderId() {
        return QuoteOrderId;
    }

    public void setQuoteOrderId(int quoteOrderId) {
        QuoteOrderId = quoteOrderId;
    }
}
