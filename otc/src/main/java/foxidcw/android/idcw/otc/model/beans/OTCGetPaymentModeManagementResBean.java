package foxidcw.android.idcw.otc.model.beans;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import foxidcw.android.idcw.otc.utils.OTCPayCodeWithLanguageCodeUtils;

public class OTCGetPaymentModeManagementResBean implements Serializable {

    private List<LocalCurrencyLinkageResponse> LocalCurrencyList; // 货币币种
    private List<PaytypeLinkageResponse> PaytypeList; // 支付方式
    private List<OtcCurrencyPaytypeResponse> CurrencyPaytypeList; // 扩展属性

    public List<LocalCurrencyLinkageResponse> getLocalCurrencyList() {
        return LocalCurrencyList;
    }

    public void setLocalCurrencyList(List<LocalCurrencyLinkageResponse> localCurrencyList) {
        LocalCurrencyList = localCurrencyList;
    }


    public List<OtcCurrencyPaytypeResponse> getCurrencyPaytypeList() {
        return CurrencyPaytypeList;
    }

    public void setCurrencyPaytypeList(List<OtcCurrencyPaytypeResponse> currencyPaytypeList) {
        CurrencyPaytypeList = currencyPaytypeList;
    }

    public List<PaytypeLinkageResponse> getPaytypeList() {
        return PaytypeList;
    }

    public void setPaytypeList(List<PaytypeLinkageResponse> paytypeList) {
        PaytypeList = paytypeList;
    }

    @Override
    public String toString() {
        return "OTCGetPaymentModeManagementResBean{" +
                "LocalCurrencyList=" + LocalCurrencyList +
                ", PaytypeList=" + PaytypeList +
                ", CurrencyPaytypeList=" + CurrencyPaytypeList +
                '}';
    }

    public static class LocalCurrencyLinkageResponse {
        private int ID;
        private String LocalCurrencyCode;
        private String LocalCurrencyLogo;
        private List<PaytypeLinkageResponse> PaytypeList;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getLocalCurrencyLogo() {
            return LocalCurrencyLogo;
        }

        public void setLocalCurrencyLogo(String localCurrencyLogo) {
            LocalCurrencyLogo = localCurrencyLogo;
        }

        public String getLocalCurrencyCode() {
            return LocalCurrencyCode;
        }

        public void setLocalCurrencyCode(String localCurrencyCode) {
            LocalCurrencyCode = localCurrencyCode;
        }

        public List<PaytypeLinkageResponse> getPaytypeList() {
            return PaytypeList;
        }

        public void setPaytypeList(List<PaytypeLinkageResponse> paytypeList) {
            PaytypeList = paytypeList;
        }

        @Override
        public String toString() {
            return "LocalCurrencyLinkageResponse{" +
                    "id=" + ID +
                    ", LocalCurrencyCode='" + LocalCurrencyCode + '\'' +
                    ", PaytypeList=" + PaytypeList +
                    '}';
        }
    }

    public static class PaytypeLinkageResponse {
        private int ID; // 支付方式ID
        private String PayTypeCode; // 支付code
        private String PayTypeLogo; // logo
        private String accountNo; // 账号
        private String currencyCode; // 法币code
        private List<LocalCurrencyLinkageResponse> LocalCurrencyList;

        public String getPayTypeLogo() {
            return PayTypeLogo;
        }

        public void setPayTypeLogo(String payTypeLogo) {
            PayTypeLogo = payTypeLogo;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getPayTypeCode() {
            return PayTypeCode;
        }

        public void setPayTypeCode(String payTypeCode) {
            PayTypeCode = payTypeCode;
        }

        public List<LocalCurrencyLinkageResponse> getLocalCurrencyList() {
            return LocalCurrencyList;
        }

        public void setLocalCurrencyList(List<LocalCurrencyLinkageResponse> localCurrencyList) {
            LocalCurrencyList = localCurrencyList;
        }

        /**
         * 根据支付类型获取显示的文字
         * @param context
         * @return
         */
        public String getShowPayTypeCode(Context context) {
            return OTCPayCodeWithLanguageCodeUtils.getShowPayCodeWithLanguageCode(context,getPayTypeCode());
        }

        /**
         * 是否是支付宝
         * @return
         */
        public boolean checkIsAlipay() {
            return OTCPayCodeWithLanguageCodeUtils.checkIsAlipay(getPayTypeCode());
        }

        /**
         * 是否是微信
         * @return
         */
        public boolean checkIsWeChat() {
            return OTCPayCodeWithLanguageCodeUtils.checkIsWeChat(getPayTypeCode());
        }

        /**
         * 是否是银行卡
         * @return
         */
        public boolean checkIsBankcard() {
            return OTCPayCodeWithLanguageCodeUtils.checkIsBankcard(getPayTypeCode());
        }

        @Override
        public String toString() {
            return "PaytypeLinkageResponse{" +
                    "id=" + ID +
                    ", PayTypeCode='" + PayTypeCode + '\'' +
                    ", LocalCurrencyList=" + LocalCurrencyList +
                    '}';
        }
    }

