package com.idcg.idcw.activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.BuildConfig;
import com.idcg.idcw.R;
import com.idcg.idcw.configs.ClientConfig;
import com.idcg.idcw.fragments.PaySuccessFragment;
import com.idcg.idcw.iprovider.GetWalletBalanceProviderServices;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.bean.ReFeeBean;
import com.idcg.idcw.model.bean.ReqSyncAddressParam;
import com.idcg.idcw.model.bean.SendTradeBean;
import com.idcg.idcw.model.bean.WalletAssetBean;
import com.idcg.idcw.model.logic.SendCurrencyLogic;
import com.idcg.idcw.model.params.CheckAddressReqParam;
import com.idcg.idcw.model.params.ReFeeReqParam;
import com.idcg.idcw.model.params.SendFormReqAndResParam;
import com.idcg.idcw.model.params.SendTradeReqParam;
import com.idcg.idcw.presenter.impl.SendCurrencyPresenterImpl;
import com.idcg.idcw.presenter.interfaces.SendCurrencyContract;
import com.idcg.idcw.utils.SoftHideKeyBoardUtil;
import com.idcg.idcw.utils.TimeCountUtils;
import com.idcg.idcw.utils.UIUtils;
import com.idcg.idcw.utils.Utils;
import com.idcg.idcw.widget.InPutEditText;
import com.idcg.idcw.widget.NewKeyBoard;
import com.idcg.idcw.widget.NewPasswordView;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.AESUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ScreenUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.shuweikeji.qrcode.activity.CaptureActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.xuelianx.fingerlib.FingerFragment;
import com.xuelianx.fingerlib.bean.EventBean;
import com.xw.repo.BubbleSeekBar;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.epsoft.keyboard.widget.PayKeyboardView;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.common.widget.ClearableEditText;
import io.reactivex.functions.Action;

/**
 * Created by hpz on 2017/12/19.
 */

