package com.idcw.pay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.AESUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ExceptionHandle;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.idcw.pay.iprovider.GetAccountAddressServices;
import com.idcw.pay.iprovider.SecurityPayServices;
import com.idcw.pay.iprovider.SecurityPayValidAddressServices;
import com.idcw.pay.model.bean.SecurityPayBean;
import com.idcw.pay.model.param.PayValidAddressReqParam;
import com.idcw.pay.model.param.SecurityPayReqParam;
import com.idcw.pay.utils.IDCWPayBackUtils;
import com.idcw.pay.widget.PayErrorInfoDialog;
import com.idcw.pay.widget.PayOpenPhotoPopWindow;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuelianx.fingerlib.FingerFragment;

import cn.com.epsoft.keyboard.basic.NewPinDialog;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.IDCWDataBean;
import foxidcw.android.idcw.common.utils.IDCWPayJumpUtils;
import foxidcw.android.idcw.common.utils.CommonAnimUtils;
import foxidcw.android.idcw.common.widget.ClearableEditText;
import foxidcw.android.idcw.foxcommon.Constants.Constants;
import foxidcw.android.idcw.foxcommon.provider.services.PinDialogProviderServices;

import static com.idcw.pay.IDCWPayActivity.BALANCE_ENOUGH;
import static com.idcw.pay.IDCWPayActivity.NET_ERROR;
import static com.idcw.pay.IDCWPayActivity.PAYING_WAIT;
import static com.idcw.pay.IDCWPayActivity.PAY_TIMEOUT;
import static com.idcw.pay.IDCWPayActivity.PIN_ERROR;
import static com.idcw.pay.IDCWPayActivity.RE_PAY;
import static com.idcw.pay.IDCWPayActivity.SERVER_ERROR;
import static com.idcw.pay.widget.PayErrorInfoDialog.ERROR_INFO_STATE_LEFT;

@Route(path = Constants.IDCW_WITH_DRAW, name = "提现界面")
public class IDCWWithDrawActivity extends BaseWalletActivity implements View.OnClickListener, PayErrorInfoDialog.OnPayErrorInfoListener, PinDialogProviderServices.PinResultStr {

    // 第一步容器
    private LinearLayout mContainerOneLl;
    // 关闭
    private ImageButton mCloseIb;
    // 数量
    private TextView mQuantityTv;
    // 粘贴
    private TextView mCopyTv;
    // 扫一扫
    private TextView mScanTv;
    // 输入地址
    private ClearableEditText mInputAddrEt;
    // 提取
    private Button mExtraBtn;

    // 第二步容器
    private LinearLayout mContainerTwoLl;
    // 返回
    private ImageButton mBackIb;
    // 数量
    private TextView mAmountTwoTv;
    // 地址
    private TextView mAddressTwoTv;
    // 确认提取
    private Button mConfirmBtn;

    // 打开设置界面
    private PayOpenPhotoPopWindow mPayOpenPhotoPopWindow = null;
    // 默认为false
    private boolean mIsConfirmEnable = false;
    // 缓存的参数
    private IDCWDataBean reqData = null;
    // 缓存当前地址信息
    private String mCurrentAddress = null;
    // 是否来自欢迎页面
    private boolean isFromWelcome = false;
    // 处理回调类
    private IDCWPayBackUtils mIDCWPayBackUtils;

    @Autowired
    SecurityPayServices mSecurityPayServices;

    @Autowired
    SecurityPayValidAddressServices mSecurityPayValidAddressServices;

    @Autowired
    GetAccountAddressServices mGetAccountAddressServices;

    // 缓存登录
    private LoginStatus mLoginStatus = null;

    @Override
    protected int getLayoutId() {
        dismissAllDialog();
        return R.layout.activity_idcw_with_draw;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        dismissAllDialog();
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        dialog.setBackPressEnable(false);
        if (getIntent().hasExtra(IDCWPayJumpUtils.JUMP_FROM_WELCOME_ACTIVITY)) {
            isFromWelcome = getIntent().getBooleanExtra(IDCWPayJumpUtils.JUMP_FROM_WELCOME_ACTIVITY, false);
        }
        // 第一个
        mContainerOneLl = findViewById(R.id.idcw_with_draw_container_one_ll);
        mCloseIb = findViewById(R.id.idcw_with_draw_close_ib);
        mQuantityTv = findViewById(R.id.idcw_with_draw_quantity_tv);
        mCopyTv = findViewById(R.id.idcw_with_draw_copy_address_tv);
        mScanTv = findViewById(R.id.idcw_with_draw_scan_tv);
        mInputAddrEt = findViewById(R.id.idcw_with_draw_input_tv);
        mExtraBtn = findViewById(R.id.idcw_with_draw_extra_btn);

        // 第二个
        mContainerTwoLl = findViewById(R.id.idcw_with_draw_container_two_ll);
        mBackIb = findViewById(R.id.idcw_with_draw_back_ib);
        mAmountTwoTv = findViewById(R.id.idcw_with_draw_quantity_two_tv);
        mAddressTwoTv = findViewById(R.id.idcw_with_draw_address_two_tv);
        mConfirmBtn = findViewById(R.id.idcw_with_draw_confirm_btn);

        mLoginStatus = (LoginStatus) ACacheUtil.get(mCtx).getAsObject(AcacheKeys.loginbean);
    }

