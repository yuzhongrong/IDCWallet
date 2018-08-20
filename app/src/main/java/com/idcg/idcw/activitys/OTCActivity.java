package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.cjwsc.idcm.base.BaseView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;

@Route(path = ArouterConstants.OTC, name = "币币闪兑界面")
public class OTCActivity extends BaseWalletActivity {

    @BindView(R.id.tv_set_Name)
    TextView mTvSetName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otc;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        mTvSetName.setText("OTC");
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.mr_back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                onBackPressed();
                break;
        }
    }
}
