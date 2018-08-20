package com.idcg.idcw.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;

import com.cjwsc.idcm.Utils.LogUtil;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.BuildConfig;
import com.idcg.idcw.R;
import com.idcg.idcw.adapter.UltraPagerAdapter;
import com.idcg.idcw.iprovider.GetWalletBalanceProviderServices;
import com.idcg.idcw.iprovider.MoneyBagListProviderServices;
import com.idcg.idcw.iprovider.TransMsgProviderServices;
import com.idcg.idcw.model.bean.CurrencyDetailInfo;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.bean.TransMsgBean;
import com.idcg.idcw.model.bean.WalletAssetBean;
import com.idcg.idcw.model.bean.WalletListBean;
import com.idcg.idcw.model.logic.CurrencyDetailLogic;
import com.idcg.idcw.model.params.TransMsgReqParam;
import com.idcg.idcw.presenter.impl.CurrencyDetailPresenterImpl;
import com.idcg.idcw.presenter.interfaces.CurrencyDetailContract;
import com.idcg.idcw.utils.Utils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tmall.ultraviewpager.UltraViewPager;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.CommonAnimUtils;
import foxidcw.android.idcw.common.utils.CommonLayoutAnimationHelper;
import foxidcw.android.idcw.common.utils.StringUtils;

/**
 * Created by hpz on 2018/3/12.
 */

