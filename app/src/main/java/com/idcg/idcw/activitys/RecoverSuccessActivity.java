package com.idcg.idcw.activitys;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import foxidcw.android.idcw.common.constant.ArouterConstants;

import com.idcg.idcw.iprovider.LoginWalletProviderServices;
import com.cjwsc.idcm.base.BaseView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2017/12/25.
 */
@Route(path = ArouterConstants.RECOVERSUCCESS, name = "找回资产页")
public class RecoverSuccessActivity extends BaseWalletActivity {
    @BindView(R.id.btn_enter_wallet)
    Button btnEnterWallet;
    @BindView(R.id.img_back)
    ImageView imgBack;

    @Autowired
    LoginWalletProviderServices loginWalletProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recover_success;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//当背景为白色的时候，顶部状态栏的字体显示为黑体
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        imgBack.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick(R.id.btn_enter_wallet)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("recovery", "recovery");
        ARouter.getInstance()
                .build(ArouterConstants.SETWALLETPIN)
                .withBundle("recoverySuccess", bundle)
                .navigation();
        RecoverSuccessActivity.this.finish();
    }

    //    private void RequestPhoneLogin(String account, int i) {
//        //登录过程中显示dialog
//
//        addSubscribe(loginWalletProviderServices.reqLogin(new LoginReqParam(account, pass)).subscribeWith(new RxSubscriber<LoginStatus>() {
//            @Override
//            protected void onStart() {
//                super.onStart();
//                showDialog();
//            }
//
//            @Override
//            protected void onError(ResponseThrowable ex) {
//
//                dismissDialog();
//                ToastUtil.show(getString(R.string.server_connection_error));
//            }
//
//            @Override
//            public void onSuccess(LoginStatus loginStatus) {
//                loginStatus.setPosMain(i);
//                ToastUtil.show(getString(R.string.login_success));
//                ACacheUtil.get(RecoverSuccessActivity.this).put(AcacheKeys.loginbean, loginStatus);
//                EventBus.getDefault().post(new PosInfo(31));
//                AppManager.getInstance().finishAllActivity();
//                navigation(ArouterConstants.MAIN);
//            }
//        }));
//    }
    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
