package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.CheckOriginalPwdProviderServices;
import com.idcg.idcw.iprovider.ExitAppProviderServices;
import com.idcg.idcw.iprovider.SettingStateProviderServices;
import com.idcg.idcw.model.bean.ChatHubBean;
import com.idcg.idcw.model.bean.CheckNewPinBean;
import com.idcg.idcw.model.bean.CheckStatusBean;
import com.idcg.idcw.model.bean.PositionBean;
import com.idcg.idcw.model.bean.TradeNewConfigBean;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.utils.UIUtils;
import com.idcg.idcw.widget.NewKeyBoard;
import com.idcg.idcw.widget.NewPasswordView;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.AESUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.NetworkUtil;
import com.cjwsc.idcm.Utils.ScreenUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.constant.BaseSignalConstants;
import com.cjwsc.idcm.iprovider.GetTradeConfigServices;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.cjwsc.idcm.signarl.impl.HubOnDataCallBackImpl;
import com.google.gson.Gson;
import com.idcw.pay.utils.IDCWPayBackUtils;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.xuelianx.fingerlib.FingerFragment;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.controller.client.ClientServiceController;
import foxidcw.android.idcw.common.controller.sise.RealTrendCallback;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.model.bean.TradeConfigPriceBean;
import foxidcw.android.idcw.common.utils.DialogVersionUtil;
import foxidcw.android.idcw.foxcommon.Constants.Constants;
import foxidcw.android.idcw.otc.iprovider.OTCGetSignalrUrlServices;


/**
 * Created by hpz on 2018/1/23.
 */
