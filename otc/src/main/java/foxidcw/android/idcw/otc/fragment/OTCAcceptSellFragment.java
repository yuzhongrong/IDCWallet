package foxidcw.android.idcw.otc.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.StringUtils;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetBuyListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeSellBean;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;

/**
 * Created by hpz on 2018/5/4.
 */

public class OTCAcceptSellFragment extends BaseWalletFragment implements View.OnClickListener {
    private String mTitle;
    private View view;
    private ImageView mImgOneAdd;
    private TextView mTvAddCurrHint;
    private RecyclerView mRvSellCurr;
    private ImageView mImgTwoAdd;
    private TextView mTvAddPayHint;
    private RecyclerView mRvSellOtc;
    private int opened = -1;
    private CommonAdapter commonAdapterAmount;
    private CommonAdapter commonAdapterCurr;

    @Autowired
    OTCGetBuyListProviderServices otcGetSellListProviderServices;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;

    public static OTCAcceptSellFragment getInstance(String title) {
        OTCAcceptSellFragment self = new OTCAcceptSellFragment();
        self.mTitle = title;
        return self;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_otc_sell_curr;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        initView(rootView);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    protected void lazyFetchData() {

    }

    public void initView(View view) {
        EventBus.getDefault().register(this);
        ARouter.getInstance().inject(this);
        mImgOneAdd = (ImageView) view.findViewById(R.id.img_one_add);
        mImgOneAdd.setOnClickListener(this);
        mTvAddCurrHint = (TextView) view.findViewById(R.id.tv_add_curr_hint);
        mRvSellCurr = (RecyclerView) view.findViewById(R.id.rv_sell_curr);
        mImgTwoAdd = (ImageView) view.findViewById(R.id.img_two_add);
        mImgTwoAdd.setOnClickListener(this);
        mTvAddPayHint = (TextView) view.findViewById(R.id.tv_add_pay_hint);
        mRvSellOtc = (RecyclerView) view.findViewById(R.id.rv_sell_otc);
        initCurrRecycler();
        initAmountListRecycler();

        requestSellList(true);//网络请求数据
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

                ((TextView) helper.getView(R.id.item_apply_curr_edit)).setOnClickListener(v -> {
                    ARouter.getInstance().build(OTCConstant.SELECTCURR)
                            .withInt("buyOrSell", 4)
                            .withInt("buyOrSellId", item.getId())
                            .withInt("buyOrSellCoinId", item.getCoinId())
                            .withString("buyOrSellCoinCode", item.getCoinCode())
                            .withDouble("Max",item.getMax())
                            .withDouble("Min",item.getMin())
                            .withDouble("Premium",item.getPremium())
                            .navigation(getActivity(),12);
                });

                ((TextView) helper.getView(R.id.item_apply_curr_delete)).setOnClickListener(v -> {
                    OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mContext);
                    OTCDeleteCurrPayPopWindow.showPopupWindow();
                    OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {
                        OTCRemoveBean otcRemoveBean = new OTCRemoveBean(item.getId());
                        otcAddBuyCurrProviderServices.requesRemoveProvider(otcRemoveBean)
                                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mContext, true) {
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

        LinearLayoutManager linearLayoutManagerPay = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvSellCurr.setLayoutManager(linearLayoutManagerPay);
        mRvSellCurr.setAdapter(commonAdapterCurr);
        commonAdapterCurr.setEmptyView(R.layout.recyclerview_otc_curr_empty_layout, mRvSellCurr);
    }

    private void initAmountListRecycler() {
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

                ((TextView) helper.getView(R.id.item_apply_curr_edit)).setOnClickListener(v -> {
                    ARouter.getInstance().build(OTCConstant.LEGALTENDER)
                            .withInt("legal", 2)
                            .withString("legalId", String.valueOf(item.getId()))
                            .withInt("legalCodeId", item.getLocalCurrencyId())
                            .withDouble("Amount",item.getAmount())
                            .navigation(getActivity(),12);
                });

                ((TextView) helper.getView(R.id.item_apply_curr_delete)).setOnClickListener(v -> {
                    OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mContext);
                    OTCDeleteCurrPayPopWindow.showPopupWindow();
                    OTCDeleteCurrPayPopWindow.setTitle(getString(R.string.str_otc_assener_curr_amount));
                    OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {
                        OTCRemoveBean otcRemoveBean = new OTCRemoveBean(item.getId());
                        otcAddBuyCurrProviderServices.requesRemoveLegalProvider(otcRemoveBean)
                                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mContext, true) {
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvSellOtc.setLayoutManager(linearLayoutManager);
        mRvSellOtc.setAdapter(commonAdapterAmount);
        commonAdapterAmount.setEmptyView(R.layout.recyclerview_amount_empty_layout, mRvSellOtc);
    }

    private void requestSellList(boolean isShowProgress) {
        otcGetSellListProviderServices.requestGetSellListProvider()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<OTCGetExchangeSellBean>((BaseProgressView) mContext, isShowProgress) {
                    @Override
                    public void onSuccess(OTCGetExchangeSellBean data) {
                        commonAdapterAmount.setNewData(data.getExchangePayList());
                        commonAdapterCurr.setNewData(data.getExchangeCoinList());
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    @Subscriber
    public void refreshBuyList(PosInfo posInfo) {
        if (posInfo==null)return;
        if (posInfo.getPos()==101){
            opened = -1;
            requestSellList(true);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_one_add) {
            ARouter.getInstance().build(OTCConstant.SELECTCURR)
                    .withInt("buyOrSell",2)
                    .navigation(getActivity(),12);
        } else if (i == R.id.img_two_add) {
            ARouter.getInstance().build(OTCConstant.LEGALTENDER)
                    .withInt("legal",1).navigation(getActivity());
        } else {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }





}
