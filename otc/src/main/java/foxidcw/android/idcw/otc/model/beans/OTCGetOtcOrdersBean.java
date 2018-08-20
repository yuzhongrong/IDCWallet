package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @project name : FoxIDCW
 * @class name :   OTCGetOtcOrdersBean
 * @package name : foxidcw.android.idcw.otc.model.beans
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/7 21:11
 * @describe :     OTCGetOtcOrders实体类
 *
 */
public class OTCGetOtcOrdersBean implements Serializable{

    /**
     * UserId : 6991
     * OrderNo : 20180507173741478900000049
     * Direction : 1
     * Num : 1000
     * CoinId : 4
     * CoinName : VHKD
     * CoinCode : vhkd
     * LocalCurrencyName : CNY
     * LocalCurrencySymbol : ￥
     * LocalCurrencyId : 1
     * AcceptantName : null
     * PaymentModeId : 1014
     * PaymentData : {"AccountNo":"不懂你的呢","BankBranch":"你到家大家","BankName":"大家见到你","UserName":"dhh四级考试dnd"}
     * PrevStatus : 0
     * Status : 9
     * StatusTime : 2018-05-07 17:40:41
     * IsTimeExpand : false
     * AcceptantUserId : 0
     * Amount : 0
     * UploadUserId : 0
     * CertificateImages : null
     * CreateUserId : 6991
     * CreateTime : 2018-05-07 17:37:41
     * UpdateUserId : null
     * UpdateTime : 2018-05-07 18:00:21
     * RowVersion : AAAAAAADyLM=
     * OfferOrderId : 0
     * IsBreak : false
     * PayTypeId : 6
     * IsTimeOut : false
     * CancelUserID : null
     * IsContact : false
     * IsTXOut : null
     * id : 50
     */

    private int UserId;
    private String UserName;
    private String  OrderNo;
    private int     Direction;
    private double  Num;
    private int     CoinId;
    private String  CoinName;
    private String  CoinCode;
    private String  LocalCurrencyName;
    private String  LocalCurrencySymbol;
    private int     LocalCurrencyId;
    private String  AcceptantName;
    private int     PaymentModeId;
    private String  PaymentData;
    private int     PrevStatus;
    private int     Status;
    private String  StatusTime;

    private long   StatusTimeSeconds;
    private boolean IsTimeExpand;
    private int     AcceptantUserId;
    private double  Amount;
    private int     UploadUserId;
    private List<String>  CertificateImages;
    private int     CreateUserId;
    private String  CreateTime;
    private long  CreateTimestamp;
    private int  UpdateUserId;
    private String  UpdateTime;
    private String  RowVersion;
    private int     OfferOrderId;
    private boolean IsBreak;
    private int     PayTypeId;
    private boolean IsTimeOut;
    private int  CancelUserID;
    private boolean IsContact;
    private boolean  IsTXOut;
    private int     id;
    private List<PaymentModeResponse> Payments;
    private String PayCertificateNO;
    private String AcceptantPhone;
    private String UserPhone;

    public int getUserId() { return UserId;}

    public void setUserId(int UserId) { this.UserId = UserId;}

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getOrderNo() { return OrderNo;}

    public void setOrderNo(String OrderNo) { this.OrderNo = OrderNo;}

    public int getDirection() { return Direction;}

    public void setDirection(int Direction) { this.Direction = Direction;}

    public double getNum() { return Num;}

    public void setNum(double Num) { this.Num = Num;}

    public int getCoinId() { return CoinId;}

    public void setCoinId(int CoinId) { this.CoinId = CoinId;}

    public String getCoinName() { return CoinName;}

    public void setCoinName(String CoinName) { this.CoinName = CoinName;}

    public String getCoinCode() { return CoinCode;}

    public void setCoinCode(String CoinCode) { this.CoinCode = CoinCode;}

    public String getLocalCurrencyName() { return LocalCurrencyName;}

    public void setLocalCurrencyName(String LocalCurrencyName) { this.LocalCurrencyName = LocalCurrencyName;}

    public String getLocalCurrencySymbol() { return LocalCurrencySymbol;}

    public void setLocalCurrencySymbol(String LocalCurrencySymbol) { this.LocalCurrencySymbol = LocalCurrencySymbol;}

    public int getLocalCurrencyId() { return LocalCurrencyId;}

    public void setLocalCurrencyId(int LocalCurrencyId) { this.LocalCurrencyId = LocalCurrencyId;}

    public String getAcceptantName() { return AcceptantName;}

    public void setAcceptantName(String AcceptantName) { this.AcceptantName = AcceptantName;}

    public int getPaymentModeId() { return PaymentModeId;}

    public void setPaymentModeId(int PaymentModeId) { this.PaymentModeId = PaymentModeId;}

