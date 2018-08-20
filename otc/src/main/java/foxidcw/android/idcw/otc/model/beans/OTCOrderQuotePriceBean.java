package foxidcw.android.idcw.otc.model.beans;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class OTCOrderQuotePriceBean implements Serializable, Comparable<OTCOrderQuotePriceBean> {

    private int OrderId; // 订单ID
    private int UserId; // 用户ID
    private int QuoteId; // 报价单ID
    private double Amount; // 报价总金额
    private String QuoteTime; // 申报时间
    private String AcceptantName; // 承兑商名称
    private int AppealCount; // 申诉次数
    private int AvgResponseTime; // 平均响应时间单位秒
    private int AvgPayTime; // 平均支付时间单位秒
    private int AvgUploadPayTime; // 平均上传支付凭证时间单位秒
    private int AvgConfirmReceiveTime; // 平均确认时间单位秒
    private String PayLogo; // 支付图标
    private int compareType; // 0 表示价格  1 表示平均支付时间
    private boolean isBuy; // 如果是买单  则看平均支付时间  如果是卖  则看平均响应时间
    private List<PayTypes> PayTypes; // 收款方式

    public String getPayLogo() {
        return PayLogo;
    }

    public void setPayLogo(String payLogo) {
        PayLogo = payLogo;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getQuoteId() {
        return QuoteId;
    }

    public void setQuoteId(int quoteId) {
        QuoteId = quoteId;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getQuoteTime() {
        return QuoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        QuoteTime = quoteTime;
    }

    public String getAcceptantName() {
        return AcceptantName;
    }

    public void setAcceptantName(String acceptantName) {
        AcceptantName = acceptantName;
    }

    public int getAppealCount() {
        return AppealCount;
    }

    public void setAppealCount(int appealCount) {
        AppealCount = appealCount;
    }

    public int getAvgResponseTime() {
        return AvgResponseTime;
    }

    public void setAvgResponseTime(int avgResponseTime) {
        AvgResponseTime = avgResponseTime;
    }

    public int getAvgPayTime() {
        return AvgPayTime;
    }

    public void setAvgPayTime(int avgPayTime) {
        AvgPayTime = avgPayTime;
    }

    public int getAvgUploadPayTime() {
        return AvgUploadPayTime;
    }

    public void setAvgUploadPayTime(int avgUploadPayTime) {
        AvgUploadPayTime = avgUploadPayTime;
    }

    public int getAvgConfirmReceiveTime() {
        return AvgConfirmReceiveTime;
    }

    public void setAvgConfirmReceiveTime(int avgConfirmReceiveTime) {
        AvgConfirmReceiveTime = avgConfirmReceiveTime;
    }

    public int getCompareType() {
        return compareType;
    }

    public void setCompareType(int compareType) {
        this.compareType = compareType;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    @Override
    public String toString() {
        return "OTCOrderQuotePriceBean{" +
                "OrderId=" + OrderId +
                ", UserId=" + UserId +
                ", QuoteId=" + QuoteId +
                ", Amount=" + Amount +
                ", QuoteTime='" + QuoteTime + '\'' +
                ", AcceptantName='" + AcceptantName + '\'' +
                ", AppealCount=" + AppealCount +
                ", AvgResponseTime=" + AvgResponseTime +
                ", AvgPayTime=" + AvgPayTime +
                ", AvgUploadPayTime=" + AvgUploadPayTime +
                ", AvgConfirmReceiveTime=" + AvgConfirmReceiveTime +
                '}';
    }

    public List<OTCOrderQuotePriceBean.PayTypes> getPayTypes() {
        return PayTypes;
    }

    public void setPayTypes(List<OTCOrderQuotePriceBean.PayTypes> payTypes) {
        PayTypes = payTypes;
    }

    @Override
    public int compareTo(@NonNull OTCOrderQuotePriceBean o) {
        boolean isBuy = isBuy();
        if (compareType == 0) {
            if (!isBuy) {
                return Double.compare(o.getAmount(), this.getAmount());
            } else {
                return Double.compare(this.getAmount(), o.getAmount());
            }
        } else {
            if (isBuy) {
                return this.getAvgPayTime() - o.getAvgPayTime();
            } else {
                return this.getAvgResponseTime() - o.getAvgResponseTime();
            }
        }
    }

    public static class PayTypes {
        private String PayCode;
        private String PayLogo;

        public String getPayCode() {
            return PayCode;
        }

        public void setPayCode(String payCode) {
            PayCode = payCode;
        }

        public String getPayLogo() {
            return PayLogo;
        }

        public void setPayLogo(String payLogo) {
            PayLogo = payLogo;
        }
    }
}