    public static class OtcCurrencyPaytypeResponse {
        private int ID;
        private int LocalCurrencyId;
        private int PayTypeId;
        private String Content;
        private List<PayAttributeResponse> Attributes; // 属性值

        public int getID() {
            return ID;
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

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public List<PayAttributeResponse> getAttributes() {
            return Attributes;
        }

        public void setAttributes(List<PayAttributeResponse> attributes) {
            Attributes = attributes;
        }

        @Override
        public String toString() {
            return "OtcCurrencyPaytypeResponse{" +
                    "Id=" + ID +
                    ", LocalCurrencyId=" + LocalCurrencyId +
                    ", PayTypeId=" + PayTypeId +
                    ", Content='" + Content + '\'' +
                    ", Attributes=" + Attributes +
                    '}';
        }
    }

    public static class PayAttributeResponse {
        private int Sort; // 排序
        private boolean IsShow; // 是否为显示字段
        private String string; // 属性ID
        private String LabelLanguageCode; // 语言编码
        private int ControlType; // 控件类型 = ['1', '2', '3', '4', '5', '6', '7', '8', '9'],
        private String Field; // 字段
        private String DefaultValue; // 默认值
        private boolean IsRequired;//是否必填
        private String RegularExpression; // 正则表达式
        private int MaxLength; // 最大长度
        private int CreateUserId;// 创建人
        private String CreateTime; // 创建时间
        private int UpdateUserId; // 修改人
        private String UpdateTime; // 修改时间
        private Object ValueRanges; // 取值范围

        public int getSort() {
            return Sort;
        }

        public void setSort(int sort) {
            Sort = sort;
        }

        public boolean isShow() {
            return IsShow;
        }

        public void setShow(boolean show) {
            IsShow = show;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public String getLabelLanguageCode() {
            return LabelLanguageCode;
        }

        public void setLabelLanguageCode(String labelLanguageCode) {
            LabelLanguageCode = labelLanguageCode;
        }

        public int getControlType() {
            return ControlType;
        }

        public void setControlType(int controlType) {
            ControlType = controlType;
        }

        public String getField() {
            return Field;
        }

        public void setField(String field) {
            Field = field;
        }

        public String getDefaultValue() {
            return DefaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            DefaultValue = defaultValue;
        }

        public boolean isRequired() {
            return IsRequired;
        }

        public void setRequired(boolean required) {
            IsRequired = required;
        }

        public String getRegularExpression() {
            return RegularExpression;
        }

        public void setRegularExpression(String regularExpression) {
            RegularExpression = regularExpression;
        }

        public int getMaxLength() {
            return MaxLength;
        }

        public void setMaxLength(int maxLength) {
            MaxLength = maxLength;
        }

        public int getCreateUserId() {
            return CreateUserId;
        }

        public void setCreateUserId(int createUserId) {
            CreateUserId = createUserId;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public int getUpdateUserId() {
            return UpdateUserId;
        }

        public void setUpdateUserId(int updateUserId) {
            UpdateUserId = updateUserId;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String updateTime) {
            UpdateTime = updateTime;
        }

        public Object getValueRanges() {
            return ValueRanges;
        }

        public void setValueRanges(Object valueRanges) {
            ValueRanges = valueRanges;
        }

        @Override
        public String toString() {
            return "PayAttributeResponse{" +
                    "Sort=" + Sort +
                    ", IsShow=" + IsShow +
                    ", string='" + string + '\'' +
                    ", LabelLanguageCode='" + LabelLanguageCode + '\'' +
                    ", ControlType=" + ControlType +
                    ", Field='" + Field + '\'' +
                    ", DefaultValue='" + DefaultValue + '\'' +
                    ", IsRequired=" + IsRequired +
                    ", RegularExpression='" + RegularExpression + '\'' +
                    ", MaxLength=" + MaxLength +
                    ", CreateUserId=" + CreateUserId +
                    ", CreateTime='" + CreateTime + '\'' +
                    ", UpdateUserId=" + UpdateUserId +
                    ", UpdateTime='" + UpdateTime + '\'' +
                    ", ValueRanges=" + ValueRanges +
                    '}';
        }
    }

}
