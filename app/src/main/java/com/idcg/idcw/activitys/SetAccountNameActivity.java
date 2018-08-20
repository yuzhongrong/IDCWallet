package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.base.BaseView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/28 16:47
 **/
@Route(path = ArouterConstants.SET_ACCOUNT_NAME, name = "设置钱包名")
public class SetAccountNameActivity extends BaseWalletActivity {

    @BindView(R.id.tv_set_Name)
    TextView mTvTitle; // 界面标题

    @BindView(R.id.id_et_ac_account_set_name_input)
    InPutEditText mEtInputAccountName;

    @BindView(R.id.id_btn_ac_set_account_name_submit)
    Button mBtnSetAccountNameSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_account_name;
    }


    @Override
    protected void onInitView(Bundle bundle) {
        mTvTitle.setText(R.string.set_account_name);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        RxTextView.textChanges(mEtInputAccountName)
                .map(charSequence -> {
                    String inputName = String.valueOf(charSequence);
                    return !TextUtils.isEmpty(inputName)
                            && inputName.length() >= 4;
                }).subscribe(aBoolean -> mBtnSetAccountNameSubmit.setEnabled(aBoolean));
    }

    @OnClick({
            R.id.mr_back_layout,
            R.id.id_btn_ac_set_account_name_submit
    })
    public void setAccountNameClick(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                finish();
                break;
            case R.id.id_btn_ac_set_account_name_submit:
                break;
            default:
                break;
        }
    }

    @Override
    protected BaseView getView() {
        return null;
    }
}
