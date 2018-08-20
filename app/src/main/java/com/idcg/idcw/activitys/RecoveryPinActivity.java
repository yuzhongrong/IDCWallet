package com.idcg.idcw.activitys;

import android.os.Bundle;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.app.WalletApplication;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.SetPayPassInfoProviderServices;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.model.bean.PositionBean;
import com.idcg.idcw.model.params.SetPayPassInfoReqParam;
import com.idcg.idcw.widget.Keyboard;
import com.idcg.idcw.widget.PasswordView;
import com.idcg.idcw.widget.PasswordViewAgain;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 15:39
 **/
@Route(path = ArouterConstants.RECOVERY_PIN,name = "重置pin")
public class RecoveryPinActivity extends BaseWalletActivity implements PasswordView.PasswordListener, PasswordViewAgain.PasswordListener1 {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.passwordView)
    PasswordView passwordView;
    @BindView(R.id.btn_set_pass_sure)
    Button btnSetPassSure;
    @BindView(R.id.ll_set_pass_one)
    LinearLayout llSetPassOne;
    @BindView(R.id.passwordView_again)
    PasswordViewAgain passwordViewAgain;
    @BindView(R.id.btn_set_pass_sure_again)
    Button btnSetPassSureAgain;
    @BindView(R.id.ll_set_pass_two)
    LinearLayout llSetPassTwo;
    @BindView(R.id.view_two)
    View viewTwo;
    @BindView(R.id.pin_keyboard)
    Keyboard pinKeyboard;
    @BindView(R.id.pin_keyboard1)
    Keyboard pinKeyboard1;
    private String passs;
    private String passss;
    private String value;
    private int tag;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "<<", "0", WalletApplication.getInstance().getString(R.string.finish)
    };

    @Autowired
    SetPayPassInfoProviderServices mSetPayPassServices;

    @Override
    protected int getLayoutId() {
        return  R.layout.activity_recovery_pin;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        tag = 0;
        setSubView();
        setSubView1();
        tvSetName.setText(getString(R.string.tv_find_pay_pass));
        passwordView.setPasswordListener(this);
        passwordViewAgain.setPasswordListener1(this);
        passwordView.setKeyBoard(pinKeyboard);
        passwordViewAgain.setKeyBoard(pinKeyboard1);
        pinKeyboard.Show();
        btnSetPassSure.setEnabled(false);
        btnSetPassSureAgain.setEnabled(false);

        pinKeyboard.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    passwordView.addData(value);
                } else if (position == 9) {
                    passwordView.delData(value);
                    btnSetPassSure.setEnabled(false);
                    btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                    btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
                } else if (position == 11) {
                    //点击完成 隐藏输入键盘
                    pinKeyboard.hide();
                }
            }
        });

        pinKeyboard1.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    passwordViewAgain.addData(value);
                } else if (position == 9) {
                    passwordViewAgain.delData(value);
                    btnSetPassSureAgain.setEnabled(false);
                    btnSetPassSureAgain.setBackgroundResource(R.drawable.item_gray_black);
                    btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.gray_90));
                } else if (position == 11) {
                    //点击完成 隐藏输入键盘
                    pinKeyboard1.hide();
                }
            }
        });
    }
    @OnClick({R.id.mr_back_layout, R.id.tv_set_Name, R.id.btn_set_pass_sure, R.id.btn_set_pass_sure_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                if (tag == 1) {
                    llSetPassOne.setVisibility(View.VISIBLE);
                    llSetPassTwo.setVisibility(View.GONE);
                    viewTwo.setVisibility(View.INVISIBLE);
                    btnSetPassSure.setEnabled(false);
                    btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                    btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
                    btnSetPassSureAgain.setEnabled(false);
                    btnSetPassSureAgain.setBackgroundResource(R.drawable.item_gray_black);
                    btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.gray_90));
                    tvSetName.setText(R.string.tv_find_pay_pass);
                    passwordView.clearText();
                    passwordViewAgain.clearText();
                    pinKeyboard1.hide();
                    tag = 0;
                } else if (tag == 0) {
                    this.finish();
                }
                break;
            case R.id.tv_set_Name:
                break;
            case R.id.btn_set_pass_sure:
                llSetPassOne.setVisibility(View.GONE);
                llSetPassTwo.setVisibility(View.VISIBLE);
                viewTwo.setVisibility(View.VISIBLE);
                passwordView.onWindowFocusChanged(false);
                pinKeyboard1.Show();
                tag = 1;
                break;
            case R.id.btn_set_pass_sure_again:
                showDialog();
                SetPayPassInfoReqParam reqParam = new SetPayPassInfoReqParam();
                reqParam.setOriginalPayPwd("");
                reqParam.setNewPayPwd(passs);
                reqParam.setAffirmPayPwd(passss);
                reqParam.setType("");
                reqParam.setVerifyCode("");
                addSubscribe(mSetPayPassServices.requestSetPassProvider(reqParam).subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        if (ex.getErrorCode().equals("108")) {
                            ToastUtil.show(getString(R.string.pay_pass_not_match));
                            passwordView.clearText();
                            passwordViewAgain.clearText();
                            llSetPassOne.setVisibility(View.VISIBLE);
                            llSetPassTwo.setVisibility(View.GONE);
                            viewTwo.setVisibility(View.INVISIBLE);
                            btnSetPassSure.setEnabled(false);
                            btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                            btnSetPassSureAgain.setEnabled(false);
                            btnSetPassSureAgain.setBackgroundResource(R.drawable.item_gray_black);
                            btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
                            btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.gray_90));
                            pinKeyboard1.hide();
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        dismissDialog();
                        navigation(ArouterConstants.FIND_PIN_SUCCESS);
                        EventBus.getDefault().post(new PositionBean(6));
                        RecoveryPinActivity.this.finish();
                    }
                }));
                break;
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
//        if (changeText.equals("1")) {
//            btnSetPassSure.setEnabled(false);
//            btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
//        }
    }

    @Override
    public void passwordComplete() {
        btnSetPassSure.setEnabled(true);
        btnSetPassSure.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
        btnSetPassSure.setTextColor(getResources().getColor(R.color.white));
        //if (pinKeyboard != null) pinKeyboard.hide();
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {
    }

    @Override
    public void getPass(String pass, boolean isCom) {
        passs = pass;
    }

    @Override
    public void passwordChange1(String changeText) {
//        if (changeText.equals("1")) {
//            btnSetPassSureAgain.setEnabled(false);
//            btnSetPassSureAgain.setBackgroundResource(R.drawable.item_gray_black);
//        }
    }

    @Override
    public void passwordComplete1() {
        btnSetPassSureAgain.setEnabled(true);
        btnSetPassSureAgain.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
        btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.white));
        //if (pinKeyboard1 != null) pinKeyboard1.hide();
    }

    @Override
    public void keyEnterPress1(String password, boolean isComplete) {

    }

    @Override
    public void getPass1(String pass, boolean isCom) {
        passss = pass;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscriber
    public void onPostionInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 8) {
                RecoveryPinActivity.this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private void setSubView() {
        //设置键盘
        pinKeyboard.setKeyboardKeys(KEY);
    }

    private void setSubView1() {
        //设置键盘
        pinKeyboard1.setKeyboardKeys(KEY);
    }
}
