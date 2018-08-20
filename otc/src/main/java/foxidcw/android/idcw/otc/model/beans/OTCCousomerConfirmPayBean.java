package foxidcw.android.idcw.otc.model.beans;

/**
 * Created by yuzhongrong on 2018/5/11.
 */

public class OTCCousomerConfirmPayBean {


    /**
     * OrderId : 0：订单号
     * Status : 0：订单状态['0', '1', '2', '3', '4', '5', '6', '7', '8', '8', '9', '10', '11'],
     * StatusTime : 2018-05-11T02:55:33.961Z：截至时间
     * LimitSecond : 0：最大限制时间，单位秒
     * CancelOrderCount : 0：撤销订单数
     */


    private int OrderId;
    private int Status;
    private String StatusTime;
    private int LimitSecond;
    private int CancelOrderCount;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getStatusTime() {
        return StatusTime;
    }

    public void setStatusTime(String StatusTime) {
        this.StatusTime = StatusTime;
    }

    public int getLimitSecond() {
        return LimitSecond;
    }

    public void setLimitSecond(int LimitSecond) {
        this.LimitSecond = LimitSecond;
    }

    public int getCancelOrderCount() {
        return CancelOrderCount;
    }

    public void setCancelOrderCount(int CancelOrderCount) {
        this.CancelOrderCount = CancelOrderCount;
    }
}
