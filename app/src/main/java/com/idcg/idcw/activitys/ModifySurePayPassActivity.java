package com.idcg.idcw.activitys;

import android.content.Context;
import android.content.Intent;
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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.model.logic.ModifySurePayPassLogic;
import com.idcg.idcw.model.params.ModifyPassReqParam;
import com.idcg.idcw.presenter.impl.ModifySurePayPassImpl;
import com.idcg.idcw.presenter.interfaces.ModifySurePayPassContract;
import foxidcw.android.idcw.common.utils.StringUtils;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 16:08
 **/
@Route(path = ArouterConstants.MODIFY_PAY_PASS_SURE, name = "修改钱包密码确认")
public class ModifySurePayPassActivity extends
        BaseWalletActivity<ModifySurePayPassLogic, ModifySurePayPassImpl>
        implements TextWatcher, ModifySurePayPassContract.View {
    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.ed_modify_new_pass)
    InPutEditText edModifyNewPass;
    @BindView(R.id.img_invisible_pass_one)
    ImageView imgInvisiblePassOne;
    @BindView(R.id.ed_modify_sure_pass)
    InPutEditText edModifySurePass;
    @BindView(R.id.img_invisible_pass_two)
    ImageView imgInvisiblePassTwo;
    @BindView(R.id.btn_sure_pass)
    Button btnSurePass;
    private boolean flag = true;
    private String value;
    private String pass;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_sure_pass;
    }

    @Override
    protected void onInitView(Bundle bundle1) {
        try {
            tvSetName.setText(R.string.edit_wallet_pass);
            edModifyNewPass.addTextChangedListener(this);
            edModifySurePass.addTextChangedListener(this);
            btnSurePass.setEnabled(false);//初始化按钮不能点击
            edModifySurePass.setFilters(new InputFilter[]{filter});
            edModifyNewPass.setFilters(new InputFilter[]{filter});
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("password");
            pass = bundle.getString("pass");
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

    @OnClick({R.id.img_invisible_pass_one, R.id.img_invisible_pass_two, R.id.btn_sure_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_invisible_pass_one:
                if (flag == false) {
                    edModifyNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    imgInvisiblePassOne.setImageResource(R.mipmap.img_pass_invisible);
                    edModifyNewPass.setSelection(edModifyNewPass.getText().toString().length());
                } else {
                    edModifyNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    imgInvisiblePassOne.setImageResource(R.mipmap.img_pass_visible);
                    edModifyNewPass.setSelection(edModifyNewPass.getText().toString().length());
                }
                break;
            case R.id.img_invisible_pass_two:
                if (flag == false) {
                    edModifySurePass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                    imgInvisiblePassTwo.setImageResource(R.mipmap.img_pass_invisible);
                    edModifySurePass.setSelection(edModifySurePass.getText().toString().length());
                } else {
                    edModifySurePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                    imgInvisiblePassTwo.setImageResource(R.mipmap.img_pass_visible);
                    edModifySurePass.setSelection(edModifySurePass.getText().toString().length());
                }
                break;
            case R.id.btn_sure_pass:
                if (edModifyNewPass.getText().toString().length() < 6 || edModifySurePass.getText().toString().length() < 6) {
                    ToastUtil.show(getString(R.string.password_length_not_enougth));
                    return;
                }
                if (!StringUtils.isPass(edModifySurePass.getText().toString())) {
                    ToastUtil.show(getString(R.string.pass_abc_num));
                    return;
                }
                LoginStatus bean = LoginUtils.getLoginBean(this);
                if (bean != null) {
                    showDialog();
                    ModifyPassReqParam reqParam = new ModifyPassReqParam();
                    reqParam.setCurrentPassword(pass);
                    reqParam.setNewPassword(edModifyNewPass.getText().toString().trim());
                    reqParam.setRePassword(edModifySurePass.getText().toString().trim());
                    reqParam.setConfirmCode("");
                    reqParam.setUserName("");
                    reqParam.setVerifyUser(bean.getPosMain() == 0 ? bean.getTelphone() : bean.getEmail());
                    mPresenter.ReqModifyPass(reqParam);
                }
                break;
        }
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
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
        if (!(edModifyNewPass.getText().toString().equals("") || edModifySurePass.getText().toString().equals("")) && edModifySurePass.getText().toString().length() >= 6 && edModifyNewPass.getText().toString().length() >= 6) {
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

    @OnClick(R.id.mr_back_layout)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        switch (throwable.getErrorCode()) {
            case "108":
                ToastUtil.show(getString(R.string.pass_not_match));
                break;
            case "114":
                ToastUtil.show(getString(R.string.new_pass_not_same_or));
                break;
            default:
                ToastUtil.show(getString(R.string.server_connection_error));
                break;
        }
    }

    @Override
    public void updateReqModifyPass(String result) {
        dismissDialog();
        navigation(ArouterConstants.MODIFY_PAY_PASS_SUCCESS);
        finish();
    }
}
