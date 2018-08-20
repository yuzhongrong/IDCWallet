package foxidcw.android.idcw.otc.model.params;

import java.io.Serializable;

public class OTCQuotePriceReqParams implements Serializable {
    private int OrderId;
    private double QuotePrice;
    private String OfferOrderId;

    public String getOfferOrderId() {
        return OfferOrderId;
    }

    public void setOfferOrderId(String offerOrderId) {
        OfferOrderId = offerOrderId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public double getQuotePrice() {
        return QuotePrice;
    }

    public void setQuotePrice(double quotePrice) {
        QuotePrice = quotePrice;
    }
}