@Route(path = Constants.PIN_START, name = "PIN页面")
public class StartActivity extends BaseWalletActivity implements NewPasswordView.PasswordListener, View.OnClickListener , RealTrendCallback {
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.passwordView)
    NewPasswordView passwordView;
    @BindView(R.id.ll_start)
    RelativeLayout llStart;
    @BindView(R.id.pin_keyboard)
    NewKeyBoard pinKeyboard;
    @BindView(R.id.pin_exit_login)
    TextView pinExitLogin;
    @BindView(R.id.pin_forget_pass)
    TextView pinForgetPass;

    private String pinPass;
    private Dialog dialogOut;
    private View inflater;
    private TextView tv_login_out;
    private TextView tv_out_cancel;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "ok"
    };

    String finalStrBtcConfigID;
    String finalStrEthConfigID;
    private TextView tv_btc_price;
    private TextView tv_eth_price;

    @Autowired(name = "pinStart")
    String pinStart = "pin";//币种code

    @Autowired
    CheckOriginalPwdProviderServices checkOriginalPwdProviderServices;

    @Autowired
    SettingStateProviderServices mSettingStateServices;

    @Autowired
    ExitAppProviderServices exitAppProviderServi;

    @Autowired
    OTCGetSignalrUrlServices otcGetSignalrUrlServices;

    @Autowired
    GetTradeConfigServices mGetTradeConfigServices;

    // 本地保存的登录参数
    private LoginStatus mLoginStatusBean = null;
    private Dialog mBackDialog = null;
    private final String btc_group = "_DSQ3BmslE-cS-HP3POlnA";
    private final String eth_group = "_DSQ3BmslE-cS-HP4POlnA";
    private boolean isFromPay = false;
    private String btc;
    private String eth;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        checkIsToPay();
    }

    @Override
    protected void onInitView(Bundle bundle) {
        LogUtil.e("class==", LoginUtils.getLoginBean(mCtx).getGuid());
        tv_eth_price = (TextView) findViewById(R.id.tv_eth_price);
        tv_btc_price = (TextView) findViewById(R.id.tv_btc_price);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder("$ " + "0.00");
        stringBuilder.setSpan(new AbsoluteSizeSpan(ScreenUtil.sp2px(StartActivity.this, 12)), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv_btc_price.setText(stringBuilder);
        tv_eth_price.setText(stringBuilder);

        LogUtil.e("====>", ScreenUtil.getScreenWidth(this) + "");
        LogUtil.e("====>", ScreenUtil.getScreenHeight(this) + "");

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        LogUtil.e("DEN", "Density is " + displayMetrics.density + " densityDpi is " + displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels +
                " width: " + displayMetrics.widthPixels + " DP is:" + convertPixelToDp(displayMetrics.widthPixels));

        ARouter.getInstance().inject(this);
        mLoginStatusBean = LoginUtils.getLoginBean(mCtx);
        setSubView();
        initExitDialog();
        passwordView.setKeyBoard(pinKeyboard);
        pinKeyboard.Show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        LoginStatus bean = LoginUtils.getLoginBean(this);
        LogUtil.e("beanid==", bean.getDevice_id() + "");
        passwordView.setPasswordListener(this);

        String touchid = ACacheUtil.get(StartActivity.this).getAsString(bean.getId() + "");
        // LogUtil.d("----touchid---->",touchid);
        if ((!TextUtils.isEmpty(touchid)) && touchid.equals("1")) {
            showFragment();
        }

        //操作键盘
        pinKeyboard.setOnClickKeyboardListener((position, value1) -> {
            if (position < 11 && position != 9) {
                passwordView.addData(value1);
            } else if (position == 9) {
                //passwordView.delData(value);
            } else if (position == 11) {
                //点击完成 隐藏输入键盘
                passwordView.delData(value1);
            }
        });

        if (!NetworkUtil.isNetworkAvailable(this)) {
            tvStart.setText(getResources().getString(R.string.str_check_net_work));
        }
        getSignalrUrl();

        checkIsToPay();
    }

    @SuppressLint("CheckResult")
    private void getSignalrUrl() {

        otcGetSignalrUrlServices
                .getSignalrUrl()
                .compose(this.bindToLifecycle())
                .subscribeWith(new RxSubscriber<Object>() {

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        LogUtil.d("-----pin页面获取signalrurl 失败---》" + ex.getMessage());

                    }

                    @Override
                    public void onSuccess(Object o) {

                        LogUtil.d("-----pin页面获取signalrurl 成功---》" + o.toString());
                        //   ACacheUtil.get(WalletApplication.getContext()).put(OTCAcacheKeys.SIGNALR_URL, eth.toString());
                        BaseSignalConstants.SIGNAL_HOST = o.toString();


                    }
                });

    }


    private void checkIsToPay() {
        Intent intent = getIntent();
        if (intent.hasExtra(IDCWPayBackUtils.FROM_IDCW_PAY)) {
            isFromPay = intent.getBooleanExtra(IDCWPayBackUtils.FROM_IDCW_PAY, false);
        }
        // 这里改为封装类统一处理
        if (isFromPay) {
            IDCWPayBackUtils.checkToMerchantsWithIntent(mCtx, intent);
            intent.putExtra(IDCWPayBackUtils.FROM_IDCW_PAY, false);
            isFromPay = false;
        }
    }


    private int convertPixelToDp(int pixel) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return (int) (pixel / displayMetrics.density);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        if (dialogVersionUtil == null) {
            dialogVersionUtil = new DialogVersionUtil();
            dialogVersionUtil.checkVersion(this);
        }
        mGetTradeConfigServices.getTradeConfig()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Gson gson = new Gson();
                        TradeNewConfigBean tradeNewConfigBean = gson.fromJson(data, TradeNewConfigBean.class);
                        if (tradeNewConfigBean.isStatus() && "200".equals(tradeNewConfigBean.getStatusCode())) {
                            String host = tradeNewConfigBean.getData().getSignalr();
                            String groupid = tradeNewConfigBean.getData().getDefaultTradeConfigID();
                            String strBtcConfigID = "";
                            String strethConfigID = "";

                            for (TradeNewConfigBean.DataBean.TradeVarietyListBean item : tradeNewConfigBean.getData().getTradeVarietyList()) {
                                if (item.getSymbol().contains("BTC") && item.getSymbol().contains("USD")) {
                                    strBtcConfigID = item.getTradingConfigID();
                                }

                                if (item.getSymbol().contains("ETH") && item.getSymbol().contains("USD")) {
                                    strethConfigID = item.getTradingConfigID();
                                }
//                                if ("BTC/VUSD".equals(item.getSymbol())) {
//                                    strBtcConfigID = item.getTradingConfigID();
//                                }
//
//                                if ("ETH/VUSD".equals(item.getSymbol())) {
//                                    strethConfigID = item.getTradingConfigID();
//                                }
                            }
                            LogUtil.e("StartPin==>", host + "-------" + groupid);
                            if (TextUtils.isEmpty(host) || TextUtils.isEmpty(groupid)) return;
                            //订阅Signalr
                            finalStrBtcConfigID = strBtcConfigID;
                            finalStrEthConfigID = strethConfigID;
                            initSignalr(host, groupid, "ExchangesHub");//初始化
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.str_server_busy_error));
                    }
                });
    }

    @Override
    protected BaseView getView() {
        return null;
    }


    private void initExitDialog() {
        try {
            dialogOut = new Dialog(this, R.style.shuweiDialog);
            inflater = LayoutInflater.from(this).inflate(R.layout.dialog_exit_hint_phrase, null);
            dialogOut.setContentView(inflater);
            dialogOut.setCancelable(false);
            Window dialogWindow = dialogOut.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
            tv_login_out = (TextView) inflater.findViewById(R.id.out_sure);
            tv_out_cancel = (TextView) inflater.findViewById(R.id.out_cancel);
            tv_login_out.setOnClickListener(this);
            tv_out_cancel.setOnClickListener(this);
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private void showFragment() {
        FingerFragment fingerFragment = new FingerFragment();
        fingerFragment.show(getFragmentManager(), "fingerFragment");
        fingerFragment.setCancelable(false);
        fingerFragment.setmFragmentCallBack(new FingerFragment.Callback() {
            @Override
            public void onSuccess() {
                try {
                    LoginStatus bean = LoginUtils.getLoginBean(StartActivity.this);
                    String pass = ACacheUtil.get(StartActivity.this).getAsString(bean.getUser_name() + "");
                    String pay = new AESUtil().decrypt(pass);
                    LogUtil.e("onSuccess==", pay);
                    RequestCheckOriginalPwd(pay);
                } catch (Exception ex) {
                    LogUtil.e("onSuccess:", ex.getMessage());
                }
            }

            @Override
            public void onError() {
            }
        });
    }


    @Override
    public void passwordChange(String changeText) {
        LogUtil.e("changeText   -> " + changeText);
    }

    @Override
    public void passwordComplete() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            UIUtils.postDelayed(() -> {
                vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {100, 400}; // 停止 开启 停止 开启
                //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次
                vibrate.vibrate(pattern, -1);
                passwordView.clearText();
                Animation shake = AnimationUtils.loadAnimation(StartActivity.this, R.anim.shake);
                passwordView.startAnimation(shake);
            }, 500);
            ToastUtil.show(getString(R.string.str_check_net_work));

            return;
        }
        UIUtils.postDelayed(() -> {
            RequestCheckOriginalPwd(pinPass);
        }, 500);
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {
    }

    @Override
    public void getPass(String pass, boolean isComplete) {
        pinPass = pass;
        LogUtil.e("getPass   ---> " + pinPass);
    }

    Vibrator vibrate;


    @SuppressLint("CheckResult")
    private void RequestCheckOriginalPwd(String pinPass) {
        LogUtil.e("pinPass  ---》  " + pinPass);
        LoginStatus bean = LoginUtils.getLoginBean(mCtx);
        checkOriginalPwdProviderServices.requestCheckOriginalPwdProvider
                (new CheckOriginalPwdReqParam("1", pinPass, bean.getDevice_id(), true))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxSubscriber<CheckNewPinBean>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onSuccess(CheckNewPinBean checkNewPinBean) {
                        dismissDialog();
                        EventBus.getDefault().post(new PositionBean(8));
                        if (!checkNewPinBean.isLocked() && checkNewPinBean.isValid()) {
                            LogUtil.e("pin_start==", "完全退出app后直接从这里进入主页");
                            if (pinStart != null && pinStart.equals("pinStart")) {
                                finish();
                            } else {
                                //intentFActivity(MainActivity.class);
                                finish();
                                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("language", "language");
                                intent.putExtra("Language", bundle);
                                startActivity(intent);

                            }
                        } else if (!checkNewPinBean.isLocked() && !checkNewPinBean.isValid()) {
                            checkPinErrorHint();
                            if (checkNewPinBean.getResidueCount() == 3) {
                                ToastUtil.show(String.format(getString(R.string.str_pin_input_count), checkNewPinBean.getResidueCount()));
                            } else if (checkNewPinBean.getResidueCount() == 2) {
                                ToastUtil.show(String.format(getString(R.string.str_pin_input_count), checkNewPinBean.getResidueCount()));
                            } else if (checkNewPinBean.getResidueCount() == 1) {
                                ToastUtil.show(String.format(getString(R.string.str_pin_input_count), checkNewPinBean.getResidueCount()));
                            } else if (checkNewPinBean.getCountingTime() > 0) {
                                String pinStartCounting = getShowCountDownMsg(checkNewPinBean.getCountingTime());
                                ToastUtil.show(String.format(getString(R.string.str_pin_retry_count), pinStartCounting));
                            } else if (checkNewPinBean.getCountingTime() > 0 && checkNewPinBean.getCountingTime() < 60000) {
                                ToastUtil.show(getString(R.string.str_pin_retry_min_count));
                            } else if (checkNewPinBean.getCountingTime() > 0 && checkNewPinBean.getCountingTime() < 3600000) {
                                String pinStartCounting = getShowCountDownMsg(checkNewPinBean.getCountingTime());
                                ToastUtil.show(String.format(getString(R.string.str_pin_retry_count), pinStartCounting));

                            }
                        } else if (checkNewPinBean.isLocked()) {
                            checkPinErrorHint();
                            if (checkNewPinBean.getCountingTime() == 10800000) {
                                ToastUtil.show(getString(R.string.str_pin_is_lock));
                                return;
                            }
                            String pinStartCounting = getShowCountDownMsg(checkNewPinBean.getCountingTime());
                            ToastUtil.show(String.format(getString(R.string.str_pin_retry_count), pinStartCounting));
                            if (checkNewPinBean.getCountingTime() > 0 && checkNewPinBean.getCountingTime() < 60000) {
                                ToastUtil.show(getString(R.string.str_pin_retry_min_count));
                            } else if (checkNewPinBean.getCountingTime() > 0 && checkNewPinBean.getCountingTime() < 3600000) {
                                String pinStartCounting1 = getShowCountDownMsg(checkNewPinBean.getCountingTime());
                                ToastUtil.show(String.format(getString(R.string.str_pin_retry_count), pinStartCounting1));
                            }
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        passwordView.clearText();
                        if (ex.getErrorCode().equals("121")) {
                            vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            long[] pattern = {100, 400}; // 停止 开启 停止 开启
                            //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次
                            vibrate.vibrate(pattern, -1);
                            passwordView.clearText();
                            Animation shake = AnimationUtils.loadAnimation(StartActivity.this, R.anim.shake);
                            passwordView.startAnimation(shake);
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }
                });
    }

    private String getShowCountDownMsg(long ms) {
        int day = 0;
        int hour;
//        if (!mCountdown.isConvertDaysToHours) {
//            day = (int) (ms / (1000 * 60 * 60 * 24));
//            hour = (int) ((ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
//        } else {
        hour = (int) (ms / (1000 * 60 * 60));
//        }
        int minute = (int) ((ms % (1000 * 60 * 60)) / (1000 * 60));
        //int second = (int) ((ms % (1000 * 60)) / 1000);秒
//        int millisecond = (int) (ms % 1000);
        if (ms > 0 && ms < 3600000) {
            return String.format(WalletApplication.getContext().getResources().getString(R.string.c2c_remain_time_min_msg), minute);
        }
        return String.format(WalletApplication.getContext().getResources().getString(R.string.c2c_remain_time_msg), hour, minute);
    }

    private void checkPinErrorHint() {//输入错误的震动提示
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400}; // 停止 开启 停止 开启
        //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次
        vibrate.vibrate(pattern, -1);
        passwordView.clearText();
        Animation shake = AnimationUtils.loadAnimation(StartActivity.this, R.anim.shake);
        passwordView.startAnimation(shake);
    }

    private void setSubView() {
        //设置键盘
        pinKeyboard.setKeyboardKeys(KEY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.pin_exit_login, R.id.pin_forget_pass, R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pin_exit_login:
                if (ACacheUtil.get(StartActivity.this).getAsString("old") != null && ACacheUtil.get(StartActivity.this).getAsString("old").equals("old") ||
                        ACacheUtil.get(StartActivity.this).getAsString("find") != null && ACacheUtil.get(StartActivity.this).getAsString("find").equals("find") ||
                        ACacheUtil.get(StartActivity.this).getAsString("namePhrase") != null && ACacheUtil.get(StartActivity.this).getAsString("namePhrase").equals("namePhrase") ||
                        ACacheUtil.get(StartActivity.this).getAsString("confirm") != null && ACacheUtil.get(StartActivity.this).getAsString("confirm").equals("confirm")) {
                    dialogOut.show();
                } else {
                    showHintBackDialog();
                }
                break;
            case R.id.pin_forget_pass:
                navigation(ArouterConstants.FORGET_PASS);
                break;
            case R.id.tv_start:
//                commonNotificationUtils = new CommonNotificationUtils(
//                        mCtx,
//                        "转汇通知",
//                        "您发送了 0.57362 BTC，您向Alex发起了卖出0.0135BTC的订单请求，您兑入了2.5983BTC，您向Alex发起了卖出0.0135BTC的订单请求",
//                        R.mipmap.icon_app_logo,
//                        R.drawable.icon_otc,
//                        "IDCW",
//                        "交易通知");
                //navigation(ArouterConstants.RecoverAsset);

//                ClientServiceController.Instance().addCallback(this);
//                ClientServiceController.Instance().Sub(this,"_DSQ3BmslE-cS-HP3POlnA");
//                ClientServiceController.Instance().Open();

                break;


        }
    }

    private void RequestCheckPin() {
        showDialog();
        //3.29发现bug，修复
        addSubscribe(mSettingStateServices.requestCheckStatusProvider(mLoginStatusBean.getDevice_id(), true).subscribeWith(new RxSubscriber<CheckStatusBean>() {
            @Override
            public void onSuccess(CheckStatusBean checkStatusBean) {
                dismissDialog();
                setUI(checkStatusBean);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                dismissDialog();
            }
        }));
    }

    /**
     * 设置界面参数
     *
     * @param checkStatusBean
     */
    private void setUI(CheckStatusBean checkStatusBean) {
        if (checkStatusBean != null) {
            // 备份短语
            if (checkStatusBean.isWallet_phrase()) {
                dialogOut.show();
            } else {
                showHintBackDialog();
            }
        }
    }

    private void showHintBackDialog() {
        try {
            if (mBackDialog == null) {
                mBackDialog = new Dialog(mCtx, R.style.shuweiDialog);
                View exitContentView = LayoutInflater.from(mCtx).inflate(R.layout.dialog_back_phrase_hint_ok, null);
                mBackDialog.setContentView(exitContentView);
                mBackDialog.setCancelable(false);
                Window dialogWindow = mBackDialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setAttributes(lp);
                exitContentView.findViewById(R.id.btn_back_close).setOnClickListener(this);
            }
            if (mBackDialog != null) {
                mBackDialog.show();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.out_sure:
                //
                if (dialogOut != null && dialogOut.isShowing()) dialogOut.dismiss();
                LoginStatus bean = LoginUtils.getLoginBean(StartActivity.this);
                ACacheUtil.get(mCtx).remove(bean.getDevice_id() + AcacheKeys.SAVEISVAILDPIN);
                ACacheUtil.get(mCtx).remove(bean.getId() + AcacheKeys.cache1);
                ACacheUtil.get(mCtx).remove("old");
                ACacheUtil.get(mCtx).remove("namePhrase");
                ACacheUtil.get(mCtx).remove("find");
                ACacheUtil.get(mCtx).remove("confirm");
                ACacheUtil.get(mCtx).remove("pinPass");
                ACacheUtil.get(mCtx).remove(AcacheKeys.loginbean);
                AppManager.getInstance().finishAllActivity();
                intentFActivity(MainGuideActivity.class);
                break;
            case R.id.out_cancel:
                if (dialogOut != null && dialogOut.isShowing()) dialogOut.dismiss();
                break;
            case R.id.btn_back_close:
                if (mBackDialog != null && mBackDialog.isShowing()) mBackDialog.dismiss();
                break;
        }
    }
//
//    @Override
//    public boolean isAutoCreateSignalrConnection() {
//        return false;
//    }


    @Override
    public void subScribes() {//链接成功 订阅

        if (subscribeStack != null && !subscribeStack.empty()) return;

        subscribe("RealTrendCallback", new HubOnDataCallBackImpl<List<ChatHubBean>>() {

            @Override
            public void convert(List<ChatHubBean> chatHubBeans) {

                LogUtil.e("------chatHubBeans------->" + chatHubBeans.size());
                for (ChatHubBean x : chatHubBeans) {
                    if (x.getTradingConfigID().equals(finalStrBtcConfigID)) {
                        LogUtil.e("------------btc最新行情----------" + x.getNewest());
                        //tv_btc_price.setText(x.getNewest()+"");
                        SpannableStringBuilder stringBuilder = new SpannableStringBuilder("$ " + Utils.toSubStringDegist(x.getNewest(), 2));
                        btc = Utils.toSubStringDegist(x.getNewest(), 2);
                        stringBuilder.setSpan(new AbsoluteSizeSpan(ScreenUtil.sp2px(StartActivity.this, 12)), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        if (tv_btc_price != null) {
                            tv_btc_price.post(() -> {
                                tv_btc_price.setText(stringBuilder);
                            });

                        }
                    }
                    if (x.getTradingConfigID().equals(finalStrEthConfigID)) {
                        LogUtil.e("------------eth最新行情----------" + x.getNewest());
                        //tv_eth_price.setText(x.getNewest()+"");
                        SpannableStringBuilder stringBuilder = new SpannableStringBuilder("$ " + Utils.toSubStringDegist(x.getNewest(), 2));
                        eth = Utils.toSubStringDegist(x.getNewest(), 2);
                        stringBuilder.setSpan(new AbsoluteSizeSpan(ScreenUtil.sp2px(StartActivity.this, 12)), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        if (tv_eth_price != null) {
                            tv_eth_price.post(() -> {
                                tv_eth_price.setText(stringBuilder);
                            });
                        }
                    }
                }
            }
        });


    }

    @Override
    protected void OnNetFail() {
        if (tvStart != null) tvStart.setText(getString(R.string.str_check_net_work));
    }

    @Override
    protected void OnNetSuccess() {
        if (tvStart != null) tvStart.setText(getString(R.string.input_pin));
        onEvent();
        subScribes();
    }

//    @Override
//    protected void checkAppVersion() {
//        //DialogVersionUtil.getInstance(this).checkVersion();
//    }

    @Subscriber
    public void refresthThisBuyList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 333) {
            showFragment();
        }
    }


    @Override
    protected boolean checkToPay() {
        return false;
    }


    @Override
    public void RealTrendCallback(TradeConfigPriceBean tradeConfigPriceBean) {
        new Thread(()->{

           // tvStart.setText(tradeConfigPriceBean.getNewest()+"");
        }).start();
    }
}
