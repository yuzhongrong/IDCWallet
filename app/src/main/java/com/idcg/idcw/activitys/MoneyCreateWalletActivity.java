package com.idcg.idcw.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import com.idcg.idcw.configs.ClientConfig;
import com.idcg.idcw.iprovider.VerifyCodeAndEMailProviderServices;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.model.params.SendEmailCodeReqParam;
import com.idcg.idcw.model.params.SendPhoneCodeReqParam;
import com.idcg.idcw.model.params.VerifyCodeReqParam;
import foxidcw.android.idcw.common.utils.StringUtils;
import com.idcg.idcw.utils.TimeCountUtil;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.CountryCodeBean;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.Subscriber;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2017/12/27.
 */

public class MoneyCreateWalletActivity extends BaseWalletActivity implements TextWatcher {
    @BindView(R.id.img_back)
    ImageView imgBack;
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
    @BindView(R.id.ll_one)
    LinearLayout llOne;

    private String UserPhoneAccount = "";
    private String UserPhoneCode = "";
    private String UserEmailAccount = "";
    private String UserEmailCode = "";
    private String tvSelectCountry = "";
    private String tvAreaNum;
    private TimeCountUtil mTimeCountUtil;
    private TimeCountUtil mTimeEmailCountUtil;
    private int positionLoginSelect;

