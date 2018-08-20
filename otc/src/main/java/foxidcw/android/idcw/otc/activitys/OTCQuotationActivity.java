package foxidcw.android.idcw.otc.activitys;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.KeyboardUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ScreenUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.BaseSignalConstants;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.cjwsc.idcm.signarl.impl.HubOnDataCallBackImpl;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.concurrent.TimeUnit;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.iprovider.OTCQuotePriceServices;
import foxidcw.android.idcw.otc.model.beans.OTCConfirmQuoteOrderMessageBean;
import foxidcw.android.idcw.otc.model.beans.OTCNewOrderNoticeAcceptantBean;
import foxidcw.android.idcw.otc.model.beans.OTCQuoteOrderPriceEventBean;
import foxidcw.android.idcw.otc.model.beans.OTCStateBean;
import foxidcw.android.idcw.otc.model.params.OTCQuotePriceReqParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTimeUpPopup;
import foxidcw.android.idcw.otc.widgets.widget.EndTimerTextView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

@Route(path = OTCConstant.QUOTATION, name = "查看确认买入卖出界面")
public class OTCQuotationActivity extends BaseWalletActivity implements View.OnClickListener {

    // 模拟状态栏
    private View mStatusVi;
    // 关闭
    private ImageButton mCloseIb;
    // 剩余时间
    private EndTimerTextView mCountDownTv;
    // 数量
    private TextView mAmnountTv;
    // 买入卖出方姓名
    private TextView mQuotationNameTv;
    // 银行卡
    private ImageView pay_type;
    // 银行卡
    private ImageView mIconAlipayIv;
    // 申诉次数
    private TextView mAppealCountTv;
    // 平均支付时间
    private TextView mSubPayTimeTv;
    // 平均确认买入时间
    private TextView mSubBuyTime;
    // 单价
    private TextView mSinglePriceEt;
    // 单价币种
    private TextView mSingleCurrencyTv;
    // 总价
    private TextView mTotalPriceTv;
    // 总价币种
    private TextView mTotalCurrencyTv;
    // 确认按钮
    private Button mConfirmBtn;
    // 超时时间显示
    private TextView mQuotationTypeTv;

    /**
     * 新版的所有
     */
    // 已经报价显示的界面
    private FrameLayout mAlreadyQuoteFl;
    // 未报价显示界面
    private RelativeLayout mNoQuoteRl;
    // 市场价格
    private TextView mMarketPriceTv;
    // 溢价比例
    private TextView mPremiumNewTv;
    // 输入价格的币种
    private TextView mQuotePriceCurrencyTv;
    // 新版的输入单价
    private EditText mNewSinglePrice;
    // 新版总价
    private TextView mNewTotalPriceTv;
    // 新版总价币种
    private TextView mNewTotalPriceCurrencyTv;

    // 界面显示bean
    private OTCNewOrderNoticeAcceptantBean intentBean;

    private TextView mTimeTv1;
    private TextView mTimeTv2;

    @Autowired
    OTCQuotePriceServices mOTCQuotePriceServices;

    private int mSuccessType;
    private int confirmText;
    private int mQuoteId;
    private boolean isNeedFinish = true;

