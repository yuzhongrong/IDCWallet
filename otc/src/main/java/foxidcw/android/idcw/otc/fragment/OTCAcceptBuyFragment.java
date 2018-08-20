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
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.StringUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.activitys.OTCAddCurrencyActivity;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetBuyListProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeBuyBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.beans.OTCSelectPayListBean;
import foxidcw.android.idcw.otc.utils.OTCPayCodeWithLanguageCodeUtils;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectPaymentMethodPopup;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectPayTypePopWindow;

import static foxidcw.android.idcw.otc.constant.OTCConstant.PAY_METHOD_ADD_OR_EDIT;

/**
 * Created by hpz on 2018/5/4.
 */

public class OTCAcceptBuyFragment extends BaseWalletFragment implements View.OnClickListener, OTCSelectPaymentMethodPopup.OnPaymentMethodSelectListener {

    private String mTitle;
    private View view;
    private TextView mTvAddCurrHint;
    private RecyclerView mRvAddCurr;
    private ImageView mImgTwoAdd;
    private TextView mTvAddPayHint;
    private RecyclerView mRvAddPay;
    private CommonAdapter commonAdapterCurr;
    private CommonAdapter commonAdapterPay;
    private ImageView mImgOneAdd;
    private HashSet<Integer> mSelect = new HashSet<>();
    private int opened = -1;
    private int refreshTag = 0;
    private List<OTCSelectPayListBean> mAllPayTypeLists = new ArrayList<>();
    private List<OTCSelectPayListBean> mCacheAllPayTypeLists = new ArrayList<>();
    private OTCSelectPaymentMethodPopup mPaymentMethodPopup;

    @Autowired
    OTCMoneyBagListProviderServices mWalletListServices;

    @Autowired
    OTCGetBuyListProviderServices otcGetBuyListProviderServices;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;

    @Autowired
    OTCGetCoinListProviderServices mGetExchangeCoinListProviderServices;

    public static OTCAcceptBuyFragment getInstance(String title) {
        OTCAcceptBuyFragment self = new OTCAcceptBuyFragment();
        self.mTitle = title;
        return self;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_accept_buy;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        initView(rootView);
    }

    @Override
    protected void onEvent() {

    }