    /**
     * 隐藏掉所有弹框
     */
    private void dismissAllDialog() {
        if (null != mPayErrorInfoDialog) {
            mPayErrorInfoDialog.dismiss();
        }
        if (null != fingerFragment) {
            fingerFragment.dismiss();
        }
        dismissPinDialog();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();
        // 获取参数并且赋值
        reqData = IDCWPayJumpUtils.getCacheParamBean(IDCWPayJumpUtils.getCacheParamData());
        if (null != reqData) {
            String mAmountData = String.valueOf(reqData.getCurrency().toUpperCase() + " " +
                    Utils.toSubStringDegist(Double.parseDouble(reqData.getAmount()), 4));
            mQuantityTv.setText(mAmountData);
            mAmountTwoTv.setText(mAmountData);

            mIDCWPayBackUtils = new IDCWPayBackUtils(mCtx, reqData.getAppId());

            /**
             * 获取币种对应的个人地址
             */
            mGetAccountAddressServices.getAccountAddress(reqData.getCurrency())
                    .compose(bindToLifecycle())
                    .subscribeWith(new RxProgressSubscriber<String>(this) {
                        @Override
                        public void onSuccess(String data) {
                            if (!TextUtils.isEmpty(data))
                                setAddressUI(data);
                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
                        }
                    });
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onEvent() {
        mCloseIb.setOnClickListener(this);
        mCopyTv.setOnClickListener(this);
        mScanTv.setOnClickListener(this);
        mExtraBtn.setOnClickListener(this);

        mBackIb.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

        /**
         * 让后面UI的地址信息跟随界面进行变动
         */
        RxTextView.textChanges(mInputAddrEt)
                .subscribe(charSequence -> {
                    mCurrentAddress = String.valueOf(charSequence);
                    mAddressTwoTv.setText(mCurrentAddress);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.idcw_with_draw_close_ib) {
            // 关闭
            onBackPressed();
        } else if (viewId == R.id.idcw_with_draw_copy_address_tv) {
            // 粘贴
            try {
                ClipboardManager paste = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                setAddressUI(paste.getText().toString().trim());
            } catch (Exception ex) {
            }
        } else if (viewId == R.id.idcw_with_draw_scan_tv) {
            // 扫码
            new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            ARouter.getInstance().build(ArouterConstants.SAOCODE).navigation(this, 11);
                        } else {
                            if (null == mPayOpenPhotoPopWindow) {
                                mPayOpenPhotoPopWindow = new PayOpenPhotoPopWindow(mCtx);
                                mPayOpenPhotoPopWindow.setClickListener(type -> {
                                    if (type == 1) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    }
                                }).showPopupWindow();
                            }
                        }
                    });
        } else if (viewId == R.id.idcw_with_draw_extra_btn) {
            // 确认提取
            if (TextUtils.isEmpty(mCurrentAddress)) {
                ToastUtil.show(getString(R.string.please_input_add));
                return;
            }
            PayValidAddressReqParam reqParam = new PayValidAddressReqParam();
            reqParam.setAddress(mCurrentAddress);
            reqParam.setCurrency(reqData.getCurrency());
            // 校验地址合法性
            mSecurityPayValidAddressServices.validAddress(reqParam)
                    .compose(bindToLifecycle())
                    .subscribeWith(new RxProgressSubscriber<Boolean>(this) {
                        @Override
                        public void onSuccess(Boolean data) {
                            if (!data) {
                                ToastUtil.show(getString(R.string.address_not_true));
                                return;
                            }
                            CommonAnimUtils.leftExitRightEnterAnimation(mContainerOneLl, mContainerTwoLl);
                            mIsConfirmEnable = true;
                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
                            if (0 == ex.getState()) {
                                ToastUtil.show(getString(R.string.address_not_true));
                                return;
                            }
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    });
        } else if (viewId == R.id.idcw_with_draw_back_ib) {
            // 返回
            CommonAnimUtils.LeftEnterRightExitAnimation(mContainerOneLl, mContainerTwoLl);
            mIsConfirmEnable = false;
        } else if (viewId == R.id.idcw_with_draw_confirm_btn) {
            // 确认提取 先验证pin
            showPayPassDialog();
        }
    }