    public String getPaymentData() { return PaymentData;}

    public void setPaymentData(String PaymentData) { this.PaymentData = PaymentData;}

    public int getPrevStatus() { return PrevStatus;}

    public void setPrevStatus(int PrevStatus) { this.PrevStatus = PrevStatus;}

    public int getStatus() { return Status;}

    public void setStatus(int Status) { this.Status = Status;}

    public String getStatusTime() { return StatusTime;}

    public void setStatusTime(String StatusTime) { this.StatusTime = StatusTime;}


    public long getStatusTimeSeconds() {
        return StatusTimeSeconds;
    }

    public void setStatusTimeSeconds(long statusTimeSeconds) {
        StatusTimeSeconds = statusTimeSeconds;
    }


    public boolean isIsTimeExpand() { return IsTimeExpand;}

    public void setIsTimeExpand(boolean IsTimeExpand) { this.IsTimeExpand = IsTimeExpand;}

    public int getAcceptantUserId() { return AcceptantUserId;}

    public void setAcceptantUserId(int AcceptantUserId) { this.AcceptantUserId = AcceptantUserId;}

    public double getAmount() { return Amount;}

    public void setAmount(double Amount) { this.Amount = Amount;}

    public int getUploadUserId() { return UploadUserId;}

    public void setUploadUserId(int UploadUserId) { this.UploadUserId = UploadUserId;}

    public List<String> getCertificateImages() { return CertificateImages;}

    public void setCertificateImages(List<String> CertificateImages) { this.CertificateImages = CertificateImages;}

    public int getCreateUserId() { return CreateUserId;}

    public void setCreateUserId(int CreateUserId) { this.CreateUserId = CreateUserId;}

    public String getCreateTime() { return CreateTime;}

    public void setCreateTime(String CreateTime) { this.CreateTime = CreateTime;}

    public long getCreateTimestamp() {
        return CreateTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        CreateTimestamp = createTimestamp;
    }

    public int getUpdateUserId() { return UpdateUserId;}

    public void setUpdateUserId(int UpdateUserId) { this.UpdateUserId = UpdateUserId;}

    public String getUpdateTime() { return UpdateTime;}

    public void setUpdateTime(String UpdateTime) { this.UpdateTime = UpdateTime;}

    public String getRowVersion() { return RowVersion;}

    public void setRowVersion(String RowVersion) { this.RowVersion = RowVersion;}

    public int getOfferOrderId() { return OfferOrderId;}

    public void setOfferOrderId(int OfferOrderId) { this.OfferOrderId = OfferOrderId;}

    public boolean isIsBreak() { return IsBreak;}

    public void setIsBreak(boolean IsBreak) { this.IsBreak = IsBreak;}

    public int getPayTypeId() { return PayTypeId;}

    public void setPayTypeId(int PayTypeId) { this.PayTypeId = PayTypeId;}

    public boolean isIsTimeOut() { return IsTimeOut;}

    public void setIsTimeOut(boolean IsTimeOut) { this.IsTimeOut = IsTimeOut;}

    public int getCancelUserID() { return CancelUserID;}

    public void setCancelUserID(int CancelUserID) { this.CancelUserID = CancelUserID;}

    public boolean isIsContact() { return IsContact;}

    public void setIsContact(boolean IsContact) { this.IsContact = IsContact;}

    public boolean getIsTXOut() { return IsTXOut;}

