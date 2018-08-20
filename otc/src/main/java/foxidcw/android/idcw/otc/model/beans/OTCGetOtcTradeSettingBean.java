package foxidcw.android.idcw.otc.model.beans;

import java.util.List;

/**
 * @author :       MayerXu10000@gamil.com
 * @project name : FoxIDCW
 * @class name :   OTCGetOtcTradeSettingBean
 * @package name : foxidcw.android.idcw.otc.model.beans
 * @date :         2018/5/5 14:25
 * @describe :     获取OTC交易相关设置BEAN
 */
public class OTCGetOtcTradeSettingBean {

    /**
     * CoinSettings : [{"CoinCode":"vhkd","CoinId":4,"CoinName":"VHKD","CreateTime":"0001-01-01 00:00:00","CreateUserId":0,"Logo":"/upload/coin/ico_vhkd.png","MaxBuyQuantity":50000,"MaxSellQuantity":50000,"MinBuyQuantity":1,"MinConfirms":0,"MinSellQuantity":1,"Precision":0.001,"Sort":1000,"UpdateTime":"0001-01-01 00:00:00","UpdateUserId":0,"id":3},{"CoinCode":"btc","CoinId":1,"CoinName":"Bitcoin","CreateTime":"0001-01-01 00:00:00","CreateUserId":0,"Logo":"/upload/coin/ico_btc.png","MaxBuyQuantity":50,"MaxSellQuantity":20,"MinBuyQuantity":0.1,"MinConfirms":0,"MinSellQuantity":0.5,"Precision":0.01,"Sort":1001,"UpdateTime":"0001-01-01 00:00:00","UpdateUserId":0,"id":2}]
     * Currencies : [{"Id":1,"IsSupportOtc":true,"Name":"CNY","Precision":0.02,"Sort":0,"Symbol":"￥"},{"Id":2,"IsSupportOtc":true,"Name":"USD","Precision":0.02,"Sort":0,"Symbol":"$"},{"Id":6,"IsSupportOtc":true,"Name":"VND","Precision":0.02,"Sort":0,"Symbol":"\u20ab"}]
     * IsForbidTrade : false
     */

    private boolean IsForbidTrade;
    private List<CoinSettingsBean> CoinSettings;
    private List<CurrenciesBean> Currencies;
    private List<OtcPaymentModeDTO> Payments;
    private long ForbidExpireTimestamp;
    private String ForbidExpireDate;
    private long ForbidExpiredTimestamp; // 解禁到期时间戳
    private long ForbidExpiredSeconds; // 解禁到期时间剩余秒数
    private int CancelCount; // 取消次数

    public long getForbidExpiredTimestamp() {
        return ForbidExpiredTimestamp;
    }

    public void setForbidExpiredTimestamp(long forbidExpiredTimestamp) {
        ForbidExpiredTimestamp = forbidExpiredTimestamp;
    }

    public long getForbidExpiredSeconds() {
        return ForbidExpiredSeconds;
    }

    public void setForbidExpiredSeconds(long forbidExpiredSeconds) {
        ForbidExpiredSeconds = forbidExpiredSeconds;
    }

    public int getCancelCount() {
        return CancelCount;
    }

    public void setCancelCount(int cancelCount) {
        CancelCount = cancelCount;
    }

    public boolean isIsForbidTrade() {
        return IsForbidTrade;
    }

    public void setIsForbidTrade(boolean IsForbidTrade) {
        this.IsForbidTrade = IsForbidTrade;
    }

    public List<CoinSettingsBean> getCoinSettings() {
        return CoinSettings;
    }

    public void setCoinSettings(List<CoinSettingsBean> CoinSettings) {
        this.CoinSettings = CoinSettings;
    }

    public List<CurrenciesBean> getCurrencies() {
        return Currencies;
    }

    public void setCurrencies(List<CurrenciesBean> Currencies) {
        this.Currencies = Currencies;
    }

    public boolean isForbidTrade() {
        return IsForbidTrade;
    }

    public void setForbidTrade(boolean forbidTrade) {
        IsForbidTrade = forbidTrade;
    }

    public List<OtcPaymentModeDTO> getPayments() {
        return Payments;
    }

    public void setPayments(List<OtcPaymentModeDTO> payments) {
        Payments = payments;
    }

    public static class OtcPaymentModeDTO {

        /**
         * ID : 1009
         * LocalCurrencyCode : CNY
         * LocalCurrencyId : 1
         * LocalCurrencyLogo : http://192.168.1.35:8203//images/usd.png
         * PayAttributes : {"AccountNo":"手机生死狙击苏","UserName":"起说你呢"}
         * PayTypeCode : http://192.168.1.35:8203/AliPay
         * PayTypeId : 2
         * PayTypeLogo : http://192.168.1.226/group1/M00/00/18/wKgB4lqsjhiAQDDlAAInn_BrY3k313.jpg
         */