    /**
     * 提示输入pin
     */
    private void showPayPassDialog() {
        try {
            String touchId = ACacheUtil.get(mCtx).getAsString(String.valueOf(mLoginStatus.getId()));
            if (!TextUtils.isEmpty(touchId) && touchId.equals("1")) {
                showTouchFragment();
            } else {
                showPinDialog();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 指纹验证
    private FingerFragment fingerFragment;

    /**
     * 弹出指纹验证
     */
    private void showTouchFragment() {
        try {
            fingerFragment = new FingerFragment();
            fingerFragment.show(getFragmentManager(), "fingerFragment");
            fingerFragment.setCancelable(false);
            fingerFragment.setmFragmentCallBack(new FingerFragment.Callback() {
                @Override
                public void onSuccess() {
                    try {
                        String pass = ACacheUtil.get(mCtx).getAsString(mLoginStatus.getUser_name() + "");
                        final String pay = new AESUtil().decrypt(pass);
                        confirmWithDraw(pay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {
                    showPinDialog();
                }
            });
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getStackTrace().toString());
            showPayStateDialog(SERVER_ERROR); // 服务器正忙
        }
    }

    /**
     * 弹出输入pin界面
     */
    private NewPinDialog mNewPinDialog;

    private void showPinDialog() {
        if (null == mNewPinDialog) {
            mNewPinDialog = new NewPinDialog(mCtx);
            mNewPinDialog.setNewPassWordListener(this);
        }
        mNewPinDialog.show();
    }

    @Override
    public void onPinResultStr(String result) {
        try {
            confirmWithDraw(result);
        } catch (Exception e) {
            e.printStackTrace();
            showPayStateDialog(SERVER_ERROR); // 服务器正忙
        }
    }

    /**
     * 第二步  确认提现
     */
    @SuppressLint("CheckResult")
    private void confirmWithDraw(String pass) {
        dismissPinDialog();
        // 提示输入pin
        try {
            SecurityPayReqParam reqParam = new SecurityPayReqParam();
            reqParam.setComment(reqData.getTrans_id());
            reqParam.setSign(reqData.getSign());
            reqParam.setPayPassword(pass);
            reqParam.setTrans_id(reqData.getTrans_id());
            reqParam.setAmount(reqData.getAmount());
            reqParam.setFee(String.valueOf(0));
            reqParam.setToAddress(mCurrentAddress);
            reqParam.setCurrency(reqData.getCurrency());
            reqParam.setNotify_url(reqData.getNotify_url());
            reqParam.setTime_span(reqData.getTime_span());
            reqParam.setDevice_id(LoginUtils.getLoginBean(mCtx).getDevice_id());
            reqParam.setNewVersion(true);
            reqParam.setAppid(reqData.getAppId());
            reqParam.setType(String.valueOf(1));
            mSecurityPayServices.securityPay(reqParam)
                    .compose(bindToLifecycle())
                    .subscribeWith(new RxProgressSubscriber<SecurityPayBean>(this) {
                        @Override
                        public void onSuccess(SecurityPayBean data) {
                            if (null == data) {
                                showPayStateDialog(SERVER_ERROR); // 服务器正忙
                                return;
                            }
                            switch (data.getStatusCode()) {
                                case 0:
                                    goToBackThirdParty();
                                    break;
                                case 106:
                                    ToastUtil.show(getString(R.string.str_pay_no_withdraw));
                                    break;
                                case 10:
                                    showPayStateDialog(PIN_ERROR); // pin码错误
                                    break;
                                case 3:
                                case 102:
                                    showPayStateDialog(BALANCE_ENOUGH); // 余额不足
                                    break;
                                case 606:
                                    showPayStateDialog(PAYING_WAIT); // 转回确认中
                                    break;
                                case 607:
                                    showPayStateDialog(RE_PAY); // 重复提现
                                    break;
                                default:
                                    showPayStateDialog(SERVER_ERROR); // 服务器正忙
                                    break;
                            }
                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
                            switch (ex.getErrorCode()) {
                                case ExceptionHandle.ERROR.NETWORD_ERROR:
                                    showPayStateDialog(NET_ERROR);
                                    break;
                                case ExceptionHandle.ERROR.TIMEOUT_ERROR:
                                    showPayStateDialog(PAY_TIMEOUT);
                                    break;
                                default:
                                    showPayStateDialog(SERVER_ERROR);
                                    break;
                            }
                        }
                    });
        } catch (Exception e) {
            showPayStateDialog(SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private void dismissPinDialog() {
        if (mNewPinDialog != null && mNewPinDialog.isShowing()) {
            mNewPinDialog.dismiss();
        }
    }

    private PayErrorInfoDialog mPayErrorInfoDialog;

    /**
     * 处理错误提示
     *
     * @param type
     */
    public void showPayStateDialog(int type) {
        try {
            int titleState = 0;
            int leftText = R.string.txt_back;
            int rightText = R.string.retry;
            switch (type) {
                case PIN_ERROR: // Pin码不正确
                    titleState = R.string.pin_not_right;
                    leftText = R.string.txt_back;
                    rightText = R.string.retry_input;
                    break;
                case BALANCE_ENOUGH:  // 余额不足
                    titleState = R.string.balance_enough;
                    rightText = 0;
                    break;
                case PAY_TIMEOUT:  // 支付超时
                    titleState = R.string.pay_str_times_up_wirh_draw;
                    break;
                case NET_ERROR:  // 网络连接失败，请检查网络后重试
                    titleState = R.string.net_work_error;
                    break;
                case SERVER_ERROR:  // 服务器异常
                    titleState = R.string.server_error;
                    break;
                case PAYING_WAIT:  // 转汇确认中
                    titleState = R.string.paying_now;
                    rightText = 0;
                    leftText = R.string.tv_sure;
                    break;
                case RE_PAY: // 重复支付
                    titleState = R.string.pay_str_re_wirh_draw;
                    rightText = 0;
                    leftText = R.string.txt_back;
                    break;
                default:
                    break;
            }
            mPayErrorInfoDialog = new PayErrorInfoDialog(mCtx)
                    .setPositiveButtonTextWithId(leftText)
                    .setNegativeButtonTextWithId(rightText)
                    .setTitleWithRes(titleState)
                    .setType(type)
                    .setOnPayErrorInfoListener(this);
            mPayErrorInfoDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onButtonClick(int state, int type) {
        switch (type) {
            case PIN_ERROR:
                // Pin码不正确
                if (state == ERROR_INFO_STATE_LEFT) {
                    goToBackThirdParty();
                } else {
                    showPayPassDialog();
                }
                break;
            case BALANCE_ENOUGH:
                // 余额不足
                goToBackThirdParty();
                break;
            case PAY_TIMEOUT:
            case NET_ERROR:
            case SERVER_ERROR:
                // 服务器异常
                if (state == ERROR_INFO_STATE_LEFT) {
                    goToBackThirdParty();
                } else {
                    showPayPassDialog();
                }
                break;
            case PAYING_WAIT:
                // 转汇确认中
                goToBackThirdParty();
                break;
            case RE_PAY: // 重复提现
                goToBackThirdParty();
                break;
            default:
                break;
        }
    }

    private void goToBackThirdParty() {
        mIsConfirmEnable = false;
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        /**
         * 如果当前在第二个界面  那么返回上一个界面
         */
        if (mIsConfirmEnable) {
            mIsConfirmEnable = false;
            CommonAnimUtils.LeftEnterRightExitAnimation(mContainerOneLl, mContainerTwoLl);
            return;
        }
        IDCWPayJumpUtils.clearCacheParam();
        // true 代表mainac存在  false 代表不存在
        if (null != mIDCWPayBackUtils) {
            if (!isFromWelcome) {
                mIDCWPayBackUtils.IDCWPayBackToPin(this);
            } else {
                mIDCWPayBackUtils.iDCWPayBackToMerchantsWithId();
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        IDCWPayJumpUtils.clearCacheParam();
        super.onDestroy();
    }

    /**
     * 设置地址相关
     *
     * @param address
     */
    private void setAddressUI(String address) {
        if (!TextUtils.isEmpty(address)) {
            int length = address.length();
            mInputAddrEt.setText(address);
            mInputAddrEt.setSelection(length);
        } else {
            mInputAddrEt.setText(address);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && data != null) {
            String address = data.getStringExtra("result");
            String regex = "([:=&?\\-+*%#@()])+";
            if (address.matches(regex)) {
                PayValidAddressReqParam reqParam = new PayValidAddressReqParam();
                reqParam.setAddress(address);
                reqParam.setCurrency(reqData.getCurrency());
                mSecurityPayValidAddressServices.validComplicatedAddress(reqParam)
                        .compose(bindToLifecycle())
                        .subscribeWith(new RxProgressSubscriber<String>(this) {
                            @Override
                            public void onSuccess(String data) {
                                setAddressUI(data);
                            }

                            @Override
                            protected void onError(ResponseThrowable ex) {
                                if (ex.getErrorCode().equals("0")) {
                                    ToastUtil.show(getString(R.string.address_not_true));
                                } else {
                                    ToastUtil.show(getString(R.string.server_connection_error));
                                }
                            }
                        });
            } else {
                setAddressUI(address);
            }
        }
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected BaseView getView() {
        return null;
    }
}