    @Autowired
    VerifyCodeAndEMailProviderServices verifyCodeAndEMailProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_money_create;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mTimeCountUtil = new TimeCountUtil(tvPhoneGetCode, 60000, 1000);
        mTimeEmailCountUtil = new TimeCountUtil(tvEmailGetCode, 60000, 1000);
        edPhoneCode.addTextChangedListener(this);
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
                        if (tvPhoneGetCode.getText().toString().contains(getString(R.string.review_send))){
                            tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                        }else {
                            tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnPhoneNextStep.setEnabled(false);//初始化按钮不能点击
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
                    if (tvEmailGetCode.getText().toString().contains(getString(R.string.review_send))){
                        tvEmailGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                    }else {
                        tvEmailGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edEmailCode.addTextChangedListener(this);
        btnEmailNextStep.setEnabled(false);//初始化按钮不能点击*/
        edEmailCreate.setFilters(new InputFilter[]{filter});
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

    @OnClick({R.id.img_back, R.id.ll_select_country, R.id.tv_phone_get_code, R.id.btn_phone_next_step, R.id.tv_email_create, R.id.tv_email_get_code, R.id.btn_email_next_step, R.id.tv_phone_create, R.id.ll_email_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.finish();
                break;
            case R.id.ll_select_country:
                intentFActivity(SeleCountryActivity.class);
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
                //imgFindPhone.setVisibility(View.INVISIBLE);
                tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
                edPhoneCode.requestFocus();
                MoneyCreateWalletActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


                if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                    RequestPhoneCode("0");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                    RequestPhoneCode("1");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fi")){
                    RequestPhoneCode("8");
                }  else if (getResources().getConfiguration().locale.getLanguage().equals("ja")) {
                    RequestPhoneCode("2");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("ko")) {
                    RequestPhoneCode("4");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fr")) {
                    RequestPhoneCode("5");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("nl")) {
                    RequestPhoneCode("3");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("es")) {
                    RequestPhoneCode("7");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("vi")) {
                    RequestPhoneCode("6");
                } else {
                    String locale = Locale.getDefault().getLanguage();
                    if ("zh".equals(locale)) {
                        RequestPhoneCode("1");
                    } else {
                        RequestPhoneCode("0");
                    }
                }

                break;
            case R.id.btn_phone_next_step:
                positionLoginSelect = 0;
                ClientConfig.Instance().setPosCreate(positionLoginSelect);
                tvAreaNum = tvCountryNum.getText().toString().substring(1, tvCountryNum.getText().toString().length());
                UserPhoneAccount = tvAreaNum + edPhoneNum.getText().toString().trim();
                UserPhoneCode = edPhoneCode.getText().toString().trim();
                if (TextUtils.isEmpty(edPhoneNum.getText().toString().trim()) || TextUtils.isEmpty(UserPhoneCode)) {
                    ToastUtil.show(getString(R.string.phone_no_code_empty));
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
                ReqVerifyCode(UserPhoneAccount,"3",UserPhoneCode,true);
                break;
            case R.id.tv_email_create:
                llOne.setVisibility(View.GONE);
                llEmailCreate.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_email_get_code:
                UserEmailAccount = edEmailCreate.getText().toString().trim();
                UserEmailCode = edEmailCode.getText().toString().trim();
                if (TextUtils.isEmpty(UserEmailAccount)) {
                    ToastUtil.show(getString(R.string.email_no_empty));
                    return;
                }
                if (!StringUtils.isEmail(UserEmailAccount)) {
                    ToastUtil.show(getString(R.string.email_error));
                    return;
                }

                //imgFindEmail.setVisibility(View.INVISIBLE);
                tvEmailGetCode.setTextColor(getResources().getColor(R.color.common_black));
                edEmailCode.requestFocus();
                MoneyCreateWalletActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                    RequestEmailCode("0");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                    RequestEmailCode("1");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fi")){
                    RequestEmailCode("8");
                }  else if (getResources().getConfiguration().locale.getLanguage().equals("ja")) {
                    RequestEmailCode("2");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("ko")) {
                    RequestEmailCode("4");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fr")) {
                    RequestEmailCode("5");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("nl")) {
                    RequestEmailCode("3");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("es")) {
                    RequestEmailCode("7");
                } else if (getResources().getConfiguration().locale.getLanguage().equals("vi")) {
                    RequestEmailCode("6");
                } else {
                    String locales = Locale.getDefault().getLanguage();
                    if ("zh".equals(locales)) {
                        RequestEmailCode("1");
                    } else {
                        RequestEmailCode("0");
                    }
                }
                break;
            case R.id.btn_email_next_step:
                positionLoginSelect = 1;
                ClientConfig.Instance().setPosCreate(positionLoginSelect);
                UserEmailAccount = edEmailCreate.getText().toString().trim();
                UserEmailCode = edEmailCode.getText().toString().trim();
                if (TextUtils.isEmpty(UserEmailAccount) || TextUtils.isEmpty(UserEmailCode)) {
                    ToastUtil.show(getString(R.string.email_code_no_empty));
                    return;
                }
             //   RequestEmailVerifyCode();
                ReqVerifyCode(UserEmailAccount,"3",UserEmailCode,false);
                break;
            case R.id.tv_phone_create:
                edEmailCreate.requestFocus();
                llOne.setVisibility(View.VISIBLE);
                llEmailCreate.setVisibility(View.GONE);
                break;
            case R.id.ll_email_create:
                break;
        }
    }

    private void ReqVerifyCode(String verifyUser,String verifyCodeType,String VerifyCode,boolean isphone){

        addSubscribe(verifyCodeAndEMailProviderServices.requestVerifyCode(new VerifyCodeReqParam(verifyUser,verifyCodeType,VerifyCode))
                .subscribeWith(new RxSubscriber<Object>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if(ex.getErrorCode().equals("102")){
                            ToastUtil.show(getString(R.string.code_error));
                        }else{
                            ToastUtil.show(getString(R.string.server_connection_error));

                        }
                    }

                    @Override
                    public void onSuccess(Object o) {
                        Intent intent = new Intent(MoneyCreateWalletActivity.this, SetFindPassActivity.class);
                        Bundle bundle = new Bundle();
                        if(isphone){
                            bundle.putString("phoneCode", UserPhoneCode);
                            bundle.putString("phoneNum", UserPhoneAccount);
                            intent.putExtra("PhoneCreate", bundle);
                        }else{
                            bundle.putString("EmailCode", UserEmailAccount);
                            bundle.putString("EmailNum", UserEmailCode);
                            intent.putExtra("EmailCreate", bundle);
                        }

                        navigateUpTo(intent);
                    }
                }));


    }

    private void RequestEmailCode(String language) {
        addSubscribe(verifyCodeAndEMailProviderServices.requestEmailCode(new SendEmailCodeReqParam(UserEmailAccount,language,"3")).subscribeWith(
                new RxSubscriber<Object>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        tvEmailGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                        if(ex.getErrorCode().equals("0")){
                            ToastUtil.show(getString(R.string.send_error));
                        }else{
                            ToastUtil.show(getString(R.string.server_connection_error));

                        }

                    }

                    @Override
                    public void onSuccess(Object o) {
                        dismissDialog();
                        mTimeEmailCountUtil.start();
                        ToastUtil.show(getString(R.string.send_to_email));

                    }
                }
        ));
    }

    private void RequestPhoneCode(String language) {

        addSubscribe(verifyCodeAndEMailProviderServices.requestPhoneCode(new SendPhoneCodeReqParam(UserPhoneAccount,"3",language))
        .subscribeWith(new RxSubscriber<Object>() {
            @Override
            protected void onStart() {
                super.onStart();
                showDialog();
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                dismissDialog();
                tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                if(ex.getErrorCode().equals("0")){
                    ToastUtil.show(getString(R.string.send_error));
                }else{
                    ToastUtil.show(getString(R.string.server_connection_error));

                }

            }

            @Override
            public void onSuccess(Object o) {
                dismissDialog();
                mTimeCountUtil.start();
                ToastUtil.show(getString(R.string.send_successed));

            }
        }));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnPhoneNextStep.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnPhoneNextStep.setBackgroundResource(R.drawable.item_gray_black);
        btnEmailNextStep.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnEmailNextStep.setBackgroundResource(R.drawable.item_gray_black);
        btnPhoneNextStep.setTextColor(getResources().getColor(R.color.gray_90));
        btnEmailNextStep.setTextColor(getResources().getColor(R.color.gray_90));
        //tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
        //tvEmailGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (edPhoneNum.getText().toString().length() >= 3) {
//            if (!TextUtils.isEmpty(tvCountryName.getText().toString())) {
//                tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
//            }
//        }
//        if (edEmailCreate.getText().toString().length() >= 3) {
//            tvEmailGetCode.setTextColor(getResources().getColor(R.color.tian_tip_blue));
//        }
//
//        if (!TextUtils.isEmpty(edPhoneCode.getText().toString())||!TextUtils.isEmpty(edEmailCode.getText().toString())){
//            tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
//            tvEmailGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
//        }
//
//        if (edPhoneCode.hasFocus()){
//            tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
//        }
//
//        if (edEmailCode.hasFocus()){
//            tvEmailGetCode.setTextColor(getResources().getColor(R.color.tip_gray));
//        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        //tvPhoneGetCode.setTextColor(getResources().getColor(R.color.tipper_blue_color));
        //tvEmailGetCode.setTextColor(getResources().getColor(R.color.tipper_blue_color));
        if (!(edPhoneNum.getText().toString().equals("") || edPhoneCode.getText().toString().equals(""))&&edPhoneCode.getText().toString().length()==5) {
            btnPhoneNextStep.setEnabled(true);
            btnPhoneNextStep.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
            btnPhoneNextStep.setTextColor(getResources().getColor(R.color.white));
        }
        if (!(edEmailCreate.getText().toString().equals("") || edEmailCode.getText().toString().equals(""))&&edEmailCode.getText().toString().length()==5) {
            btnEmailNextStep.setEnabled(true);
            btnEmailNextStep.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
            btnEmailNextStep.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Subscriber
    public void subCountryCodeEvent(CountryCodeBean.DataBeanX.DataBean dataBean) {
        tvCountryName.setText(dataBean.getName());
        tvAreaNum = dataBean.getAreacode();
        tvCountryNum.setText(tvAreaNum);
        edPhoneNum.requestFocus();
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    @Subscriber
    public void onPossInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 11) {
                if (!TextUtils.isEmpty(edPhoneNum.getText().toString())){
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

    @Subscriber
    public void onMoneyInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 3333) {
                MoneyCreateWalletActivity.this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
