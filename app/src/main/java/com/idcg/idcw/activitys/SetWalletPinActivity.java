package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.iprovider.ReqRegisterIproviderServices;
import com.idcg.idcw.iprovider.SetPayPassInfoProviderServices;
import com.idcg.idcw.model.bean.EventBean;
import com.idcg.idcw.model.bean.PhraseListBean;
import com.idcg.idcw.model.bean.PositionBean;
import com.idcg.idcw.model.bean.VerfifyListBean;
import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.idcg.idcw.model.params.SetPayPassInfoReqParam;
import com.idcg.idcw.widget.NewKeyBoard;
import com.idcg.idcw.widget.NewPasswordView;
import com.idcg.idcw.widget.NewPasswordViewAgain;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.constant.BaseSignalConstants;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.otc.iprovider.OTCGetSignalrUrlServices;


/**
 * Created by hpz on 2018/3/31.
 */

@Route(path = ArouterConstants.SETWALLETPIN, name = "设置用户钱包pin页面")
public class SetWalletPinActivity extends BaseWalletActivity implements NewPasswordView.PasswordListener, NewPasswordViewAgain.PasswordListener1 {
    @BindView(R.id.passwordView)
    NewPasswordView passwordView;
    @BindView(R.id.btn_set_pass_sure)
    TextView btnSetPassSure;
    @BindView(R.id.passwordView1)
    NewPasswordViewAgain passwordView1;
    @BindView(R.id.btn_set_pass_sure_two)
    TextView btnSetPassSureTwo;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.pin_keyboard)
    NewKeyBoard pinKeyboard;
    @BindView(R.id.pin_keyboard1)
    NewKeyBoard pinKeyboard1;
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;

    private String payPin;
    private String payTwoPin;
    private String phraseLaterTag;
    private String recoveryTag;
    private List<PhraseDataReqAndResParam.RandomWordBean> phraseAllList = new ArrayList<>();
    private List<PhraseDataReqAndResParam.VerinfyWordBean> verifyList = new ArrayList<>();
    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "ok"
    };

    @Autowired
    ReqRegisterIproviderServices newRegisterIproviderServices;

    @Autowired
    SetPayPassInfoProviderServices mSetPayPassServices;

    @Autowired
    OTCGetSignalrUrlServices otcGetSignalrUrlServices;

    private String userName;
    private String device_id;
    private LoginStatus loginStatus;
    private String phraseNew;
    private String finish;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_must_set_pay;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        getPhraseBundleData();
        getLaterTag();
        getRecoverySuccess();
        getFinishTag();

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("phraseNewPin");
        if (bundle != null) {
            phraseNew = bundle.getString("phraseNew");
        }
        loginStatus = (LoginStatus) ACacheUtil.get(this).getAsObject(AcacheKeys.loginbean);
        if (!TextUtils.isEmpty(phraseNew) && phraseNew.equals(phraseNew)) {
            mrBackLayout.setVisibility(View.VISIBLE);
        } else if (loginStatus != null && !TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.getPosMain() == 6 && ACacheUtil.get(SetWalletPinActivity.this).getAsString("namePhrase") != null
                && ACacheUtil.get(SetWalletPinActivity.this).getAsString("namePhrase").equals("namePhrase")) {
            mrBackLayout.setVisibility(View.GONE);
        }
        setSubView();
        setSubView1();
        pinKeyboard.Show();
        passwordView.setPasswordListener(this);
        passwordView.setKeyBoard(pinKeyboard);
        passwordView1.setKeyBoard(pinKeyboard1);
        passwordView1.setPasswordListener1(this);
        btnSetPassSure.setEnabled(false);
        btnSetPassSureTwo.setEnabled(false);

        pinKeyboard.setOnClickKeyboardListener((position, value1) -> {
            if (position < 11 && position != 9) {
                passwordView.addData(value1);
            } else if (position == 9) {

            } else if (position == 11) {
                //点击完成 隐藏输入键盘
                passwordView.delData(value1);
                btnSetPassSure.setEnabled(false);
                btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
            }
        });

        pinKeyboard1.setOnClickKeyboardListener((position, value1) -> {
            if (position < 11 && position != 9) {
                passwordView1.addData(value1);
            } else if (position == 9) {

            } else if (position == 11) {
                //点击完成 隐藏输入键盘
                passwordView1.delData(value1);
                btnSetPassSureTwo.setEnabled(false);
                btnSetPassSureTwo.setBackgroundResource(R.drawable.item_gray_black);
                btnSetPassSureTwo.setTextColor(getResources().getColor(R.color.gray_90));
            }
        });

        if (loginStatus != null && loginStatus.getPosMain() == 2) {
            mrBackLayout.setVisibility(View.GONE);
        } else if (loginStatus != null && loginStatus.getPosMain() == 3 && ACacheUtil.get(SetWalletPinActivity.this).getAsString("old").equals("old")) {
            mrBackLayout.setVisibility(View.GONE);
        }

        getSignalrUrl();
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

    private void getFinishTag() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("backFinish");
        if (bundle != null) {
            finish = bundle.getString("finish");//说明是从恢复资产页面进来的
        }
    }

    private void getRecoverySuccess() {
        LoginStatus loginStatus = (LoginStatus) ACacheUtil.get(this).getAsObject(AcacheKeys.loginbean);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("recoverySuccess");
        if (bundle != null) {
            recoveryTag = bundle.getString("recovery");//说明是从恢复资产页面进来的
            if (recoveryTag.equals("recovery") || loginStatus.getPosMain() == 2) {
                mrBackLayout.setVisibility(View.GONE);
            }
        }
    }

    private void getPhraseBundleData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("phrase");
        if (bundle != null) {
            PhraseListBean bean = (PhraseListBean) bundle.getSerializable("phraseAllList");
            phraseAllList.addAll(bean.getPhrase());
        }

        if (bundle != null) {
            VerfifyListBean beanVerify = (VerfifyListBean) bundle.getSerializable("phraseVerifyList");
            verifyList.addAll(beanVerify.getPhrase());
        }
    }

    private void getLaterTag() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("laterSay");
        if (bundle != null) {
            phraseLaterTag = bundle.getString("later");
        }
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void passwordChange(String changeText) {
    }

    @Override
    public void passwordComplete() {
        btnSetPassSure.setEnabled(true);
        btnSetPassSure.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
        btnSetPassSure.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {

    }

    @Override
    public void getPass(String pass, boolean isCom) {
        payPin = pass;
    }

    @Override
    public void passwordChange1(String changeText) {
    }

    @Override
    public void passwordComplete1() {
        btnSetPassSureTwo.setEnabled(true);
        btnSetPassSureTwo.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
        btnSetPassSureTwo.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void keyEnterPress1(String password, boolean isComplete) {

    }

    @Override
    public void getPass1(String pass, boolean isCom) {
        payTwoPin = pass;
    }

    @OnClick({R.id.btn_set_pass_sure, R.id.btn_set_pass_sure_two, R.id.mr_back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                if (!TextUtils.isEmpty(finish) && finish.equals("finish")) {
                    EventBus.getDefault().post(new EventBean("finish"));
                }
                this.finish();
//                passwordView.setVisibility(View.VISIBLE);
//                passwordView1.setVisibility(View.GONE);
//                btnSetPassSure.setVisibility(View.VISIBLE);
//                btnSetPassSureTwo.setVisibility(View.GONE);
//                tvStart.setText(R.string.set_pay_pass);
//                passwordView.clearText();
//                btnSetPassSure.setEnabled(false);
//                btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
//                btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
//                pinKeyboard1.setVisibility(View.GONE);
//                mrBackLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_set_pass_sure:
                //mrBackLayout.setVisibility(View.VISIBLE);
                passwordView.setVisibility(View.GONE);
                passwordView1.setVisibility(View.VISIBLE);
                btnSetPassSure.setVisibility(View.GONE);
                btnSetPassSureTwo.setVisibility(View.VISIBLE);
                tvStart.setText(R.string.tv_sure_pin);
                btnSetPassSureTwo.setEnabled(false);
                btnSetPassSureTwo.setBackgroundResource(R.drawable.item_gray_black);
                btnSetPassSureTwo.setTextColor(getResources().getColor(R.color.gray_90));
                passwordView1.clearText();
                pinKeyboard1.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_set_pass_sure_two:
                LoginStatus loginStatus = (LoginStatus) ACacheUtil.get(this).getAsObject(AcacheKeys.loginbean);
                if (!payPin.equals(payTwoPin)) {
                    ToastUtil.show(getString(R.string.pay_not_match));
                    passwordView.setVisibility(View.VISIBLE);
                    passwordView1.setVisibility(View.GONE);
                    btnSetPassSure.setVisibility(View.VISIBLE);
                    btnSetPassSureTwo.setVisibility(View.GONE);
                    tvStart.setText(R.string.set_pay_pass);
                    Animation shake = AnimationUtils.loadAnimation(mCtx, R.anim.shake);
                    passwordView.startAnimation(shake);
                    passwordView.clearText();
                    btnSetPassSure.setEnabled(false);
                    btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                    btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
                    pinKeyboard1.setVisibility(View.GONE);
                    //mrBackLayout.setVisibility(View.GONE);
                    return;
                }

                if (!TextUtils.isEmpty(recoveryTag) && recoveryTag.equals("recovery") || loginStatus.getPosMain() == 2) {
                    showDialog();
                    RequestSetPayPin();
                    return;
                }

                if (!TextUtils.isEmpty(phraseLaterTag) && phraseLaterTag.equals("later")) {
                    //ToastUtil.show("直接下次再说进来的");
                    showDialog();
                    RequestSetPayPin();
                    return;
                }
                //请求新版本的注册接口
                showDialog();
                RequestSetPayPin();
                break;
        }
    }

    private void RequestSetPayPin() {
        LoginStatus loginStatus = (LoginStatus) ACacheUtil.get(this).getAsObject(AcacheKeys.loginbean);
        if (loginStatus != null) {
            userName = loginStatus.getUser_name();
            device_id = loginStatus.getDevice_id();
        }
        SetPayPassInfoReqParam reqParam = new SetPayPassInfoReqParam();
        reqParam.setOriginalPayPwd("");
        reqParam.setNewPayPwd(payPin);
        reqParam.setAffirmPayPwd(payTwoPin);
        reqParam.setType("0");
        reqParam.setVerifyCode("");
        reqParam.setVerifyUser(userName);
        reqParam.setDevice_id(device_id);
        reqParam.setNewVersion(true);
        addSubscribe(mSetPayPassServices.requestSetPassProvider(reqParam).subscribeWith(new RxSubscriber<String>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                dismissDialog();
                if (ex.getErrorCode().equals("108")) {
                    ToastUtil.show(getString(R.string.pay_not_match));
                    finish();
                    navigation(ArouterConstants.SETWALLETPIN);
                } else {
                    ToastUtil.show(getString(R.string.server_connection_error));
                }
            }

            @Override
            public void onSuccess(String s) {
                dismissDialog();
                ACacheUtil.get(mCtx).put(AcacheKeys.finger, payTwoPin);
                EventBus.getDefault().post(new PositionBean(6));
                AppManager.getInstance().finishAllActivity();
                //intentFActivity(MainActivity.class);
                Intent intent = new Intent(SetWalletPinActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("language", "language");
                intent.putExtra("Language", bundle);
                startActivity(intent);
            }
        }));
    }

//    private void RequestNewVersionRegister(String language, String url, String walletName, PhraseDataReqAndResParam reqAndResParam) {
//        addSubscribe(newRegisterIproviderServices.requestRegisterProvider(new CreateSetPassReqParam(walletName,
//                "",
//                "",
//                payPin,
//                payTwoPin,
//                "",
//                "",
//                "2",
//                language,
//                url,
//                "",
//                true,
//                "",
//                reqAndResParam
//        )).subscribeWith(new RxProgressSubscriber<LoginStatus>(this) {
//            @Override
//            protected void onError(ResponseThrowable ex) {
//
//                if (ex.getErrorCode().equals("0")) {
//                    ToastUtil.show(getString(R.string.pass_not_match));
//                } else if (ex.getErrorCode().equals("106")) {
//                    ToastUtil.show(getString(R.string.username_exist));
//                } else if (ex.getErrorCode().equals("102")) {
//
//                    ToastUtil.show(getString(R.string.code_error));
//                } else {
//                    ToastUtil.show(getString(R.string.server_connection_error));
//                }
//            }
//
//            @Override
//            public void onSuccess(LoginStatus loginStatus) {
//                ToastUtil.show(getString(R.string.create_wallet_success));
//            }
//        }));
//    }

    private void setSubView() {
        //设置键盘
        pinKeyboard.setKeyboardKeys(KEY);
    }

    private void setSubView1() {
        //设置键盘
        pinKeyboard1.setKeyboardKeys(KEY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
