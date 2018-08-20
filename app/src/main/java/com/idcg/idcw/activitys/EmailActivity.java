package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import foxidcw.android.idcw.common.constant.ArouterConstants;

import com.idcg.idcw.iprovider.VerifyCodeAndEMailProviderServices;
import com.idcg.idcw.iprovider.VerifyUserNameProviderServices;

import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.idcg.idcw.model.logic.LocalCurrencyLogic;
import com.idcg.idcw.model.params.LocalCurrencyReqParam;
import com.idcg.idcw.model.params.SendEmailCodeReqParam;
import com.idcg.idcw.model.params.VerfityNameReqParam;
import com.idcg.idcw.presenter.impl.LocalCurrencyPresenterImpl;
import com.idcg.idcw.presenter.interfaces.LocalCurrencyContract;

import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.StringUtils;

import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/28 18:04
 **/
@Route(path = ArouterConstants.ACTIVITY_EMAIL, name = "绑定或者更改邮箱")
public class EmailActivity extends BaseWalletActivity<LocalCurrencyLogic, LocalCurrencyPresenterImpl> implements LocalCurrencyContract.View {

    private static final int MAX_COUNT_TIME = 60;

    @Autowired(name = "bind_or_change")
    boolean mIsBind = false;

    @BindView(R.id.tv_set_Name)
    TextView mTvTitle; // 界面标题

    @BindView(R.id.id_et_ac_email_input)
    InPutEditText mEtEmailInput;

    @BindView(R.id.id_et_ac_email_code)
    InPutEditText mEtEmailCode;

    @BindView(R.id.id_btn_ac_email_bind)
    TextView mBtnBindEmail;

    @BindView(R.id.id_tv_ac_email_get_code)
    TextView mTvGetEmailCode;

    // 获取验证码
    @Autowired
    VerifyCodeAndEMailProviderServices verifyCodeAndEMailProviderServices;

    @Autowired
    VerifyUserNameProviderServices verifyUserNameProviderServices;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_email;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        int titleId;
        int btnId;
        if (mIsBind) {
            titleId = R.string.str_modify_email;
            btnId = R.string.change;
        } else {
            titleId = R.string.str_bind_email;
            btnId = R.string.bind;
        }
        mTvTitle.setText(titleId);
        mBtnBindEmail.setText(btnId);

        mEtEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTvGetEmailCode.setTextColor(getResources().getColor(R.color.tip_gray));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtEmailInput.getText().toString().length() >= 3) {
                    if (mTvGetEmailCode.getText().toString().contains(getString(R.string.review_send))) {
                        mTvGetEmailCode.setTextColor(getResources().getColor(R.color.tip_gray));
                    } else {
                        mTvGetEmailCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({
            R.id.mr_back_layout,
            R.id.id_tv_ac_email_get_code,
            R.id.id_btn_ac_email_bind
    })
    public void emailAcClick(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                finish();
                break;
            case R.id.id_tv_ac_email_get_code:
                String inputEmail = mEtEmailInput.getText().toString().trim();
                if (TextUtils.isEmpty(inputEmail)) {
                    ToastUtil.show(getString(R.string.email_no_empty));
                    return;
                }
                if (!StringUtils.isEmail(inputEmail)) {
                    ToastUtil.show(getString(R.string.email_error));
                    return;
                }

                RquestPhoneUserName(inputEmail);
                //
                mEtEmailCode.requestFocus();
                break;
            case R.id.id_btn_ac_email_bind:
                String email = mEtEmailInput.getText().toString().trim();
                String emailCode = mEtEmailCode.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(emailCode)) {
                    ToastUtil.show(getString(R.string.email_code_no_empty));
                    return;
                }
                if (!StringUtils.isEmail(email)) {
                    ToastUtil.show(getString(R.string.email_error));
                    return;
                }
                LocalCurrencyReqParam modifyPhoneReqParam = new LocalCurrencyReqParam();
                modifyPhoneReqParam.setContent(email);
                modifyPhoneReqParam.setType(String.valueOf(1));
                modifyPhoneReqParam.setVerifyCode(emailCode);
                mPresenter.requestModifyCurrency(modifyPhoneReqParam);
                break;
            default:
                break;
        }
    }

    private void RquestPhoneUserName(String email) {
        //mPresenter.verfityName(new VerfityNameReqParam(tvAreaNum + UserPhoneAccount,"0"));
        verifyUserNameProviderServices.requestCheckUserName(new VerfityNameReqParam(email, "1"))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<Object>(EmailActivity.this) {
                    @Override
                    public void onSuccess(Object data) {
                        SendEmailCodeReqParam reqParam = new SendEmailCodeReqParam();
                        showDialog();
                        reqParam.setLanguage(AppLanguageUtils.getLanguageLocalCode(mCtx));
                        reqParam.setMailAddress(email);
                        reqParam.setVerifyCodeType(String.valueOf(5));
                        verifyCodeAndEMailProviderServices.requestEmailCode(reqParam)
                                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                                .subscribeWith(new RxProgressSubscriber<Object>(EmailActivity.this) {
                            @Override
                            protected void onError(ResponseThrowable ex) {
                                EmailActivity.this.dismissDialog();
                                mTvGetEmailCode.setEnabled(true);
                                if (ex.getErrorCode().equals(String.valueOf(0))) {
                                    ToastUtil.show(getString(R.string.send_error));//发送失败
                                } else {
                                    ToastUtil.show(getString(R.string.server_connection_error));//服务报错
                                }
                            }

                            @SuppressLint("CheckResult")
                            @Override
                            public void onSuccess(Object data) {
                                EmailActivity.this.dismissDialog();
                                ToastUtil.show(getString(R.string.send_to_email));
                                addSubscribe(Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                                        .take(MAX_COUNT_TIME)
                                        .map(aLong -> MAX_COUNT_TIME - (aLong + 1))
                                        .doOnSubscribe(disposable -> mTvGetEmailCode.setEnabled(false))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(aLong -> {
                                            if (aLong == 0) {
                                                mTvGetEmailCode.setEnabled(true);
                                                mTvGetEmailCode.setText(getString(R.string.str_re_get_code));
                                                mTvGetEmailCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                                            } else {
                                                mTvGetEmailCode.setText(String.valueOf(getString(R.string.review_send) + "(" + aLong + ")"));
                                                mTvGetEmailCode.setTextColor(getResources().getColor(R.color.tip_gray));
                                            }
                                        }));
                            }
                        });
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if (ex.getErrorCode().equals("0")) {
                            ToastUtil.show(getString(R.string.email_already_bind));
                        }
                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        Observable<CharSequence> inputEmail = RxTextView.textChanges(mEtEmailInput);
        Observable<CharSequence> inputCode = RxTextView.textChanges(mEtEmailCode);
        Observable.combineLatest(inputEmail, inputCode, (charSequence, charSequence2) ->
                !TextUtils.isEmpty(charSequence) &&
                        !TextUtils.isEmpty(charSequence2) &&
                        charSequence2.length() >= 4)
                .subscribe(aBoolean -> mBtnBindEmail.setEnabled(aBoolean));
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void updateRequestModifyCurrency(String result) {
        int successId;
        if (mIsBind) {
            successId = R.string.modify_phone_or_email_success;
        } else {
            successId = R.string.bind_success;
        }
        ToastUtil.show(getString(successId));
        EventBus.getDefault().post(new PosInfo(603));
        EmailActivity.this.finish();
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        int errorId = -1;
        String errorCode = throwable.getErrorCode();
        if (errorCode.equals(String.valueOf(132))) {
            errorId = R.string.email_already_bind;
        } else if (errorCode.equals(String.valueOf(102))) {
            errorId = R.string.code_error;
        } else {

            if (mTvTitle.getText().toString().equals(getString(R.string.str_bind_email))) {
                errorId = R.string.str_bind_unsuccessful;
            } else if (mTvTitle.getText().toString().equals(getString(R.string.str_modify_email))) {
                errorId = R.string.str_modify_unsuccessful;
            }

        }
        if (errorId != -1) {
            ToastUtil.show(getString(errorId));
        }

    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
