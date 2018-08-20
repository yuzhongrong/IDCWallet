package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

/**
 * Created by hpz on 2018/5/10.
 */

public class OTCDepositWaterBean implements Serializable{
    /**
     * FlowNo : 2018051117401500006
     * UserId : 4284
     * CoinId : 4
     * BookTypeCode : Out
     * CreateTime : 2018-05-11 17:40:15
     * Remark : 充值保证金
     * RelateOrderNo : null
     * OriginalBalance : 3002.0
     * ChangeBalance : 1009.0
     * Balance : 4011.0
     * OriginalFrozenNum : 0.0
     * ChangeFrozenNum : 0.0
     * FrozenNum : 0.0
     * MinerFee : 0.3027
     * Address : VB2za8LcwD5j5DxWbkwAGfNZfcaLgW3KyL
     * TXID : b82c4dfac513dc54a749f4403ea89afa1252187891efb5b84f06c446cc1dceae
     * PaymentType : 1
     * WithdrawFailure : null
     * WithdrawGuid : null
     * CoinCode : vhkd
     * Logo : http://192.168.1.36:8888//group1/M00/00/00/wKgBJFrwBxeARSFTAAAPgZGgQdY783.png
     * BookTypeLogo : http://192.168.1.36:8888//group1/M00/00/00/wKgBJFr1WwyAG5pnAAAERAD3ok0553.png
     * id : 1037
     */

    private String FlowNo;
    private int UserId;
    private int CoinId;
    private String BookTypeCode;
    private String CreateTime;
    private String Remark;
    private String RelateOrderNo;
    private String RelateOrderId;
    private double OriginalBalance;
    private double ChangeBalance;
    private double Balance;
    private double OriginalFrozenNum;
    private double ChangeFrozenNum;
    private double FrozenNum;
    private double MinerFee;
    private String Address;
    private String TXID;
    private int PaymentType;
    private Object WithdrawFailure;
    private Object WithdrawGuid;
    private String CoinCode;
    private String Logo;
    private String BookTypeLogo;
    private int id;

    public String getFlowNo() {
        return FlowNo;
    }

    public void setFlowNo(String FlowNo) {
        this.FlowNo = FlowNo;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public int getCoinId() {
        return CoinId;
    }

    public void setCoinId(int CoinId) {
        this.CoinId = CoinId;
    }

    public String getBookTypeCode() {
        return BookTypeCode;
    }

    public void setBookTypeCode(String BookTypeCode) {
        this.BookTypeCode = BookTypeCode;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getRelateOrderNo() {
        return RelateOrderNo;
    }

    public void setRelateOrderNo(String RelateOrderNo) {
        this.RelateOrderNo = RelateOrderNo;
    }

    public double getOriginalBalance() {
        return OriginalBalance;
    }

    public void setOriginalBalance(double OriginalBalance) {
        this.OriginalBalance = OriginalBalance;
    }

    public double getChangeBalance() {
        return ChangeBalance;
    }

    public void setChangeBalance(double ChangeBalance) {
        this.ChangeBalance = ChangeBalance;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }

    public double getOriginalFrozenNum() {
        return OriginalFrozenNum;
    }

    public void setOriginalFrozenNum(double OriginalFrozenNum) {
        this.OriginalFrozenNum = OriginalFrozenNum;
    }

    public double getChangeFrozenNum() {
        return ChangeFrozenNum;
    }

    public void setChangeFrozenNum(double ChangeFrozenNum) {
        this.ChangeFrozenNum = ChangeFrozenNum;
    }

    public double getFrozenNum() {
        return FrozenNum;
    }

    public void setFrozenNum(double FrozenNum) {
        this.FrozenNum = FrozenNum;
    }

    public double getMinerFee() {
        return MinerFee;
    }

    public void setMinerFee(double MinerFee) {
        this.MinerFee = MinerFee;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getTXID() {
        return TXID;
    }

    public void setTXID(String TXID) {
        this.TXID = TXID;
    }

    public int getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(int PaymentType) {
        this.PaymentType = PaymentType;
    }

    public Object getWithdrawFailure() {
        return WithdrawFailure;
    }

    public void setWithdrawFailure(Object WithdrawFailure) {
        this.WithdrawFailure = WithdrawFailure;
    }

    public Object getWithdrawGuid() {
        return WithdrawGuid;
    }

    public void setWithdrawGuid(Object WithdrawGuid) {
        this.WithdrawGuid = WithdrawGuid;
    }

    public String getCoinCode() {
        return CoinCode;
    }

    public void setCoinCode(String CoinCode) {
        this.CoinCode = CoinCode;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public String getBookTypeLogo() {
        return BookTypeLogo;
    }

    public void setBookTypeLogo(String BookTypeLogo) {
        this.BookTypeLogo = BookTypeLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelateOrderId() {
        return RelateOrderId;
    }

    public void setRelateOrderId(String relateOrderId) {
        RelateOrderId = relateOrderId;
    }
}
