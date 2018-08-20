package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

public class OTCConfirmOrderResBean implements Serializable {
    private int OrderId;
    private int Status;
    private String StatusTime;
    private long LimitSecond;
    private int CancelOrderCount;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStatusTime() {
        return StatusTime;
    }

    public void setStatusTime(String statusTime) {
        StatusTime = statusTime;
    }

    public long getLimitSecond() {
        return LimitSecond;
    }

    public void setLimitSecond(long limitSecond) {
        LimitSecond = limitSecond;
    }

    public int getCancelOrderCount() {
        return CancelOrderCount;
    }

    public void setCancelOrderCount(int cancelOrderCount) {
        CancelOrderCount = cancelOrderCount;
    }

    @Override
    public String toString() {
        return "OTCConfirmOrderResBean{" +
                "OrderId=" + OrderId +
                ", Status=" + Status +
                ", StatusTime='" + StatusTime + '\'' +
                ", LimitSecond=" + LimitSecond +
                ", CancelOrderCount=" + CancelOrderCount +
                '}';
    }
}
