package foxidcw.android.idcw.otc.activitys.pay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.TakePhotoUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.iprovider.UploadImgProviderServices;
import com.cjwsc.idcm.iprovider.impl.UploadImgProviderImpl;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCGetPaymentModeManagementServices;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectCoinTypePopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectPaymentMethodPopup;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

import static foxidcw.android.idcw.otc.activitys.pay.OTCAddOrEditPayMethodContacts.STATE_ALIPAY;
import static foxidcw.android.idcw.otc.activitys.pay.OTCAddOrEditPayMethodContacts.STATE_CNY;
import static foxidcw.android.idcw.otc.activitys.pay.OTCAddOrEditPayMethodContacts.STATE_USD;
import static foxidcw.android.idcw.otc.activitys.pay.OTCAddOrEditPayMethodContacts.STATE_VND;
import static foxidcw.android.idcw.otc.activitys.pay.OTCAddOrEditPayMethodContacts.STATE_WECHAT;

@Route(path = OTCConstant.PAY_METHOD_ADD_OR_EDIT, name = "添加或者修改支付方式")
public class OTCAddOrEditPayMethodActivity extends OTCPayTitleActivity implements OTCSelectPaymentMethodPopup.OnPaymentMethodSelectListener, OTCSelectCoinTypePopWindow.OnCoinTypeClickListener, OTCPayPinDialog.OnOTCPinInputCompleteListener, TakePhotoUtils.CallBack {

    private LinearLayout mRootLL;
    private ImageView mCurrencyCodeIv; // 法币图
    private TextView mCurrencyCodeTv; // 法币CODE
    private ImageView mPayTypeIv; // 支付方式图
    private TextView mPayTypeTv; // 支付方式文字
    private Button mSubmitBtn; // 提交按钮

    private LinearLayout mBankInfoContainerLl; // 银行卡信息
    private LinearLayout mAlipayContainerLl; // 支付宝信息

    private int mCurrentState = STATE_CNY; // 0 支付宝  1 CNY  2 USD  3 VND  4 WeChat

    // 事件监听
    private ArrayList<Observable> mCombineLatests = new ArrayList<>();
    // 所有的输入框
    private LinkedList<String> mAllCharSequences = new LinkedList<>();

    InitialValueObservable<CharSequence> mCurrencySubject; // 法币回调
    InitialValueObservable<CharSequence> mPayTypeSubject; // 支付方式回调

    // 选择法币方式
    private OTCSelectCoinTypePopWindow mCurrencySelectPopup;
    private OTCCoinListBean mCurrentSelectBean = null; // 当前选中的币种
    // 选择支付方式
    private OTCSelectPaymentMethodPopup mPaymentMethodPopup;
    private OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse mCurrentPaymentMethod; // 当前选中的支付方式

    @Autowired
    OTCGetPaymentModeManagementServices mGetPaymentModeManagementServices;

    // 上传图片
    UploadImgProviderServices mUploadImgProviderServices;

    // 所有的法币币种列表
    private List<OTCGetPaymentModeManagementResBean.LocalCurrencyLinkageResponse> mAllCurrecnyList = new ArrayList<>();
    // 缓存所有的法币币种列表
    private List<OTCGetPaymentModeManagementResBean.LocalCurrencyLinkageResponse> mCacheAllCurrecnyList = new ArrayList<>();
    // 法币对应的支付方式
    private List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> mAllPayTypeLists = new ArrayList<>();
    // 缓存法币对应的支付方式
    private List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> mCacheAllPayTypeLists = new ArrayList<>();

    // 支付宝或者微信姓名
    private EditText mAlipayNameEt;
    // 显示支付宝或者微信账号
    private TextView mAccountKeyTv;
    // 支付宝或者微信账号
    private EditText mAlipayAccountNoEt;

    // 编辑item
    OTCAddOrEditReqParams modifyItem;
    private List<String> mCurrents = new ArrayList<>();

