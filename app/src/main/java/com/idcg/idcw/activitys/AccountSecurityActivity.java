package com.idcg.idcw.activitys;

import android.app.Dialog;
import android.os.Bundle;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.iprovider.ExitAppProviderServices;
import com.idcg.idcw.iprovider.SettingStateProviderServices;
import com.idcg.idcw.model.bean.CheckStatusBean;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.PosInfo;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/27 12:01
 **/
@Route(path = ArouterConstants.ACCOUNT_AND_SECURITY, name = "账户与安全")
public class AccountSecurityActivity extends BaseWalletActivity implements View.OnClickListener {


    @BindView(R.id.tv_set_Name)
    TextView mTvTitle; // 界面标题

    @BindView(R.id.id_tv_ac_account_security_name)
    TextView mTvWalletName; // 钱包名称

    @BindView(R.id.id_tv_ac_account_security_phone)
    TextView mTvPhoneNumber; // 电话号码

    @BindView(R.id.id_tv_ac_account_security_email)
    TextView mTvEmail; // 邮箱

    @BindView(R.id.id_tv_ac_account_security_short_words)
    TextView mTvCopyWords; // 备份短语
    @BindView(R.id.tv_if_set_pin)
    TextView tvIfSetPin;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    private Dialog mExitDialog = null;
    private Dialog mBackDialog = null;

    @Autowired
    ExitAppProviderServices mLogOutServices;

    @Autowired
    SettingStateProviderServices mSettingStateServices;

