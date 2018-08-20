package com.idcg.idcw.activitys;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.model.logic.UserBackLogic;
import com.idcg.idcw.model.params.RequestCommonReqParam;
import com.idcg.idcw.presenter.impl.UserBackPresenterImpl;
import com.idcg.idcw.presenter.interfaces.UserBackContract;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.widget.ClearableEditText;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/17 16:14
 **/
@Route(path = ArouterConstants.USER_BACK,name = "用户反馈")
public class UserBackActivity extends BaseWalletActivity<UserBackLogic, UserBackPresenterImpl> implements TextWatcher, View.OnClickListener, UserBackContract.View {

    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.ed_free_back)
    AppCompatEditText edFreeBack;
    @BindView(R.id.tv_back_count)
    TextView tvBackCount;
    @BindView(R.id.ed_phone_email)
    ClearableEditText edPhoneEmail;
    @BindView(R.id.btn_commit)
    TextView btnCommit;

    private String value = "";
    private static final int MAX_NUMBER = 160;
    private View inflater;
    private Dialog dialog;
    private TextView tv_activity_update;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_back;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        tvSetName.setText(getString(R.string.user_callback));
        LoginStatus bean = LoginUtils.getLoginBean(this);
        if (bean != null) {
            value = bean.getTicket();
        }
        edPhoneEmail.setFilters(new InputFilter[]{filter});
        btnCommit.setEnabled(false);
        edFreeBack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                tvBackCount.setText("" + number);
            }
        });
        edPhoneEmail.addTextChangedListener(this);
        initUserCommitDialog();
    }

    private void initUserCommitDialog() {
        try {
            dialog = new Dialog(this, R.style.shuweiDialog);
            inflater = LayoutInflater.from(this).inflate(R.layout.activity_commit_dialog, null);
            dialog.setContentView(inflater);
            dialog.setCancelable(false);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
            tv_activity_update = (TextView) inflater.findViewById(R.id.tv_activity_update);
            tv_activity_update.setOnClickListener(this);
            //dialog.show();
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private int num = 160;
    private CharSequence wordNum;//记录输入的字数
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
        return this;
    }

    @OnClick({R.id.mr_back_layout, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.btn_commit:
                showDialog();
                RequestCommonReqParam reqParam = new RequestCommonReqParam();
                reqParam.setContent(edFreeBack.getText().toString());
                reqParam.setContact(edPhoneEmail.getText().toString());
                mPresenter.requestCommonToServer(reqParam);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnCommit.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnCommit.setTextColor(getResources().getColor(R.color.color_a0a2b1));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!(edFreeBack.getText().toString().equals("") || edPhoneEmail.getText().toString().equals(""))) {
            btnCommit.setEnabled(true);
            btnCommit.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_activity_update) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            this.finish();
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        ToastUtil.show(getString(R.string.server_connection_error));
    }

    @Override
    public void updateRequestCommonToServer(Object result) {
        dismissDialog();
        dialog.show();
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
