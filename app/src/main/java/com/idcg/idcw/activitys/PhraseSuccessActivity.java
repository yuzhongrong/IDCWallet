package com.idcg.idcw.activitys;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.cjwsc.idcm.base.BaseView;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import foxidcw.android.idcw.common.constant.ArouterConstants;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 14:08
 **/
@Route(path = ArouterConstants.PHRASE_SUCCESS, name = "短语备份成功界面")
public class PhraseSuccessActivity extends BaseWalletActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phrase_success;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tvSetName.setText(getString(R.string.str_back_phrase));
        imgBack.setVisibility(View.INVISIBLE);
        tvRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick({R.id.btn_again_back, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_again_back:
                boolean mIsPhrase = true;
                EventBus.getDefault().post(new PosInfo(603));
                ARouter.getInstance().build(ArouterConstants.REMPHRASE)
                        .withBoolean("mIsPhrase", mIsPhrase).navigation(mCtx);
                finish();
                break;
            case R.id.btn_finish:
                EventBus.getDefault().post(new PosInfo(603));
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
