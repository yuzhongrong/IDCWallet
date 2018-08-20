package com.idcg.idcw.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.OTCCannotInBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.SpUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.FragmentAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.base.ui.pagestatemanager.PageManager;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.cjwsc.idcm.widget.NoScrollViewPager;
import com.google.gson.Gson;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.OTCDotShowBean;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.otc.adapter.OTCTransactionRecordAdapter;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.fragment.OTCBuyFragment;
import foxidcw.android.idcw.otc.fragment.OTCSellFragment;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.iprovider.OTCGetOfferListServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetOtcOrdersServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetOtcSettingServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetOtcTradeSettingServices;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.OTCOrderStatus;
import foxidcw.android.idcw.otc.model.beans.OTCConfirmQuoteOrderMessageBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceListBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcSettingBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcTradeSettingBean;
import foxidcw.android.idcw.otc.model.beans.OTCNewOrderNoticeAcceptantBean;
import foxidcw.android.idcw.otc.model.beans.OTCQuoteOrderPriceEventBean;
import foxidcw.android.idcw.otc.model.beans.OTCStateBean;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOfficeListParam;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOrdersParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCAcceptorQuotePopup;
import foxidcw.android.idcw.otc.widgets.dialog.OTCOneBtnAndImgPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTwoBtnTitlePopWindow;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by hpz on 2018/3/21.
 */

