package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.model.bean.PositionBean;
import com.cjwsc.idcm.base.BaseView;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 15:10
 **/
@Route(path = ArouterConstants.SET_PAY_PW_SUCCESS, name = "设置pin成功界面")
public class SetPwSuccessActivity extends BaseWalletActivity {

    @BindView(R.id.tv_set_Name)
    TextView tvSetName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pw_success;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        EventBus.getDefault().post(new PositionBean(8));
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @OnClick(R.id.btn_finish)
    public void onViewClicked() {
        this.finish();
    }
}
