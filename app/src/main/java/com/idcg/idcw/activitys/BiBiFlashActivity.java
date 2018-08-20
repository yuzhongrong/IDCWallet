package com.idcg.idcw.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.ExchangeInProviderServices;
import com.idcg.idcw.iprovider.GetExchangeBalanceProviderServices;
import com.idcg.idcw.model.bean.CoinBean;
import com.idcg.idcw.model.bean.CoinExchangeRateBean;
import com.idcg.idcw.model.bean.CoinPairBean;
import com.idcg.idcw.model.bean.ExchangeResultBean;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.logic.BiBiFlashLogic;
import com.idcg.idcw.model.params.CoinRateReqParam;
import com.idcg.idcw.model.params.ExchangeInParam;
import com.idcg.idcw.presenter.impl.BiBiFlashPresenterImpl;
import com.idcg.idcw.presenter.interfaces.BiBiFlashContract;
import com.idcg.idcw.utils.UIUtils;
import com.idcg.idcw.utils.Utils;
import com.idcg.idcw.widget.EditTextJudgeNumberWatcher;
import com.idcg.idcw.widget.dialog.BiBiHelpPopWindow;
import com.idcg.idcw.widget.dialog.ChooseCoinTypePopWindow;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.widget.ClearableEditText;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

@Route(path = ArouterConstants.BiBiFlash, name = "币币 闪兑界面")
public class BiBiFlashActivity extends BaseWalletActivity<BiBiFlashLogic, BiBiFlashPresenterImpl> implements
        BiBiFlashContract.View {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.img_code)
    ImageView imgCode;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
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
    @BindView(R.id.content_container)
    LinearLayout contentContainer;
    @BindView(R.id.tv_left_2)
    TextView mTvLeft2;
    @BindView(R.id.tv_right_2)
    TextView mTvRight2;
    @BindView(R.id.layout_error)
    LinearLayout mLayoutError;

    private static final int debounceTime = 0;
    @BindView(R.id.error_btn_retry)
    Button mErrorBtnRetry;

    @Autowired
    GetExchangeBalanceProviderServices mGetWalletBalanceProviderServices;
    @Autowired
    String pin;
    @BindView(R.id.iv_left_arrow)
    ImageView mIvLeftArrow;
    @BindView(R.id.iv_right_arrow)
    ImageView mIvRightArrow;

    /**
     * 可兑出余额
     */
    private double availableBalance = 0;
    /**
     * 最小可兑出
     */
    private double minExchange = 1;
    /**
     * 最大可兑出
     */
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

    /**
     * 币种列表
     */
    private List<CoinBean> mCoins = new ArrayList<>();
    /**
     * 两种币种之间的汇率实体类
     */
    private CoinExchangeRateBean mRate;
    private CoinBean mLeftCoin;
    private CoinBean mRightCoin;
    private CoinBean mBtcCoin;

    private List<CoinBean> atLeastOneBeans = new ArrayList<>();
    private EditTextJudgeNumberWatcher mOutEtDigistController;
    private EditTextJudgeNumberWatcher mInEtDigistController;
    private ExchangeInParam mExchangeInParam;
    private Disposable mAutoRefreshRateDisposable;
    /**
     * 存放交易对
     * 一个币可以对应多个币，成为多个交易对
     */
    private HashMap<String, Set<String>> mCoinBeanSetHashMap = new HashMap<>();
    /**
     * 存放所有币实例
     */
    private HashMap<String, CoinBean> mCoinBeanMap = new LinkedHashMap<>();
    /**
     * 存放所有交易对汇率
     */
    private HashMap<String, Double> mCoinPairRateMap = new HashMap<>();
    /**
     * 存放所有交易对汇率位数
     */
    private HashMap<String, Integer> mCoinPairRateDigitMap = new HashMap<>();
    /**
     * 存放交易对位数pair，first为digit
     */
    private HashMap<String, Pair<Integer, Integer>> digitMap = new HashMap<>();
    /**
     * 存放交易对对应的兑出范围，first为最小，second为最大
     */
    private HashMap<String, Pair<Double, Double>> mCanExchangeScope = new HashMap<>();
    /**
     * 存放所有为正方向的交易对
     */
    private Set<String> mDirectionSet = new HashSet<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bibi_flash;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (dialog != null)
            dialog.setBackPressEnable(false);
        ARouter.getInstance().inject(this);
        mWindowManager = getWindowManager();
        imgCode.setImageResource(R.drawable.help);
        imgCode.setVisibility(View.VISIBLE);
        tvSetName.setText(R.string.bibi_exchange);

        refreshEtState();
