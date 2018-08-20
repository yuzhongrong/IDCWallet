package com.idcw.pay;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.AESUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ExceptionHandle;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.idcw.pay.iprovider.SecurityPayCustomerInfoServices;
import com.idcw.pay.iprovider.SecurityPayServices;

import foxidcw.android.idcw.common.model.bean.IDCWDataBean;

import com.idcw.pay.model.bean.CustomerInfoBean;
import com.idcw.pay.model.bean.SecurityPayBean;
import com.idcw.pay.model.param.SecurityPayReqParam;
import com.idcw.pay.utils.IDCWPayBackUtils;
import com.xuelianx.fingerlib.FingerFragment;
import com.xuelianx.fingerlib.bean.EventBean;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.lang.ref.WeakReference;

import cn.com.epsoft.keyboard.basic.NewPinDialog;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.IDCWPayJumpUtils;
import foxidcw.android.idcw.foxcommon.Constants.Constants;
import foxidcw.android.idcw.foxcommon.provider.services.PinDialogProviderServices;

import static com.idcw.pay.utils.IDCWPayBackUtils.IDCW_DEFAULT_APPID;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/26 17:15
 **/
@Route(path = Constants.IDCW_PAY, name = "支付界面")
public class IDCWPayActivity extends BaseWalletActivity implements View.OnClickListener, PinDialogProviderServices.PinResultStr {

    public static final int PIN_ERROR = 1; // Pin码不正确
    public static final int BALANCE_ENOUGH = 2; // 余额不足
    public static final int PAY_TIMEOUT = 3;// 支付超时
    public static final int NET_ERROR = 4;// 网络连接失败，请检查网络后重试
    public static final int SERVER_ERROR = 5;// 服务器异常
    public static final int PAYING_WAIT = 6; // 转汇确认中
    public static final int RE_PAY = 7; // 重复支付
    public static final int SERVER_BUSY = 8; // 服务器正忙

    public static final int LEFT_BTN_TYPE = 1; // 左边按钮
    public static final int RIGHT_BTN_TYPE = 2; // 右边按钮

    private LoginStatus mLoginStatus = null; // 缓存登录

    private IDCWDataBean reqData = null;

    private NewPinDialog mNewPinDialog;
    private TextView mTvCurrencyQuantity; // 显示币种拼接数量
    private TextView mCompanyNameTv; // 显示公司名称

    @Autowired
    SecurityPayServices mSecurityPayServices;

    @Autowired
    SecurityPayCustomerInfoServices mSecurityPayCustomerInfoServices;

    private IDCWPayBackUtils mIDCWPayBackUtils;

    private boolean isFromWelcome = false;

