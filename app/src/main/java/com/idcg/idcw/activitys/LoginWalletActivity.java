package com.idcg.idcw.activitys;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import com.idcg.idcw.configs.ClientConfig;
import com.idcg.idcw.iprovider.LoginWalletProviderServices;

import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.idcg.idcw.model.params.LoginReqParam;

import foxidcw.android.idcw.common.utils.StringUtils;

import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.CountryCodeBean;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;

import foxidcw.android.idcw.common.constant.ArouterConstants;

/**
 * Created by hpz on 2017/12/23.
 */
@Route(path = ArouterConstants.LOGINWALLET, name = "旧用户钱包登录页面")
public class LoginWalletActivity extends BaseWalletActivity implements TextWatcher {
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.tv_select_country)
    TextView tvSelectCountry;
    @BindView(R.id.ll_select_country)
    LinearLayout llSelectCountry;
    @BindView(R.id.tv_country_num)
    TextView tvCountryNum;
    @BindView(R.id.ed_phone_number)
    InPutEditText edPhoneNumber;
    @BindView(R.id.ed_login_pass)
    InPutEditText edLoginPass;
    @BindView(R.id.tv_email_create)
    TextView tvEmailCreate;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.btn_next_step)
    TextView btnNextStep;
    @BindView(R.id.ll_phone_login)
    LinearLayout llPhoneLogin;
    @BindView(R.id.btn_phone_login)
    TextView btnPhoneLogin;
    @BindView(R.id.tv_phone_login)
    TextView tvPhoneLogin;
    @BindView(R.id.ll_email_login)
    LinearLayout llEmailLogin;
    @BindView(R.id.ed_email_number)
    InPutEditText edEmailNumber;
    @BindView(R.id.ed_email_pass)
    InPutEditText edEmailPass;
    @BindView(R.id.ll_forget)
    LinearLayout llForget;
    @BindView(R.id.ll_phone_forget)
    LinearLayout llPhoneForget;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    @BindView(R.id.img_invisible_pass)
    ImageView imgInvisiblePass;
    @BindView(R.id.img_invisible_sure_pass)
    ImageView imgInvisibleSurePass;

    private ProgressDialog progressDialog;
    private String UserPhoneID = "";
    private String passPhoneWord = "";
    private String UserEmailID = "";
    private String passEmailWord = "";
    private String tvSelectCountryName = "";
    private String tvNum;
    private boolean flag = true;

    @Autowired
    LoginWalletProviderServices loginWalletProviderServices;//登录的服务

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_wallet;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tvSetName.setText(R.string.login_wallet);
        tvRight.setText("");
        edPhoneNumber.addTextChangedListener(this);
        edLoginPass.addTextChangedListener(this);
        edEmailNumber.addTextChangedListener(this);
        edEmailPass.addTextChangedListener(this);
        btnNextStep.setEnabled(false);//初始化按钮不能点击
        btnPhoneLogin.setEnabled(false);//初始化按钮不能点击
        edLoginPass.setFilters(new InputFilter[]{filter});
        edEmailNumber.setFilters(new InputFilter[]{filter});
        edEmailPass.setFilters(new InputFilter[]{filter});
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };

    @OnClick({R.id.img_invisible_pass, R.id.img_invisible_sure_pass, R.id.ll_phone_forget, R.id.ll_forget, R.id.btn_next_step, R.id.btn_phone_login, R.id.tv_phone_login, R.id.tv_right, R.id.mr_back_layout, R.id.ll_select_country, R.id.ed_phone_number, R.id.ed_login_pass, R.id.tv_email_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_forget:
                ClientConfig.Instance().setPosFind(0);
                navigation(ArouterConstants.FINDMONEY);
                break;
            case R.id.ll_phone_forget:
                ClientConfig.Instance().setPosFind(1);
                navigation(ArouterConstants.FINDMONEY);
                break;
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.ll_select_country:
                navigation(ArouterConstants.SELECOUNTRY);
                break;
            case R.id.ed_phone_number:
                break;
            case R.id.ed_login_pass:
                break;
            case R.id.tv_email_create:
                llEmailLogin.setVisibility(View.VISIBLE);
                llPhoneLogin.setVisibility(View.GONE);
                break;
            case R.id.tv_right:
                break;
            case R.id.btn_next_step:
                checkPhoneTypeIsTrue();//检查手机号的格式是否正确
                RequestPhoneLogin();
                break;
            case R.id.btn_phone_login:
                checkEmailTypeIsTrue();//检查邮箱的格式是否正确
                RequestEmailLogin();
                break;
            case R.id.tv_phone_login:
                llEmailLogin.setVisibility(View.GONE);
                llPhoneLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.img_invisible_pass:
                if (flag == false) {
                    edLoginPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    imgInvisiblePass.setImageResource(R.mipmap.img_pass_invisible);
                    edLoginPass.setSelection(edLoginPass.getText().toString().length());
                } else {
                    edLoginPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    imgInvisiblePass.setImageResource(R.mipmap.img_pass_visible);
                    edLoginPass.setSelection(edLoginPass.getText().toString().length());
                }
                break;
            case R.id.img_invisible_sure_pass:
                if (flag == false) {
                    edEmailPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    imgInvisibleSurePass.setImageResource(R.mipmap.img_pass_invisible);
                    edEmailPass.setSelection(edEmailPass.getText().toString().length());
                } else {
                    edEmailPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    imgInvisibleSurePass.setImageResource(R.mipmap.img_pass_visible);
                    edEmailPass.setSelection(edEmailPass.getText().toString().length());
                }
                break;
        }
    }

    private void checkPhoneTypeIsTrue() {
        UserPhoneID = edPhoneNumber.getText().toString().trim();
        passPhoneWord = edLoginPass.getText().toString().trim();
        tvSelectCountryName = tvSelectCountry.getText().toString().trim();
        if (TextUtils.isEmpty(UserPhoneID) || TextUtils.isEmpty(passPhoneWord)) {
            ToastUtil.show(getString(R.string.input_id_pwd));
            return;
        }
        if (tvSelectCountryName.equals("")) {
            ToastUtil.show(getString(R.string.title_select_country));
            return;
        }
        tvNum = tvCountryNum.getText().toString().substring(1, tvCountryNum.getText().toString().length());
        LogUtil.e("num:", tvNum);
        if ("86".equals(tvNum)) {
            if (!StringUtils.isMobileNum(UserPhoneID)) {
                ToastUtil.show(getString(R.string.phone_error));
                return;
            }
        }

        if (passPhoneWord.length() < 6) {
            ToastUtil.show(getString(R.string.password_length_not_enougth));
            return;
        }
    }

    private void checkEmailTypeIsTrue() {
        UserEmailID = edEmailNumber.getText().toString().trim();
        passEmailWord = edEmailPass.getText().toString().trim();
        if (TextUtils.isEmpty(UserEmailID) || TextUtils.isEmpty(passEmailWord)) {
            ToastUtil.show(getString(R.string.input_account_pwd));
            return;
        }
        if (passEmailWord.length() < 6) {
            ToastUtil.show(getString(R.string.password_length_not_enougth));
            return;
        }
    }

    private void RequestPhoneLogin() {
        //登录过程中显示dialog
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.login_wait));
        progressDialog.setCancelable(true);
        loginWalletProviderServices.reqLogin(new LoginReqParam(tvNum + UserPhoneID
                , passPhoneWord))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxSubscriber<LoginStatus>() {

            @Override
            protected void onStart() {
                super.onStart();
                if (progressDialog != null) progressDialog.show();
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                progressDialog.dismiss();
                if (ex.getErrorCode().equals("0")) {
                    edLoginPass.getText().clear();
                    ToastUtil.show(getString(R.string.user_or_pass_error));
                } else if (ex.getErrorCode().equals("130")) {
                    ToastUtil.show(getString(R.string.str_phrase_not_old_login));
                } else {
                    ToastUtil.show(getString(R.string.server_connection_error));
                }
            }

            @Override
            public void onSuccess(LoginStatus loginStatus) {
                ClientConfig.Instance().setTicketValue(loginStatus.getTicket());
                progressDialog.dismiss();
                loginStatus.setPosMain(0);
                ToastUtil.show(getString(R.string.login_success));
                loginStatus.setPosMain(3);
                ACacheUtil.get(LoginWalletActivity.this).put(AcacheKeys.loginbean, loginStatus);
                navigation(ArouterConstants.OLDBACKPHRASE);
            }
        });
    }

    private void RequestEmailLogin() {
        //登录过程中显示dialog
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.login_wait));
        progressDialog.setCancelable(true);
        loginWalletProviderServices.reqLogin(new LoginReqParam(UserEmailID, passEmailWord))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxSubscriber<LoginStatus>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (progressDialog != null) progressDialog.show();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        progressDialog.dismiss();
                        if (ex.getErrorCode().equals("0")) {
                            ToastUtil.show(getString(R.string.user_or_pass_error));
                            edEmailPass.getText().clear();
                        } else if (ex.getErrorCode().equals("130")) {
                            ToastUtil.show(getString(R.string.str_phrase_not_old_login));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(LoginStatus loginStatus) {
                        loginStatus.setPosMain(1);
                        progressDialog.dismiss();
                        ToastUtil.show(getString(R.string.login_success));
                        loginStatus.setPosMain(3);
                        ACacheUtil.get(LoginWalletActivity.this).put(AcacheKeys.loginbean, loginStatus);
                        EventBus.getDefault().post(new PosInfo(31));
                        navigation(ArouterConstants.OLDBACKPHRASE);
                    }
                });
    }

    @Subscriber
    public void subCountryCodeEvent(CountryCodeBean.DataBeanX.DataBean dataBean) {//跳转进入选择国家列表的回调
        edPhoneNumber.requestFocus();
        LoginWalletActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        tvSelectCountry.setText(dataBean.getName());
        tvNum = dataBean.getAreacode();
        tvCountryNum.setText(tvNum);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnNextStep.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnNextStep.setBackgroundResource(R.drawable.item_gray_black);
        btnPhoneLogin.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnPhoneLogin.setBackgroundResource(R.drawable.item_gray_black);
        btnNextStep.setTextColor(getResources().getColor(R.color.gray_90));
        btnPhoneLogin.setTextColor(getResources().getColor(R.color.gray_90));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {//不能输入空格
        if (s.toString().contains(" ")) {
            String[] str = s.toString().split(" ");
            String str1 = "";
            for (int i = 0; i < str.length; i++) {
                str1 += str[i];
            }
            edLoginPass.setText(str1);
            edLoginPass.setSelection(start);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edPhoneNumber.getText().toString().equals("") && edLoginPass.getText().toString().equals("")) && edLoginPass.getText().toString().length() >= 6) {
            btnNextStep.setEnabled(true);
            btnNextStep.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
            btnNextStep.setTextColor(getResources().getColor(R.color.white));
        }

        if (!(edEmailNumber.getText().toString().equals("") || edEmailPass.getText().toString().equals("")) && edEmailPass.getText().toString().length() >= 6) {
            btnPhoneLogin.setEnabled(true);
            btnPhoneLogin.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
            btnPhoneLogin.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Subscriber
    public void onOldLoginResultInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 601) {
                this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected void checkAppVersion() {//前后台切换检查更新
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
