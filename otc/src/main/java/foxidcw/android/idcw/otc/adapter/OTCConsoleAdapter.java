package foxidcw.android.idcw.otc.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.Utils;

import java.util.List;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.model.beans.OTCOrderQuotePriceBean;

public class OTCConsoleAdapter extends BaseQuickAdapter<OTCOrderQuotePriceBean, BaseViewHolder> {

    private boolean mIsBuy;
    private String mCurrency;
    private int mPayTypeSize;

    public OTCConsoleAdapter(@Nullable List<OTCOrderQuotePriceBean> data) {
        super(R.layout.activity_otc_console_layout_item, data);
        mPayTypeSize = ConvertUtils.dp2px(20);
    }

    @Override
    protected void convert(BaseViewHolder helper, OTCOrderQuotePriceBean item) {
        helper.setText(R.id.otc_console_item_name_tv, item.getAcceptantName());
        helper.setText(R.id.otc_console_item_price_tv, Utils.toSubStringDegist(item.getAmount(), 2) + " " + mCurrency);
        helper.setText(R.id.otc_console_item_appeal_tv,
                mContext.getResources().getString(R.string.str_otc_appeal_count) + item.getAppealCount());
        helper.setText(R.id.otc_console_item_pay_tv,
                mContext.getResources().getString(mIsBuy ? R.string.str_otc_sub_pay_time : R.string.str_otc_sub_receiver_time) + getTimeWithSeconds(mIsBuy ? item.getAvgPayTime() : item.getAvgResponseTime()));
        List<OTCOrderQuotePriceBean.PayTypes> payTypesList = item.getPayTypes();
        LinearLayout container = helper.getView(R.id.otc_console_item_paytype_container);
        container.removeAllViews();
        container.setVisibility(payTypesList.size() > 0 ? View.VISIBLE : View.GONE);
        for (OTCOrderQuotePriceBean.PayTypes payItem : payTypesList) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mPayTypeSize, mPayTypeSize);
            ImageView imageView = new ImageView(helper.itemView.getContext());
            imageView.setLayoutParams(layoutParams);
            GlideUtil.loadImageView(mContext, payItem.getPayLogo(), imageView);
            container.addView(imageView);
        }
//        // 支付方式校验
//        Object[] states = getPayState(item.getPayTypes());
//        int state = (int) states[0];
//        boolean alipayVisible = state == 1 || state == 3;
//        boolean bankVisible = state == 2 || state == 3;
//        helper.setGone(R.id.otc_console_item_alipay_iv, alipayVisible);
//        helper.setGone(R.id.otc_console_item_bank_iv, bankVisible);
//        // 支付宝
//        if (alipayVisible && states[1] != null) {
//            OTCOrderQuotePriceBean.PayTypes alipay = (OTCOrderQuotePriceBean.PayTypes) states[1];
//            //设置支付宝
//            GlideUtil.loadImageView(mContext, alipay.getPayLogo(), helper.getView(R.id.otc_console_item_alipay_iv));
//        }
//        // 银行卡
//        if (bankVisible && states[2] != null) {
//            OTCOrderQuotePriceBean.PayTypes bank = (OTCOrderQuotePriceBean.PayTypes) states[2];
//            //设置银行卡
//            GlideUtil.loadImageView(mContext, bank.getPayLogo(), helper.getView(R.id.otc_console_item_bank_iv));
//        }
    }

    /**
     * 如果只有支付宝 1
     * 如果只有银行卡 2
     * 如果两者都有 3
     *
     * @param payTypes
     * @return
     */
    private Object[] getPayState(List<OTCOrderQuotePriceBean.PayTypes> payTypes) {
        Object[] objects = new Object[3];
        OTCOrderQuotePriceBean.PayTypes alipay = null;
        OTCOrderQuotePriceBean.PayTypes bank = null;
        int hasAlipay = -1;
        int hasBank = -1;
        if (payTypes != null) {
            for (OTCOrderQuotePriceBean.PayTypes item : payTypes) {
                String code = item.getPayCode();
                if (code.equals("AliPay")) {
                    hasAlipay = 1;
                    alipay = item;
                } else if (code.equals("Bankcard")) {
                    hasBank = 2;
                    bank = item;
                }
            }
        }
        objects[0] = hasAlipay == -1 && hasBank == -1 ? -1 : hasAlipay != -1 && hasBank != -1 ? 3 : hasAlipay != -1 ? hasAlipay : hasBank;
        objects[1] = alipay;
        objects[2] = bank;
        return objects;
    }

    /**
     * 时间格式化
     *
     * @param time
     * @return
     */
    private String getTimeWithSeconds(long time) {
        time = time * 1000;
        int minute = (int) ((time % (1000 * 60 * 60)) / (1000 * 60));
        int second = (int) ((time % (1000 * 60)) / 1000);
        return String.valueOf((minute < 10 ? String.valueOf("0" + minute) : minute) + ":" + (second < 10 ? String.valueOf("0" + second) : second));
    }

    public void setmIsBuy(boolean mIsBuy) {
        this.mIsBuy = mIsBuy;
    }

    public void setmCurrency(String mCurrency) {
        this.mCurrency = mCurrency;
    }
}