public class SendCurrencyActivity extends BaseWalletActivity<SendCurrencyLogic, SendCurrencyPresenterImpl> implements View.OnClickListener
        , PayKeyboardView.OnKeyboardListener, TextWatcher, SendCurrencyContract.View, NewPasswordView.PasswordListener {
    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.ed_account)
    ClearableEditText edAccount;
    @BindView(R.id.btn_sure_send)
    TextView btnSureSend;
    @BindView(R.id.seekBar)
    BubbleSeekBar seekBar;
    @BindView(R.id.tv_btc_amount)
    TextView tvBtcAmount;
    @BindView(R.id.btn_code)
    LinearLayout btnCode;
    @BindView(R.id.tv_enable_balance)
    TextView tvEnableBalance;
    @BindView(R.id.tv_enable_currency)
    TextView tvEnableCurrency;
    @BindView(R.id.tv_free_currency)
    TextView tvFreeCurrency;
    @BindView(R.id.ed_amount)
    ClearableEditText edAmount;
    @BindView(R.id.ed_common)
    InPutEditText edCommon;
    @BindView(R.id.send_layout)
    LinearLayout sendLayout;
    @BindView(R.id.tv_copy_address)
    TextView tvCopyAddress;
    @BindView(R.id.text_free)
    TextView textFree;
    @BindView(R.id.ll_relative)
    LinearLayout llRelative;
    @BindView(R.id.tv_enable_type)
    TextView tvEnableType;
    @BindView(R.id.img_code)
    ImageView imgCode;
    @BindView(R.id.ll_free_seekbar)
    LinearLayout llFreeSeekbar;
    @BindView(R.id.ll_vhkd_btl)
    LinearLayout llVhkdBtl;
    @BindView(R.id.tv_seek_balance)
    TextView tvSeekBalance;
    @BindView(R.id.tv_seek_currency)
    TextView tvSeekCurrency;
    @BindView(R.id.ll_seek_free)
    LinearLayout llSeekFree;
    @BindView(R.id.ll_send_head)
    LinearLayout llSendHead;
    @BindView(R.id.app_two_view)
    View appTwoView;

    private View inflate;
    private String currency;
    private TextView btn_now_send;
    private AppCompatDialog mDialog;
    private LinearLayout mr_back_layout;
    private String value;
    private String payPass;
    private PayKeyboardView panel = null;
    private double currentBalance;
    private double realityBalance;
    private String slow, fast, veryFast;
    private TextView tv_amount_currency, tv_now_address, tv_now_free, tv_now_common;
    private String tv_sure_amount;
    private String tv_now_only_free;
    private Dialog dialogSure;
    private Dialog dialogNewPin;
    private Dialog dialog1;
    private View inflater;
    private TextView tv_sure;
    private TextView tv_activity_update;
    private int currencyLayoutType;
    private boolean isToken;
    private String tokenCategory;
    private String coinUnit;
    private double ethBalanceForToken;
    private boolean istoken;
    private TimeCountUtils timeCountUtils;
    private static final int INTERVAL = 90;


    @Autowired
    GetWalletBalanceProviderServices getWalletBalanceProviderServices;
    private ProgressBar progress;
    private LinearLayout ll_send_sure;
    private LinearLayout btn_send_layout;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "ok"
    };
    private NewKeyBoard pin_keyboard;
    private String pinPass;
    private NewPasswordView passwordView;
    private ProgressBar sure_progress;
    private LinearLayout sure_ll_send_sure;
    private TextView sure_btn_now_send;
    private ImageView icon;
    private ImageView icon_sure;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_currency;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        tvSetName.setText(R.string.send);
        imgCode.setVisibility(View.GONE);
        LoginStatus bean = LoginUtils.getLoginBean(this);
        if (bean != null) {
            value = bean.getTicket();
        }
        initCameraDialog();
        edCommon.addTextChangedListener(this);
        Intent intent = getIntent();
        Bundle bundle1 = intent.getBundleExtra("Send");
        currency = bundle1.getString("currency");
        isToken = bundle1.getBoolean("isTokenSend");
        currencyLayoutType = bundle1.getInt("currencyLayoutType");
        LogUtil.e("istoken==", isToken + "");
        tokenCategory = bundle1.getString("tokenCategorySend");
        coinUnit = bundle1.getString("coinUnit");
        ethBalanceForToken = bundle1.getDouble("ethBalanceForToken");

        keepWalletBtnNotOver(llSendHead, sendLayout);//
        if (isToken) {
            tvEnableType.setText(currency.toUpperCase());
            tvEnableCurrency.setText(currency.toUpperCase());
            tvSeekCurrency.setText(tokenCategory.toUpperCase());
            LogUtil.e("tvSeekCurrency==", tokenCategory.toUpperCase());
            tvFreeCurrency.setText(currency.toUpperCase());
            RequestReFree(currency);
        } else {
            tvEnableType.setText(currency.toUpperCase());
            tvEnableCurrency.setText(currency.toUpperCase());
            tvSeekCurrency.setText(currency.toUpperCase());
            tvFreeCurrency.setText(currency.toUpperCase());
        }

        if (currencyLayoutType == 1) {
            textFree.setVisibility(View.INVISIBLE);
            llSeekFree.setVisibility(View.GONE);
            llVhkdBtl.setVisibility(View.VISIBLE);
        }else if (currencyLayoutType == 2){
            llSeekFree.setVisibility(View.GONE);
            appTwoView.setVisibility(View.GONE);
        }

        btnSureSend.setEnabled(false);
        edAccount.addTextChangedListener(this);
        edAmount.addTextChangedListener(this);
        edAccount.setFilters(new InputFilter[]{filter});
        ClientConfig.Instance().setSaveBtc(currency.toUpperCase());

        seekBar.setCustomSectionTextArray((sectionCount, array) ->  {
                array.clear();
                array.put(0, getString(R.string.slow));
                array.put(1, getString(R.string.fast));
                array.put(2, getString(R.string.very_fast));
                return array;
        });
        seekBar.setProgress(1);
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                switch (progress) {
                    case 0:
                        tvSeekBalance.setText(slow);
                        break;
                    case 1:
                        tvSeekBalance.setText(fast);
                        break;
                    case 2:
                        tvSeekBalance.setText(veryFast);
                        break;
                }
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });
        sendLayout.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (!TextUtils.isEmpty(edAmount.getText().toString())) {
                edAmount.clearFocus();
                edAccount.clearFocus();
            } else {

            }
            return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        });
        SpannableString ss = new SpannableString(getString(R.string.please_input_amount));//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(ScreenUtil.dp2px(5, SendCurrencyActivity.this), true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        edAmount.setHint(new SpannedString(ss));

        //初始化发送对话框
        initSendDialog();
        //新的PIN键盘
        //initNewPinDialog();
    }

    /**
     * 保持按钮始终不会被覆盖
     *
     * @param root
     * @param subView
     */
    private void keepWalletBtnNotOver(final View root, final View subView) {
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();       // 屏幕宽（像素，如：480px）
        final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();      // 屏幕高（像素，如：800p） 适配Note8
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
                if (rootInvisibleHeight > 200 && screenHeight < 2800) {
                    // 显示键盘时
                    int srollHeight = rootInvisibleHeight - (root.getHeight() - subView.getHeight()) - SoftHideKeyBoardUtil.getNavigationBarHeight(root.getContext());
                    if (srollHeight > 0) { //当键盘高度覆盖按钮时
                        root.scrollTo(0, com.cjwsc.idcm.base.ui.view.countdownview.Utils.dp2px(getApplicationContext(), 25));
                    }
                } else {
                    // 隐藏键盘时
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    @Override
    protected void onEvent() {
        RequestNewBalance(currency);
        edAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                //ToastUtils.showToast(SendCurrencyActivity.this,"聚焦");
            } else {
                // 此处为失去焦点时的处理内容
                //ToastUtils.showToast(SendCurrencyActivity.this,"失焦");
                if (!TextUtils.isEmpty(edAmount.getText().toString())) {
                    if (isToken) return;
                    RequestReFree(currency);
                }
            }
        });

        edAccount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                //ToastUtils.showToast(SendCurrencyActivity.this,"聚焦");
            } else {
                // 此处为失去焦点时的处理内容
                //ToastUtils.showToast(SendCurrencyActivity.this,"失焦");
                if (!TextUtils.isEmpty(edAmount.getText().toString())) {
                    if (isToken) return;
                    RequestReFree(currency);
                }
            }
        });

    }

    @Override
    protected BaseView getView() {
        return this;
    }


    private void initCameraDialog() {
        try {
            dialog1 = new Dialog(this, R.style.shuweiDialog);
            inflater = LayoutInflater.from(this).inflate(R.layout.activity_sao_qr_dialog, null);
            dialog1.setContentView(inflater);
            dialog1.setCancelable(false);
            Window dialogWindow = dialog1.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
            tv_activity_update = (TextView) inflater.findViewById(R.id.tv_activity_update);
            tv_sure = (TextView) inflater.findViewById(R.id.tv_activity_cancel);
            tv_sure.setOnClickListener(this);
            tv_activity_update.setOnClickListener(this);
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };

    private void showFragment() {
        try {
            FingerFragment fingerFragment = new FingerFragment();
            fingerFragment.show(getFragmentManager(), "fingerFragment");
            fingerFragment.setCancelable(false);
            fingerFragment.setmFragmentCallBack(new FingerFragment.Callback() {
                @Override
                public void onSuccess() {
                    try {
                        icon.setVisibility(View.VISIBLE);
                        LoginStatus bean = LoginUtils.getLoginBean(SendCurrencyActivity.this);
                        String pass = ACacheUtil.get(SendCurrencyActivity.this).getAsString(bean.getUser_name() + "");
                        String pay = new AESUtil().decrypt(pass);
                        RequestSendTrade(currency, pay);
                    } catch (Exception ex) {
                        LogUtil.e("onSuccess:", ex.getMessage());
                    }
                }

                @Override
                public void onError() {
                    if (panel != null) {
                        panel.clear();
                    }
                    initNewPinDialog();
                }
            });
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getStackTrace().toString());
        }
    }

    private void showSureSendDialog() {
        try {
            if (TextUtils.isEmpty(tvBtcAmount.getText().toString()) || TextUtils.isEmpty(tvSeekBalance.getText().toString())) {
                //     RequestSendFrom(currency, "0");
                mPresenter.requestSendFrom(new SendFormReqAndResParam(edAccount.getText().toString().trim(), edAmount.getText().toString(), currency, "0", 1));
            } else {
                // RequestSendFrom(currency, tvSeekBalance.getText().toString());
                mPresenter.requestSendFrom(new SendFormReqAndResParam(edAccount.getText().toString().trim(), edAmount.getText().toString(), currency, tvSeekBalance.getText().toString(), 1));
            }

        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getStackTrace().toString());
        }
    }


    private void initSendDialog() {

        dialogSure = new Dialog(this, R.style.BottomDialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_sure_send_dialog, null);
        dialogSure.setCancelable(false);
        //初始化控件
        btn_now_send = (TextView) inflate.findViewById(R.id.btn_now_send);
        mr_back_layout = (LinearLayout) inflate.findViewById(R.id.mr_back_layout);
        tv_amount_currency = (TextView) inflate.findViewById(R.id.tv_amount_currency);
        tv_now_address = (TextView) inflate.findViewById(R.id.tv_now_address);
        tv_now_free = (TextView) inflate.findViewById(R.id.tv_now_free);
        tv_now_common = (TextView) inflate.findViewById(R.id.tv_now_common);
        sure_progress = (ProgressBar) inflate.findViewById(R.id.sure_progressBar);//
        icon = (ImageView) inflate.findViewById(R.id.icon_sure);
        startAnimation();
        sure_ll_send_sure = (LinearLayout) inflate.findViewById(R.id.ll_send_sure);//
        sure_btn_now_send = (TextView) inflate.findViewById(R.id.btn_now_send);//
        mr_back_layout.setOnClickListener(this);
        btn_now_send.setOnClickListener(this);
        //dialogSure
        dialogSure.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialogSure.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        //将属性设置给窗体
        dialogSure.setCanceledOnTouchOutside(false); // 外部点击取消
        //设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialogSure.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = this.getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);
    }

    AnimationDrawable mAnimation;

    private void initNewPinDialog() {
        dialogNewPin = new Dialog(this, R.style.BottomDialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_new_please_send_pin, null);
        dialogNewPin.setCancelable(false);
        //初始化控件
        pin_keyboard = (NewKeyBoard) inflate.findViewById(R.id.pin_keyboard);
        LinearLayout new_mr_back_layout = (LinearLayout) inflate.findViewById(R.id.new_mr_back_layout);
        passwordView = (NewPasswordView) inflate.findViewById(R.id.passwordView);
        progress = (ProgressBar) inflate.findViewById(R.id.progressBar);//
        icon = (ImageView) inflate.findViewById(R.id.icon);
        startAnimation();
        ll_send_sure = (LinearLayout) inflate.findViewById(R.id.ll_send_sure);//
        new_mr_back_layout.setOnClickListener(this);
        passwordView.setPasswordListener(this);
        passwordView.setKeyBoard(pin_keyboard);
        setSubView();
        pin_keyboard.Show();

        //操作键盘
        pin_keyboard.setOnClickKeyboardListener((position, value1) -> {
            if (position < 11 && position != 9) {
                passwordView.addData(value1);
            } else if (position == 9) {
                //passwordView.delData(value);
            } else if (position == 11) {
                //点击完成 隐藏输入键盘
                passwordView.delData(value1);
            }
        });
        //将布局设置给Dialog
        dialogNewPin.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialogNewPin.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        //将属性设置给窗体
        dialogNewPin.setCanceledOnTouchOutside(false); // 外部点击取消
        //设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialogNewPin.getWindow();
        window.setWindowAnimations(R.style.AnimRightBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = this.getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);
        dialogNewPin.show();
    }

    private void startAnimation() {
        mAnimation = new AnimationDrawable();
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_01), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_02), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_03), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_04), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_05), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_06), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_07), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_08), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_09), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_10), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_11), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_12), INTERVAL);
        mAnimation.setOneShot(false);
        icon.setBackground(mAnimation);
        if (mAnimation != null && !mAnimation.isRunning()) {
            mAnimation.start();
        }
    }

    private void RequestNewBalance(String currency) {
        if(dialog!=null){
            dialog.setBackPressEnable(false);
        }
        mPresenter.requestNewBalance(currency);
    }

    @OnClick({R.id.mr_back_layout, R.id.btn_sure_send, R.id.btn_code, R.id.tv_copy_address, R.id.img_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_code:
                new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            LogUtil.i("--------granted------>", granted + "");
                            if (granted) {
                                Intent intent = new Intent();
                                intent.setClass(SendCurrencyActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, 11);
                            } else {
                                dialog1.show();
                            }
                        });
                break;
            case R.id.mr_back_layout:
                EventBus.getDefault().post(new PosInfo(2020));
                this.finish();
                break;
            case R.id.tv_copy_address:
                try {
                    ClipboardManager paste = (ClipboardManager) SendCurrencyActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    String content = paste.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    edAccount.setText(content);
                    showDialog();
                    RequestCheckAddress(edAccount.getText().toString(), currency);
                } catch (Exception ex) {
                    LogUtil.e("Exception:", ex.getMessage());
                }
                break;
            case R.id.btn_sure_send:
                showDialog();

                timeCountUtils = new TimeCountUtils(1000, 2000, btnSureSend, "");
                timeCountUtils.start();
                hideSoftKeyBoard(view.getWindowToken());
                if (!TextUtils.isEmpty(edAmount.getText().toString()) && TextUtils.isEmpty(tvSeekBalance.getText().toString())) {
                    RequestNewReFree(currency);
                } else {
                    if (!TextUtils.isEmpty(edAccount.getText().toString())) {
                        RequestCheckBtnAddress(edAccount.getText().toString(), tvEnableCurrency.getText().toString().toLowerCase());
                    }
                }
                break;
            case R.id.btn_code:
                new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            LogUtil.i("--------granted------>", granted + "");
                            if (granted) {
                                Intent intent = new Intent();
                                intent.setClass(SendCurrencyActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, 11);
                            } else {
                                //Toast.makeText(SendCurrencyActivity.this, "无权限访问", Toast.LENGTH_SHORT).show();
                                dialog1.show();
                            }
                        });
                break;
        }
    }

    private void RequestCheckBtnAddress(String address, String currency) {
        if(dialog!=null){
            dialog.setBackPressEnable(false);
        }
        getWalletBalanceProviderServices.requestCheckBtnAddressProvider(new CheckAddressReqParam(address, currency))
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(dialog!=null && !BuildConfig.DEBUG){
                            dialog.setBackPressEnable(true);
                        }
                    }
                })
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<Boolean>(this) {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.server_connection_error));
                        if (0 == ex.getState()) {
                            edAccount.setText(address);
                            ToastUtil.show(getString(R.string.address_not_true));
                        }
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (!aBoolean) {
                            dismissDialog();
                            edAccount.setText(address);
                            ToastUtil.show(getString(R.string.address_not_true));
                        }

                        String strEnable = tvEnableBalance.getText().toString().replace(",", "");
                        LogUtil.e("tvEnableBalance:", strEnable);
                        if (isToken) {
                            if (!TextUtils.isEmpty(tvSeekBalance.getText().toString())) {
                                if (ethBalanceForToken < Double.valueOf(slow)) {
                                    dismissDialog();
                                    ToastUtil.show(getString(R.string.eth_not_enough));
                                    return;
                                }
//                                if (Double.valueOf(edAmount.getText().toString()) < 0.00006) {
//                                    dismissDialog();
//                                    ToastUtil.show(getString(R.string.most_amount_more_enable));
//                                    return;
//                                }
//                                if (Double.valueOf(edAmount.getText().toString()) > realityBalance) {
//                                    dismissDialog();
//                                    ToastUtil.show(getString(R.string.earn_not_enable));
//                                    LogUtil.e("edAmount2=====", "客专不足");
//                                    return;
//                                }
                                if (StringUtils.subZeroAndDot(edAmount.getText().toString()).equals("0")) {
                                    dismissDialog();
                                    ToastUtil.show(getString(R.string.str_send_amount_can_not));
                                    return;
                                }
                            }
                            showSureSendDialog();
                            LogUtil.e("ethBalanceForToken==", ethBalanceForToken + "" + "," + slow);
                        } else {
                            if (currency.toUpperCase().equals("LTC")) {
                                if (Double.valueOf(edAmount.getText().toString()) < 0.0006 && !isToken) {
                                    dismissDialog();
                                    ToastUtil.show(getString(R.string.most_amount_enable));
                                    return;
                                }
                            } else {
                                if (currency.toUpperCase().equals("ETH") || currency.toUpperCase().equals("ETC")) {
                                    if (StringUtils.subZeroAndDot(edAmount.getText().toString()).equals("0")) {
                                        dismissDialog();
                                        ToastUtil.show(getString(R.string.str_send_amount_can_not));
                                        return;
                                    }
                                } else {
                                    if (Double.valueOf(edAmount.getText().toString()) < 0.00006 && !isToken) {
                                        dismissDialog();
                                        ToastUtil.show(getString(R.string.most_amount_more_enable));
                                        return;
                                    }
                                }
                            }

                            if (currentBalance == realityBalance && currentBalance < Double.valueOf(strEnable)) {
                                dismissDialog();
                                LogUtil.e("tvEnableBalance:", Double.valueOf(strEnable) + "");
                                ToastUtil.show(getString(R.string.earn_not_enable));
                                LogUtil.e("edAmount1=====", "客专不足");
                                return;
                            }
                            if (TextUtils.isEmpty(tvBtcAmount.getText().toString())) {
                                if (realityBalance > currentBalance && (currentBalance < (Double.valueOf(edAmount.getText().toString()) + 0))) {
                                    dismissDialog();
                                    ToastUtil.show(getString(R.string.trade_text_send));
                                    LogUtil.e("edAmount222=====", "这里转回异常1");
                                    return;
                                }
                            } else {
                                if (realityBalance > currentBalance && (currentBalance < (Double.valueOf(edAmount.getText().toString()) + Double.valueOf(tvBtcAmount.getText().toString())))) {
                                    dismissDialog();
                                    ToastUtil.show(getString(R.string.trade_text_send));
                                    LogUtil.e("edAmount222=====", "这里转回异常2");
                                    return;
                                }
                            }
                            if (Double.valueOf(edAmount.getText().toString()) > realityBalance) {
                                dismissDialog();
                                ToastUtil.show(getString(R.string.earn_not_enable));
                                LogUtil.e("edAmount2=====", "客专不足");
                                return;
                            }
                            showSureSendDialog();
                        }
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && data != null) {
            String codestring = data.getStringExtra("result");
            if (!TextUtils.isEmpty(codestring) && !TextUtils.isEmpty(currency)) {
                RequestSyncAddress(codestring, currency);
            }
        }
    }

    private void RequestCheckAddress(String address, String currency) {
        getWalletBalanceProviderServices.requestCheckBtnAddressProvider(new CheckAddressReqParam(address, currency))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(
                new RxSubscriber<Boolean>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.server_connection_error));
                        if (0 == ex.getState()) {
                            edAccount.setText(address);
                            ToastUtil.show(getString(R.string.address_not_true));
                        }
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        dismissDialog();
                        if (!aBoolean) {//判断
                            edAccount.setText(address);
                            ToastUtil.show(getString(R.string.address_not_true));
                        } else {
                            if (!TextUtils.isEmpty(edAmount.getText().toString())) {
                                if (isToken) return;
                                RequestReFree(currency);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_cancel:
                if (dialog1 != null && dialog1.isShowing()) dialog1.dismiss();
                break;
            case R.id.tv_activity_update:
                openAppSetting();
                if (dialog1 != null && dialog1.isShowing()) dialog1.dismiss();
                break;
            case R.id.mr_back_layout://
                dismissDialog();
                if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                break;
            case R.id.new_mr_back_layout://new_mr_back_layout
                dismissDialog();
                if (dialogNewPin != null && dialogNewPin.isShowing()) dialogNewPin.dismiss();
                break;
            case R.id.btn_now_send:
                try {
                    LoginStatus bean = LoginUtils.getLoginBean(this);
                    if (bean != null) {
                        value = bean.getTicket();
                    }
                    String touchid = ACacheUtil.get(SendCurrencyActivity.this).getAsString(bean.getId() + "");
                    if ((!TextUtils.isEmpty(touchid)) && touchid.equals("1")) {
                        showFragment();
                    } else {
                        initNewPinDialog();
//                        if (panel != null) {
//                            panel.clear();
//                        }
//                        if (mDialog == null) {
//                            mDialog = new AppCompatDialog(SendCurrencyActivity.this, R.style.KeyboardDialog);
//                            panel = new PayKeyboardView(getBaseContext());
//                            panel.setOnKeyboardListener(SendCurrencyActivity.this);
//                            panel.setTitle(getString(R.string.input_pin));
//                            //panel.setTitleForget(getString(R.string.tv_forget));
//                            mDialog.setContentView(panel);
//                            Window dialogWindow = mDialog.getWindow();
//                            //设置Dialog从窗体底部弹出
//                            dialogWindow.setGravity(Gravity.BOTTOM);
//                            //获得窗体的属性
//                            //将属性设置给窗体
//                            mDialog.setCanceledOnTouchOutside(false); // 外部点击取消
//                            //设置宽度为屏宽, 靠近屏幕底部。
//                            final Window window = mDialog.getWindow();
//                            //window.setWindowAnimations(R.style.AnimBottom);
//                            final WindowManager.LayoutParams lp = window.getAttributes();
//                            lp.gravity = Gravity.BOTTOM; // 紧贴底部
//                            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                            window.setAttributes(lp);
//                        }
//                        mDialog.show();
                    }
                } catch (Exception ex) {
                    LogUtil.e("Exception:", ex.getMessage());
                }
                break;
        }
    }

    /**
     * 打开设置页面
     */
    private void openAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onComplete(String one, String two, String three, String four, String five, String six) {
        payPass = one + two + three + four + five + six;
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        try {
            String mdAESstring = new AESUtil().encrypt(payPass);
            String pass = new AESUtil().decrypt(mdAESstring);
            progress.setVisibility(View.VISIBLE);
            RequestSendTrade(currency, pass);
        } catch (Exception ex) {
            LogUtil.e("onComplete:", ex.getMessage());
        }
    }

    private void RequestSendTrade(String currency, String pay) {
        LoginStatus bean = LoginUtils.getLoginBean(mCtx);
        getWalletBalanceProviderServices.requestSendTradeProvider(new SendTradeReqParam(tv_now_address.getText().toString(),
                tv_sure_amount, tv_now_common.getText().toString(), tv_now_only_free,
                currency, pay, bean.getDevice_id(), true))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<SendTradeBean>(this) {
            @Override
            protected void onError(ResponseThrowable ex) {
                if (icon != null) {
                    icon.setVisibility(View.INVISIBLE);
                }
                SendCurrencyActivity.this.dismissDialog();
                if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                if (dialogNewPin != null && dialogNewPin.isShowing()) dialogNewPin.dismiss();
                if (ex.getErrorCode().equals("0")) {
                    ToastUtil.show(getString(R.string.tv_zhuan_catch));
                } else {
                    ToastUtil.show(getString(R.string.server_connection_error));

                }
            }

            @Override
            public void onSuccess(SendTradeBean sendTradeBean) {
                SendCurrencyActivity.this.dismissDialog();
                if (icon != null) {
                    icon.setVisibility(View.INVISIBLE);
                }
                if (0 == sendTradeBean.getStatusCode()) {
                    PaySuccessFragment payDetailFragment = new PaySuccessFragment();
                    payDetailFragment.show(getSupportFragmentManager(), "PaySuccessFragment");
                    new Handler().postDelayed(() -> {
                        if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                        if (dialogNewPin != null && dialogNewPin.isShowing())
                            dialogNewPin.dismiss();
                    }, 500);
                    LogUtil.e("payDetailFragment==", payDetailFragment.isCancelable() + "");
                } else if (6 == sendTradeBean.getStatusCode()) {
                    if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                    if (dialogNewPin != null && dialogNewPin.isShowing()) dialogNewPin.dismiss();
                    ToastUtil.show(getString(R.string.address_not_true));
                } else if (102 == sendTradeBean.getStatusCode()) {
                    if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                    if (dialogNewPin != null && dialogNewPin.isShowing()) dialogNewPin.dismiss();
                    ToastUtil.show(getString(R.string.balance_enough));
                } else if (108 == sendTradeBean.getStatusCode() || 3 == sendTradeBean.getStatusCode() ||
                        102 == sendTradeBean.getStatusCode() || 107 == sendTradeBean.getStatusCode()) {
                    if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                    if (dialogNewPin != null && dialogNewPin.isShowing()) dialogNewPin.dismiss();
                    ToastUtil.show(getString(R.string.trade_text_send));
                } else if (10 == sendTradeBean.getStatusCode()) {
                    icon.setVisibility(View.INVISIBLE);
                    checkPinErrorHint();
                    ToastUtil.show(getString(R.string.pay_pass_word_error));
                } else if (802 == sendTradeBean.getStatusCode()) {
                    if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                    if (dialogNewPin != null && dialogNewPin.isShowing()) dialogNewPin.dismiss();
                    ToastUtil.show(getString(R.string.otc_no_finish_order));
                } else if (106 == sendTradeBean.getStatusCode()) {
                    if (dialogSure != null && dialogSure.isShowing()) {
                        dialogSure.dismiss();
                    }
                    ToastUtil.show(getString(R.string.eth_not_enough));//
                } else if (1 == sendTradeBean.getStatusCode()) {
                    if (dialogSure != null && dialogSure.isShowing()) {
                        dialogSure.dismiss();
                    }
                    ToastUtil.show(currency.toUpperCase() + getString(R.string.str_send_trading_prompt));
                } else {
                    if (dialogNewPin != null && dialogNewPin.isShowing()) dialogNewPin.dismiss();
                    if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                    ToastUtil.show(getString(R.string.tv_zhuan_catch));
                }
            }
        });

    }

    private void checkPinErrorHint() {//输入错误的震动提示
        Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400}; // 停止 开启 停止 开启
        //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次
        vibrate.vibrate(pattern, -1);
        passwordView.clearText();
        Animation shake = AnimationUtils.loadAnimation(SendCurrencyActivity.this, R.anim.shake);
        passwordView.startAnimation(shake);
    }

    private void RequestReFree(String currency) {
        if(dialog!=null){
            dialog.setBackPressEnable(false);
        }
        if (isToken) {
            mPresenter.requestReFree(new ReFeeReqParam("1"
                    , currency, ""));
        } else {
            mPresenter.requestReFree(new ReFeeReqParam(edAmount.getText().toString().trim()
                    , currency, ""));
        }

    }

    private void RequestNewReFree(String currency) {
        if(dialog!=null){
            dialog.setBackPressEnable(false);
        }
        mPresenter.requestNewReFree(new ReFeeReqParam(edAmount.getText().toString().trim()
                , currency, ""));

    }


    @Subscriber
    public void onPossInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 10) {
                if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
                EventBus.getDefault().post(new PosInfo(2020));
                SendCurrencyActivity.this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Subscriber
    public void onSaoInfo(EventBean posInfo) {
        try {
            if (posInfo == null) return;
            LogUtil.e("posInfo---", posInfo.getMsg());
            if (posInfo.getMsg().equals("finger")) return;
            showDialog();
            RequestSyncAddress(posInfo.getMsg(), currency);
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private void RequestSyncAddress(String address, String currency) {
        getWalletBalanceProviderServices.requestSyncAddressProvider(new ReqSyncAddressParam(address, currency))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        if (ex.getErrorCode().equals("0")) {
                            edAccount.setText(address);
                            ToastUtil.show(getString(R.string.address_not_true));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        edAccount.setText(s);
                        RequestCheckAddress(edAccount.getText().toString(), currency);
                    }
                });
    }

    @Subscriber
    public void onPanInfo(EventBean eventBean) {
        try {
            if (eventBean == null) return;
            if (eventBean.getMsg().equals("finger")) {
                if (panel != null) {
                    panel.clear();
                }
                initNewPinDialog();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Subscriber
    public void onPanConfirmInfo(EventBean eventBean) {
        try {
            if (eventBean == null) return;
            if (eventBean.getMsg().equals("fingerConfirm")) {
                if (dialogSure != null && dialogSure.isShowing()) dialogSure.dismiss();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public void onBack() {
        dismissDialog();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnSureSend.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnSureSend.setTextColor(getResources().getColor(R.color.color_a0a2b1));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().substring(0).equals(".")) {
            edAmount.setText("0" + s);
            edAmount.setSelection(2);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edAccount.getText().toString().equals("") || edAmount.getText().toString().equals(""))) {
            btnSureSend.setEnabled(true);
            btnSureSend.setTextColor(getResources().getColor(R.color.white));
        }
        if (edCommon.hasFocus()) {
            if (s.toString().length() == 50) {
                ToastUtil.show(getString(R.string.content_not_50));
            }
        }
    }

    @Override
    public void onSetPay() {
        intentFActivity(ForgetPayPassActivity.class);
        if (mDialog.isShowing() && mDialog != null) {
            mDialog.dismiss();
        }
        if (dialogSure.isShowing() && dialogSure != null) {
            dialogSure.dismiss();
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        ToastUtil.show(getString(R.string.server_connection_error));
    }

    @Override
    public void updateRequestSendFrom(SendFormReqAndResParam param) {
        dismissDialog();
        if (!TextUtils.isEmpty(param.getTag()) && param.getTag().equals("pending")) {//以太坊发币pending情况
            if (dialogSure != null && dialogSure.isShowing()) {
                dialogSure.dismiss();
            }
            ToastUtil.show(getString(R.string.tv_pedding_status));
        } else if (!TextUtils.isEmpty(param.getTag()) && param.getTag().equals("lackofmoney")) {
            if (dialogSure != null && dialogSure.isShowing()) {
                dialogSure.dismiss();
            }
            ToastUtil.show(getString(R.string.trade_text_send));
        } else if (802 == param.getStatusCode()) {
            if (dialogSure != null && dialogSure.isShowing()) {
                dialogSure.dismiss();
            }
            ToastUtil.show(getString(R.string.otc_no_finish_order));
        } else {//正常发币情况
            if (dialogSure != null && !dialogSure.isShowing()) {
                dialogSure.show();
                tv_amount_currency.setText(param.getAmount() + " " + param.getCurrency().toUpperCase());
                tv_sure_amount = param.getAmount();
                tv_now_address.setText(param.getToAddress());
                if (isToken) {
                    tv_now_free.setText(param.getFee() + " " + tokenCategory.toUpperCase());
                } else {
                    tv_now_free.setText(param.getFee() + " " + param.getCurrency().toUpperCase());
                }
                tv_now_only_free = param.getFee();
                tv_now_common.setText(edCommon.getText().toString().trim());
                if (tv_now_common.getLineCount() == 3) {
                    tv_now_common.setPadding(0, ScreenUtil.dp2px(14, SendCurrencyActivity.this), 0, 0);
                }
            }
        }
    }

    @Override
    public void updateRequestNewBalance(NewBalanceBean param) {
        if(dialog!=null){
            dialog.setBackPressEnable(true);
        }
        if (param != null) {
            currentBalance = param.getCurrentBalance();
            realityBalance = param.getRealityBalance();
            String amount = Utils.toSubStringDegist(param.getRealityBalance(), 4);
            if (tvEnableBalance != null) {
                tvEnableBalance.setText(amount);
            }
        }
    }

    @Override
    public void updateGetWalletList(List<WalletAssetBean> params) {

    }

    @Override
    public void updateRequestCheckBtnAddress(Boolean isOk) {
        if (isOk) {
            String strEnable = tvEnableBalance.getText().toString().replace(",", "");
            LogUtil.e("tvEnableBalance:", strEnable);
            if (isToken) {
                if (!TextUtils.isEmpty(tvSeekBalance.getText().toString())) {
                    if (ethBalanceForToken < Double.valueOf(slow)) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.eth_not_enough));
                        return;
                    }

                    if (StringUtils.subZeroAndDot(edAmount.getText().toString()).equals("0")) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.str_send_amount_can_not));
                        return;
                    }

//                    if (Double.valueOf(edAmount.getText().toString()) < 0.00006) {
//                        dismissDialog();
//                        ToastUtil.show(getString(R.string.most_amount_more_enable));
//                        return;
//                    }
                }
                showSureSendDialog();
                LogUtil.e("ethBalanceForToken==", ethBalanceForToken + "" + "," + slow);
            } else {
//                if (Double.valueOf(edAmount.getText().toString()) < 0.0006) {
//                    dismissDialog();
//                    ToastUtil.show(getString(R.string.most_amount_enable));
//                    return;
//                }
                if (currency.toUpperCase().equals("LTC")) {
                    if (Double.valueOf(edAmount.getText().toString()) < 0.0006 && !isToken) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.most_amount_enable));
                        return;
                    }
                } else {
                    if (currency.toUpperCase().equals("ETH") || currency.toUpperCase().equals("ETC")) {
                        if (StringUtils.subZeroAndDot(edAmount.getText().toString()).equals("0")) {
                            dismissDialog();
                            ToastUtil.show(getString(R.string.str_send_amount_can_not));
                            return;
                        }
                    } else {
                        if (Double.valueOf(edAmount.getText().toString()) < 0.00006 && !isToken) {
                            dismissDialog();
                            ToastUtil.show(getString(R.string.most_amount_more_enable));
                            return;
                        }
                    }
                }
                if (currentBalance == realityBalance && currentBalance < Double.valueOf(strEnable)) {
                    dismissDialog();
                    LogUtil.e("tvEnableBalance:", Double.valueOf(strEnable) + "");
                    ToastUtil.show(getString(R.string.earn_not_enable));
                    LogUtil.e("edAmount1=====", "客专不足");
                    return;
                }
                if (TextUtils.isEmpty(tvBtcAmount.getText().toString())) {
                    if (realityBalance > currentBalance && (currentBalance < (Double.valueOf(edAmount.getText().toString()) + 0))) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.trade_text_send));
                        return;
                    }
                } else {
                    if (realityBalance > currentBalance && (currentBalance < (Double.valueOf(edAmount.getText().toString()) + Double.valueOf(tvBtcAmount.getText().toString())))) {
                        dismissDialog();
                        ToastUtil.show(getString(R.string.trade_text_send));
                        return;
                    }
                }
                if (Double.valueOf(edAmount.getText().toString()) > realityBalance) {
                    dismissDialog();
                    ToastUtil.show(getString(R.string.earn_not_enable));
                    LogUtil.e("edAmount2=====", "客专不足");
                    return;
                }
                showSureSendDialog();
            }
        } else {
            dismissDialog();
            ToastUtil.show(getString(R.string.address_not_true));
        }
    }

    @Override
    public void updateRequestSendTrade(SendTradeBean param) {

    }

    @Override
    public void updateRequestReFree(ReFeeBean param) {
        if(dialog!=null){
            dialog.setBackPressEnable(true);
        }
        if (param == null) return;
        slow = param.getSlow();
        fast = param.getFastFee();
        veryFast = param.getVeryFastFee();
        if (tvBtcAmount!=null){
            tvBtcAmount.setText(StringUtils.subZeroAndDot(param.getFastFee()));
        }
        if (tvSeekBalance!=null){
            tvSeekBalance.setText(StringUtils.subZeroAndDot(param.getFastFee()));
        }
        if (param.getFastFee().equals(param.getVeryFastFee())) {
            seekBar.setProgress(1);
        } else {
            if (tvSeekBalance.getText().toString().equals(slow)) {
                seekBar.setProgress(0);
            }
            if (tvSeekBalance.getText().toString().equals(fast)) {
                seekBar.setProgress(1);
            }
            if (tvSeekBalance.getText().toString().equals(veryFast)) {
                seekBar.setProgress(2);
            }
        }
    }

    @Override
    public void updateRequestNewReFree(ReFeeBean param) {
        if(dialog!=null){
            dialog.setBackPressEnable(true);
        }
        if (param == null) return;
        if (isDestroyed() || isFinishing()) return;//如果activity不存在 那么直接返回
        slow = param.getSlow();
        fast = param.getFastFee();
        veryFast = param.getVeryFastFee();
        if (tvBtcAmount!=null){
            tvBtcAmount.setText(StringUtils.subZeroAndDot(param.getFastFee()));
        }
        if (tvSeekBalance!=null){
            tvSeekBalance.setText(StringUtils.subZeroAndDot(param.getFastFee()));
        }
        if (param.getFastFee().equals(param.getVeryFastFee())) {
            seekBar.setProgress(1);
        } else {
            if (tvSeekBalance.getText().toString().equals(slow)) {
                seekBar.setProgress(0);
            }
            if (tvSeekBalance.getText().toString().equals(fast)) {
                seekBar.setProgress(1);
            }
            if (tvSeekBalance.getText().toString().equals(veryFast)) {
                seekBar.setProgress(2);
            }
        }

        if (!TextUtils.isEmpty(edAccount.getText().toString())) {
            RequestCheckBtnAddress(edAccount.getText().toString(), tvEnableCurrency.getText().toString().toLowerCase());
        }

    }

    private void hideSoftKeyBoard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setSubView() {
        //设置键盘
        pin_keyboard.setKeyboardKeys(KEY);
    }

    @Override
    public void passwordChange(String changeText) {

    }

    @Override
    public void passwordComplete() {
        UIUtils.postDelayed(() -> {
            try {
                icon.setVisibility(View.VISIBLE);
                String mdAESstring = new AESUtil().encrypt(payPass);
                String pass = new AESUtil().decrypt(mdAESstring);
                RequestSendTrade(currency, pass);
            } catch (Exception ex) {
                LogUtil.e("onComplete:", ex.getMessage());
            }
        }, 500);
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {

    }

    @Override
    public void getPass(String pass, boolean isCom) {
        payPass = pass;
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new PosInfo(2020));
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("onPause----", "onParse");
    }

    @Subscriber
    public void refrestrSendPinList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 333) {
            showFragment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogSure != null) dialogSure.dismiss();
        if (dialogNewPin != null) dialogNewPin.dismiss();
        if (mAnimation != null) {
            mAnimation.stop();
        }
    }
}
