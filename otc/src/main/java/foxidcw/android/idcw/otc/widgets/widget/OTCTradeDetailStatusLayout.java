package foxidcw.android.idcw.otc.widgets.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjwsc.idcm.Utils.SpUtils;
import com.google.gson.Gson;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.model.OTCOrderStatus;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcSettingBean;


/**
 * 设计理念：垃圾自回收模式
 * Created by yuzhongrong on 2018/5/4.
 * 此页面是交易详情顶部状态页面，状态由自身管理，避免activity代码逻辑量过大
 */

public class OTCTradeDetailStatusLayout extends LinearLayout {

    private TextView mTvStatus;
    private OTCGetOtcOrdersBean mBean;

    public OTCTradeDetailStatusLayout(Context context) {
        super(context);
        EventBus.getDefault().register(this);
        initView();
    }

    public OTCTradeDetailStatusLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        View root = LayoutInflater.from(getContext()).inflate(R.layout.activity_tradedetail_status_top, this, true);

        mTvStatus = (TextView) root.findViewById(R.id.status);
    }

//
//    //通过订阅
//@Subscriber(tag = EventTags.TAGS_STATUS_CHANGE)
//    public void setProperty(int state) {
//
//        switch (state) {//不同交易状态 设置不同属性
//
//            case 1:
//                mTvStatus.setText("111");
//                // mTvStatus.setTextColor();
//                break;
//
//            case 2:
//                mTvStatus.setText("222");
//                break;
//
//            case 3:
//                mTvStatus.setText("333");
//                break;
//
//            case 4:
//
//                mTvStatus.setText("444");
//
//                break;
//
//            case 5:
//                break;
//
//        }
//
//}

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
//        OTCGetOtcOrdersBean otcGetOtcOrdersBean = new OTCGetOtcOrdersBean();
//        otcGetOtcOrdersBean.setStatus(0);
//        otcGetOtcOrdersBean.setDirection(2);
//        onOrderChanged(otcGetOtcOrdersBean);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscriber(tag = EventTags.TAGS_STATUS_CHANGE)
    public void onOrderChanged(OTCGetOtcOrdersBean bean) {
//        boolean isMine = bean.getCreateUserId() == myId;
//        boolean isBuy = isMine == (bean.getDirection() ==1);
        mBean = bean;
        updateStatus(bean.getStatus(), bean.getDirection() == 1);

    }

    private void updateStatus(int status, boolean isBuy) {
        String text = "";
        boolean isTimeOut = mBean.isIsTimeOut();
        String oppositeTimeout = getContext().getString(R.string.otc_status_opposite_timeout_buyer);
        String mineTimeout = getContext().getString(R.string.otc_status_opposite_timeout_seller);

        String tradeSettingInfo = SpUtils.getStringData("otc_trade_setting_bean", "");
        OTCGetOtcSettingBean settingBean = new OTCGetOtcSettingBean();
        if (!TextUtils.isEmpty(tradeSettingInfo)) {
            settingBean = new Gson().fromJson(tradeSettingInfo, OTCGetOtcSettingBean.class);
        }

        switch (status) {
            case OTCOrderStatus.WaitPay:
                text = getDirectionText(isBuy, getContext().getString(R.string.otc_status_wait_pay_buyer, String.valueOf(settingBean.getConfirmTransferDuration())),
                        getContext().getString(R.string.otc_status_wait_pay_seller));
                break;
            case OTCOrderStatus.Transfered:
            case OTCOrderStatus.Paied:
                text = getDirectionText(isBuy, isTimeOut ? oppositeTimeout : getContext().getString(R.string
                                .otc_status_transfered_buyer),
                        isTimeOut ? mineTimeout : getContext().getString(R.string.otc_status_transfered_seller, String.valueOf(settingBean.getConfirmReceivablesDuration())));
                break;
            case OTCOrderStatus.Appeal:
                text = getDirectionText(isBuy, isTimeOut ? mineTimeout : getContext().getString(R.string
                                .otc_status_appeal_buyer, String.valueOf(settingBean.getHandlerAppealDuration())),
                        isTimeOut ? oppositeTimeout : getContext().getString(R.string.otc_status_appeal_seller));
                break;
            case OTCOrderStatus.UploadPayCertficate:
                text = getDirectionText(isBuy, isTimeOut ? mineTimeout : getContext().getString(R.string
                                .otc_status_upload_cert_buyer),
                        isTimeOut ? oppositeTimeout : getContext().getString(R.string.otc_status_upload_cert_seller));
                break;
            case OTCOrderStatus.WaitApproval:
                text = getDirectionText(isBuy, getContext().getString(R.string.otc_status_wait_approval_buyer),
                        getContext().getString(R.string.otc_status_wait_approval_seller));
                break;
            case OTCOrderStatus.CustomerRefund:
            case OTCOrderStatus.CustomerPayCoin:
                text = getDirectionText(isBuy, getContext().getString(R.string.otc_status_approval_complete),
                        getContext().getString(R.string.otc_status_approval_complete));
                mTvStatus.setTextColor(getResources().getColor(R.color.c_FF333333));
                break;
            case OTCOrderStatus.Finish:
                text = getDirectionText(isBuy, String.format(getContext().getString(R.string
                        .otc_status_finish_with_code), mBean.getCoinCode().toUpperCase()), getContext().getString(R.string
                        .otc_status_finish));
                mTvStatus.setTextColor(getResources().getColor(R.color.c_FF333333));
                break;
            case OTCOrderStatus.Cancel:
                text = getDirectionText(isBuy, getContext().getString(R.string.otc_status_cancel_buyer), getContext()
                        .getString(R.string.otc_status_cancel_seller));
                mTvStatus.setTextColor(getResources().getColor(R.color.c_FF333333));
                break;
            case OTCOrderStatus.WaitRefundCoin:
                text = getDirectionText(isBuy, "", "");
                break;
            case OTCOrderStatus.AgreeRefund:
                text = getDirectionText(isBuy, "", "");
                break;
        }
        mTvStatus.setText(text);
    }

    private String getDirectionText(boolean isBuy, String s, String s1) {
        return isBuy ? s : s1;
    }


}
