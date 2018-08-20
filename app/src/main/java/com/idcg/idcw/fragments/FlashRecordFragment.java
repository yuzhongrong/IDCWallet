package com.idcg.idcw.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.BuildConfig;
import com.idcg.idcw.R;

import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.base.BaseWalletFragment;

import com.idcg.idcw.activitys.MainActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.ExchangeInProviderServices;
import com.idcg.idcw.iprovider.GetExchangeBalanceProviderServices;
import com.idcg.idcw.iprovider.GetExchangeCoinListProviderServices;
import com.idcg.idcw.iprovider.GetExchangeDetailProviderServices;
import com.idcg.idcw.model.bean.CoinBean;
import com.idcg.idcw.model.bean.CoinExchangeBean;
import com.idcg.idcw.model.bean.CoinPairBean;
import com.idcg.idcw.model.bean.ExchangeDataBean;
import com.idcg.idcw.model.bean.ExchangeResultBean;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.logic.FlashRecordFragmentLogic;
import com.idcg.idcw.model.params.ExchangeInParam;
import com.idcg.idcw.model.params.FlashRecordListReqParam;
import com.idcg.idcw.presenter.impl.FlashRecordFragmentPresenterImpl;
import com.idcg.idcw.presenter.interfaces.FlashRecordFragmentContract;
import com.idcg.idcw.utils.UIUtils;
import com.idcg.idcw.utils.Utils;
import com.idcg.idcw.widget.EditTextJudgeNumberWatcher;
import com.idcg.idcw.widget.dialog.BiBiHelpPopWindow;
import com.idcg.idcw.widget.dialog.ChooseCoinTypePopWindow;
import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.base.ui.pagestatemanager.PageManager;
import com.cjwsc.idcm.event.BiBiRecordRefreshEvent;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.Subscriber;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.Unbinder;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.common.widget.ClearableEditText;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by hpz on 2018/3/21.
 */

public class FlashRecordFragment extends BaseWalletFragment<FlashRecordFragmentLogic,
        FlashRecordFragmentPresenterImpl> implements FlashRecordFragmentContract.View {
    private static final int debounceTime = 0;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout mSmartrefreshlayout;
    @BindView(R.id.coordinator_Layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.rl_left)
    RelativeLayout rlLeft;
    @BindView(R.id.btn_swap)
    ImageView btnSwap;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_right)
    LinearLayout rlRight;
    @BindView(R.id.et_out)
    ClearableEditText etOut;
    @BindView(R.id.et_in)
    ClearableEditText etIn;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_min)
    TextView tvMin;
    @BindView(R.id.tv_max)
    TextView tvMax;
    @BindView(R.id.btn_exchange)
    TextView btnExchange;
    @BindView(R.id.tv_left_2)
    TextView mTvLeft2;
    @BindView(R.id.tv_right_2)
    TextView mTvRight2;
    @BindView(R.id.iv_left_arrow)
    ImageView mIvLeftArrow;
    @BindView(R.id.iv_right_arrow)
    ImageView mIvRightArrow;
    @BindView(R.id.content_container)
    LinearLayout contentContainer;
    
    @Autowired
    GetExchangeBalanceProviderServices mGetWalletBalanceProviderServices;
    private String mTitle;
    private CommonAdapter<ExchangeDataBean> commonAdapter;

    /**可兑出余额*/
    private double availableBalance = 0;
    /**最小可兑出*/
    private double minExchange = 1;
    /**最大可兑出*/
    private double maxExchange = 0;

    /*--------------- 动画 ---------------*/
    private WindowManager mWindowManager;
    private int[] mLeftLocation;
    private int[] mRightLocation;
    private Bitmap mLeftCacheBitmap;
    private Bitmap mRightCacheBitmap;
    private ImageView copyViewLeft;
    private ImageView copyViewRight;
    /*--------------- 动画 ---------------*/

    private CoinBean mLeftCoin;
    private CoinBean mRightCoin;
    private CoinBean mBtcCoin;

    private EditTextJudgeNumberWatcher mOutEtDigistController;
    private EditTextJudgeNumberWatcher mInEtDigistController;
    private ExchangeInParam mExchangeInParam;
    private Disposable mAutoRefreshRateDisposable;
    /**
     * 存放交易对
     * 一个币可以对应多个币，成为多个交易对
     */
    private HashMap<String, Set<String>> mCoinPairsMap = new HashMap<>();
    /**
     * 存放所有币实例
     * */
    private HashMap<String, CoinBean> mCoinBeanMap = new LinkedHashMap<>();

    private Map<String, CoinBean> mSortCoins = new LinkedHashMap<>();
    /**
     * 存放所有交易对汇率
     * */
    private HashMap<String, Double> mCoinPairRateMap = new HashMap<>();
    /**
     * 存放所有交易对汇率位数
     * */
    private HashMap<String, Integer> mCoinPairRateDigitMap = new HashMap<>();
    /**
     * 存放交易对位数pair，first为digit
     * */
    private HashMap<String, Pair<Integer, Integer>> digitMap = new HashMap<>();
    /**
     * 存放交易对对应的兑出范围，first为最小，second为最大
     * */
    private HashMap<String, Pair<Double, Double>> mCanExchangeScope = new HashMap<>();
    /**
     * 存放所有为正方向的交易对
     * */
    private Set<String> mDirectionSet = new HashSet<>();

    @Autowired
    GetExchangeDetailProviderServices mGetExchangeDetailProviderServices;
    @Autowired
    GetExchangeCoinListProviderServices mGetExchangeCoinListProviderServices;
    private PageManager mPageManager;
    private MainActivity.MyOnTouchListener mMyOnTouchListener;
    private MainActivity mMainActivity;
    private CoinPairBean mDefaultCoinPairBean;
    private RxProgressSubscriber<NewBalanceBean> mReqBalanceDisposable;


    public static FlashRecordFragment getInstance(String title) {
        FlashRecordFragment self = new FlashRecordFragment();
        self.mTitle = title;
        return self;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_flash_record;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        initRecyclerFlashData();//初始化闪兑记录

        mWindowManager = getActivity().getWindowManager();

        mPageManager = PageManager.init(mSmartrefreshlayout, false, new Runnable() {
            @Override
            public void run() {
                if (requesetType == RequestType.REQ_COIN_LIST) {
                    requestCoinList(true);
                }  else if (requesetType == RequestType.REQ_BALANCE) {
                    requestBalance();
                }
            }
        });
        mMainActivity = (MainActivity) mContext;
        mMyOnTouchListener = new MainActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
// 获取当前焦点所在的控件；
                    View view = mMainActivity.getCurrentFocus();
                    if (view != null && view instanceof EditText) {
                        Rect r = new Rect();
                        view.getGlobalVisibleRect(r);
                        int rawX = (int) ev.getRawX();
                        int rawY = (int) ev.getRawY();

// 判断点击的点是否落在当前焦点所在的 view 上；
                        if (!r.contains(rawX, rawY)) {
                            KeyboardUtils.hideSoftInput(view);
                            view.clearFocus();
                        }
                    }
                }
                return false;
            }
        };
        mMainActivity.registerMyOnTouchListener(mMyOnTouchListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMyOnTouchListener!=null)
            mMainActivity.unregisterMyOnTouchListener(mMyOnTouchListener);
    }

    /**
     * 将1.0这样的double返回1
     */
    public static String doubleTrans(double d){
        if(Math.round(d)-d==0){
            return String.valueOf((long)d);
        }
        return String.valueOf(d);
    }
    private void initRecyclerFlashData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commonAdapter = new CommonAdapter<ExchangeDataBean>(R.layout.recycler_flash_view) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            public void commonconvert(BaseViewHolder helper, ExchangeDataBean bean) {
                helper.setGone(R.id.view_divider, helper.getAdapterPosition()==0);
                helper.setGone(R.id.view_divider_bottom, helper.getAdapterPosition()!=commonAdapter.getItemCount()-1);
                helper.setText(R.id.tv_amount_out, ""+ StringUtils.subZeroAndDot(BigDecimal.valueOf(bean.getAmount()).toString())+" "+bean.getUpperCaseCurrency());
                helper.setText(R.id.tv_amount_in, ""+StringUtils.subZeroAndDot(BigDecimal.valueOf(bean.getToAmount()).toString())+" "+bean.getUpperCaseToCurrency());

//                    if (bean.getConfirmCount() > bean.getMinCount()) {
//                        if (bean.getConfirmCount() > 1000) {
//                            ((TextView) helper.getView(R.id.tv_status)).setText(String.format("%s(1,000+%s)", bean
//                                    .getStatusDescription(), getString(R.string.one)));
//
//                        } else {
//                            ((TextView) helper.getView(R.id.tv_status)).setText(String.format("%s(%d%s)", bean
//                                    .getStatusDescription(), bean.getConfirmCount(), getString(R.string.one)));
//                        }
//                    } else {
//                        helper.setText(R.id.tv_status, String.format("%s(%s/%s%s)", bean.getStatusDescription(), bean
//                                .getMinCount(), bean.getConfirmCount(), getString(R.string.one)));
//                    }

                    /*
                 * 0是兑入中。
                 * 1是兑出中，兑入完成。
                 * 2是兑出完成，兑入完成
                 * 3是兑出完成，兑入完成
                 * 4是兑出失败，兑入失败
                 * 5是兑出失败，兑入完成
                 * */
                /*int status = bean.getStatus();
                if (status == 4) {
                    helper.<TextView>getView(R.id.tv_status).setText(R.string.exchange_faild);
                } else if (status == 0) {
                    helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_doing), bean.getConfirmCount(),bean.getMinCount()));
                }else if(status == 1){
                    helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(bean.getConfirmCount())));
                }else if(status == 2 || status == 3){
                    helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(bean.getConfirmCount())));
                }else if(status == 5){
                    helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(bean.getConfirmCount())));
                }*/
                /*
                 * 0是兑出中。
                 * 1是兑入中，兑出完成。
                 * 2是兑入完成，兑出完成
                 * 3是兑入完成，兑出完成
                 * 4是兑入失败，兑出失败
                 * 5是兑入失败，兑出完成
                 * */
                /*int status = bean.getStatus();
                if (status == 4) {
                    helper.<TextView>getView(R.id.tv_status).setText(R.string.exchange_faild);
                } else if (status == 0) {
                    helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_doing), bean.getToConfirmCount(),bean.getToMinCount()));
                }else if(status == 1){
                    helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_doing), bean.getToConfirmCount(),bean.getToMinCount()));
                }else if(status == 2 || status == 3){
                    helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(bean.getToConfirmCount())));
                }else if(status == 5){
                    helper.<TextView>getView(R.id.tv_status).setText(R.string.exchange_faild);
                }*/
                int inStatus = bean.getOutStatus();
                if(inStatus ==0 || inStatus == 3){//兑入中
                    //这里状态刷新不及时，需要自己判断下相同就是完成
                    if(bean.getToConfirmCount()>=bean.getToMinCount()&&bean.getToConfirmCount()!=0){
                        helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(bean.getToConfirmCount())));
                    }else {
                        helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_doing), bean.getToConfirmCount(),bean.getToMinCount()));
                    }

                }else if(inStatus == 1){//兑入确认
                    helper.<TextView>getView(R.id.tv_status).setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(bean.getToConfirmCount())));
                }else if(inStatus == 2){//兑入失败
                    helper.<TextView>getView(R.id.tv_status).setText(R.string.exchange_faild);
                }

                //设置时间
                if (bean.getDate() > 1 && bean.getDateType().equals("day")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_days_ago));
                } else if (bean.getDate() == 1 && bean.getDateType().equals("day")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_day_ago));
                }
                if (bean.getDate() > 1 && bean.getDateType().equals("week")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_weeks_ago));
                } else if (bean.getDate() == 1 && bean.getDateType().equals("week")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_week_ago));
                }
                if (bean.getDate() > 1 && bean.getDateType().equals("year")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_years_ago));
                } else if (bean.getDate() == 1 && bean.getDateType().equals("year")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_year_ago));
                }
                if (bean.getDate() > 1 && bean.getDateType().equals("month")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_mons_ago));
                } else if (bean.getDate() == 1 && bean.getDateType().equals("month")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_mon_ago));
                }
                if (bean.getDate() > 1 && bean.getDateType().equals("hour")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_hours_ago));
                } else if (bean.getDate() == 1 && bean.getDateType().equals("hour")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_hour_ago));
                }
                if (bean.getDate() > 1 && bean.getDateType().equals("min")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_mins_ago));
                } else if (bean.getDate() == 1 && bean.getDateType().equals("min")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_min_ago));
                }
                if (bean.getDate() > 1 && bean.getDateType().equals("sec")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_secs_ago));
                } else if (bean.getDate() == 1 && bean.getDateType().equals("sec")) {
                    ((TextView) helper.getView(R.id.tv_time)).setText(bean.getDate() + getString(R.string.tv_sec_ago));
                }
                helper.getConvertView().setOnClickListener(v -> {//点击
//                    showDialog();
                    ARouter.getInstance().build(ArouterConstants.FlashRecordDetail)
                            .withString("id", String.valueOf(bean.getId()))
                            .navigation();
                });
            }
        });
        mSmartrefreshlayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageIndex += 1;
                isloadmore = true;
                loadData(false);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                isloadmore = false;
                refreshCount = 2;
                loadData(false);
                requestBalance();
            }
        });
        mSmartrefreshlayout.setEnableLoadmore(false);
