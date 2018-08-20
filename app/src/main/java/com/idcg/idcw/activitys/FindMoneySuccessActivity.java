package com.idcg.idcw.activitys;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.cjwsc.idcm.base.BaseView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2017/12/25.
 */
@Route(path = ArouterConstants.FINDMONEYSUCCESS)
public class FindMoneySuccessActivity extends BaseWalletActivity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.btn_create_new_wallet)
    Button btnCreateNewWallet;
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_money_success;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//当背景为白色的时候，顶部状态栏的字体显示为黑体
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



    @OnClick({R.id.img_back, R.id.btn_create_new_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.finish();
                break;
            case R.id.btn_create_new_wallet:

              //  intentFActivity(MoneyCreateWalletActivity.class);
                this.finish();
                break;
        }
    }
}