public class CurrTestActivity extends BaseWalletActivity<CurrencyDetailLogic, CurrencyDetailPresenterImpl> implements CurrencyDetailContract.View {
    @BindView(R.id.ultra_viewpager)
    UltraViewPager ultraViewpager;
    @BindView(R.id.tv_currency_balance)
    TextView tvCurrencyBalance;
    @BindView(R.id.tv_currency_market)
    TextView tvCurrencyMarket;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_set_Name)
    TextView tvCurrencyName;
    @BindView(R.id.ll_relative_btn)
    LinearLayout llRelativeBtn;
    @BindView(R.id.ll_send_btn)
    LinearLayout llSendBtn;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_currency_currency)
    TextView tvCurrencyCurrency;
    @BindView(R.id.tv_market_market)
    TextView tvMarketMarket;
    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_receive)
    TextView tvReceive;
    @BindView(R.id.tv_send)
    TextView tvSend;

    private UltraPagerAdapter adapter;
    private UltraViewPager.Orientation gravity_indicator;
    private List<WalletAssetBean> mCurrentCoinList = new ArrayList<>();

    private CommonAdapter commonAdapter;
    private double currentBalance;
    private double realityBalance;
    private String currency;
    private int posSelect = 0;
    private int index = 1;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int id;
    private String label;
    private double market;
    private String currencyName;
    private int tag;
    private int currencyLayoutType;
    private boolean isToken;
    private String tokenCategory;
    private boolean isTokenSend;
    private String tokenCategorySend;
    private double ethBalanceForToken;
    private boolean enable;
    private String logoUrl;

    @Autowired
    TransMsgProviderServices transMsgProviderServices;
    @Autowired
    MoneyBagListProviderServices mWalletListServices;
    @Autowired
    GetWalletBalanceProviderServices getWalletBalanceProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_curr_test;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);

        initSkipBundleData();//获取上个页面携带的数据

        initCollapsingToolbarLayoutConfig();//CollapsingToolbarLayout的相关设置

        showTabLayout();//显示tablelayout

        initRecyclerData();//初始化列表数据

        initRefreshLayout();//下拉刷新

        initUltraViewpager();//UltraViewpager的初始化

        judgmentButtonClick();//判断数字货币是否处于映射状态，设置对应按钮是否可以点击

    }

    private void initSkipBundleData() {
        Intent intent = getIntent();
        Bundle bundle1 = intent.getBundleExtra("detail");
        if (bundle1 != null) {
            id = bundle1.getInt("id");
            currency = bundle1.getString("currency");
            label = bundle1.getString("label");
            market = bundle1.getDouble("market");
            isToken = bundle1.getBoolean("isToken");
            tokenCategory = bundle1.getString("tokenCategory");
            currencyName = bundle1.getString("name");
            realityBalance = bundle1.getDouble("balance");
            enable = bundle1.getBoolean("enable");
            WalletListBean beanWallet = (WalletListBean) bundle1.getSerializable("walletList");
            mCurrentCoinList.addAll(beanWallet.getWalletList());
        }
    }

    private void initCollapsingToolbarLayoutConfig() {
        final CollapsingToolbarLayout collapsingtoolbarlayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout);
        //设置标题
        collapsingtoolbarlayout.setTitle(" ");
        String str = "Bitcoin";
        Spannable WordtoSpan = new SpannableString(str);
        WordtoSpan.setSpan(new AbsoluteSizeSpan(14), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        WordtoSpan.setSpan(new AbsoluteSizeSpan(18), 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        collapsingtoolbarlayout.setTitle(" ");
        //设置颜色变化
        collapsingtoolbarlayout.setExpandedTitleColor(Color.WHITE);
        collapsingtoolbarlayout.setCollapsedTitleTextColor(Color.WHITE);

    }

    private void showTabLayout() {
        try {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.all));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.received));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.sended));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    posSelect = tab.getPosition();
                    tag = 1;
                    if (tab.getPosition() == 1) {
                        pageIndex = 1;
                        RequestTransMsgOne("0", currency, pageIndex, false, true);
                    }
                    if (tab.getPosition() == 0) {
                        pageIndex = 1;
                        RequestTransMsgOne("2", currency, pageIndex, false, true);
                    }

                    if (tab.getPosition() == 2) {
                        pageIndex = 1;
                        RequestTransMsgOne("1", currency, pageIndex, false, true);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getStackTrace().toString());
        }
    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                tag = 2;
                if (posSelect == 0) {
                    pageIndex += 1;
                    RequestTransMsgOne("2", currency, pageIndex, true, false);
                } else if (posSelect == 1) {
                    pageIndex += 1;
                    RequestTransMsgOne("0", currency, pageIndex, true, false);
                } else if (posSelect == 2) {
                    pageIndex += 1;
                    RequestTransMsgOne("1", currency, pageIndex, true, false);
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                RequestNewBalance(currency, false);
                tag = 1;
                if (posSelect == 0) {
                    RequestOpt(currency, false);
                } else if (posSelect == 1) {
                    RequestOpt(currency, false);
                } else if (posSelect == 2) {
                    RequestOpt(currency, false);
                }
            }
        });
    }

    private void initRecyclerData() {
        try {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(linearLayoutManager);
            recyclerview.setAdapter(commonAdapter = new CommonAdapter<TransMsgBean>(R.layout.recyclerview_item) {
                @Override
                public void commonconvert(BaseViewHolder helper, TransMsgBean s) {
                    if (s == null) return;
                    TextView textView = (TextView) helper.getConvertView().findViewById(R.id.state_name);
                    TextView textView1 = (TextView) helper.getConvertView().findViewById(R.id.tv_send_or_receive);
                    ImageView imageView = (ImageView) helper.getConvertView().findViewById(R.id.icon);
                    if (s.getTrade_type() == 0) {
                        ((TextView) helper.getView(R.id.tv_from_or_send)).setText(getString(R.string.from));
                        ((TextView) helper.getView(R.id.state_name)).setText(getString(R.string.receive));
                        textView.setTextColor(getResources().getColor(R.color.main_color));
                        textView1.setTextColor(getResources().getColor(R.color.main_color));
                        imageView.setBackground(getResources().getDrawable(R.mipmap.tag_receive));
                        ((TextView) helper.getView(R.id.id_item_list_title)).setText(s.getSend_address());
                        String total = StringUtils.subZeroAndDot(Utils.toSubStringDegist(s.getAmount(), 8));
                        ((TextView) helper.getView(R.id.tv_send_or_receive)).setText("+" + total + " " + s.getCurrency().toUpperCase());
                    } else if (s.getTrade_type() == 1) {
                        ((TextView) helper.getView(R.id.tv_from_or_send)).setText(getString(R.string.to));
                        ((TextView) helper.getView(R.id.state_name)).setText(getString(R.string.send));
                        textView.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                        textView1.setTextColor(getResources().getColor(R.color.tian_tip_blue));
                        imageView.setBackground(getResources().getDrawable(R.mipmap.tag_send));
                        ((TextView) helper.getView(R.id.id_item_list_title)).setText(s.getReceiver_address());
                        if (s.isToken()) {
                            String amount = StringUtils.subZeroAndDot(Utils.toSubStringDegist(s.getAmount(), 8));
                            ((TextView) helper.getView(R.id.tv_send_or_receive)).setText("-" + amount + " " + s.getCurrency().toUpperCase());
                        } else {
                            String amount = StringUtils.subZeroAndDot(Utils.toSubStringDegist(Double.parseDouble(Utils.add(s.getAmount(), s.getTx_fee())), 8));
                            ((TextView) helper.getView(R.id.tv_send_or_receive)).setText("-" + amount + " " + s.getCurrency().toUpperCase());
                        }

                    }

                    if (s.isTxReceiptStatus()) {
                        ((TextView) helper.getView(R.id.tv_zhuan_catch)).setVisibility(View.GONE);
                        ((TextView) helper.getView(R.id.tv_confirm_count)).setVisibility(View.VISIBLE);
                        if (s.getConfirmations() < s.getTotal_confirmations()) {
                            ((TextView) helper.getView(R.id.tv_confirm_count)).setText(getString(R.string.doing) + "(" + s.getConfirmation_des() + getString(R.string.one) + ")");
                        } else {
                            String finishConfirm = StringUtils.formatChangeInt(s.getConfirmations());
                            if (s.getConfirmations() > 1000) {
                                ((TextView) helper.getView(R.id.tv_confirm_count)).setText(getString(R.string.complite) + "(" + "1,000+" + getString(R.string.one) + ")");
                            } else {
                                ((TextView) helper.getView(R.id.tv_confirm_count)).setText(getString(R.string.complite) + "(" + finishConfirm + getString(R.string.one) + ")");
                            }
                        }
                    } else {
                        ((TextView) helper.getView(R.id.tv_zhuan_catch)).setVisibility(View.VISIBLE);
                        ((TextView) helper.getView(R.id.tv_confirm_count)).setVisibility(View.GONE);
                        ((TextView) helper.getView(R.id.tv_confirm_count)).setText(getString(R.string.doing) + "(" + s.getConfirmation_des() + getString(R.string.one) + ")");
                    }
                    if (s.getTimeInterval() > 1 && s.getIntervalUnit().equals("day")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_days_ago));
                    } else if (s.getTimeInterval() == 1 && s.getIntervalUnit().equals("day")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_day_ago));
                    }
                    if (s.getTimeInterval() > 1 && s.getIntervalUnit().equals("week")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_weeks_ago));
                    } else if (s.getTimeInterval() == 1 && s.getIntervalUnit().equals("week")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_week_ago));
                    }
                    if (s.getTimeInterval() > 1 && s.getIntervalUnit().equals("year")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_years_ago));
                    } else if (s.getTimeInterval() == 1 && s.getIntervalUnit().equals("year")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_year_ago));
                    }
                    if (s.getTimeInterval() > 1 && s.getIntervalUnit().equals("month")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_mons_ago));
                    } else if (s.getTimeInterval() == 1 && s.getIntervalUnit().equals("month")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_mon_ago));
                    }
                    if (s.getTimeInterval() > 1 && s.getIntervalUnit().equals("hour")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_hours_ago));
                    } else if (s.getTimeInterval() == 1 && s.getIntervalUnit().equals("hour")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_hour_ago));
                    }
                    if (s.getTimeInterval() > 1 && s.getIntervalUnit().equals("min")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_mins_ago));
                    } else if (s.getTimeInterval() == 1 && s.getIntervalUnit().equals("min")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_min_ago));
                    }
                    if (s.getTimeInterval() > 1 && s.getIntervalUnit().equals("sec")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_secs_ago));
                    } else if (s.getTimeInterval() == 1 && s.getIntervalUnit().equals("sec")) {
                        ((TextView) helper.getView(R.id.tv_time_content)).setText(s.getTimeInterval() + getString(R.string.tv_sec_ago));
                    }

                    helper.getConvertView().setOnClickListener(v -> {
                        Bundle b = new Bundle();
                        b.putInt("tradeTypes", s.getTrade_type());
                        b.putString("currencys", s.getCurrency());
                        b.putString("ids", s.getTxhash());
                        b.putBoolean("istoken", isToken);
                        b.putString("tokenCategory", tokenCategory);
                        b.putString("des", s.getConfirmation_des());
                        ARouter.getInstance().build(ArouterConstants.TradeDetail).withBundle("trade", b).navigation();

                    });
                }
            });
            commonAdapter.setEmptyView(R.layout.recyclerview_empty_layout, recyclerview);
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private void initUltraViewpager() {
        ultraViewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultraViewpager.setMultiScreen(0.25f);
        ultraViewpager.setAdapter(adapter = new UltraPagerAdapter(true, new ArrayList<>()));
        ultraViewpager.setAutoMeasureHeight(true);
        ultraViewpager.setOffscreenPageLimit(0);
        gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;
    }

    private void judgmentButtonClick() {
        if (enable) {
            llRelativeBtn.setBackground(getResources().getDrawable(R.mipmap.button_trad));
            llSendBtn.setBackground(getResources().getDrawable(R.mipmap.button_trad));
            llRelativeBtn.setEnabled(true);
            llSendBtn.setEnabled(true);
            tvSend.setTextColor(getResources().getColor(R.color.white));
            tvReceive.setTextColor(getResources().getColor(R.color.white));
        } else {
            llRelativeBtn.setBackground(getResources().getDrawable(R.mipmap.button_gray_trad));
            llSendBtn.setBackground(getResources().getDrawable(R.mipmap.button_gray_trad));
            llRelativeBtn.setEnabled(false);
            llSendBtn.setEnabled(false);
            tvSend.setTextColor(getResources().getColor(R.color.c_80FFFFFF));
            tvReceive.setTextColor(getResources().getColor(R.color.c_80FFFFFF));
        }
    }

    @Override
    protected void onEvent() {
        RequestWalletList(true);//获取币种列表
        RequestNewBalance(currency, true);
        RequestOpt(currency, true);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    //请求币种列表
    public void RequestWalletList(boolean isShowProgress) {
        if (dialog != null && !BuildConfig.DEBUG) {
            dialog.setBackPressEnable(false);
        }
        mWalletListServices.getWalletListProvider()
                .doOnTerminate(() -> {
                    if (dialog != null && !BuildConfig.DEBUG) {
                        dialog.setBackPressEnable(true);
                    }
                })
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<List<WalletAssetBean>>(this, isShowProgress) {
                    @Override
                    public void onSuccess(List<WalletAssetBean> bean) {
                        if (bean != null && bean.size() > 0) {//有数据
                            mCurrentCoinList.clear();
                            mCurrentCoinList.addAll(bean);

                            for (int i = 0; i < mCurrentCoinList.size(); i++) {
                                if (currency.equals(mCurrentCoinList.get(i).getCurrency())) {
                                    tvCurrencyName.setText(mCurrentCoinList.get(i).getCurrencyName());
                                    tvCurrencyBalance.setText(mCurrentCoinList.get(i).getCurrency().toUpperCase());
                                    tvMarketMarket.setText(StringUtils.formatDouTwo(mCurrentCoinList.get(i).getRealityBalance() * mCurrentCoinList.get(i).getLocalCurrencyMarket()));
                                    tvCurrencyMarket.setText(mCurrentCoinList.get(i).getLocalCurrencyName());
                                    currencyLayoutType = mCurrentCoinList.get(i).getCurrencyLayoutType();
                                    logoUrl = mCurrentCoinList.get(i).getLogo_url();
                                }
                            }

                            int currentItem = -1;
                            for (int i = 0; i < mCurrentCoinList.size(); i++) {
                                WalletAssetBean itemBean1 = mCurrentCoinList.get(i);
                                if (itemBean1.getCurrency().equals(currency)) {
                                    itemBean1.setSelect(true);
                                    currentItem = i;
                                } else {
                                    itemBean1.setSelect(false);
                                }
                            }

                            ultraViewpager.setInfiniteLoop(mCurrentCoinList.size() >= 5);
                            adapter = new UltraPagerAdapter(true, mCurrentCoinList);
                            adapter.setOnItemListener(position-> {
                                tag = 1;
                                tvCurrencyBalance.setText(mCurrentCoinList.get(position).getCurrency().toUpperCase());
                                tvCurrencyName.setText(mCurrentCoinList.get(position).getCurrencyName());
                                tvMarketMarket.setText(StringUtils.formatDouTwo(mCurrentCoinList.get(position).getRealityBalance() * mCurrentCoinList.get(position).getLocalCurrencyMarket()));
                                tvCurrencyMarket.setText(mCurrentCoinList.get(position).getLocalCurrencyName());
                                currencyLayoutType = mCurrentCoinList.get(position).getCurrencyLayoutType();
                                RequestOpt(mCurrentCoinList.get(position).getCurrency(), true);
                                //getSelectWalletList(mCurrentCoinList.get(position).getCurrency(), true);
                                currency = mCurrentCoinList.get(position).getCurrency();
                                logoUrl = mCurrentCoinList.get(position).getLogo_url();
                                if (mCurrentCoinList.get(position).isIs_enable_ransceiver()) {
                                    llRelativeBtn.setBackground(getResources().getDrawable(R.mipmap.button_trad));
                                    llSendBtn.setBackground(getResources().getDrawable(R.mipmap.button_trad));
                                    llRelativeBtn.setEnabled(true);
                                    llSendBtn.setEnabled(true);
                                    tvSend.setTextColor(getResources().getColor(R.color.white));
                                    tvReceive.setTextColor(getResources().getColor(R.color.white));
                                } else {
                                    llRelativeBtn.setBackground(getResources().getDrawable(R.mipmap.button_gray_trad));
                                    llSendBtn.setBackground(getResources().getDrawable(R.mipmap.button_gray_trad));
                                    llRelativeBtn.setEnabled(false);
                                    llSendBtn.setEnabled(false);
                                    tvSend.setTextColor(getResources().getColor(R.color.c_80FFFFFF));
                                    tvReceive.setTextColor(getResources().getColor(R.color.c_80FFFFFF));
                                }
                                RequestNewBalance(mCurrentCoinList.get(position).getCurrency(), true);
                                LogUtil.e("点击哪个币种的名称==", mCurrentCoinList.get(position).getCurrency() + "," + position);
                                for (WalletAssetBean itembean : mCurrentCoinList) {
                                    if (itembean.isSelect()) {
                                        itembean.setSelect(false);//重置前面选中状态
                                        LogUtil.e("isSelect==", "重置状态" + "," + position);
                                    }

                                }
                                mCurrentCoinList.get(position).setSelect(true);
                                adapter.notifyDataSetChanged();
                                ultraViewpager.getViewPager().getAdapter().notifyDataSetChanged();
                                if (mCurrentCoinList.size() > 4) {
                                    ultraViewpager.setCurrentItem(position);
                                }
                            });
                            ultraViewpager.setAdapter(adapter);
                            if (mCurrentCoinList.size() == 4) {
                                ultraViewpager.setCurrentItem(2);
                            } else if (mCurrentCoinList.size() == 3) {
                                ultraViewpager.setCurrentItem(1);
                            } else {
                                ultraViewpager.setCurrentItem(currentItem);
                            }
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        LogUtil.e("onError ---> " + ex.ErrorCode);
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }
                });
        ;
    }

    private void RequestOpt(String currency, boolean isShowProgress) {
        mPresenter.requestOpt(currency, isShowProgress);
    }

    private void RequestTransMsgOne(String type, String coinName, int pageIndex, boolean isloadmore, boolean isShowProgress) {
        if (tag == 1) {
            commonAdapter.setNewData(null);
        }

        transMsgProviderServices.
                requestTransMsgProvider(new TransMsgReqParam(currency, type, pageIndex + "",
                        pageSize + "", "", ""))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<List<TransMsgBean>>(this, isShowProgress) {

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }

                    @Override
                    public void onSuccess(List<TransMsgBean> transMsgBean) {
                        if (transMsgBean != null && transMsgBean.size() > 0) {//有数据
                            if (mRefreshLayout != null) mRefreshLayout.resetNoMoreData();
                            if (isloadmore) {//加载更多
                                if (mRefreshLayout != null) mRefreshLayout.finishLoadmore();
                                commonAdapter.addData(transMsgBean);
                            } else {//重新加载数据
                                if (mRefreshLayout != null) mRefreshLayout.finishRefresh();
                                commonAdapter.setNewData(transMsgBean);
                                CommonAnimUtils.playCommonAllViewAnimation(recyclerview, CommonLayoutAnimationHelper.getAnimationSetFromRight(), false);
                            }
                        } else {//请求成功但是没有数据
                            onUpdateSmartRefresh(mRefreshLayout);
                            if (mRefreshLayout != null)
                                mRefreshLayout.finishLoadmoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                        }
                    }
                });

    }

    private void onUpdateSmartRefresh(SmartRefreshLayout smartRefreshLayout) {
        if (smartRefreshLayout == null) return;
        if (smartRefreshLayout.isRefreshing()) {
            smartRefreshLayout.finishRefresh();
        } else if (smartRefreshLayout.isLoading()) {
            smartRefreshLayout.finishLoadmore();
        }
    }

    private void RequestNewBalance(String currency, boolean isShowProgress) {
        getWalletBalanceProviderServices.requestNewBalanceProvider(currency)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<NewBalanceBean>(this, isShowProgress) {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }

                    @Override
                    public void onSuccess(NewBalanceBean newBalanceBean) {
                        currentBalance = newBalanceBean.getCurrentBalance();
                        realityBalance = newBalanceBean.getRealityBalance();
                        String amount = Utils.toSubStringDegist(newBalanceBean.getRealityBalance(), 4);
                        tvCurrencyCurrency.setText(amount);
                        if (newBalanceBean.getTokenInfo() != null) {
                            isTokenSend = newBalanceBean.getTokenInfo().isIsToken();
                            tokenCategorySend = newBalanceBean.getTokenInfo().getCoinUnit();
                            ethBalanceForToken = newBalanceBean.getTokenInfo().getEthBalanceForToken();
                        } else {
                            isTokenSend = false;
                        }
                    }
                });
    }

    @OnClick({R.id.ll_relative_btn, R.id.ll_send_btn, R.id.mr_back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                EventBus.getDefault().post(new PosInfo(998));
                this.finish();
                break;
            case R.id.ll_relative_btn:
                Intent intent1 = new Intent(CurrTestActivity.this, ReceiveCodeActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("currency", currency);
                bundle1.putString("logoUrl", logoUrl);
                intent1.putExtra("receive", bundle1);
                startActivity(intent1);
                break;
            case R.id.ll_send_btn:
                Intent intent = new Intent(CurrTestActivity.this, SendCurrencyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("currency", currency);
                bundle.putInt("currencyLayoutType", currencyLayoutType);
                bundle.putString("tokenCategorySend", tokenCategorySend);
                bundle.putBoolean("isTokenSend", isTokenSend);
                bundle.putDouble("currentBalance", currentBalance);
                bundle.putDouble("ethBalanceForToken", ethBalanceForToken);
                bundle.putDouble("realityBalance", realityBalance);
                intent.putExtra("Send", bundle);
                startActivity(intent);
                break;
        }
    }

    @Subscriber
    public void onTradeRefreshInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 2020) {
                RequestNewBalance(currency, true);
                RequestOpt(currency, true);
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {

    }

    @Override
    public void updateRequestOpt(Boolean isOk) {
        int type = posSelect == 1 ? 0 : posSelect == 0 ? 2 : posSelect == 2 ? 1 : -1;
//        if (posSelect == 1) {
//            pageIndex = 1;
//            RequestTransMsgOne("0", currency, pageIndex, false);
//        } else if (posSelect == 0) {
//            pageIndex = 1;
//            RequestTransMsgOne("2", currency, pageIndex, false);
//        } else if (posSelect == 2) {
//            pageIndex = 1;
//            RequestTransMsgOne("1", currency, pageIndex, false);
//        }
        if (type != -1) {
            pageIndex = 1;
            if (tag == 1) {
                RequestTransMsgOne(String.valueOf(type), currency, pageIndex, false, false);
            } else {
                RequestTransMsgOne(String.valueOf(type), currency, pageIndex, false, true);
            }
        }
    }

    @Override
    public void updateRequestAccountAddress(String param) {

    }

    @Override
    public void updateRequestTransMsg(List<TransMsgBean> params) {

    }

    @Override
    protected void checkAppVersion() {
    }

    /**
     * 点击手机返回键finish app
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new PosInfo(998));
    }
}