    private void requestPayTypeList(int tag,boolean isShowProgress) {
        mGetExchangeCoinListProviderServices.requestOTCPayTypeList()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<List<OTCSelectPayListBean>>(this) {
                    @Override
                    public void onSuccess(List<OTCSelectPayListBean> data) {
                        mAllPayTypeLists.clear();
                        mAllPayTypeLists.addAll(data);
                        mCacheAllPayTypeLists.clear();
                        mCacheAllPayTypeLists.addAll(data);

                        if (tag == 1) return;
                        if (mAllPayTypeLists.size() <= 0) return;
                        setPaymentMethodUI();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        //如果 619 说明没有设置支付方式
                        if (ex.getErrorCode().equals("619")) {
                            OTCSelectPayTypePopWindow OTCSelectPayTypePopWindow = new OTCSelectPayTypePopWindow(mContext);
                            OTCSelectPayTypePopWindow.showPopupWindow();
                            OTCSelectPayTypePopWindow.getClickSetting().setOnClickListener(v1 -> {
                                OTCSelectPayTypePopWindow.dismiss();
                                navigation(PAY_METHOD_ADD_OR_EDIT);
                            });
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }
                });
    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    protected void lazyFetchData() {

    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        view = View.inflate(getActivity(), R.layout.fragment_accept_buy, null);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // TODO:OnCreateView Method has been created, run FindViewById again to generate code
//        initView(view);
//        return view;
//    }

    public void initView(View view) {
        EventBus.getDefault().register(this);
        ARouter.getInstance().inject(this);
        mTvAddCurrHint = (TextView) view.findViewById(R.id.tv_add_curr_hint);
        mRvAddCurr = (RecyclerView) view.findViewById(R.id.rv_add_curr);
        mImgTwoAdd = (ImageView) view.findViewById(R.id.img_two_add);
        mImgOneAdd = (ImageView) view.findViewById(R.id.img_one_add);
        mTvAddPayHint = (TextView) view.findViewById(R.id.tv_add_pay_hint);
        mRvAddPay = (RecyclerView) view.findViewById(R.id.rv_add_pay);
        mImgOneAdd.setOnClickListener(this);
        mImgTwoAdd.setOnClickListener(this);

        initCurrRecycler();
        initPayListRecycler();

        requestAcceptBuyList(true);
        //requestPayTypeList();
    }

    private void initCurrRecycler() {
        commonAdapterCurr = new CommonAdapter<OTCGetExchangeBuyBean.ExchangeCoinListBean>(R.layout.item_add_apply_curr) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCGetExchangeBuyBean.ExchangeCoinListBean item) {
                helper.setVisible(R.id.view_diver, helper.getAdapterPosition() != getItemCount() - 1);
                ((TextView) helper.getView(R.id.str_item_curr_name)).setText(StringUtils.handlerNull(item.getCoinCode()).toUpperCase());
                ((TextView) helper.getView(R.id.str_item_curr_up)).setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getMin(), 4)) + "-" + StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getMax(), 4)) + "");
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
//                    if (opened == helper.getLayoutPosition()) {
//                        //当点击的item已经被展开了, 就关闭.
//                        opened = -1;
//                        notifyItemChanged(helper.getLayoutPosition());
//                    }
                    ARouter.getInstance().build(OTCConstant.SELECTCURR)
                            .withInt("buyOrSell", 3)
                            .withInt("buyOrSellId", item.getId())
                            .withInt("buyOrSellCoinId", item.getCoinId())
                            .withString("buyOrSellCoinCode", item.getCoinCode())
                            .withDouble("Max", item.getMax())
                            .withDouble("Min", item.getMin())
                            .withDouble("Premium", item.getPremium())
                            .navigation(getActivity(), 12);
                });

                ((TextView) helper.getView(R.id.item_apply_curr_delete)).setOnClickListener(v -> {
                    OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mContext);
                    OTCDeleteCurrPayPopWindow.showPopupWindow();
                    OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {
                        OTCRemoveBean otcRemoveBean = new OTCRemoveBean(item.getId());
                        otcAddBuyCurrProviderServices.requesRemoveProvider(otcRemoveBean)
                                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mContext, true) {
                                    @Override
                                    public void onSuccess(Object data) {
                                        OTCDeleteCurrPayPopWindow.dismiss();
                                        //requestAcceptBuyList(true);
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
    }

    private void initPayListRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvAddCurr.setLayoutManager(linearLayoutManager);
        mRvAddCurr.setAdapter(commonAdapterCurr);
        commonAdapterCurr.setEmptyView(R.layout.recyclerview_curr_empty_layout, mRvAddCurr);

        commonAdapterPay = new CommonAdapter<OTCGetExchangeBuyBean.ExchangePayListBean>(R.layout.item_add_apply_pay) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCGetExchangeBuyBean.ExchangePayListBean item) {
                helper.setVisible(R.id.view_diver, helper.getAdapterPosition() != getItemCount() - 1);
                GlideUtil.loadImageView(mContext, item.getLogo(), (ImageView) helper.getView(R.id.str_item_img_curr));
                ((TextView) helper.getView(R.id.str_item_pay_type)).setText(item.getLocalCurrencyCode());
                if ("AliPay".equals(item.getPayTypeCode())||"WeChat".equals(item.getPayTypeCode())) {
                    ((TextView) helper.getView(R.id.str_item_pay_logo)).setText(OTCPayCodeWithLanguageCodeUtils.getShowPayCodeWithLanguageCode(helper.itemView.getContext(),item.getPayTypeCode()));
//                    switch (AppLanguageUtils.getLanguageLocalCode(mContext)) {
//                        case "1":
//                            ((TextView) helper.getView(R.id.str_item_pay_logo)).setText(getString(R.string.str_otc_pay_alipay));
//                            break;
//                        case "8":
//                            ((TextView) helper.getView(R.id.str_item_pay_logo)).setText(getString(R.string.str_otc_pay_alipay));
//                            break;
//                        default:
//                            ((TextView) helper.getView(R.id.str_item_pay_logo)).setText(item.getPayTypeCode());
//                            break;
//                    }
                } else {
                    ((TextView) helper.getView(R.id.str_item_pay_logo)).setText(item.getPayAttributes().getBankName());
                }

                helper.getConvertView().setOnClickListener(v -> {

                });

                ((ImageView) helper.getView(R.id.str_item_img_delete)).setOnClickListener(v -> {
                    OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mContext);
                    OTCDeleteCurrPayPopWindow.showPopupWindow();
                    OTCDeleteCurrPayPopWindow.setTitle(getString(R.string.str_otc_assener_sure_delete_money));
                    OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {
                        OTCRemoveBean otcRemoveBean = new OTCRemoveBean(item.getId());
                        otcAddBuyCurrProviderServices.requesRemovePayProvider(otcRemoveBean)
                                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mContext, true) {
                                    @Override
                                    public void onSuccess(Object data) {
                                        OTCDeleteCurrPayPopWindow.dismiss();
                                        //requestAcceptBuyList(true);
                                        //requestPayTypeList();
                                        if (helper.getLayoutPosition() == -1) return;
                                        commonAdapterPay.remove(helper.getLayoutPosition());
                                        commonAdapterPay.notifyItemChanged(helper.getLayoutPosition());
                                        mSelect.remove(item.getId());
                                        mSelect.remove(item.getPayModeId());
                                    }

                                    @Override
                                    protected void onError(ResponseThrowable ex) {

                                    }
                                });
                    });
                });
            }
        };

        LinearLayoutManager linearLayoutManagerPay = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvAddPay.setLayoutManager(linearLayoutManagerPay);
        mRvAddPay.setAdapter(commonAdapterPay);
        commonAdapterPay.setEmptyView(R.layout.recyclerview_pay_empty_layout, mRvAddPay);
    }


    private void requestAcceptBuyList(boolean isShowProgress) {
        otcGetBuyListProviderServices.requestGetBuyListProvider()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<OTCGetExchangeBuyBean>((BaseProgressView) mContext, isShowProgress) {
                    @Override
                    public void onSuccess(OTCGetExchangeBuyBean data) {
                        commonAdapterCurr.setNewData(data.getExchangeCoinList());
                        if (data.getExchangePayList() == null) return;
                        for (OTCGetExchangeBuyBean.ExchangePayListBean item :
                                data.getExchangePayList()) {
                            mSelect.add(item.getPayModeId());
                        }
                        commonAdapterPay.setNewData(data.getExchangePayList());
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_one_add) {
            ARouter.getInstance().build(OTCConstant.SELECTCURR)
                    .withInt("buyOrSell", 1)
                    .navigation(getActivity(), 12);

        } else if (i == R.id.img_two_add) {
            requestPayTypeList(0,true);
        } else {
        }
    }

    @Override
    public void onPaymentMethodSelected(ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> selectItems) {
        if (selectItems.size() <= 0) return;
        StringBuffer stringBuffer = new StringBuffer();
        for (OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse item : selectItems) {
            if (!mSelect.contains(item.getID())) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(item.getID());
            }
        }

        OTCAddCurrencyActivity.CacheBean cacheBean = new OTCAddCurrencyActivity.CacheBean();
        cacheBean.ID = stringBuffer.toString();
        otcAddBuyCurrProviderServices.requestAddPayProvider(cacheBean)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<Boolean>(this) {
                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }

                    @Override
                    public void onSuccess(Boolean checkNewPinBean) {
                        requestAcceptBuyList(true);//网络请求数据
                        requestPayTypeList(1,true);
                    }
                });
    }

    public static final class CacheBean {
        public String ID;

    }

    private void setPaymentMethodUI() {
        List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> beans = new ArrayList<>();
        for (OTCSelectPayListBean item : mAllPayTypeLists) {
            OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse bean = new OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse();
            if (item.getPayTypeCode().equals("AliPay")||item.getPayTypeCode().equals("WeChat")) {
                bean.setPayTypeCode(item.getPayTypeCode());
                bean.setAccountNo(item.getPayAttributes().getAccountNo());
            } else if (item.getPayTypeCode().equals("Bankcard")){
                bean.setPayTypeCode(item.getPayAttributes().getBankName());
                bean.setAccountNo(item.getPayAttributes().getBankNoHide());
            }
            bean.setCurrencyCode(item.getLocalCurrencyCode());
            bean.setPayTypeLogo(item.getPayTypeLogo());
            bean.setID(item.getID());
            beans.add(bean);
        }

        if (mAllPayTypeLists.size() <= 0) return;
        if (commonAdapterPay.getData() == null || commonAdapterPay.getData().size() == 0) {
            mPaymentMethodPopup = new OTCSelectPaymentMethodPopup(mContext)
                    //.setItemEnable(false)
                    //.setItemBtnChangeEnable(false)
                    .setMultiEnable(true);
        } else {
            mPaymentMethodPopup = new OTCSelectPaymentMethodPopup(mContext)
                    //setItemEnable(true)
                    //.setItemBtnChangeEnable(true)
                    .setMultiEnable(true);
        }
        mPaymentMethodPopup.setOnPaymentMethodSelectListener(this);
        mPaymentMethodPopup.showSelectPaymentWithData(beans,
                mContext.getResources().getString(R.string.str_otc_select_receive_type), mSelect);
    }

    @Subscriber
    public void refreshBuyList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 101) {
            refreshTag = 1;
            opened = -1;
            requestAcceptBuyList(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