    // 当前的总价
    private double mCurrentTotalPrice;
    // 当前的法币币种
    private String mCurrencyCode;
    // 当前的溢价比例
    private double mCurrentPremiumPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BaseSignalConstants.isAddGroup = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otc_quotation;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mStatusVi = findViewById(R.id.otc_quotation_status_view_vi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            mStatusVi.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusVi.getLayoutParams();
        params.height = ScreenUtil.getStatusBarHeight(mCtx);
        mStatusVi.setLayoutParams(params);

        mCloseIb = findViewById(R.id.otc_quotation_close_ib);
        mCloseIb.setOnClickListener(this);

        mCountDownTv = findViewById(R.id.otc_quotation_limit_time_tv);

        mAmnountTv = findViewById(R.id.otc_quotation_amount_tv);

        mQuotationNameTv = findViewById(R.id.otc_quotation_name_tv);

        pay_type = findViewById(R.id.otc_quotation_pay_type_iv);
        mIconAlipayIv = findViewById(R.id.otc_quotation_icon_alipay_iv);

        mAppealCountTv = findViewById(R.id.otc_quotation_appeal_count_value_tv);
        mSubPayTimeTv = findViewById(R.id.otc_quotation_sub_time_value_tv);
        mSubBuyTime = findViewById(R.id.otc_quotation_sub_confirm_time_value_tv);

        mSinglePriceEt = findViewById(R.id.otc_quotation_single_price_key);
        mSingleCurrencyTv = findViewById(R.id.otc_quotation_currency_tv);

        mTotalPriceTv = findViewById(R.id.otc_quotation_total_price_tv);
        mTotalCurrencyTv = findViewById(R.id.otc_quotation_total_currency_tv);

        mConfirmBtn = findViewById(R.id.otc_quotation_confirm_btn);
        mConfirmBtn.setOnClickListener(this);

        mQuotationTypeTv = findViewById(R.id.otc_quotation_type_tv);

        mTimeTv1 = findViewById(R.id.otc_quotation_sub_time_tv);
        mTimeTv2 = findViewById(R.id.otc_quotation_sub_confirm_time_tv);

        mAlreadyQuoteFl = findViewById(R.id.otc_quotation_already_quote_fl);
        mNoQuoteRl = findViewById(R.id.otc_quotation_no_quote_rl);
        mMarketPriceTv = findViewById(R.id.otc_quotation_market_price_tv);
        mPremiumNewTv = findViewById(R.id.otc_quotation_your_premium_price_tv);
        mQuotePriceCurrencyTv = findViewById(R.id.otc_quotation_your_price_currency_tv);
        mNewSinglePrice = findViewById(R.id.otc_quotation_your_quote_price_tv);
        mNewTotalPriceTv = findViewById(R.id.otc_quotation_your_total_price_tv);
        mNewTotalPriceCurrencyTv = findViewById(R.id.otc_quotation_your_total_price_currency_tv);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        // 启动signalr
        initSignalr(BaseSignalConstants.SIGNAL_HOST, "", BaseSignalConstants.SIGNAL_HUB_NAME);
        if (getIntent() != null && getIntent().getExtras() != null) {
            intentBean = (OTCNewOrderNoticeAcceptantBean) getIntent().getExtras().getSerializable(OTCConstant.NOTICEORDERBEAN);
        }
//        RxTextView.textChanges(mSinglePriceEt)
//                .filter(charSequence -> mSinglePriceEt.isFocused())
//                .subscribe(charSequence -> observerStopInput()
//                );
//
//        RxTextView.textChanges(mTotalPriceTv)
//                .filter(charSequence -> mTotalPriceTv.isFocused())
//                .subscribe(charSequence -> observerStopInput());
        if (intentBean.getStatus() != 1) {
            RxTextView.textChanges(mNewSinglePrice)
                    .subscribe(charSequence -> observerStopInput());
            mNewSinglePrice.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
                String sourceStr = source.toString();
                String destStr = dest.toString();
                if ((TextUtils.isEmpty(destStr) || destStr.contains(".")) && sourceStr.equals(".")) { // 如果首位输入. 或者源str已经有.
                    ToastUtil.show(mCtx.getResources().getString(R.string.otc_str_input_right_price));
                    return "";
                }
                return null;
            }});
        } // 不是已报价的时候才订阅

        updateUi();
    }


    @Override
    protected String getUserId() {//解决有时候重连报错问题
        if (LoginUtils.getLoginBean(this) != null)
            return String.valueOf(LoginUtils.getLoginBean(this).getId());
        return "";

    }

    @Override
    public void subScribes() {
        // 用户确认报价之后推送
        subscribe("confirmQuoteOrderMessage", new HubOnDataCallBackImpl<OTCConfirmQuoteOrderMessageBean>() {
            @Override
            public void convert(OTCConfirmQuoteOrderMessageBean o) {
                LogUtil.e("用户确认了  ---> " + o.toString());
                // 如果选择的是我
                if (intentBean.getOrderID() == o.getOrderId()
                        && intentBean.getAcceptantId() == o.getAcceptantUserId()
                    //&& mQuoteId == o.getQuoteOrderId()
                        ) {
                    ARouter.getInstance()
                            .build(OTCConstant.TRADE_DETAIL)
                            .withInt("order_id", o.getOrderId())
                            .navigation(OTCQuotationActivity.this);
                    finish();
                    // 不是我
                } else {
                    finish();
                }
                //ToastUtil.show("用户确认了 ~~~~~ confirmQuoteOrderMessage");
            }
        });
    }

    // 订阅订单状态变更通知
    @Subscriber(tag = EventTags.TAGS_STATUS_CHANGE_MAIN)
    public void reciveOrderChanged(OTCStateBean statebean) {
        try {
            if (null == statebean || statebean.getOrderID() != intentBean.getOrderID()) return;
            int status = statebean.getStatus();
            if ((status == 8 || status == 9 || status == 10) && isNeedFinish) {
                finish();
            }
        } catch (Exception e) {
        }
    }

    private double getDoubleValue(EditText editText) {
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

    private void observerStopInput() {
        clearStopInputDisposable();
        mStopInputDisposable = Flowable.timer(0, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
//                    boolean enable = true;
//                    if (mSinglePriceEt.isFocused()) { // 如果在输入总价
//                        String total = Utils.toSubStringDegist(Utils.mul(getDoubleValue(mSinglePriceEt), intentBean.getNum()), 2);
//                        total = total.replaceAll(",", "");
//                        if (Double.parseDouble(total) == 0) {
//                            mTotalPriceTv.setText("0.00");
//                        } else {
//                            mTotalPriceTv.setText(total);
//                            //enable = true;
//                        }
//                    } else {
//                        String single = Utils.toSubStringDegist(Utils.div(getDoubleValue(mTotalPriceTv), intentBean.getNum(), 2), 2);
//                        single = single.replaceAll(",", "");
//                        if (Double.parseDouble(single) == 0) {
//                            mSinglePriceEt.setText("0.00");
//                        } else {
//                            mSinglePriceEt.setText(single);
//                            //enable = true;
//                        }
//                    }
                    setTotalPrice(getDoubleValue(mNewSinglePrice), false);
                    mConfirmBtn.setEnabled(!TextUtils.isEmpty(mNewSinglePrice.getText().toString()));
                });
    }

    Disposable mStopInputDisposable;

    private void clearStopInputDisposable() {
        if (mStopInputDisposable != null && !mStopInputDisposable.isDisposed())
            mStopInputDisposable.dispose();
    }


    @SuppressLint({"SetTextI18n", "StringFormatInvalid", "StringFormatMatches"})
    private void updateUi() {
        if (intentBean == null) return;
        // 设置法币币种
        mCurrencyCode = intentBean.getCurrencyName();
        setCurrencyCode();
        mCountDownTv.setVarTime(intentBean.getDeadLineSeconds(), () -> {
            //弹出对话框
            isNeedFinish = false;
            if (KeyboardUtil.isSoftShowing(mCtx))
                KeyboardUtil.hideInputMethod(OTCQuotationActivity.this);
            OTCTimeUpPopup popup = new OTCTimeUpPopup(mCtx)
                    .setTimesUpInfo(mQuoteId != 0 ? R.string.str_otc_time_up_no_selected : R.string.str_head_release)
                    .setListener(() -> finish());
            popup.showPopupWindow();
        });

        mQuotationTypeTv.setText(String.format(getResources().getString(R.string.str_otc_quotation_info_with_90_minutes), intentBean.getMaxDeadLineSeconds()));

        //处理订单信息显示
        int color;
        StringBuilder strInfo = new StringBuilder();
        if (intentBean.getDirection() == 1) {//买入
            strInfo.append(getResources().getString(R.string.str_otc_single_buy));
            color = R.color.color_2968B9;

            // 平均支付时间
            mSubPayTimeTv.setText(getTimeWithSeconds(intentBean.getUserAvgConfirmReceiveTime()));
            // 平均确认买入时间
            mSubBuyTime.setText(getTimeWithSeconds(intentBean.getUserAvgPayTime()));

            mTimeTv1.setText(R.string.str_otc_sub_receiver_time);
            mTimeTv2.setText(R.string.str_otc_sub_pay_time);

            mSuccessType = R.string.str_otc_waiting_for_buyer;
            confirmText = R.string.str_otc_confirm_to_sell;

            mConfirmBtn.setText(R.string.str_otc_single_sell_in);
        } else {//卖出
            strInfo.append(getResources().getString(R.string.str_otc_single_sell));
            color = R.color.color_f88633;

            // 平均支付时间
            mSubPayTimeTv.setText(getTimeWithSeconds(intentBean.getUserAvgConfirmReceiveTime()));
            // 平均确认买入时间
            mSubBuyTime.setText(getTimeWithSeconds(intentBean.getUserAvgSelloutTime()));
            mTimeTv1.setText(R.string.str_otc_sub_receiver_time);
            mTimeTv2.setText(R.string.str_otc_sub_receiver_money);

            mSuccessType = R.string.str_otc_waiting_for_seller;
            confirmText = R.string.str_otc_confirm_to_buy;

            mConfirmBtn.setText(R.string.str_otc_single_buy_in);
        }
        strInfo.append(" ");
        strInfo.append(Utils.toSubStringDegist(intentBean.getNum(), 4));
        strInfo.append(" ");
        strInfo.append(intentBean.getCoinCode().toUpperCase());
        mAmnountTv.setText(strInfo);
        mAmnountTv.setTextColor(getResources().getColor(color));

        mQuotationNameTv.setText(intentBean.getUserName());

        // 申诉次数
        mAppealCountTv.setText(String.valueOf(intentBean.getUserAppealCount()));

        //设置支付方式
        GlideUtil.loadImageView(this, intentBean.getPayLogo(), pay_type);

//        if (intentBean.IsPremium) {
//            setPrice();
//        } else {
//        }

        // 隐藏和显示是否已经报价
        int status = intentBean.getStatus();
        mAlreadyQuoteFl.setVisibility(status == 1 ? View.VISIBLE : View.GONE);
        mNoQuoteRl.setVisibility(status == 1 ? View.GONE : View.VISIBLE);

        if (status == 1) { // 已经报价的单设置提示等待的文字
            mQuotationTypeTv.setText(mSuccessType);
            mConfirmBtn.setVisibility(View.GONE);

            mSinglePriceEt.setText(Utils.toSubStringDegist(intentBean.getPrice(), 2).replaceAll(",", ""));
            mTotalPriceTv.setText(Utils.toSubStringDegist(Utils.mul(intentBean.getPrice(), intentBean.getNum()), 2).replaceAll(",", ""));

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            /**
             * 1.设置市场价
             * 2.设置溢价
             * 3.根据市场价  以及溢价  计算并设置总价
             */
            mCurrentPremiumPresent = intentBean.getPremium();
            mPremiumNewTv.setText(String.format(mCtx.getResources().getString(R.string.otc_str_your_quote_price_premium_info),
                    getShowPremium(Utils.mul(mCurrentPremiumPresent, 100d))));
            // 市场价
            mMarketPriceTv.setText(Utils.toSubStringDegist(intentBean.getLastPrice(), 2) + " " + mCurrencyCode);

            String singlePrice = Utils.toSubStringDegist((1 + mCurrentPremiumPresent) * intentBean.getLastPrice(), 2);
            mNewSinglePrice.setText(singlePrice);
            mNewSinglePrice.setSelection(singlePrice.length());
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);//同时弹出键盘
            setTotalPrice(intentBean.getLastPrice(), true);
        }