    // 本地保存的登录参数
    private LoginStatus mLoginStatusBean = null;
    private boolean mIsBindPhone = false;
    private boolean mIsBindEmail = false;
    private boolean mIsBindPay = false;
    private boolean isPhrase;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_security;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mLoginStatusBean = LoginUtils.getLoginBean(mCtx);
        mTvTitle.setText(getString(R.string.account_and_security));
        mTvWalletName.setText(mLoginStatusBean.getUser_name());
        getDefaultSettingState();
        smartrefreshlayout.setEnableRefresh(false);
        smartrefreshlayout.setEnableLoadmore(false);
    }

    private void getDefaultSettingState() {
        showDialog();
        //3.29发现bug，修复
        mSettingStateServices.requestCheckStatusProvider(mLoginStatusBean.getDevice_id(), true)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxSubscriber<CheckStatusBean>() {
            @Override
            public void onSuccess(CheckStatusBean checkStatusBean) {
                dismissDialog();
                setUI(checkStatusBean);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                dismissDialog();
            }
        });
    }

    /**
     * 设置界面参数
     *
     * @param checkStatusBean
     */
    private void setUI(CheckStatusBean checkStatusBean) {
        if (checkStatusBean != null) {
            // 手机号
            CheckStatusBean.MobileValidBean mobileBean = checkStatusBean.getMobile_valid();
            if (mobileBean != null && mobileBean.isValid()) {
                mIsBindPhone = true;
                mTvPhoneNumber.setText(mobileBean.getMobile());
                mTvPhoneNumber.setTextColor(getResources().getColor(R.color.tip_gray_black));
            } else {
                mIsBindPhone = false;
                mTvPhoneNumber.setText(getString(R.string.not_bind));
                mTvPhoneNumber.setTextColor(getResources().getColor(R.color.tip_gray));

            }
            // 邮箱
            CheckStatusBean.EmailValidBean emailBean = checkStatusBean.getEmail_valid();
            if (emailBean != null && emailBean.isValid()) {
                mIsBindEmail = true;
                mTvEmail.setText(emailBean.getEmail());
                mTvEmail.setTextColor(getResources().getColor(R.color.tip_gray_black));
            } else {
                mIsBindEmail = false;
                mTvEmail.setText(getString(R.string.not_bind));
                mTvEmail.setTextColor(getResources().getColor(R.color.tip_gray));
            }
            // 备份短语
            int copyId;
            if (checkStatusBean.isWallet_phrase()) {
                isPhrase = checkStatusBean.isWallet_phrase();
                copyId = R.string.backed_up;
                mTvCopyWords.setTextColor(getResources().getColor(R.color.tip_gray_black));
            } else {
                copyId = R.string.not_backed_up;
                mTvCopyWords.setTextColor(getResources().getColor(R.color.tip_gray));
            }
            mTvCopyWords.setText(copyId);

            CheckStatusBean.PayPasswordBean payBean = checkStatusBean.getPayPassword();
            if (payBean != null && payBean.isValid()) {
                mIsBindPay = true;
                tvIfSetPin.setText(getString(R.string.already_set));
            } else {
                mIsBindPay = false;
                tvIfSetPin.setText(getString(R.string.not_set_pin));
            }
        }
    }

    @Override
    protected void onEvent() {

    }

    @OnClick({
            R.id.mr_back_layout,
            R.id.id_rl_ac_account_security_name,
            R.id.id_rl_ac_account_security_phone,
            R.id.id_rl_ac_account_security_email,
            R.id.id_rl_ac_account_security_copy,
            R.id.id_rl_ac_account_security_pin,
            R.id.id_rl_ac_account_security_password,
            R.id.id_tv_ac_account_security_logout
    })
    public void onAccountSecurityClick(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                finish();
                break;
            case R.id.id_rl_ac_account_security_name:
                //navigation(ArouterConstants.SET_ACCOUNT_NAME);
                break;
            case R.id.id_rl_ac_account_security_phone:
                ARouter.getInstance().build(ArouterConstants.ACTIVITY_PHONE)
                        .withBoolean("bind_or_change", mIsBindPhone).navigation();
                break;
            case R.id.id_rl_ac_account_security_email:
                ARouter.getInstance().build(ArouterConstants.ACTIVITY_EMAIL)
                        .withBoolean("bind_or_change", mIsBindEmail).navigation();
                break;
            case R.id.id_rl_ac_account_security_copy:
                //navigation(ArouterConstants.BACK_AGAIN_PHRASE);
                boolean mIsPhrase = true;
                ARouter.getInstance().build(ArouterConstants.REMPHRASE)
                        .withBoolean("mIsPhrase", mIsPhrase).navigation();
                break;
            case R.id.id_rl_ac_account_security_pin:
                ARouter.getInstance().build(ArouterConstants.SET_PAY_PASS)
                        .withBoolean("mIsBindPay", mIsBindPay).navigation();
                break;
            case R.id.id_rl_ac_account_security_password:
                navigation(ArouterConstants.MODIFY_PAY_PASS);
                break;
            case R.id.id_tv_ac_account_security_logout:
//                if (isPhrase) {
//                    showExitDialog();
//                } else {
//                    showHintBackDialog();
//                }
                if (ACacheUtil.get(AccountSecurityActivity.this).getAsString("old") != null && ACacheUtil.get(AccountSecurityActivity.this).getAsString("old").equals("old") ||
                        ACacheUtil.get(AccountSecurityActivity.this).getAsString("find") != null && ACacheUtil.get(AccountSecurityActivity.this).getAsString("find").equals("find") ||
                        ACacheUtil.get(AccountSecurityActivity.this).getAsString("namePhrase") != null && ACacheUtil.get(AccountSecurityActivity.this).getAsString("namePhrase").equals("namePhrase") ||
                        ACacheUtil.get(AccountSecurityActivity.this).getAsString("confirm") != null && ACacheUtil.get(AccountSecurityActivity.this).getAsString("confirm").equals("confirm")) {
                    showExitDialog();//confirm
                } else {
                    showHintBackDialog();
                }
                break;
            default:
                break;
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

    private void showExitDialog() {
        try {
            if (mExitDialog == null) {
                mExitDialog = new Dialog(mCtx, R.style.shuweiDialog);
                View exitContentView = LayoutInflater.from(mCtx).inflate(R.layout.dialog_exit_hint_phrase, null);
                mExitDialog.setContentView(exitContentView);
                mExitDialog.setCancelable(false);
                Window dialogWindow = mExitDialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setAttributes(lp);
                exitContentView.findViewById(R.id.out_sure).setOnClickListener(this);
                exitContentView.findViewById(R.id.out_cancel).setOnClickListener(this);
            }
            if (mExitDialog != null) {
                mExitDialog.show();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.out_sure:
                if (mExitDialog != null && mExitDialog.isShowing()) mExitDialog.dismiss();
                LoginStatus loginStatus = LoginUtils.getLoginBean(mCtx);
                ACacheUtil.get(mCtx).remove(loginStatus.getDevice_id() + AcacheKeys.SAVEISVAILDPIN);
                ACacheUtil.get(mCtx).remove(loginStatus.getId() + AcacheKeys.cache1);
                ACacheUtil.get(mCtx).remove(AcacheKeys.loginbean);
                ACacheUtil.get(mCtx).remove("old");
                ACacheUtil.get(mCtx).remove("namePhrase");
                ACacheUtil.get(mCtx).remove("find");
                ACacheUtil.get(mCtx).remove("confirm");
                ACacheUtil.get(mCtx).remove("pinPass");
                AppManager.getInstance().finishAllActivity();
                intentFActivity(MainGuideActivity.class);
//                addSubscribe(mLogOutServices.requestLoginOutProvider().subscribeWith(new RxProgressSubscriber<String>(this) {
//                    @Override
//                    public void onSuccess(String s) {
//                        LoginStatus loginStatus = LoginUtils.getLoginBean(mCtx);
//                        ACacheUtil.get(mCtx).remove(loginStatus.getId() + AcacheKeys.cache1);
//                        ACacheUtil.get(mCtx).remove(AcacheKeys.loginbean);
//                        AppManager.getInstance().finishAllActivity();
//                        navigation(ArouterConstants.MAINGUIDE);
//                    }
//
//                    @Override
//                    protected void onError(ResponseThrowable ex) {
//                        dismissDialog();
//                        ToastUtil.show(getString(R.string.server_connection_error));
//                    }
//                }));
                break;
            case R.id.out_cancel:
                if (mExitDialog != null && mExitDialog.isShowing()) mExitDialog.dismiss();
                break;
            case R.id.btn_back_close:
                if (mBackDialog != null && mBackDialog.isShowing()) mBackDialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Subscriber
    public void onRefreshStatusInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 603) {
                getDefaultSettingState();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
