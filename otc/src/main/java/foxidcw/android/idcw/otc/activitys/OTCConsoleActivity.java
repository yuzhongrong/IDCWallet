package foxidcw.android.idcw.otc.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cjwsc.idcm.Utils.KeyboardUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ScreenUtil;
import com.cjwsc.idcm.Utils.SpUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.BaseSignalConstants;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.cjwsc.idcm.signarl.impl.HubOnDataCallBackImpl;
import com.google.gson.Gson;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.adapter.OTCConsoleAdapter;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCCancelOrderServices;
import foxidcw.android.idcw.otc.iprovider.OTCConfirmOfferOrderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetQuoteOrdersServices;
import foxidcw.android.idcw.otc.model.beans.OTCConfirmOrderResBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcSettingBean;
import foxidcw.android.idcw.otc.model.beans.OTCOrderQuotePriceBean;
import foxidcw.android.idcw.otc.model.params.OTCOrderIdQuoteIdReqParams;
import foxidcw.android.idcw.otc.utils.OTCUtils;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTimeUpPopup;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTitleContent2BtnPopWindow;
import foxidcw.android.idcw.otc.widgets.widget.EndTimerTextView;

@Route(path = OTCConstant.CONSOLE, name = "操作台界面")
public class OTCConsoleActivity extends BaseWalletActivity implements View.OnClickListener {

    private View mStatusView;

    // tab 切换排序方式
    private MagicIndicator mTabSt;
    private FragmentContainerHelper mContainerHelper;

    private RecyclerView mConsoleItemsRv;
    //列表适配器
    private OTCConsoleAdapter mOTCConsoleAdapter;
    private List<OTCOrderQuotePriceBean> mQuoteLists = new ArrayList<>();

    private TextView mNoDataTv; // 空数据
    private ImageButton mCloseIb; // 关闭

    // OrderID
    private String mOrderId;
    private boolean mIsBuy = false;
    private String mSymbol;
    private String mAmount;
    private int mSeconds;
    private String mCurrency;
    private static final String OTCCONSOLE_CONSTANT = "OrderID";
    private static final String OTCCONSOLE_SYMBOL = "symbol";
    private static final String OTCCONSOLE_AMOUNT = "amount";
    private static final String OTCCONSOLE_COIN = "coin";
    private static final String OTCCONSOLE_SECONDS = "seconds";
    private static final String OTCCONSOLE_TYPE = "Is_Buy";
    @Autowired
    OTCGetQuoteOrdersServices mOTCGetQuoteOrdersServices;

    @Autowired
    OTCConfirmOfferOrderServices mOTCConfirmOfferOrderServices;

    @Autowired
    OTCCancelOrderServices mOTCCancelOrderServices;

