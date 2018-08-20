package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpz on 2018/5/5.
 */

public class OTCGetExchangeBuyBean implements Serializable {
    private List<ExchangeCoinListBean> ExchangeCoinList;
    private List<ExchangePayListBean> ExchangePayList;

    public List<ExchangeCoinListBean> getExchangeCoinList() {
        return ExchangeCoinList;
    }

    public void setExchangeCoinList(List<ExchangeCoinListBean> exchangeCoinList) {
        ExchangeCoinList = exchangeCoinList;
    }

    public List<ExchangePayListBean> getExchangePayList() {
        return ExchangePayList;
    }

    public void setExchangePayList(List<ExchangePayListBean> exchangePayList) {
        ExchangePayList = exchangePayList;
    }

    public static class ExchangeCoinListBean implements Serializable {
        /**
         * id : 1011
         * CoinId : 4
         * CoinCode : vhkd
         * Direction : 1
         * Min : 12.0
         * Max : 11.0
         * Premium : 0.2
         */

        private int id;
        private int CoinId;
        private String CoinCode;
        private int Direction;
        private double Min;
        private double Max;
        private double Premium;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCoinId() {
            return CoinId;
        }

        public void setCoinId(int CoinId) {
            this.CoinId = CoinId;
        }

        public String getCoinCode() {
            return CoinCode;
        }

        public void setCoinCode(String CoinCode) {
            this.CoinCode = CoinCode;
        }

        public int getDirection() {
            return Direction;
        }

        public void setDirection(int Direction) {
            this.Direction = Direction;
        }

        public double getMin() {
            return Min;
        }

        public void setMin(double Min) {
            this.Min = Min;
        }

        public double getMax() {
            return Max;
        }

        public void setMax(double Max) {
            this.Max = Max;
        }

        public double getPremium() {
            return Premium;
        }

        public void setPremium(double Premium) {
            this.Premium = Premium;
        }

    }

    public static class ExchangePayListBean implements Serializable {
        private int id;
        private int LocalCurrencyId ;
        private int PayModeId;
        private String LocalCurrencyCode;
        private String Logo ;
        private int PayTypeId ;
        private String PayTypeCode;
        private OTCSelectPayListBean.PayAttributesBean PayAttributes;

        public OTCSelectPayListBean.PayAttributesBean getPayAttributes() {
            return PayAttributes;
        }

        public void setPayAttributes(OTCSelectPayListBean.PayAttributesBean PayAttributes) {
            this.PayAttributes = PayAttributes;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPayModeId() {
            return PayModeId;
        }

        public void setPayModeId(int payModeId) {
            PayModeId = payModeId;
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

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String logo) {
            Logo = logo;
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
        }
    }
}
