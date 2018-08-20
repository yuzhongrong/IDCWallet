package foxidcw.android.idcw.otc.activitys.pay.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import foxidcw.android.idcw.otc.utils.OTCPayCodeWithLanguageCodeUtils;

public class OTCPayMethodManagerAdapter extends BaseQuickAdapter<OTCAddOrEditReqParams, BaseViewHolder> {

    public OTCPayMethodManagerAdapter(@Nullable List<OTCAddOrEditReqParams> data) {
        super(R.layout.activity_otc_pay_method_manager_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OTCAddOrEditReqParams item) {
        int itemBg;
        int icon;
        int rightIcon;
        OTCAddOrEditReqParams.PayAttributes payAttributes = item.getPayAttributes();

        String payTypeCode = item.getPayTypeCode();
        boolean isAlipay = OTCPayCodeWithLanguageCodeUtils.checkIsAlipay(payTypeCode);
        boolean isWechat = OTCPayCodeWithLanguageCodeUtils.checkIsWeChat(payTypeCode);
        if (isAlipay) { // 支付宝
            itemBg = R.drawable.otc_gradient_blue;
            icon = R.drawable.otc_alipay_circle_icon;
            rightIcon = R.drawable.otc_alipay_transport_icon;
        } else if (isWechat) { // 微信
            itemBg = R.drawable.otc_gradient_green;
            icon = R.drawable.otc_wechat_circle_icon;
            rightIcon = R.drawable.otc_wechat_transport_icon;
        } else { // 银行卡
            itemBg = R.drawable.otc_gradient_gray;
            rightIcon = R.drawable.otc_bank_transport_icon;
            icon = R.drawable.otc_bank_circle_icon;
        }
        /**
         * 如果是微信或者支付宝 那么显示对应的中文文字 英文文字  繁体文字
         */
        if (isAlipay || isWechat) {
            helper.setText(R.id.pay_method_name_tv,
                    OTCPayCodeWithLanguageCodeUtils.getShowPayCodeWithLanguageCode(helper.itemView.getContext(), payTypeCode));
            helper.setText(R.id.pay_method_number_tv, payAttributes.getAccountNo());
        } else {
            helper.setText(R.id.pay_method_name_tv, payAttributes.getBankName());
            helper.setText(R.id.pay_method_number_tv, payAttributes.getBankNoHide());
        }

        helper.setBackgroundRes(R.id.pay_method_item_cr, itemBg);
        helper.setImageResource(R.id.pay_method_logo_iv, icon);
        helper.setImageResource(R.id.pay_method_icon_iv, rightIcon);

        helper.setText(R.id.pay_method_currency_tv, item.getLocalCurrencyCode());
    }
}
