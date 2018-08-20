package com.idcg.idcw.activitys;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.cjwsc.idcm.base.BaseView;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import foxidcw.android.idcw.common.constant.ArouterConstants;

/**
 * Created by hpz on 2018/3/30.
 */

@Route(path = ArouterConstants.OLDRECOVERYSUCCESS, name = "旧用户备份助记词成功的页面")
public class OldRecoverySuccessActivity extends BaseWalletActivity {
    @BindView(R.id.btn_back_finish)
    Button btnBackFinish;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_old_recovery_success;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick(R.id.btn_back_finish)
    public void onViewClicked() {
        EventBus.getDefault().post(new PosInfo(601));
        this.finish();
    }
    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
