package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import com.cjwsc.idcm.base.BaseView;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.constant.ArouterConstants;

/**
 * Created by hpz on 2018/4/9.
 */

@Route(path = ArouterConstants.DEVICEINTERCEPTOR, name = "多设备拦截页面")
public class DeviceInterceptorActivity extends BaseWalletActivity {
    @BindView(R.id.tv_device_content)
    TextView tvDeviceContent;
    @BindView(R.id.btn_back_close)
    Button btnBackClose;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_interceptor;
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

    @Override
    public void onBackPressed() {
        return;
    }

    @OnClick(R.id.btn_back_close)
    public void onViewClicked() {
        this.finish();
    }
}
