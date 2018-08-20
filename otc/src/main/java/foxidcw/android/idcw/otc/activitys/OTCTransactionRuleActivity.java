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
 *
 * @project name : FoxIDCW
 * @class name :   OTCTransactionRuleActivity
 * @package name : foxidcw.android.idcw.otc.activitys
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/4 11:35
 * @describe :     OTC买卖币规则
 *
 */
@Route(path = OTCConstant.TRANSACTION_RULE, name = "OTC买卖币规则")
public class OTCTransactionRuleActivity
        extends BaseWalletActivity
        implements View.OnClickListener
{

    private TextView mTvSetName;
    private LinearLayout mMrBackLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otc_transaction_rule;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        mTvSetName = findViewById(R.id.tv_set_Name);
        mMrBackLayout = findViewById(R.id.mr_back_layout);


        //标题
        mTvSetName.setText(getString(R.string.str_otc_transaction_rule));
    }

    @Override
    protected void onEvent() {
        mTvSetName.setOnClickListener(this);
        mMrBackLayout.setOnClickListener(this);
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.mr_back_layout) {
            this.finish();
        }
    }
}
