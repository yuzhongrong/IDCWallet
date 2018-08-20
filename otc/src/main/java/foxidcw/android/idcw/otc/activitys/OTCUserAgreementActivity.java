package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.BaseView;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;

/**
 * Created by hpz on 2018/5/22.
 */

@Route(path = OTCConstant.USERAGREE, name = "发现的游戏协议页面")
public class OTCUserAgreementActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    /**
     * title
     */
    private TextView mTvSetName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_agree;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mMrBackLayout.setOnClickListener(this);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);

        mTvSetName.setText("用户协议");
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
            this.finish();
        } else {
        }
    }
}
