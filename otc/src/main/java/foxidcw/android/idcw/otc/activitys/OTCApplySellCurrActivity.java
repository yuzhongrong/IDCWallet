package foxidcw.android.idcw.otc.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.StringUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetBuyListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeSellBean;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.params.OTCCurrentStepParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;

/**
 * Created by hpz on 2018/5/2.
 */

@Route(path = OTCConstant.APPLYSELLCURR, name = "申请承兑商添加卖币")
public class OTCApplySellCurrActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    private TextView mTvSetName;
    private ImageView mImgOneStep;
    private View mViewOneStep;
    private ImageView mImgTwoStep;
    private View mViewTwoStep;
    private ImageView mImgThreeStep;
    private View mViewThreeStep;
    private ImageView mImgFourStep;
    private ImageView mImgOneAdd;
    private TextView mTvAddCurrHint;
    private RecyclerView mRvSellCurr;
    private ImageView mImgTwoAdd;
    private TextView mTvAddPayHint;
    private RecyclerView mRvSellOtc;
    private TextView mBtnAcceptChangeNextStep;
    private TextView mTvTwoSellText;
    private TextView mTvThreeSellText;
    private TextView mTvFourSellText;
    private int opened = -1;
    private CommonAdapter commonAdapterCurr;
    private CommonAdapter commonAdapterAmount;

    @Autowired
    OTCGetBuyListProviderServices otcGetSellListProviderServices;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_sell_curr;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        EventBus.getDefault().register(this);
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
        mImgOneAdd = (ImageView) findViewById(R.id.img_one_add);
        mTvAddCurrHint = (TextView) findViewById(R.id.tv_add_curr_hint);
        mRvSellCurr = (RecyclerView) findViewById(R.id.rv_sell_curr);
        mImgTwoAdd = (ImageView) findViewById(R.id.img_two_add);
        mTvAddPayHint = (TextView) findViewById(R.id.tv_add_pay_hint);
        mRvSellOtc = (RecyclerView) findViewById(R.id.rv_sell_otc);
        mBtnAcceptChangeNextStep = (TextView) findViewById(R.id.btn_accept_change_next_step);
        mTvTwoSellText = (TextView) findViewById(R.id.tv_two_sell_text);
        mTvThreeSellText = (TextView) findViewById(R.id.tv_three_sell_text);
        mTvFourSellText = (TextView) findViewById(R.id.tv_four_sell_text);
        mMrBackLayout.setOnClickListener(this);
        mImgOneAdd.setOnClickListener(this);
        mImgTwoAdd.setOnClickListener(this);
        mBtnAcceptChangeNextStep.setOnClickListener(this);
        mTvSetName.setText(getString(R.string.str_otc_apply_assurer));
        mViewOneStep.setBackgroundColor(getResources().getColor(R.color.tipper_blue_color));
        mImgTwoStep.setImageResource(R.mipmap.otc_icon_two_select);
        mTvTwoSellText.setTextColor(getResources().getColor(R.color.tipper_blue_color));
        mBtnAcceptChangeNextStep.setEnabled(false);

        initCurrRecycler();//加载承兑卖币的币种列表
        initAmountRecycler();//加载承兑卖币的资金量列表
    }

    private void initCurrRecycler() {
        commonAdapterCurr = new CommonAdapter<OTCGetExchangeSellBean.ExchangeCoinListBean>(R.layout.item_add_apply_curr) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCGetExchangeSellBean.ExchangeCoinListBean item) {
                helper.setVisible(R.id.view_diver, helper.getAdapterPosition()!=getItemCount()-1);
                ((TextView) helper.getView(R.id.str_item_curr_name)).setText(StringUtils.handlerNull(item.getCoinCode()).toUpperCase());
                ((TextView) helper.getView(R.id.str_item_curr_up)).setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getMin(),4)) + "-" + StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getMax(),4)) + "");
                if (item.getPremium()>0){
                    ((TextView) helper.getView(R.id.str_item_curr_down)).setText("+"+StringUtils.formatChangeOne(item.getPremium()*100) + "%");
                }else {
                    ((TextView) helper.getView(R.id.str_item_curr_down)).setText(StringUtils.formatChangeOne(item.getPremium()*100) + "%");
                }

                if (helper.getLayoutPosition() == opened) {
                    ((LinearLayout) helper.getView(R.id.ll_item_dialog_select)).setVisibility(View.VISIBLE);
                    ((LinearLayout) helper.getView(R.id.accept_buy_or_sell)).setBackgroundResource(R.color.c_F7FAFE);
                } else {
                    ((LinearLayout) helper.getView(R.id.ll_item_dialog_select)).setVisibility(View.GONE);
                    ((LinearLayout) helper.getView(R.id.accept_buy_or_sell)).setBackgroundResource(R.color.white);
                }

                ((TextView) helper.getView(R.id.item_apply_curr_edit)).setOnClickListener(v -> {//点击item的编辑按钮
                    ARouter.getInstance().build(OTCConstant.SELECTCURR)
                            .withInt("buyOrSell", 4)
                            .withInt("buyOrSellId", item.getId())
                            .withInt("buyOrSellCoinId", item.getCoinId())
                            .withString("buyOrSellCoinCode", item.getCoinCode())
                            .withDouble("Max",item.getMax())
                            .withDouble("Min",item.getMin())
                            .withDouble("Premium",item.getPremium())
                            .navigation(OTCApplySellCurrActivity.this, 12);
                });

                ((TextView) helper.getView(R.id.item_apply_curr_delete)).setOnClickListener(v -> {
                    OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mCtx);
                    OTCDeleteCurrPayPopWindow.showPopupWindow();
                    OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {//点击删除按钮
                        OTCRemoveBean otcRemoveBean = new OTCRemoveBean(item.getId());
                        otcAddBuyCurrProviderServices.requesRemoveProvider(otcRemoveBean)
                                .compose(bindToLifecycle())
                                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mCtx, true) {
                                    @Override
                                    public void onSuccess(Object data) {
                                        OTCDeleteCurrPayPopWindow.dismiss();
                                        //requestSellList(true);//网络请求数据
                                        opened = -1;
                                        commonAdapterCurr.remove(helper.getLayoutPosition());
                                        commonAdapterCurr.notifyItemChanged(helper.getLayoutPosition());
                                    }

                                    @Override
                                    protected void onError(ResponseThrowable ex) {

                                    }
                                });
                    });
                });

                ((ImageView) helper.getView(R.id.img_curr_more)).setOnClickListener(v -> {
                    if (opened == helper.getLayoutPosition()) {
                        //当点击的item已经被展开了, 就关闭.
                        opened = -1;
                        notifyItemChanged(helper.getLayoutPosition());
                    } else {
                        int oldOpened = opened;
                        opened = helper.getLayoutPosition();
                        notifyItemChanged(oldOpened);
                        notifyItemChanged(opened);
                    }

                });


                helper.getConvertView().setOnClickListener(v -> {
                    if (opened == helper.getLayoutPosition()) {
                        //当点击的item已经被展开了, 就关闭.
                        opened = -1;
                        notifyItemChanged(helper.getLayoutPosition());
                    }
                });
            }
        };

        LinearLayoutManager linearLayoutManagerPay = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvSellCurr.setLayoutManager(linearLayoutManagerPay);
        mRvSellCurr.setAdapter(commonAdapterCurr);
        commonAdapterCurr.setEmptyView(R.layout.recyclerview_otc_curr_empty_layout, mRvSellCurr);//没有数据显示的布局
    }

    private void initAmountRecycler() {
        commonAdapterAmount = new CommonAdapter<OTCGetExchangeSellBean.ExchangePayListBean>(R.layout.item_add_sell_curr) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCGetExchangeSellBean.ExchangePayListBean item) {
                helper.setVisible(R.id.view_diver, helper.getAdapterPosition()!=getItemCount()-1);
                ((TextView) helper.getView(R.id.str_item_curr_name)).setText(item.getLocalCurrencyCode().toUpperCase());
                ((TextView) helper.getView(R.id.str_item_curr_up)).setText(Utils.toSubStringDegist(item.getAmount(), 2));

                if (helper.getLayoutPosition() == opened) {
                    ((LinearLayout) helper.getView(R.id.ll_item_dialog_select)).setVisibility(View.VISIBLE);
                    ((LinearLayout) helper.getView(R.id.accept_buy_or_sell)).setBackgroundResource(R.color.c_F7FAFE);
                } else {
                    ((LinearLayout) helper.getView(R.id.ll_item_dialog_select)).setVisibility(View.GONE);
                    ((LinearLayout) helper.getView(R.id.accept_buy_or_sell)).setBackgroundResource(R.color.white);
                }

                ((TextView) helper.getView(R.id.item_apply_curr_edit)).setOnClickListener(v -> {//点击编辑按钮
                    ARouter.getInstance().build(OTCConstant.LEGALTENDER)
                            .withInt("legal", 2)
                            .withString("legalId", String.valueOf(item.getId()))
                            .withInt("legalCodeId", item.getLocalCurrencyId())
                            .withDouble("Amount",item.getAmount())
                            .navigation(OTCApplySellCurrActivity.this, 12);
                });

                ((TextView) helper.getView(R.id.item_apply_curr_delete)).setOnClickListener(v -> {
                    OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mCtx);
                    OTCDeleteCurrPayPopWindow.showPopupWindow();
                    OTCDeleteCurrPayPopWindow.setTitle(getString(R.string.str_otc_assener_curr_amount));
                    OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {//点击删除按钮
                        OTCRemoveBean otcRemoveBean = new OTCRemoveBean(item.getId());
                        otcAddBuyCurrProviderServices.requesRemoveLegalProvider(otcRemoveBean)
                                .compose(bindToLifecycle())
                                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mCtx, true) {
                                    @Override
                                    public void onSuccess(Object data) {
                                        OTCDeleteCurrPayPopWindow.dismiss();
                                        //requestSellList(true);//网络请求数据
                                        opened = -1;
                                        commonAdapterAmount.remove(helper.getLayoutPosition());
                                        commonAdapterAmount.notifyItemChanged(helper.getLayoutPosition());
                                    }

                                    @Override
                                    protected void onError(ResponseThrowable ex) {

                                    }
                                });
                    });
                });

                ((ImageView) helper.getView(R.id.img_curr_more)).setOnClickListener(v -> {
                    if (opened == helper.getLayoutPosition()) {
                        //当点击的item已经被展开了, 就关闭.
                        opened = -1;
                        notifyItemChanged(helper.getLayoutPosition());
                    } else {
                        int oldOpened = opened;
                        opened = helper.getLayoutPosition();
                        notifyItemChanged(oldOpened);
                        notifyItemChanged(opened);
                    }

                });

                helper.getConvertView().setOnClickListener(v -> {
                    if (opened == helper.getLayoutPosition()) {
                        //当点击的item已经被展开了, 就关闭.
                        opened = -1;
                        notifyItemChanged(helper.getLayoutPosition());
                    }
                });
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvSellOtc.setLayoutManager(linearLayoutManager);
        mRvSellOtc.setAdapter(commonAdapterAmount);
        commonAdapterAmount.setEmptyView(R.layout.recyclerview_amount_empty_layout, mRvSellOtc);//没有数据的时候显示的页面
    }

    @Override
    protected void onEvent() {
        requestSellList(true);//网络请求数据
    }

    private void requestSellList(boolean isShowProgress) {
        otcGetSellListProviderServices.requestGetSellListProvider()
                .compose(bindToLifecycle())
                .subscribe(new RxProgressSubscriber<OTCGetExchangeSellBean>((BaseProgressView) mCtx, isShowProgress) {
                    @Override
                    public void onSuccess(OTCGetExchangeSellBean data) {
                        commonAdapterAmount.setNewData(data.getExchangePayList());
                        commonAdapterCurr.setNewData(data.getExchangeCoinList());

                        //如果列表没有数据下一步的按钮置为灰
                        if (data.getExchangeCoinList() != null && data.getExchangeCoinList().size() > 0 && data.getExchangePayList() != null && data.getExchangePayList().size() > 0) {
                            mBtnAcceptChangeNextStep.setEnabled(true);
                            mBtnAcceptChangeNextStep.setTextColor(getResources().getColor(R.color.white));
                            mBtnAcceptChangeNextStep.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
                        } else if (data.getExchangeCoinList() == null || data.getExchangeCoinList().size() == 0 || data.getExchangePayList() == null || data.getExchangePayList().size() == 0) {
                            mBtnAcceptChangeNextStep.setEnabled(false);
                            mBtnAcceptChangeNextStep.setTextColor(getResources().getColor(R.color.gray_90));
                            mBtnAcceptChangeNextStep.setBackgroundResource(R.drawable.item_gray_black);
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
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
        } else if (i == R.id.img_one_add) {
            ARouter.getInstance().build(OTCConstant.SELECTCURR)
                    .withInt("buyOrSell", 2)
                    .navigation(OTCApplySellCurrActivity.this, 12);

        } else if (i == R.id.img_two_add) {
            ARouter.getInstance().build(OTCConstant.LEGALTENDER)
                    .withInt("legal", 1)
                    .navigation(OTCApplySellCurrActivity.this, 12);
            //navigation(OTCConstant.LEGALTENDER);
        } else if (i == R.id.btn_accept_change_next_step) {//点击下一步按钮设置步骤3
            requestSetThirdStep();
            //navigation(OTCConstant.RECHARGEDEPOSIT);
        } else {
        }
    }

    private void requestSetThirdStep() {
        OTCCurrentStepParams otcCurrentStepParams = new OTCCurrentStepParams(3);
        otcAddBuyCurrProviderServices.requestSetCurrentStepProvider(otcCurrentStepParams)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mCtx, true) {
                    @Override
                    public void onSuccess(Object data) {
                        EventBus.getDefault().post(new PosInfo(166));
                        navigation(OTCConstant.RECHARGEDEPOSIT);
                        finish();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_error));
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//回调刷新页面
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 && data != null) {
            opened = -1;
            requestSellList(true);//网络请求数据
        }
    }

    @Subscriber
    public void refreshBuyList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 166) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
