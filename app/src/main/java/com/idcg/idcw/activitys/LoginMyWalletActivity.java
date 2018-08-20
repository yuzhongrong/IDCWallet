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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.LoginWalletProviderServices;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.model.params.LoginReqParam;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2018/3/27.
 */

@Route(path = ArouterConstants.loginmywallet, name = "登录钱包新页面")
public class LoginMyWalletActivity extends BaseWalletActivity implements TextWatcher {
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_wallet_name)
    InPutEditText edWalletName;
    @BindView(R.id.ed_wallet_pass)
    InPutEditText edWalletPass;
    @BindView(R.id.img_invisible_pass)
    ImageView imgInvisiblePass;
    @BindView(R.id.ll_find_assets)
    LinearLayout llFindAssets;
    @BindView(R.id.btn_login_wallet)
    Button btnLoginWallet;

    private ProgressDialog progressDialog;
    private String UserEmailID = "";
    private String passEmailWord = "";
    private boolean flag = true;

    @Autowired
    LoginWalletProviderServices loginWalletProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_my_wallet;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tvSetName.setText(getString(R.string.login_wallet));
        tvRight.setText(getString(R.string.tv_create));

        edWalletName.addTextChangedListener(this);
        edWalletPass.addTextChangedListener(this);
        btnLoginWallet.setEnabled(false);//初始化按钮不能点击
        edWalletName.setFilters(new InputFilter[]{filter});
        edWalletPass.setFilters(new InputFilter[]{filter});
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    private void RequestWalletLogin() {
        //登录过程中显示dialog
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.login_wait));
        progressDialog.setCancelable(true);
        addSubscribe(loginWalletProviderServices.reqLogin(new LoginReqParam(UserEmailID,passEmailWord))
                .subscribeWith(new RxSubscriber<LoginStatus>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (progressDialog != null) progressDialog.show();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                        if(ex.getErrorCode().equals("0")){
                            ToastUtil.show(getString(R.string.wallet_name_or_pass_error));
                            edWalletPass.getText().clear();

                        }else{
                            progressDialog.dismiss();
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }
                    @Override
                    public void onSuccess(LoginStatus loginStatus) {
                        loginStatus.setPosMain(1);
                        progressDialog.dismiss();
                        ToastUtil.show(getString(R.string.login_success));
                        ACacheUtil.get(LoginMyWalletActivity.this).put(AcacheKeys.loginbean, loginStatus);
                        EventBus.getDefault().post(new PosInfo(31));
                        intentFActivity(MainActivity.class);
                        finish();
                    }
                }));
    }

    @OnClick({R.id.mr_back_layout, R.id.tv_right, R.id.btn_login_wallet,R.id.ll_find_assets,R.id.img_invisible_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.tv_right:
                navigation(ArouterConstants.registerWallet);
                break;
            case R.id.btn_login_wallet:
                UserEmailID = edWalletName.getText().toString().trim();
                passEmailWord = edWalletPass.getText().toString().trim();
                if (TextUtils.isEmpty(UserEmailID) || TextUtils.isEmpty(passEmailWord)) {
                    ToastUtil.show(getString(R.string.input_account_pwd));
                    return;
                }
                if (passEmailWord.length() < 6) {
                    ToastUtil.show(getString(R.string.password_length_not_enougth));
                    return;
                }
                RequestWalletLogin();
                break;
            case R.id.ll_find_assets:
                navigation(ArouterConstants.FINDMONEY);
                break;
            case R.id.img_invisible_pass:
                if (flag == false) {
                    edWalletPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    imgInvisiblePass.setImageResource(R.mipmap.img_pass_invisible);
                    edWalletPass.setSelection(edWalletPass.getText().toString().length());
                } else {
                    edWalletPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    imgInvisiblePass.setImageResource(R.mipmap.img_pass_visible);
                    edWalletPass.setSelection(edWalletPass.getText().toString().length());
                }
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnLoginWallet.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnLoginWallet.setBackgroundResource(R.drawable.item_gray_black);
        btnLoginWallet.setTextColor(getResources().getColor(R.color.gray_90));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(" ")) {
            String[] str = s.toString().split(" ");
            String str1 = "";
            for (int i = 0; i < str.length; i++) {
                str1 += str[i];
            }
            edWalletPass.setText(str1);
            edWalletPass.setSelection(start);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edWalletName.getText().toString().equals("") || edWalletPass.getText().toString().equals("")) && edWalletPass.getText().toString().length() >= 6) {
            btnLoginWallet.setEnabled(true);
            btnLoginWallet.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
            btnLoginWallet.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