    // pin弹框
    private OTCPayPinDialog mPinDialog;

    // 请求参数
    private OTCAddOrEditReqParams reqParams = new OTCAddOrEditReqParams();

    // 本地缓存
    private boolean isFirst = true;
    private String mNewCardNumber;

    // 是否已经选择图片
    private PublishSubject<CharSequence> mHasImageSubject;

    // 二维码外层控件
    private RelativeLayout mQrcodeContainerRl;
    // 选择二维码
    private TextView mAddQrcodeTv;
    //二维码图片
    private RoundedImageView mQrcodeIV;
    // 删除二维码
    private ImageView mDelQrcodeIb;

    private boolean mCurrentIsEdit = false;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otc_add_pay_method);
        if (getIntent().hasExtra("payment_method_bean")) {
            modifyItem = (OTCAddOrEditReqParams) getIntent().getSerializableExtra("payment_method_bean");
        }
        ARouter.getInstance().inject(this);

        mRootLL = findViewById(R.id.payment_method_add_or_edit_root_con_ll);
        mCurrencyCodeTv = findViewById(R.id.pay_method_currency_code_tv);
        mPayTypeTv = findViewById(R.id.pay_method_type_tv);
        mCurrencyCodeIv = findViewById(R.id.pay_method_currency_code_iv);
        mPayTypeIv = findViewById(R.id.pay_method_type_iv);
        mSubmitBtn = findViewById(R.id.add_pay_method_submit_btn);

        mAccountKeyTv = findViewById(R.id.pay_method_alipay_account_tv);
        mAlipayNameEt = findViewById(R.id.pay_method_alipay_account_name_tv);
        mAlipayAccountNoEt = findViewById(R.id.pay_method_alipay_account_no_tv);

        mAlipayContainerLl = findViewById(R.id.pay_method_alipay_container_ll);
        mBankInfoContainerLl = findViewById(R.id.pay_method_add_bank_container_ll);

        mSubmitBtn.setOnClickListener(this);
        findViewById(R.id.add_pay_method_select_pay_type_rl).setOnClickListener(this);
        findViewById(R.id.add_pay_method_select_currency_rl).setOnClickListener(this);

        mQrcodeContainerRl = findViewById(R.id.pay_method_qrcode_container_rl);
        mAddQrcodeTv = findViewById(R.id.pay_method_add_qrcode_tv);
        mAddQrcodeTv.setOnClickListener(this);
        mQrcodeIV = findViewById(R.id.pay_method_img_qrcode_iv);
        mDelQrcodeIb = findViewById(R.id.pay_method_del_qrcode_ib);
        mDelQrcodeIb.setOnClickListener(this);

        mCurrencySubject = RxTextView.textChanges(mCurrencyCodeTv);
        mPayTypeSubject = RxTextView.textChanges(mPayTypeTv);

        mHasImageSubject = PublishSubject.create();

        mCurrentIsEdit = modifyItem != null;
        if (mCurrentIsEdit) { // 修改
            /**
             * 设置本地的展示信息
             */
            mTitleTv.setText(R.string.str_otc_edit_payment_method);
            //mSubmitBtn.setText(getResources().getText(R.string.str_otc_accept_save));

            mCurrentSelectBean = new OTCCoinListBean();
            mCurrentSelectBean.setId(modifyItem.getLocalCurrencyId());
            mCurrentSelectBean.setCoinCode(modifyItem.getLocalCurrencyCode());
            mCurrentSelectBean.setLogo(modifyItem.getLocalCurrencyLogo());
            mCurrencyCodeTv.setCompoundDrawables(null, null, null, null);
            setCurrencyUI();

            mCurrentPaymentMethod = new OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse();
            mCurrentPaymentMethod.setID(modifyItem.getPayTypeId());
            mCurrentPaymentMethod.setPayTypeCode(modifyItem.getPayTypeCode());
            mCurrentPaymentMethod.setPayTypeLogo(modifyItem.getPayTypeLogo());
            mPayTypeTv.setCompoundDrawables(null, null, null, null);
            setPaymentMethodUI();
        } else {
            changeAddQrcodeVisible("");
            mGetPaymentModeManagementServices.getPaymentModeManagement()
                    .compose(bindToLifecycle())
                    .subscribeWith(new RxSubscriber<OTCGetPaymentModeManagementResBean>() {
                        @Override
                        public void onSuccess(OTCGetPaymentModeManagementResBean getPaymentModeManagementResBean) {
                            //mRootViewSL.setVisibility(View.VISIBLE);
                            mAllCurrecnyList.clear();
                            mAllCurrecnyList.addAll(getPaymentModeManagementResBean.getLocalCurrencyList());

                            mCacheAllCurrecnyList.clear();
                            mCacheAllCurrecnyList.addAll(getPaymentModeManagementResBean.getLocalCurrencyList());

                            mAllPayTypeLists.clear();
                            mAllPayTypeLists.addAll(getPaymentModeManagementResBean.getPaytypeList());

                            mCacheAllPayTypeLists.clear();
                            mCacheAllPayTypeLists.addAll(getPaymentModeManagementResBean.getPaytypeList());
                            LogUtil.e(getPaymentModeManagementResBean.toString());
                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                        }
                    });
        }
        changeUIState();
        keepWalletBtnNotOver(mRootLL, mRootLL);
    }

    private int scrollToPosition = 0;

    /**
     * 保持按钮始终不会被覆盖
     *
     * @param root
     * @param subView
     */
    private void keepWalletBtnNotOver(final View root, final View subView) {
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();// 屏幕宽（像素，如：480px）
        final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();// 屏幕高（像素，如：800p） 适配Note8
        LogUtil.e(Build.MODEL + ":screenWidth=" + screenWidth + ";screenHeight=" + screenHeight);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                // 若不可视区域高度大于200，则键盘显示,其实相当于键盘的高度
                if (rootInvisibleHeight > 200) {
                    //获取scrollToView在窗体的坐标,location[0]为x坐标，location[1]为y坐标
                    int[] location = new int[2];
                    subView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域的底部
                    int scrollHeight = (location[1] + subView.getHeight()) - rect.bottom;
                    //注意，scrollHeight是一个相对移动距离，而scrollToPosition是一个绝对移动距离
                    scrollToPosition += scrollHeight;
                } else {
                    //键盘隐藏
                    scrollToPosition = 0;
                }
                root.scrollTo(0, scrollToPosition);
            }
        });
    }

    /**
     * 是否已经有qrcode
     *
     * @param path
     */
    private void changeAddQrcodeVisible(String path) {
        if (TextUtils.isEmpty(path)) {
            mHasImageSubject.onNext("");
            mQrcodeContainerRl.setBackgroundResource(R.drawable.otc_add_payment_method_receiver_bg); // 如果是添加的时候才有
            mAddQrcodeTv.setVisibility(View.VISIBLE);
            mDelQrcodeIb.setVisibility(View.GONE);
            mQrcodeIV.setVisibility(View.GONE);
        } else {
            mHasImageSubject.onNext(path);
            mQrcodeContainerRl.setBackgroundDrawable(null); // 如果是添加的时候才有
            mAddQrcodeTv.setVisibility(View.GONE);
            mDelQrcodeIb.setVisibility(View.VISIBLE);
            mQrcodeIV.setVisibility(View.VISIBLE);
            // 加载图片
            GlideUtil.loadImageView(mCtx, path, mQrcodeIV);
        }
    }

    /**
     * 根据不同的状态来切换不同的界面展示
     */
    @SuppressLint("CheckResult")
    private void changeUIState() {
        if (mCurrentSelectBean == null || mCurrentPaymentMethod == null) return;
        if (mCurrentPaymentMethod.checkIsAlipay()) {
            mCurrentState = STATE_ALIPAY;
        } else if (mCurrentPaymentMethod.checkIsWeChat()) {
            mCurrentState = STATE_WECHAT;
        } else {
            switch (mCurrentSelectBean.getCoinCode()) {
                case "VND":
                    mCurrentState = STATE_VND;
                    break;
                case "USD":
                    mCurrentState = STATE_USD;
                    break;
                case "CNY":
                    mCurrentState = STATE_CNY;
                    break;
            }
        }
        if (!OTCAddOrEditPayMethodContacts.checkIsBankCard(mCurrentState)) { // 微信或者支付宝
            if (null == mUploadImgProviderServices) {
                mUploadImgProviderServices = new UploadImgProviderImpl();
            }

            mAlipayContainerLl.setVisibility(View.VISIBLE);
            mBankInfoContainerLl.setVisibility(View.GONE);
            mCombineLatests.clear();
            mCombineLatests.add(RxTextView.textChanges(mAlipayNameEt));
            mCombineLatests.add(RxTextView.textChanges(mAlipayAccountNoEt));
            mCombineLatests.add(mHasImageSubject);

            // 设置显示账号key部分
            mAccountKeyTv.setText(mCurrentPaymentMethod.checkIsAlipay() ? R.string.str_otc_pay_alipay_account_name : R.string.str_otc_pay_wechat_account_name);

            if (mCurrentIsEdit) { // 如果是修改
                String userName = modifyItem.getPayAttributes().getUserName();
                mAlipayNameEt.setText(userName);
                mAlipayNameEt.setSelection(userName.length());
                mAlipayAccountNoEt.setText(modifyItem.getPayAttributes().getAccountNo());

                changeAddQrcodeVisible(modifyItem.getPayAttributes().getQRCode());
            }
        } else { // 银行卡
            mAlipayContainerLl.setVisibility(View.GONE);
            int[][] contacts = OTCAddOrEditPayMethodContacts.getBankContactsWithState(mCurrentState);
            int[] keys = contacts[0];
            int[] hints = contacts[1];
            mCombineLatests.clear();
            mBankInfoContainerLl.removeAllViews();
            mBankInfoContainerLl.addView(getLayoutInflater().inflate(R.layout.activity_otc_add_pay_method_bank_top, null));
            int length = keys.length;
            boolean flag = false;
            if (modifyItem != null) {
                flag = true;
                OTCAddOrEditReqParams.PayAttributes attributes = modifyItem.getPayAttributes();
                mCurrents.add(attributes.getUserName());
                mCurrents.add(attributes.getAccountNo());
                mCurrents.add(attributes.getBankName());
                switch (mCurrentState) {
                    case STATE_CNY:
                        mCurrents.add(attributes.getBankBranch());
                        break;
                    case STATE_VND:
                        mCurrents.add(attributes.getCity());
                        mCurrents.add(attributes.getBankBranch());
                        break;
                    case STATE_USD:
                        //mCurrents.add(attributes.getBankBranch());
                        mCurrents.add(attributes.getBankAddress());
                        mCurrents.add(attributes.getSwiftCode());
                        break;
                }
            }
            for (int i = 0; i < length; i++) {
                FrameLayout item = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_otc_add_pay_method_item, null);
                TextView key = item.findViewById(R.id.pay_method_item_key_tv);
                EditText value = item.findViewById(R.id.pay_method_item_value_et);
                key.setText(keys[i]);
                if (flag) {
                    value.setText(mCurrents.get(i));
                    if (i == 0) {
                        value.setSelection(mCurrents.get(0).length());
                    }
                }
                value.setHint(hints[i]);
                if (i == 1) { // 卡号
                    value.setInputType(InputType.TYPE_CLASS_NUMBER);
                    value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
                }
                //autoScrollView(mRootLL);
                if (i == 1) {
                    InitialValueObservable<CharSequence> valueObservable = RxTextView.textChanges(value);
                    valueObservable.share().subscribe(new Consumer<CharSequence>() {
                        @Override
                        public void accept(CharSequence charSequence) throws Exception {
                            if (charSequence == null || charSequence.length() == 0) return;
                            String temp = charSequence.toString();
                            String newText = "";
                            temp = temp.replace(" ", "");
                            for (int j = 0, length = temp.length(); j < length; j++) {
                                newText = newText + temp.subSequence(j, j + 1);
                                if ((j + 1) % 4 == 0 && j != length - 1) {
                                    newText = newText + " ";
                                }
                            }
                            int selection;
                            if (isFirst || charSequence.length() == 24) {
                                isFirst = false;
                                selection = newText.length();
                            } else {
                                selection = Math.min(value.getSelectionStart(), value.getSelectionEnd()) + (newText.length() - charSequence.toString().length());
                            }
                            if (mNewCardNumber == null || !newText.equals(mNewCardNumber)) {
                                mNewCardNumber = newText;
                                value.setText(newText);
                                if (value.getText().toString().length() <= newText.length()) {
                                    try {
                                        value.setSelection(selection < 0 ? 0 : selection);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    });
                    mCombineLatests.add(valueObservable);
                } else {
                    mCombineLatests.add(RxTextView.textChanges(value));
                }
                mBankInfoContainerLl.addView(item);
            }
            mBankInfoContainerLl.setVisibility(View.VISIBLE);
        }
        mCombineLatests.add(mCurrencySubject); // 币种
        mCombineLatests.add(mPayTypeSubject); // 支付方式

        Observable.combineLatest(mCombineLatests.toArray(new Observable[mCombineLatests.size()]), new Function<Object[], Boolean>() {
            @Override
            public Boolean apply(Object[] charSequences) throws Exception {
                mAllCharSequences.clear();
                return Observable
                        .fromArray(charSequences)
                        .all(new Predicate<Object>() {
                                 @Override
                                 public boolean test(Object o) throws Exception {
                                     String str = String.valueOf(o);
                                     mAllCharSequences.add(str);
                                     for (String ds :
                                             mAllCharSequences) {
                                         LogUtil.e("ds --- > " + ds);
                                     }
                                     return str.length() > 0;
                                 }
                             }
                        ).blockingGet();
            }
        }).distinctUntilChanged().subscribe((Consumer<Boolean>) flag -> mSubmitBtn.setEnabled(flag));

        /**
         * 只能这里回调编辑的
         */
        if (mCurrentIsEdit && (mCurrentPaymentMethod.checkIsWeChat() || mCurrentPaymentMethod.checkIsAlipay()))
            mHasImageSubject.onNext(!TextUtils.isEmpty(modifyItem.getPayAttributes().getQRCode()) ? modifyItem.getPayAttributes().getQRCode() : "");
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.add_pay_method_select_pay_type_rl) { // 选择支付方式
            if (null != modifyItem || mAllPayTypeLists.size() <= 0) return;
            if (null == mPaymentMethodPopup) {
                mPaymentMethodPopup = new OTCSelectPaymentMethodPopup(mCtx).setShowSureButtonEnable(false);
                mPaymentMethodPopup.setOnPaymentMethodSelectListener(this);
            }
            HashSet<Integer> hashSet = new HashSet<>();
            if (null != mCurrentPaymentMethod) hashSet.add(mCurrentPaymentMethod.getID());
            mPaymentMethodPopup.showSelectPaymentWithData(mAllPayTypeLists, mCtx.getResources().getString(R.string.str_otc_buy_pay_way_hint), hashSet);
        } else if (id == R.id.add_pay_method_select_currency_rl) { // 选择法币
            if (null != modifyItem || mAllCurrecnyList.size() <= 0) return;
            if (null == mCurrencySelectPopup) {
                mCurrencySelectPopup = new OTCSelectCoinTypePopWindow(mCtx);
                mCurrencySelectPopup.setOnItemClickListener(this);
            }
            List<OTCCoinListBean> beans = new ArrayList<>();
            for (OTCGetPaymentModeManagementResBean.LocalCurrencyLinkageResponse item : mAllCurrecnyList) {
                OTCCoinListBean bean = new OTCCoinListBean();
                bean.setId(item.getID());
                bean.setCoinCode(item.getLocalCurrencyCode());
                bean.setLogo(item.getLocalCurrencyLogo());
                beans.add(bean);
            }
            mCurrencySelectPopup.showPopWindowForData(beans, 1, mCurrentSelectBean);
            mCurrencySelectPopup.setTitle(mCtx.getResources().getString(R.string.str_otc_select_currency));
        } else if (id == R.id.add_pay_method_submit_btn) { // 提交
            reqParams.setID(modifyItem == null ? 0 : modifyItem.getID());
            reqParams.setLocalCurrencyId(mCurrentSelectBean.getId());
            reqParams.setPayTypeId(mCurrentPaymentMethod.getID());
            OTCAddOrEditReqParams.PayAttributes attributes = new OTCAddOrEditReqParams.PayAttributes();
            attributes.setUserName(mAllCharSequences.get(0)); // 姓名
            attributes.setAccountNo(mAllCharSequences.get(1).replaceAll(" ", "")); // 账号
//            if (OTCAddOrEditPayMethodContacts.checkIsBankCard(mCurrentState)) { // 如果不是微信并且不是支付宝
//                attributes.setBankName(mAllCharSequences.get(2)); // 银行
//                switch (mCurrentState) {
//                    case STATE_CNY:
//                        attributes.setBankBranch(mAllCharSequences.get(3));
//                        break;
//                    case STATE_VND:
//                        attributes.setCity(mAllCharSequences.get(3));
//                        attributes.setBankBranch(mAllCharSequences.get(4));
//                        break;
//                    case STATE_USD:
//                        attributes.setBankBranch("");
//                        attributes.setBankAddress(mAllCharSequences.get(3));
//                        attributes.setSwiftCode(mAllCharSequences.get(4));
//                        break;
//                }
//            }
            // 如果是微信或者支付宝
            if (OTCAddOrEditPayMethodContacts.checkIsAlipay(mCurrentState) || OTCAddOrEditPayMethodContacts.checkIsWeChat(mCurrentState)) {
                String imagePath = mAllCharSequences.get(2);
                if (imagePath.startsWith("http")) {
                    attributes.setQRCode(imagePath);
                    showPinDialog(attributes);
                } else {
                    mUploadImgProviderServices.requestUpload(Arrays.asList(imagePath))
                            .compose(bindToLifecycle())
                            .subscribeWith(new RxProgressSubscriber<Object>(this) {
                                @Override
                                public void onSuccess(Object data) {
                                    // 如果是微信或者支付宝  那么第二个就是图片地址
                                    attributes.setQRCode(String.valueOf(data));
                                    showPinDialog(attributes);
                                }

                                @Override
                                protected void onError(ResponseThrowable ex) {
                                    ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                }
                            });
                }
                return;
            } else {
                attributes.setBankName(mAllCharSequences.get(2)); // 银行
                switch (mCurrentState) {
                    case STATE_CNY:
                        attributes.setBankBranch(mAllCharSequences.get(3));
                        break;
                    case STATE_VND:
                        attributes.setCity(mAllCharSequences.get(3));
                        attributes.setBankBranch(mAllCharSequences.get(4));
                        break;
                    case STATE_USD:
                        attributes.setBankBranch("");
                        attributes.setBankAddress(mAllCharSequences.get(3));
                        attributes.setSwiftCode(mAllCharSequences.get(4));
                        break;
                }
                showPinDialog(attributes);
            }
        } else if (id == R.id.pay_method_add_qrcode_tv) {
            // 选择二维码图片
            TakePhotoUtils.showDialog(this, this);
        } else if (id == R.id.pay_method_del_qrcode_ib) {
            // 删除二维码图片
            new OTCDeleteCurrPayPopWindow(mCtx)
                    .setTitle(mCtx.getResources().getString(R.string.str_otc_pay_sure_to_delete_qrcode))
                    .setContentTextSize(14)
                    .setCancelContent(mCtx.getResources().getString(R.string.cancel))
                    .setConfirmContent(mCtx.getResources().getString(R.string.tv_sure))
                    .setClickListener(type -> {
                        if (type == 1)
                            changeAddQrcodeVisible("");
                    })
                    .showPopupWindow();
        }
    }

    @Override
    protected void clearCurrentFocus(View view) {
        view.clearFocus();
    }

    /**
     * 显示输入pin的界面
     */
    private void showPinDialog(OTCAddOrEditReqParams.PayAttributes attributes) {
        reqParams.setPayAttributes(attributes);
        reqParams.setDeviceId(LoginUtils.getLoginBean(mCtx).getDevice_id());
        mPinDialog = new OTCPayPinDialog(mCtx)
                .setType(OTCPayPinDialog.STATE_ALL_PAGE)
                .setResultText(getString(modifyItem == null ?
                        R.string.str_otc_add_payment_method_success :
                        R.string.str_otc_edit_payment_method_success))
                .setOnOTCPinInputCompleteListener(this);
        mPinDialog.show();
    }

    /**
     * 选择后通知界面更新
     *
     * @param path
     */
    @Override
    public void onActivityResult(String path) {
        changeAddQrcodeVisible(path);
    }

    private void setPaymentMethodUI() {
        mPayTypeIv.setVisibility(View.VISIBLE);
        GlideUtil.loadImageView(mCtx, mCurrentPaymentMethod.getPayTypeLogo(), mPayTypeIv);
        mPayTypeTv.setText(mCurrentPaymentMethod.getShowPayTypeCode(mCtx));
        mPayTypeTv.setTextColor(mCtx.getResources().getColor(R.color.c_FF333333));
    }

    @Override
    public void onClick(OTCCoinListBean bean, int position) {
        mCurrentSelectBean = bean;
        setCurrencyUI();
        mAllPayTypeLists.clear();
        for (OTCGetPaymentModeManagementResBean.LocalCurrencyLinkageResponse response : mCacheAllCurrecnyList) {
            if (response.getID() == bean.getId() && bean.getCoinCode().equals(response.getLocalCurrencyCode())) {
                mAllPayTypeLists.addAll(response.getPaytypeList());
            }
        }
        changeUIState();
    }

    private void setCurrencyUI() {
        mCurrencyCodeIv.setVisibility(View.VISIBLE);
        GlideUtil.loadImageView(mCtx, mCurrentSelectBean.getLogo(), mCurrencyCodeIv);
        mCurrencyCodeTv.setText(mCurrentSelectBean.getCoinCode());
        mCurrencyCodeTv.setTextColor(mCtx.getResources().getColor(R.color.c_FF333333));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onComplete(String inputPass) {
        reqParams.setPIN(inputPass);
        mGetPaymentModeManagementServices
                .paymentModeChange(reqParams)
                .compose(bindToLifecycle())
                .subscribeWith(new RxSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        // 添加或者修改成功
                        if (null != mPinDialog) mPinDialog.showResultPage();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        mPinDialog.dismiss();
                        ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                    }
                });
    }


    @Override
    public void onPaymentMethodSelected(ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> selectItems) {
        if (selectItems.size() <= 0) return;
        mCurrentPaymentMethod = selectItems.get(0);
        setPaymentMethodUI();
        mAllCurrecnyList.clear();
        for (OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse response : mCacheAllPayTypeLists) {
            if (response.getID() == mCurrentPaymentMethod.getID()) {
                mAllCurrecnyList.addAll(response.getLocalCurrencyList());
            }
        }
        changeUIState();
    }

    @Override
    protected String getTitleText() {
        return mCtx.getResources().getString(R.string.str_otc_add_payment_method);
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onConfirmBtnClick() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

}