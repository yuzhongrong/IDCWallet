package foxidcw.android.idcw.otc.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.GlideUtil;

import java.util.ArrayList;
import java.util.HashSet;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;

public class OTCSelectPaymentMethodAdapter extends BaseQuickAdapter<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse, BaseViewHolder> {

    public HashSet<Integer> mMultiSelects = new HashSet<>();

    public OTCSelectPaymentMethodAdapter() {
        super(R.layout.popup_otc_selete_payment_item, new ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse>());
    }

    @Override
    protected void convert(BaseViewHolder helper, OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse item) {
        int selectBgId;
        int itemBg;
        if (mMultiSelects.contains(item.getID())) {
            selectBgId = R.drawable.icon_otc_tick_blue;
            itemBg = R.drawable.popup_otc_select_payment_item_corner_select_bg;
        } else {
            selectBgId = R.drawable.icon_otc_tick_gray;
            itemBg = R.drawable.popup_otc_select_payment_item_corner_bg;
        }
        helper.setBackgroundRes(R.id.popup_select_payment_item_container,itemBg);
        helper.setImageResource(R.id.popup_select_payment_item_current_iv, selectBgId);

        helper.setGone(R.id.popup_select_payment_item_number_tv, !TextUtils.isEmpty(item.getAccountNo()));
        helper.setText(R.id.popup_select_payment_item_number_tv, item.getAccountNo());

        helper.setGone(R.id.popup_select_payment_item_currency_tv, !TextUtils.isEmpty(item.getCurrencyCode()));
        helper.setText(R.id.popup_select_payment_item_currency_tv, item.getCurrencyCode());

        helper.setText(R.id.popup_select_payment_item_name_tv, item.getShowPayTypeCode(helper.itemView.getContext()));
        GlideUtil.loadImageView(mContext, item.getPayTypeLogo(), helper.getView(R.id.popup_select_payment_item_image_iv));
    }

    public HashSet<Integer> getSelects() {
        return mMultiSelects;
    }

    public void setmMultiSelects(HashSet<Integer> multiSelects) {
        mMultiSelects.clear();
        mMultiSelects.addAll(multiSelects);
    }

    public void addSelect(int ID) {
        if (mMultiSelects.contains(ID)) {
            mMultiSelects.remove(ID);
        } else {
            mMultiSelects.add(ID);
        }
    }
}