        private int ID;
        private String LocalCurrencyCode;
        private int LocalCurrencyId;
        private String LocalCurrencyLogo;
        private PayAttributesBean PayAttributes;
        private String PayTypeCode;
        private int PayTypeId;
        private String PayTypeLogo;
        private String BankNoHide;
        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getLocalCurrencyCode() {
            return LocalCurrencyCode;
        }

        public void setLocalCurrencyCode(String LocalCurrencyCode) {
            this.LocalCurrencyCode = LocalCurrencyCode;
        }

        public int getLocalCurrencyId() {
            return LocalCurrencyId;
        }

        public void setLocalCurrencyId(int LocalCurrencyId) {
            this.LocalCurrencyId = LocalCurrencyId;
        }

        public String getLocalCurrencyLogo() {
            return LocalCurrencyLogo;
        }

        public void setLocalCurrencyLogo(String LocalCurrencyLogo) {
            this.LocalCurrencyLogo = LocalCurrencyLogo;
        }

        public PayAttributesBean getPayAttributes() {
            return PayAttributes;
        }

        public void setPayAttributes(PayAttributesBean PayAttributes) {
            this.PayAttributes = PayAttributes;
        }

        public String getPayTypeCode() {
            return PayTypeCode;
        }

        public void setPayTypeCode(String PayTypeCode) {
            this.PayTypeCode = PayTypeCode;
        }

        public int getPayTypeId() {
            return PayTypeId;
        }

        public void setPayTypeId(int PayTypeId) {
            this.PayTypeId = PayTypeId;
        }

        public String getPayTypeLogo() {
            return PayTypeLogo;
        }

        public void setPayTypeLogo(String PayTypeLogo) {
            this.PayTypeLogo = PayTypeLogo;
        }

        public static class PayAttributesBean {
            /**
             * AccountNo : 手机生死狙击苏
             * UserName : 起说你呢
             */

            private String AccountNo;
            private String UserName;
            private String BankAddress;
            private String BankBranch;
            private String BankName;
            private String SwiftCode;
            private String BankNoHide;
            public String getAccountNo() {
                return AccountNo;
            }

            public void setAccountNo(String AccountNo) {
                this.AccountNo = AccountNo;
            }

            public String getUserName() {
                return UserName;
            }

            public void setUserName(String UserName) {
                this.UserName = UserName;
            }

            public String getBankAddress() {
                return BankAddress;
            }

            public void setBankAddress(String bankAddress) {
                BankAddress = bankAddress;
            }

            public String getBankBranch() {
                return BankBranch;
            }

            public void setBankBranch(String bankBranch) {
                BankBranch = bankBranch;
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

            public String getBankNoHide() {
                return BankNoHide;
            }

            public void setBankNoHide(String bankNoHide) {
                BankNoHide = bankNoHide;
            }
        }
    }

    public static class CoinSettingsBean {
        /**
         * CoinCode : vhkd
         * CoinId : 4
         * CoinName : VHKD
         * CreateTime : 0001-01-01 00:00:00
         * CreateUserId : 0
         * Logo : /upload/coin/ico_vhkd.png
         * MaxBuyQuantity : 50000
         * MaxSellQuantity : 50000
         * MinBuyQuantity : 1
         * MinConfirms : 0
         * MinSellQuantity : 1
         * Precision : 0.001
         * Sort : 1000
         * UpdateTime : 0001-01-01 00:00:00
         * UpdateUserId : 0
         * id : 3
         */

        private String CoinCode;
        private int CoinId;
        private String CoinName;
        private String CreateTime;
        private int CreateUserId;
        private String Logo;
        private double MaxBuyQuantity;
        private double MaxSellQuantity;
        private double MinBuyQuantity;
        private int MinConfirms;
        private double MinSellQuantity;
        private double Precision;
        private int Sort;
        private String UpdateTime;
        private int UpdateUserId;
        private int id;
        private int Digit;

        public String getCoinCode() {
            return CoinCode;
        }

        public void setCoinCode(String CoinCode) {
            this.CoinCode = CoinCode;
        }

        public int getCoinId() {
            return CoinId;
        }

        public void setCoinId(int CoinId) {
            this.CoinId = CoinId;
        }

        public String getCoinName() {
            return CoinName;
        }

