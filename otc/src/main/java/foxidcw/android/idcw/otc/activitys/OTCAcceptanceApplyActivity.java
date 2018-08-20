package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.BaseView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;

/**
 * Created by hpz on 2018/4/21.
 */

@Route(path = OTCConstant.APPLYACCEPT, name = "承兑商申请页面")
public class OTCAcceptanceApplyActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    /**
     * title
     */
    private TextView mTvSetName;
    private TextView mBtnAcceptApply;
    private TextView btn_test;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_acceptance_apply;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        EventBus.getDefault().register(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mBtnAcceptApply = (TextView) findViewById(R.id.btn_accept_apply);
        btn_test = (TextView) findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);
        mBtnAcceptApply.setOnClickListener(this);
        mMrBackLayout.setOnClickListener(this);

        mTvSetName.setText(getString(R.string.str_otc_set_assurer));
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            EventBus.getDefault().post(new PosInfo(166));
            this.finish();

        } else if (i == R.id.btn_accept_apply) {
            navigation(OTCConstant.APPLYACCEPTREAD);
        } else if (i == R.id.btn_test) {
            navigation(OTCConstant.DEPOSITBALANCE);
        }else {
        }
    }

    @Subscriber
    public void refreshBuyList(PosInfo posInfo) {
        if (posInfo==null)return;
        if (posInfo.getPos()==166){
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