//        InputFilter[] filters = {new CashierInputFilter()};
//        //限制用户输入
//        etOut.setFilters(filters);
//        etIn.setFilters(filters);
        observeEditText();
        requestCoinList(true);
    }

    private void clearAutoRefreshRateDisposable() {
        if (mAutoRefreshRateDisposable != null && !mAutoRefreshRateDisposable.isDisposed())
            mAutoRefreshRateDisposable.dispose();
    }

    //每20秒刷新费率
    private void autoRefreshRate() {
        clearAutoRefreshRateDisposable();
        mAutoRefreshRateDisposable = Observable.interval(20, 20, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (isDialogShowing() || mLayoutError.getVisibility() == View.VISIBLE) {
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
            contentContainer.setVisibility(View.GONE);
            mLayoutError.setVisibility(View.VISIBLE);
        } else {
            contentContainer.setVisibility(View.VISIBLE);
            mLayoutError.setVisibility(View.GONE);
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
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
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
        addSubscribe(mStopInputDisposable = Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        resetEtOutWithLegal();
                    }
                }));
    }

    /**
     * 清除所有提示颜色
     */
    private void clearHintColor() {
        tvBalance.setSelected(false);
        tvMax.setSelected(false);
        tvMin.setSelected(false);
    }

    /**
     * 通过value判断各个提示的颜色
     *
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
     *
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
     *
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
        mPresenter.requestCoinList(showDialog);
    }

    private void requestBalance() {
        if (mLeftCoin == null)
            return;
//        showDialog();
        clearAutoRefreshRateDisposable();
        requesetType = RequestType.REQ_BALANCE;
        mGetWalletBalanceProviderServices.requestNewBalanceProvider(mLeftCoin.getCoin())
                .retry(2)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith
                        (new RxProgressSubscriber<NewBalanceBean>(this) {
                            @Override
                            protected void onError(ResponseThrowable ex) {
//                        dismissDialog();
                                showError(true);
                            }

                            @Override
                            public void onSuccess(NewBalanceBean newBalanceBean) {
                                showError(false);
                                availableBalance = newBalanceBean.getCurrentBalance();
                                //这里需要处理成4位
                                availableBalance = new BigDecimal(availableBalance).setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
                                tvBalance.setText(Utils.toSubStringDegist(availableBalance, 4) + " " + mLeftCoin
                                        .getUpperCaseCoin());
//                        tvBalance.setText(String.format(getString(R.string.exchange_available_balance), Utils
//                                .toNoPointSubStringDegistIfDegistIsZero(availableBalance, digest), mLeftCoin
//                                .getUpperCaseCoin()));
//                        requestUpdateRate(false);
                                autoRefreshRate();
                            }
                        });
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @OnClick({R.id.mr_back_layout, R.id.img_code, R.id.rl_left, R.id.btn_swap, R.id.rl_right, R.id.btn_exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                finish();
                break;
            case R.id.img_code:
                /*String lang;
                if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
                    lang="0";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                    lang="1";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fi")) {
                    lang="2";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("ja")) {
                    lang="3";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("ko")) {
                    lang="4";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("fr")) {
                    lang="5";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("nl")) {
                    lang="6";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("es")) {
                    lang="7";
                } else if (getResources().getConfiguration().locale.getLanguage().equals("vi")) {
                    lang="8";
                } else {
                    String locale = Locale.getDefault().getLanguage();
                    if ("zh".equals(locale)) {
                        lang="1";
                    } else {
                        lang="0";
                    }
                }
                BIBIHelpReqParam param = new BIBIHelpReqParam();
                param.setLanguage(lang);
                requesetType = RequestType.REQ_OTHER;
                showDialog();
                mPresenter.requestHelpString(param);*/
                BiBiHelpPopWindow biBiHelpPopWindow = new BiBiHelpPopWindow(mCtx);
                biBiHelpPopWindow.showWithText
                        (getString(R.string.bibi_help_title), getString(R.string.bibi_help_content));
                break;
            case R.id.btn_swap:
                Animation circle_anim = AnimationUtils.loadAnimation(mCtx, R.anim.tip_img);
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
        if (mLeftCoin == null || mRightCoin == null)
            return;
        Double value = getDoubleValue(etOut);
        if (value.compareTo(availableBalance) > 0 || availableBalance < minExchange) {
            ToastUtil.show(getString(R.string.exchange_more_then_max_hint));
            return;
        }
        //最大兑出为0，或者最小兑出为0，并且输入的值是0
        if (maxExchange == 0 || getDoubleValue(etIn) == 0 && minExchange == 0) {
            ToastUtil.show(getString(R.string.not_support_pair));
            return;
        }
        if (value.compareTo(minExchange) < 0 || value.compareTo(maxExchange) > 0 || getDoubleValue(etIn) == 0) {
            ToastUtil.show(getString(R.string.exchange_less_then_min_hint));
            return;
        }
        /*showDialog();
        Observable.just(shotActivity(BiBiFlashActivity.this))
                .doOnNext(bitmap -> {
                    //保存pin页面需要的高斯模糊背景
                    ACacheUtil.get(mCtx).put("bibiflashbg", EasyBlur.with(mCtx)
                            .bitmap(bitmap) //要模糊的图片
                            .policy(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? EasyBlur
                                    .BlurPolicy.RS_BLUR : EasyBlur.BlurPolicy.FAST_BLUR)
                            .radius(25)//模糊半径
                            .blur());
                    //保存需要交易完成界面的币种logo图
                    ACacheUtil.get(mCtx).put("exchange_logo", mLeftCoin.getLogo());
                    ACacheUtil.get(mCtx).put("exchange_to_logo", mRightCoin.getLogo());
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    dismissDialog();
                    ARouter.getInstance()
                            .build(ArouterConstants.CheckFlashPin)
                            .withParcelable("mExchangeInParam", new ExchangeInParam(mRightCoin.getCoin(),
                                    getDoubleValue(etIn), mRate.getExchangeRate(),
                                    null, null,
                                    getDoubleValue(etOut), null, mLeftCoin.getCoin(), "", LoginUtils.getLoginBean(mCtx).getDevice_id(),mRate.isDirection()))
                            .withParcelable("mRate", mRate)
                            .navigation();
                    finish();
                });*/
        ACacheUtil.get(mCtx).put("exchange_logo", mLeftCoin.getLogo());
        ACacheUtil.get(mCtx).put("exchange_to_logo", mRightCoin.getLogo());

        mExchangeInParam = new ExchangeInParam(mRightCoin.getCoin(),
                getDoubleValue(etIn), getRate(),
                null, null,
                getDoubleValue(etOut), null, mLeftCoin.getCoin(), "", LoginUtils.getLoginBean(mCtx).getDevice_id(),
                getDirection(), digest, toDigest, getRateDigit());
        onCheckPinSuccess(pin);
    }

    private void onCheckPinSuccess(String pinPass) {
        if (mExchangeInParam == null)
            return;
        mExchangeInParam.setPayPassword(pinPass);
        btnExchange.setEnabled(false);
        requesetType = RequestType.REQ_OTHER;
        mExchangeInProviderServices.exchangeIn(mExchangeInParam)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<ExchangeResultBean>(this) {
                    @Override
                    public void onSuccess(ExchangeResultBean data) {
                        btnExchange.setEnabled(true);
                        int statusCode = data.getStatusCode();
                        if (statusCode == 0) {
                            exchangeSuccess(data.getTxId());
                        } else if (3 == statusCode
                                || 102 == statusCode
                                || 107 == statusCode
                                || 108 == statusCode
                                || 106 == statusCode) {
                            ToastUtil.show(getString(R.string.balance_enough));
                        } else if (1 == statusCode) {
                            ToastUtil.show(getString(R.string.eth_not_enough));
                        } else if (9 == statusCode) {
                            String minAmount;
                            if (mLeftCoin.getCoin().equalsIgnoreCase("LTC")) {
                                minAmount = "0.0006";
                                ToastUtil.show(getString(R.string.most_amount_enable));
                            } else {
                                minAmount = "0.00006";
                                ToastUtil.show(getString(R.string.most_amount_more_enable));
                            }
                        } else if (800 == statusCode) {
                            ToastUtil.show(getString(R.string.tv_pedding_status));
                        } else {
//                    ToastUtil.show(getString(R.string.server_connection_error));
                            ToastUtil.show(getString(R.string.bibi_exchange_failed));
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        btnExchange.setEnabled(true);
                        if (ex.getErrorCode().equals("121")) {
                            UIUtils.post(() -> {
                                ToastUtil.show(ex.getErrorMsg());
                                onBackPressed();
                            });
                        } else {
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
        finish();
    }

    private void showChooseCoinPopWin(View view) {
        if (mCoinBeanSetHashMap.size() == 0)
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
    private static final String VHDC = "vhdc";

    /**
     * 展示选择的框，
     *
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
        ChooseCoinTypePopWindow chooseCoinTypePopWindow = new ChooseCoinTypePopWindow(mCtx);
        chooseCoinTypePopWindow.showPopWindowForData(Observable.fromIterable(isLeft ? mCoinBeanMap.keySet() : mCoinBeanSetHashMap.get(anotherBean.getCoin()))
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
            } else if (!mCoinBeanSetHashMap.get(anotherBean.getCoin()).contains(bean.getCoin())) {
                //如果点击左边的，不与右边的形成交易对，则把右边替换成btc
                updatePart(mBtcCoin, !isLeft);
            }
            if (isLeft || bean == anotherBean) {
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
     *
     * @param nowBean
     * @param left    是不是更新左边
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
            GlideUtil.loadImageView(mCtx, logo, imgLeft);
        } else {
            mRightCoin = nowBean;
            tvRight.setText(currency);
//            mTvRight2.setText(currency);
            GlideUtil.loadImageView(mCtx, logo, imgRight);
        }
    }

    private void refreshUi() {
        if (mLeftCoin == null || mRightCoin == null)
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
        if (minStr.contains(".")) {
            String strAftreDot = minStr.substring(minStr.indexOf(".") + 1);
            if (strAftreDot.length() > digest) {
                minExchange = new BigDecimal(minExchange).setScale(digest, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        String maxStr = String.valueOf(maxExchange);
        if (maxStr.contains(".")) {
            String strAftreDot = maxStr.substring(maxStr.indexOf(".") + 1);
            if (strAftreDot.length() > toDigest) {
                maxExchange = new BigDecimal(maxExchange).setScale(digest, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        /*此处修复后台返回精度与实际最小值的精度不等*/


        tvMin.setText(Utils.toNoPointSubStringDegistIfDegistIsZero(minExchange, digest) + " " + mLeftCoin.getUpperCaseCoin());
        tvMax.setText(Utils.toNoPointSubStringDegistIfDegistIsZero(maxExchange, digest) + " " + mLeftCoin.getUpperCaseCoin());
//            tvMin.setText(String.format(getString(R.string.exchange_min_with_value), Utils.toNoPointSubStringDegistIfDegistIsZero(minExchange, digest), mLeftCoin.getUpperCaseCoin()));
//            tvMax.setText(String.format(getString(R.string.exchange_max_with_value), Utils.toNoPointSubStringDegistIfDegistIsZero(maxExchange, digest), mLeftCoin.getUpperCaseCoin()));

        mOutEtDigistController.setPointerLength(digest);

        mInEtDigistController.setPointerLength(toDigest);

        if (etIn.isFocused()) {
            if (TextUtils.isEmpty(etIn.getText())) {
                return;
            }
            if (checkAndResetInAboutWithDigest()) {
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
                if (checkAndResetOutAboutWithDigest()) {
                    return;
                }
                updateIn(getDoubleValue(etOut));
                updateHintColor(getDoubleValue(etOut));
            } else {
                clearHintColor();
            }
        }
    }

    private void requestUpdateRate(boolean isBackground) {
        if (mLeftCoin == null || mRightCoin == null) {
            //初始化所有的提示
            return;
        }
//        if (!isBackground)
//            showDialog();
        requesetType = RequestType.REQ_RATE;
        CoinRateReqParam param = new CoinRateReqParam();
        param.setFrom(mLeftCoin.getCoin());
        param.setTo(mRightCoin.getCoin());
        mPresenter.requestCoinRate(param, isBackground);
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
        int leftOffset = rightArrowLoc[0] - leftArrowLoc[0];
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
        mLeftCacheBitmap = Bitmap.createBitmap(mLeftCacheBitmap, 0, 0, mIvLeftArrow.getWidth() + mIvLeftArrow.getLeft(), mLeftCacheBitmap.getHeight());
        //获取右边tv的缓存bitmap
        rlRight.setDrawingCacheEnabled(true);
        mRightCacheBitmap = Bitmap.createBitmap(rlRight.getDrawingCache());
        rlRight.destroyDrawingCache();
        //截取从右边imgRight控件开始往右边的bitmap
        mRightCacheBitmap = Bitmap.createBitmap(mRightCacheBitmap, imgRight.getLeft(), 0, mRightCacheBitmap.getWidth() - imgRight.getLeft(), mRightCacheBitmap.getHeight());

        //创建出两个镜像view
        copyViewLeft = createCopyView(mLeftLocation[0], mLeftLocation[1], mLeftCacheBitmap);
        copyViewRight = createCopyView(mRightLocation[0] + imgRight.getLeft(), mRightLocation[1], mRightCacheBitmap);
        //释放bitmap资源...这我不确定是不是这么做
        mLeftCacheBitmap = null;
        mRightCacheBitmap = null;
    }

    /**
     * 左侧镜像view的动画
     *
     * @param offset 偏移量
     * @param defX   原始位置的x
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
     *
     * @param offset 偏移量
     * @param defX   原始位置的x
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
        mLayoutParams.y = y - getStatusHeight(this);
        mLayoutParams.alpha = 1f;                                //设置透明度
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        ImageView copyView = new ImageView(this);
        copyView = new ImageView(this);
        copyView.setImageBitmap(bitmap);
        mWindowManager.addView(copyView, mLayoutParams);   //添加该iamgeView到window
        return copyView;
    }

    /**
     * 获取状态栏的高度
     *
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

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
//        dismissDialog();

        if (requesetType == RequestType.REQ_RATE && "608".equals(throwable.getErrorCode())) {
            //该状态表示不支持该交易对
            mRate = null;
            minExchange = 0;
            maxExchange = 0;
            refreshDataAboutRate();
            setEditTextEnable(etOut, false);
            setEditTextEnable(etIn, false);
            ToastUtil.show(getString(R.string.not_support_pair));
        } else if ("611".equals(throwable.getErrorCode())) {
            ToastUtil.show(getString(R.string.eth_not_enough));
        } else {
            ToastUtil.show(getString(R.string.server_connection_error));
            showError(true);
        }
    }

    @Override
    public void updateCoinList(List<CoinPairBean> coinBeans) {
/*//        dismissDialog();
        showError(false);
        *//*atLeastOneBeans.clear();
        mBtcCoin = Observable.fromIterable(mCoins)
                .filter(bean1 -> BTC.equalsIgnoreCase(bean1.getUpperCaseCoin()))
                .blockingFirst();
        updatePart(mBtcCoin, true);
        atLeastOneBeans.add(mBtcCoin);
        try {
            //如果没返回该币会报错
            OTCCoinBean vhdcCoin = Observable.fromIterable(mCoins)
                    .filter(bean1 -> VHDC.equalsIgnoreCase(bean1.getUpperCaseCoin()))
                    .blockingFirst();
            atLeastOneBeans.add(vhdcCoin);
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        mCoins.clear();
        atLeastOneBeans.clear();
        Observable.fromIterable(coinBeans)
                .filter(OTCCoinBean::isIsSupportExchange)
                .doOnNext(bean -> {
                    mCoins.add(bean);
                    if (bean.getDefault() == 1) {
                        updatePart(bean, true);
                    } else if (bean.getDefault() == 2) {
                        updatePart(bean, false);
                    }
                    if (BTC.equalsIgnoreCase(bean.getUpperCaseCoin())) {
                        mBtcCoin = bean;
                    }
//                        //TODO 这个是暂时的，需要替换成判断是否市场币的方法
//                        if(bean.getUpperCaseCoin().equalsIgnoreCase(BTC)){
//                            atLeastOneBeans.add(bean);
//                        }
                    if (bean.isMarket()) {
                        atLeastOneBeans.add(bean);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        requestBalance();
                        refreshEtState();
                    }
                })
                .subscribe();*/
        showError(false);
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

                        String toCoin = coinPairBean.getToCoin();
                        int toDigit = coinPairBean.getToDigit();
                        double toExchangeMax = coinPairBean.getToExchangeMax();
                        double toExchangeMin = coinPairBean.getToExchangeMin();
                        String toLogo = coinPairBean.getToLogo();
                        boolean toIsMarket = coinPairBean.isToIsMarket();
                        boolean toIsSupportExchange = coinPairBean.isToIsSupportExchange();

                        CoinBean toCoinBean = new CoinBean(toCoin, toIsSupportExchange, toLogo, toIsMarket,
                                toExchangeMin, toExchangeMax, toDigit);

                        if (mCoinBeanSetHashMap.get(fromCoin) == null) {
                            mCoinBeanSetHashMap.put(fromCoin, new LinkedHashSet<>());
                        }
                        Set<String> fromSet = mCoinBeanSetHashMap.get(fromCoin);
                        fromSet.add(toCoinBean.getCoin());
                        if (mCoinBeanSetHashMap.get(toCoin) == null) {
                            mCoinBeanSetHashMap.put(toCoin, new LinkedHashSet<>());
                        }
                        Set<String> toSet = mCoinBeanSetHashMap.get(toCoin);
                        toSet.add(fromCoinBean.getCoin());

                        if(!mCoinBeanMap.containsKey(fromCoin))
                            mCoinBeanMap.put(fromCoin, fromCoinBean);
                        if(!mCoinBeanMap.containsKey(toCoin))
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

                        if (BTC.equalsIgnoreCase(fromCoin)) {
                            mBtcCoin = fromCoinBean;
                        } else if (BTC.equalsIgnoreCase(toCoin)) {
                            mBtcCoin = toCoinBean;
                        }

                        if (mLeftCoin != null && mRightCoin != null) {
                            refreshUi();
                        } else {
                            if (coinPairBean.isDefault()) {
                                if ("BTC".equalsIgnoreCase(toCoin)) {
                                    //如果右边是btc，则切换位置
                                    updatePart(toCoinBean, true);
                                    updatePart(fromCoinBean, false);
                                } else {
                                    updatePart(fromCoinBean, true);
                                    updatePart(toCoinBean, false);
                                }
                                requestBalance();
                            }
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //这里如果是没有返回default就会取第一对键值对，如果还没。。。那就都是kong
                        if(mLeftCoin==null || mRightCoin==null){
                            for (String coin  : mCoinBeanSetHashMap.keySet()) {
                                Set<String> strings = mCoinBeanSetHashMap.get(coin);
                                for (String toCoin : strings) {
                                    updatePart(mCoinBeanMap.get(coin), true);
                                    updatePart(mCoinBeanMap.get(toCoin), false);
                                	break;
                                }
                            }
                            requestBalance();
                        }
                        refreshEtState();
                        refreshUi();
                    }
                })
                .subscribe();
    }

    private void putScope(CoinBean fromCoin, CoinBean toCoin, double minExchange, double maxExchange) {
        mCanExchangeScope.put(fromCoin.getCoin() + toCoin.getCoin(), new Pair<>(minExchange, maxExchange));
    }

    private Pair<Double, Double> getScope(CoinBean fromCoin, CoinBean toCoin) {
        return mCanExchangeScope.get(fromCoin.getCoin() + toCoin.getCoin());
    }

    private void putDigit(CoinBean fromCoin, CoinBean toCoin, int digit, int toDigit) {
        digitMap.put(fromCoin.getCoin() + toCoin.getCoin(), new Pair<>(digit, toDigit));
    }

    private Pair<Integer, Integer> getDigit(CoinBean fromCoin, CoinBean toCoin) {
        return digitMap.get(fromCoin.getCoin() + toCoin.getCoin());
    }

    private void putDirection(CoinBean fromCoin, CoinBean toCoin) {
        mDirectionSet.add(fromCoin.getCoin() + toCoin.getCoin());
    }

    private boolean getDirection() {
        return mDirectionSet.contains(mLeftCoin.getCoin() + mRightCoin.getCoin());
    }

    private void putRate(CoinBean fromCoin, CoinBean toCoin, double rate) {
        mCoinPairRateMap.put(fromCoin.getCoin() + toCoin.getCoin(), rate);
    }

    private double getRate(CoinBean fromCoin, CoinBean toCoin) {
        return mCoinPairRateMap.get(fromCoin.getCoin() + toCoin.getCoin());
    }

    private void putRateDigit(CoinBean fromCoin, CoinBean toCoin, int digit) {
        mCoinPairRateDigitMap.put(fromCoin.getCoin() + toCoin.getCoin(), digit);
    }

    private int getRateDigit() {
        return getRateDigit(mLeftCoin, mRightCoin);
    }

    private int getRateDigit(CoinBean fromCoin, CoinBean toCoin) {
        return mCoinPairRateDigitMap.get(fromCoin.getCoin() + toCoin.getCoin());
    }

    @Override
    public void updateRate(CoinExchangeRateBean coinExchangeRateBean) {
//        dismissDialog();
        showError(false);
        mRate = coinExchangeRateBean;
        autoRefreshRate();
        refreshDataAboutRate();
    }

    @Override
    public void updateHelpString(String helpString) {
//        dismissDialog();
//        BiBiHelpPopWindow biBiHelpPopWindow = new BiBiHelpPopWindow(mCtx);
//        biBiHelpPopWindow.showWithText(helpString);
    }

    private void refreshDataAboutRate() {
        if (mRate == null) {
            tvRate.setText("");
            tvMin.setText(R.string.exchange_min);
            tvMax.setText(R.string.exchange_max);
            digest = DEFAULT_DIGEST;
            toDigest = DEFAULT_DIGEST;
            rateDigest = DEFAULT_DIGEST;
            mOutEtDigistController.setPointerLength(Integer.MAX_VALUE);
            mInEtDigistController.setPointerLength(Integer.MAX_VALUE);
            clearHintColor();
        } else {
            try {
                digest = Integer.parseInt(mRate.getDigit());
            } catch (NumberFormatException e) {
                //限制位数，如果为0则设置为无穷大
                digest = DEFAULT_DIGEST;
                e.printStackTrace();
            }
            toDigest = mRate.getToDigit();
            rateDigest = mRate.getRateDigit();
//            <b>1</b> %s = <b>%s</b> %s
            tvRate.setText(Html.fromHtml(String.format("1</b> %s = <b>%s</b> %s", tvLeft.getText(), Utils
                    .toNoPointSubStringDegistIfDegistIsZero(mRate.getRightExchangeRate(), rateDigest), tvRight.getText())));

            /*此处修复后台返回精度与实际最小值的精度不等*/
            String minStr = String.valueOf(mRate.getExchangeMin());
            if (minStr.contains(".")) {
                String strAftreDot = minStr.substring(minStr.indexOf(".") + 1);
                if (strAftreDot.length() > digest) {
                    mRate.setExchangeMin(new BigDecimal(mRate.getExchangeMin()).setScale(digest, BigDecimal.ROUND_UP).doubleValue());
                }
            }
            /*此处修复后台返回精度与实际最小值的精度不等*/

            tvMin.setText(String.format(getString(R.string.exchange_min_with_value), Utils.toNoPointSubStringDegistIfDegistIsZero(mRate
                    .getExchangeMin(), digest), mLeftCoin.getUpperCaseCoin()));
            tvMax.setText(String.format(getString(R.string.exchange_max_with_value), Utils.toNoPointSubStringDegistIfDegistIsZero(mRate
                    .getExchangeMax(), digest), mLeftCoin.getUpperCaseCoin()));
            tvBalance.setText(String.format(getString(R.string.exchange_available_balance), Utils
                    .toNoPointSubStringDegistIfDegistIsZero(availableBalance, digest), mLeftCoin
                    .getUpperCaseCoin()));

            mOutEtDigistController.setPointerLength(digest);
            mInEtDigistController.setPointerLength(toDigest);

            maxExchange = mRate.getExchangeMax();
            minExchange = mRate.getExchangeMin();

            if (etIn.isFocused()) {
                if (TextUtils.isEmpty(etIn.getText())) {
                    return;
                }
                if (checkAndResetInAboutWithDigest()) {
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
                    if (checkAndResetOutAboutWithDigest()) {
                        return;
                    }
                    updateIn(getDoubleValue(etOut));
                    updateHintColor(getDoubleValue(etOut));
                } else {
                    clearHintColor();
                }
            }

        }

    }

    /**
     * 检测刷新费率的时候out输入框小数点后的长度是不是比限制的长，如果是截取掉，并返回ture
     *
     * @return
     */
    private boolean checkAndResetOutAboutWithDigest() {
        String s = etOut.getText().toString();
        int i = s.indexOf(".");
        if (i != -1) {
            String substring = s.substring(i + 1);
            if (substring.length() > digest) {
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
     *
     * @return
     */
    private boolean checkAndResetInAboutWithDigest() {
        String s = etIn.getText().toString();
        int i = s.indexOf(".");
        if (i != -1) {
            String substring = s.substring(i + 1);
            if (substring.length() > toDigest) {
                int needCut = substring.length() - toDigest;
                String result = s.substring(0, s.length() - needCut);
                etIn.setText(result);
                etIn.setSelection(result.length());
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.error_btn_retry)
    public void onViewClicked() {
        if (requesetType == RequestType.REQ_COIN_LIST) {
            requestCoinList(true);
        } else if (requesetType == RequestType.REQ_RATE) {
            requestUpdateRate(false);
        } else if (requesetType == RequestType.REQ_BALANCE) {
            requestBalance();
        }
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