    @Override
    protected void onNewIntent(Intent intent) {
        dismissAllDialog();
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected int getLayoutId() {
        dismissAllDialog();
        return R.layout.activity_idcw_pay;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (getIntent().hasExtra(IDCWPayJumpUtils.JUMP_FROM_WELCOME_ACTIVITY)) {
            isFromWelcome = getIntent().getBooleanExtra(IDCWPayJumpUtils.JUMP_FROM_WELCOME_ACTIVITY, false);
        }
        setContentView(R.layout.activity_idcw_pay);
        dialog.setBackPressEnable(false);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        mTvCurrencyQuantity = findViewById(R.id.tv_currency_quantity);
        mCompanyNameTv = findViewById(R.id.tv_accept_addr);
        mLoginStatus = (LoginStatus) ACacheUtil.get(mCtx).getAsObject(AcacheKeys.loginbean);
    }

    private void dismissAllDialog() {
        if (null != fingerFragment) {
            fingerFragment.dismiss();
        }
        if (null != mDialog) {
            mDialog.dismiss();
        }
        dismissPinDialog();
    }


    @Override
    protected void onEvent() {
        findViewById(R.id.id_ll_ac_idcw_pay_close).setOnClickListener(this);
        findViewById(R.id.btn_pay).setOnClickListener(this);
    }

    @Override
    protected boolean isCheckVersion() {
        return false;
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();
        // 获取参数并且赋值
        reqData = IDCWPayJumpUtils.getCacheParamBean(IDCWPayJumpUtils.getCacheParamData());
        if (reqData != null) {
            mIDCWPayBackUtils = new IDCWPayBackUtils(this, reqData.getAppId());
            mTvCurrencyQuantity.setText(String.valueOf(reqData.getCurrency().toUpperCase() + " " +
                    Utils.toSubStringDegist(Double.parseDouble(reqData.getAmount()), 4)));

            mSecurityPayCustomerInfoServices.getCustomerInfo(reqData.getAppId(), AppLanguageUtils.getLanguageLocalCode(this))
                    .compose(bindToLifecycle()) // 重试两次
                    .retry(1)
                    .subscribeWith(new RxSubscriber<CustomerInfoBean>() {
                        @Override
                        public void onSuccess(CustomerInfoBean customerInfoBean) {
                            mCompanyNameTv.setText(customerInfoBean.getCompanyName());
                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
                        }
                    });
        }
    }

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

    private void showPinDialog() {
        if (null == mNewPinDialog) {
            mNewPinDialog = new NewPinDialog(mCtx);
            mNewPinDialog.setNewPassWordListener(this);
        }
        mNewPinDialog.show();
    }

    private FingerFragment fingerFragment;

    /**
     * 显示指纹输入的界面
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
                        pay(pay);
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
        }
    }

    @Subscriber
    public void onPanInfo(EventBean eventBean) {
        try {
            if (eventBean == null) return;
            if (eventBean.getMsg().equals("finger")) {
                showPinDialog();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        IDCWPayJumpUtils.clearCacheParam();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
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

    @SuppressLint("CheckResult")
    private void pay(String pass) {
        dismissPinDialog();
        try {
            SecurityPayReqParam reqParam = new SecurityPayReqParam();
            reqParam.setComment(reqData.getTrans_id());
            reqParam.setSign(reqData.getSign());
            reqParam.setPayPassword(pass);
            reqParam.setTrans_id(reqData.getTrans_id());
            reqParam.setAmount(reqData.getAmount());
            reqParam.setFee(String.valueOf(0));
            reqParam.setToAddress(" ");
            reqParam.setCurrency(reqData.getCurrency());
            reqParam.setNotify_url(reqData.getNotify_url());
            reqParam.setTime_span(reqData.getTime_span());
            reqParam.setDevice_id(mLoginStatus.getDevice_id());
            reqParam.setNewVersion(true);
            // 不是瑞士会的才传appid
            if (!IDCW_DEFAULT_APPID.equals(reqData.getAppId())) {
                reqParam.setAppid(reqData.getAppId());
                reqParam.setType(String.valueOf(0));
            }
            mSecurityPayServices.securityPay(reqParam)
                    .compose(bindToLifecycle())
                    .subscribeWith(new RxProgressSubscriber<SecurityPayBean>(this) {
                        @Override
                        public void onSuccess(SecurityPayBean data) {
                            if (null == data) {
                                showPayStateDialog(SERVER_ERROR);
                                return;
                            }
                            switch (data.getStatusCode()) {
                                case 0:
                                    onBackPressed();
                                    break;
                                case 106:
                                    ToastUtil.show(getString(R.string.eth_not_enough));
                                    break;
                                case 3:
                                case 102:
                                    showPayStateDialog(BALANCE_ENOUGH); // 余额不足
                                    break;
                                case 10:
                                    showPayStateDialog(PIN_ERROR); // pin码错误
                                    break;
                                case 604:
                                case 605:
                                    showPayStateDialog(SERVER_ERROR); // 服务器正忙
                                    break;
                                case 606:
                                    showPayStateDialog(PAYING_WAIT); // 转回确认中
                                    break;
                                case 607:
                                    showPayStateDialog(RE_PAY); // 重复支付
                                    break;
                                case 802:
                                    ToastUtil.show(getString(R.string.otc_no_finish_order));//您有未完成的OTC订单
                                    break;
                                case -32600:
                                case -32601:
                                case -32602:
                                case -32700:
                                    showPayStateDialog(SERVER_ERROR);// 参数错误
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

    private Dialog mDialog;

    /**
     * 弹出支付结果显示
     *
     * @param /1   Pin码不正确   2 余额不足  3 支付超时  4 网络连接失败，请检查网络后重试 5 服务器异常
     * @param /1   重新输入  2、3、4、5 返回
     * @param /1   忘记Pin  2 隐藏该按钮  3、4、5 重试
     * @param type 1  Pin码不正确   2 余额不足  3 支付超时  4 网络连接失败，请检查网络后重试 5 服务器异常
     */
    private void showPayStateDialog(int type) {
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
                    titleState = R.string.pay_time_out;
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
                    titleState = R.string.re_pay;
                    rightText = 0;
                    leftText = R.string.txt_back;
                    break;
                case SERVER_BUSY:  // 服务器正忙
                    titleState = R.string.server_connection_error;
                    rightText = 0;
                    leftText = R.string.tv_sure;
                    break;
                default:
                    break;
            }
            View rootView = getLayoutInflater().inflate(R.layout.activity_third_party_pay_state, null);
            mDialog = new Dialog(mCtx, R.style.dialog);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setContentView(rootView);
            mDialog.setOnDismissListener(dialog1 -> {
            });
            mDialog.show();
            TextView mTvStateInfo = rootView.findViewById(R.id.id_tv_third_pay_state_info);
            mTvStateInfo.setText(titleState);

            TextView mBtnCancel = rootView.findViewById(R.id.id_btn_third_pay_cancel);
            mBtnCancel.setOnClickListener(new PayStateClickListener(type, LEFT_BTN_TYPE, mDialog, IDCWPayActivity.this));
            TextView mBtnConfirm = rootView.findViewById(R.id.id_btn_third_pay_confirm);
            mBtnCancel.setText(leftText);
            if (rightText == 0) {
                mBtnConfirm.setVisibility(View.GONE);
                mBtnCancel.setBackgroundResource(R.drawable.common_left_bottom_coner_bg);
                if (type == BALANCE_ENOUGH) {
                    mBtnCancel.setTextColor(mCtx.getResources().getColor(R.color.color_0076FF));
                    //mTvStateInfo.setTextSize(ScreenUtil.px2sp(mContext,60));
                    rootView.findViewById(R.id.id_tv_third_pay_state_balance_enough)
                            .setVisibility(View.VISIBLE);
                    mTvStateInfo.setVisibility(View.GONE);
                }
            } else {
                mBtnConfirm.setVisibility(View.VISIBLE);
                mBtnConfirm.setOnClickListener(new PayStateClickListener(type, RIGHT_BTN_TYPE, mDialog, IDCWPayActivity.this));
                mBtnConfirm.setText(rightText);
            }
            mDialog.setContentView(rootView);
            mDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_ll_ac_idcw_pay_close) {
            onBackPressed();
        } else if (v.getId() == R.id.btn_pay) {
            showPayPassDialog();
        }
    }

    @Override
    public void onPinResultStr(String result) {
        try {
            pay(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PayStateClickListener implements View.OnClickListener {

        private int mStateType;
        private int mBtnType;
        private WeakReference<Dialog> mWeakReference;
        private WeakReference<IDCWPayActivity> mWeakActivity;

        public PayStateClickListener(int mStateType, int btnType, Dialog dialog, IDCWPayActivity activity) {
            this.mStateType = mStateType;
            this.mBtnType = btnType;
            this.mWeakReference = new WeakReference<Dialog>(dialog);
            this.mWeakActivity = new WeakReference<IDCWPayActivity>(activity);
        }

        @Override
        public void onClick(View v) {
            if (mWeakReference.get() != null) {
                mWeakReference.get().dismiss();
            }
            switch (mStateType) {
                case PIN_ERROR:
                    // Pin码不正确
                    if (mBtnType == LEFT_BTN_TYPE) {
                        goToThirdActivity();
                    } else {
                        mWeakActivity.get().showPinDialog();
                    }
                    break;
                case BALANCE_ENOUGH:
                    // 余额不足
                    goToThirdActivity();
                    break;
                case PAY_TIMEOUT:
                    // 支付超时
                    if (mBtnType == LEFT_BTN_TYPE) {
                        goToThirdActivity();
                    } else {
                        mWeakActivity.get().showPayPassDialog();
                    }
                    break;
                case NET_ERROR:
                    // 网络连接失败，请检查网络后重试
                    if (mBtnType == LEFT_BTN_TYPE) {
                        goToThirdActivity();
                    } else {
                        mWeakActivity.get().showPayPassDialog();
                    }
                    break;
                case SERVER_ERROR:
                    // 服务器异常
                    if (mBtnType == LEFT_BTN_TYPE) {
                        goToThirdActivity();
                    } else {
                        mWeakActivity.get().showPayPassDialog();
                    }
                    break;
                case PAYING_WAIT:
                    // 转汇确认中
                    goToThirdActivity();
                    break;
                case SERVER_BUSY:
                    // 服务器正忙
                    //goToThirdActivity();
                    break;
                case RE_PAY: // 重复支付
                    goToThirdActivity();
                    break;
                default:
                    break;
            }
        }

        private void goToThirdActivity() {
            mWeakActivity.get().onBackPressed();
        }
    }

    @Subscriber
    public void refrestrfhBuyList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 333) {
            showPayPassDialog();
        }
    }

    private void dismissPinDialog() {
        if (mNewPinDialog != null && mNewPinDialog.isShowing()) {
            mNewPinDialog.dismiss();
        }
    }
}
