package com.idcg.idcw.activitys;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import com.idcg.idcw.configs.ClientConfig;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.VerifyCodeAndEMailProviderServices;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.model.params.SendEmailCodeReqParam;
import com.idcg.idcw.model.params.SendPhoneCodeReqParam;
import com.idcg.idcw.model.params.VerifyCodeReqParam;
import foxidcw.android.idcw.common.utils.StringUtils;
import com.idcg.idcw.utils.TimeCountUtil;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.CountryCodeBean;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 14:25
 **/
@Route(path = ArouterConstants.FORGET_PASS, name = "忘记密码")
public class ForgetPayPassActivity extends BaseWalletActivity implements TextWatcher {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.tv_country_name)
    TextView tvCountryName;
    @BindView(R.id.ll_select_country)
    LinearLayout llSelectCountry;
    @BindView(R.id.tv_country_num)
    TextView tvCountryNum;
    @BindView(R.id.ed_phone_num)
    InPutEditText edPhoneNum;
    @BindView(R.id.ed_phone_code)
    InPutEditText edPhoneCode;
    @BindView(R.id.tv_phone_get_code)
    TextView tvPhoneGetCode;
    @BindView(R.id.btn_phone_next_step)
    Button btnPhoneNextStep;
    @BindView(R.id.tv_email_create)
    TextView tvEmailCreate;
    @BindView(R.id.ll_phone_create)
    LinearLayout llPhoneCreate;
    @BindView(R.id.ed_email_create)
    InPutEditText edEmailCreate;
    @BindView(R.id.ed_email_code)
    InPutEditText edEmailCode;
    @BindView(R.id.tv_email_get_code)
    TextView tvEmailGetCode;
    @BindView(R.id.btn_email_next_step)
    Button btnEmailNextStep;
    @BindView(R.id.tv_phone_create)
    TextView tvPhoneCreate;
    @BindView(R.id.ll_email_create)
    LinearLayout llEmailCreate;

    private String UserPhoneAccount = "";
    private String UserPhoneCode = "";
    private String UserEmailAccount = "";
    private String UserEmailCode = "";
    private String tvSelectCountry = "";
    private String tvAreaNum;
    private TimeCountUtil mTimeCountUtil;
    private TimeCountUtil mTimeCountUtilEmail;
    private int positionSelect;
    private String value;
    private LoginStatus loginStatus;

    @Autowired
    VerifyCodeAndEMailProviderServices mCodeServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pay_pass;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        loginStatus = LoginUtils.getLoginBean(mCtx);
        try {
            tvSetName.setText(R.string.tv_find_pay_pass);
            if (loginStatus != null) {
                value = loginStatus.getTicket();
                if (loginStatus.getPosMain() == 0) {
                    LogUtil.e("getPosMain==>", loginStatus.getPosMain() + "");
                    if (!TextUtils.isEmpty(loginStatus.getMobile()) && TextUtils.isEmpty(loginStatus.getEmail())) {
                        llPhoneCreate.setVisibility(View.VISIBLE);
                        llEmailCreate.setVisibility(View.GONE);
                        tvEmailCreate.setVisibility(View.GONE);
                    } else if (!TextUtils.isEmpty(loginStatus.getMobile()) && !TextUtils.isEmpty(loginStatus.getEmail())) {
                        llPhoneCreate.setVisibility(View.VISIBLE);
                        llEmailCreate.setVisibility(View.GONE);
                        tvEmailCreate.setVisibility(View.VISIBLE);
                    }
                } else if (loginStatus.getPosMain() == 1) {
                    LogUtil.e("getPosMain==>", loginStatus.getPosMain() + "");
                    if (TextUtils.isEmpty(loginStatus.getMobile()) && !TextUtils.isEmpty(loginStatus.getEmail())) {
                        llPhoneCreate.setVisibility(View.GONE);
                        llEmailCreate.setVisibility(View.VISIBLE);
                        tvPhoneCreate.setVisibility(View.GONE);
                    } else if (!TextUtils.isEmpty(loginStatus.getMobile()) && !TextUtils.isEmpty(loginStatus.getEmail())) {
                        llPhoneCreate.setVisibility(View.VISIBLE);
                        llEmailCreate.setVisibility(View.GONE);
                        tvEmailCreate.setVisibility(View.VISIBLE);
                    } else if (!TextUtils.isEmpty(loginStatus.getMobile()) && TextUtils.isEmpty(loginStatus.getEmail())) {
                        llPhoneCreate.setVisibility(View.VISIBLE);
                        llEmailCreate.setVisibility(View.GONE);
                        tvEmailCreate.setVisibility(View.GONE);
                    }
                }
            }

            mTimeCountUtil = new TimeCountUtil(tvPhoneGetCode, 60000, 1000);
            mTimeCountUtilEmail = new TimeCountUtil(tvEmailGetCode, 60000, 1000);

            edPhoneNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    btnPhoneNextStep.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
                    tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                    btnPhoneNextStep.setTextColor(getResources().getColor(R.color.gray_90));
                    btnPhoneNextStep.setBackgroundResource(R.drawable.item_gray_black);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (edPhoneNum.getText().toString().length() >= 3) {
                        if (!TextUtils.isEmpty(tvCountryName.getText().toString())) {
                            if (tvPhoneGetCode.getText().toString().contains(getString(R.string.review_send))) {
                                tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                            } else {
                                tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edPhoneCode.addTextChangedListener(this);
            edEmailCreate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    btnEmailNextStep.setEnabled(false);
                    tvEmailGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                    btnEmailNextStep.setTextColor(getResources().getColor(R.color.gray_90));
                    btnEmailNextStep.setBackgroundResource(R.drawable.item_gray_black);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (edEmailCreate.getText().toString().length() >= 3) {
                        if (tvEmailGetCode.getText().toString().contains(getString(R.string.review_send))) {
                            tvEmailGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                        } else {
                            tvEmailGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edEmailCode.addTextChangedListener(this);
            btnPhoneNextStep.setEnabled(false);//初始化按钮不能点击
            btnEmailNextStep.setEnabled(false);//初始化按钮不能点击

            edEmailCreate.setFilters(new InputFilter[]{filter});

        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };

    @OnClick({R.id.tv_phone_get_code, R.id.mr_back_layout, R.id.ll_select_country, R.id.btn_phone_next_step, R.id.tv_email_create, R.id.tv_email_get_code, R.id.btn_email_next_step, R.id.tv_phone_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                finish();
                break;
            case R.id.ll_select_country:
                navigation(ArouterConstants.SELECOUNTRY);
                break;
            case R.id.tv_phone_get_code:
                tvAreaNum = tvCountryNum.getText().toString().substring(1, tvCountryNum.getText().toString().length());
                UserPhoneAccount = tvAreaNum + edPhoneNum.getText().toString().trim();
                UserPhoneCode = edPhoneCode.getText().toString().trim();
                tvSelectCountry = tvCountryName.getText().toString().trim();
                if (TextUtils.isEmpty(edPhoneNum.getText().toString().trim())) {
                    ToastUtil.show(getString(R.string.phone_no_empty));
                    return;
                }
                if (tvSelectCountry.equals("")) {
                    ToastUtil.show(getString(R.string.title_select_country));
                    return;
                }
                if ("86".equals(tvAreaNum)) {
                    if (!StringUtils.isMobileNum(edPhoneNum.getText().toString().trim())) {
                        ToastUtil.show(getString(R.string.phone_error));
                        return;
                    }
                }
                if (loginStatus != null) {
                    if (loginStatus.getPosMain() == 0) {
                        if (!UserPhoneAccount.equals(loginStatus.getTelphone())) {
                            LogUtil.e("getSavePhone:", loginStatus.getTelphone());
                            ToastUtil.show(getString(R.string.wallet_pass_not_match));
                            return;
                        }
                    }

                    if (loginStatus.getPosMain() == 1) {
                        if (!UserPhoneAccount.equals(loginStatus.getMobile())) {
                            ToastUtil.show(getString(R.string.wallet_pass_not_match));
                            return;
                        }
                    }
                }
                tvPhoneGetCode.setTextColor(getResources().getColor(R.color.common_black));
                edPhoneCode.requestFocus();
                ForgetPayPassActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                String language;
                if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                    language = "0";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                    language = "1";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fi")) {
                    language = "8";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("ja")) {
                    language = "2";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("ko")) {
                    language = "4";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fr")) {
                    language = "5";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("nl")) {
                    language = "3";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("es")) {
                    language = "7";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("vi")) {
                    language = "6";
                } else {
                    String locale = Locale.getDefault().getLanguage();
                    if ("zh".equals(locale)) {
                        language = "1";
                    } else {
                        language = "0";
                    }
                }
                SendPhoneCodeReqParam reqParam = new SendPhoneCodeReqParam(UserPhoneAccount, "2", language);
                showDialog();
                addSubscribe(mCodeServices.requestPhoneCode(reqParam).subscribeWith(new RxSubscriber<Object>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        if (ex.getErrorCode().equals("0")) {
                            ToastUtil.show(getString(R.string.send_error));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(Object object) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.send_successed));
                        mTimeCountUtil.start();
                    }
                }));
                break;
            case R.id.btn_phone_next_step:
                positionSelect = 3;
                ClientConfig.Instance().setPosForget(positionSelect);
                tvAreaNum = tvCountryNum.getText().toString().substring(1, tvCountryNum.getText().toString().length());
                UserPhoneAccount = tvAreaNum + edPhoneNum.getText().toString().trim();
                UserPhoneCode = edPhoneCode.getText().toString().trim();
                tvSelectCountry = tvCountryName.getText().toString().trim();
                if (TextUtils.isEmpty(edPhoneNum.getText().toString().trim())) {
                    ToastUtil.show(getString(R.string.phone_no_empty));
                    return;
                }
                if (tvSelectCountry.equals(getString(R.string.tv_select_phone_country))) {
                    ToastUtil.show(getString(R.string.tv_select_country));
                    return;
                }
                if ("86".equals(tvAreaNum)) {
                    if (!StringUtils.isMobileNum(edPhoneNum.getText().toString().trim())) {
                        ToastUtil.show(getString(R.string.phone_error));
                        return;
                    }
                }
                VerifyCodeReqParam req = new VerifyCodeReqParam(UserPhoneAccount, "2", UserPhoneCode);
                addSubscribe(mCodeServices.requestVerifyCode(req).subscribeWith(new RxSubscriber<Boolean>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if (ex.getErrorCode().equals("0")) {
                            ToastUtil.show(getString(R.string.code_error));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Bundle bundleEmail = new Bundle();
                        bundleEmail.putString("codePhone", UserPhoneCode);
                        ARouter.getInstance().build(ArouterConstants.RECOVERY_PIN)
                                .withBundle("PhoneForget", bundleEmail).navigation();
                        EventBus.getDefault().post(new PosInfo(1023));
                        finish();
                    }
                }));
                break;
            case R.id.tv_email_create:
                llEmailCreate.setVisibility(View.VISIBLE);
                llPhoneCreate.setVisibility(View.GONE);
                break;
            case R.id.tv_email_get_code:
                UserEmailAccount = edEmailCreate.getText().toString().trim();
                UserEmailCode = edEmailCode.getText().toString().trim();
                if (TextUtils.isEmpty(UserEmailAccount)) {
                    ToastUtil.show(getString(R.string.email_no_empty));
                    return;
                }
                if (loginStatus != null) {
                    if (loginStatus.getPosMain() == 1) {
                        if (!UserEmailAccount.equals(loginStatus.getEmail())) {
                            ToastUtil.show(getString(R.string.email_wallet_pass_not_match));
                            return;
                        }
                    }
                    if (loginStatus.getPosMain() == 0) {
                        if (!UserEmailAccount.equals(loginStatus.getEmail())) {
                            ToastUtil.show(getString(R.string.email_wallet_pass_not_match));
                            return;
                        }
                    }
                }
                tvEmailGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                edEmailCode.requestFocus();
                ForgetPayPassActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                String languageCode;
                if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                    languageCode = "0";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                    languageCode = "1";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fi")) {
                    languageCode = "8";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("ja")) {
                    languageCode = "2";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("ko")) {
                    languageCode = "4";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fr")) {
                    languageCode = "5";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("nl")) {
                    languageCode = "3";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("es")) {
                    languageCode = "7";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("vi")) {
                    languageCode = "6";
                } else {
                    String locales = Locale.getDefault().getLanguage();
                    if ("zh".equals(locales)) {
                        languageCode = "1";
                    } else {
                        languageCode = "0";
                    }
                }
                showDialog();
                SendEmailCodeReqParam codeReqParam = new SendEmailCodeReqParam(UserEmailAccount, languageCode, "2");
                addSubscribe(mCodeServices.requestEmailCode(codeReqParam).subscribeWith(new RxSubscriber<Object>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        if (ex.getErrorCode().equals("0")) {
                            ToastUtil.show(getString(R.string.send_error));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(Object object) {
                        dismissDialog();
                        mTimeCountUtilEmail.start();
                        ToastUtil.show(getString(R.string.send_to_email));
                    }
                }));
                break;
            case R.id.btn_email_next_step:
                positionSelect = 4;
                ClientConfig.Instance().setPosForget(positionSelect);
                UserEmailAccount = edEmailCreate.getText().toString().trim();
                UserEmailCode = edEmailCode.getText().toString().trim();
                if (TextUtils.isEmpty(UserEmailAccount) || TextUtils.isEmpty(UserEmailCode)) {
                    ToastUtil.show(getString(R.string.email_code_no_empty));
                    return;
                }
                VerifyCodeReqParam verifyCodeReqParam = new VerifyCodeReqParam(UserEmailAccount, "2", UserEmailCode);
                addSubscribe(mCodeServices.requestVerifyCode(verifyCodeReqParam).subscribeWith(new RxSubscriber<Object>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if (ex.getErrorCode().equals("0")) {
                            ToastUtil.show(getString(R.string.code_error));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(Object object) {
                        Bundle bundleEmail = new Bundle();
                        bundleEmail.putString("EmailCode", UserEmailCode);
                        ARouter.getInstance().build(ArouterConstants.RECOVERY_PIN)
                                .withBundle("EmailForget", bundleEmail).navigation();
                        EventBus.getDefault().post(new PosInfo(1023));
                        finish();
                    }
                }));
                break;
            case R.id.tv_phone_create:
                llEmailCreate.setVisibility(View.GONE);
                llPhoneCreate.setVisibility(View.VISIBLE);
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

    @Subscriber
    public void subCountryCodeEvent(CountryCodeBean.DataBeanX.DataBean dataBean) {
        tvCountryName.setText(dataBean.getName());
        tvAreaNum = dataBean.getAreacode();
        tvCountryNum.setText(tvAreaNum);
        edPhoneNum.requestFocus();
        ForgetPayPassActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscriber
    public void onPosForgetInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 20) {
                ForgetPayPassActivity.this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Subscriber
    public void onPossInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 11) {
                if (!TextUtils.isEmpty(edPhoneNum.getText().toString())) {
                    tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                }
                if (!TextUtils.isEmpty(edEmailCreate.getText().toString())) {
                    tvEmailGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                }
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnPhoneNextStep.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnEmailNextStep.setEnabled(false);
        btnPhoneNextStep.setBackgroundResource(R.drawable.item_gray_black);
        btnEmailNextStep.setBackgroundResource(R.drawable.item_gray_black);
        //tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
        //tvEmailGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
        btnPhoneNextStep.setTextColor(getResources().getColor(R.color.gray_90));
        btnEmailNextStep.setTextColor(getResources().getColor(R.color.gray_90));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edPhoneNum.getText().toString().equals("") || edPhoneCode.getText().toString().equals("")) && edPhoneCode.getText().toString().length() == 5) {
            btnPhoneNextStep.setEnabled(true);
            btnPhoneNextStep.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
            btnPhoneNextStep.setTextColor(getResources().getColor(R.color.white));
        }

        if (!(edEmailCreate.getText().toString().equals("") || edEmailCode.getText().toString().equals("")) && edEmailCode.getText().toString().length() == 5) {
            btnEmailNextStep.setEnabled(true);
            btnEmailNextStep.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
            btnEmailNextStep.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return super.dispatchTouchEvent(ev);
            }
            // 必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }

        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        try {
            if (v != null && (v instanceof EditText)) {
                int[] leftTop = {0, 0};
                //获取输入框当前的location位置
                v.getLocationInWindow(leftTop);
                int left = leftTop[0];
                int top = leftTop[1];
                int bottom = top + v.getHeight();
                int right = left + v.getWidth();
                if (event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom) {
                    // 点击的是输入框区域，保留点击EditText的事件
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
        return false;
    }
}
