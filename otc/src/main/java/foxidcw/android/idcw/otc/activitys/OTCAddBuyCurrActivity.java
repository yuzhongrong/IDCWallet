package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.BaseView;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;

/**
 * Created by hpz on 2018/4/21.
 */

@Route(path = OTCConstant.ADDCHANGEXCURR, name = "添加承兑换币")
public class OTCAddBuyCurrActivity extends BaseWalletActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_buy_curr;
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
}