        public void setCoinName(String CoinName) {
            this.CoinName = CoinName;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public int getCreateUserId() {
            return CreateUserId;
        }

        public void setCreateUserId(int CreateUserId) {
            this.CreateUserId = CreateUserId;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String Logo) {
            this.Logo = Logo;
        }

        public double getMaxBuyQuantity() {
            return MaxBuyQuantity;
        }

        public void setMaxBuyQuantity(int MaxBuyQuantity) {
            this.MaxBuyQuantity = MaxBuyQuantity;
        }

        public double getMaxSellQuantity() {
            return MaxSellQuantity;
        }

        public void setMaxSellQuantity(int MaxSellQuantity) {
            this.MaxSellQuantity = MaxSellQuantity;
        }

        public double getMinBuyQuantity() {
            return MinBuyQuantity;
        }

        public void setMinBuyQuantity(int MinBuyQuantity) {
            this.MinBuyQuantity = MinBuyQuantity;
        }

        public int getMinConfirms() {
            return MinConfirms;
        }

        public void setMinConfirms(int MinConfirms) {
            this.MinConfirms = MinConfirms;
        }

        public double getMinSellQuantity() {
            return MinSellQuantity;
        }

        public void setMinSellQuantity(int MinSellQuantity) {
            this.MinSellQuantity = MinSellQuantity;
        }

        public double getPrecision() {
            return Precision;
        }

        public void setPrecision(double Precision) {
            this.Precision = Precision;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }

        public int getUpdateUserId() {
            return UpdateUserId;
        }

        public void setUpdateUserId(int UpdateUserId) {
            this.UpdateUserId = UpdateUserId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setMaxBuyQuantity(double maxBuyQuantity) {
            MaxBuyQuantity = maxBuyQuantity;
        }

        public void setMaxSellQuantity(double maxSellQuantity) {
            MaxSellQuantity = maxSellQuantity;
        }

        public void setMinBuyQuantity(double minBuyQuantity) {
            MinBuyQuantity = minBuyQuantity;
        }

        public void setMinSellQuantity(double minSellQuantity) {
            MinSellQuantity = minSellQuantity;
        }

        public int getDigit() {
            return Digit;
        }

        public void setDigit(int digit) {
            Digit = digit;
        }

        @Override
        public String toString() {
            return "CoinSettingsBean{" + "CoinCode='" + CoinCode + '\'' + ", CoinId=" + CoinId + ", CoinName='" + CoinName + '\'' + ", CreateTime='" + CreateTime + '\'' + ", CreateUserId=" + CreateUserId + ", Logo='" + Logo + '\'' + ", MaxBuyQuantity=" + MaxBuyQuantity + ", MaxSellQuantity=" + MaxSellQuantity + ", MinBuyQuantity=" + MinBuyQuantity + ", MinConfirms=" + MinConfirms + ", MinSellQuantity=" + MinSellQuantity + ", Precision=" + Precision + ", Sort=" + Sort + ", UpdateTime='" + UpdateTime + '\'' + ", UpdateUserId=" + UpdateUserId + ", id=" + id + ", Digit=" + Digit + '}';
        }
    }

    public static class CurrenciesBean {

        /**
         * Id : 6
         * Name : VND
         * Symbol : ₫
         * Sort : 0
         * Precision : 0.01
         * IsSupportOtc : true
         * Logo : http://192.168.1.36:8888//group1/M00/00/00/wKgBJFrv-f-Ad3mwAAAGG6mMr9E071.png
         * Digit : 2
         */

        private int Id;
        private String Name;
        private String Symbol;
        private int Sort;
        private double Precision;
        private boolean IsSupportOtc;
        private String Logo;
        private int Digit;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getSymbol() {
            return Symbol;
        }

        public void setSymbol(String Symbol) {
            this.Symbol = Symbol;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }

        public double getPrecision() {
            return Precision;
        }

        public void setPrecision(double Precision) {
            this.Precision = Precision;
        }

        public boolean isIsSupportOtc() {
            return IsSupportOtc;
        }

        public void setIsSupportOtc(boolean IsSupportOtc) {
            this.IsSupportOtc = IsSupportOtc;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String Logo) {
            this.Logo = Logo;
        }

        public int getDigit() {
            return Digit;
        }

        public void setDigit(int Digit) {
            this.Digit = Digit;
        }
    }

    public long getForbidExpireTimestamp() {
        return ForbidExpireTimestamp;
    }

    public void setForbidExpireTimestamp(long forbidExpireTimestamp) {
        ForbidExpireTimestamp = forbidExpireTimestamp;
    }

    public String getForbidExpireDate() {
        return ForbidExpireDate;
    }

    public void setForbidExpireDate(String forbidExpireDate) {
        ForbidExpireDate = forbidExpireDate;
    }

    @Override
    public String toString() {
        return "OTCGetOtcTradeSettingBean{" + "IsForbidTrade=" + IsForbidTrade + ", CoinSettings=" + CoinSettings + ", Currencies=" + Currencies + ", Payments=" + Payments + ", ForbidExpireTimestamp=" + ForbidExpireTimestamp + ", ForbidExpireDate='" + ForbidExpireDate + '\'' + '}';
    }
}