    public void setIsTXOut(boolean IsTXOut) { this.IsTXOut = IsTXOut;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public List<PaymentModeResponse> getPayments() {
        return Payments;
    }

    public void setPayments(List<PaymentModeResponse> payments) {
        Payments = payments;
    }

    public String getPayCertificateNO() {
        return PayCertificateNO;
    }

    public void setPayCertificateNO(String payCertificateNO) {
        PayCertificateNO = payCertificateNO;
    }

    public String getAcceptantPhone() {
        return AcceptantPhone;
    }

    public void setAcceptantPhone(String acceptantPhone) {
        AcceptantPhone = acceptantPhone;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public class PaymentModeResponse{
//        ID (integer, optional): 主键id ,
//        LocalCurrencyId (integer, optional): 法币id ,
//        LocalCurrencyCode (string, optional): 法币Code ,
//        LocalCurrencyLogo (string, optional): 法币LOGO ,
//        PayTypeId (integer, optional): 支付方式id ,
//        PayTypeCode (string, optional): 支付方式Code ,
//        PayTypeLogo (string, optional): 支付方式logo ,
//        PayAttributes (inline_model_0, optional): 扩展属性
        private String ID ;
        private int LocalCurrencyId ;
        private String LocalCurrencyCode ;
        private String LocalCurrencyLogo ;
        private int PayTypeId ;
        private String PayTypeCode ;
        private String PayTypeLogo ;
        private PayAttributeBean PayAttributes ;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public int getLocalCurrencyId() {
            return LocalCurrencyId;
        }

        public void setLocalCurrencyId(int localCurrencyId) {
            LocalCurrencyId = localCurrencyId;
        }

        public String getLocalCurrencyCode() {
            return LocalCurrencyCode;
        }

        public void setLocalCurrencyCode(String localCurrencyCode) {
            LocalCurrencyCode = localCurrencyCode;
        }

        public String getLocalCurrencyLogo() {
            return LocalCurrencyLogo;
        }

        public void setLocalCurrencyLogo(String localCurrencyLogo) {
            LocalCurrencyLogo = localCurrencyLogo;
        }

        public int getPayTypeId() {
            return PayTypeId;
        }

        public void setPayTypeId(int payTypeId) {
            PayTypeId = payTypeId;
        }

        public String getPayTypeCode() {
            return PayTypeCode;
        }

        public void setPayTypeCode(String payTypeCode) {
            PayTypeCode = payTypeCode;
        }

        public String getPayTypeLogo() {
            return PayTypeLogo;
        }

        public void setPayTypeLogo(String payTypeLogo) {
            PayTypeLogo = payTypeLogo;
        }

        public PayAttributeBean getPayAttributes() {
            return PayAttributes;
        }

        public void setPayAttributes(PayAttributeBean payAttributes) {
            PayAttributes = payAttributes;
        }

        public class PayAttributeBean{
            private String UserName;
            private String AccountNo;
            private String BankName;
            private String SwiftCode;
            private String City;
            private String BankAddress;
            private String BankBranch;
            private String QRCode;

            public String getQRCode() {
                return QRCode;
            }

            public void setQRCode(String QRCode) {
                this.QRCode = QRCode;
            }

            public String getBankBranch() {
                return BankBranch;
            }

            public void setBankBranch(String bankBranch) {
                BankBranch = bankBranch;
            }

            public String getUserName() {
                return UserName;
            }

            public void setUserName(String userName) {
                UserName = userName;
            }

            public String getAccountNo() {
                return AccountNo;
            }

            public void setAccountNo(String accountNo) {
                AccountNo = accountNo;
            }

            public String getBankName() {
                return BankName;
            }

            public void setBankName(String bankName) {
                BankName = bankName;
            }

            public String getSwiftCode() {
                return SwiftCode;
            }

            public void setSwiftCode(String swiftCode) {
                SwiftCode = swiftCode;
            }

            public String getCity() {
                return City;
            }

            public void setCity(String city) {
                City = city;
            }

            public String getBankAddress() {
                return BankAddress;
            }

            public void setBankAddress(String bankAddress) {
                BankAddress = bankAddress;
            }

            @Override
            public String toString() {
                return "PayArrributes{" +
                        "UserName='" + UserName + '\'' +
                        ", AccountNo='" + AccountNo + '\'' +
                        ", BankName='" + BankName + '\'' +
                        ", SwiftCode='" + SwiftCode + '\'' +
                        ", City='" + City + '\'' +
                        ", BankAddress='" + BankAddress + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "OTCGetOtcOrdersBean{" + "UserId=" + UserId + ", OrderNo='" + OrderNo + '\'' + ", Direction=" + Direction + ", Num=" + Num + ", CoinId=" + CoinId + ", CoinName='" + CoinName + '\'' + ", CoinCode='" + CoinCode + '\'' + ", LocalCurrencyName='" + LocalCurrencyName + '\'' + ", LocalCurrencySymbol='" + LocalCurrencySymbol + '\'' + ", LocalCurrencyId=" + LocalCurrencyId + ", AcceptantName=" + AcceptantName + ", PaymentModeId=" + PaymentModeId + ", PaymentData='" + PaymentData + '\'' + ", PrevStatus=" + PrevStatus + ", Status=" + Status + ", StatusTime='" + StatusTime + '\'' + ", IsTimeExpand=" + IsTimeExpand + ", AcceptantUserId=" + AcceptantUserId + ", Amount=" + Amount + ", UploadUserId=" + UploadUserId + ", CertificateImages=" + CertificateImages + ", CreateUserId=" + CreateUserId + ", CreateTime='" + CreateTime + '\'' + ", UpdateUserId=" + UpdateUserId + ", UpdateTime='" + UpdateTime + '\'' + ", RowVersion='" + RowVersion + '\'' + ", OfferOrderId=" + OfferOrderId + ", IsBreak=" + IsBreak + ", PayTypeId=" + PayTypeId + ", IsTimeOut=" + IsTimeOut + ", CancelUserID=" + CancelUserID + ", IsContact=" + IsContact + ", IsTXOut=" + IsTXOut + ", id=" + id + '}';
    }
}
