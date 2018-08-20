package com.idcg.idcw.activitys;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.widget.InPutEditText;
import com.cjwsc.idcm.base.BaseView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2018/3/27.
 */

@Route(path = ArouterConstants.registerWallet, name = "创建钱包新页面")
public class RegisterWalletActivity extends BaseWalletActivity {
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_wallet_name)
    InPutEditText edWalletName;
    @BindView(R.id.ed_wallet_pass)
    InPutEditText edWalletPass;
    @BindView(R.id.img_invisible_pass)
    ImageView imgInvisiblePass;
    @BindView(R.id.ed_sure_pass)
    InPutEditText edSurePass;
    @BindView(R.id.img_invisible_sure_pass)
    ImageView imgInvisibleSurePass;
    @BindView(R.id.btn_register_wallet)
    Button btnRegisterWallet;
    @BindView(R.id.tv_service_item)
    TextView tvServiceItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_wallet;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tvSetName.setText(getString(R.string.create_wallet));
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick({R.id.mr_back_layout, R.id.img_invisible_pass, R.id.img_invisible_sure_pass, R.id.btn_register_wallet, R.id.tv_service_item,R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.img_invisible_pass:
                break;
            case R.id.img_invisible_sure_pass:
                break;
            case R.id.btn_register_wallet:
                break;
            case R.id.tv_service_item:
                navigation(ArouterConstants.serviceItem);
                break;
            case R.id.tv_right:
                navigation(ArouterConstants.loginmywallet);
                break;
        }
    }
}
