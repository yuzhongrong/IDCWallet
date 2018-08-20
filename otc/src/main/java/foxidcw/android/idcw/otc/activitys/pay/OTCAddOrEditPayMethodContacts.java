package foxidcw.android.idcw.otc.activitys.pay;

import foxidcw.android.idcw.otc.R;

public final class OTCAddOrEditPayMethodContacts {

    public static final int STATE_ALIPAY = 0;
    public static final int STATE_CNY = 1;
    public static final int STATE_USD = 2;
    public static final int STATE_VND = 3;
    public static final int STATE_WECHAT = 4;

    public static final int[] CNY_NAMES = {
            R.string.str_otc_pay_account_name,
            R.string.str_otc_pay_account_number,
            R.string.str_otc_pay_account_bank,
            R.string.str_otc_pay_account_bank_branch};

    public static final int[] CNY_HINTS = {
            R.string.str_otc_pay_input_account_card_name,
            R.string.str_otc_pay_input_account_card_number,
            R.string.str_otc_pay_input_account_bank_name,
            R.string.str_otc_pay_input_account_bank_branch};

    public static final int[] USD_NAMES = {
            R.string.str_otc_pay_account_name,
            R.string.str_otc_pay_account_number,
            R.string.str_otc_pay_account_bank,
            R.string.str_otc_pay_account_bank_address,
            R.string.str_otc_pay_account_bank_swift_code};

    public static final int[] USD_HINTS = {
            R.string.str_otc_pay_input_account_card_name,
            R.string.str_otc_pay_input_account_card_number,
            R.string.str_otc_pay_input_account_bank_name,
            R.string.str_otc_pay_input_account_bank_address,
            R.string.str_otc_pay_input_account_bank_swift_code};

    public static final int[] VND_NAMES = {
            R.string.str_otc_pay_account_name,
            R.string.str_otc_pay_account_number,
            R.string.str_otc_pay_account_bank,
            R.string.str_otc_pay_account_bank_city,
            R.string.str_otc_pay_account_bank_branch};

    public static final int[] VND_HINTS = {
            R.string.str_otc_pay_input_account_card_name,
            R.string.str_otc_pay_input_account_card_number,
            R.string.str_otc_pay_input_account_bank_name,
            R.string.str_otc_pay_input_account_bank_city,
            R.string.str_otc_pay_input_account_bank_branch};

    /**
     * 根据银行卡类型获取要显示的文字
     *
     * @param state
     * @return
     */
    public static int[][] getBankContactsWithState(int state) {
        int[][] results = new int[2][];
        switch (state) {
            case STATE_CNY:
                results[0] = CNY_NAMES;
                results[1] = CNY_HINTS;
                break;
            case STATE_VND:
                results[0] = VND_NAMES;
                results[1] = VND_HINTS;
                break;
            default:
                results[0] = USD_NAMES;
                results[1] = USD_HINTS;
                break;
        }
        return results;
    }

    /**
     * 检测是否是银行卡
     *
     * @param state
     * @return
     */
    public static boolean checkIsBankCard(int state) {
        return state != STATE_ALIPAY && state != STATE_WECHAT;
    }

    /**
     * 检测是否是支付宝
     *
     * @param state
     * @return
     */
    public static boolean checkIsAlipay(int state) {
        return state == STATE_ALIPAY;
    }

    /**
     * 检测是否是微信
     *
     * @param state
     * @return
     */
    public static boolean checkIsWeChat(int state) {
        return state == STATE_WECHAT;
    }
}
