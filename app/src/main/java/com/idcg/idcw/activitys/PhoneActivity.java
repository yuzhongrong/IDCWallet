package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.idcg.idcw.model.params.SendPhoneCodeReqParam;
import com.idcg.idcw.model.params.VerfityNameReqParam;
import com.idcg.idcw.presenter.impl.LocalCurrencyPresenterImpl;
import com.idcg.idcw.presenter.interfaces.LocalCurrencyContract;

import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.StringUtils;

import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.CountryCodeBean;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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
 * 修改时间：2018/3/27 15:56
 **/
@Route(path = ArouterConstants.ACTIVITY_PHONE, name = "绑定或者更改手机号")
public class PhoneActivity extends BaseWalletActivity<LocalCurrencyLogic, LocalCurrencyPresenterImpl> implements LocalCurrencyContract.View {

    private static final int MAX_COUNT_TIME = 60;

    @BindView(R.id.tv_set_Name)
    TextView mTvTitle;

    @BindView(R.id.tv_country_name)
    TextView mTvCountryName;

    @BindView(R.id.tv_country_num)
    TextView mTvCountryCode;

    @BindView(R.id.ed_phone_num)
    EditText mEtPhoneNumber;

    @BindView(R.id.ed_phone_code)
    EditText mEtVerifyCode;

    @BindView(R.id.btn_phone_next_step)
    TextView mBtnBind;

    @BindView(R.id.tv_phone_get_code)
    TextView mTvGetCode;

    private String mCountryName;
    private String mCountryCode;

    @Autowired(name = "bind_or_change")
    boolean isBind = false;

    // 获取验证码
    @Autowired
    VerifyCodeAndEMailProviderServices verifyCodeAndEMailProviderServices;

    @Autowired
    VerifyUserNameProviderServices verifyUserNameProviderServices;
    private String tvAreaNum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        int titleId;
        int btnId;
        if (isBind) {
            titleId = R.string.modify_phone_number;
            btnId = R.string.change;
        } else {
            titleId = R.string.bind_phone_number;
            btnId = R.string.bind;
        }
        mTvTitle.setText(titleId);
        mBtnBind.setText(btnId);

        mEtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTvGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtPhoneNumber.getText().toString().length() >= 3) {
                    if (!TextUtils.isEmpty(mTvCountryName.getText().toString())) {
                        if (mTvGetCode.getText().toString().contains(getString(R.string.review_send))) {
                            mTvGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                        } else {
                            mTvGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initCountryMsg();//设置默认语言区号
    }

    private void initCountryMsg() {
        switch (AppLanguageUtils.getLanguageLocalCode(mCtx)){
            case "0"://英文
                mCountryName = "United Kingdom";
                mCountryCode =  "+44";
                break;
            case "1"://中文
                mCountryName = "中国";
                mCountryCode =  "+86";
                break;
            case "8"://繁体
                mCountryName = "中國香港特別行政區";
                mCountryCode =  "+852";
                break;
            case"6"://越南
                mCountryName = "Việt Nam";
                mCountryCode =  "+84";
                break;
                default:
                    break;
        }
        if (!TextUtils.isEmpty(mCountryName)&&!TextUtils.isEmpty(mCountryCode)){
            mTvCountryName.setText(mCountryName);
            mTvCountryCode.setText(mCountryCode);
        }
    }

    @OnClick({
            R.id.ll_select_country,
            R.id.btn_phone_next_step,
            R.id.tv_phone_get_code,
            R.id.mr_back_layout
    })
    public void onPhoneClick(View view) {
        switch (view.getId()) {
            case R.id.ll_select_country:
                ARouter.getInstance().build(ArouterConstants.SELECOUNTRY).navigation(this);
                break;
            case R.id.btn_phone_next_step:
                String inPhone = mEtPhoneNumber.getText().toString().trim();
                String phoneCode = mEtVerifyCode.getText().toString().trim();
                if (TextUtils.isEmpty(inPhone) || TextUtils.isEmpty(phoneCode)) {
                    ToastUtil.show(getString(R.string.phone_no_code_empty));
                    return;
                }
                mCountryName = mTvCountryName.getText().toString().trim();
                if (TextUtils.isEmpty(mCountryName)) { // 判断国家是否有选择
                    ToastUtil.show(getString(R.string.str_tv_select_country));
                    return;
                }
                if (mCountryCode.equals(String.valueOf(86))) {
                    if (!StringUtils.isMobileNum(inPhone)) {
                        ToastUtil.show(getString(R.string.phone_error));
                        return;
                    }
                }

                LocalCurrencyReqParam modifyPhoneReqParam = new LocalCurrencyReqParam();
                modifyPhoneReqParam.setContent(tvAreaNum + inPhone);
                modifyPhoneReqParam.setType(String.valueOf(0));
                modifyPhoneReqParam.setVerifyCode(phoneCode);
                mPresenter.requestModifyCurrency(modifyPhoneReqParam);
                showDialog();
                break;
            case R.id.tv_phone_get_code:
                String inputPhone = mEtPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(inputPhone)) {
                    ToastUtil.show(getString(R.string.phone_no_empty));
                    return;
                }
                if (TextUtils.isEmpty(mCountryName)) { // 判断国家是否有选择
                    ToastUtil.show(getString(R.string.str_tv_select_country));
                    return;
                }
                tvAreaNum = mCountryCode.substring(1, mCountryCode.length());
                if (tvAreaNum.equals(String.valueOf(86))) {
                    if (!StringUtils.isMobileNum(inputPhone)) {
                        ToastUtil.show(getString(R.string.phone_error));
                        return;
                    }
                }
                RquestPhoneUserName(tvAreaNum + inputPhone);
                mEtVerifyCode.requestFocus();
                break;
            case R.id.mr_back_layout:
                finish();
                break;
            default:
                break;
        }
    }

    private void RquestPhoneUserName(String phone) {
        //mPresenter.verfityName(new VerfityNameReqParam(tvAreaNum + UserPhoneAccount,"0"));
        verifyUserNameProviderServices.requestCheckUserName(new VerfityNameReqParam(phone, "0"))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<Object>(PhoneActivity.this) {
                    @Override
                    public void onSuccess(Object data) {
                        SendPhoneCodeReqParam reqParam = new SendPhoneCodeReqParam();
                        reqParam.setLanguage(AppLanguageUtils.getLanguageLocalCode(mCtx));
                        showDialog();
                        //tvAreaNum = mCountryCode.substring(1, mCountryCode.length());
                        reqParam.setMobile(phone);
                        reqParam.setVerifyCodeType(String.valueOf(4));
                        verifyCodeAndEMailProviderServices.requestPhoneCode(reqParam)
                                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                                .subscribeWith(new RxProgressSubscriber<Object>(PhoneActivity.this) {
                            @Override
                            protected void onError(ResponseThrowable ex) {
                                PhoneActivity.this.dismissDialog();
                                mTvGetCode.setEnabled(true);
                                if (ex.getErrorCode().equals(String.valueOf(0))) {
                                    ToastUtil.show(getString(R.string.send_error));//发送失败
                                } else {
                                    ToastUtil.show(getString(R.string.server_connection_error));//服务报错
                                }
                            }

                            @SuppressLint("CheckResult")
                            @Override
                            public void onSuccess(Object data) {
                                PhoneActivity.this.dismissDialog();
                                ToastUtil.show(getString(R.string.send_successed));
                                addSubscribe(Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                                        .take(MAX_COUNT_TIME)
                                        .map(aLong -> MAX_COUNT_TIME - (aLong + 1))
                                        .doOnSubscribe(disposable -> mTvGetCode.setEnabled(false))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(aLong -> {
                                            if (aLong == 0) {
                                                mTvGetCode.setEnabled(true);
                                                mTvGetCode.setText(getString(R.string.str_re_get_code));
                                                mTvGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                                            } else {
                                                mTvGetCode.setText(String.valueOf(getString(R.string.review_send) + "(" + aLong + ")"));
                                                mTvGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                                            }
                                        }));
                            }
                        });
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if (ex.getErrorCode().equals("0")) {
                            ToastUtil.show(getString(R.string.phone_already_bind));
                        }
                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        Observable<CharSequence> mEtPhone = RxTextView.textChanges(mEtPhoneNumber);
        Observable<CharSequence> mEtCode = RxTextView.textChanges(mEtVerifyCode);
        Observable<CharSequence> mTvCode = RxTextView.textChanges(mTvCountryCode);
        Observable.combineLatest(mEtPhone, mEtCode, mTvCode, (phone, code, countryCode) ->
                !TextUtils.isEmpty(phone) &&
                        !TextUtils.isEmpty(code) &&
                        !TextUtils.isEmpty(countryCode)).subscribe(aBoolean -> {
            mBtnBind.setEnabled(aBoolean);
        });
    }

    @Subscriber
    public void subCountryCodeEvent(CountryCodeBean.DataBeanX.DataBean dataBean) {
        try {
            mEtPhoneNumber.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            mCountryName = dataBean.getName();
            mTvCountryName.setText(mCountryName);
            mCountryCode = dataBean.getAreacode();
            mTvCountryCode.setText(mCountryCode);
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void updateRequestModifyCurrency(String result) {
        dismissDialog();
        int successId;
        if (isBind) {
            successId = R.string.modify_phone_or_email_success;
        } else {
            successId = R.string.bind_success;
        }
        ToastUtil.show(getString(successId));
        boolean isbind = true;
        ACacheUtil.get(mCtx).put(AcacheKeys.ISBINDPHONE, isbind);
        EventBus.getDefault().post(new PosInfo(603));
        PhoneActivity.this.finish();
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        String code = throwable.getErrorCode();
        int errorId = -1;
        if (code.equals(String.valueOf(131))) {
            errorId = R.string.phone_already_bind;
        } else if (code.equals(String.valueOf(102))) {
            errorId = R.string.code_error;
        } else {
            if (mTvTitle.getText().toString().equals(getString(R.string.bind_phone_number))) {
                errorId = R.string.str_bind_unsuccessful;
            } else if (mTvTitle.getText().toString().equals(getString(R.string.modify_phone_number))) {
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
