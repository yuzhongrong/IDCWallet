package com.idcg.idcw.fragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

import com.cjwsc.idcm.Utils.LogUtil;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.widget.XyMarkView;
import com.idcg.idcw.R;
import com.idcg.idcw.activitys.CurrTestActivity;
import com.idcg.idcw.activitys.WebViewActivity;

import foxidcw.android.idcw.common.base.BaseWalletFragment;

import com.idcg.idcw.configs.ClientConfig;

import foxidcw.android.idcw.common.constant.ArouterConstants;

import com.idcg.idcw.iprovider.MoneyBagListProviderServices;
import com.idcg.idcw.model.bean.CheckStatusBean;
import com.idcg.idcw.model.bean.HistoryAmountBean;
import com.idcg.idcw.model.bean.LocalCurrencyEvent;
import com.idcg.idcw.model.bean.NoticeTopBean;

import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.idcg.idcw.model.bean.WalletAssetBean;
import com.idcg.idcw.model.bean.WalletListBean;
import com.idcg.idcw.model.logic.AssetsFragmentLogic;
import com.idcg.idcw.presenter.impl.AssetsFragmentPresenterImpl;
import com.idcg.idcw.presenter.interfaces.AssetsFragmentContract;
import com.idcg.idcw.utils.SmartRefreshUtil;

import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.CommonLayoutAnimationHelper;
import foxidcw.android.idcw.common.utils.StringUtils;

import com.idcg.idcw.utils.TimeUtils;
import com.idcg.idcw.widget.MyDialog;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.DateTimeUtil;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.constant.EventBusTags;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import foxidcw.android.idcw.foxcommon.Constants.Constants;
import foxidcw.android.idcw.foxcommon.provider.bean.ScanLoginBean;
import foxidcw.android.idcw.foxcommon.provider.params.ScanLoginReqParam;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.github.mikephil.charting.data.LineDataSet.Mode.CUBIC_BEZIER;

/**
 * Created by admin-2 on 2018/3/14.
 */
