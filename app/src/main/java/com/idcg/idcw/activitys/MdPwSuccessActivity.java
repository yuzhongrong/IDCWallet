package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 16:18
 **/
@Route(path = ArouterConstants.MODIFY_PAY_PASS_SUCCESS, name = "修改钱包密码确认")
public class MdPwSuccessActivity extends BaseWalletActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.btn_restart_login)
    Button btnRestartLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_md_pw_success;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        tvSetName.setText(R.string.edit_wallet_pass);
        imgBack.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick({R.id.img_back, R.id.btn_restart_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                break;
            case R.id.btn_restart_login:
                LoginStatus bean = LoginUtils.getLoginBean(this);
                ACacheUtil.get(this).remove(bean.getId() + AcacheKeys.cache1);
                ACacheUtil.get(this).remove(AcacheKeys.loginbean);
                AppManager.getInstance().finishAllActivity();
                navigation(ArouterConstants.WALLET_CREATE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}
