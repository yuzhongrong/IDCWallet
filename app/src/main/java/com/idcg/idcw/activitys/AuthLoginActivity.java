package com.idcg.idcw.activitys;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import foxidcw.android.idcw.common.base.BaseWalletActivity;

import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.idcg.idcw.R;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import foxidcw.android.idcw.foxcommon.Constants.Constants;
import foxidcw.android.idcw.foxcommon.provider.bean.ScanLoginBean;
import foxidcw.android.idcw.foxcommon.provider.params.ScanLoginReqParam;
import foxidcw.android.idcw.foxcommon.provider.services.ScanLoginProviderServices;

/**
 * Created by admin-2 on 2018/3/31.
 */
@Route(path = Constants.AUTHLOGIN)
public class AuthLoginActivity extends BaseWalletActivity {
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.rescan)
    TextView rescan;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.san_waring)
    TextView san_waring;


    @Autowired
    ScanLoginProviderServices scanLoginProviderServices;


    ScanLoginReqParam reqParam;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_authlogin_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ARouter.getInstance().inject(this);
        if(getIntent()!=null&&getIntent().getExtras()!=null){
            reqParam= (ScanLoginReqParam) getIntent().getExtras().get("reqparam");

        }
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }


    @OnClick({R.id.login, R.id.cancel,R.id.rescan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                LoginStatus bean = LoginUtils.getLoginBean(mCtx);
                if(reqParam!=null){
                    reqParam.setType(1);
                    reqParam.setDevice_Id(bean.getDevice_id());
                }
                reqScanLogin(reqParam);
                break;
            case R.id.cancel:
                finish();
                EventBus.getDefault().post(getResources().getString(R.string.str_authlogin_fail));
                break;

            case R.id.rescan:
                finish();
                ARouter.getInstance().build(Constants.SCANLOGIN)
                        .withBoolean("isfinish",false)
                        .navigation();
                break;
        }
    }

    //授权登录请求
    private void  reqScanLogin(final ScanLoginReqParam param){

        scanLoginProviderServices.requestScanLoginProvider(param)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(
                new RxProgressSubscriber<ScanLoginBean>(this) {
                    @Override
                    public void onSuccess(ScanLoginBean data) {
                        if(data==null)return;

                        if(data.getStatus().equals("3")){//二维码失效处理
                            upDateUI(true);
                        }else if(data.getStatus().equals("2")){//授权成功
                            finish();//关闭此页
                            EventBus.getDefault().post(data);
                        }//授权成功
                        else{
                            finish();//关闭此页
                            EventBus.getDefault().post(data);
                        }


                    }
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_error));
                        finish();//关闭此页
                    }
                }
        );


    }

    private void upDateUI(boolean refresh) {

            if(refresh){
                login.setText(R.string.str_scan_rescan);
                rescan.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                san_waring.setVisibility(View.VISIBLE);

            }else{
                login.setText(R.string.login_new);
                login.setVisibility(View.VISIBLE);
                rescan.setVisibility(View.GONE);
                san_waring.setVisibility(View.GONE);
            }

    }


    @Override
    public void onBackPressed() {
        finish();
        EventBus.getDefault().post(getResources().getString(R.string.str_authlogin_fail));
    }
}