    private TextView mConsoleTypeTv; // 买卖类型
    private EndTimerTextView mTimerViewTv; // 倒计时
    private int mCurrentType = 0; // 默认价格排序
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BaseSignalConstants.isAddGroup=false;
        super.onCreate(savedInstanceState);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_otc_console_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        dialog.setBackPressEnable(false);
        initDatas();
        mStatusView = findViewById(R.id.otc_console_status_view_vi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            mStatusView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusView.getLayoutParams();
        params.height = ScreenUtil.getStatusBarHeight(mCtx);
        mStatusView.setLayoutParams(params);

        mTabSt = findViewById(R.id.otc_console_tab_magic);
        initIndicator();

        mConsoleItemsRv = findViewById(R.id.otc_console_items_rv);
        mConsoleItemsRv.setLayoutManager(new LinearLayoutManager(this));
        mOTCConsoleAdapter = new OTCConsoleAdapter(mQuoteLists);
        mOTCConsoleAdapter.setmIsBuy(mIsBuy);
        mOTCConsoleAdapter.setmCurrency(mCurrency);
        mConsoleItemsRv.setAdapter(mOTCConsoleAdapter);
        mConsoleItemsRv.addOnItemTouchListener(listener);

        mNoDataTv = findViewById(R.id.otc_console_no_data_tv);
        mConsoleTypeTv = findViewById(R.id.otc_console_type_tv);
        mTimerViewTv = findViewById(R.id.otc_console_limit_time_tv);

        mCloseIb = findViewById(R.id.otc_console_close_ib);
        mCloseIb.setOnClickListener(this);

        int resStrId;
        if (mIsBuy) {
            resStrId = R.string.str_otc_console_buy_info;
        } else {
            resStrId = R.string.str_otc_console_sell_info;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        builder.append(Utils.toSubStringDegist(Double.parseDouble(mAmount), 4));
        builder.append(" ");
        builder.append(mSymbol);
        String str = String.format(getResources().getString(resStrId), builder.toString());
        mConsoleTypeTv.setText(str);

        mTimerViewTv.setVarTime(mSeconds, () -> {
            // 倒计时结束回调
            int resId;
            if (mOTCConsoleAdapter.getData().size() <= 0) {
                // 很遗憾没有承兑商报价
                resId = R.string.str_otc_time_up_no_acceptance;
            } else {
                // 未作出选择
                resId = R.string.str_otc_time_up_you_not_selected;
            }
            //弹出对话框
            if (KeyboardUtil.isSoftShowing(mCtx))
                KeyboardUtil.hideInputMethod(OTCConsoleActivity.this);
            OTCTimeUpPopup popup = new OTCTimeUpPopup(mCtx)
                    .setTimesUpInfo(resId)
                    .setListener(() -> finish());
            popup.showPopupWindow();
        });

//        mIsBuy = true;
//        for (int i = 0; i < 10; i++) {
//            OTCOrderQuotePriceBean item = new OTCOrderQuotePriceBean();
//            item.setCompareType(mCurrentType);
//            item.setBuy(mIsBuy);
//            item.setAmount(new Random().nextDouble());
//            item.setAvgPayTime(new Random().nextInt(50));
//            mQuoteLists.add(item);
//        }
//        resetDatas();
    }

    private void initDatas() {
        if (getIntent().hasExtra(OTCCONSOLE_CONSTANT)) {
            mOrderId = getIntent().getStringExtra(OTCCONSOLE_CONSTANT);
        }
        if (getIntent().hasExtra(OTCCONSOLE_TYPE)) {
            mIsBuy = getIntent().getBooleanExtra(OTCCONSOLE_TYPE, false);
        }
        if (getIntent().hasExtra(OTCCONSOLE_SYMBOL)) {
            mSymbol = getIntent().getStringExtra(OTCCONSOLE_SYMBOL);
        }
        if (getIntent().hasExtra(OTCCONSOLE_AMOUNT)) {
            mAmount = getIntent().getStringExtra(OTCCONSOLE_AMOUNT);
        }
        if (getIntent().hasExtra(OTCCONSOLE_SECONDS)) {
            mSeconds = getIntent().getIntExtra(OTCCONSOLE_SECONDS, 180);
        }
        if (getIntent().hasExtra(OTCCONSOLE_COIN)) {
            mCurrency = getIntent().getStringExtra(OTCCONSOLE_COIN);
        }
    }

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            OTCOrderQuotePriceBean item = mOTCConsoleAdapter.getItem(position);
            if (null == item) return;
            OTCTitleContent2BtnPopWindow popWindow = new OTCTitleContent2BtnPopWindow(mCtx);
            popWindow.setTitle(getResources().getString(R.string.str_otc_sure_to_deal_title))
                    .setContent(String.format(getResources().getString(R.string.str_otc_sure_to_deal_info)
                            , String.valueOf(Utils.toSubStringDegist(item.getAmount(), 2) + " " + mCurrency), item.getAcceptantName()))
                    .setClickListener(type -> {
                        if (type != 1) return;
                        OTCOrderIdQuoteIdReqParams reqParams = new OTCOrderIdQuoteIdReqParams();
                        reqParams.setOrderId(item.getOrderId());
                        reqParams.setQuoteOrderId(item.getQuoteId());
                        mOTCConfirmOfferOrderServices.confirmOfferOrder(reqParams)
                                .compose(bindToLifecycle())
                                .subscribeWith(new RxProgressSubscriber<OTCConfirmOrderResBean>(OTCConsoleActivity.this) {
                                    @Override
                                    public void onSuccess(OTCConfirmOrderResBean data) {
                                        if (data == null) return;
                                        ///ToastUtil.show(data.toString());
                                        ARouter.getInstance()
                                                .build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", item.getOrderId())
                                                .navigation();
                                        finish();
                                    }

                                    @Override
                                    protected void onError(ResponseThrowable ex) {
                                        ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                    }
                                });
                    });
            popWindow.showPopupWindow();
        }
    };

    private void setEmptyView() {
//        mConsoleItemsRv.setVisibility(View.VISIBLE);
//        mNoDataTv.setVisibility(View.GONE);
        if (mOTCConsoleAdapter.getData().size() <= 0) {
            mConsoleItemsRv.setVisibility(View.GONE);
            mNoDataTv.setVisibility(View.VISIBLE);
        } else {
            mConsoleItemsRv.setVisibility(View.VISIBLE);
            mNoDataTv.setVisibility(View.GONE);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        // 启动signalr
        initSignalr(BaseSignalConstants.SIGNAL_HOST, "", BaseSignalConstants.SIGNAL_HUB_NAME);
        mOTCGetQuoteOrdersServices.getQuoteOrders(mOrderId)
                .compose(bindToLifecycle())
                .subscribeWith(new RxSubscriber<List<OTCOrderQuotePriceBean>>() {
                    @Override
                    public void onSuccess(List<OTCOrderQuotePriceBean> OTCOrderQuotePriceBeans) {
                        LogUtil.e("otcOrderQuotePriceBeansotcOrderQuotePriceBeansotcOrderQuotePriceBeansotcOrderQuotePriceBeansotcOrderQuotePriceBeans");
                        mQuoteLists.clear();
                        mQuoteLists.addAll(OTCOrderQuotePriceBeans);
                        resetDatas();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        LogUtil.e("11111111111111111");
                        setEmptyView();
                    }
                });
    }

    @Override
    public void subScribes() {
        subscribe("quotePriceNotification", new HubOnDataCallBackImpl<OTCOrderQuotePriceBean>() {
            @Override
            public void convert(OTCOrderQuotePriceBean o) {
                //ToastUtil.show(o.toString());
                LogUtil.e(o.toString());
                mQuoteLists.add(o);
                resetDatas();
            }
        });
    }


    @Override
    protected BaseView getView() {
        return null;
    }


    @Override
    public void onBackPressed() {
        int count = SpUtils.getIntData("cancel_count", 0);
        String tradeSettingInfo = SpUtils.getStringData("otc_trade_setting_bean", "");
        OTCGetOtcSettingBean settingBean = new OTCGetOtcSettingBean();
        if (!TextUtils.isEmpty(tradeSettingInfo)) {
            settingBean = new Gson().fromJson(tradeSettingInfo, OTCGetOtcSettingBean.class);
        }
        OTCTitleContent2BtnPopWindow popWindow = new OTCTitleContent2BtnPopWindow(mCtx);
        popWindow.setTitle(getResources().getString(R.string.str_otc_cancel_deal_title))
                .setContent(String.format(getResources().getString(R.string.str_otc_cancel_order_info_with_24),
                        OTCUtils.getCancelCountStringWithLanguage(mCtx,count + 1), settingBean.getAllowCancelOrderDuration(),
                        settingBean.getAllowCancelOrderCount(), settingBean.getCancelOrderForbidTradeDuration()))
                .setConfirmContent(getResources().getString(R.string.otc_str_btn_yes))
                .setCancelContent(getResources().getString(R.string.otc_str_btn_no))
                .setClickListener(type -> {
                    if (type != 1) return;
                    cancelOrder(count);
                });
        popWindow.showPopupWindow();
    }

    @SuppressLint("CheckResult")
    private void cancelOrder(int count) {
        mOTCCancelOrderServices.cancelOrder(mOrderId)
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<OTCConfirmOrderResBean>(OTCConsoleActivity.this) {
                    @Override
                    public void onSuccess(OTCConfirmOrderResBean data) {
                        //SpUtils.setIntData("cancel_count", count);
                        finish();
                        ToastUtil.show(getResources().getString(R.string.otc_str_cancel_order_success));
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                    }
                });
    }

    private void initIndicator() {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add(getString(R.string.str_otc_sort_with_price));
        tabs.add(getString(mIsBuy ? R.string.str_otc_sort_with_time : R.string.str_otc_sort_with_receiver));
        CommonNavigator navigator = new CommonNavigator(mCtx);
        navigator.setAdjustMode(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int i) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color.tip_black));
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color.c_FF2E406B));
                colorTransitionPagerTitleView.setText(tabs.get(i));
                colorTransitionPagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                colorTransitionPagerTitleView.setOnClickListener(view -> {
                    mContainerHelper.handlePageSelected(i);
                    mCurrentType = i;
                    resetDatas();
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setLineHeight(ConvertUtils.dp2px(2));
                indicator.setColors(getResources().getColor(R.color.c_FF2E406B));
                indicator.setPadding(0, 0, 0, ConvertUtils.dp2px(2));
                indicator.setRoundRadius(ConvertUtils.dp2px(5));
                return indicator;
            }
        });
        mTabSt.setNavigator(navigator);
        mContainerHelper = new FragmentContainerHelper(mTabSt);
    }

    private void resetDatas() {
        if (mQuoteLists != null && mQuoteLists.size() > 1) {
            for (OTCOrderQuotePriceBean bean : mQuoteLists) {
                bean.setBuy(mIsBuy);
                bean.setCompareType(mCurrentType);
            }
            Collections.sort(mQuoteLists);
        }
        mOTCConsoleAdapter.setNewData(mQuoteLists);
        mOTCConsoleAdapter.notifyDataSetChanged();
        setEmptyView();
    }


    @Override
    protected String getUserId() {
        return String.valueOf(LoginUtils.getLoginBean(mCtx).getId());
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.otc_console_close_ib) {
            onBackPressed();
        }
    }
}
