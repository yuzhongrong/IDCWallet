package com.idcg.idcw.activitys;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.cjwsc.idcm.base.BaseView;

/**
 * Created by hpz on 2018/3/28.
 */

@Route(path = ArouterConstants.LOGINCONFIRM, name = "登录确认弹出页面")
public class LoginConfirmActivity extends BaseWalletActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_confirm;
    }

    @Override
    protected void onInitView(Bundle bundle) {

    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }
}
