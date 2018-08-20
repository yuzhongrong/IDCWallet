package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

public class OTCNewOrderNoticeAcceptantBean implements Serializable {
    private int AcceptantId;
    private int OfferOrderID;
    private int OrderID;
    private String UserName;
    private int UserId;
    private int CoinId;
    private int CurrencyId;
    private String CurrencyName;
    private String CurrencyCode;
    private String CoinName;
    private String CoinCode;
    private double Amount; // 总价
    private String PayCode;
    private String PayLogo;
    private int Direction;//1买2卖
    private String DeadLineTime;
    private long DeadLineTimestamp;
    private int DeadLineSeconds;
    private String OrderNO;
    private int OrderStatus;
    private long CreateTimestamp;
    private String CreateDate;
    public boolean IsPremium;
    private int currentDeadLineSeconds;
    private int Status;
    private int UserAppealCount;
    private int UserAvgPayTime;
    private int UserAvgConfirmReceiveTime;
    private int UserAvgSelloutTime;
    private int UserAvgBuyinTime;
    private int maxDeadLineSeconds;
    private double Price;
    private double Num; // 虚拟币数量
    private double LastPrice; // 市场价
    private double Premium; // 溢价比例

    public double getNum() {
        return Num;
    }

    public void setNum(double num) {
        Num = num;
    }

    public int getOfferOrderID() {
        return OfferOrderID;
    }

    public void setOfferOrderID(int offerOrderID) {
        OfferOrderID = offerOrderID;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getAcceptantId() {
        return AcceptantId;
    }

    public void setAcceptantId(int acceptantId) {
        AcceptantId = acceptantId;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getCoinId() {
        return CoinId;
    }

    public void setCoinId(int coinId) {
        CoinId = coinId;
    }

    public int getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(int currencyId) {
        CurrencyId = currencyId;
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    public void setCurrencyName(String currencyName) {
        CurrencyName = currencyName;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getCoinName() {
        return CoinName;
    }

    public void setCoinName(String coinName) {
        CoinName = coinName;
    }

    public String getCoinCode() {
        return CoinCode;
    }

    public void setCoinCode(String coinCode) {
        CoinCode = coinCode;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

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

    public int getDirection() {
        return Direction;
    }

    public void setDirection(int direction) {
        Direction = direction;
    }

    public String getDeadLineTime() {
        return DeadLineTime;
    }

    public void setDeadLineTime(String deadLineTime) {
        DeadLineTime = deadLineTime;
    }

    public long getDeadLineTimestamp() {
        return DeadLineTimestamp;
    }

    public void setDeadLineTimestamp(long deadLineTimestamp) {
        DeadLineTimestamp = deadLineTimestamp;
    }

    public int getDeadLineSeconds() {
        return DeadLineSeconds;
    }

    public void setDeadLineSeconds(int deadLineSeconds) {
        DeadLineSeconds = deadLineSeconds;
    }

    public String getOrderNO() {
        return OrderNO;
    }

    public void setOrderNO(String orderNO) {
        OrderNO = orderNO;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public long getCreateTimestamp() {
        return CreateTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        CreateTimestamp = createTimestamp;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public boolean isPremium() {
        return IsPremium;
    }

    public void setPremium(boolean premium) {
        IsPremium = premium;
    }

    public int getCurrentDeadLineSeconds() {
        return currentDeadLineSeconds;
    }

    public void setCurrentDeadLineSeconds(int currentDeadLineSeconds) {
        this.currentDeadLineSeconds = currentDeadLineSeconds;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getUserAppealCount() {
        return UserAppealCount;
    }

    public void setUserAppealCount(int userAppealCount) {
        UserAppealCount = userAppealCount;
    }

    public int getUserAvgPayTime() {
        return UserAvgPayTime;
    }

    public void setUserAvgPayTime(int userAvgPayTime) {
        UserAvgPayTime = userAvgPayTime;
    }

    public int getUserAvgConfirmReceiveTime() {
        return UserAvgConfirmReceiveTime;
    }

    public void setUserAvgConfirmReceiveTime(int userAvgConfirmReceiveTime) {
        UserAvgConfirmReceiveTime = userAvgConfirmReceiveTime;
    }

    public int getUserAvgSelloutTime() {
        return UserAvgSelloutTime;
    }

    public void setUserAvgSelloutTime(int userAvgSelloutTime) {
        UserAvgSelloutTime = userAvgSelloutTime;
    }

    public int getUserAvgBuyinTime() {
        return UserAvgBuyinTime;
    }

    public void setUserAvgBuyinTime(int userAvgBuyinTime) {
        UserAvgBuyinTime = userAvgBuyinTime;
    }

    public int getMaxDeadLineSeconds() {
        return maxDeadLineSeconds;
    }

    public void setMaxDeadLineSeconds(int maxDeadLineSeconds) {
        this.maxDeadLineSeconds = maxDeadLineSeconds;
    }

    public double getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(double lastPrice) {
        LastPrice = lastPrice;
    }

    public double getPremium() {
        return Premium;
    }

    public void setPremium(double premium) {
        Premium = premium;
    }

    @Override
    public String toString() {
        return "OTCNewOrderNoticeAcceptantBean{" +
                "AcceptantId=" + AcceptantId +
                ", OfferOrderID=" + OfferOrderID +
                ", OrderID=" + OrderID +
                ", UserName='" + UserName + '\'' +
                ", UserId=" + UserId +
                ", CoinId=" + CoinId +
                ", CurrencyId=" + CurrencyId +
                ", CurrencyName='" + CurrencyName + '\'' +
                ", CurrencyCode='" + CurrencyCode + '\'' +
                ", CoinName='" + CoinName + '\'' +
                ", CoinCode='" + CoinCode + '\'' +
                ", Amount=" + Amount +
                ", PayCode='" + PayCode + '\'' +
                ", PayLogo='" + PayLogo + '\'' +
                ", Direction=" + Direction +
                ", DeadLineTime='" + DeadLineTime + '\'' +
                ", DeadLineTimestamp=" + DeadLineTimestamp +
                ", DeadLineSeconds=" + DeadLineSeconds +
                ", OrderNO='" + OrderNO + '\'' +
                ", OrderStatus=" + OrderStatus +
                ", CreateTimestamp=" + CreateTimestamp +
                ", CreateDate='" + CreateDate + '\'' +
                ", IsPremium=" + IsPremium +
                ", currentDeadLineSeconds=" + currentDeadLineSeconds +
                ", Status=" + Status +
                ", UserAppealCount=" + UserAppealCount +
                ", UserAvgPayTime=" + UserAvgPayTime +
                ", UserAvgConfirmReceiveTime=" + UserAvgConfirmReceiveTime +
                ", UserAvgSelloutTime=" + UserAvgSelloutTime +
                ", UserAvgBuyinTime=" + UserAvgBuyinTime +
                ", maxDeadLineSeconds=" + maxDeadLineSeconds +
                ", Price=" + Price +
                ", Num=" + Num +
                ", LastPrice=" + LastPrice +
                ", Premium=" + Premium +
                '}';
    }
}
