package com.idcg.idcw.activitys;

import android.os.Build;
import android.os.Bundle;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.cjwsc.idcm.base.BaseView;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.DialogVersionUtil;

/**
 * Created by hpz on 2018/3/28.
 */

@Route(path = ArouterConstants.MAINGUIDE, name = "新的引导页")
public class MainGuideActivity extends BaseWalletActivity {
    @BindView(R.id.tv_old_version_main)
    TextView tvOldVersionMain;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;
    @BindView(R.id.btn_phrase_recovery)
    TextView btnPhraseRecovery;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_guide;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    protected void onEvent() {
        if (dialogVersionUtil == null) {
            dialogVersionUtil = new DialogVersionUtil();
            dialogVersionUtil.checkVersion(this);
        }
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick({R.id.tv_old_version_main, R.id.btn_create_wallet, R.id.btn_phrase_recovery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_old_version_main:
                navigation(ArouterConstants.LOGINWALLET);//旧用户入口
                break;
            case R.id.btn_create_wallet:
                navigation(ArouterConstants.SETWALLETNAME);//创建新用户钱包
                break;
            case R.id.btn_phrase_recovery:
                navigation(ArouterConstants.RecoverAsset);//使用助记词恢复
                break;
        }
    }

    @Subscriber
    public void onMainGuideInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 602) {
                finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected boolean isCheckVersion() {
        //关闭后台版本更新
        return false;
    }

}
