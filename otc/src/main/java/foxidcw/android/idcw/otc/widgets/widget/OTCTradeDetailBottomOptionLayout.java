package foxidcw.android.idcw.otc.widgets.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.SpUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.activitys.OTCTradeDetailActivity;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.iprovider.OTCCancelOrderServices;
import foxidcw.android.idcw.otc.iprovider.OTCCustomerOptionsServices;
import foxidcw.android.idcw.otc.model.OTCOrderStatus;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcSettingBean;
import foxidcw.android.idcw.otc.utils.OTCUtils;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTitleContent2BtnPopWindow;
import io.reactivex.functions.Consumer;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;


/**
 * 设计理念：垃圾自回收模式
 * Created by yuzhongrong on 2018/5/4.
 * 此页面是交易详情顶部状态页面，状态由自身管理，避免activity代码逻辑量过大
 */

public class OTCTradeDetailBottomOptionLayout extends LinearLayout implements View.OnClickListener {

    private CountdownView countdowntime;
    private ImageView cell_phone;
    private View root;
    private LinearLayout root_option;
    private TextView btn_left;
    private TextView btn_right;
    private TextView simple_tip;
    private TextView delay;
    private RelativeLayout root_timer;

    private OTCGetOtcOrdersBean bean;
    @Autowired
    OTCCustomerOptionsServices customerOptionsServices;

    @Autowired
    OTCCancelOrderServices cancelOrderServices;


    public OTCTradeDetailBottomOptionLayout(Context context) {
        super(context);
        initView();
    }

    public OTCTradeDetailBottomOptionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        ARouter.getInstance().inject(this);
        root = LayoutInflater.from(getContext()).inflate(R.layout.activity_otc_tradedetail_bottom_option, this, true);
        countdowntime = $(R.id.countdowntime);
        cell_phone = $(R.id.cell_phone);
        root_option = $(R.id.root_option);
        btn_left = $(R.id.btn_left);
        btn_right = $(R.id.btn_right);
        simple_tip = $(R.id.simple_tip);
        root_timer = $(R.id.root_timer);
        delay = $(R.id.delay);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        cell_phone.setOnClickListener(this);
        delay.setOnClickListener(this);
        countdowntime.setOnCountdownEndListener(cv -> {
            countdowntime.stop();
            LogUtil.d("-------倒计时结束----->");
            if (getContext() instanceof OnEndTimerListener) {

                ((OnEndTimerListener) getContext()).onEndTime(bean);
            }

        });

    }


    //通过订阅 这里负责ui上的显示
    @Subscriber(tag = EventTags.TAGS_STATUS_CHANGE)
    public void onOrderChanged(OTCGetOtcOrdersBean statebean) {
        bean = statebean;
        handlerCountDownTime(statebean);//初始化时间倒计时
        switch (statebean.getStatus()) {//不同状态设置不同ui

            case OTCOrderStatus.WaitPay://待转账
                if (statebean.getDirection() == 1) {//买家
                    root_option.setVisibility(VISIBLE);
                    delay.setVisibility(GONE);
                    btn_left.setText(R.string.str_can_trade);
                    btn_right.setText(R.string.str_action_wait_pay);

                } else if (statebean.getDirection() == 2) {//卖家
                    root_option.setVisibility(GONE);
                    if (bean.isIsTimeExpand()) {//已经延时

                        delay.setVisibility(GONE);
                        simple_tip.setVisibility(VISIBLE);
                        simple_tip.setText(R.string.otc_str_already_delay_time);

                    } else {//延时倒计时中
                        delay.setVisibility(VISIBLE);
                        simple_tip.setVisibility(GONE);
                    }


                }


                break;

            case OTCOrderStatus.delay://卖家设置延时


                break;

            case OTCOrderStatus.Transfered://已支付 只剩下时间倒计时不为0 显示出来
            case OTCOrderStatus.Paied:
                if (statebean.getDirection() == 1) {//买家开始倒计时
                    delay.setVisibility(GONE);
                    root_option.setVisibility(GONE);
                    simple_tip.setVisibility(GONE);

                } else if (statebean.getDirection() == 2) {//卖家显示[提起申诉|确认收款]
                    simple_tip.setVisibility(GONE);
                    delay.setVisibility(GONE);
                    root_option.setVisibility(VISIBLE);
                    btn_left.setText(R.string.str_apply_appeal);
                    btn_right.setText(R.string.str_otc_confirm_money);


                }

                break;

            case OTCOrderStatus.Appeal://申诉


                if (statebean.getDirection() == 1) {//买家显示【取消交易|上传凭证】
                    simple_tip.setVisibility(GONE);
                    delay.setVisibility(GONE);
                    root_option.setVisibility(VISIBLE);
                    btn_left.setText(R.string.str_can_trade);
                    btn_right.setText(R.string.str_otc_upload_certificate_img);

                } else if (statebean.getDirection() == 2) {//卖家显示【倒计时】

                    delay.setVisibility(GONE);
                    root_option.setVisibility(GONE);
                    simple_tip.setVisibility(GONE);
                }

                break;

            case OTCOrderStatus.WaitApproval://审核状态只有上传凭证的图片显示
                root_timer.setVisibility(GONE);
                delay.setVisibility(GONE);
                root_option.setVisibility(GONE);
                simple_tip.setVisibility(GONE);
                break;

            case OTCOrderStatus.UploadPayCertficate://只显示倒计时
                delay.setVisibility(GONE);
                root_option.setVisibility(GONE);
                simple_tip.setVisibility(GONE);
                break;
            case OTCOrderStatus.Finish://交易完成不管是买家还是卖家都不存在这个页面了
            case OTCOrderStatus.Cancel://取消交易这个页面也不存存在了
                this.setVisibility(GONE);
                break;
            default:
                this.setVisibility(GONE);
                break;


        }
    }


    //这里负责点击处理区分买家还是卖家
    @Override
    public void onClick(View v) {

        if (bean == null) return;

        if (v.getId() == R.id.btn_left) {

            if (bean.getDirection() == 1) {//买方


                switch (bean.getStatus()) {

                    case OTCOrderStatus.WaitPay://待转账
                        cancelTrade(getResources().getString(R.string.str_otc_cancel_order_info_with_24));//取消交易
                        break;

                    case OTCOrderStatus.Appeal://【取消交易|上传凭证】
                    case OTCOrderStatus.UploadPayCertficate://上传凭证状态
                        cancelTrade(getResources().getString(R.string.otc_str_otc_order_cancel_hint));
                        break;

                }


            } else if (bean.getDirection() == 2) {//卖方

                switch (bean.getStatus()) {

                    case OTCOrderStatus.WaitPay://等待买家付款状态


                        break;

                    case OTCOrderStatus.Transfered:
                    case OTCOrderStatus.Paied://【已转账---->申诉】
                        customerOptionsServices.requestApplyAppealActionService(bean.getId() + "")
                                .compose(((BaseWalletActivity) getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                                .subscribeWith(new RxProgressSubscriber<Object>((BaseWalletActivity) getContext()) {

                                    @Override
                                    public void onSuccess(Object data) {

                                        LogUtil.d("-----申诉成功-------->" + data);
//                                        ToastUtil.show("申诉成功");

                                    }

                                    @Override
                                    protected void onError(ResponseThrowable ex) {
                                        LogUtil.d("-----申诉失败------->" + ex.getMessage());
                                        ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));

                                    }
                                });


                        break;
                }


            }


        } else if (v.getId() == R.id.btn_right) {

            if (bean.getDirection() == 1) {//买方

                switch (bean.getStatus()) {

                    case OTCOrderStatus.WaitPay://待付款【待付款--->已付款】
                        OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(getContext());
                        OTCDeleteCurrPayPopWindow.setTitle(getResources().getString(R.string.otc_chat_with_sure_send));
                        OTCDeleteCurrPayPopWindow.showPopupWindow();
                        OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {

                            customerOptionsServices
                                    .requestConfirmPayService(bean.getId() + "")
                                    .compose(((BaseWalletActivity) getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                                    .subscribeWith(new RxProgressSubscriber<Object>((BaseWalletActivity) getContext()) {


                                        @Override
                                        public void onSuccess(Object data) {

                                            LogUtil.d("-----确认转账成功-------->" + data);
                                            OTCDeleteCurrPayPopWindow.dismiss();
                                        }

                                        @Override
                                        protected void onError(ResponseThrowable ex) {
                                            LogUtil.d("-----确认转账失败-------->" + ex.getMessage());
                                            //    ToastUtil.common("确认转账失败"+ex.getMessage());
                                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                            OTCDeleteCurrPayPopWindow.dismiss();
                                        }
                                    });

                        });
                        break;

                    case OTCOrderStatus.Appeal://【上传凭证】
                        //点击上传凭证 请求接口 改变订单状态 然后进入 上传图片页面  上传完成后进入审核状态
                        customerOptionsServices.requestAppeal2UploadCerfiticate(bean.getId() + "")
                                .compose(((BaseWalletActivity) getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                                .subscribeWith(new RxProgressSubscriber<Object>((BaseWalletActivity) getContext()) {


                                    @Override
                                    public void onSuccess(Object data) {

                                        LogUtil.d("-----切换到上传凭证成功-------->" + data);

                                    }

                                    @Override
                                    protected void onError(ResponseThrowable ex) {
                                        LogUtil.d("-----切换到上传凭证失败-------->" + ex.getMessage());
                                        //  ToastUtil.common("确认转账失败"+ex.getMessage());
                                        ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));

                                    }
                                });

                        break;

                }

            } else if (bean.getDirection() == 2) {//卖方

                switch (bean.getStatus()) {

                    case OTCOrderStatus.WaitPay://待转账


                        break;

                    case OTCOrderStatus.delay://卖家延时


                        break;


                    case OTCOrderStatus.Transfered://【已付款--->确认收款】
                    case OTCOrderStatus.Paied:
                        OTCTitleContent2BtnPopWindow popWindow = new OTCTitleContent2BtnPopWindow(getContext())
                                .setTitle(getResources().getString(R.string.str_otc_confirm_money))
                                .setContent(getResources().getString(R.string.otc_confirm_receive_money))
                                .setClickListener(new OTCTitleContent2BtnPopWindow.BtnClickListener() {
                                    @SuppressLint("CheckResult")
                                    @Override
                                    public void onBtnClick(int type) {
                                        if (type == 1) {
                                            customerOptionsServices
                                                    .requestConfirmMoneyActionService(bean.getId() + "")
                                                    .compose(((BaseWalletActivity) getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                                                    .subscribeWith(new RxProgressSubscriber<Object>((BaseWalletActivity) getContext()) {


                                                        @Override
                                                        public void onSuccess(Object data) {
                                                            LogUtil.d("-----确认收款成功-------->" + data);
                                                            ToastUtil.show(getContext().getString(R.string.str_confirm_money_success));
                                                            resetCountDownTime();
                                                            //为了确保一定成功除了eventbus推送一次 这里也要主动去请求一次
                                                            ((OTCTradeDetailActivity) getContext()).requestTradeDetailInfo(bean.getId() + "");


                                                        }

                                                        @Override
                                                        protected void onError(ResponseThrowable ex) {
                                                            LogUtil.d("-----确认收款失败-------->" + ex.getMessage());
                                                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                                        }
                                                    });
                                        }
                                    }
                                });
                        popWindow.showPopupWindow();
                        break;
                }

            }


        } else if (v.getId() == R.id.delay) {//卖家延时操作

            customerOptionsServices
                    .requestDelayActionService(bean.getId() + "")
                    .compose(((BaseWalletActivity) getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeWith(new RxProgressSubscriber<Object>((BaseWalletActivity) getContext()) {


                        @Override
                        public void onSuccess(Object data) {

                            //因为这里延时状态和等待支付状态统
                            LogUtil.d("-----延时交易请求成功-------->" + data);
                            ToastUtil.show(getContext().getResources().getString(R.string.otc_set_delay_time_success));


                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
                            LogUtil.d("-----延时交易请求失败-------->" + ex.getMessage());
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                        }
                    });

        } else if (v.getId() == R.id.cell_phone) {
            LoginStatus loginBean = LoginUtils.getLoginBean(BaseApplication.getContext());
            int id = -1;
            if (loginBean != null)
                id = loginBean.getId();

            String phone = bean.getUserId() == id ? bean.getAcceptantPhone() : bean.getUserPhone();
            OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(getContext());
            OTCDeleteCurrPayPopWindow.setTitle(getResources().getString(R.string.otc_chat_with_phone_num, phone));
            OTCDeleteCurrPayPopWindow.showPopupWindow();
            OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {
                OTCDeleteCurrPayPopWindow.dismiss();
                cellPhone(phone);
            });
        }


//        OTCCellPhonePopWIndow cellPhonePopWIndow=new OTCCellPhonePopWIndow(getContext());
//        if(cellPhonePopWIndow!=null)cellPhonePopWIndow.showPopupWindow(this);


    }


    //拨打电话
    private void cellPhone(String phone) {
        if (TextUtils.isEmpty(phone))
            return;
        new RxPermissions((Activity) getContext())
                .request(Manifest.permission.CALL_PHONE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + phone);
                            intent.setData(data);
                            startActivity(intent);
                        } else {
                            ToastUtil.show(getResources().getString(R.string.permission_deny));
                        }
                    }
                });

    }


    private void cancelTrade(String hintText) {
        int count = SpUtils.getIntData("cancel_count", 0);
        String tradeSettingInfo = SpUtils.getStringData("otc_trade_setting_bean", "");
        OTCGetOtcSettingBean settingBean = new OTCGetOtcSettingBean();
        if (!TextUtils.isEmpty(tradeSettingInfo)) {
            settingBean = new Gson().fromJson(tradeSettingInfo, OTCGetOtcSettingBean.class);
        }
        OTCTitleContent2BtnPopWindow popWindow = new OTCTitleContent2BtnPopWindow(getContext());
        popWindow.setTitle(getResources().getString(R.string.str_otc_cancel_deal_title))
                .setContent(String.format(hintText,
                        OTCUtils.getCancelCountStringWithLanguage(getContext(),count + 1), settingBean.getAllowCancelOrderDuration(),
                        settingBean.getAllowCancelOrderCount(), settingBean.getCancelOrderForbidTradeDuration()))
                .setConfirmContent(getResources().getString(R.string.otc_str_btn_yes))
                .setCancelContent(getResources().getString(R.string.otc_str_btn_no))
                .setClickListener(type -> {
                    if (type != 1) return;
                    cancelOrderServices.agreeRefund(bean.getId() + "")
                            .compose(((BaseWalletActivity) getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                            .subscribeWith(new RxProgressSubscriber<Object>((BaseWalletActivity) getContext()) {

                                @Override
                                public void onSuccess(Object data) {

                                    LogUtil.d("-----取消交易成功-------->" + data);
                                    resetCountDownTime();
//                                    ToastUtil.show("申诉成功");

                                }

                                @Override
                                protected void onError(ResponseThrowable ex) {
                                    LogUtil.d("-----取消交易失败------->" + ex.getMessage());
                                    ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));

                                }
                            });

                });
        popWindow.showPopupWindow();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }


    public View getView(int resid) {
        return $(resid);
    }


    public <T extends View> T $(@IdRes int resId) {
        return (T) root.findViewById(resId);
    }


    public interface OnEndTimerListener {

        void onEndTime(OTCGetOtcOrdersBean orderid);

    }

    private void resetCountDownTime() {

        if (countdowntime != null) {

            countdowntime.stop();
            countdowntime.allShowZero();

        }
    }

    private void handlerCountDownTime(OTCGetOtcOrdersBean statebean) {

        countdowntime.allShowZero();
        countdowntime.stop();
        DynamicConfig.Builder builder = new DynamicConfig.Builder().setShowHour(true);
        if (statebean.getStatusTimeSeconds() > (59 * 60) || statebean.getStatus() == OTCOrderStatus.Transfered || statebean.getStatus() == OTCOrderStatus.Paied) {
            builder.setShowHour(true);

        } else {
            builder.setShowHour(false);
        }
        countdowntime.dynamicShow(builder.build());
        countdowntime.start(statebean.getStatusTimeSeconds() * 1000);
    }
}
