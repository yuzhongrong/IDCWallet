package com.idcg.idcw.activitys;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.app.WalletApplication;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.CheckOriginalPwdProviderServices;
import com.idcg.idcw.iprovider.ExchangeInProviderServices;
import com.idcg.idcw.model.bean.CheckNewPinBean;
import com.idcg.idcw.model.bean.CoinExchangeRateBean;
import com.idcg.idcw.model.bean.ExchangeResultBean;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.model.params.ExchangeInParam;
import com.idcg.idcw.utils.UIUtils;
import com.idcg.idcw.widget.NewKeyBoard;
import com.idcg.idcw.widget.NewPasswordView;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.AESUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.xuelianx.fingerlib.FingerFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

@Route(path = ArouterConstants.CheckFlashPin, name = "闪兑PIN页面")
public class CheckFlashPinActivity extends BaseWalletActivity implements NewPasswordView.PasswordListener {
    @BindView(R.id.pin_exit_login)
    ImageView pinExitLogin;
    @BindView(R.id.img_start)
    ImageView imgStart;
    @BindView(R.id.tv_btc_name)
    TextView tvBtcName;
    @BindView(R.id.tv_btc_price)
    TextView tvBtcPrice;
    @BindView(R.id.tv_eth_name)
    TextView tvEthName;
    @BindView(R.id.tv_eth_price)
    TextView tvEthPrice;
    @BindView(R.id.ll_start_siglar)
    LinearLayout llStartSiglar;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.passwordView)
    NewPasswordView passwordView;
    @BindView(R.id.ll_start)
    RelativeLayout llStart;
    @BindView(R.id.pin_forget_pass)
    TextView pinForgetPass;
    @BindView(R.id.pin_keyboard)
    NewKeyBoard pinKeyboard;
    @BindView(R.id.rootView)
    RelativeLayout rootView;

    private Vibrator vibrate;//调用系统手机震动
    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "ok"
    };

    @Autowired
    ExchangeInProviderServices mExchangeInProviderServices;

    @Autowired
    ExchangeInParam mExchangeInParam;
    @Autowired
    CoinExchangeRateBean mRate;
    private LoginStatus mLoginStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_flash_pin;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        setSubView();
        passwordView.setKeyBoard(pinKeyboard);
        pinKeyboard.Show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        rootView.setBackground(new BitmapDrawable(getResources(), ACacheUtil.get(mCtx).getAsBitmap("bibiflashbg")));

        passwordView.setPasswordListener(this);

        mLoginStatus = LoginUtils.getLoginBean(this);
        String touchid = ACacheUtil.get(mCtx).getAsString(mLoginStatus.getId() + "");
        // LogUtil.d("----touchid---->",touchid);
        if ((!TextUtils.isEmpty(touchid)) && touchid.equals("1")) {
            showFragment();
        }

        //操作键盘
        pinKeyboard.setOnClickKeyboardListener((position, value) -> {
            if (position < 11 && position != 9) {
                passwordView.addData(value);
            } else if (position == 9) {
            } else if (position == 11) {
                //点击完成 隐藏输入键盘
                passwordView.delData(value);
            }
        });
    }


    private void showFragment() {
        FingerFragment fingerFragment = new FingerFragment();
        fingerFragment.show(getFragmentManager(), "fingerFragment");
        fingerFragment.setmFragmentCallBack(new FingerFragment.Callback() {
            @Override
            public void onSuccess() {
                try {
                    String pass = ACacheUtil.get(mCtx).getAsString(mLoginStatus.getUser_name() + "");
                    String pay = new AESUtil().decrypt(pass);
//                    onCheckPinSuccess(pay);
                    RequestCheckOriginalPwd(pay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
            }
        });
    }


    private void setSubView() {
        //设置键盘
        pinKeyboard.setKeyboardKeys(KEY);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick({R.id.pin_exit_login, R.id.pin_forget_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pin_exit_login:
                finish();
                break;
            case R.id.pin_forget_pass:
                navigation(ArouterConstants.FORGET_PASS);
                break;
        }
    }

    public void exchangeSuccess(String txId) {
        ARouter.getInstance().build(ArouterConstants.FlashResult)
                .withParcelable("mExchangeInParam", mExchangeInParam)
                .withParcelable("mRate", mRate)
                .navigation();
        finish();
    }

    //键盘的灰调操作
    @Override
    public void passwordChange(String changeText) {

    }

    @Override
    public void passwordComplete() {
        addSubscribe(Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //输入完数字之后的回调，请求验证pin接口
//                        onCheckPinSuccess(pinPass);
                        RequestCheckOriginalPwd(pinPass);
                    }
                }));
    }

    @Autowired
    CheckOriginalPwdProviderServices checkOriginalPwdProviderServices;
    private void RequestCheckOriginalPwd(String pinPass) {
        LogUtil.e("pinPass  ---》  " + pinPass);
        LoginStatus bean = LoginUtils.getLoginBean(mCtx);
        checkOriginalPwdProviderServices.requestCheckOriginalPwdProvider(new CheckOriginalPwdReqParam("1", pinPass, bean.getDevice_id(), true))
                .subscribeWith(new RxSubscriber<CheckNewPinBean>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onSuccess(CheckNewPinBean checkNewPinBean) {
                        dismissDialog();
                        if (!checkNewPinBean.isLocked() && checkNewPinBean.isValid()) {
//                            navigation(ArouterConstants.BiBiFlash);
                            ARouter.getInstance().build(ArouterConstants.BiBiFlash)
                                    .withString("pin", pinPass).navigation();
                            finish();
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
                            Animation shake = AnimationUtils.loadAnimation(mCtx, R.anim.shake);
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
        Animation shake = AnimationUtils.loadAnimation(WalletApplication.getContext(), R.anim.shake);
        passwordView.startAnimation(shake);
    }

    private void onCheckPinSuccess(String pinPass) {
        if(mExchangeInParam==null)
            return;
        mExchangeInParam.setPayPassword(pinPass);
        LoginStatus bean = LoginUtils.getLoginBean(mCtx);
        addSubscribe(mExchangeInProviderServices.exchangeIn(mExchangeInParam).subscribeWith(new RxProgressSubscriber
                <ExchangeResultBean>(this) {
            @Override
            public void onSuccess(ExchangeResultBean data) {
                if(data.getStatusCode() == 0){
                    exchangeSuccess(data.getTxId());
                }else {
                    ToastUtil.show(data.getStatusMessage());
                }
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                passwordView.clearText();
                if(ex.getErrorCode().equals("121")){
                    UIUtils.post(() -> {
                        //ToastUtils.showToast(StartActivity.this, getString(R.string.pay_pass_word_error));
                        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {100, 400}; // 停止 开启 停止 开启
                        //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次
                        vibrate.vibrate(pattern, -1);
                        Animation shake = AnimationUtils.loadAnimation(mCtx, R.anim.shake);
                        passwordView.startAnimation(shake);
                    });
                }else{
                    ToastUtil.show(getString(R.string.server_connection_error));
                }
            }
        }));
    }

    private void pinErrorVibrate() {//pin输入错误的震动效果的方法
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400}; // 停止 开启 停止 开启
        //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次
        vibrate.vibrate(pattern, -1);
        passwordView.clearText();
        Animation shake = AnimationUtils.loadAnimation(mCtx, R.anim.shake);
        passwordView.startAnimation(shake);
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {

    }

    private String pinPass;
    @Override
    public void getPass(String pass, boolean isCom) {
        pinPass = pass;
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
