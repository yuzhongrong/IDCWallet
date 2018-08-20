package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

public class OTCQuoteOrderPriceEventBean implements Serializable {
    private int OrderID;
    private double price;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OTCQuoteOrderPriceEventBean(int orderID) {
        OrderID = orderID;
    }

    public OTCQuoteOrderPriceEventBean(int orderID, double price) {
        OrderID = orderID;
        this.price = price;
    }
}