public class FoxAssetsFragment extends BaseWalletFragment<AssetsFragmentLogic, AssetsFragmentPresenterImpl> implements View.OnClickListener, AssetsFragmentContract.View {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.rv_asset)
    RecyclerView rvAsset;
    @BindView(R.id.toolbar_assets)
    TextView toolbarAssets;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.ll_visible)
    LinearLayout llVisible;
    @BindView(R.id.boxmail)
    ImageView boxmail;
    @BindView(R.id.img_expend)
    ImageView imgExpend;
    @BindView(R.id.icon_scon)
    ImageView icon_scon;

    private View view1;
    private View view2;
    private View inflate;
    private BGABanner bgaBanner;
    private CommonAdapter commonAdapter;
    private HistoryAmountBean tempamountbean = null;
    private MyDialog dialogBack;
    private Timer timer;
    private TextView backUpBtn;
    private TextView tv_next_say;
    private int scroY;
    private String currencySymbol;
    private String strValue;
    private String result;
    private String currencyLocal;
    private String device_id;

    @Autowired
    MoneyBagListProviderServices mWalletListServices;

    public static FoxAssetsFragment newInstance() {
        Bundle args = new Bundle();
        FoxAssetsFragment fragment = new FoxAssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_assets_layout;
    }

    @Override
    protected void onEvent() {
        processLogic();//每5分钟拉下广告数据
        requestCount(true);
        mPresenter.requestCheckStatus(device_id, true);
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    protected void lazyFetchData() {

    }

    private void requestCount(boolean isShowProgress) {
        mPresenter.requestHistoryAmountNew(isShowProgress);
        addSubscribe(mWalletListServices.getWalletListProvider()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxProgressSubscriber<List<WalletAssetBean>>(this, isShowProgress) {
                    @Override
                    public void onSuccess(List<WalletAssetBean> bean) {
                        if (bean != null && bean.size() > 0) {//有数据
                            commonAdapter.setNewData(bean);

                        } else {
                            commonAdapter.setNewData(null);
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if ("120".equals(ex.ErrorCode)) {
                            commonAdapter.setNewData(null);
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }
                }));
    }

    protected void processLogic() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtil.d("----开始执行请求通知--->", "");
                mPresenter.requestTopNotice(AppLanguageUtils.getLanguageLocalCode(getActivity()));
                LogUtil.e("----开始执行请求通知--->", AppLanguageUtils.getLanguageLocalCode(getActivity()));
            }
        }, 1000, 5 * 60 * 1000);//16 * 60 * 1000
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        LoginStatus bean = LoginUtils.getLoginBean(getActivity());
        if (bean != null) {
            device_id = bean.getDevice_id();
            textView.setText(bean.getUser_name());//改动
        }
        initRecycler();

        initShowDialog();

        smartrefreshlayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
        smartrefreshlayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartrefreshlayout.setEnableLoadmore(false);
        smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                requestCount(false);
                if (mPresenter != null) mPresenter.requestCheckStatus(device_id, true);//检测助记词
            }
        });

        initSmartRefreshListener();//监听SmartRefreshLayout的滑动事件

        initRecyclerViewScrollListener();//监听列表的滑动事件

        initBGABannerData();//设置资产和行情轮播图的数据

    }

    private void initRecycler() {
        view2 = View.inflate(getContext(), R.layout.fragment_asset_header, null);
        view1 = View.inflate(getContext(), R.layout.item_empty_view, null);
        bgaBanner = view2.findViewById(R.id.bga_puretaste_banner);
        commonAdapter = new CommonAdapter<WalletAssetBean>(R.layout.item_asset) {
            @Override
            public void commonconvert(BaseViewHolder helper, WalletAssetBean item) {
                GlideUtil.loadImageView(mContext, item.getLogo_url(), (ImageView) helper.getView(R.id.img_currency));
                ((TextView) helper.getView(R.id.id_item_name)).setText(item.getCurrency().toUpperCase());//item.getCurrencyName()
                if (item.getCurrency().equals("vhkd")) {
                    String vhkd = Utils.toSubStringDegist(item.getRealityBalance(), 8);
                    ((TextView) helper.getView(R.id.tv_market)).setText(vhkd);// + " " + item.getCurrency().toUpperCase() + ""
                } else {
                    String other = Utils.toSubStringDegist(item.getRealityBalance(), 8);
                    ((TextView) helper.getView(R.id.tv_market)).setText(other);// + " " + item.getCurrency().toUpperCase() + ""
                }

                if (item.getCurrency().equals("vhkd")) {
                    String vhkd = Utils.toSubStringDegist(item.getLocalCurrencyMarket(), 2);
                    ((TextView) helper.getView(R.id.tv_balance)).setText("1" + " " + item.getCurrency().toUpperCase() + " " + "=" + " " + vhkd + " " + item.getLocalCurrencyName());
                } else {
                    String all = Utils.toSubStringDegist(item.getLocalCurrencyMarket(), 2);
                    ((TextView) helper.getView(R.id.tv_balance)).setText("1" + " " + item.getCurrency().toUpperCase() + " " + "=" + " " + all + " " + item.getLocalCurrencyName());
                }

                if (item.getLocalCurrencyName().equals("CNY") || item.getLocalCurrencyName().equals("JPY")) {
                    String all = Utils.toSubStringDegist(item.getRealityBalance() * item.getLocalCurrencyMarket(), 2);
                    ((TextView) helper.getView(R.id.tv_market_usd)).setText("≈" + " " + "¥ " + all + "");
                } else {
                    String all = Utils.toSubStringDegist(item.getRealityBalance() * item.getLocalCurrencyMarket(), 2);
                    ((TextView) helper.getView(R.id.tv_market_usd)).setText("≈" + " " + item.getCurrencySymbol() + " " + all + "");
                }

                helper.getConvertView().setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), CurrTestActivity.class);
                    Bundle bundle = new Bundle();
                    //bundle.putSerializable("walletList", new WalletListBean(walletList));
                    bundle.putSerializable("walletList", new WalletListBean(commonAdapter.getData()));
                    bundle.putString("label", item.getCurrencyName());
                    bundle.putString("currency", item.getCurrency());
                    bundle.putDouble("balance", item.getRealityBalance());
                    bundle.putDouble("market", item.getLocalCurrencyMarket());
                    bundle.putString("name", item.getLocalCurrencyName());
                    bundle.putInt("id", item.getId());
                    bundle.putBoolean("enable", item.isIs_enable_ransceiver());
                    intent.putExtra("detail", bundle);
                    startActivity(intent);
                });
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAsset.setLayoutManager(linearLayoutManager);
        rvAsset.setAdapter(commonAdapter);
        setHeader(rvAsset);
        setFooter(rvAsset);
        ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setOnClickListener(v -> {
            navigation(ArouterConstants.ADD_NEW_CURRENCY);
        });

        findViewById(commonAdapter, R.id.notice_root).setOnClickListener(this);
        findViewById(commonAdapter, R.id.icon_close_notice).setOnClickListener(this);
    }

    private void initShowDialog() {
        dialogBack = new MyDialog(getActivity(), R.style.push_animation_dialog_style);
        //填充对话框的布局
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.create_wallet_dialog, null);
        //初始化控件
        dialogBack.setCancelable(false);
        backUpBtn = (TextView) inflate.findViewById(R.id.btn_back_up);
        tv_next_say = (TextView) inflate.findViewById(R.id.tv_next_say);
        tv_next_say.setOnClickListener(this);
        backUpBtn.setOnClickListener(this);
        //waitFinish.setOnClickListener(this);
        //将布局设置给Dialog
        dialogBack.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialogBack.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    private void initSmartRefreshListener() {//监听smart
        smartrefreshlayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                LogUtil.e("smartrefreshlayout11==", offset + "");
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {
                LogUtil.e("onHeaderReleased==", "======11");
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                LogUtil.e("onHeaderReleased==", "======22");
                if (llVisible == null) return;
                if (scroY == 0) {
                    llVisible.setVisibility(View.GONE);
                    ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {
                if (llVisible == null) return;
                LogUtil.e("smartrefreshlayout33==", offset + "" + "," + percent + "" + "," + footerHeight + "," + extendHeight);
                LogUtil.e("extendHeight==", ((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getHeight() + "," + ((LinearLayout) findViewById(commonAdapter, R.id.ll_assets)).getHeight() + "");
                if (((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getVisibility() == View.VISIBLE) {
                    if (scroY == 0) {
                        if (offset > ((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getHeight()) {
                            if (llVisible.getVisibility() == View.VISIBLE) return;
                            llVisible.setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.GONE);
                            ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.INVISIBLE);
                        } else {
                            if (llVisible.getVisibility() == View.VISIBLE) {
                                llVisible.setVisibility(View.GONE);
                                ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.VISIBLE);
                            }

                        }
                    }
                } else if (((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getVisibility() == View.GONE) {
                    if (scroY == 0) {
                        if (offset > ((LinearLayout) findViewById(commonAdapter, R.id.ll_assets)).getHeight()) {
                            if (llVisible.getVisibility() == View.VISIBLE) return;
                            llVisible.setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.GONE);
                            ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.INVISIBLE);
                        } else {
                            if (llVisible.getVisibility() == View.VISIBLE) {
                                llVisible.setVisibility(View.GONE);
                                ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {
                LogUtil.e("smartrefreshlayout44==", percent + "");
            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

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

    private void initRecyclerViewScrollListener() {//监听列表
        rvAsset.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (llVisible == null) return;
                int offSetY = getScollYDistance();
                LogUtil.e("offSetY", offSetY + "");

                scroY = offSetY;
                if (((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getVisibility() == View.VISIBLE) {
                    if (offSetY > ((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getHeight()) {
                        if (llVisible.getVisibility() == View.VISIBLE) return;
                        llVisible.setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.GONE);
                        ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.INVISIBLE);

                    } else {
                        if (llVisible.getVisibility() == View.VISIBLE) {
                            llVisible.setVisibility(View.GONE);
                            ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.VISIBLE);
                        }
                    }
                } else if (((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getVisibility() == View.GONE) {
                    if (offSetY > ((LinearLayout) findViewById(commonAdapter, R.id.ll_assets)).getHeight()) {
                        if (llVisible.getVisibility() == View.VISIBLE) return;
                        llVisible.setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.GONE);
                        ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.INVISIBLE);
                    } else {
                        if (llVisible.getVisibility() == View.VISIBLE) {
                            llVisible.setVisibility(View.GONE);
                            ((ImageView) findViewById(commonAdapter, R.id.imageView2)).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setVisibility(View.VISIBLE);
                        }
                    }
                }


            }
        });
    }

    private void initBGABannerData() {//资产轮播图
        //((BGABanner) findViewById(commonAdapter, R.id.bga_puretaste_banner)).setAutoPlayInterval(10000000);设置轮询时间
        ((BGABanner) findViewById(commonAdapter, R.id.bga_puretaste_banner)).setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
                //判断这个实例是走势图还是曲线
                if (model instanceof HistoryAmountBean.HistoryAssetDataBean) {
                    List<HistoryAmountBean.HistoryAssetDataBean.DateListBean> countdata = ((HistoryAmountBean.HistoryAssetDataBean) model).getDateList();
                    List<Entry> entries = new ArrayList<Entry>();
                    LineChart mChart = (LineChart) itemView.findViewById(R.id.chart);//线
                    //去掉拖拽
                    mChart.setDragEnabled(true);
                    mChart.setScaleEnabled(false);
                    mChart.setTouchEnabled(true);
                    mChart.getDescription().setEnabled(false);//去掉描述
                    mChart.getXAxis().setGridColor(Color.TRANSPARENT);//去掉X网格
                    mChart.getAxisLeft().setGridColor(Color.TRANSPARENT);//去掉y网格
                    mChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
                    mChart.getAxisLeft().setEnabled(false); // 隐藏左边 的坐标轴
                    mChart.getXAxis().setEnabled(true);
                    mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    mChart.setHighlightPerDragEnabled(true);
                    mChart.getAxisLeft().setSpaceTop(15f);
                    //线和X轴的顶部间距
                    mChart.getAxisLeft().setSpaceBottom(15f);//线和X轴的底部间距
                    upDataAmountDatas(tempamountbean);
                    //mChart.getAxisLeft().setLabelCount(5, true);
                    //去掉正方形
                    Legend l = mChart.getLegend();
                    l.setForm(Legend.LegendForm.EMPTY);
                    //设置markview
                    //markview设置
                    XyMarkView mv = new XyMarkView(getActivity(), R.layout.customer_marker_view1);
                    mv.setChartView(mChart); //For bounds control
                    mv.setCallBack(new XyMarkView.CallBack() {//文字回调
                        @Override
                        public void onCallBack(float x, String value, Object data) {//不同人对文字的要求格式不一样 所以放在外面加工数据格式
                            //mv.getTvContent().setText(tempamountbean.getCurrencySymbol()+ " "+value);
                            if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                                mv.getTvContent().setText("¥" + " " + value);
                            } else {
                                mv.getTvContent().setText(tempamountbean.getCurrencySymbol() + " " + value);
                            }

                            String date = ((HistoryAmountBean.HistoryAssetDataBean.DateListBean) data).getDate();//获取日期
                            String finalData = TimeUtils.getTargetTime(date);
                            ((TextView) mv.getViewById(R.id.date)).setText(DateTimeUtil.getCustomerDate(finalData));
                        }
                    });
                    mChart.setMarker(mv); // Set the marker to the chart
                    mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {
                            // 清空之前高亮的entry icon
                            List<ILineDataSet> datasets = mChart.getData().getDataSets();
                            for (ILineDataSet iset : datasets) {//只有一个dataset
                                LineDataSet set = (LineDataSet) iset;
                                for (int i = 0; i < set.getEntryCount(); i++) {
                                    Entry entry = entries.get(i);
                                    if (entry.getIcon() != null) entry.setIcon(null);
                                }
                            }
                            e.setIcon(getResources().getDrawable(R.mipmap.endpoint));
                            mChart.invalidate();
                            LogUtil.d("--onValueSelected---->", e + "  " + h);
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });

                    //填充点坐标
                    for (HistoryAmountBean.HistoryAssetDataBean.DateListBean data : countdata) {
                        // turn your data into Entry objects
                        if (countdata.indexOf(data) == countdata.size() - 1) {//最后一个画点图片
                            entries.add(new Entry(countdata.indexOf(data), (float) data.getMarketMoney(), getResources().getDrawable(R.mipmap.endpoint), data));
                        } else {//正常情况不带图片
                            entries.add(new Entry(countdata.indexOf(data), (float) data.getMarketMoney(), data));
                        }
                    }
                    setData(mChart, entries, true);

                    mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            //构建X轴数据
                            String date = ((HistoryAmountBean.HistoryAssetDataBean.DateListBean) entries.get((int) value).getData()).getDate();//获取日期
                            LogUtil.d("----------->", "getFormattedValue: " + value + "   " + axis + "   " + date);
                            String finalData = TimeUtils.getTargetTime(date);
                            finalData = DateTimeUtil.getCustomerDate(finalData);
                            return finalData;
                        }
                    });

                    mChart.animateX(500);//动画时间设置
                    if (((HistoryAmountBean.HistoryAssetDataBean) model).getCurrency().equals("total")) {
                        ((TextView) itemView.findViewById(R.id.tv_seven_data)).setText(getString(R.string.seven_day_asset_market));
                    } else {
                        ((TextView) itemView.findViewById(R.id.tv_seven_data)).setText(getString(R.string.seven_day) + ((HistoryAmountBean.HistoryAssetDataBean) model).getCurrency().toUpperCase() + getString(R.string.asset_what));
                    }

                } else if (model instanceof HistoryAmountBean.HistoryMarketData) {//zoushitu
                    HistoryAmountBean.HistoryMarketData countdata = (HistoryAmountBean.HistoryMarketData) model;
                    //LineChartMarketView lineChartView = itemView.findViewById(R.id.line_chart_view_market);//线
                    // -------------------------------------------------------------------------
                    upDataAmountDatas(tempamountbean);
                    // -------------------------------------------------------------------------
                    FrameLayout root = (FrameLayout) itemView.findViewById(R.id.cardView);
                    TextView tv = (TextView) root.findViewById(R.id.tv_market_data);
                    tv.setText(countdata.getCurrency().toUpperCase() + " " + getString(R.string.seven_four));
                    LineChart mChart = itemView.findViewById(R.id.chart);//线

                    //去掉拖拽
                    mChart.setDragEnabled(true);
                    mChart.setScaleEnabled(false);
                    mChart.setTouchEnabled(false);
                    mChart.getDescription().setEnabled(false);//去掉描述mChart.getXAxis().setGridColor(Color.TRANSPARENT);//去掉X网格
                    mChart.getAxisLeft().setGridColor(Color.TRANSPARENT);//去掉y网格
                    mChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
                    mChart.getXAxis().setEnabled(false);
                    mChart.getXAxis().setSpaceMin(1f);
                    mChart.getAxisLeft().setLabelCount(5, true);
                    //去掉正方形
                    Legend l = mChart.getLegend();
                    l.setForm(Legend.LegendForm.EMPTY);

                    mChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            //    String result = com.idcg.idcw.utils.Utils.toSubStringDegistForChart(Double.parseDouble(String.valueOf(value)), 5, false);
                            //     LogUtil.d("-----result---->", "getFormattedValue: " + result);
                            String result = "";
                            int type = ruleYFormatter(axis);
                            switch (type) {
                                case 0://小波
                                    //   boolean iszero=ruleZero(axis);
                                    if (axis.mEntries[0] > 1) {
                                        result = com.idcg.idcw.utils.Utils.toSubStringDegistForChart(Double.parseDouble(String.valueOf(value)), 3, true);
                                    } else {
                                        result = com.idcg.idcw.utils.Utils.toSubStringDegistForChart(Double.parseDouble(String.valueOf(value)), 5, false);
                                        result = StringUtils.subZeroAndDot(result);
                                    }
//
                                    break;
                                case 1://大波
                                    result = com.idcg.idcw.utils.Utils.toSubStringDegistForChart(Double.parseDouble(String.valueOf(value)), 0, false);
                                    break;
                                default:
                                    result = com.idcg.idcw.utils.Utils.toSubStringDegistForChart(Double.parseDouble(String.valueOf(value)), 5, false);
                                    break;

                            }

                            //默认只显示截取小数点后面5位 不补0
                            return result;
                        }

                    });

                    mChart.animateX(500);
                    List<HistoryAmountBean.HistoryMarketData.DateListBean> datas = countdata.getDateList();
                    List<Entry> entries = new ArrayList<Entry>();

                    //填充点坐标
                    for (HistoryAmountBean.HistoryMarketData.DateListBean data : datas) {
                        // turn your data into Entry objects
                        if (datas.indexOf(data) == datas.size() - 1) {//最后一个画点图片
                            entries.add(new Entry(datas.indexOf(data), data.getPrice(), getResources().getDrawable(R.mipmap.endpoint)));
                        } else {//正常情况不带图片
                            entries.add(new Entry(datas.indexOf(data), data.getPrice()));
                        }
                    }

                    //markview设置
                    XyMarkView mv = new XyMarkView(getActivity(), R.layout.custom_marker_view);
                    mv.setChartView(mChart); // For bounds control
                    mv.setCallBack(new XyMarkView.CallBack() {//文字回调
                        @Override
                        public void onCallBack(float x, String value, Object data) {//不同人对文字的要求格式不一样 所以放在外面加工数据格式
                            //   mv.getTvContent().setText(currencySymbol + " " + com.shuweikeji.idcwallet2.utils.Utils.toSubStringDegist(Double.parseDouble(value), 2));
                            //   String markvalue=  com.github.mikephil.charting.utils.Utils.formatNumber(value, 0, true,',');
                            if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                                mv.getTvContent().setText("¥" + " " + value);
                            } else {
                                mv.getTvContent().setText(currencySymbol + " " + value);
                            }
                        }
                    });
                    mChart.setMarker(mv); // Set the marker to the chart
                    setData(mChart, entries, false);
                }


            }
        });
        ((BGABanner) findViewById(commonAdapter, R.id.bga_puretaste_banner)).setDelegate((banner, itemView, model, position) ->  {
            //设置点击
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void upDataAmountDatas(HistoryAmountBean tempamountbean) {
        if (tempamountbean != null) {
            strValue = StringUtils.formatDouTwo(tempamountbean.getTotalAssetMoney());
            currencySymbol = tempamountbean.getCurrencySymbol();
            currencyLocal = tempamountbean.getLocalCurrency();

            if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                result = getString(R.string.total_assets)
                        + "(" + tempamountbean.getLocalCurrency() + ")"
                        + " " + "¥" + " " + strValue;
            } else {
                result = getString(R.string.total_assets)
                        + "(" + tempamountbean.getLocalCurrency() + ")"
                        + " " + tempamountbean.getCurrencySymbol() + " " + strValue;
            }

            String locale = Locale.getDefault().getLanguage();
            if (getResources().getConfiguration().locale.getLanguage().equals("zh") || locale.equals("zh")) {
                if (tempamountbean.getLocalCurrency().equals("HKD")) {
                    if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(" "), result.indexOf("¥") + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf("¥") + 3, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    } else {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(" "), result.indexOf(currencySymbol) + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf(currencySymbol) + 3, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    }

                } else {
                    if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(" "), result.indexOf("¥") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf("¥") + 1, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    } else {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(" "), result.indexOf(currencySymbol) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf(currencySymbol) + 1, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    }

                }

            } else if (getResources().getConfiguration().locale.getLanguage().equals("en") || locale.equals("en")) {
                if (tempamountbean.getLocalCurrency().equals("HKD")) {
                    if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(")") + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(")") + 2, result.indexOf("¥") + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf("¥") + 3, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    } else {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(")") + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(")") + 2, result.indexOf(currencySymbol) + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf(currencySymbol) + 3, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    }

                } else {
                    if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(")") + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(")") + 2, result.indexOf("¥") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf("¥") + 1, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    } else {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(")") + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(")") + 2, result.indexOf(currencySymbol) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf(currencySymbol) + 1, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    }

                }


            } else if (getResources().getConfiguration().locale.getLanguage().equals("fi")) {
                if (tempamountbean.getLocalCurrency().equals("HKD")) {
                    if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(" "), result.indexOf("¥") + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf("¥") + 3, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    } else {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(" "), result.indexOf(currencySymbol) + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf(currencySymbol) + 3, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    }

                } else {
                    if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(" "), result.indexOf("¥") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf("¥") + 1, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    } else {
                        SpannableString rateText = new SpannableString(result);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_one), 0, result.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), result.indexOf(" "), result.indexOf(currencySymbol) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        rateText.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_three), result.indexOf(currencySymbol) + 1, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        toolbarAssets.setText(rateText);
                    }
                }

            } else {
                toolbarAssets.setText(result);
            }

            LogUtil.d("-------->", "111");

            ((TextView) findViewById(commonAdapter, R.id.text_currency)).setText(getString(R.string.total_assets) + " " + "(" + tempamountbean.getLocalCurrency() + ")");

            String s = tempamountbean.getCurrencySymbol() + " " + strValue;

            if (currencyLocal.equals("CNY") || currencyLocal.equals("JPY")) {
                SpannableString rateText1 = new SpannableString("¥" + " " + strValue);
                rateText1.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), 0, s.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                rateText1.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_five), s.indexOf(" "), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //toolbarAssets.setText(rateText);
                ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setText(rateText1);
            } else {
                SpannableString rateText1 = new SpannableString(tempamountbean.getCurrencySymbol() + " " + strValue);
                rateText1.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_two), 0, s.indexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                rateText1.setSpan(new TextAppearanceSpan(getActivity(), R.style.dayup_shouyilv_five), s.indexOf(" "), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //toolbarAssets.setText(rateText);
                ((TextView) findViewById(commonAdapter, R.id.tv_assets_amount)).setText(rateText1);
            }

            //assetChangeType (integer, optional): 资产变化枚举 1=up
            //2=down 3=nochange = ['1', '2', '3']integerEnum:1, 2, 3,
            if (tempamountbean.getAssetChangeType() == 1) {
                ((ImageView) findViewById(commonAdapter, R.id.img_up_or_down)).setImageResource(R.mipmap.icon_up_green);
                ((TextView) findViewById(commonAdapter, R.id.tv_add_price)).setText(" " + tempamountbean.getDValue());
                ((TextView) findViewById(commonAdapter, R.id.tv_rate_num)).setText(" " + "(" + tempamountbean.getPersent() + ")" + " ");
                ((TextView) findViewById(commonAdapter, R.id.day_change)).setText(" " + getString(R.string.hours_change));
            } else if (tempamountbean.getAssetChangeType() == 2) {
                ((ImageView) findViewById(commonAdapter, R.id.img_up_or_down)).setImageResource(R.mipmap.icon_down_red);
                ((TextView) findViewById(commonAdapter, R.id.tv_add_price)).setText(" " + tempamountbean.getDValue());
                ((TextView) findViewById(commonAdapter, R.id.tv_rate_num)).setText(" " + "(" + tempamountbean.getPersent() + ")" + " ");
                ((TextView) findViewById(commonAdapter, R.id.day_change)).setText(" " + getString(R.string.hours_change));
            } else if (tempamountbean.getAssetChangeType() == 3) {
                if (tempamountbean.getTotalAssetMoney() == 0) {
                    ((LinearLayout) findViewById(commonAdapter, R.id.ll_change_hours)).setVisibility(View.INVISIBLE);
                } else if (TextUtils.isEmpty(tempamountbean.getPersent()) && TextUtils.isEmpty(tempamountbean.getDValue())) {
                    ((LinearLayout) findViewById(commonAdapter, R.id.ll_change_hours)).setVisibility(View.INVISIBLE);
                } else {
                    ((ImageView) findViewById(commonAdapter, R.id.img_up_or_down)).setVisibility(View.GONE);
                    ((TextView) findViewById(commonAdapter, R.id.tv_add_price)).setText(" " + tempamountbean.getDValue());
                    ((TextView) findViewById(commonAdapter, R.id.tv_rate_num)).setText(" " + "(" + tempamountbean.getPersent() + ")" + " ");
                    ((TextView) findViewById(commonAdapter, R.id.day_change)).setText(" " + getString(R.string.hours_change));
                }
            }
        }

        EventBus.getDefault().post(new LocalCurrencyEvent(currencyLocal));
        ClientConfig.Instance().setSaveLocal(currencyLocal);
    }

    private void setData(LineChart mChart, List<Entry> entries, boolean bezier) {
        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(entries, null);
            if (bezier) set1.setMode(CUBIC_BEZIER);
            set1.setDrawIcons(true);
            if (!bezier) {
                if (entries.get(0).getY() < 1.0) {
                    mChart.getAxisLeft().setStartAtZero(true);
                    //去掉点（这里开启点并且给点设置1半径 解决画线不连贯问题）
                    set1.setDrawCircles(false);
                }

            } else {
                //去掉点（这里开启点并且给点设置1半径 解决画线不连贯问题）
                set1.setDrawCircles(true);
            }

            // set the line to be drawn like this "- - - - - -"
            //去掉虚线
            //  set1.enableDashedLine(10f, 5f, 0f);
            //    set1.enableDashedHighlightLine(10f, 0f, 0f);
            set1.setColor(getResources().getColor(R.color.color_425A92));
            set1.setCircleColor(getResources().getColor(R.color.color_425A92));
            set1.setLineWidth(2f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(false);
            //把高亮颜色设置成透明色解决十字线颜色问题
            set1.setHighlightEnabled(true);
            set1.setHighLightColor(getResources().getColor(R.color.color_00000000));//设置十字线的颜色
            set1.setDrawHighlightIndicators(true);
            if (bezier) {
                set1.setCircleRadius(3f);
            } else {
                set1.setCircleRadius(1f);//修改线之间不连贯问题;//修改线之间不连贯问题
            }
            //去点上的数字
            set1.setDrawValues(!set1.isDrawValuesEnabled());

//            if (com.github.mikephil.charting.utils.Utils.getSDKInt() >= 18) {//设置走势图北京阴影颜色
//                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.BLACK);
//            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets
            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            // set data
            mChart.setData(data);
            mChart.highlightValue(new Highlight(entries.size() - 1, entries.get(entries.size() - 1).getY(), 0), false);
        }
    }

    //添加头部的方法
    private void setHeader(RecyclerView view) {
        commonAdapter.setHeaderView(view2);
    }

    private void setFooter(RecyclerView view) {
        commonAdapter.setFooterView(view1);
    }

    private View findViewById(CommonAdapter commonAdapter, int id) {
        if (commonAdapter.getHeaderLayout() != null) {
            return commonAdapter.getHeaderLayout().findViewById(id);
        }

        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back_up:
                if (dialogBack != null && dialogBack.isShowing()) {
                    dialogBack.dismiss();
                }
                boolean mIsPhrase = true;
                ARouter.getInstance().build(ArouterConstants.REMPHRASE)
                        .withBoolean("mIsPhrase", mIsPhrase).navigation();
                ClientConfig.Instance().setPos(888);
                break;
            case R.id.btn_cancel:
                break;
            case R.id.tv_next_say:
                ClientConfig.Instance().setPos(888);
                dialogBack.dissmissDialog(new MyDialog.DissMissCustomerDialog() {
                    @Override
                    public void dissMissAction() {

                    }
                });
                break;
            case R.id.notice_root:
                if (tempBean != null) {
                    if (TextUtils.isEmpty(tempBean.getContentUrl()))
                        return;//如果没有url不跳转
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title", tempBean.getMsgTitle());
                    intent.putExtra("url", tempBean.getContentUrl());
                    startActivity(intent);
                    ((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).setVisibility(View.GONE);
                    requestReadNotice(tempBean.getId());//不是可跳转通知 则去读
                }
                break;
            case R.id.icon_close_notice://关闭公告
                closeNotice((LinearLayout) findViewById(commonAdapter, R.id.notice_root), tempBean, true);
                break;
        }
    }

    private int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvAsset.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        LogUtil.e("getScollYDistance", (position) * itemHeight - firstVisiableChildView.getTop() + "");
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        if (isAdded()) {
            ToastUtil.show(getResources().getString(R.string.server_connection_error));
            SmartRefreshUtil.onUpdateSmartRefresh(smartrefreshlayout);
        }
    }

    @Override
    public void updateRequestTopNotice(NoticeTopBean param) {
        if (param == null) return;//数据为空
        if (param.getMsgType() != 3 && param.getId() != 0) {
            openNotice((LinearLayout) findViewById(commonAdapter, R.id.notice_root), param);
        } else if (param.getMsgType() == 3 && param.getReaded() != 0) {//弹框类型公告
            EventBus.getDefault().post(param, "DOLEON");
        }

    }


    //公告布局操作
    private NoticeTopBean tempBean;

    private void openNotice(LinearLayout layout, NoticeTopBean bean) {
        //设置公告内容
        if (layout != null && bean != null) {
            ((TextView) layout.findViewById(R.id.noticetitle)).setText(bean.getMsgTitle() + ":" + bean.getSecondaryTitle());
            tempBean = bean;
        }

        if (((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getVisibility() == View.GONE) {
            //显示公告
            if (bean.getMsgType() == 1) {
                layout.setBackgroundColor(getResources().getColor(R.color.color_FFFF6666));
            } else {
                layout.setBackgroundColor(getResources().getColor(R.color.color_47A6FF));
            }
            layout.setVisibility(View.VISIBLE);
            layout.startAnimation(CommonLayoutAnimationHelper.getAnimationSetFromTop());
        }
    }


    private void closeNotice(LinearLayout layout, NoticeTopBean bean, boolean needrequest) {
        if (bean == null) return;
        if (((LinearLayout) findViewById(commonAdapter, R.id.notice_root)).getVisibility() == View.VISIBLE) {
            //layout.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_bottom_in));
            layout.setVisibility(View.GONE);
            if (needrequest) {
                requestReadNotice(bean.getId());//不是可跳转通知 则去读
            }
        }
    }


    //阅读公告
    private void requestReadNotice(int msgId) {
        mPresenter.requestReadNotice(msgId);
    }


    @Override
    public void updateRequestReadNotice(Object object) {
        tempBean = null;
    }

    @Override
    public void updateRequestHistoryAmountNew(HistoryAmountBean param) {
        SmartRefreshUtil.onUpdateSmartRefresh(smartrefreshlayout);
        tempamountbean = param;
        if (param != null && param.getShowType() == 0) {//资产
            ACacheUtil.get(mContext).put(AcacheKeys.switchtype, "0");
            //((BGABanner) findViewById(commonAdapter, R.id.bga_puretaste_banner)).setData(R.layout.item_home_asset_layout, amountbean.getData().getHistoryAssetData(), null);
            bgaBanner.setData(R.layout.item_home_asset_layout_new, param.getHistoryAssetData(), null);
        } else if (param != null && param.getShowType() == 1) {//走势图
            LogUtil.i("------getActivity()->", getActivity() + "");
            ACacheUtil.get(mContext).put(AcacheKeys.switchtype, "1");
            bgaBanner.setData(R.layout.item_home_runchar_layout_new, param.getHistoryMarketData(), null);
        }
        //最后一组是总资产
//              HomeDataBean.HistoryAssetDataBean  countdata= noticeBean.getData()
//                      .getHistoryAssetData()
//                      .get(noticeBean.getData()
//                      .getHistoryAssetData().size()-1);
        //获取资产或者走势
        //排序默认到top
        sortListAseets(param);
        //设置数据

    }

    private void sortListAseets(HistoryAmountBean noticeBean) {
        if (noticeBean != null && noticeBean.getShowType() == 0) {
            HistoryAmountBean.HistoryAssetDataBean tempbean = null;
            for (Iterator<HistoryAmountBean.HistoryAssetDataBean> it = noticeBean.getHistoryAssetData().iterator(); it.hasNext(); ) {
                HistoryAmountBean.HistoryAssetDataBean itemBean = (HistoryAmountBean.HistoryAssetDataBean) it.next();
                if (itemBean.isIsDefault()) {
                    tempbean = itemBean;
                    it.remove();
                }
            }
            if (tempbean != null) noticeBean.getHistoryAssetData().add(0, tempbean);
        } else if (noticeBean != null && noticeBean.getShowType() == 1) {
            HistoryAmountBean.HistoryMarketData tempbeanMarket = null;
            for (Iterator<HistoryAmountBean.HistoryMarketData> it = noticeBean.getHistoryMarketData().iterator(); it.hasNext(); ) {
                HistoryAmountBean.HistoryMarketData itemBeanMarket = (HistoryAmountBean.HistoryMarketData) it.next();
                if (itemBeanMarket.isIsDefault()) {
                    tempbeanMarket = itemBeanMarket;
                    it.remove();
                }
            }
            if (tempbeanMarket != null)
                noticeBean.getHistoryMarketData().add(0, tempbeanMarket);
        }
    }

    @Override
    public void updateRequestCheckStatus(CheckStatusBean param) {
        LoginStatus bean1 = LoginUtils.getLoginBean(mContext);
        ACacheUtil.get(mContext).put(bean1.getDevice_id() + AcacheKeys.SAVEISVAILDPIN, param.getPayPassword().isValid());
        LogUtil.e("LoginStatus===", param.isWallet_phrase() + "");
        ACacheUtil.get(mContext).put(AcacheKeys.ISBINDPHONE, param.getMobile_valid().isValid());
        if (!param.isWallet_phrase()) {
            LoginStatus bean = LoginUtils.getLoginBean(mContext);
            String twoTime = TimeUtils.getTime();//当前时间
            String oneTime = ACacheUtil.get(mContext).getAsString(bean.getId() + AcacheKeys.cache1);
            if (TextUtils.isEmpty(oneTime)) {//用户第一次进来没有设置找回密码
                if (dialogBack != null) dialogBack.show();
                ACacheUtil.get(mContext).put(bean.getId() + AcacheKeys.cache1, twoTime);
            } else {//第二次进来不为空时间比较
                int diff = (int) TimeUtils.getFromEndTime(oneTime, twoTime);
                if (diff > 10000) {
                    ACacheUtil.get(mContext).put(bean.getId() + AcacheKeys.cache1, twoTime);
                    if (dialogBack != null) dialogBack.show();
                }
            }
        }
    }

    @OnClick({R.id.boxmail, R.id.img_expend, R.id.icon_scon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.boxmail:
                navigation(ArouterConstants.NOTICE_LIST);
                //OTCGetOtcOrdersBean bean=new OTCGetOtcOrdersBean();
                //bean.setStatus(OTCOrderStatus.WaitPay);
                //ARouter.getInstance().build(TRADE_DETAIL).withSerializable("OTCGetOtcOrdersBean",bean).navigation();
                //navigation(TRADE_DETAIL);
                //ARouter.getInstance().build(TRADE_DETAIL).withSerializable("111",null);
                break;
            case R.id.img_expend:
                navigation(ArouterConstants.ADD_NEW_CURRENCY);
                break;
            case R.id.icon_scon:
                new RxPermissions(getActivity())
                        .request(Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            LogUtil.i("--------granted------>", granted + "");
                            if (granted) {
                                ARouter.getInstance()
                                        .build(Constants.SCANLOGIN)
                                        .withBoolean("isfinish", false)
                                        .navigation();
                            } else {
                                ToastUtil.show(getString(R.string.no_permission_camera));
                            }
                        });
                break;
        }
    }


    @Subscriber
    public void onTotalingInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 998) {
                requestCount(true);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }

    @Subscriber(tag = EventBusTags.SCAN2AUTH)
    public void onAuthResult(ScanLoginReqParam reqparam) {
        if (reqparam == null) return;
        ARouter.getInstance()
                .build(Constants.AUTHLOGIN)
                .withSerializable("reqparam", reqparam)
                .withTransition(R.anim.push_bottom_in, 0).notifyAll();
    }

    //处理授权结果
    @Subscriber
    public void OnAuthLoginResult(ScanLoginBean bean) {
        if (bean == null) {
            ToastUtil.show(getString(R.string.str_authlogin_fail));
            return;
        }
        if (bean.getStatus().equals("2")) {
            ToastUtil.show(getString(R.string.str_authlogin_seccess));
        } else if (bean.getStatus().equals("3")) {
            ToastUtil.show(getString(R.string.str_code_fail));
        } else {
            ToastUtil.show(getString(R.string.str_authlogin_fail));
        }
    }

    @Subscriber
    public void OnCancelAuthLogin(String result) {
        ToastUtil.show(result);
    }

    private int ruleYFormatter(AxisBase axis) {
        //大波小波算法
        //去掉科学计数法E
        if (axis.mEntries != null && axis.mEntries.length > 0) {
            String lastvalueE = com.idcg.idcw.utils.Utils.toNormal(axis.mEntries[axis.mEntries.length - 1]);
            String frontvalueE = com.idcg.idcw.utils.Utils.toNormal(axis.mEntries[0]);
            //String currentvalueE = com.idcg.idcw.utils.Utils.toNormal(value);
            //截取小数位
            String lastvalue = com.idcg.idcw.utils.Utils.toSubStringDegistNo(lastvalueE, 4).trim();
            String frontvalue = com.idcg.idcw.utils.Utils.toSubStringDegistNo(frontvalueE, 4).trim();
            //String currentvalue = com.idcg.idcw.utils.Utils.toSubStringDegistNo(currentvalueE, 5);
            if (lastvalue.contains(",") || frontvalue.contains(",")) {
                lastvalue = lastvalue.replaceAll(",", "");
                frontvalue = frontvalue.replaceAll(",", "");
            }
            double offset = com.idcg.idcw.utils.Utils.sub2Double(Double.parseDouble(lastvalue), Double.parseDouble(frontvalue));
            if (offset > 10d) {
                return 1;
            } else {
                return 0;
            }
        }
        return -1;
    }

    //如果后面原来的小数点位数一样就不需要补0
    private boolean ruleZero(AxisBase axis) {
        if (axis.mEntries != null && axis.mEntries.length > 0) {
            List<String> axisEntries = new ArrayList<>();
            String lastvalueE = com.idcg.idcw.utils.Utils.toNormal(axis.mEntries[axis.mEntries.length - 1]);
            String frontvalueE = com.idcg.idcw.utils.Utils.toNormal(axis.mEntries[0]);

            //判断小数位是否相等 如果相等则不需要补0 否则需要补0
            for (int i = 0; i < axis.mEntries.length; i++) {
                String valueE = com.idcg.idcw.utils.Utils.toNormal(axis.mEntries[i]);
                if (valueE.contains(".")) {
                    String temp = valueE.split("\\.")[1];
                    axisEntries.add(temp);
                } else {
                    return false;
                }
            }

            //判断小数位是否相同 相同-不补0，不相同-补0
            for (String value : axisEntries) {
                if (axisEntries.get(0).length() != value.length()) {
                    return true;
                }
            }
        }
        return false;
    }
}