public class OTCRecordFragment
        extends BaseWalletFragment
        implements View.OnClickListener {

    RecyclerView mRecyclerView;
    CoordinatorLayout mCoordinatorLayout;

    Unbinder unbinder;
    private String mTitle;
    private String[] mTitles;
    private NoScrollViewPager mFragmentviewpager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;
    private TextView mTvBuy;  //买入
    private TextView mTvSell; //卖出
    private OTCTransactionRecordAdapter mOtcTransactionRecordAdapter;
    SmartRefreshLayout smartrefreshlayout;

    @Autowired
    OTCGetOtcOrdersServices         mOTCGetOtcOrdersServices;  //获取OTC记录
    @Autowired
    OTCGetOfferListServices         mOTCGetOfferListServices; //获取OTC承兑信息
    @Autowired
    OTCGetOtcTradeSettingServices   mGetOtcTradeSettingServices;//获取交易配置信息
    @Autowired
    OTCGetOtcSettingServices        mOTCGetOtcSettingServices; //获取OTC配置信息
    @Autowired
    OTCMoneyBagListProviderServices mOTCMoneyBagListProviderServices; //获取承兑商状态和保证金列表
    private int mPageIndex = 1; //OTC记录页面
    private List<OTCGetOtcOrdersBean> mOtcGetOtcOrdersBeansList = new ArrayList<>();
    private final int mPageSize = 15; //单页记录条目数量
    private boolean isCanLoadMore = true;
    private LinearLayout footerLayout;
    private FrameLayout mFlbAcceptorNotice;
    private OTCNewOrderNoticeAcceptantBean mOtcNewOrderNoticeAcceptantBean;
    private OTCAcceptorQuotePopup mPopup;
    private List<OTCNewOrderNoticeAcceptantBean> mProviders; //Popwin的OTC承兑商数据
    private boolean mIsChangeData = false;     //是否可以倒计时减数
    private int mAllowQuotePriceDuration = 0;  //允许接受报价时长，单位秒 , 用于OTC报价的倒计时的总时间
    private boolean mIsAcceptor = false;  //是否是承兑商
    private static boolean mIsFirstShow = true;  //用于判断是不是第一次进入
    private boolean mIsVisible = false;   //本页面是否可见
    private boolean mHasOtcRequest = false; //是否已请求

    private LinearLayout mFooterLayout;
    private View mFooterView;
    private OTCOneBtnAndImgPopWindow mOtcOneBtnAndImgPopWindow;

    private PageManager mPageManager; //无数据页面管理
    private RequestType requesetType;
    private QBadgeView badgeView;

    private enum RequestType {
        //请求otc交易配置信息
        REQ_TRADE_SETTING,
        //请求otc配置信息
        REQ_SETTING,
        //请求otc记录
        REQ_REFRESH,
        //请求其他不需要重试的
        REQ_OTHER
    }

    public static OTCRecordFragment getInstance(String title) {
        OTCRecordFragment self = new OTCRecordFragment();
        self.mTitle = title;
        return self;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_otc_record;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mCoordinatorLayout = rootView.findViewById(R.id.coordinator_Layout);
        ARouter.getInstance()
                .inject(this);
        dialog.setBackPressEnable(false);
        mTitles = new String[]{getString(R.string.str_otc_buy),
                getString(R.string.str_otc_sell)};

        mFragmentviewpager = (NoScrollViewPager) rootView.findViewById(R.id.fragmentviewpager);
        mTvBuy = rootView.findViewById(R.id.tv_buy);        //买入OTC
        mTvSell = rootView.findViewById(R.id.tv_sell);      //卖入OTC
        smartrefreshlayout = rootView.findViewById(R.id.smartrefreshlayout); //smartrefresh
        //        mRlFooter = rootView.findViewById(R.id.rl_footer);        //rl_footer
        mFlbAcceptorNotice = rootView.findViewById(R.id.badgeroot); //承兑商

        badgeView=new QBadgeView(getContext());

        //Recycylerview footerview
        mFooterView = getLayoutInflater().inflate(R.layout.recyclerview_otc_footer_layout,
                (ViewGroup) mRecyclerView.getParent(),
                false);
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                dp2px(45)));

        //买卖的碎片
        OTCBuyFragment fragment1 = new OTCBuyFragment();
        OTCSellFragment fragment2 = new OTCSellFragment();
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        mFragmentAdapter.setParent(getActivity(), mFragments);
        mFragmentviewpager.setAdapter(mFragmentAdapter);

        //OTC列表
        mOtcTransactionRecordAdapter = new OTCTransactionRecordAdapter(getActivity(),
                R.layout.item_otc_transaction_record,
                mOtcGetOtcOrdersBeansList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        mOtcTransactionRecordAdapter.bindToRecyclerView(mRecyclerView);
        mOtcTransactionRecordAdapter.setEmptyView(R.layout.recyclerview_trade_empty_layout);
        mRecyclerView.setAdapter(mOtcTransactionRecordAdapter);


        mOtcTransactionRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance()
                        .build(OTCConstant.TRADE_DETAIL)
                        .withInt("order_id",
                                mOtcGetOtcOrdersBeansList.get(position)
                                        .getId())
                        .navigation();
            }
        });

        smartrefreshlayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartrefreshlayout.setDisableContentWhenLoading(true);
        smartrefreshlayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header,
                                        float percent,
                                        int offset,
                                        int headerHeight,
                                        int extendHeight) {

            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderReleasing(RefreshHeader header,
                                          float percent,
                                          int offset,
                                          int headerHeight,
                                          int extendHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header,
                                              int headerHeight,
                                              int extendHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterPulling(RefreshFooter footer,
                                        float percent,
                                        int offset,
                                        int footerHeight,
                                        int extendHeight) {
                LogUtil.d("FlashRecordFragment", "pulling" + offset);
                mCoordinatorLayout.scrollTo(0, -offset);
                mRecyclerView.setTranslationY(-offset);
            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {
                LogUtil.d("FlashRecordFragment", "onFooterReleased");
            }

            @Override
            public void onFooterReleasing(RefreshFooter footer,
                                          float percent,
                                          int offset,
                                          int footerHeight,
                                          int extendHeight) {
                LogUtil.d("FlashRecordFragment", "Releasing" + offset);
                mCoordinatorLayout.scrollTo(0, -offset);
                mRecyclerView.setTranslationY(-offset);
            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer,
                                              int footerHeight,
                                              int extendHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                LogUtil.d("FlashRecordFragment", "onFooterFinish");

            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout,
                                       RefreshState oldState,
                                       RefreshState newState) {

            }
        });

        mPageManager = PageManager.init(smartrefreshlayout, false, new Runnable() {
            @Override
            public void run() {
                if (requesetType == RequestType.REQ_SETTING) {
                    getOtcSetting();
                } else if (requesetType == RequestType.REQ_TRADE_SETTING) {
                    getOtcTradeSetting();
                } else if (requesetType == RequestType.REQ_REFRESH) {
                    getFirstOtcRecord();
                }
            }
        });

        initBadgeView(mFlbAcceptorNotice);

    }


    //初始化badgeview
    private void initBadgeView(FrameLayout mFlbAcceptorNotice){

        badgeView.bindTarget(mFlbAcceptorNotice)
                .setBadgeGravity(Gravity.END|Gravity.TOP)
                .setGravityOffset(getResources().getDimensionPixelSize(R.dimen.dp10),getResources().getDimensionPixelSize(R.dimen.dp2),false)
                .setBadgeTextSize(getResources().getDimensionPixelSize(R.dimen.sp10),false)
                .setShowShadow(false)
                .setBadgeNumber(0);//初始化默认设置0


    }


    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        mTvBuy.setOnClickListener(this);
        mTvSell.setOnClickListener(this);
        mFlbAcceptorNotice.setOnClickListener(this);


        mFragmentviewpager.setNoScroll(true);
        mFragmentviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //买入
                if (position == 0) {
                    mTvBuy.setBackgroundResource(R.drawable.bg_otc_transaction_rectangle_btn);
                    mTvSell.setBackgroundResource(R.drawable.bg_otc_transaction_rectangle_btn_unselect);
                    mTvBuy.setTextColor(getResources().getColor(R.color.tipper_blue_color));
                    mTvSell.setTextColor(getResources().getColor(R.color.c_8191BB));
                    //卖入
                } else if (position == 1) {
                    mTvBuy.setBackgroundResource(R.drawable.bg_otc_transaction_rectangle_btn_unselect);
                    mTvSell.setBackgroundResource(R.drawable.bg_otc_transaction_rectangle_btn);
                    mTvBuy.setTextColor(getResources().getColor(R.color.c_8191BB));
                    mTvSell.setTextColor(getResources().getColor(R.color.tipper_blue_color));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //上下拉刷新OTC列表
        //        smartrefreshlayout.setEnableLoadmore(false);
        smartrefreshlayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getOtcRecordLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getFirstOtcRecord();
            }
        });

        //承兑商信息
        mProviders = new ArrayList<>();
        if (mPopup == null) {
            mPopup = new OTCAcceptorQuotePopup(mContext, this);
        }
        //OTC报价popwindow倒计时
        Flowable.interval(1, TimeUnit.SECONDS)
                .retry(3)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong)
                            throws Exception {
                        badgeView.setBadgeNumber(mProviders.size());
                        if (mProviders != null && mProviders.size() > 0 && !mIsChangeData) {
                            int notQuotedCount = 0; //未报价的otc报价单数量
                            for (int i = 0; i < mProviders.size(); i++) {
                                OTCNewOrderNoticeAcceptantBean otcNewOrderNoticeAcceptantBean = mProviders.get(i);
                                int currentDeadLineSeconds = otcNewOrderNoticeAcceptantBean.getDeadLineSeconds();
                                if (currentDeadLineSeconds > 0) { //大于1s才能自减
                                    mProviders.get(i)
                                            .setDeadLineSeconds(currentDeadLineSeconds - 1);
                                } else {//倒计时结束，删除当前数据
                                    if (mPopup != null && mPopup.getAdapter() != null) {
                                        if (mProviders.get(i)
                                                .getStatus() != 6) { //非交易中的订单都有倒计时
                                            mProviders.remove(i);
                                            mPopup.getAdapter()
                                                    .notifyItemRemoved(i);

                                            if (mProviders.size() == 0) { //倒计时结束
                                                EventBus.getDefault().post(new OTCDotShowBean(false), EventTags.TAGS_OTC_SHOW_DOT);
                                                if (mPopup != null && mPopup.isShowing())
                                                    mPopup.dismiss();
                                            }
                                        }
                                    }
                                }

                                //统计未报价的数量
                                if(otcNewOrderNoticeAcceptantBean.getStatus() == 8 && currentDeadLineSeconds > 0){
                                    notQuotedCount++;
                                }
                            }

                            //通知mainactivitiy和交易首页的小红点显示隐藏
                            if(notQuotedCount >0 && mIsAcceptor){
                                EventBus.getDefault().post(new OTCDotShowBean(true), EventTags.TAGS_OTC_SHOW_DOT);
                            }else if(notQuotedCount == 0 && mIsAcceptor){
                                EventBus.getDefault().post(new OTCDotShowBean(false), EventTags.TAGS_OTC_SHOW_DOT);
                            }


                            if (mPopup != null && mPopup.getAdapter() != null) {
                                mPopup.getAdapter()
                                        .notifyDataSetChanged();
                            }

                            if (mProviders.size() == 0) { //倒计时结束
                                mPopup.dismiss();
                                EventBus.getDefault().post(new OTCDotShowBean(false), EventTags.TAGS_OTC_SHOW_DOT);
                            }

                        }else if(mProviders != null && mProviders.size() == 0){
                            if(mIsVisible && mHasOtcRequest)
                                EventBus.getDefault().post(new OTCDotShowBean(false), EventTags.TAGS_OTC_SHOW_DOT);
                        }
                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            getAcceptantInfo(); //判断是不是承兑商

            //OTC记录列表
//            showDialog();
            getFirstOtcRecord();
            getOtcTradeSetting(); //获取OTC交易配置信息
            getOtcSetting();      //获取OTC配置信息
            if (mProviders != null && mProviders.size() == 0) { //没有报价信息就去请求接口
                getOfferOrderList();
            }
        }else{
            mIsVisible = false;
            mIsAcceptor = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        //设置支付方式
        if (!mIsFirstShow && getUserVisibleHint()) { //再次进入并且是当前碎片可见
            getAcceptantInfo(); //判断是不是承兑商
            //OTC记录列表
//            showDialog();
            getFirstOtcRecord();
            getOtcTradeSetting(); //获取OTC交易配置信息
            getOtcSetting();      //获取OTC配置信息
            if (mProviders != null && mProviders.size() == 0) { //没有报价信息就去请求接口
                getOfferOrderList();
            }
        }
        mIsFirstShow = true;
        mIsVisible = true;

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsFirstShow = false;
        mIsVisible = false;
        mHasOtcRequest = false;
        if (mPopup != null && mPopup.isShowing())
            mPopup.dismiss();
        if (mOtcOneBtnAndImgPopWindow != null && mOtcOneBtnAndImgPopWindow.isShowing())
            mOtcOneBtnAndImgPopWindow.dismiss();
        //        LogUtil.e("isFirst setting==================================" );
    }

    @Override
    public void onDestroy() {
        mIsFirstShow = true;
        super.onDestroy();
    }

    //获取承兑商报价信息
    @SuppressLint("CheckResult")
    private void getOfferOrderList() {
        mOTCGetOfferListServices.getOTCOfferList(new OTCGetOtcOfficeListParam())
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<List<OTCNewOrderNoticeAcceptantBean>>((BaseWalletActivity) getContext()) {
                    @Override
                    public void onSuccess(List<OTCNewOrderNoticeAcceptantBean> otcNewOrderNoticeAcceptantBeans) {
                        mHasOtcRequest = true; //请求到otc报价才发送小红点显示隐藏
                        if (otcNewOrderNoticeAcceptantBeans != null && otcNewOrderNoticeAcceptantBeans.size() > 0) {
                            if (mProviders != null && mProviders.size() == 0) {
                                //取5条 过滤倒计时为0
                                for (int i = 0; i < otcNewOrderNoticeAcceptantBeans.size(); i++) {
                                    if (i == 4) {
                                        break;
                                    }
                                    OTCNewOrderNoticeAcceptantBean item = otcNewOrderNoticeAcceptantBeans.get(
                                            i);

                                    LoginStatus loginBean = LoginUtils.getLoginBean(
                                            mContext);

                                    //过滤订单（1.订单状态为6的交易中记时可以为0 ， 2.状态非6计时不能为0，）
                                    if (item != null && ((item.getDeadLineSeconds() > 0 && (item.getStatus() == 1 || item.getStatus() == 8)) || item.getStatus() == 6)) {

                                        if (loginBean != null && loginBean.getId() != item.getUserId()) { // 下单的不是承兑商
                                            item.setMaxDeadLineSeconds(
                                                    mAllowQuotePriceDuration);
                                            mProviders.add(item);
                                        }
                                    }
                                }

                            }

                            /*if (!mPopup.isShowing() && mProviders != null && mProviders.size() > 0) {
                                mPopup.setFirstData(mProviders)
                                        .showPopupWindow();
                            } else if (!mPopup.isShowing() && mProviders != null && mProviders.size() == 0) {
                                noQuotationInfo();//没有报价信息
                            }*/

                        } else {
//                            noQuotationInfo();//没有报价信息
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        LogUtil.e(this.getClass().toString() + "--->:" +  ex.ErrorMsg);
                    }
                });
    }

    private void noQuotationInfo() {
        if (mOtcOneBtnAndImgPopWindow == null) {
            mOtcOneBtnAndImgPopWindow = new OTCOneBtnAndImgPopWindow(mContext);
            mOtcOneBtnAndImgPopWindow.setTitle(getActivity().getString(R.string.str_otc_no_order))
                    .getSkipSureDelete()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOtcOneBtnAndImgPopWindow.dismiss();
                        }
                    });
        }
        if (mOtcOneBtnAndImgPopWindow!= null &&!mOtcOneBtnAndImgPopWindow.isShowing()) {
            mOtcOneBtnAndImgPopWindow.showPopupWindow();
        }
    }

    //下拉刷新 第一次进入
    @SuppressLint("CheckResult")
    private void getFirstOtcRecord() {
        requesetType = RequestType.REQ_REFRESH;
        mPageIndex = 1;
        OTCGetOtcOrdersParams otcGetOtcOrdersParams = new OTCGetOtcOrdersParams();
        otcGetOtcOrdersParams.setPageIndex(mPageIndex);
        otcGetOtcOrdersParams.setPageSize(mPageSize);
        mOTCGetOtcOrdersServices.getOtcOrders(otcGetOtcOrdersParams)
                .retry(2)
                .compose(bindToLifecycle())
                .subscribeWith(new RxSubscriber<List<OTCGetOtcOrdersBean>>() {
                    @Override
                    public void onSuccess(List<OTCGetOtcOrdersBean> otcGetOtcOrdersBeans) {
                        showError(false);
                        if (otcGetOtcOrdersBeans != null && otcGetOtcOrdersBeans.size() > 0) {
                            mOtcGetOtcOrdersBeansList.clear();
                            mOtcGetOtcOrdersBeansList.addAll(otcGetOtcOrdersBeans);
                            mOtcTransactionRecordAdapter.notifyDataSetChanged();

                            smartrefreshlayout.resetNoMoreData();
                        } else {
                            smartrefreshlayout.finishLoadmoreWithNoMoreData();
                        }
                        if (smartrefreshlayout != null) {
                            smartrefreshlayout.finishRefresh();
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        showError(true);
//                                        dismissDialog();
                        if (smartrefreshlayout != null) {
                            smartrefreshlayout.finishRefresh();
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getOtcRecordLoadMore() {

        if (isCanLoadMore) {
            mPageIndex++;
            OTCGetOtcOrdersParams otcGetOtcOrdersParams = new OTCGetOtcOrdersParams();
            otcGetOtcOrdersParams.setPageIndex(mPageIndex);
            otcGetOtcOrdersParams.setPageSize(mPageSize);
            mOTCGetOtcOrdersServices.getOtcOrders(otcGetOtcOrdersParams)
                    .retry(2)
                    .compose(bindToLifecycle())
                    .subscribeWith(new RxSubscriber<List<OTCGetOtcOrdersBean>>() {
                        @Override
                        public void onSuccess(List<OTCGetOtcOrdersBean> otcGetOtcOrdersBeans) {
                            if (otcGetOtcOrdersBeans != null && otcGetOtcOrdersBeans.size() > 0) {
                                smartrefreshlayout.resetNoMoreData();
                                mOtcGetOtcOrdersBeansList.addAll(
                                        otcGetOtcOrdersBeans);
                                mOtcTransactionRecordAdapter.notifyDataSetChanged();

                            } else {
                                smartrefreshlayout.finishLoadmoreWithNoMoreData();
                            }
                            smartrefreshlayout.finishLoadmore();
                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
//                                            dismissDialog();
                            smartrefreshlayout.finishLoadmore();
                        }
                    });
        } else {
            smartrefreshlayout.finishLoadmore();
        }
    }

    //显示FooterView
    private void addFooterView() {
        if (mFooterLayout == null) {
            mFooterLayout = mOtcTransactionRecordAdapter.getFooterLayout();
        }
        mOtcTransactionRecordAdapter.addFooterView(mFooterView);
    }

    //隐藏FooterView
    private void removeFooterView() {
        //        if (mFooterLayout != null) {
        //
        //        }
        mOtcTransactionRecordAdapter.removeFooterView(mFooterView);
    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_buy) {
            if (mFragmentviewpager.getCurrentItem() != 0) {
                mTvBuy.setBackgroundResource(R.drawable.bg_otc_transaction_rectangle);
                mTvSell.setBackgroundResource(R.color.transparent);
                mFragmentviewpager.setCurrentItem(0);
            }
        } else if (id == R.id.tv_sell) {
            if (mFragmentviewpager.getCurrentItem() != 1) {
                mTvBuy.setBackgroundResource(R.color.transparent);
                mTvSell.setBackgroundResource(R.drawable.bg_otc_transaction_rectangle);
                mFragmentviewpager.setCurrentItem(1);
            }
        } else if (id == R.id.badgeroot) { //OTC承兑商按钮

            if (mPopup == null) {
                mPopup = new OTCAcceptorQuotePopup(mContext, this);
            }

            //原来在点击承兑商按钮才进行网络请求
            /*if (mProviders != null && mProviders.size() == 0) { //没有报价信息就去请求接口
                getOfferOrderList();
            } else {
                if (!mPopup.isShowing() && mProviders != null && mProviders.size() > 0) {
                    mPopup.setFirstData(mProviders)
                            .showPopupWindow();
                } else {
                    noQuotationInfo();//没有报价信息
                }
            }*/

            if (!mPopup.isShowing() && mProviders != null && mProviders.size() > 0) {
                mPopup.setFirstData(mProviders)
                      .showPopupWindow();
            } else {
                noQuotationInfo();//没有报价信息
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private int dp2px(float dp) {
        float scale = getContext().getResources()
                .getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //推送过来的OTC报价单
    @Subscriber
    public void reciveOTCAccceptor(OTCNewOrderNoticeAcceptantBean otcNewOrderNoticeAcceptantBean) {
        //添加一条数据
        if (otcNewOrderNoticeAcceptantBean != null) { //过滤承兑商自己下的单
            LoginStatus loginBean = LoginUtils.getLoginBean(mContext);
            if (loginBean != null && !otcNewOrderNoticeAcceptantBean.getUserName().equals(loginBean.getUser_name())) {
                mIsChangeData = true; //停止刷新倒计时
                mOtcNewOrderNoticeAcceptantBean = otcNewOrderNoticeAcceptantBean;
                //本地设置当前倒计时剩余时间
                mOtcNewOrderNoticeAcceptantBean.setMaxDeadLineSeconds(mAllowQuotePriceDuration);
                if (mProviders != null && mProviders.size() < 5 && mPopup.getAdapter() != null) {

                    boolean hasExistOrder = false;
                    for(int i = 0; i < mProviders.size(); i++){
                        if (mProviders.get(i).getOrderID() == mOtcNewOrderNoticeAcceptantBean.getOrderID()){
                            hasExistOrder = true;
                        }
                    }

                    if(hasExistOrder) return; //存在订单就不加到otc弹框里面
                    mProviders.add(0, mOtcNewOrderNoticeAcceptantBean);
                    mPopup.getAdapter().notifyItemInserted(0);
                }
                mIsChangeData = false; //开始刷新倒计时
                //                mPopup.setData(mOtcNewOrderNoticeAcceptantBean);
                LogUtil.e("otcNewOrderNoticeAcceptantBean==================:" + otcNewOrderNoticeAcceptantBean.toString());
            }
        }
    }


    //推送过来的OTC订单变成交易中
    @Subscriber
    public void reciveOTCOrder2Transaction(OTCConfirmQuoteOrderMessageBean otcConfirmQuoteOrderMessageBean) {
        if (otcConfirmQuoteOrderMessageBean != null) {
            if (mProviders != null && mProviders.size() > 0) {
                mIsChangeData = true; //停止刷新倒计时
                for (int i = 0; i < mProviders.size(); i++) {
                    if (mProviders.get(i)
                            .getOrderID() == otcConfirmQuoteOrderMessageBean.getOrderId()) {
                        mProviders.get(i)
                                .setStatus(6);
                        if (mProviders.get(i)
                                .getAcceptantId() != otcConfirmQuoteOrderMessageBean.getAcceptantUserId()) { //变为交易中的承兑商不是自己移除该条数据
                            mProviders.remove(i);
                            mPopup.getAdapter()
                                    .notifyItemRemoved(i);
                            if (mProviders.size() == 0) { //倒计时结束
                                if (mPopup != null && mPopup.isShowing()) mPopup.dismiss();
                                EventBus.getDefault().post(new OTCDotShowBean(false), EventTags.TAGS_OTC_SHOW_DOT);
                            }
                        }
                    }
                }
                mIsChangeData = false; //开始刷新倒计时
            }
        }
    }

    //推送过来的OTC订单变成已报价
    @Subscriber
    public void reciveOTCOrder2Quoted(OTCQuoteOrderPriceEventBean otcQuoteOrderPriceEventBean) {
        if (otcQuoteOrderPriceEventBean != null) {
            if (mProviders != null && mProviders.size() > 0) {
                mIsChangeData = true; //停止刷新倒计时
                for (int i = 0; i < mProviders.size(); i++) {
                    if (mProviders.get(i)
                            .getOrderID() == otcQuoteOrderPriceEventBean.getOrderID()) {
                        int acceptantUserId = otcQuoteOrderPriceEventBean.getOrderID();
                        mProviders.get(i)
                                .setStatus(1);
                        mProviders.get(i)
                                .setPrice(otcQuoteOrderPriceEventBean.getPrice());
                        //                        mPopup.getAdapter()
                        //                              .notifyItemChanged(i);
                    }
                }
                mIsChangeData = false; //开始刷新倒计时
            }
        }
    }

    //交易中变为其它状态就移除报价单popwindow
    @Subscriber(tag = EventTags.TAGS_STATUS_CHANGE)
    public void reciveOrderChanged(OTCGetOtcOrdersBean statebean) {
        if (statebean != null) {
            removePopItemAndData(statebean.getId());
        }
    }

    // 订阅订单状态变更通知 (只处理订单状态变为已取消的)
    @Subscriber(tag = EventTags.TAGS_STATUS_CHANGE_MAIN)
    public void reciveOrderChanged(OTCStateBean statebean) {
        if (statebean != null) {
            if (statebean.getStatus() == OTCOrderStatus.Cancel) { //只去除已取消的
                removePopItemAndData(statebean.getOrderID());
            }
        }
    }

    //移除OTC报价item和数据
    private void removePopItemAndData(int orderID) {
        if (mProviders != null && mProviders.size() > 0) {
            mIsChangeData = true; //停止刷新倒计时
            for (int i = 0; i < mProviders.size(); i++) {
                if (mProviders.get(i)
                        .getOrderID() == orderID) {
                    mProviders.remove(i);
                    mPopup.getAdapter()
                            .notifyItemRemoved(i);
                    if (mProviders.size() == 0) { //倒计时结束
                        if (mPopup != null && mPopup.isShowing()) mPopup.dismiss();
                        EventBus.getDefault().post(new OTCDotShowBean(false), EventTags.TAGS_OTC_SHOW_DOT);
                    }
                }
            }
            mIsChangeData = false; //开始刷新倒计时
        }
    }

    //获取下单配置信息
    private void getOtcTradeSetting() {
        mGetOtcTradeSettingServices.getOtcTradeSetting()
                .retry(2)
                .compose(bindToLifecycle())
                .subscribe(new RxProgressSubscriber<OTCGetOtcTradeSettingBean>((BaseWalletActivity) getContext()) {
                    @Override
                    public void onSuccess(OTCGetOtcTradeSettingBean getOtcTradeSettingBean) {
                        showError(false);
                        //                                           OTCGetOtcTradeSettingBean getOtcTradeSettingBean1 = getOtcTradeSettingBean;
                        if (getOtcTradeSettingBean != null) {
                            EventBus.getDefault()
                                    .post(getOtcTradeSettingBean);
                            SpUtils.setIntData("cancel_count",
                                    getOtcTradeSettingBean.getCancelCount());
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        showError(true);
                        LogUtil.e(ex.ErrorMsg + "OTCRecordFragment=========================" + ex.getErrorCode());
                        //                                           ToastUtil.show(ex.getErrorMsg());
                    }
                });
    }

    ////获取OTC配置信息
    @SuppressLint("CheckResult")
    private void getOtcSetting() {
        requesetType = RequestType.REQ_SETTING;
        mOTCGetOtcSettingServices.getOtcSetting()
                .compose(bindToLifecycle())
                .retry(2)
                .subscribeWith(new RxProgressSubscriber<OTCGetOtcSettingBean>((BaseWalletActivity) getContext()) {
                    @Override
                    public void onSuccess(OTCGetOtcSettingBean otcGetOtcSettingBean) {
                        showError(false);
                        //允许接受报价时长
                        if (otcGetOtcSettingBean != null) {
                            mAllowQuotePriceDuration = otcGetOtcSettingBean.getAllowQuotePriceDuration();
                            EventBus.getDefault()
                                    .post(otcGetOtcSettingBean);
                            SpUtils.setStringData("otc_trade_setting_bean", new Gson().toJson(otcGetOtcSettingBean));
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        showError(true);
                    }
                });
    }

    //获取承兑商信息, 判断用户是不是承兑商
    @SuppressLint("CheckResult")
    public void getAcceptantInfo() {
        if (mOTCMoneyBagListProviderServices != null) {
            mOTCMoneyBagListProviderServices.requestDepositBalanceList()
                    .compose(bindToLifecycle())
                    .retry(2)
                    .subscribeWith(new RxProgressSubscriber<OTCDepositBalanceListBean>((BaseWalletActivity) getContext()) {
                        @Override
                        public void onSuccess(OTCDepositBalanceListBean otcDepositBalanceListBean) {
                            if (otcDepositBalanceListBean != null) {
                                if (otcDepositBalanceListBean.getStatus() == 3) { //已开通承兑商
                                    mIsAcceptor = true;
                                    mFlbAcceptorNotice.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
                            if ("627".equals(ex.ErrorCode)) { //没绑定手机号码
                                OTCTwoBtnTitlePopWindow OTCTwoBtnTitlePopWindow = new OTCTwoBtnTitlePopWindow(
                                        mContext);
                                OTCTwoBtnTitlePopWindow.setTitle(getActivity().getString(
                                        foxidcw.android.idcw.otc.R.string.str_otc_bind_phone))
                                        .setConfirmContent(
                                                getActivity().getString(
                                                        foxidcw.android.idcw.otc.R.string.str_otc_to_bind))
                                        .setCancelContent(
                                                getActivity().getString(
                                                        foxidcw.android.idcw.otc.R.string.str_otc_talk_later))
                                        .showPopupWindow();
                                OTCTwoBtnTitlePopWindow.getSkipSureDelete()
                                        .setOnClickListener(v1 -> {
                                            ARouter.getInstance()
                                                    .build(ArouterConstants.ACTIVITY_PHONE)
                                                    .navigation();
                                            OTCTwoBtnTitlePopWindow.dismiss();
                                        });
                                OTCTwoBtnTitlePopWindow.getClickToDismissView()
                                        .setOnClickListener(v2 -> {
                                            EventBus.getDefault()
                                                    .post(new OTCCannotInBean());
                                            OTCTwoBtnTitlePopWindow.dismiss();
                                        });
                                return;
                            } else {
                                ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                            }
                        }
                    });
        }

    }

    private void showError(boolean isShow) {
        if (RequestType.REQ_OTHER == requesetType) {
            //其他请求不显示错误
            return;
        }
        if (isShow) {
            mFlbAcceptorNotice.setVisibility(View.GONE);
            rootView.findViewById(R.id.bg_view).setBackgroundColor(Color.TRANSPARENT);
            mPageManager.showError();
        } else {
            if (mIsAcceptor) {
                mFlbAcceptorNotice.setVisibility(View.VISIBLE);
            }
            rootView.findViewById(R.id.bg_view).setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            mPageManager.showContent();
        }
    }

}
