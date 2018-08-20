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
import com.cjwsc.idcm.Utils.GlideUtil;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetBuyListProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetPaymentModeManagementServices;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeBuyBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.beans.OTCSelectPayListBean;
import foxidcw.android.idcw.otc.model.params.OTCCurrentStepParams;
import foxidcw.android.idcw.otc.utils.OTCPayCodeWithLanguageCodeUtils;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectPaymentMethodPopup;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectPayTypePopWindow;

/**
 * Created by hpz on 2018/4/21.
 */

@Route(path = OTCConstant.ADDCURRENCY, name = "申请承兑商添加币种和支付方式")
public class OTCAddCurrencyActivity extends BaseWalletActivity implements View.OnClickListener, OTCSelectPaymentMethodPopup.OnPaymentMethodSelectListener {
    private LinearLayout mMrBackLayout;
    /**
     * title
     */
    private TextView mTvSetName;
    private ImageView mImgOneStep;
    private ImageView mImgTwoAdd;
    private View mViewOneStep;
    private ImageView mImgTwoStep;
    private View mViewTwoStep;
    private ImageView mImgThreeStep;
    private View mViewThreeStep;
    private ImageView mImgFourStep;
    private ImageView mImgOneAdd;
    private TextView mBtnAcceptChangeNextStep;
    private TextView mTvAddCurrHint;
    private RecyclerView mRvAddCurr;
    private TextView mTvAddPayHint;
    private RecyclerView mRvAddPay;
    private CommonAdapter commonAdapterCurr;
    private CommonAdapter commonAdapterPay;
    private int opened = -1;

    private List<OTCSelectPayListBean> mAllPayTypeLists = new ArrayList<>();
    private List<OTCSelectPayListBean> mCacheAllPayTypeLists = new ArrayList<>();
    private OTCSelectPaymentMethodPopup mPaymentMethodPopup;//支付方式多选框
    private OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse mCurrentPaymentMethod = new OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse(); // 当前选中的支付方式

    private HashSet<Integer> mSelect = new HashSet<>();//缓存选中的item

    @Autowired//服务请求
    OTCMoneyBagListProviderServices mWalletListServices;

    @Autowired
    OTCGetBuyListProviderServices otcGetBuyListProviderServices;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;

    @Autowired
    OTCGetCoinListProviderServices mGetExchangeCoinListProviderServices;

    @Autowired
    OTCGetPaymentModeManagementServices mGetPaymentModeManagementServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_currency;
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
        mImgTwoAdd = (ImageView) findViewById(R.id.img_two_add);
        mBtnAcceptChangeNextStep = (TextView) findViewById(R.id.btn_accept_change_next_step);
        mTvAddCurrHint = (TextView) findViewById(R.id.tv_add_curr_hint);
        mRvAddCurr = (RecyclerView) findViewById(R.id.rv_add_curr);
        mTvAddPayHint = (TextView) findViewById(R.id.tv_add_pay_hint);
        mRvAddPay = (RecyclerView) findViewById(R.id.rv_add_pay);
        mBtnAcceptChangeNextStep.setOnClickListener(this);
        mImgTwoStep.setOnClickListener(this);
        mImgTwoAdd.setOnClickListener(this);
        mImgOneAdd.setOnClickListener(this);
        mMrBackLayout.setOnClickListener(this);
        mTvSetName.setText(getString(R.string.str_otc_apply_assurer));
        mBtnAcceptChangeNextStep.setEnabled(false);

