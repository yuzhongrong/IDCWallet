package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

/**
 * Created by yuzhongrong on 2018/5/14.
 */

public class OTCStateBean implements Serializable {


    /**
     * OrderID : 961
     * Status : 2
     * ExpiredDate : 2018-05-15T12:00:04
     * ExpiredTimestamp : 1526385604608
     * ExpiredSeconds : 53999
     * IsDelay : false
     * CancelCount : 0
     */

    private int OrderID;
    private int Status;
    private String ExpiredDate;
    private long ExpiredTimestamp;
    private int ExpiredSeconds;
    private boolean IsDelay;
    private int CancelCount;

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int OrderID) {
        this.OrderID = OrderID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String ExpiredDate) {
        this.ExpiredDate = ExpiredDate;
    }

    public long getExpiredTimestamp() {
        return ExpiredTimestamp;
    }

    public void setExpiredTimestamp(long ExpiredTimestamp) {
        this.ExpiredTimestamp = ExpiredTimestamp;
    }

    public int getExpiredSeconds() {
        return ExpiredSeconds;
    }

    public void setExpiredSeconds(int ExpiredSeconds) {
        this.ExpiredSeconds = ExpiredSeconds;
    }

    public boolean isIsDelay() {
        return IsDelay;
    }

    public void setIsDelay(boolean IsDelay) {
        this.IsDelay = IsDelay;
    }

    public int getCancelCount() {
        return CancelCount;
    }

    public void setCancelCount(int CancelCount) {
        this.CancelCount = CancelCount;
    }
}
