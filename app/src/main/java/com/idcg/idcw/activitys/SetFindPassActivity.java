package com.idcg.idcw.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import com.idcg.idcw.configs.ClientConfig;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.model.logic.SetFindPassLogic;
import com.idcg.idcw.model.params.SetFindPassReqParam;
import com.idcg.idcw.presenter.impl.SetFindPassPresenterImpl;
import com.idcg.idcw.presenter.interfaces.SetFindPassContract;
import foxidcw.android.idcw.common.utils.StringUtils;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2017/12/25.
 */

public class SetFindPassActivity extends BaseWalletActivity<SetFindPassLogic,SetFindPassPresenterImpl> implements TextWatcher, SetFindPassContract.View {
    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.ed_id_pass)
    InPutEditText edIdPass;
    @BindView(R.id.ed_sure_pass)
    InPutEditText edSurePass;
    @BindView(R.id.btn_find_create)
    Button btnFindCreate;
    @BindView(R.id.tv_service_item)
    TextView tvServiceItem;
    @BindView(R.id.img_invisible_pass)
    ImageView imgInvisiblePass;
    @BindView(R.id.img_invisible_sure_pass)
    ImageView imgInvisibleSurePass;

    private String UserPass = "";
    private String SurePass = "";
    private String codePhone = "";
    private String PhoneNum = "";
    private String codeEmail = "";
    private String EmailNum = "";
    private boolean flag = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_find_pass;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tvSetName.setText(R.string.find_assets_new);
        edIdPass.addTextChangedListener(this);
        edSurePass.addTextChangedListener(this);
        btnFindCreate.setEnabled(false);//初始化按钮不能点击
        if (ClientConfig.Instance().getPosCreate() == 0) {
            Intent intent = getIntent();
            Bundle bundle1 = intent.getBundleExtra("PhoneCreate");
            codePhone = bundle1.getString("phoneCode");
            PhoneNum = bundle1.getString("phoneNum");
        } else if (ClientConfig.Instance().getPosCreate() == 1) {
            Intent intent = getIntent();
            Bundle bundle2 = intent.getBundleExtra("EmailCreate");
            codeEmail = bundle2.getString("EmailCode");
            LogUtil.e("codeEmail===>", codeEmail);
            EmailNum = bundle2.getString("EmailNum");
        }
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @OnClick({R.id.mr_back_layout, R.id.ed_id_pass, R.id.ed_sure_pass, R.id.btn_find_create, R.id.tv_service_item,R.id.img_invisible_pass, R.id.img_invisible_sure_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
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
            case R.id.ed_id_pass:
                break;
            case R.id.ed_sure_pass:
                break;
            case R.id.btn_find_create:
                if (ClientConfig.Instance().getPosCreate() == 0) {
                    if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                        RequestPhoneRegister("0");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                        RequestPhoneRegister("1");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("fi")) {
                        RequestPhoneRegister("2");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("ja")) {
                        RequestPhoneRegister("3");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("ko")) {
                        RequestPhoneRegister("4");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("fr")) {
                        RequestPhoneRegister("5");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("nl")) {
                        RequestPhoneRegister("6");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("es")) {
                        RequestPhoneRegister("7");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("vi")) {
                        RequestPhoneRegister("8");
                    } else {
                        String locale = Locale.getDefault().getLanguage();
                        if ("zh".equals(locale)) {
                            RequestPhoneRegister("1");
                        } else {
                            RequestPhoneRegister("0");
                        }
                    }

                } else if (ClientConfig.Instance().getPosCreate() == 1) {

                    if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                        RequestEmailRegister("0");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                        RequestEmailRegister("1");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("fi")) {
                        RequestEmailRegister("2");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("ja")) {
                        RequestEmailRegister("3");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("ko")) {
                        RequestEmailRegister("4");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("fr")) {
                        RequestEmailRegister("5");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("nl")) {
                        RequestEmailRegister("6");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("es")) {
                        RequestEmailRegister("7");
                    } else if (getResources().getConfiguration().locale.getLanguage().equals("vi")) {
                        RequestEmailRegister("8");
                    } else {
                        String locale = Locale.getDefault().getLanguage();
                        if ("zh".equals(locale)) {
                            RequestEmailRegister("1");
                        } else {
                            RequestEmailRegister("0");
                        }
                    }
                }
                break;
            case R.id.tv_service_item:
             //   intentFActivity(ServiceItemActivity.class);
                break;
        }
    }

    private void RequestPhoneRegister(String language) {
        UserPass = edIdPass.getText().toString().trim();
        SurePass = edSurePass.getText().toString().trim();
        if (TextUtils.isEmpty(UserPass) || TextUtils.isEmpty(SurePass)) {
            ToastUtil.show(getString(R.string.content_no_empty));
            return;
        }

        if (UserPass.length() != SurePass.length() || !UserPass.equals(SurePass)) {
            ToastUtil.show(getString(R.string.pass_not_match));
            return;
        }

        if (!StringUtils.isPass(UserPass)) {
            ToastUtil.show(getString(R.string.pass_abc_num));
            return;
        }

        mPresenter.SetFindPass(new SetFindPassReqParam(ClientConfig.Instance().getSaveName(),PhoneNum,
                "",UserPass,SurePass,codePhone,language));
    }

    private void RequestEmailRegister(String language) {
        UserPass = edIdPass.getText().toString().trim();
        SurePass = edSurePass.getText().toString().trim();
        if (TextUtils.isEmpty(UserPass) || TextUtils.isEmpty(SurePass)) {
            ToastUtil.show(getString(R.string.content_no_empty));
            return;
        }

        if (UserPass.length() != SurePass.length() || !UserPass.equals(SurePass)) {
            ToastUtil.show(getString(R.string.pass_not_match));
            return;
        }

        if (!StringUtils.isPass(UserPass)) {
            ToastUtil.show(getString(R.string.pass_abc_num));
            return;
        }
        mPresenter.SetFindPass(new SetFindPassReqParam( ClientConfig.Instance().getSaveName(),"",codeEmail,UserPass,SurePass,EmailNum,language));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnFindCreate.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnFindCreate.setBackgroundResource(R.drawable.item_gray_black);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edIdPass.getText().toString().equals("") || edSurePass.getText().toString().equals("")) && edSurePass.getText().toString().length() >= 6) {
            btnFindCreate.setEnabled(true);
            btnFindCreate.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {

        if(throwable.getErrorCode().equals("102")){
            ToastUtil.show(getString(R.string.code_error));

        }else if(throwable.getErrorCode().equals("108")){
            ToastUtil.show(getString(R.string.pass_not_match));
        }else{
            ToastUtil.show(getString(R.string.server_connection_error));
        }

    }

    @Override
    public void updateSetFindPass(Object result) {


        if(ClientConfig.Instance().getPosCreate()==0){//走手机注册流程
            Intent intent = new Intent(SetFindPassActivity.this, RecoverSuccessActivity.class);
            //用数据捆传递数据
            Bundle bundle = new Bundle();
            bundle.putString("pass", SurePass);
            bundle.putString("phone", PhoneNum);
            intent.putExtra("assets", bundle);
            navigateUpTo(intent);
            EventBus.getDefault().post(new PosInfo(3333));
            finish();
//           // startActivity(intent);
//            EventBus.getDefault().post(new PosInfo(3333));
//            finish();
        }else if(ClientConfig.Instance().getPosCreate()==1){//走邮箱注册流程
            Intent intent = new Intent(SetFindPassActivity.this, RecoverSuccessActivity.class);
            //用数据捆传递数据
            Bundle bundle = new Bundle();
            bundle.putString("pass", SurePass);
            bundle.putString("email", codeEmail);
            intent.putExtra("assets", bundle);
           navigateUpTo(intent);
            finish();

        }


    }
}
