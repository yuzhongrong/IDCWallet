package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

/**
 * Created by hpz on 2018/5/7.
 */

public class OTCSelectPayListBean implements Serializable {

    /**
     * ID : 1004
     * LocalCurrencyId : 2
     * LocalCurrencyCode : USD
     * LocalCurrencyLogo : null
     * PayTypeId : 6
     * PayTypeCode : Bankcard
     * PayTypeLogo : null
     * PayAttributes : {"AccountNo":"34555","BankAddress":"4456","BankBranch":"44555","BankName":"334455","SwiftCode":"3445","UserName":"12346"}
     */

    private int ID;
    private int LocalCurrencyId;
    private String LocalCurrencyCode;
    private Object LocalCurrencyLogo;
    private int PayTypeId;
    private String PayTypeCode;
    private String PayTypeLogo;
    private PayAttributesBean PayAttributes;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLocalCurrencyId() {
        return LocalCurrencyId;
    }

    public void setLocalCurrencyId(int LocalCurrencyId) {
        this.LocalCurrencyId = LocalCurrencyId;
    }

    public String getLocalCurrencyCode() {
        return LocalCurrencyCode;
    }

    public void setLocalCurrencyCode(String LocalCurrencyCode) {
        this.LocalCurrencyCode = LocalCurrencyCode;
    }

    public Object getLocalCurrencyLogo() {
        return LocalCurrencyLogo;
    }

    public void setLocalCurrencyLogo(Object LocalCurrencyLogo) {
        this.LocalCurrencyLogo = LocalCurrencyLogo;
    }

    public int getPayTypeId() {
        return PayTypeId;
    }

    public void setPayTypeId(int PayTypeId) {
        this.PayTypeId = PayTypeId;
    }

    public String getPayTypeCode() {
        return PayTypeCode;
    }

    public void setPayTypeCode(String PayTypeCode) {
        this.PayTypeCode = PayTypeCode;
    }

    public String getPayTypeLogo() {
        return PayTypeLogo;
    }

    public void setPayTypeLogo(String PayTypeLogo) {
        this.PayTypeLogo = PayTypeLogo;
    }

    public PayAttributesBean getPayAttributes() {
        return PayAttributes;
    }

    public void setPayAttributes(PayAttributesBean PayAttributes) {
        this.PayAttributes = PayAttributes;
    }

    public static class PayAttributesBean implements Serializable{
        /**
         * AccountNo : 34555
         * BankAddress : 4456
         * BankBranch : 44555
         * BankName : 334455
         * SwiftCode : 3445
         * UserName : 12346
         */

        private String AccountNo;
        private String BankAddress;
        private String BankBranch;
        private String BankName;
        private String SwiftCode;
        private String UserName;
        private String BankNoHide;

        public String getAccountNo() {
            return AccountNo;
        }

        public void setAccountNo(String AccountNo) {
            this.AccountNo = AccountNo;
        }

        public String getBankAddress() {
            return BankAddress;
        }

        public void setBankAddress(String BankAddress) {
            this.BankAddress = BankAddress;
        }

        public String getBankBranch() {
            return BankBranch;
        }

        public void setBankBranch(String BankBranch) {
            this.BankBranch = BankBranch;
        }

        public String getBankName() {
            return BankName;
        }

        public void setBankName(String BankName) {
            this.BankName = BankName;
        }

        public String getSwiftCode() {
            return SwiftCode;
        }

        public void setSwiftCode(String SwiftCode) {
            this.SwiftCode = SwiftCode;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getBankNoHide() {
            return BankNoHide;
        }

        public void setBankNoHide(String bankNoHide) {
            BankNoHide = bankNoHide;
        }
    }
}
