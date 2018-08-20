package com.idcg.idcw.activitys;

import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import com.idcg.idcw.configs.ClientConfig;

import foxidcw.android.idcw.common.constant.ArouterConstants;

import com.idcg.idcw.iprovider.ReqRegisterIproviderServices;
import foxidcw.android.idcw.common.utils.StringUtils;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2017/12/22.
 */
@Route(path = ArouterConstants.CREATESETPASS, name = "注册页面")
public class CreateSetPassActivity extends BaseWalletActivity implements TextWatcher {
    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;
    @BindView(R.id.tv_service_item)
    TextView tvServiceItem;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_id_pass)
    InPutEditText edIdPass;
    @BindView(R.id.ed_sure_pass)
    InPutEditText edSurePass;
    @BindView(R.id.ed_user_name)
    InPutEditText edUserName;
    @BindView(R.id.img_invisible_pass)
    ImageView imgInvisiblePass;
    @BindView(R.id.img_invisible_sure_pass)
    ImageView imgInvisibleSurePass;
    private String userPhone;
    private String userEmail;
    private String UserPass = "";
    private String SurePass = "";
    private String UserName = "";
    private boolean flag = true;
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };
    @Autowired
    ReqRegisterIproviderServices reqRegisterIproviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pass;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//背景为白色的时候，顶部状态栏的字体显示为黑体
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tvSetName.setText(R.string.set_pass);
        tvRight.setText("");
        edUserName.addTextChangedListener(this);
        edSurePass.addTextChangedListener(this);
        edIdPass.addTextChangedListener(this);
        btnNextStep.setEnabled(false);//初始化按钮不能点击
    }

    @Override
    protected void onEvent() {
        edIdPass.setFilters(new InputFilter[]{filter});
        edSurePass.setFilters(new InputFilter[]{filter});
        edUserName.setFilters(new InputFilter[]{filter});
        edUserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//20
        edIdPass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//20
        edSurePass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//20
    }

    @Override
    protected BaseView getView() {
        return null;
    }


    private void RequestPhoneRegister(String language, String url) {

        UserName = edUserName.getText().toString().trim();
        UserPass = edIdPass.getText().toString().trim();
        SurePass = edSurePass.getText().toString().trim();
        if (TextUtils.isEmpty(UserName) || TextUtils.isEmpty(UserPass) || TextUtils.isEmpty(SurePass)) {
            ToastUtil.show(getString(R.string.content_no_empty));
            return;
        }

        if (UserPass.length() < 6 || SurePass.length() < 6) {
            ToastUtil.show(getString(R.string.password_length_not_enougth));
            return;
        }

        if (UserPass.length() != SurePass.length() || !UserPass.equals(SurePass)) {
            ToastUtil.show(getString(R.string.pass_not_match));
            return;
        }

        if (!StringUtils.isUserName(UserName)) {
            ToastUtil.show(getString(R.string.input_four_sixteen_text));
            return;
        }

        if (!StringUtils.isPass(UserPass)) {
            ToastUtil.show(getString(R.string.pass_abc_num));
            return;
        }


//        addSubscribe(reqRegisterIproviderServices.requestRegisterProvider(new CreateSetPassReqParam(UserName,
//                ClientConfig.Instance().getSaveID(),
//                "",
//                UserPass,
//                SurePass,
//                "",
//
//                ClientConfig.Instance().getPhoneNameSaveID(),
//                "2",
//                language,
//                url,
//                "",
//                true,
//                ""
//        )).subscribeWith(new RxSubscriber<LoginStatus>() {
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
//                loginStatus.setPosMain(0);
//                ACacheUtil.get(CreateSetPassActivity.this).put(AcacheKeys.loginbean, loginStatus);
//                intentFActivity(MainActivity.class);
//                CreateSetPassActivity.this.finish();
//            }
//        }));
    }

    private void RequestEmailRegister(String language, String url) {
        UserName = edUserName.getText().toString().trim();
        UserPass = edIdPass.getText().toString().trim();
        SurePass = edSurePass.getText().toString().trim();
        if (TextUtils.isEmpty(UserName) || TextUtils.isEmpty(UserPass) || TextUtils.isEmpty(SurePass)) {
            ToastUtil.show(getString(R.string.content_no_empty));
            return;
        }
        if (UserPass.length() < 6 || SurePass.length() < 6) {
            ToastUtil.show(getString(R.string.password_length_not_enougth));
            return;
        }
        if (UserPass.length() != SurePass.length() || !UserPass.equals(SurePass)) {
            ToastUtil.show(getString(R.string.pass_not_match));
            return;
        }
        if (!StringUtils.isUserName(UserName)) {
            ToastUtil.show(getString(R.string.input_four_sixteen_text));
            return;
        }
        if (!StringUtils.isPass(UserPass)) {
            ToastUtil.show(getString(R.string.pass_abc_num));
            return;
        }
        if (UserPass.length() < 6 || SurePass.length() < 6) {
            ToastUtil.show(getString(R.string.password_length_not_enougth));
            return;
        }

//        addSubscribe(reqRegisterIproviderServices.requestRegisterProvider(new CreateSetPassReqParam(UserName, "", ClientConfig.Instance().getEmailSaveID(),
//                UserPass, SurePass, "", ClientConfig.Instance().getEmailNameSaveID(), "2", language, url,"",
//                true,
//                ""))
//                .subscribeWith(new RxSubscriber<LoginStatus>() {
//                    @Override
//                    protected void onError(ResponseThrowable ex) {
//
//                        if (ex.getErrorCode().equals("0")) {
//                            ToastUtil.show(getString(R.string.pass_not_match));
//                        } else if (ex.getErrorCode().equals("106")) {
//                            ToastUtil.show(getString(R.string.username_exist));
//                        } else if (ex.getErrorCode().equals("102")) {
//                            ToastUtil.show(getString(R.string.code_error));
//                        } else {
//                            ToastUtil.show(getString(R.string.server_connection_error));
//                        }
//                    }
//
//                    @Override
//                    public void onSuccess(LoginStatus loginStatus) {
//
//                    }
//                }));
    }

    @OnClick({R.id.mr_back_layout, R.id.btn_next_step, R.id.tv_service_item, R.id.tv_right, R.id.ed_id_pass, R.id.ed_sure_pass, R.id.ed_user_name, R.id.img_invisible_pass, R.id.img_invisible_sure_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.btn_next_step:
                if (ClientConfig.Instance().getPosition() == 0) {
                    String locale = Locale.getDefault().getLanguage();
                    if ("zh".equals(locale)) {
                        //RequestPhoneRegister("1", "zh-CN");
                    } else {
                        //RequestPhoneRegister("0", "en");
                    }

                } else if (ClientConfig.Instance().getPosition() == 1) {
                    String locale = Locale.getDefault().getLanguage();
                    if ("zh".equals(locale)) {
                        RequestEmailRegister("1", "zh-CN");
                    } else {
                        RequestEmailRegister("0", "en");
                    }
                }
                break;
            case R.id.tv_service_item:
                //   intentFActivity(ServiceItemActivity.class);
                break;
            case R.id.tv_right:
                break;
            case R.id.ed_id_pass:
                break;
            case R.id.ed_sure_pass:
                break;
            case R.id.ed_user_name:
                break;
            case R.id.img_invisible_pass:
                if (flag == false) {
                    edIdPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    imgInvisiblePass.setImageResource(R.mipmap.img_pass_invisible);
                    edIdPass.setSelection(edIdPass.getText().toString().length());
                } else {
                    edIdPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    imgInvisiblePass.setImageResource(R.mipmap.img_pass_visible);
                    edIdPass.setSelection(edIdPass.getText().toString().length());
                }
                break;
            case R.id.img_invisible_sure_pass:
                if (flag == false) {
                    edSurePass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    imgInvisibleSurePass.setImageResource(R.mipmap.img_pass_invisible);
                    edSurePass.setSelection(edSurePass.getText().toString().length());
                } else {
                    edSurePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    imgInvisibleSurePass.setImageResource(R.mipmap.img_pass_visible);
                    edSurePass.setSelection(edSurePass.getText().toString().length());
                }
                break;
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnNextStep.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnNextStep.setBackgroundResource(R.drawable.item_gray_black);
        btnNextStep.setTextColor(getResources().getColor(R.color.gray_90));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edIdPass.getText().toString().equals("") || edSurePass.getText().toString().equals("") || edUserName.getText().toString().equals("")) && edUserName.getText().toString().length() >= 4) {
            btnNextStep.setEnabled(true);
            btnNextStep.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
            btnNextStep.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
