package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 15:48
 **/
@Route(path = ArouterConstants.FIND_PIN_SUCCESS, name = "找回pin成功")
public class FindPinSuccessActivity extends BaseWalletActivity {

    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.btn_finish)
    Button btnFinish;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_pin_success;
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

    @OnClick({R.id.tv_set_Name, R.id.btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_set_Name:
                break;
            case R.id.btn_finish:
                this.finish();
                break;
        }
    }
}
