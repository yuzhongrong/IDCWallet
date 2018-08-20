package foxidcw.android.idcw.otc.model.params;

import java.io.Serializable;

public class OTCAddOrEditReqParams implements Serializable {
    private int ID; // 列表返回ID 添加为空，编辑不能为空
    private int LocalCurrencyId; // 法币ID
    private String LocalCurrencyCode; // 法币Code
    private int PayTypeId; //支付方式Id
    private String PayTypeCode;//支付方式Code
    private PayAttributes PayAttributes; // 参数信息
    private String LocalCurrencyLogo;
    private String PayTypeLogo;
    private String PIN;
    private String DeviceId;

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public int getID() {
        return ID;
    }

    public String getLocalCurrencyLogo() {
        return LocalCurrencyLogo;
    }

    public void setLocalCurrencyLogo(String localCurrencyLogo) {
        LocalCurrencyLogo = localCurrencyLogo;
    }

    public String getPayTypeLogo() {
        return PayTypeLogo;
    }

    public void setPayTypeLogo(String payTypeLogo) {
        PayTypeLogo = payTypeLogo;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLocalCurrencyId() {
        return LocalCurrencyId;
    }

    public void setLocalCurrencyId(int localCurrencyId) {
        LocalCurrencyId = localCurrencyId;
    }

    public int getPayTypeId() {
        return PayTypeId;
    }

    public void setPayTypeId(int payTypeId) {
        PayTypeId = payTypeId;
    }

    public OTCAddOrEditReqParams.PayAttributes getPayAttributes() {
        return PayAttributes;
    }

    public void setPayAttributes(OTCAddOrEditReqParams.PayAttributes payAttributes) {
        PayAttributes = payAttributes;
    }

    public String getLocalCurrencyCode() {
        return LocalCurrencyCode;
    }

    public void setLocalCurrencyCode(String localCurrencyCode) {
        LocalCurrencyCode = localCurrencyCode;
    }

    public String getPayTypeCode() {
        return PayTypeCode;
    }

    public void setPayTypeCode(String payTypeCode) {
        PayTypeCode = payTypeCode;
    }

    @Override
    public String toString() {
        return "OTCAddOrEditReqParams{" +
                "id=" + ID +
                ", LocalCurrencyId=" + LocalCurrencyId +
                ", LocalCurrencyCode='" + LocalCurrencyCode + '\'' +
                ", PaytypeId=" + PayTypeId +
                ", PayTypeCode='" + PayTypeCode + '\'' +
                ", PayArrributes=" + PayAttributes +
                '}';
    }

    public static class PayAttributes implements Serializable {
        private String UserName;
        private String AccountNo;
        private String BankNo;
        private String BankNoHide;
        private String BankName;
        private String SwiftCode;
        private String City;
        private String BankAddress;
        private String BankBranch;
        private String QRCode; // 支付宝微信二维码地址

        public String getQRCode() {
            return QRCode;
        }

        public void setQRCode(String QRCode) {
            this.QRCode = QRCode;
        }

        public String getBankNo() {
            return BankNo;
        }

        public void setBankNo(String bankNo) {
            BankNo = bankNo;
        }

        public String getBankNoHide() {
            return BankNoHide;
        }

        public void setBankNoHide(String bankNoHide) {
            BankNoHide = bankNoHide;
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