//        if (intentBean.getStatus() == 1) { // 已报价
//            setAlreadyQuotePriceUI();
//            setPrice();
//        }
    }

    /**
     * 根据后台返回的值 进行展示
     *
     * @param v
     * @return
     */
    private String getShowPremium(Double v) {
        String result;
        if (v < 0) {
            result = String.valueOf(v);
        } else if (v == 0) {
            result = String.valueOf("+0");
        } else {
            result = String.valueOf("+" + v);
        }
        if (result.contains(".0")) {
            result = result.replace(".0", "");
        }
        return result;
    }

    /**
     * 根据市场价  以及溢价计算出当前的价格
     *
     * @param lastPrice
     */
    private void setTotalPrice(double lastPrice, boolean needPremium) {
        double currentSinglePrice;
        if (needPremium) {
            currentSinglePrice = (1 + mCurrentPremiumPresent) * lastPrice;
        } else {
            currentSinglePrice = lastPrice;
        }
        String singlePrice = Utils.toSubStringDegist(currentSinglePrice, 2);
        singlePrice = singlePrice.replaceAll(",", "");
        mSinglePriceEt.setText(singlePrice);
        String price = Utils.toSubStringDegist(Utils.mul(Double.parseDouble(singlePrice), intentBean.getNum()), 2);
        price = price.replaceAll(",", "");
        mCurrentTotalPrice = Double.parseDouble(price);
        mTotalPriceTv.setText(price);
        mNewTotalPriceTv.setText(price);
    }

    /**
     * 界面上设置法币币种
     */
    private void setCurrencyCode() {
        mSingleCurrencyTv.setText(mCurrencyCode); // 显示单价币种
        mTotalCurrencyTv.setText(mCurrencyCode); // 显示总价币种

        mQuotePriceCurrencyTv.setText(mCurrencyCode); // 输入单价币种
        mNewTotalPriceCurrencyTv.setText(mCurrencyCode); // 总价币种
    }


    private void setAlreadyQuotePriceUI() {
        mConfirmBtn.setVisibility(View.GONE);
        mQuotationTypeTv.setText(mSuccessType);
        mSinglePriceEt.setEnabled(false);
        mTotalPriceTv.setEnabled(false);
    }

    private void setPrice() {
        String singlePrice = Utils.toSubStringDegist(intentBean.getPrice(), 2);
        singlePrice = singlePrice.replaceAll(",", "");
        mSinglePriceEt.setText(singlePrice);
//        mSinglePriceEt.setSelection(singlePrice.length());
//        mSinglePriceEt.requestFocus();
//        OTCQuotationActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);//同时弹出键盘
//        mTotalPriceTv.setText(Utils.toSubStringDegist(Utils.mul(getDoubleValue(mSinglePriceEt), intentBean.getNum()), 2));
        mConfirmBtn.setEnabled(!TextUtils.isEmpty(mSinglePriceEt.getText().toString()) && !TextUtils.isEmpty(mTotalPriceTv.getText().toString()));
    }

    /**
     * 时间格式化
     *
     * @param time
     * @return
     */
    private String getTimeWithSeconds(long time) {
        if (time < 0) return "00:00";
        time = time * 1000;
        int minute = (int) ((time % (1000 * 60 * 60)) / (1000 * 60));
        int second = (int) ((time % (1000 * 60)) / 1000);
        return String.valueOf((minute < 10 ? String.valueOf("0" + minute) : minute) + ":" + (second < 10 ? String.valueOf("0" + second) : second));
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.otc_quotation_close_ib) { // 关闭按钮
            finish();
        } else if (id == R.id.otc_quotation_confirm_btn) {
            if (mCurrentTotalPrice <= 0) {
                // 总价不能为0提示
                ToastUtil.show(getResources().getString(R.string.otc_total_price_can_not_less_than_zero));
                return;
            }
            OTCDeleteCurrPayPopWindow payPopWindow = new OTCDeleteCurrPayPopWindow(mCtx)
                    .setTitle(getResources().getString(confirmText))
                    //.setConfirmContent(getResources().getString(R.string.str_otc_confirm))
                    .setClickListener(type -> {
                        if (type == 1) {
                            OTCQuotePriceReqParams reqParams = new OTCQuotePriceReqParams();
                            reqParams.setQuotePrice(mCurrentTotalPrice);
                            reqParams.setOrderId(intentBean.getOrderID());
                            mOTCQuotePriceServices.quotePrice(reqParams)
                                    .compose(bindToLifecycle())
                                    .subscribeWith(new RxProgressSubscriber<OTCQuotePriceReqParams>(OTCQuotationActivity.this) {
                                        @Override
                                        public void onSuccess(OTCQuotePriceReqParams reqParams) {
                                            mQuoteId = Integer.parseInt(reqParams.getOfferOrderId());
                                            mQuotationTypeTv.setText(mSuccessType);
                                            mAlreadyQuoteFl.setVisibility(View.VISIBLE);
                                            mNoQuoteRl.setVisibility(View.GONE);
                                            mConfirmBtn.setVisibility(View.GONE);
                                            //setAlreadyQuotePriceUI();
                                            // 回传状态回去
                                            EventBus.getDefault().post(new OTCQuoteOrderPriceEventBean(intentBean.getOrderID(), Utils.div(mCurrentTotalPrice, intentBean.getNum())));
//                                            EventBus.getDefault().post(new OTCQuoteOrderPriceEventBean(intentBean.getOrderID()
//                                                    , getDoubleValue(mTotalPriceTv)));
                                        }

                                        @Override
                                        protected void onError(ResponseThrowable ex) {
                                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                        }
                                    });
                        }
                    });
            payPopWindow.showPopupWindow();
        }
    }

    @Override
    protected boolean isCheckVersion() {
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
