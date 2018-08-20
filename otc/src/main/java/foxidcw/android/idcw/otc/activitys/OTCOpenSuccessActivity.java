package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.model.params.OTCCurrentStepParams;

/**
 * Created by hpz on 2018/5/4.
 */

@Route(path = OTCConstant.DEPOSITSUCCESS, name = "申请承兑商充值保证金页面")
public class OTCOpenSuccessActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    private TextView mTvSetName;
    private ImageView mImgOneStep;
    private View mViewOneStep;
    private ImageView mImgTwoStep;
    private View mViewTwoStep;
    private ImageView mImgThreeStep;
    private View mViewThreeStep;
    private ImageView mImgFourStep;
    private TextView mTvTwoSellText;
    private TextView mTvThreeSellText;
    private TextView mTvFourSellText;
    private TextView mBtnAcceptRechargeDeposit;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_success;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mImgOneStep = (ImageView) findViewById(R.id.img_one_step);
        mViewOneStep = (View) findViewById(R.id.view_one_step);
        mImgTwoStep = (ImageView) findViewById(R.id.img_two_step);
        mViewTwoStep = (View) findViewById(R.id.view_two_step);
        mImgThreeStep = (ImageView) findViewById(R.id.img_three_step);
        mViewThreeStep = (View) findViewById(R.id.view_three_step);
        mImgFourStep = (ImageView) findViewById(R.id.img_four_step);
        mTvTwoSellText = (TextView) findViewById(R.id.tv_two_sell_text);
        mTvThreeSellText = (TextView) findViewById(R.id.tv_three_sell_text);
        mTvFourSellText = (TextView) findViewById(R.id.tv_four_sell_text);
        mBtnAcceptRechargeDeposit = (TextView) findViewById(R.id.btn_accept_recharge_deposit);
        mBtnAcceptRechargeDeposit.setOnClickListener(this);
        mMrBackLayout.setOnClickListener(this);
        mMrBackLayout.setVisibility(View.GONE);
        mTvSetName.setText(getString(R.string.str_otc_apply_assurer));
        mViewOneStep.setBackgroundColor(getResources().getColor(R.color.tipper_blue_color));
        mViewTwoStep.setBackgroundColor(getResources().getColor(R.color.tipper_blue_color));
        mImgTwoStep.setImageResource(R.mipmap.otc_icon_two_select);
        mImgThreeStep.setImageResource(R.mipmap.otc_icon_three_select);
        mTvTwoSellText.setTextColor(getResources().getColor(R.color.tipper_blue_color));
        mTvThreeSellText.setTextColor(getResources().getColor(R.color.tipper_blue_color));
        mViewThreeStep.setBackgroundColor(getResources().getColor(R.color.tipper_blue_color));
        mImgFourStep.setImageResource(R.mipmap.otc_icon_four_select);
        mTvFourSellText.setTextColor(getResources().getColor(R.color.tipper_blue_color));
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
        } else if (i == R.id.btn_accept_recharge_deposit) {
            EventBus.getDefault().post(new PosInfo(166));
            finish();
        } else {
        }
    }

    private void requestSetFourthStep() {
        OTCCurrentStepParams otcCurrentStepParams = new OTCCurrentStepParams(4);
        otcAddBuyCurrProviderServices.requestSetCurrentStepProvider(otcCurrentStepParams)
                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mCtx, true) {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
