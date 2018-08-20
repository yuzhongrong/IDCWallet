package com.idcg.idcw.activitys;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
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
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.CheckOriginalPwdProviderServices;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/17 19:14
 **/
@Route(path = ArouterConstants.MODIFY_PAY_PASS, name = "修改钱包密码")
public class ModifyPayPassActivity extends BaseWalletActivity
        implements TextWatcher {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.ed_modify_pass)
    InPutEditText edModifyPass;
    @BindView(R.id.img_invisible_pass)
    ImageView imgInvisiblePass;
    @BindView(R.id.btn_sure_pass)
    Button btnSurePass;
    private boolean flag = true;

    @Autowired
    CheckOriginalPwdProviderServices mCheckOriginalPwdServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pass;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        tvSetName.setText(R.string.edit_wallet_pass);
        edModifyPass.addTextChangedListener(this);
        btnSurePass.setEnabled(false);//初始化按钮不能点击
        edModifyPass.setFilters(new InputFilter[]{filter});
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnSurePass.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnSurePass.setBackgroundResource(R.drawable.item_gray_black);
        btnSurePass.setTextColor(getResources().getColor(R.color.gray_90));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edModifyPass.getText().toString().equals("")) && edModifyPass.getText().toString().length() >= 6) {
            btnSurePass.setEnabled(true);
            btnSurePass.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
            btnSurePass.setTextColor(getResources().getColor(R.color.white));
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

    @OnClick({R.id.mr_back_layout, R.id.img_invisible_pass, R.id.btn_sure_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.img_invisible_pass:
                if (flag == false) {
                    edModifyPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    imgInvisiblePass.setImageResource(R.mipmap.img_pass_invisible);
                    edModifyPass.setSelection(edModifyPass.getText().toString().length());
                } else {
                    edModifyPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    imgInvisiblePass.setImageResource(R.mipmap.img_pass_visible);
                    edModifyPass.setSelection(edModifyPass.getText().toString().length());
                }
                break;
            case R.id.btn_sure_pass:
                if (edModifyPass.getText().toString().length() < 6) {
                    ToastUtil.show(getString(R.string.pass_length_not_enougth));
                    return;
                }
                LoginStatus bean1 = LoginUtils.getLoginBean(mCtx);
                CheckOriginalPwdReqParam reqParam = new CheckOriginalPwdReqParam("0",edModifyPass.getText().toString().trim(),bean1.getDevice_id(),true);
                addSubscribe(mCheckOriginalPwdServices.requestOldCheckOriginalPwdProvider(reqParam)
                        .subscribeWith(new RxSubscriber<Boolean>() {
                            @Override
                            public void onSuccess(Boolean checkNewPinBean) {
                                Bundle bundle = new Bundle();
                                bundle.putString("pass", edModifyPass.getText().toString().trim());
                                ARouter.getInstance().build(ArouterConstants.MODIFY_PAY_PASS_SURE)
                                        .withBundle("password", bundle)
                                        .navigation();
                            }

                            @Override
                            protected void onError(ResponseThrowable ex) {
                                if (ex.getErrorCode().equals("109")) {
                                    ToastUtil.show(getString(R.string.current_pass_error));
                                } else {
                                    ToastUtil.show(getString(R.string.server_connection_error));
                                }
                            }
                        }));
                break;
        }
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