        initCurrRecycler();//加载承兑币种列表数据
        initPayListRecycler();//加载支付方式列表数据
    }

    private void initCurrRecycler() {
        commonAdapterCurr = new CommonAdapter<OTCGetExchangeBuyBean.ExchangeCoinListBean>(R.layout.item_add_apply_curr) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCGetExchangeBuyBean.ExchangeCoinListBean item) {
                helper.setVisible(R.id.view_diver, helper.getAdapterPosition()!=getItemCount()-1);
                ((TextView) helper.getView(R.id.str_item_curr_name)).setText(StringUtils.handlerNull(item.getCoinCode()).toUpperCase());
                ((TextView) helper.getView(R.id.str_item_curr_up)).setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getMin(),4)) + "-" + StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getMax(),4)) + "");
                if (item.getPremium()>0){
                    ((TextView) helper.getView(R.id.str_item_curr_down)).setText("+"+StringUtils.formatChangeOne(item.getPremium()*100) + "%");
                }else {
                    ((TextView) helper.getView(R.id.str_item_curr_down)).setText(StringUtils.formatChangeOne(item.getPremium()*100) + "%");
                }

                if (helper.getLayoutPosition() == opened) {//
                    ((LinearLayout) helper.getView(R.id.ll_item_dialog_select)).setVisibility(View.VISIBLE);
                    ((LinearLayout) helper.getView(R.id.accept_buy_or_sell)).setBackgroundResource(R.color.c_F7FAFE);
                } else {
                    ((LinearLayout) helper.getView(R.id.ll_item_dialog_select)).setVisibility(View.GONE);
                    ((LinearLayout) helper.getView(R.id.accept_buy_or_sell)).setBackgroundResource(R.color.white);
                }

                ((TextView) helper.getView(R.id.item_apply_curr_edit)).setOnClickListener(v -> {
                    ARouter.getInstance().build(OTCConstant.SELECTCURR)//点击item编辑按钮
                            .withInt("buyOrSell", 3)
                            .withInt("buyOrSellId", item.getId())
                            .withInt("buyOrSellCoinId", item.getCoinId())
                            .withString("buyOrSellCoinCode", item.getCoinCode())
                            .withDouble("Max",item.getMax())
                            .withDouble("Min",item.getMin())
                            .withDouble("Premium",item.getPremium())
                            .navigation(OTCAddCurrencyActivity.this, 12);
                });

                ((TextView) helper.getView(R.id.item_apply_curr_delete)).setOnClickListener(v -> {
                    OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mCtx);
                    OTCDeleteCurrPayPopWindow.showPopupWindow();
                    OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {//点击item删除按钮
                        OTCRemoveBean otcRemoveBean = new OTCRemoveBean(item.getId());
                        otcAddBuyCurrProviderServices.requesRemoveProvider(otcRemoveBean)
                                .compose(bindToLifecycle())
                                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mCtx, true) {
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
                                        //showError(true);
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
        mRvAddCurr.setLayoutManager(linearLayoutManager);
        mRvAddCurr.setAdapter(commonAdapterCurr);
        commonAdapterCurr.setEmptyView(R.layout.recyclerview_curr_empty_layout, mRvAddCurr);//没有数据列表数据显示的布局
    }

    private void initPayListRecycler() {
        commonAdapterPay = new CommonAdapter<OTCGetExchangeBuyBean.ExchangePayListBean>(R.layout.item_add_apply_pay) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCGetExchangeBuyBean.ExchangePayListBean item) {
                helper.setVisible(R.id.view_diver, helper.getAdapterPosition()!=getItemCount()-1);
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
                    OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mCtx);
                    OTCDeleteCurrPayPopWindow.showPopupWindow();
                    OTCDeleteCurrPayPopWindow.setTitle(getString(R.string.str_otc_assener_sure_delete_money));
                    OTCDeleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {//点击删除按钮
                        OTCRemoveBean otcRemoveBean = new OTCRemoveBean(item.getId());
                        otcAddBuyCurrProviderServices.requesRemovePayProvider(otcRemoveBean)
                                .compose(bindToLifecycle())
                                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mCtx, true) {
                                    @Override
                                    public void onSuccess(Object data) {
                                        OTCDeleteCurrPayPopWindow.dismiss();
                                        //requestAcceptBuyList(true);
                                        //requestPayTypeList();
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

        LinearLayoutManager linearLayoutManagerPay = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvAddPay.setLayoutManager(linearLayoutManagerPay);
        mRvAddPay.setAdapter(commonAdapterPay);
        commonAdapterPay.setEmptyView(R.layout.recyclerview_pay_empty_layout, mRvAddPay);//没有数据列表显示的布局
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//回调刷新页面
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 && data != null) {
            opened = -1;
            requestAcceptBuyList(true);
        }
    }

    @Override
    protected void onEvent() {
        requestAcceptBuyList(true);//请求承兑币种和支付方式列表数据
        //requestPayTypeList();//请求设置的支付方式列表
    }

    private void requestAcceptBuyList(boolean isShowProgress) {
        otcGetBuyListProviderServices.requestGetBuyListProvider()
                .compose(bindToLifecycle())
                .subscribe(new RxProgressSubscriber<OTCGetExchangeBuyBean>((BaseProgressView) mCtx, isShowProgress) {
                    @Override
                    public void onSuccess(OTCGetExchangeBuyBean data) {
                        commonAdapterCurr.setNewData(data.getExchangeCoinList());

                        if (data.getExchangePayList() == null) {
                            return;
                        }
                        for (OTCGetExchangeBuyBean.ExchangePayListBean item :
                                data.getExchangePayList()) {
                            mSelect.add(item.getPayModeId());
                        }
                        commonAdapterPay.setNewData(data.getExchangePayList());
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
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }
                });
    }

    private void requestPayTypeList(int tag) {
        mGetExchangeCoinListProviderServices.requestOTCPayTypeList()
                .compose(bindToLifecycle())
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
                            OTCSelectPayTypePopWindow OTCSelectPayTypePopWindow = new OTCSelectPayTypePopWindow(mCtx);
                            OTCSelectPayTypePopWindow.showPopupWindow();
                            OTCSelectPayTypePopWindow.getClickSetting().setOnClickListener(v1 -> {
                                OTCSelectPayTypePopWindow.dismiss();
                                ARouter.getInstance().build(OTCConstant.PAY_METHOD_ADD_OR_EDIT)
                                        .navigation(OTCAddCurrencyActivity.this, 12);
                            });
                        }else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
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
        } else if (i == R.id.img_one_add) {//跳转选择添加承兑币种页面
            ARouter.getInstance().build(OTCConstant.SELECTCURR)
                    .withInt("buyOrSell", 1)
                    .navigation(OTCAddCurrencyActivity.this, 12);
        } else if (i == R.id.img_two_add) {//显示设置的支付方式列表弹框
            requestPayTypeList(0);//请求设置的支付方式列表
        } else if (i == R.id.btn_accept_change_next_step) {//设置承兑商的设置步骤
            requestSetSecondStep();
            //navigation(OTCConstant.APPLYSELLCURR);
        } else {
        }
    }

    private void requestSetSecondStep() {
        OTCCurrentStepParams otcCurrentStepParams = new OTCCurrentStepParams(2);
        otcAddBuyCurrProviderServices.requestSetCurrentStepProvider(otcCurrentStepParams)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mCtx, true) {
                    @Override
                    public void onSuccess(Object data) {
                        EventBus.getDefault().post(new PosInfo(166));
                        navigation(OTCConstant.APPLYSELLCURR);
                        finish();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_error));
                    }
                });
    }

    @Override
    public void onPaymentMethodSelected(ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> selectItems) {//弹框的确定按钮的回调
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

        mCurrentPaymentMethod = selectItems.get(0);
        CacheBean cacheBean = new CacheBean();
        cacheBean.ID = stringBuffer.toString();
        otcAddBuyCurrProviderServices.requestAddPayProvider(cacheBean)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<Boolean>(this) {
            @Override
            protected void onError(ResponseThrowable ex) {

            }

            @Override
            public void onSuccess(Boolean checkNewPinBean) {
                requestAcceptBuyList(true);//网络请求数据
                requestPayTypeList(1);
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
            mPaymentMethodPopup = new OTCSelectPaymentMethodPopup(mCtx)
                    //.setItemEnable(false)
                    //.setItemBtnChangeEnable(false)
                    .setMultiEnable(true);
        } else {
            mPaymentMethodPopup = new OTCSelectPaymentMethodPopup(mCtx)
                    //.setItemEnable(true)
                    //.setItemBtnChangeEnable(true)
                    .setMultiEnable(true);
        }
        mPaymentMethodPopup.setOnPaymentMethodSelectListener(this);
        mPaymentMethodPopup.showSelectPaymentWithData(beans,
                mCtx.getResources().getString(R.string.str_otc_select_receive_type), mSelect);
    }

    @Subscriber(tag = EventTags.TAGS_REFRESH_BUY)
    public void refreshBuyList() {
        requestAcceptBuyList(true);
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
