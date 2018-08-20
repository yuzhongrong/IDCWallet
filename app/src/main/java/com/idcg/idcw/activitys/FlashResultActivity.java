package com.idcg.idcw.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;

import com.idcg.idcw.model.params.ExchangeInParam;
import com.idcg.idcw.utils.Utils;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.event.BiBiRecordRefreshEvent;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ArouterConstants.FlashResult, name = "闪兑结果页面")
public class FlashResultActivity extends BaseWalletActivity {

    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.mr_back_layout)
    LinearLayout mMrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView mTvSetName;
    @BindView(R.id.img_code)
    ImageView mImgCode;
    @BindView(R.id.title_bar)
    RelativeLayout mTitleBar;
    @BindView(R.id.iv_type_out)
    ImageView mIvTypeOut;
    @BindView(R.id.tv_type_out)
    TextView mTvTypeOut;
    @BindView(R.id.iv_type_in)
    ImageView mIvTypeIn;
    @BindView(R.id.tv_type_in)
    TextView mTvTypeIn;
    @BindView(R.id.tv_amount_out)
    TextView mTvAmountOut;
    @BindView(R.id.tv_amount_in)
    TextView mTvAmountIn;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    @Autowired
    ExchangeInParam mExchangeInParam;
    @Autowired(name = "digit")
    int digit;
    @Autowired(name = "toDigit")
    int toDigit;

    @BindView(R.id.tv_status)
    TextView mTvStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flash_result;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mTvSetName.setText(R.string.exchange_result);
        mMrBackLayout.setVisibility(View.INVISIBLE);

        mTvAmountOut.setText(Utils.toSubStringNo(mExchangeInParam.getAmount(), digit)+" "+mExchangeInParam.getCurrency().toUpperCase());
        mTvAmountIn.setText(Utils.toSubStringNo(mExchangeInParam.getToAmount(), toDigit)+" "+mExchangeInParam.getToCurrency().toUpperCase());
        mTvTypeOut.setText(mExchangeInParam.getCurrency().toUpperCase());
        mTvTypeIn.setText(mExchangeInParam.getToCurrency().toUpperCase());
        String exchange_logo = ACacheUtil.get(mCtx).getAsString("exchange_logo");
        String exchange_to_logo = ACacheUtil.get(mCtx).getAsString("exchange_to_logo");
        GlideUtil.loadImageView(mCtx, exchange_logo, mIvTypeOut);
        GlideUtil.loadImageView(mCtx, exchange_to_logo, mIvTypeIn);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }


    @OnClick({R.id.mr_back_layout, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
            case R.id.btn_confirm:
                finish();
                EventBus.getDefault().post(new BiBiRecordRefreshEvent());
                break;
        }
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
