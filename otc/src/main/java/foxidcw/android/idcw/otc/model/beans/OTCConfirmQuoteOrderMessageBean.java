package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

public class OTCConfirmQuoteOrderMessageBean implements Serializable {
    private int OrderId;
    private int QuoteOrderId;
    private int AcceptantUserId;

    public int getAcceptantUserId() {
        return AcceptantUserId;
    }

    public void setAcceptantUserId(int acceptantUserId) {
        AcceptantUserId = acceptantUserId;
    }

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

    @Override
    public String toString() {
        return "OTCConfirmQuoteOrderMessageBean{" +
                "OrderId=" + OrderId +
                ", QuoteOrderId=" + QuoteOrderId +
                ", AcceptantUserId=" + AcceptantUserId +
                '}';
    }
}
