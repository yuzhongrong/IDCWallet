package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.PosInfo;

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
 * 修改时间：2018/3/18 15:35
 **/
@Route(path = ArouterConstants.EDIT_PAY_PASS_SUCCESS, name = "修改密码成功")
public class EditPaySuccessActivity extends BaseWalletActivity {

    @BindView(R.id.tv_set_Name)
    TextView tvSetName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_pay_success;
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

    @OnClick(R.id.btn_success_finish)
    public void onViewClicked() {
        EventBus.getDefault().post(new PosInfo(10));
        this.finish();
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
