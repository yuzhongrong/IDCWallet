package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.CheckOriginalPwdProviderServices;
import com.idcg.idcw.model.bean.CheckNewPinBean;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.model.logic.EditPayPassLogic;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.model.params.ReqPayPassParam;
import com.idcg.idcw.presenter.impl.EditPayPassPresenterImpl;
import com.idcg.idcw.presenter.interfaces.EditPayPassContract;
import com.idcg.idcw.widget.NewKeyBoard;
import com.idcg.idcw.widget.NewPasswordView;
import com.idcg.idcw.widget.NewPasswordViewAgain;
import com.idcg.idcw.widget.NewPasswordViewThird;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 15:15
 **/

@Route(path = ArouterConstants.EDIT_PAY_PASS, name = "修改pin")
public class EditPayPassActivity extends BaseWalletActivity<EditPayPassLogic, EditPayPassPresenterImpl>
        implements NewPasswordView.PasswordListener, NewPasswordViewAgain.PasswordListener1,
        NewPasswordViewThird.PasswordListener2, EditPayPassContract.View {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.tv_set_pass)
    TextView tvSetPass;
    @BindView(R.id.passwordView)
    NewPasswordView passwordView;
    @BindView(R.id.tv_forget_pay_pass)
    TextView tvForgetPayPass;
    @BindView(R.id.btn_set_pass_sure)
    TextView btnSetPassSure;
    @BindView(R.id.ll_set_pass_one)
    LinearLayout llSetPassOne;
    @BindView(R.id.passwordView_again)
    NewPasswordViewAgain passwordViewAgain;
    @BindView(R.id.btn_set_pass_sure_again)
    TextView btnSetPassSureAgain;
    @BindView(R.id.ll_set_pass_two)
    LinearLayout llSetPassTwo;
    @BindView(R.id.passwordView_third)
    NewPasswordViewThird passwordViewThird;
    @BindView(R.id.btn_set_pass_sure_third)
    TextView btnSetPassSureThird;
    @BindView(R.id.ll_set_pass_three)
    LinearLayout llSetPassThree;
    @BindView(R.id.view_one)
    View viewOne;
    @BindView(R.id.view_two)
    View viewTwo;
    @BindView(R.id.view_three)
    View viewThree;
    @BindView(R.id.pin_keyboard)
    NewKeyBoard pinKeyboard;
    @BindView(R.id.pin_keyboard1)
    NewKeyBoard pinKeyboard1;
    @BindView(R.id.pin_keyboard2)
    NewKeyBoard pinKeyboard2;
    private String value;
    private String passOne;
    private String passTwo;
    private String passThree;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "<<"
    };
    private int tag;

    @Autowired
    CheckOriginalPwdProviderServices mCheckOriginServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_pay_pass;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        tag = 0;
        try {
            setSubView();
            setSubView1();
            setSubView2();
            tvSetName.setText(R.string.modify_pay_pass);
            passwordView.setPasswordListener(this);
            passwordViewAgain.setPasswordListener1(this);
            passwordViewThird.setPasswordListener2(this);
            passwordView.setKeyBoard(pinKeyboard);
            passwordViewAgain.setKeyBoard(pinKeyboard1);
            passwordViewThird.setKeyBoard(pinKeyboard2);
            pinKeyboard.Show();
            btnSetPassSure.setEnabled(false);
            btnSetPassSureAgain.setEnabled(false);
            btnSetPassSureThird.setEnabled(false);

            pinKeyboard.setOnClickKeyboardListener(new NewKeyBoard.OnClickKeyboardListener() {
                @Override
                public void onKeyClick(int position, String value) {
                    if (position < 11 && position != 9) {
                        passwordView.addData(value);
                    } else if (position == 9) {

                    } else if (position == 11) {
                        //点击完成 隐藏输入键盘
                        passwordView.delData(value);
                        btnSetPassSure.setEnabled(false);
                        btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                        btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
                    }
                }
            });

            pinKeyboard1.setOnClickKeyboardListener(new NewKeyBoard.OnClickKeyboardListener() {
                @Override
                public void onKeyClick(int position, String value) {
                    if (position < 11 && position != 9) {
                        passwordViewAgain.addData(value);
                    } else if (position == 9) {

                    } else if (position == 11) {
                        //点击完成 隐藏输入键盘
                        passwordViewAgain.delData(value);
                        btnSetPassSureAgain.setEnabled(false);
                        btnSetPassSureAgain.setBackgroundResource(R.drawable.item_gray_black);
                        btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.gray_90));
                    }
                }
            });

            pinKeyboard2.setOnClickKeyboardListener(new NewKeyBoard.OnClickKeyboardListener() {
                @Override
                public void onKeyClick(int position, String value) {
                    if (position < 11 && position != 9) {
                        passwordViewThird.addData(value);
                    } else if (position == 9) {

                    } else if (position == 11) {
                        //点击完成 隐藏输入键盘
                        passwordViewThird.delData(value);
                        btnSetPassSureThird.setEnabled(false);
                        btnSetPassSureThird.setBackgroundResource(R.drawable.item_gray_black);
                        btnSetPassSureThird.setTextColor(getResources().getColor(R.color.gray_90));
                    }
                }
            });
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @OnClick({R.id.mr_back_layout, R.id.tv_forget_pay_pass, R.id.btn_set_pass_sure, R.id.btn_set_pass_sure_again, R.id.btn_set_pass_sure_third})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                if (tag == 1) {
                    llSetPassOne.setVisibility(View.VISIBLE);
                    llSetPassTwo.setVisibility(View.GONE);
                    llSetPassThree.setVisibility(View.GONE);
                    viewTwo.setVisibility(View.INVISIBLE);
                    btnSetPassSure.setEnabled(false);
                    btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                    btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
                    tvSetName.setText(R.string.modify_pay_pass);
                    passwordView.clearText();
                    pinKeyboard1.setVisibility(View.GONE);
                    tag = 0;
                } else if (tag == 0) {
                    this.finish();
                } else if (tag == 2) {
                    llSetPassOne.setVisibility(View.GONE);
                    llSetPassTwo.setVisibility(View.VISIBLE);
                    llSetPassThree.setVisibility(View.GONE);
                    viewThree.setVisibility(View.INVISIBLE);
                    btnSetPassSureAgain.setEnabled(false);
                    btnSetPassSureAgain.setBackgroundResource(R.drawable.item_gray_black);
                    btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.gray_90));
                    tvSetName.setText(R.string.set_pay_pass);
                    passwordViewAgain.clearText();
                    pinKeyboard2.setVisibility(View.GONE);
                    tag = 1;
                }
                break;
            case R.id.tv_forget_pay_pass:
                navigation(ArouterConstants.FORGET_PASS);
                break;
            case R.id.btn_set_pass_sure:
                showDialog();
                LoginStatus bean1 = LoginUtils.getLoginBean(mCtx);
                CheckOriginalPwdReqParam reqParam = new CheckOriginalPwdReqParam("1",passOne,bean1.getDevice_id(),true);

                mCheckOriginServices.requestOldCheckOriginalPwdProvider(reqParam)
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeWith(new RxSubscriber<Boolean>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        if (ex.getErrorCode().equals("121")) {
                            ToastUtil.show(getString(R.string.error_again_error));
                            Animation shake = AnimationUtils.loadAnimation(mCtx, R.anim.shake);
                            passwordView.startAnimation(shake);
                            passwordView.clearText();
                            btnSetPassSure.setEnabled(false);
                            btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                            btnSetPassSure.setTextColor(getResources().getColor(R.color.gray_90));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(Boolean checkNewPinBean) {
                        dismissDialog();
                        tag = 1;
                        llSetPassOne.setVisibility(View.GONE);
                        llSetPassTwo.setVisibility(View.VISIBLE);
                        llSetPassThree.setVisibility(View.GONE);
                        viewTwo.setVisibility(View.VISIBLE);
                        tvSetName.setText(R.string.set_pay_pass);
                        pinKeyboard1.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.btn_set_pass_sure_again:
                tag = 2;
                passwordView.onWindowFocusChanged(false);
                llSetPassOne.setVisibility(View.GONE);
                llSetPassTwo.setVisibility(View.GONE);
                llSetPassThree.setVisibility(View.VISIBLE);
                viewThree.setVisibility(View.VISIBLE);
                tvSetName.setText(R.string.set_pay_pass);
                pinKeyboard2.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_set_pass_sure_third:
                if (TextUtils.isEmpty(passOne) || TextUtils.isEmpty(passTwo) || TextUtils.isEmpty(passThree)) {
                    ToastUtil.show(getString(R.string.pass_not_empty));
                    return;
                }
                LoginStatus bean = LoginUtils.getLoginBean(mCtx);
                if (bean != null) {
                    showDialog();
                    ReqPayPassParam reqPayPassParam = new ReqPayPassParam();
                    reqPayPassParam.setOriginalPayPwd(passOne);
                    reqPayPassParam.setNewPayPwd(passTwo);
                    reqPayPassParam.setAffirmPayPwd(passThree);
                    reqPayPassParam.setType(String.valueOf(0));
                    reqPayPassParam.setVerifyCode("");
                    reqPayPassParam.setNewVersion(true);
                    reqPayPassParam.setDevice_id(bean.getDevice_id());
                    reqPayPassParam.setVerifyUser(bean.getPosMain() == 0 ? bean.getTelphone() : bean.getEmail());
                    mPresenter.EditPayPass(reqPayPassParam);
                }
                break;
        }
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
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
        passOne = pass;

    }

    @Override
    public void passwordChange1(String changeText) {
    }

    @Override
    public void passwordComplete1() {
        btnSetPassSureAgain.setEnabled(true);
        btnSetPassSureAgain.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
        btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void keyEnterPress1(String password, boolean isComplete) {

    }

    @Override
    public void getPass1(String pass, boolean isCom) {
        passTwo = pass;
    }

    @Override
    public void passwordChange2(String changeText) {
    }

    @Override
    public void passwordComplete2() {
        btnSetPassSureThird.setEnabled(true);
        btnSetPassSureThird.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
        btnSetPassSureThird.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void keyEnterPress2(String password, boolean isComplete) {

    }

    @Override
    public void getPass2(String pass, boolean isCom) {
        passThree = pass;
    }

    @Subscriber
    public void onPostionInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 10) {
                EditPayPassActivity.this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setSubView() {
        //设置键盘
        pinKeyboard.setKeyboardKeys(KEY);
    }

    private void setSubView1() {
        //设置键盘
        pinKeyboard1.setKeyboardKeys(KEY);
    }

    private void setSubView2() {
        //设置键盘
        pinKeyboard2.setKeyboardKeys(KEY);
    }

    @Subscriber
    public void oneBackFinishInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 1023) {
                EditPayPassActivity.this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        switch (throwable.getErrorCode()) {
            case "108": {
                ToastUtil.show(getString(R.string.pay_not_match));
                llSetPassOne.setVisibility(View.GONE);
                llSetPassTwo.setVisibility(View.VISIBLE);
                llSetPassThree.setVisibility(View.GONE);
                viewThree.setVisibility(View.INVISIBLE);
                btnSetPassSureAgain.setEnabled(false);
                btnSetPassSureAgain.setBackgroundResource(R.drawable.item_gray_black);
                btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.gray_90));
                btnSetPassSureThird.setEnabled(false);
                btnSetPassSureThird.setBackgroundResource(R.drawable.item_gray_black);
                btnSetPassSureThird.setTextColor(getResources().getColor(R.color.gray_90));
                tvSetName.setText(R.string.set_pay_pass);
                Animation shake = AnimationUtils.loadAnimation(mCtx, R.anim.shake);
                passwordViewAgain.startAnimation(shake);
                passwordViewAgain.clearText();
                passwordViewThird.clearText();
                pinKeyboard2.setVisibility(View.GONE);
                tag = 1;
                break;
            }
            case "121": {
                ToastUtil.show(getString(R.string.error_again_error));
                finish();
                navigation(ArouterConstants.EDIT_PAY_PASS);
                break;
            }
            case "114": {
                ToastUtil.show(getString(R.string.new_pay_not_same_or));
                llSetPassOne.setVisibility(View.GONE);
                llSetPassTwo.setVisibility(View.VISIBLE);
                llSetPassThree.setVisibility(View.GONE);
                viewThree.setVisibility(View.INVISIBLE);
                btnSetPassSureAgain.setEnabled(false);
                btnSetPassSureAgain.setBackgroundResource(R.drawable.item_gray_black);
                btnSetPassSureAgain.setTextColor(getResources().getColor(R.color.gray_90));
                btnSetPassSureThird.setEnabled(false);
                btnSetPassSureThird.setBackgroundResource(R.drawable.item_gray_black);
                btnSetPassSureThird.setTextColor(getResources().getColor(R.color.gray_90));
                tvSetName.setText(R.string.set_pay_pass);
                Animation shake = AnimationUtils.loadAnimation(mCtx, R.anim.shake);
                passwordViewAgain.startAnimation(shake);
                passwordViewAgain.clearText();
                passwordViewThird.clearText();
                pinKeyboard2.setVisibility(View.GONE);
                tag = 1;
                break;
            }
            default: {
                ToastUtil.show(getString(R.string.server_connection_error));
            }
        }
    }

    @Override
    public void updateEditPayPass(Object result) {
        dismissDialog();
        navigation(ArouterConstants.EDIT_PAY_PASS_SUCCESS);
    }

    @Override
    public void updateRequestCheckOriginalPwd(CheckNewPinBean checkNewPinBean) {

    }

    @Override
    public void updateRequestCheckOriginalPwd(Boolean isOk) {

    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
