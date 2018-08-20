package com.idcg.idcw.activitys;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;

import foxidcw.android.idcw.common.iprovider.CheckVersionProviderServices;
import foxidcw.android.idcw.common.model.bean.CheckAppVersionBean;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.StringUtils;
import com.idcg.idcw.utils.UIUtils;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.foxcommon.Constants.Constants;

/**
 * Created by hpz on 2017/12/18.
 */
@Route(path = ArouterConstants.WALLET_CREATE,name = "钱包创建页面")
public class GuideActivity extends BaseWalletActivity {
    @BindView(R.id.btn_login_wallet)
    Button btnLoginWallet;
    @BindView(R.id.btn_create_wallet)
    Button btnCreateWallet;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ll_page)
    RelativeLayout llPage;
    private Dialog dialogActivity;
    private View inflaterExit;
    private TextView tv_new_version;
    private String mLanguagePosition;
    private Resources mResources;
    private Configuration mConfig;
    private DisplayMetrics mDm;
    private static String mLanguageCode;
    private static String mServerCode;

    @Autowired
    CheckVersionProviderServices checkVersionProviderServices;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        RequestCheckVersion();
        LoginStatus loginStatus = (LoginStatus) ACacheUtil.get(this).getAsObject(AcacheKeys.loginbean);
        if (loginStatus != null) {
            if (!TextUtils.isEmpty(loginStatus.getTicket()) && loginStatus.isPayPasswordFlag()) {
                ARouter.getInstance().build(Constants.PIN_START).navigation();
            }
        }
    }

    @Override
    protected void onEvent() {
    }
    @Override
    protected BaseView getView() {
        return null;
    }
    private void RequestCheckVersion() {
        addSubscribe( checkVersionProviderServices.CheckVersionProvider().subscribeWith(new RxSubscriber<CheckAppVersionBean>() {
            @Override
            public void onSuccess(CheckAppVersionBean checkAppVersionBean) {

                if (checkAppVersionBean==null) return;
                if (!StringUtils.getVersionName(GuideActivity.this).equals(checkAppVersionBean.getLatestVersion())) {
                    dialogActivity = new Dialog(GuideActivity.this, R.style.shuweiDialog);
                    inflaterExit = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_base_check_dialog, null);
                    dialogActivity.setContentView(inflaterExit);
                    TextView tv_activity_cancel = (TextView) inflaterExit.findViewById(R.id.tv_activity_cancel);
                    Button tv_activity_update = (Button) inflaterExit.findViewById(R.id.tv_activity_update);
                    tv_activity_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogActivity.dismiss();
                        }
                    });
                    tv_activity_update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(checkAppVersionBean.getUpdateUrl());
                            intent.setData(content_url);
                            startActivity(intent);
                        }
                    });
                    Window dialogWindow = dialogActivity.getWindow();
                    dialogWindow.setGravity(Gravity.CENTER);
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setAttributes(lp);
                    dialogActivity.show();
                }

            }

            @Override
            protected void onError(ResponseThrowable ex) {
                UIUtils.post(() -> {
                    ToastUtil.show(getString(R.string.server_connection_error));
                });
            }
        }));
    }


    @OnClick({R.id.btn_login_wallet, R.id.btn_create_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_wallet:
                //ARouter.getInstance().build(ArouterConstants.LOGINWALLET).navigation();
                navigation(ArouterConstants.LOGINWALLET);//跳转登录新页面
                break;
            case R.id.btn_create_wallet:
                ARouter.getInstance().build(ArouterConstants.CREATEWALLET).navigation();//跳转新注册页面
                break;
        }
    }
    @Subscriber
    public void onPossInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 888) {
               finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }
    @Subscriber
    public void onGuideInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 31) {
               finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解决内存泄漏问题
            if(dialogActivity!=null)dialogActivity.dismiss();
        this.dialogActivity = null;
    }



}
