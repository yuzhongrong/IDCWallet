package foxidcw.android.idcw.otc.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.DecimalDigitsInputFilter;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.SpUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.api.NetWorkApi;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;


import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCGetOtcSettingServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetOtcTradeSettingServices;
import foxidcw.android.idcw.otc.iprovider.OTCSendOrderServices;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcSettingBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcTradeSettingBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import foxidcw.android.idcw.otc.model.beans.OTCSendOrderDTOBean;
import foxidcw.android.idcw.otc.model.beans.OTCWalletAssetBean;
import foxidcw.android.idcw.otc.model.params.OTCSendOrderParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCOneBtnPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectCoinTypePopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectPaymentMethodPopup;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTitleContent2BtnPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTwoBtnTitlePopWindow;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Predicate;

import static foxidcw.android.idcw.otc.constant.OTCConstant.PAY_METHOD_ADD_OR_EDIT;

/**
 * @author :       MayerXu10000@gamil.com
 * @project name : FoxIDCW1.0
 * @class name :   OTCBuyFragment
 * @package name : foxidcw.android.idcw.otc.fragments
 * @date :         2018/5/2 16:50
 * @describe :     OTC买入
 */
public class OTCBuyFragment
        extends BaseWalletFragment
        implements View.OnClickListener
{

    private TextView mTextViewvBuyRule;
    private RelativeLayout mRlChoiceBuyCoin;
    private RelativeLayout mRlChoiceSellCoin;
    private List<OTCCoinListBean> coinBeanList = new ArrayList<>();//虚拟币的数组
    private List<OTCCoinListBean> currentBeanList = new ArrayList<>(); //法币的数组
    private List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> mAllPayTypeLists = new ArrayList<>();    // 法币对应的支付方式
    private OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse mCurrentPaymentMethod; // 当前选中的支付方式
    private OTCCoinListBean mOTCCoinListBean = null; //币种选中的item
    private OTCCoinListBean mCurrencyListBean = null; //法币选中的item

    private final String OTCCONSOLE_CONSTANT = "OrderID";
    private final String OTCCONSOLE_SYMBOL = "symbol";
    private final String OTCCONSOLE_AMOUNT = "amount";
    private final String OTCCONSOLE_SECONDS = "seconds";
    private final String OTCCONSOLE_COIN = "coin";
    private final String OTCCONSOLE_TYPE = "Is_Buy";
    private final int ADD_OR_EDIT_REQUEST_CODE = 104;
    private static final String ISCHECK = "ISCHECK_BUY";

    @Autowired
    OTCSendOrderServices          mOTCSendOrderServices;
    @Autowired
    OTCGetOtcSettingServices      mOTCGetOtcSettingServices;
    @Autowired
    OTCGetOtcTradeSettingServices mGetOtcTradeSettingServices;//获取交易配置信息
    /**
     * 存放所有币实例
     */
    private TextView mTextvtvBuyOrder;
    private boolean mIsSetPayWay = false;
    private static boolean mIsCheck = false; //是否已点击过已读按钮
    private ImageView mImgBuyCoin;
    private TextView mTvBuyCoinYype;
    private EditText mEtCoinNum;
    private TextView mTvCoinSymbol;

    private int mCoinId = -1; //选中的币种ID
    private int mCurrencyId; //选中的法币ID
    private int mPaymentModeId; //选中的付款方式ID
    private String mMinBuyQuantity; //最小买入数量
    private String mMaxBuyQuantity; //最大买入数量
    private int mDigit = 4; //虚拟币精度
    private TextView mTvBuyPayCoinType;
    private ImageView mImgBuyPayCoin;
    private ImageView mImgBuyPayWay;
    private RelativeLayout mRlPayWay;
    private RelativeLayout mRlInNum;
    private TextView mTvBuyPayCoinWay;
    private CheckBox mCbxBuy;
    private TextView mTvAgreeBuy;
    private boolean mIsForbidTrade; //是否被禁止交易
    private long mForbidExpireTimestamp; //禁止交易到期时间 1527678813000
    private long mForbidExpiredSeconds; //禁止交易到期秒数 1527678813000
    private long debounceTime = 1000; //输入框监听延时
    private int mAllowQuotePriceDuration = 0; //允许接受报价时长，单位秒 ,
    private List<OTCWalletAssetBean> mOTCWalletAssetBeans = new ArrayList<>(); //用户的资产信息
    private String mCoin; //币种符号
    private OTCTwoBtnTitlePopWindow mOTCTwoBtnTitlePopWindow;

    private final int REQUESTCOIN  = 1;    //弹出虚拟币
    private final int REQUESTCURRENT  = 2; //弹出法币
    private final int REQUESTPAY  = 3;     //弹出付款方式
    private OTCSelectCoinTypePopWindow mOTCSelectCoinTypePopWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_otc_buy;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance()
                .inject(this);
        dialog.setBackPressEnable(false);

        mTextViewvBuyRule = rootView.findViewById(R.id.tv_buy_rule);
        mRlChoiceBuyCoin = rootView.findViewById(R.id.rl_choice_buy_coin);
        mRlChoiceSellCoin = rootView.findViewById(R.id.rl_choice_sell_coin);
        mImgBuyCoin = rootView.findViewById(R.id.img_buy_coin);                    //币种图
        mTvBuyCoinYype = rootView.findViewById(R.id.tv_buy_coin_type);             //币种名字
        mEtCoinNum = rootView.findViewById(R.id.et_coin_num);                      //买入数量
        mTvCoinSymbol = rootView.findViewById(R.id.tv_coin_symbol);                //买入数量的币种
        mTvBuyPayCoinType = rootView.findViewById(R.id.tv_buy_pay_coin_type);      //选中法币类型
        mImgBuyPayCoin = rootView.findViewById(R.id.img_buy_pay_coin);             //法币图
        mImgBuyPayWay = rootView.findViewById(R.id.img_buy_pay_way);               //法币图
        mRlPayWay = rootView.findViewById(R.id.rl_pay_way);                        //付款方式列表
        mTvBuyPayCoinWay = rootView.findViewById(R.id.tv_buy_pay_coin_way);        //付款方式名字
        mCbxBuy = rootView.findViewById(R.id.cbx_buy);                             //协议按钮
        mTextvtvBuyOrder = rootView.findViewById(R.id.tv_buy_order);               //下买单
        mRlInNum = rootView.findViewById(R.id.rl_in_num);                         //输入栏 rl
        mTvAgreeBuy = rootView.findViewById(R.id.tv_agree_buy);                      //同意协议文本


        combineButtonFive(getActivity(),
                mTvBuyCoinYype,
                mEtCoinNum,
                mTvBuyPayCoinType,
                mTvBuyPayCoinWay,
                mCbxBuy,
                mTextvtvBuyOrder);

        mIsCheck = SpUtils.getBooleanData(ISCHECK, false);
        mCbxBuy.setChecked(mIsCheck);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        mTextViewvBuyRule.setOnClickListener(this);
        mRlChoiceBuyCoin.setOnClickListener(this);
        mRlChoiceSellCoin.setOnClickListener(this);
        mTextvtvBuyOrder.setOnClickListener(this);
        mRlPayWay.setOnClickListener(this);
        mEtCoinNum.setOnClickListener(this);
        mTvAgreeBuy.setOnClickListener(this);

        mEtCoinNum.setFocusable(false);

        //无选择币种就输入数量
        mEtCoinNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    String s = mTvCoinSymbol.getText()
                            .toString()
                            .trim();
                    if (TextUtils.isEmpty(s)) { //没选中买入币种
                        choiceCoinDialog();
                    }
                }
            }
        });

        RxTextView.textChanges(mEtCoinNum)
                .debounce(debounceTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence)
                            throws Exception {
                        if (!TextUtils.isEmpty(charSequence.toString())) {
                            return isMatch(charSequence.toString());
                        } else {
                            return false;

                        }
                    }
                })
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence)
                            throws Exception {
                        String inputNum = charSequence.toString();

//                        if("0".equals(inputNum)){ //过滤0
//                            return;
//                        }

                        if (Double.parseDouble(inputNum) < Double.parseDouble(mMinBuyQuantity)) {
                            if(Double.parseDouble(mMinBuyQuantity) == 0){
                                mEtCoinNum.setText("0");
                                mEtCoinNum.setSelection("0".length());
                            }else{
                                mEtCoinNum.setText(StringUtils.subZeroAndDot(mMinBuyQuantity));
                                mEtCoinNum.setSelection(mEtCoinNum.getText().toString().trim().length());
                            }
                        } else if (Double.parseDouble(inputNum) > Double.parseDouble(mMaxBuyQuantity)) {
                            if(Double.parseDouble(mMaxBuyQuantity) == 0){
                                mEtCoinNum.setText("0");
                                mEtCoinNum.setSelection("0".length());
                            }else{
                                mEtCoinNum.setText(StringUtils.subZeroAndDot(mMaxBuyQuantity));
                                mEtCoinNum.setSelection(mEtCoinNum.getText().toString().trim().length());
                            }
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


    @SuppressLint("CheckResult")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_buy_rule) {
            String url = NetWorkApi.TRANSACTION_RULE_URL;
            String langType = "en";
            switch (AppLanguageUtils.getLanguageLocalCode(mContext)) {
                case "0":
                    langType = "en";
                    break;
                case "1":
                    langType = "zh-CN";
                    break;
                case "2":
                    langType = "ja";
                    break;
                case "3":
                    langType = "nl";
                    break;
                case "4":
                    langType = "ko";
                    break;
                case "5":
                    langType = "fr";
                    break;
                case "6":
                    langType = "vi";
                    break;
                case "7":
                    langType = "es";
                    break;
                case "8":
                    langType = "hk";
                    break;
            }
            ARouter.getInstance().build(OTCConstant.RULEWEBVIEW)
                    .withString("url", url + "?lang=" + langType)
                    .withInt("skipTag", 2)
                    .navigation(mContext);
            mIsSetPayWay = true;
        } else if (id == R.id.rl_choice_buy_coin) {//选虚拟币弹框

            getOtcTradeSetting(REQUESTCOIN); //点击选虚拟币就请求最新配置信息

        } else if (id == R.id.rl_choice_sell_coin) { //选法币弹框
            choiceCurrent();
        } else if (id == R.id.rl_pay_way) { //选择付款方式
            if (TextUtils.isEmpty(mTvBuyPayCoinType.getText().toString())) {
                choiceCurrent();
            } else if ((mAllPayTypeLists != null && mAllPayTypeLists.size() <= 0) || mAllPayTypeLists == null || mCurrencyListBean == null) { //没有付款方式
                showNoPayWay();
            } else {//有付款方式
                List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> mCurrentAllPayTypeLists = new ArrayList<>();    // 实际法币对应的支付方式
                mCurrentAllPayTypeLists.addAll(mAllPayTypeLists);

                //判断是否有付款方式
                String localCurrencyCode = mCurrencyListBean.getLocalCurrencyCode(); //法币名字
                if (!"CNY".toUpperCase()
                        .equals(localCurrencyCode.toUpperCase())) { //非CNY有支付宝和微信
                    for (int i = 0; i < mCurrentAllPayTypeLists.size(); i++) {
                        //TODO 写死判断非 alipay 和 cny
                        if ("AliPay".toUpperCase()
                                .equals(mCurrentAllPayTypeLists.get(i)
                                        .getPayTypeCode()
                                        .toUpperCase()) || "CNY".toUpperCase()
                                .equals(mCurrentAllPayTypeLists.get(
                                        i)
                                        .getCurrencyCode()) || !localCurrencyCode.toUpperCase()
                                .equals(mCurrentAllPayTypeLists.get(
                                        i)
                                        .getCurrencyCode())) {
                            mCurrentAllPayTypeLists.remove(i);
                            i--;
                        }
                    }

                    if (mCurrentAllPayTypeLists.size() == 0) {
                        showNoPayWay();
                        return;
                    }

                } else {
                    for (int i = 0; i < mCurrentAllPayTypeLists.size(); i++) {
                        //TODO 写死判断非 alipay 和 cny
                        if (!localCurrencyCode.toUpperCase()
                                .equals(mCurrentAllPayTypeLists.get(i)
                                        .getCurrencyCode())) {
                            mCurrentAllPayTypeLists.remove(i);
                            i--;
                        }
                    }

                    if (mCurrentAllPayTypeLists.size() == 0) {
                        showNoPayWay();
                        return;
                    }
                }

                HashSet<Integer> hashSet = new HashSet<>();
                if (null != mCurrentPaymentMethod) {
                    hashSet.add(mCurrentPaymentMethod.getID());
                }

                //如果付款方式只有一种就默认选中
                if (mCurrentAllPayTypeLists != null && mCurrentAllPayTypeLists.size() == 1) {
                    mCurrentPaymentMethod = mCurrentAllPayTypeLists.get(0);
                    hashSet.add(mCurrentPaymentMethod.getID());
                }

                OTCSelectPaymentMethodPopup otcSelectPaymentMethodPopup = new OTCSelectPaymentMethodPopup(
                        mContext);
                if (otcSelectPaymentMethodPopup.isShowing()) {
                    return;
                }
                otcSelectPaymentMethodPopup.showSelectPaymentWithData(mCurrentAllPayTypeLists,
                        getActivity().getResources()
                                .getString(R.string.str_otc_buy_pay_way_hint),
                        hashSet);
                otcSelectPaymentMethodPopup.setOnPaymentMethodSelectListener(new OTCSelectPaymentMethodPopup.OnPaymentMethodSelectListener() {
                    @Override
                    public void onPaymentMethodSelected(ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> selectItems) {
                        if (selectItems.size() <= 0) {
                            return;
                        }
                        OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse response = selectItems.get(0);
                        GlideUtil.loadImageView(mContext,
                                selectItems.get(0)
                                        .getPayTypeLogo(),
                                mImgBuyPayWay);

                        mCurrentPaymentMethod = selectItems.get(0);
                        mTvBuyPayCoinWay.setHint("");
//                        mTvBuyPayCoinWay.setText(response.getPayTypeCode());
                        mTvBuyPayCoinWay.setText(response.getShowPayTypeCode(mContext));
                        mPaymentModeId = selectItems.get(0)
                                .getID();
                    }
                });
            }

        } else if (id == R.id.et_coin_num) { //输入数量 ->用于判断是否选中了虚拟币
            String s = mTvCoinSymbol.getText()
                    .toString()
                    .trim();
            if (TextUtils.isEmpty(s)) { //没选中买入币种
//                choiceCoinDialog();
                getOtcTradeSetting(REQUESTCOIN); //点击选虚拟币就请求最新配置信息
            } else {
                if (mEtCoinNum.getText().toString().trim().length() > 0) {
                    mEtCoinNum.setSelection(mEtCoinNum.getText().toString().trim().length());
                }
            }
        }else if(id == R.id.rl_in_num){
            String s = mTvCoinSymbol.getText()
                                    .toString()
                                    .trim();
            if (TextUtils.isEmpty(s)) { //没选中买入币种
//                choiceCoinDialog();
                getOtcTradeSetting(REQUESTCOIN); //点击选虚拟币就请求最新配置信息
            } else {
                if (mEtCoinNum.getText().toString().trim().length() > 0) {
                    mEtCoinNum.setSelection(mEtCoinNum.getText().toString().trim().length());
                }
            }
        } else if (id == R.id.tv_buy_order) {
            if(!showBindPhonePop()){
                return;
            }
            Boolean asBoolean = ACacheUtil.get(mContext)
                    .getAsBoolean(AcacheKeys.ISBINDPHONE, false);

            if (!asBoolean) { //没设置手机号
                OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mContext);
                OTCDeleteCurrPayPopWindow.setTitle(getActivity().getString(R.string.str_otc_bind_phone))
                        .setConfirmContent(getActivity().getString(R.string.str_otc_to_bind))
                        .setCancelContent(getActivity().getString(R.string.str_otc_talk_later))
                        .showPopupWindow();
                OTCDeleteCurrPayPopWindow.getSkipSureDelete()
                        .setOnClickListener(v1 -> {
                            ARouter.getInstance()
                                    .build(ArouterConstants.ACTIVITY_PHONE)
                                    .navigation();
                            OTCDeleteCurrPayPopWindow.dismiss();
                        });

                return;
            }

            //购买数量不在限制范围内
            String buyNUm = mEtCoinNum.getText()
                    .toString()
                    .trim();

            if (TextUtils.isEmpty(buyNUm)) {
                return;
            }
            if (Double.parseDouble(buyNUm) < Double.parseDouble(mMinBuyQuantity)) {
                mEtCoinNum.setText(StringUtils.subZeroAndDot(mMinBuyQuantity));
                mEtCoinNum.setSelection(mEtCoinNum.getText().toString().trim().length());
                return;
            } else if (Double.parseDouble(buyNUm) > Double.parseDouble(mMaxBuyQuantity)) {
                mEtCoinNum.setText(StringUtils.subZeroAndDot(mMaxBuyQuantity));
                mEtCoinNum.setSelection(mEtCoinNum.getText().toString().trim().length());
                return;
            }


            //是否被禁止交易
            if (mIsForbidTrade) {
                //计算时间差
//                String distanceTime = TimeUtils.getDistanceTime(mForbidExpireTimestamp, System.currentTimeMillis(), getActivity().getString(R.string.str_otc_no_trading_prompt)); //使用时间戳
                String distanceTime = StringUtils.getTimeLeft(mForbidExpiredSeconds, mContext); //使用秒

                OTCOneBtnPopWindow otcOneBtnPopWindow = new OTCOneBtnPopWindow(mContext);
                otcOneBtnPopWindow.setTitle(distanceTime)
                        .getSkipSureDelete()
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                otcOneBtnPopWindow.dismiss();
                            }
                        });
                otcOneBtnPopWindow.showPopupWindow();
                return;
            }

            //确认交易对话框
            String coinNum = mEtCoinNum.getText()
                    .toString()
                    .trim();
            String coinSymbol = mTvCoinSymbol.getText()
                    .toString()
                    .trim();
            String noteTitle = String.format(getActivity().getString(R.string.str_otc_sure_buy_title),
                    coinNum,
                    coinSymbol);
            @SuppressLint({"StringFormatInvalid",
                    "LocalSuppress"})
            String noteContent = String.format(getActivity().getString(R.string.str_otc_sure_buy_content),
                    (mAllowQuotePriceDuration + ""));

            OTCTitleContent2BtnPopWindow OTCTitleContent2BtnPopWindow = new OTCTitleContent2BtnPopWindow(
                    mContext);
            OTCTitleContent2BtnPopWindow.setTitle(noteTitle)
                    .setContent(noteContent)
                    .getSkipSureDelete()
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            sendOrder(buyNUm,
                                    coinSymbol,
                                    OTCTitleContent2BtnPopWindow);

                        }
                    });

            OTCTitleContent2BtnPopWindow.showPopupWindow();


        }else if(id == R.id.tv_agree_buy){
            if (!mCbxBuy.isChecked()) {
                mCbxBuy.setChecked(true);
            }else {
                mCbxBuy.setChecked(false);
            }
        }
    }

    //选择法币
    private void choiceCurrent() {
        OTCSelectCoinTypePopWindow OTCSelectCoinTypePopWindow = new OTCSelectCoinTypePopWindow(mContext);
        if (coinBeanList.size() > 0) {
            OTCSelectCoinTypePopWindow.setTitle(getActivity().getString(R.string.str_otc_select_fiat_currency));
            OTCSelectCoinTypePopWindow.showPopWindowForData(currentBeanList, 2, mCurrencyListBean);
            OTCSelectCoinTypePopWindow.setOnItemClickListener(((bean, position) -> {
                mTvBuyPayCoinType.setText(bean.getLocalCurrencyCode());
                mTvBuyPayCoinType.setHint("");
                mCurrencyId = bean.getId();                  //选中的币种ID
                GlideUtil.loadImageView(mContext, bean.getLogo(), mImgBuyPayCoin);
                mCurrencyListBean = bean;                    //选中的bean


                List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> mCurrencyWithList = new ArrayList<>();
                for (OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse response : mAllPayTypeLists) {
                    if (bean.getLocalCurrencyCode().equals(response.getCurrencyCode())) {
                        mCurrencyWithList.add(response);
                    }
                }
                if (mCurrencyWithList.size() == 1) {
                    mCurrentPaymentMethod = mCurrencyWithList.get(0);
                    GlideUtil.loadImageView(mContext, mCurrentPaymentMethod.getPayTypeLogo(), mImgBuyPayWay);
                    mTvBuyPayCoinWay.setHint("");
//                    mTvBuyPayCoinWay.setText(mCurrentPaymentMethod.getPayTypeCode());
                    mTvBuyPayCoinWay.setText(mCurrentPaymentMethod.getShowPayTypeCode(mContext));
                    mPaymentModeId = mCurrentPaymentMethod.getID();
                } else {
                    //清空支付方式
                    mImgBuyPayWay.setImageResource(R.color.transparent);

                    mCurrentPaymentMethod = null;
                    mTvBuyPayCoinWay.setHint(getActivity().getString(R.string.str_otc_buy_pay_way_hint));
                    mTvBuyPayCoinWay.setText("");
                    mPaymentModeId = -1;
                }
            }));
        }
    }

    @SuppressLint("CheckResult")
    private void sendOrder(String buyNUm,
                           String coinSymbol,
                           OTCTitleContent2BtnPopWindow OTCTitleContent2BtnPopWindow) {

        showDialog();

        OTCSendOrderParams OTCSendOrderParams = new OTCSendOrderParams();
        OTCSendOrderParams.setCoinId(mCoinId);
        OTCSendOrderParams.setCurrencyId(mCurrencyId);
        OTCSendOrderParams.setPaymentModeId(mPaymentModeId);
        OTCSendOrderParams.setQuoteQuantity(Double.parseDouble(buyNUm));
        OTCSendOrderParams.setTradeDirection(1);
        mOTCSendOrderServices.sendOrder(OTCSendOrderParams)
                .compose(bindToLifecycle())
                .subscribeWith(new RxSubscriber<OTCSendOrderDTOBean>() {
                    @Override
                    public void onSuccess(OTCSendOrderDTOBean OTCSendOrderDTOBean) {
                        //                                     ToastUtil.show("Success");

                        ARouter.getInstance()
                                .build(OTCConstant.CONSOLE)
                                .withString(OTCCONSOLE_CONSTANT,
                                        OTCSendOrderDTOBean.getOrderId() + "")
                                .withString(OTCCONSOLE_SYMBOL, coinSymbol)
                                .withString(OTCCONSOLE_AMOUNT, buyNUm)
                                .withInt(OTCCONSOLE_SECONDS, mAllowQuotePriceDuration)
                                .withString(OTCCONSOLE_COIN, mCurrencyListBean.getLocalCurrencyCode())
                                .withBoolean(OTCCONSOLE_TYPE, true)
                                .navigation();

                        if (OTCTitleContent2BtnPopWindow != null) {
                            OTCTitleContent2BtnPopWindow.dismiss();
                        }
                        dismissDialog();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if (OTCTitleContent2BtnPopWindow != null) {
                            OTCTitleContent2BtnPopWindow.dismiss();
                        }
                        ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                        dismissDialog();
                    }
                });
    }

    //选择虚拟币弹框
    private void choiceCoinDialog() {
        mOTCSelectCoinTypePopWindow = new OTCSelectCoinTypePopWindow(mContext);
        if (mOTCSelectCoinTypePopWindow.isShowing()) {
            mEtCoinNum.clearFocus();
            return;
        }
        if (coinBeanList.size() > 0) {
            mOTCSelectCoinTypePopWindow.showPopWindowForData(coinBeanList, 1, mOTCCoinListBean);
            mOTCSelectCoinTypePopWindow.setTitle(getActivity().getString(R.string.str_otc_select_coin));
            mOTCSelectCoinTypePopWindow.setOnItemClickListener(((bean, position) -> {
                GlideUtil.loadImageView(mContext, bean.getLogo(), mImgBuyCoin);
                mTvBuyCoinYype.setText(bean.getCoinCode().toUpperCase()); //显示的币种名
                mTvBuyCoinYype.setHint("");
                mCoinId = bean.getId();                  //选中的币种ID
                mCoin = bean.getCoinCode()
                        .toUpperCase(); //选中的币种符号
                mTvCoinSymbol.setText(bean.getCoinCode()
                        .toUpperCase());  //选中的币种符号
                mTvCoinSymbol.setVisibility(View.VISIBLE);
                mOTCCoinListBean = bean;                 //选中的bean
                mMaxBuyQuantity = Utils.toNormal(bean.getMaxBuyQuantity()); //最最大数量
//                mDigit = bean.getDigit();  //虚拟币精度

                mEtCoinNum.setText("");
                double minBuyQuantity = "0.0".equals(bean.getMinBuyQuantity()+"") ? 0.0001 : bean.getMinBuyQuantity(); //避免0.0比较不了问题
                String currentMinBuyQuantity = Utils.toNormal(Utils.toSubStringDegistNo(Utils.toNormal( minBuyQuantity > 0.0001 ? bean.getMinBuyQuantity() : 0.0001),
                        bean.getDigit()));
                String currentMaxBuyQuantity = Utils.toNormal(Utils.toSubStringDegistNo(Utils.toNormal(bean.getMaxBuyQuantity()).contains(".") ? Utils.toNormal(bean.getMaxBuyQuantity()) : Utils.toNormal(bean.getMaxBuyQuantity()) + ".0000",
                        bean.getDigit()));
                if(bean.getMaxBuyQuantity() == 0 && bean.getMinBuyQuantity() == 0){ //最小买入和最大买入量都为0 就显示 0
                    mEtCoinNum.setHint("0");
                    mMinBuyQuantity = "0";
                }else{ //hint显示例如：0.0001 - 123124
                    mEtCoinNum.setHint(currentMinBuyQuantity + "-" + StringUtils.subZeroAndDot(currentMaxBuyQuantity));
                    mMinBuyQuantity = Utils.toNormal(bean.getMinBuyQuantity() > 0.0001 ? bean.getMinBuyQuantity() : 0.0001); //最小数量
                }
                mEtCoinNum.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(bean.getDigit())}); //输入数量的精度限制
                //设置可编辑状态
                mEtCoinNum.setFocusableInTouchMode(true);
                mEtCoinNum.setFocusable(true);
                mEtCoinNum.requestFocus();
            }));
        }
    }

    //没有设置支付方式提示
    private void showNoPayWay() {
        OTCDeleteCurrPayPopWindow OTCDeleteCurrPayPopWindow = new OTCDeleteCurrPayPopWindow(mContext);
        if (OTCDeleteCurrPayPopWindow.isShowing()) {
            return;
        }
        OTCDeleteCurrPayPopWindow.getSkipSureDelete()
                .setOnClickListener(v1 -> {
//                    ARouter.getInstance().build(OTCConstant.PAY_METHOD_MANAGER).navigation();
                    mIsSetPayWay = true;
                    ARouter.getInstance().build(PAY_METHOD_ADD_OR_EDIT).navigation(getActivity(), ADD_OR_EDIT_REQUEST_CODE);
                    OTCDeleteCurrPayPopWindow.dismiss();
                });
        OTCDeleteCurrPayPopWindow.setTitle(getActivity().getString(R.string.str_otc_set_payway))
                .setConfirmContent(getActivity().getString(R.string.str_otc_to_set))
                .setCancelContent(getActivity().getString(R.string.str_otc_set_up_later))
                .showPopupWindow();
    }


    /**
     * 四个输入栏和一个复选框都选中才变色
     *
     * @param context
     * @param et1
     * @param et2
     * @param et3
     * @param et4
     * @param cb_user
     * @param btn
     */
    public static void combineButtonFive(Context context,
                                         TextView et1,
                                         EditText et2,
                                         TextView et3,
                                         TextView et4,
                                         CheckBox cb_user,
                                         TextView btn) {

        Observable<CharSequence> Observable1 = RxTextView.textChanges(et1);
        Observable<CharSequence> Observable2 = RxTextView.textChanges(et2);
        Observable<CharSequence> Observable3 = RxTextView.textChanges(et3);
        Observable<CharSequence> Observable4 = RxTextView.textChanges(et4);
        Observable<Boolean> checkBox = RxCompoundButton.checkedChanges(cb_user);

        Observable.combineLatest(Observable1,
                Observable2,
                Observable3,
                Observable4,
                checkBox,
                new Function5<CharSequence, CharSequence, CharSequence, CharSequence, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(CharSequence charSequence1,
                                         CharSequence charSequence2,
                                         CharSequence charSequence3,
                                         CharSequence charSequence4,
                                         Boolean aBoolean)
                            throws Exception {

                        if (!TextUtils.isEmpty(charSequence1.toString()) && !TextUtils.isEmpty(
                                charSequence2.toString()) && !TextUtils.isEmpty(
                                charSequence3.toString()) && !TextUtils.isEmpty(
                                charSequence4.toString()) && aBoolean) {
                            return true;
                        }

                        if (aBoolean) {
                            mIsCheck = true; //点击过一次后都是默认选中
                            SpUtils.setBooleanData(ISCHECK, true);
                        }

                        return false;
                    }

                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            btn.setEnabled(true);
                            btn.setBackground(context.getResources()
                                    .getDrawable(R.drawable.shape_solid_corner_2e406b_4));
                            btn.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            btn.setEnabled(false);
                            btn.setBackground(context.getResources()
                                    .getDrawable(R.drawable.shape_solid_corner_999fa5_4));
                            btn.setTextColor(Color.parseColor("#80ffffff"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //是否是数字开头数字结尾
    private boolean isMatch(String s) {
        String pattern = "[0-9]";
        char c = s.charAt(0);
        char c1 = s.charAt(s.length() - 1);
        return Pattern.matches(pattern, c + "") && Pattern.matches(pattern, c1 + "");
    }

    //获取顶层fragment的配置信息
    @Subscriber
    public void getOtcSetting(OTCGetOtcSettingBean otcGetOtcSettingBean) {
        if (otcGetOtcSettingBean != null) {
            mAllowQuotePriceDuration = otcGetOtcSettingBean.getAllowQuotePriceDuration();
            //            ToastUtil.show(otcGetOtcSettingBean.toString());
            LogUtil.e("getOtcSetting----------------------------" + otcGetOtcSettingBean.toString());
        }
    }

    //分发OTC交易配置信息
//    @Subscriber //原来由OTCRecordFragment EventBus发送过来改为点击 买入币种和显示买入币种前调用 1.点击买入币种，2，没选中买入币种
    public void getOtcTradeSettingInner(OTCGetOtcTradeSettingBean getOtcTradeSettingBean, int requestCode) {

        if (getOtcTradeSettingBean != null) {
            LogUtil.e("======================:" + getOtcTradeSettingBean.toString());

            mIsForbidTrade = getOtcTradeSettingBean.isForbidTrade(); //true为禁止交易
            mForbidExpireTimestamp = getOtcTradeSettingBean.getForbidExpireTimestamp();
            mForbidExpiredSeconds = getOtcTradeSettingBean.getForbidExpiredSeconds();

            //添加虚拟币弹框信息
            if (getOtcTradeSettingBean.getCoinSettings() != null && getOtcTradeSettingBean.getCoinSettings()
                    .size() > 0) {
                List<OTCGetOtcTradeSettingBean.CoinSettingsBean> coinSettings = getOtcTradeSettingBean.getCoinSettings();
                coinBeanList.clear();
                for (int i = 0; i < coinSettings.size(); i++) {
                    OTCGetOtcTradeSettingBean.CoinSettingsBean coinSettingsBean = coinSettings.get(i);
                    OTCCoinListBean otcCoinListBean = new OTCCoinListBean();
                    otcCoinListBean.setId(coinSettingsBean.getCoinId());
                    otcCoinListBean.setCoinCode(coinSettings.get(i)
                            .getCoinCode());
                    otcCoinListBean.setLogo(coinSettings.get(i)
                            .getLogo());
                    otcCoinListBean.setMinBuyQuantity(coinSettingsBean.getMinBuyQuantity());
                    otcCoinListBean.setMaxBuyQuantity(coinSettingsBean.getMaxBuyQuantity());
                    otcCoinListBean.setDigit(coinSettingsBean.getDigit());
                    coinBeanList.add(otcCoinListBean);
                }
            }

            //添加法币信息
            if (getOtcTradeSettingBean.getCurrencies() != null && getOtcTradeSettingBean.getCurrencies()
                    .size() > 0) {
                List<OTCGetOtcTradeSettingBean.CurrenciesBean> currencies = getOtcTradeSettingBean.getCurrencies();

                currentBeanList.clear();
                for (int i = 0; i < currencies.size(); i++) {
                    OTCCoinListBean otcCoinListBean = new OTCCoinListBean();
                    otcCoinListBean.setId(currencies.get(i)
                            .getId());
                    otcCoinListBean.setLocalCurrencyCode(currencies.get(i)
                            .getName());
                    otcCoinListBean.setLogo(currencies.get(i)
                            .getLogo());
                    currentBeanList.add(otcCoinListBean);
                }
            }

            //设置了支付方式
            mAllPayTypeLists.clear();
            if (getOtcTradeSettingBean.getPayments() != null && getOtcTradeSettingBean.getPayments()
                    .size() > 0) {

                for (int i = 0; i < getOtcTradeSettingBean.getPayments()
                        .size(); i++) {
                    OTCGetOtcTradeSettingBean.OtcPaymentModeDTO otcPaymentModeDTO = getOtcTradeSettingBean.getPayments()
                            .get(i);

                    OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse paytypeLinkageResponse = new OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse();
                    if("bankcard".equals(otcPaymentModeDTO.getPayTypeCode().toLowerCase())){//银行卡 四位一空格
                        paytypeLinkageResponse.setAccountNo(StringUtils.formatBankNum(otcPaymentModeDTO.getPayAttributes().getAccountNo()));
                    }else{
                        paytypeLinkageResponse.setAccountNo(otcPaymentModeDTO.getPayAttributes().getAccountNo());
                    }
                    paytypeLinkageResponse.setCurrencyCode(otcPaymentModeDTO.getLocalCurrencyCode());
                    paytypeLinkageResponse.setID(otcPaymentModeDTO.getID());
                    paytypeLinkageResponse.setPayTypeCode(otcPaymentModeDTO.getPayTypeCode());
                    paytypeLinkageResponse.setPayTypeLogo(otcPaymentModeDTO.getPayTypeLogo());

                    mAllPayTypeLists.add(paytypeLinkageResponse);
                }
            }
        }

        switch (requestCode){
            case 1:
                choiceCoinDialog();//请求到结果后才弹出选择虚拟币弹框
                break;
            case 2:
//                choiceCurrent();   //请求到结果后才弹出选择虚拟币弹框
                break;
            case 3:
                break;
            default:
                break;
        }
    }



    //获取OTC交易配置信息
    @Subscriber
    public void reciveOtcTradeSetting(OTCGetOtcTradeSettingBean getOtcTradeSettingBean) {
        /*mGetOtcTradeSettingServices.getOtcTradeSetting()
                                   .retry(2)
                                   .compose(bindToLifecycle())
                                   .subscribe(new RxProgressSubscriber<OTCGetOtcTradeSettingBean>((BaseWalletActivity) getContext()) {
                                       @Override
                                       public void onSuccess(OTCGetOtcTradeSettingBean getOtcTradeSettingBean) {
                                           //                                           OTCGetOtcTradeSettingBean getOtcTradeSettingBean1 = getOtcTradeSettingBean;
                                           if (getOtcTradeSettingBean != null) {
                                               //                                               EventBus.getDefault().post(getOtcTradeSettingBean);
                                               getOtcTradeSettingInner(getOtcTradeSettingBean, 4); //由Eventbus改为直接请求
                                               SpUtils.setIntData("cancel_count",
                                                                  getOtcTradeSettingBean.getCancelCount());
                                           }
                                       }

                                       @Override
                                       protected void onError(ResponseThrowable ex) {
                                           LogUtil.e(ex.ErrorMsg + "OTCRecordFragment=========================" + ex.getErrorCode());
                                           ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                       }
                                   });*/
        if (getOtcTradeSettingBean != null) {
            getOtcTradeSettingInner(getOtcTradeSettingBean, 4); //由Eventbus改为直接请求
            SpUtils.setIntData("cancel_count",
                               getOtcTradeSettingBean.getCancelCount());
        }
    }

    //获取下单配置信息 1,虚拟币 2，法币 3,付款方式
    private void getOtcTradeSetting(int requestCode) {
        if (mOTCSelectCoinTypePopWindow!= null && mOTCSelectCoinTypePopWindow.isShowing()) { //虚拟币弹框防双击
            mEtCoinNum.clearFocus();
            return;
        }
        mGetOtcTradeSettingServices.getOtcTradeSetting()
                                   .retry(2)
                                   .compose(bindToLifecycle())
                                   .subscribe(new RxProgressSubscriber<OTCGetOtcTradeSettingBean>((BaseWalletActivity) getContext()) {
                                       @Override
                                       public void onSuccess(OTCGetOtcTradeSettingBean getOtcTradeSettingBean) {
                                           //                                           OTCGetOtcTradeSettingBean getOtcTradeSettingBean1 = getOtcTradeSettingBean;
                                           if (getOtcTradeSettingBean != null) {
//                                               EventBus.getDefault().post(getOtcTradeSettingBean);
                                               getOtcTradeSettingInner(getOtcTradeSettingBean, requestCode); //由Eventbus改为直接请求
                                               SpUtils.setIntData("cancel_count",
                                                                  getOtcTradeSettingBean.getCancelCount());
                                           }
                                       }

                                       @Override
                                       protected void onError(ResponseThrowable ex) {
                                           LogUtil.e(ex.ErrorMsg + "OTCRecordFragment=========================" + ex.getErrorCode());
                                           ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                       }
                                   });
    }

    //获取剩余资产数量
    @Subscriber
    public void subscribeMineAsset(List<OTCWalletAssetBean> OTCWalletAssetBeans) {
        if (OTCWalletAssetBeans != null && OTCWalletAssetBeans.size() > 0) {
            mOTCWalletAssetBeans = OTCWalletAssetBeans;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser) {
            if (!mIsSetPayWay) {
                setDefaultInputData();
            }
            mIsSetPayWay = false;
        }

        super.setUserVisibleHint(isVisibleToUser);

    }


    @Override
    public void onStop() {
        if (!mIsSetPayWay) {
            setDefaultInputData();
        }
        mIsSetPayWay = false;
        super.onStop();
    }

    //输入栏初始化
    private void setDefaultInputData() {
        if (mImgBuyCoin != null && mTvBuyCoinYype != null && mEtCoinNum != null && mImgBuyPayCoin != null && mTvBuyPayCoinType != null && mImgBuyPayWay != null && mTvBuyPayCoinWay != null) {
            mImgBuyCoin.setImageDrawable(null);
            mTvBuyCoinYype.setText("");
            mTvBuyCoinYype.setHint(getActivity().getString(R.string.str_otc_buy_type_hint));

            mEtCoinNum.setText("");
            mEtCoinNum.setFocusable(false);
            mEtCoinNum.setHint(getActivity().getString(R.string.str_otc_enter_num));

            mImgBuyPayCoin.setImageDrawable(null);
            mTvBuyPayCoinType.setText("");
            mTvBuyPayCoinType.setHint(getActivity().getString(R.string.str_otc_buy_pay_type_hint));

            mImgBuyPayWay.setImageDrawable(null);
            mTvBuyPayCoinWay.setHint(getActivity().getString(R.string.str_otc_buy_pay_way_hint));
            mTvBuyPayCoinWay.setText("");
            mTvCoinSymbol.setText("");
            mTvCoinSymbol.setVisibility(View.GONE);

            mOTCCoinListBean = null;
            mCurrencyListBean = null;

            mIsCheck = SpUtils.getBooleanData(ISCHECK, false);
            mCbxBuy.setChecked(mIsCheck);
        }
    }

    //绑定手机的pop
    private boolean showBindPhonePop() {
        if(ACacheUtil.get(mContext).getAsBoolean(AcacheKeys.ISBINDPHONE, false)){ //已绑定手机返回true
            return true;
        }

        if(mOTCTwoBtnTitlePopWindow == null){
            mOTCTwoBtnTitlePopWindow = new OTCTwoBtnTitlePopWindow(
                    mContext);
            mOTCTwoBtnTitlePopWindow.setTitle(getActivity().getString(
                    foxidcw.android.idcw.otc.R.string.str_otc_bind_phone))
                                    .setConfirmContent(
                                            getActivity().getString(
                                                    foxidcw.android.idcw.otc.R.string.str_otc_to_bind))
                                    .setCancelContent(
                                            getActivity().getString(
                                                    foxidcw.android.idcw.otc.R.string.str_otc_talk_later));
            mOTCTwoBtnTitlePopWindow.getSkipSureDelete()
                                    .setOnClickListener(v1 -> {
                                        mIsSetPayWay = true;
                                        ARouter.getInstance()
                                               .build(ArouterConstants.ACTIVITY_PHONE)
                                               .navigation();
                                        mOTCTwoBtnTitlePopWindow.dismiss();
                                    });
            mOTCTwoBtnTitlePopWindow.getClickToDismissView()
                                    .setOnClickListener(v2 -> {
                                        mOTCTwoBtnTitlePopWindow.dismiss();
                                    });
        }
        if(!mOTCTwoBtnTitlePopWindow.isShowing()){
            mOTCTwoBtnTitlePopWindow.showPopupWindow();
        }

        return false;
    }

}