//        mSmartrefreshlayout.setDisableContentWhenLoading(true);
//        mSmartrefreshlayout.setDisableContentWhenRefresh(true);
        commonAdapter.bindToRecyclerView(recyclerView);
        commonAdapter.setEmptyView(R.layout.recyclerview_trade_empty_layout);
        mSmartrefreshlayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int
                    extendHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int
                    extendHeight) {
                LogUtil.d("FlashRecordFragment", "pulling" + offset);
                mCoordinatorLayout.scrollTo(0,-offset);
                recyclerView.setTranslationY(-offset);
            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {
                LogUtil.d("FlashRecordFragment", "onFooterReleased");
            }

            @Override
            public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {
                LogUtil.d("FlashRecordFragment", "Releasing" + offset);
                mCoordinatorLayout.scrollTo(0,-offset);
                recyclerView.setTranslationY(-offset);
            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

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
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

            }
        });
    }

    private String getCountString(int count) {
        return count>1000?"1000+": String.valueOf(count);
    }

    @Override
    protected void onEvent() {

    }

    @Subscriber()
    public void onRefreshEvent(BiBiRecordRefreshEvent event){
//        pageIndex = 1;
//        isloadmore = false;
//        loadData(true);
        pageIndex = 1;
        isloadmore = false;
        refreshCount = 2;
        loadData(false);
//        requestBalance();

        etOut.clearFocus();
        etIn.clearFocus();
        etOut.setText("");
        etIn.setText("");
        requestBalance();
    }
    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    protected void lazyFetchData() {
        loadData(true);
        refreshEtState();
//        InputFilter[] filters = {new CashierInputFilter()};
//        //限制用户输入
//        etOut.setFilters(filters);
//        etIn.setFilters(filters);
        observeEditText();
        requestCoinList(true);
    }

    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isloadmore = false;

    private void loadData(boolean showDialog) {
        if (showDialog)
            showDialog();
        FlashRecordListReqParam reqParam = new FlashRecordListReqParam();
        reqParam.setPageNum(String.valueOf(pageIndex));
        reqParam.setPageSize(String.valueOf(pageSize));
        mPresenter.getExchangeDataList(reqParam);
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        refreshCount --;
        refreshLayoutState(false);
        pageIndex--;
        dismissDialog();
//        ToastUtil.show(getString(R.string.server_connection_error));
        if(null != mSmartrefreshlayout) {
            if (mSmartrefreshlayout.isRefreshing())
                mSmartrefreshlayout.finishRefresh(false);
            else if (mSmartrefreshlayout.isLoading()) {
                mSmartrefreshlayout.finishLoadmore(false);
            }
        }
    }

    private void onUpdateSmartRefresh(SmartRefreshLayout smartRefreshLayout) {
        if(smartRefreshLayout == null)
            return;
        if (smartRefreshLayout.isRefreshing()) {
            smartRefreshLayout.finishRefresh();
        } else if (smartRefreshLayout.isLoading()) {
            smartRefreshLayout.finishLoadmore();
        }
    }

    private int refreshCount;
    private void refreshLayoutState(boolean isSuccess){
        if(mSmartrefreshlayout==null)
            return;
        if(mSmartrefreshlayout.isRefreshing() && refreshCount <=0){
            refreshCount = 0;
            mSmartrefreshlayout.finishRefresh(isSuccess);
        } else if (mSmartrefreshlayout.isLoading()) {
            mSmartrefreshlayout.finishLoadmore(isSuccess);
        }
    }
    @Override
    public void updateExchangeDataList(List<ExchangeDataBean> result) {
        refreshCount--;
        if(mSmartrefreshlayout == null) {
            return;
        }
        dismissDialog();
//        onUpdateSmartRefresh(mSmartrefreshlayout);
        refreshLayoutState(true);
        if(result == null || result.size()==0){
            mSmartrefreshlayout.finishLoadmoreWithNoMoreData();
            return;
        }
        if(isloadmore){
            commonAdapter.getData().addAll(result);
            commonAdapter.notifyDataSetChanged();
//            commonAdapter.addData(result);
        }else {
            commonAdapter.setNewData(result);
        }
        if(result.size()==pageSize){
            mSmartrefreshlayout.setEnableLoadmore(true);
            mSmartrefreshlayout.resetNoMoreData();
        }/*else {
            mSmartrefreshlayout.finishLoadmoreWithNoMoreData();
        }*/
    }

    private void clearAutoRefreshRateDisposable(){
        if(mAutoRefreshRateDisposable!=null && !mAutoRefreshRateDisposable.isDisposed())
            mAutoRefreshRateDisposable.dispose();
    }
    //每20秒刷新费率
    private void autoRefreshRate(){
        clearAutoRefreshRateDisposable();
        mAutoRefreshRateDisposable = Observable.interval(20, 20, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Activity currentActivity = AppManager.getInstance().currentActivity();
                        if (mPageManager.mLoadingAndRetryLayout.getRetryView().getVisibility()==View.VISIBLE
                                ||isHidden()
                                ||!getUserVisibleHint()
                                ||currentActivity==null
                                ||!(currentActivity instanceof BaseWalletActivity)
                                ||!((BaseWalletActivity)currentActivity).isRunningForeground()
                                ||!currentActivity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")) {
                            //如果正在显示或者在错误页面则不做操作
                            return;
                        }
//                        requestUpdateRate(true);
                        requestCoinList(false);
                    }
                });
        addSubscribe(mAutoRefreshRateDisposable);
    }

    private void showError(boolean isShow) {
        if (RequestType.REQ_OTHER == requesetType) {
            //其他请求不显示错误
            return;
        }
        if (isShow) {
//            contentContainer.setVisibility(View.GONE);
//            mLayoutError.setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.bg_view).setBackgroundColor(Color.TRANSPARENT);
            mPageManager.showError();
        } else {
//            contentContainer.setVisibility(View.VISIBLE);
//            mLayoutError.setVisibility(View.GONE);
            rootView.findViewById(R.id.bg_view).setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            mPageManager.showContent();
        }
    }

    /**
     * 根据选择状态刷新输入框状态
     */
    private void refreshEtState() {
        if (mLeftCoin != null) {
//            etOut.setHint(String.format(getString(R.string.please_input_out_number), mLeftCoin.getUpperCaseCoin()));
            etOut.setHint(String.format(getString(R.string.please_input_out_number), ""));
        } else {
            etOut.setHint("");
        }
        if (mRightCoin != null) {
//            etIn.setHint(String.format(getString(R.string.please_input_in_number), mRightCoin.getUpperCaseCoin()));
            etIn.setHint(String.format(getString(R.string.please_input_in_number), ""));
        } else {
            etIn.setHint("");
        }
        if (mLeftCoin != null && mRightCoin != null) {
            setEditTextEnable(etOut, true);
            setEditTextEnable(etIn, true);
        } else {
            setEditTextEnable(etOut, false);
            setEditTextEnable(etIn, false);
        }
    }

    public void setEditTextEnable(EditText et, boolean enable) {
        et.setEnabled(enable);
        et.setFocusable(enable);
        et.setFocusableInTouchMode(enable);
    }

    /**
     * @return 两个币种之间的汇率
     */
    public double getRate() {
//        return mRate == null ? 0 : mRate.getRightExchangeRate();
        return getRate(mLeftCoin, mRightCoin);
    }

    private void observeEditText() {
        mOutEtDigistController = new EditTextJudgeNumberWatcher(etOut);
        mInEtDigistController = new EditTextJudgeNumberWatcher(etIn);
        etOut.addTextChangedListener(mOutEtDigistController);
        etIn.addTextChangedListener(mInEtDigistController);
        etOut.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ((View) etOut.getParent()).setSelected(hasFocus);
            }
        });
        etIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ((View) etIn.getParent()).setSelected(hasFocus);
            }
        });
        RxTextView.textChanges(etOut)
                .debounce(debounceTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<CharSequence, ObservableSource<Double>>() {
                    @Override
                    public ObservableSource<Double> apply(CharSequence charSequence) throws Exception {
                        //先清除停止输入的监听
                        clearStopInputDisposable();
                        //根据是否有输入来判断后边的币种名称是否显示
                        if (TextUtils.isEmpty(charSequence)) {
                            mTvLeft2.setText("");
                            updateHintColor(0d);
                        } else if (mLeftCoin != null) {
                            mTvLeft2.setText(mLeftCoin.getUpperCaseCoin());
                            addStopInputObservable();
                        }
                        //非focus不处理
                        if (!etOut.isFocused())
                            return Observable.empty();
                        //两方状态同步
                        if (TextUtils.isEmpty(charSequence.toString())) {
                            if (!TextUtils.isEmpty(etIn.getText())) {
                                etIn.setText("");
                                clearHintColor();
                            }
                            return Observable.empty();
                        }
                        //正常计算
                        return Observable.just(getDoubleValue(etOut));
                    }
                })
                .subscribe(new Consumer<Double>() {
                    @Override
                    public void accept(Double value) throws Exception {
                        updateHintColor(value);
                        updateIn(value);
                    }
                });

        RxTextView.textChanges(etIn)
                .debounce(debounceTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .flatMap(new Function<CharSequence, ObservableSource<Double>>() {
                    @Override
                    public ObservableSource<Double> apply(CharSequence charSequence) throws Exception {
                        clearStopInputDisposable();
                        if (TextUtils.isEmpty(charSequence)) {
                            mTvRight2.setText("");
                        } else if (mRightCoin != null) {
                            mTvRight2.setText(mRightCoin.getUpperCaseCoin());
                            addStopInputObservable();
                        }
                        if (!etIn.isFocused())
                            return Observable.empty();
                        if (TextUtils.isEmpty(charSequence.toString())) {
                            if (!TextUtils.isEmpty(etOut.getText())) {
                                etOut.setText("");
                                clearHintColor();
                            }
                            return Observable.empty();
                        }
                        return Observable.just(getDoubleValue(etIn));
                    }
                })
                .subscribe(new Consumer<Double>() {
                    @Override
                    public void accept(Double value) throws Exception {
                        updateOut(value);
                    }
                });
    }

    private void clearStopInputDisposable() {
        if (mStopInputDisposable != null && !mStopInputDisposable.isDisposed())
            mStopInputDisposable.dispose();
    }

    Disposable mStopInputDisposable;

    /**
     * 每次输入重新启动记时
     */
    private void addStopInputObservable() {
        addSubscribe(mStopInputDisposable = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        resetEtOutWithLegal();
                    }
                }));
    }

    /**清除所有提示颜色*/
    private void clearHintColor() {
        tvBalance.setSelected(false);
        tvMax.setSelected(false);
        tvMin.setSelected(false);
    }

    /**
     * 通过value判断各个提示的颜色
     * @param value
     */
    private void updateHintColor(Double value) {
        /*//超过可用余额，变红
        if (value.compareTo(availableBalance) > 0) {
            tvBalance.setSelected(true);
        } else {
            tvBalance.setSelected(false);
        }
        //小于最小，变红
        if (value.compareTo(minExchange) < 0) {
            tvMin.setSelected(true);
        } else {
            tvMin.setSelected(false);
        }
        //大于最大，变红
        if (value.compareTo(maxExchange) > 0) {
            tvMax.setSelected(true);
        } else {
            tvMax.setSelected(false);
        }*/
        if (isAmountCanExchange(value)) {
            btnExchange.setEnabled(true);
        } else if (availableBalance < minExchange && !TextUtils.isEmpty(etOut.getText())/* && getDoubleValue(etOut) <= availableBalance*/) {
            //限制改为判断只要可用小于最小兑出,并且兑出输入框不为空，则为可点击状态
            btnExchange.setEnabled(true);
        } else {
            btnExchange.setEnabled(false);
        }
    }

    /**
     * 将输出的edittext重置到可以交易的
     */
    private void resetEtOutWithLegal() {
        Double curAmount = getDoubleValue(etOut);
        if (isAmountCanExchange(curAmount)) {
            //金额可以交易不需要重置
            return;
        }
        double legalAmount = 0;
        if (availableBalance < minExchange) {
            //出现这种情况，说明不可交易，
            //目前做法是可以点击兑换按钮来提示，但是提示用户余额不足
            btnExchange.setEnabled(true);
            /*if(curAmount<minExchange){
                legalAmount = minExchange;
            }else if(curAmount>maxExchange){
                legalAmount = maxExchange;
            }else {
                return;
            }*/
            //这里目前需求是把需要的值设置为这个可用余额
            legalAmount = availableBalance;
            if (curAmount == legalAmount) {
                return;
            }
            etOut.setText(String.valueOf(Utils.toSubStringNo(legalAmount, digest)));
            if (etOut.isFocused()) {
                etOut.setSelection(etOut.getText().length());
            } else {
                etIn.clearFocus();
                updateIn(legalAmount);
                updateHintColor(legalAmount);
                //这里需要延迟一下，不然还是在监听里面判断还是focus状态，导致又重新设置输出et
                UIUtils.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        etIn.requestFocus();
                    }
                }, 10);
            }
            return;
        } else {
            if (curAmount < minExchange) {
                legalAmount = minExchange;
            } else {
                if (availableBalance > maxExchange) {
                    legalAmount = maxExchange;
                } else {
                    legalAmount = availableBalance;
                }
            }
        }
        //计算出来的与目前的值相同，则不处理,为0也不处理
        if (curAmount == legalAmount || curAmount == 0) {
            return;
        }
        etOut.setText(String.valueOf(Utils.toSubStringNo(legalAmount, digest)));
        if (etOut.isFocused()) {
            etOut.setSelection(etOut.getText().length());
        } else {
            etIn.clearFocus();
            updateIn(legalAmount);
            updateHintColor(legalAmount);
            //这里需要延迟一下，不然还是在监听里面判断还是focus状态，导致又重新设置输出et
            UIUtils.postDelayed(new Runnable() {
                @Override
                public void run() {
                    etIn.requestFocus();
                    etIn.setSelection(etIn.getText().length());
                }
            }, 10);
        }

    }

    /**
     * @param value
     * @return 该金额是否可以交易
     */
    private boolean isAmountCanExchange(Double value) {
        return (value.compareTo(availableBalance) <= 0) && (value.compareTo(minExchange) >= 0) && (value.compareTo
                (maxExchange) <= 0);
    }

    private int digest = 4;
    private int toDigest = 4;
    private int rateDigest = 4;
    private final static int DEFAULT_DIGEST = 4;


    /**
     * 通过兑入值计算兑出值，并设置颜色
     * @param value
     */
    private void updateOut(Double value) {
        if (value.compareTo(0d) == 0 || getRate() == 0) {
            etOut.setText(Utils.toSubStringNo(0d, toDigest));
            return;
        }

        etOut.setText(Utils.toSubStringNo(new BigDecimal(Double.toString(value)).divide(new BigDecimal(Double.toString(getRate())), digest, BigDecimal.ROUND_DOWN).doubleValue(), digest));
        updateHintColor(getDoubleValue(etOut));
    }

    /**
     * 通过输入兑出币种的金额来计算兑入币种金额
     * @param value
     */
    private void updateIn(Double value) {
        if (value.compareTo(0d) == 0) {
            etIn.setText(Utils.toSubStringNo(0d, toDigest));
            etIn.setSelection(etIn.getText().length());
            return;
        }
        etIn.setText(Utils.toSubStringNo(Utils.mul(value, getRate()), toDigest));
//        etIn.setSelection(etIn.getText().length());
    }

    private Double getDoubleValue(EditText editText) {
        if (TextUtils.isEmpty(editText.getText())) {
            return 0d;
        }
        Double aDouble = null;
        try {
            aDouble = Double.valueOf(editText.getText().toString().replace(",", ""));
        } catch (NumberFormatException e) {
            aDouble = 0d;
            e.printStackTrace();
        }
        return aDouble;
    }

    private RequestType requesetType;

    private enum RequestType {
        //请求CoinList
        REQ_COIN_LIST,
        //请求Rate
        REQ_RATE,
        //请求兑出币种余额
        REQ_BALANCE,
        //请求其他不需要重试的
        REQ_OTHER
    }

    private void requestCoinList(boolean showDialog) {
//        showDialog();
        requesetType = RequestType.REQ_COIN_LIST;
        mGetExchangeCoinListProviderServices.requestCoinList()
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<CoinExchangeBean>(this, showDialog) {
                    @Override
                    public void onSuccess(CoinExchangeBean data) {
                        updateCoinList(data.getCoinList(), data.getSortList());
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        showError(true);
                    }
                });
    }

    private void requestBalance() {
        if (mLeftCoin == null) {
            refreshCount--;
            refreshLayoutState(false);
            return;
        }
//        showDialog();
        if(dialog!=null && !mSmartrefreshlayout.isRefreshing() && !BuildConfig.DEBUG){
            dialog.setBackPressEnable(false);
        }
        clearAutoRefreshRateDisposable();
        if(mReqBalanceDisposable!=null && !mReqBalanceDisposable.isDisposed())
            mReqBalanceDisposable.dispose();
        requesetType = RequestType.REQ_BALANCE;
        mReqBalanceDisposable = mGetWalletBalanceProviderServices.requestNewBalanceProvider(mLeftCoin.getCoin())
                .retry(2)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(dialog!=null && !BuildConfig.DEBUG){
                            dialog.setBackPressEnable(true);
                        }
                    }
                })
                .subscribeWith
                        (new RxProgressSubscriber<NewBalanceBean>(this, !mSmartrefreshlayout.isRefreshing()) {
                            @Override
                            protected void onError(ResponseThrowable ex) {
                                refreshCount--;
//                        dismissDialog();
                                showError(true);
                                refreshLayoutState(false);
                            }

                            @Override
                            public void onSuccess(NewBalanceBean newBalanceBean) {
                                refreshCount--;
                                showError(false);
                                availableBalance = newBalanceBean.getCurrentBalance();
                                //这里需要处理成4位
                                availableBalance = new BigDecimal(availableBalance).setScale(4, BigDecimal
                                        .ROUND_DOWN).doubleValue();
                                tvBalance.setText(Utils.toSubStringDegist(availableBalance, 4) + " " + mLeftCoin
                                        .getUpperCaseCoin());
//                        tvBalance.setText(String.format(getString(R.string.exchange_available_balance), Utils
//                                .toNoPointSubStringDegistIfDegistIsZero(availableBalance, digest), mLeftCoin
//                                .getUpperCaseCoin()));
//                        requestUpdateRate(false);
                                autoRefreshRate();
                                refreshLayoutState(true);
                            }
                        });
    }
    @OnClick({R.id.img_code, R.id.rl_left, R.id.btn_swap, R.id.rl_right, R.id.btn_exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_code:
                BiBiHelpPopWindow biBiHelpPopWindow = new BiBiHelpPopWindow(mContext);
                biBiHelpPopWindow.showWithText
                        (getString(R.string.bibi_help_title), getString(R.string.bibi_help_content));
                break;
            case R.id.btn_swap:
                Animation circle_anim = AnimationUtils.loadAnimation(mContext, R.anim.tip_img);
                LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
                circle_anim.setInterpolator(interpolator);
                btnSwap.startAnimation(circle_anim);  //开始动画
                textAnim();
                btnSwap.setEnabled(false);
                break;
            case R.id.rl_left:
            case R.id.rl_right:
                showChooseCoinPopWin(view);
                break;
            case R.id.btn_exchange:
                exchange();

                break;
        }
    }

    @Autowired
    ExchangeInProviderServices mExchangeInProviderServices;

    private void exchange() {
        if(mLeftCoin == null || mRightCoin == null)
            return;
        Double value = getDoubleValue(etOut);
        if (value.compareTo(availableBalance) > 0 || availableBalance < minExchange) {
            ToastUtil.show(getString(R.string.exchange_more_then_max_hint));
            return;
        }
        //最大兑出为0，或者最小兑出为0，并且输入的值是0
        if(maxExchange==0 || getDoubleValue(etIn)==0 && minExchange==0){
            ToastUtil.show(getString(R.string.not_support_pair));
            return;
        }
        if (value.compareTo(minExchange) < 0 || value.compareTo(maxExchange) > 0 || getDoubleValue(etIn)==0) {
            ToastUtil.show(getString(R.string.exchange_less_then_min_hint));
            return;
        }
        /*showDialog();
        Observable.just(shotActivity(this))
                .doOnNext(bitmap -> {
                    //保存pin页面需要的高斯模糊背景
                    ACacheUtil.get(mContext).put("bibiflashbg", EasyBlur.with(mContext)
                            .bitmap(bitmap) //要模糊的图片
                            .policy(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? EasyBlur
                                    .BlurPolicy.RS_BLUR : EasyBlur.BlurPolicy.FAST_BLUR)
                            .radius(25)//模糊半径
                            .blur());
                    //保存需要交易完成界面的币种logo图
                    ACacheUtil.get(mContext).put("exchange_logo", mLeftCoin.getLogo());
                    ACacheUtil.get(mContext).put("exchange_to_logo", mRightCoin.getLogo());
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    dismissDialog();
                    ARouter.getInstance()
                            .build(ArouterConstants.CheckFlashPin)
                            .withParcelable("mExchangeInParam", new ExchangeInParam(mRightCoin.getCoin(),
                                    getDoubleValue(etIn), mRate.getExchangeRate(),
                                    null, null,
                                    getDoubleValue(etOut), null, mLeftCoin.getCoin(), "", LoginUtils.getLoginBean(mContext).getDevice_id(),mRate.isDirection()))
                            .withParcelable("mRate", mRate)
                            .navigation();
                    finish();
                });*/
        ACacheUtil.get(mContext).put("exchange_logo", mLeftCoin.getLogo());
        ACacheUtil.get(mContext).put("exchange_to_logo", mRightCoin.getLogo());

        mExchangeInParam = new ExchangeInParam(mRightCoin.getCoin(),
                getDoubleValue(etIn), getRate(),
                null, null,
                getDoubleValue(etOut), null, mLeftCoin.getCoin(), "", LoginUtils.getLoginBean(mContext).getDevice_id(),
                getDirection(), digest, toDigest, getRateDigit());
        onCheckPinSuccess("");
    }

    private void onCheckPinSuccess(String pinPass) {
        if(mExchangeInParam==null)
            return;
        mExchangeInParam.setPayPassword(pinPass);
        btnExchange.setEnabled(false);
        requesetType = RequestType.REQ_OTHER;
        mExchangeInProviderServices.exchangeIn(mExchangeInParam)
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<ExchangeResultBean>(this) {
                    @Override
                    public void onSuccess(ExchangeResultBean data) {
                        btnExchange.setEnabled(true);
                        int statusCode = data.getStatusCode();
                        if(statusCode == 0){
                            exchangeSuccess(data.getTxId());
                        }else if(3 == statusCode
                                ||102 == statusCode
                                ||107 == statusCode
                                ||108 == statusCode
                                ||106 == statusCode){
                            ToastUtil.show(getString(R.string.balance_enough));
                        }else if(1 == statusCode){
                            ToastUtil.show(getString(R.string.eth_not_enough));
                        }else if(9 == statusCode){
                            String minAmount;
                            if(mLeftCoin.getCoin().equalsIgnoreCase("LTC")){
                                minAmount = "0.0006";
                                ToastUtil.show(getString(R.string.most_amount_enable));
                            }else {
                                minAmount = "0.00006";
                                ToastUtil.show(getString(R.string.most_amount_more_enable));
                            }
                        }else if(800 == statusCode){
                            ToastUtil.show(getString(R.string.tv_pedding_status));
                        }else if(802 == statusCode){
                            ToastUtil.show(getString(R.string.otc_no_finish_order));
                        }else{
//                    ToastUtil.show(getString(R.string.server_connection_error));
                            ToastUtil.show(getString(R.string.bibi_exchange_failed));
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        btnExchange.setEnabled(true);
                        if(ex.getErrorCode().equals("121")){
                            UIUtils.post(() -> {
                                ToastUtil.show(ex.getErrorMsg());
//                                onBackPressed();
                            });
                        }else{
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }
                });
    }
    public void exchangeSuccess(String txId) {
        ARouter.getInstance().build(ArouterConstants.FlashResult)
                .withParcelable("mExchangeInParam", mExchangeInParam)
                .withInt("digit", digest)
                .withInt("toDigit", toDigest)
                .navigation();
//        finish();
    }

    private void showChooseCoinPopWin(View view) {
        if (mCoinPairsMap.size()==0)
            return;
        switch (view.getId()) {
            case R.id.rl_left:
                showCoinChoose(true);
                break;
            case R.id.rl_right:
//                showRightChoose();
                showCoinChoose(false);
                break;
            default:
                return;
        }

    }

    private static final String BTC = "btc";

    /**
     * 展示选择的框，
     * @param isLeft 是不是点击了左边
     */
    private void showCoinChoose(boolean isLeft) {
        if(mLeftCoin==null || mRightCoin == null)
            return;
        CoinBean modifyBean;
        CoinBean anotherBean;
        if (isLeft) {
            modifyBean = mLeftCoin;
            anotherBean = mRightCoin;
        } else {
            modifyBean = mRightCoin;
            anotherBean = mLeftCoin;
        }
        ChooseCoinTypePopWindow chooseCoinTypePopWindow = new ChooseCoinTypePopWindow(mContext);
        chooseCoinTypePopWindow.showPopWindowForData(Observable.fromIterable(isLeft?mCoinBeanMap.keySet(): mCoinPairsMap.get(anotherBean.getCoin()))
                .map(new Function<String, CoinBean>() {
                    @Override
                    public CoinBean apply(String s) throws Exception {
                        return mCoinBeanMap.get(s);
                    }
                })
                .filter(new Predicate<CoinBean>() {
                    @Override
                    public boolean test(CoinBean bean) throws Exception {
                        //如果点击右边，左边是BTC，那么不能有BTC选项
//                        return !(modifyBean == mBtcCoin && mBtcCoin == bean || modifyBean == bean);
                        return true;
                    }
                }).toList().blockingGet(), getString(isLeft ? R.string.choose_exchange_in_coin_type : R.string
                .choose_exchange_out_coin_type), modifyBean, anotherBean);
        //下面这种是如果需要判断已选设置不可点击的时候启用
//        chooseCoinTypePopWindow.showPopWindowForData(mCoins, getString(isLeft ? R.string
// .choose_exchange_in_coin_type : R.string
//                .choose_exchange_out_coin_type), modifyBean, anotherBean);
        chooseCoinTypePopWindow.setOnItemClickListener((bean) -> {
            /*//更新左边，然后更新右边
            updatePart(bean, isLeft);
            if (!atLeastOneBeans.contains(bean) && !atLeastOneBeans.contains(anotherBean)) {
                //如果如果选择的不是市场币，则将另外一个置为btc
                updatePart(mBtcCoin, !isLeft);
            } else if (bean == anotherBean) {
                //选择相同的，则为互换
                updatePart(modifyBean, !isLeft);
            }
            etOut.setText("");
            etIn.setText("");
            mTvLeft2.setText("");
            mTvRight2.setText("");
            refreshEtState();
//            requestUpdateRate();
            requestBalance();*/
            updatePart(bean, isLeft);
            if (bean.getCoin().equalsIgnoreCase(anotherBean.getCoin())) {
                //选择相同的，则为互换
                updatePart(modifyBean, !isLeft);
            }else if(!mCoinPairsMap.get(anotherBean.getCoin()).contains(bean.getCoin())){
                //如果点击左边的，不与右边的形成交易对，则把右边替换成btc
                updatePart(mBtcCoin, !isLeft);
            }
            if(isLeft || bean == anotherBean){
                requestBalance();
            }
            etOut.setText("");
            etIn.setText("");
            refreshEtState();
            refreshUi();
        });
    }

    /**
     * 更新选中
     * @param nowBean
     * @param left 是不是更新左边
     */
    private void updatePart(CoinBean nowBean, boolean left) {
        String currency = "";
        String logo = "";
        if (nowBean != null) {
            currency = nowBean.getUpperCaseCoin();
            logo = nowBean.getLogo();
        }
        if (left) {
            mLeftCoin = nowBean;
            tvLeft.setText(currency);
//            mTvLeft2.setText(currency);
            GlideUtil.loadImageView(mContext, logo, imgLeft);
        } else {
            mRightCoin = nowBean;
            tvRight.setText(currency);
//            mTvRight2.setText(currency);
            GlideUtil.loadImageView(mContext, logo, imgRight);
        }
    }

    private void refreshUi(){
        if(mLeftCoin == null || mRightCoin == null)
            return;
        rateDigest = getRateDigit(mLeftCoin, mRightCoin);
//        <b>1</b> %s = <b>%s</b> %s
        tvRate.setText(Html.fromHtml(String.format("1 %s = %s %s", tvLeft.getText(), Utils
                .toNoPointSubStringDegistIfDegistIsZero(getRate(mLeftCoin, mRightCoin), rateDigest), tvRight.getText())));

        digest = getDigit(mLeftCoin, mRightCoin).first;
        toDigest = getDigit(mLeftCoin, mRightCoin).second;

        Pair<Double, Double> scopePair = getScope(mLeftCoin, mRightCoin);
        maxExchange = scopePair.second;
        minExchange = scopePair.first;
        /*此处修复后台返回精度与实际最小值的精度不等*/
//        String minStr = String.valueOf(minExchange);
//        if(minStr.contains(".")){
//            String strAftreDot = minStr.substring(minStr.indexOf(".") + 1);
//            if(strAftreDot.length()>digest){
//                minExchange = new BigDecimal(minExchange).setScale(digest, BigDecimal.ROUND_UP).doubleValue();
//            }
//        }
        //4.24 根据产品确认，如果返回位数大于精度，统一最大最小都是四舍五入
        String minStr = String.valueOf(minExchange);
        if(minStr.contains(".")){
            String strAftreDot = minStr.substring(minStr.indexOf(".") + 1);
            if(strAftreDot.length()>digest){
                minExchange = new BigDecimal(minExchange).setScale(digest, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        String maxStr = String.valueOf(maxExchange);
        if(maxStr.contains(".")){
            String strAftreDot = maxStr.substring(maxStr.indexOf(".") + 1);
            if(strAftreDot.length()>toDigest){
                maxExchange = new BigDecimal(maxExchange).setScale(digest, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        /*此处修复后台返回精度与实际最小值的精度不等*/


        tvMin.setText(Utils.toNoPointSubStringDegistIfDegistIsZero(minExchange, digest)+" "+ mLeftCoin.getUpperCaseCoin());
        tvMax.setText( Utils.toNoPointSubStringDegistIfDegistIsZero(maxExchange, digest)+" "+mLeftCoin.getUpperCaseCoin());
//            tvMin.setText(String.format(getString(R.string.exchange_min_with_value), Utils.toNoPointSubStringDegistIfDegistIsZero(minExchange, digest), mLeftCoin.getUpperCaseCoin()));
//            tvMax.setText(String.format(getString(R.string.exchange_max_with_value), Utils.toNoPointSubStringDegistIfDegistIsZero(maxExchange, digest), mLeftCoin.getUpperCaseCoin()));

        mOutEtDigistController.setPointerLength(digest);

        mInEtDigistController.setPointerLength(toDigest);

        if (etIn.isFocused()) {
            if (TextUtils.isEmpty(etIn.getText())) {
                return;
            }
            if(checkAndResetInAboutWithDigest()){
                //如果小数点超限，只要修改当前输入框，则另外一个输入框也会跟着改变
                return;
            }
            //更新费率时先根据兑入值计算兑出值，再检验合法
            Double doubleValue = getDoubleValue(etIn);
            updateOut(doubleValue);
            resetEtOutWithLegal();
        } else {
            if (!TextUtils.isEmpty(etOut.getText())) {
                //如果小数点超限，只要修改当前输入框，则另外一个输入框也会跟着改变
                if(checkAndResetOutAboutWithDigest()){
                    return;
                }
                updateIn(getDoubleValue(etOut));
                updateHintColor(getDoubleValue(etOut));
            } else {
                clearHintColor();
            }
        }
    }

    /**
     * 获取tv的属性,计算偏移量,
     */
    private void textAnim() {

        //获取tv控件距离父控件的位置
//        int leftRight = rlLeft.getRight();
//        int rightLeft = rlRight.getLeft();
//
//        //包裹右侧tv距离父控件的距离
//        int rlRight = this.rlRight.getRight();
//        int rlLeft = this.rlLeft.getLeft();
//        //在哪里设的padding就要用哪个控件来获取padding值
//        int paddingStart = mLl.getPaddingStart();
//
//        LogUtil.d("AddressActivity", "paddingStart:" + paddingStart);
//
//        //左侧textview需要移动的距离
//        int leftOffset = rlRight - leftRight - paddingStart;
//        //右侧textview需要移动的距离
//        int rightOffset = rlLeft + rightLeft - paddingStart;

        //创建出镜像view
        createCopyView();

        //隐藏掉两边的tv
        this.rlLeft.setVisibility(View.INVISIBLE);
        this.rlRight.setVisibility(View.INVISIBLE);

        int[] rightArrowLoc = new int[2];
        mIvRightArrow.getLocationInWindow(rightArrowLoc);
        int[] leftArrowLoc = new int[2];
        mIvLeftArrow.getLocationInWindow(leftArrowLoc);
        //左侧textview需要移动的距离
        int leftOffset = rightArrowLoc[0]-leftArrowLoc[0];
        //右侧textview需要移动的距离
        int rightOffset = mRightLocation[0] - mLeftLocation[0] + imgRight.getLeft();
//
//        //左侧textview需要移动的距离
//        int leftOffset = mRightLocation[0] - mLeftLocation[0] + imgRight.getLeft();
//        //右侧textview需要移动的距离
//        int rightOffset = leftOffset;

        //开启镜像view的动画
        leftAnim(leftOffset, mLeftLocation[0]);
        int[] imgRightLocation = new int[2];
        imgRight.getLocationInWindow(imgRightLocation);
        rightAnim(rightOffset, imgRightLocation[0]);
    }

    /**
     * 创建镜像view
     */
    private void createCopyView() {
        mLeftLocation = new int[2];
        mRightLocation = new int[2];
        //获取相对window的坐标
        rlLeft.getLocationInWindow(mLeftLocation);
        rlRight.getLocationInWindow(mRightLocation);

        //获取左边tv的缓存bitmap
        rlLeft.setDrawingCacheEnabled(true);
        mLeftCacheBitmap = Bitmap.createBitmap(rlLeft.getDrawingCache());
        rlLeft.destroyDrawingCache();
        //截取从箭头往左的bitmap避免无法移动
        mLeftCacheBitmap = Bitmap.createBitmap(mLeftCacheBitmap, 0,0,mIvLeftArrow.getWidth()+mIvLeftArrow.getLeft(), mLeftCacheBitmap.getHeight());
        //获取右边tv的缓存bitmap
        rlRight.setDrawingCacheEnabled(true);
        mRightCacheBitmap = Bitmap.createBitmap(rlRight.getDrawingCache());
        rlRight.destroyDrawingCache();
        //截取从右边imgRight控件开始往右边的bitmap
        mRightCacheBitmap = Bitmap.createBitmap(mRightCacheBitmap, imgRight.getLeft(), 0, mRightCacheBitmap.getWidth()-imgRight.getLeft(), mRightCacheBitmap.getHeight());

        //创建出两个镜像view
        copyViewLeft = createCopyView(mLeftLocation[0], mLeftLocation[1], mLeftCacheBitmap);
        copyViewRight = createCopyView(mRightLocation[0]+imgRight.getLeft(), mRightLocation[1], mRightCacheBitmap);
        //释放bitmap资源...这我不确定是不是这么做
        mLeftCacheBitmap = null;
        mRightCacheBitmap = null;
    }

    /**
     * 左侧镜像view的动画
     * @param offset    偏移量
     * @param defX      原始位置的x
     */
    private void leftAnim(int offset, final int defX) {
        ValueAnimator leftAnimV = ValueAnimator.ofInt(0, offset);
        leftAnimV.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) copyViewLeft.getLayoutParams();
                //往右边移动所以x是变大的
                layoutParams.x = defX + animatedValue;
                mWindowManager.updateViewLayout(copyViewLeft, layoutParams);
            }
        });
        leftAnimV.setDuration(300);
        leftAnimV.start();
        //左侧动画监听
        leftAnimV.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //改变值
                rlLeft.setVisibility(View.VISIBLE);
                mWindowManager.removeView(copyViewLeft);
                copyViewLeft = null;
                btnSwap.setEnabled(true);

                CoinBean cacheBean = mLeftCoin;
                updatePart(mRightCoin, true);
                updatePart(cacheBean, false);
                etOut.setText("");
                etIn.setText("");
                requestBalance();
                refreshEtState();
                refreshUi();
            }
        });
    }

    /**
     * 右侧镜像view动画
     * @param offset    偏移量
     * @param defX      原始位置的x
     */
    private void rightAnim(int offset, final int defX) {
        ValueAnimator rightAnimV = ValueAnimator.ofInt(0, offset);
        rightAnimV.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) copyViewRight.getLayoutParams();
                layoutParams.x = defX - animatedValue;
                mWindowManager.updateViewLayout(copyViewRight, layoutParams);
            }
        });
        rightAnimV.setDuration(300);
        rightAnimV.start();
        rightAnimV.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rlRight.setVisibility(View.VISIBLE);
                mWindowManager.removeView(copyViewRight);
                copyViewRight = null;
            }
        });
    }

    /**
     * 创建镜像view
     *
     * @param x
     * @param y
     * @param bitmap
     */
    private ImageView createCopyView(int x, int y, Bitmap bitmap) {
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.TRANSLUCENT;            //图片之外其他地方透明
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.x = x;   //设置imageView的原点
        mLayoutParams.y = y - getStatusHeight(mContext);
        mLayoutParams.alpha = 1f;                                //设置透明度
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        ImageView copyView = new ImageView(mContext);
        copyView = new ImageView(mContext);
        copyView.setImageBitmap(bitmap);
        mWindowManager.addView(copyView, mLayoutParams);   //添加该iamgeView到window
        return copyView;
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 根据指定的Activity截图（带空白的状态栏）
     *
     * @param context 要截图的Activity
     * @return Bitmap
     */
    public static Bitmap shotActivity(Activity context) {
        View view = context.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view
                .getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    public void updateCoinList(List<CoinPairBean> coinBeans, List<CoinBean> sortList) {
        if(coinBeans==null || sortList==null){
            showError(true);
            return;
        }
        //转小写是为了后台总是有时候返回大写，有时候返回小写。。。无敌
        for (CoinPairBean bean : coinBeans) {
        	bean.setFromCoin(bean.getFromCoin().toLowerCase(Locale.ENGLISH));
        	bean.setToCoin(bean.getToCoin().toLowerCase(Locale.ENGLISH));
        }
        for (CoinBean bean : sortList) {
        	bean.setCoin(bean.getCoin().toLowerCase(Locale.ENGLISH));
        }
        showError(false);
        mSortCoins.clear();
        mCoinBeanMap.clear();
        mCoinPairsMap.clear();
        //清空所有汇率
        mCoinPairRateMap.clear();
        mDefaultCoinPairBean = null;
        Observable.fromIterable(sortList)
                .map(new Function<CoinBean, String>() {
                    @Override
                    public String apply(CoinBean bean) throws Exception {
                        mSortCoins.put(bean.getCoin(), bean);
                        return bean.getCoin();
                    }
                })
                .toList()
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                    }
                });
        Observable.fromIterable(coinBeans)
                .doOnNext(new Consumer<CoinPairBean>() {
                    @Override
                    public void accept(CoinPairBean coinPairBean) throws Exception {
                        String fromCoin = coinPairBean.getFromCoin();
                        int fromDigit = coinPairBean.getFromDigit();
                        double fromExchangeMax = coinPairBean.getFromExchangeMax();
                        double fromExchangeMin = coinPairBean.getFromExchangeMin();
                        String fromLogo = coinPairBean.getFromLogo();
                        boolean fromIsMarket = coinPairBean.isFromIsMarket();
                        boolean fromIsSupportExchange = coinPairBean.isFromIsSupportExchange();

                        CoinBean fromCoinBean = new CoinBean(fromCoin, fromIsSupportExchange, fromLogo, fromIsMarket,
                                fromExchangeMin, fromExchangeMax, fromDigit);
                        int sort = 0;
                        try {
                            sort = mSortCoins.get(fromCoin).getSort();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        fromCoinBean.setSort(sort);

                        String toCoin = coinPairBean.getToCoin();
                        int toDigit = coinPairBean.getToDigit();
                        double toExchangeMax = coinPairBean.getToExchangeMax();
                        double toExchangeMin = coinPairBean.getToExchangeMin();
                        String toLogo = coinPairBean.getToLogo();
                        boolean toIsMarket = coinPairBean.isToIsMarket();
                        boolean toIsSupportExchange = coinPairBean.isToIsSupportExchange();

                        CoinBean toCoinBean = new CoinBean(toCoin, toIsSupportExchange, toLogo, toIsMarket,
                                toExchangeMin, toExchangeMax, toDigit);
                        int toSort = 0;
                        try {
                            toSort = mSortCoins.get(toCoin).getSort();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        toCoinBean.setSort(toSort);


                        if(mCoinPairsMap.get(fromCoin)==null){
                            mCoinPairsMap.put(fromCoin, new TreeSet<>());
                        }
                        Set<String> fromSet = mCoinPairsMap.get(fromCoin);
                        fromSet.add(toCoinBean.getCoin());
                        if(mCoinPairsMap.get(toCoin)==null){
                            mCoinPairsMap.put(toCoin, new TreeSet<>());
                        }
                        Set<String> toSet = mCoinPairsMap.get(toCoin);
                        toSet.add(fromCoinBean.getCoin());

//                        if(!mCoinBeanMap.containsKey(fromCoin))
                            mCoinBeanMap.put(fromCoin, fromCoinBean);
//                        if(!mCoinBeanMap.containsKey(toCoin))
                            mCoinBeanMap.put(toCoin, toCoinBean);

                        double exchangeRate = coinPairBean.getExchangeRate();
                        double toExchangeRate = coinPairBean.getToExchangeRate();

                        putRate(fromCoinBean, toCoinBean, exchangeRate);
                        putRate(toCoinBean, fromCoinBean, toExchangeRate);

                        putDigit(fromCoinBean, toCoinBean, fromDigit, toDigit);
                        putDigit(toCoinBean, fromCoinBean, toDigit, fromDigit);

                        int rateDigit = coinPairBean.getRateDigit();

                        putRateDigit(fromCoinBean, toCoinBean, rateDigit);
                        putRateDigit(toCoinBean, fromCoinBean, rateDigit);

                        putDirection(fromCoinBean, toCoinBean);

                        putScope(fromCoinBean, toCoinBean, fromExchangeMin, fromExchangeMax);
                        putScope(toCoinBean, fromCoinBean, toExchangeMin, toExchangeMax);

                        if(BTC.equalsIgnoreCase(fromCoin)){
                            mBtcCoin = fromCoinBean;
                        }else if(BTC.equalsIgnoreCase(toCoin)){
                            mBtcCoin = toCoinBean;
                        }

                        if (coinPairBean.isDefault()) {
                            mDefaultCoinPairBean = coinPairBean;
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(mLeftCoin!=null && mRightCoin!=null && getRate()==0){
                            //如果更新了汇率，不返回该交易对，那么就先置空，然后再设置默认
                            updatePart(null, true);
                            updatePart(null, false);
                        }
                        if((mLeftCoin == null || mRightCoin == null )&&null != mDefaultCoinPairBean){
                            //如果都是空，说明是第一次，或者被置空了，将默认的赋值进去
                            String fromCoin = mDefaultCoinPairBean.getFromCoin();
                            String toCoin = mDefaultCoinPairBean.getToCoin();

                            if(BTC.equalsIgnoreCase(toCoin)){
                                //如果右边是btc，则切换位置
                                updatePart(mCoinBeanMap.get(toCoin), true);
                                updatePart(mCoinBeanMap.get(fromCoin), false);
                            }else {
                                updatePart(mCoinBeanMap.get(fromCoin), true);
                                updatePart(mCoinBeanMap.get(toCoin), false);
                            }
                            requestBalance();
                        }
                        //这里如果是没有返回default就会取第一对键值对，如果还没。。。那就都是kong
                        if(mLeftCoin==null || mRightCoin==null){
                            /*for (String coin  : mCoinPairsMap.keySet()) {
                                Set<String> strings = mCoinPairsMap.get(coin);
                                for (String toCoin : strings) {
                                    updatePart(mCoinBeanMap.get(coin), true);
                                    updatePart(mCoinBeanMap.get(toCoin), false);
                                    break;
                                }
                            }*/
                            //5.12修改：取sortcoins的第一个，然后取然后取对应的第一个
                            Iterator<String> iterator = mSortCoins.keySet().iterator();
                            if(iterator.hasNext()) {
                                String coin = iterator.next();
                                List<CoinBean> list = Observable.fromIterable(mCoinPairsMap.get(coin)).map(new Function<String, CoinBean>() {

                                    @Override
                                    public CoinBean apply(String s) throws Exception {
                                        return mCoinBeanMap.get(s);
                                    }
                                }).toList().blockingGet();
                                Collections.sort(list);
                                updatePart(mCoinBeanMap.get(coin), true);
                                updatePart(list.get(0), false);
                                requestBalance();
                            }
                        }
                        refreshEtState();
                        refreshUi();
                    }
                })
                .subscribe();
    }

    private void putScope(CoinBean fromCoin, CoinBean toCoin, double minExchange, double maxExchange){
        mCanExchangeScope.put(fromCoin.getCoin()+toCoin.getCoin(), new Pair<>(minExchange, maxExchange));
    }
    private Pair<Double, Double> getScope(CoinBean fromCoin, CoinBean toCoin){
        return mCanExchangeScope.get(fromCoin.getCoin()+toCoin.getCoin());
    }
    private void putDigit(CoinBean fromCoin, CoinBean toCoin, int digit, int toDigit){
        digitMap.put(fromCoin.getCoin()+toCoin.getCoin(), new Pair<>(digit, toDigit));
    }
    private Pair<Integer, Integer> getDigit(CoinBean fromCoin, CoinBean toCoin){
        return digitMap.get(fromCoin.getCoin()+toCoin.getCoin());
    }

    private void putDirection(CoinBean fromCoin, CoinBean toCoin){
        mDirectionSet.add(fromCoin.getCoin()+toCoin.getCoin());
    }
    private boolean getDirection(){
        return mDirectionSet.contains(mLeftCoin.getCoin()+mRightCoin.getCoin());
    }

    private void putRate(CoinBean fromCoin, CoinBean toCoin, double rate){
        mCoinPairRateMap.put(fromCoin.getCoin()+toCoin.getCoin(), rate);
    }
    private double getRate(CoinBean fromCoin, CoinBean toCoin){
        Double aDouble = mCoinPairRateMap.get(fromCoin.getCoin() + toCoin.getCoin());
        return null == aDouble?0:aDouble;
    }
    private void putRateDigit(CoinBean fromCoin, CoinBean toCoin, int digit){
        mCoinPairRateDigitMap.put(fromCoin.getCoin()+toCoin.getCoin(), digit);
    }
    private int getRateDigit(){
        return getRateDigit(mLeftCoin, mRightCoin);
    }
    private int getRateDigit(CoinBean fromCoin, CoinBean toCoin){
        return mCoinPairRateDigitMap.get(fromCoin.getCoin()+toCoin.getCoin());
    }

    /**
     * 检测刷新费率的时候out输入框小数点后的长度是不是比限制的长，如果是截取掉，并返回ture
     * @return
     */
    private boolean checkAndResetOutAboutWithDigest() {
        String s = etOut.getText().toString();
        int i = s.indexOf(".");
        if(i!=-1){
            String substring = s.substring(i+1);
            if(substring.length()>digest){
                int needCut = substring.length() - digest;
                String result = s.substring(0, s.length() - needCut);
                etOut.setText(result);
                etOut.setSelection(result.length());
                return true;
            }
        }
        return false;
    }

    /**
     * 检测刷新费率的时候out输入框小数点后的长度是不是比限制的长，如果是截取掉，并返回ture
     * @return
     */
    private boolean checkAndResetInAboutWithDigest() {
        String s = etIn.getText().toString();
        int i = s.indexOf(".");
        if(i!=-1){
            String substring = s.substring(i+1);
            if(substring.length()>toDigest){
                int needCut = substring.length() - toDigest;
                String result = s.substring(0, s.length() - needCut);
                etIn.setText(result);
                etIn.setSelection(result.length());
                return true;
            }
        }
        return false;
    }
}
