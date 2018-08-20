package foxidcw.android.idcw.otc.utils;

import android.content.Context;

import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.otc.R;

public class OTCPayCodeWithLanguageCodeUtils {

    public static final String ALIPAY_CODE = "AliPay";
    public static final String BANKCARD_CODE = "Bankcard";
    public static final String WECHAT_CODE = "WeChat";
    private static final int ALIPAY_STR_ID = R.string.str_otc_pay_alipay;
    private static final String ZH_BANKCARD_STR = "银行借记卡";
    private static final String HK_BANKCARD_STR = "銀行借記卡";
    private static final String WECHAT_STR = "微信";

    private static final int LANGUAGE_ZH_CODE = 1;
    private static final int LANGUAGE_HK_CODE = 8;

    /**
     * 根据payTypeCode 以及当前设置的语言返回
     *
     * @param context
     * @param currentPayCode
     * @return
     */
    public static String getShowPayCodeWithLanguageCode(Context context, String currentPayCode) {
        switch (Integer.parseInt(AppLanguageUtils.getLanguageLocalCode(context))) {
            case LANGUAGE_ZH_CODE:
                if (currentPayCode.equals(ALIPAY_CODE)) {
                    return context.getResources().getString(ALIPAY_STR_ID);
                } else if (currentPayCode.equals(BANKCARD_CODE)) {
                    return ZH_BANKCARD_STR;
                } else if (currentPayCode.equals(WECHAT_CODE)) {
                    return WECHAT_STR;
                }
                break;
            case LANGUAGE_HK_CODE:
                if (currentPayCode.equals(ALIPAY_CODE)) {
                    return context.getResources().getString(ALIPAY_STR_ID);
                } else if (currentPayCode.equals(BANKCARD_CODE)) {
                    return HK_BANKCARD_STR;
                } else if (currentPayCode.equals(WECHAT_CODE)) {
                    return WECHAT_STR;
                }
                break;
        }
        return currentPayCode;
    }

    /**
     * 是否是支付宝
     *
     * @return
     */
    public static boolean checkIsAlipay(String payTypeCode) {
        return ALIPAY_CODE.equals(payTypeCode);
    }

    /**
     * 是否是微信
     *
     * @return
     */
    public static boolean checkIsWeChat(String payTypeCode) {
        return WECHAT_CODE.equals(payTypeCode);
    }

    /**
     * 是否是银行卡
     *
     * @return
     */
    public static boolean checkIsBankcard(String payTypeCode) {
        return BANKCARD_CODE.equals(payTypeCode);
    }
}
