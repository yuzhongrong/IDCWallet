package foxidcw.android.idcw.otc.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.common.widget.ClearableEditText;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.model.beans.OTCWithdrawResultBean;
import foxidcw.android.idcw.otc.model.params.OTCReqSyncAddressParams;
import foxidcw.android.idcw.otc.model.params.OTCWithdrawDepositParam;
import foxidcw.android.idcw.otc.widgets.dialog.OTCOpenPhotoPopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCRechargeDepositDialog;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectCoinTypePopWindow;

/**
 * Created by hpz on 2018/5/5.
 */

@Route(path = OTCConstant.WITHDRAWDEPOSIT, name = "提取保证金页面")
public class OTCWithdrawDepositActivity extends BaseWalletActivity implements View.OnClickListener, TextWatcher, OTCRechargeDepositDialog.OnOTCPinInputCompleteListener {
    private LinearLayout mMrBackLayout;
    /**
     * title
     */
    private TextView mTvSetName;
    private ImageView mImgSelect;
    /**
     * BTC
     */
    private TextView mTvSelect;
    private LinearLayout mLlCurrAndName;
    /**
     * str_otc_select_curr
     */
    private TextView mTvSelectCurrHint;
    private ImageView mIvRightArrow;
    private RelativeLayout mLlSelectCurr;
    /**
     * 0
     */
    private EditText mEdUpInput;
    /**
     * BTC
     */
    private TextView mTvCurrTypeOne;
    /**
     * 56.0182 BTC
     */
    private TextView mTvWithdrawBalance;
    /**
     * tx_copy
     */
    private TextView mTvCopyAddress;
    /**  */
    private ClearableEditText mEdAccount;
    private ImageButton mBtnCode;
    private LinearLayout mLlSaoLayout;
    private Dialog dialogRecharge;
    /**
     * str_otc_assurer_buy
     */
    private TextView mBtnAcceptBuyCurr;

    private List<OTCCoinListBean> coinBeanList = new ArrayList<>();

    private OTCCoinListBean mOTCCoinListBean = null; //币种选中的item

    @Autowired
    OTCGetCoinListProviderServices mGetExchangeCoinListProviderServices;

    @Autowired
    OTCMoneyBagListProviderServices otcMoneyBagListProviderServices;

    private int coinId;
    private EditText ed_amount_down_hint;
    private LinearLayout ll_withdraw_deposit_layout;
    private LinearLayout mr_back_layout;
    private TextView tv_set_Name;
    private TextView tv_now_address;
    private TextView btn_now_recharge;
    private TextView tv_amount_currency;
    private OTCRechargeDepositDialog mOtcRechargeDepositDialog;//pin验证
    private double newBalance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw_deposit;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mMrBackLayout.setOnClickListener(this);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mImgSelect = (ImageView) findViewById(R.id.img_select);
        mTvSelect = (TextView) findViewById(R.id.tv_select);
        mLlCurrAndName = (LinearLayout) findViewById(R.id.ll_curr_and_name);
        mTvSelectCurrHint = (TextView) findViewById(R.id.tv_select_curr_hint);
        mIvRightArrow = (ImageView) findViewById(R.id.iv_right_arrow);
        mLlSelectCurr = (RelativeLayout) findViewById(R.id.ll_select_curr);
        mEdUpInput = (EditText) findViewById(R.id.ed_up_input);
        mTvCurrTypeOne = (TextView) findViewById(R.id.tv_curr_type_one);
        mTvWithdrawBalance = (TextView) findViewById(R.id.tv_withdraw_balance);
        mTvCopyAddress = (TextView) findViewById(R.id.tv_copy_address);
        mTvCopyAddress.setOnClickListener(this);
        mEdAccount = (ClearableEditText) findViewById(R.id.ed_account);
        mBtnCode = (ImageButton) findViewById(R.id.btn_code);
        mLlSaoLayout = (LinearLayout) findViewById(R.id.ll_sao_layout);
        mLlSaoLayout.setOnClickListener(this);
        mBtnAcceptBuyCurr = (TextView) findViewById(R.id.btn_accept_buy_curr);
        ed_amount_down_hint = (EditText) findViewById(R.id.ed_amount_down_hint);
        ll_withdraw_deposit_layout = (LinearLayout) findViewById(R.id.ll_withdraw_deposit_layout);
        mBtnAcceptBuyCurr.setEnabled(false);
        mEdUpInput.addTextChangedListener(this);
        mEdAccount.addTextChangedListener(this);
        mBtnAcceptBuyCurr.setOnClickListener(this);
        mLlSelectCurr.setOnClickListener(this);
        mLlSaoLayout.setOnClickListener(this);
        mTvSetName.setText(getString(R.string.str_otc_withdraw_deposit));
        initWithdrawDialog();
    }

    private void initWithdrawDialog() {
        dialogRecharge = new Dialog(this, R.style.BottomDialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_sure_recharge_dialog, null);
        dialogRecharge.setCancelable(false);
        //初始化控件
        mr_back_layout = (LinearLayout) inflate.findViewById(R.id.mr_back_to_layout);
        tv_set_Name = (TextView) inflate.findViewById(R.id.tv_set_Name);
        tv_now_address = (TextView) inflate.findViewById(R.id.tv_now_address);
        btn_now_recharge = (TextView) inflate.findViewById(R.id.btn_now_recharge);//
        tv_amount_currency = (TextView) inflate.findViewById(R.id.tv_amount_currency);//
        mr_back_layout.setOnClickListener(this);
        btn_now_recharge.setOnClickListener(this);
        tv_set_Name.setText(getString(R.string.str_otc_withdraw_sure_text));
        btn_now_recharge.setText(getString(R.string.str_otc_withdraw_sure_text));
        //将布局设置给Dialog
        dialogRecharge.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialogRecharge.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        //将属性设置给窗体
        dialogRecharge.setCanceledOnTouchOutside(false); // 外部点击取消
        //设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialogRecharge.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        //lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.width = this.getWindowManager().getDefaultDisplay().getWidth(); // 宽度持平
        lp.height = this.getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);
    }

    @Override
    protected void onEvent() {
        requestCoinList(true);
    }

    private void requestCoinList(boolean showDialog) {
        mGetExchangeCoinListProviderServices.requestOTCCurrWaterList()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<List<OTCCoinListBean>>((BaseProgressView) mCtx, showDialog) {
                    @Override
                    public void onSuccess(List<OTCCoinListBean> data) {
                        //updateCoinList(data.getCoinList(), data.getSortList());
                        coinBeanList.addAll(data);
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        //showError(true);
                    }
                });
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            this.finish();
        } else if (i == R.id.mr_back_to_layout) {//btn_now_recharge
            dialogRecharge.dismiss();
        } else if (i == R.id.tv_copy_address) {
            try {
                ClipboardManager paste = (ClipboardManager) OTCWithdrawDepositActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                String content = paste.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                mEdAccount.setText(content);
            } catch (Exception ex) {
                LogUtil.e("Exception:", ex.getMessage());
            }
        } else if (i == R.id.ll_sao_layout) {
            new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        LogUtil.i("--------granted------>", granted + "");
                        if (granted) {
                            ARouter.getInstance().build(ArouterConstants.SAOCODE).navigation(this, 11);
                        } else {
                            OTCOpenPhotoPopWindow otcOpenPhotoPopWindow = new OTCOpenPhotoPopWindow(mCtx);
                            otcOpenPhotoPopWindow.showPopupWindow();
                            otcOpenPhotoPopWindow.getSkipSureDelete().setOnClickListener(v1 -> {
                                if (otcOpenPhotoPopWindow != null) otcOpenPhotoPopWindow.dismiss();
                                openAppSetting();
                            });

                        }
                    });
        } else if (i == R.id.btn_accept_buy_curr) {
//            if (Double.parseDouble(mEdUpInput.getText().toString().trim()) > newBalance) {
//                ToastUtil.show(getString(R.string.str_otc_assener_can_withdraw_not));
//                return;
//            }
            if (mTvSelect.getText().toString().toUpperCase().equals("LTC")) {
                if (Double.valueOf(mEdUpInput.getText().toString().trim()) < 0.0006) {
                    ToastUtil.show(getString(R.string.otc_most_amount_enable));
                    return;
                }
            }else {
                if (Double.valueOf(mEdUpInput.getText().toString().trim()) < 0.00006) {
                    ToastUtil.show(getString(R.string.otc_most_amount_more_enable));
                    return;
                }
            }
            mOtcRechargeDepositDialog = new OTCRechargeDepositDialog(mCtx);
            mOtcRechargeDepositDialog.setOnOTCPinInputCompleteListener(this);
            requestCheckAddress(mEdAccount.getText().toString(), mTvSelect.getText().toString().toLowerCase());
        } else if (i == R.id.btn_now_recharge) {
            mOtcRechargeDepositDialog = new OTCRechargeDepositDialog(mCtx);
            mOtcRechargeDepositDialog.show();
            mOtcRechargeDepositDialog.setOnOTCPinInputCompleteListener(this);
            //requestRechargeDeposit(coinId,mEdUpInput.getText().toString().trim(),mTvRechargeAddress.getText().toString().trim(),);
        } else if (i == R.id.ll_select_curr) {
            showChooseCoinPopWin();
        } else {
        }
    }

    private void openAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void requestCheckAddress(String address, String currency) {
        otcMoneyBagListProviderServices.requestSyncAddressProvider(new OTCReqSyncAddressParams(address, currency))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxSubscriber<String>() {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        dismissDialog();
                        if (ex.getErrorCode().equals("0")) {
                            mEdAccount.setText(address);
                            ToastUtil.show(getString(R.string.address_not_true));
                        } else {
                            ToastUtil.show(getString(R.string.server_connection_error));
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        mEdAccount.setText(address);
                        requestCheckLastAddress(mEdAccount.getText().toString(), mTvSelect.getText().toString().toLowerCase());
                    }
                });
    }

    private void requestCheckLastAddress(String address, String currency) {
        otcMoneyBagListProviderServices.requestCheckBtnAddressProvider(new OTCReqSyncAddressParams(address, currency))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(
                new RxProgressSubscriber<Boolean>(this) {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if (0 == ex.getState()) {
                            mEdAccount.setText(address);
                            ToastUtil.show(getString(R.string.address_not_true));
                        }
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (!aBoolean) {//判断
                            mEdAccount.setText(address);
                            ToastUtil.show(getString(R.string.address_not_true));
                        } else {
                            mOtcRechargeDepositDialog.show();
                            mOtcRechargeDepositDialog.setResultText(getString(R.string.str_otc_assener_withdraw_success), getString(R.string.str_otc_withdraw_finish));
                            mOtcRechargeDepositDialog.setTextViewVisible();
                            mOtcRechargeDepositDialog.setTitle(getString(R.string.str_otc_withdraw_sure_text), mEdAccount.getText().toString().trim(), mTvSelect.getText().toString() + " " + mEdUpInput.getText().toString().trim(), getString(R.string.str_otc_assener_now_withdrawt));
                        }
                    }
                }
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && data != null) {
            String codestring = data.getStringExtra("result");
            if (!TextUtils.isEmpty(codestring)) {
                mEdAccount.setText(codestring);
            }
        }
    }

    private void showChooseCoinPopWin() {
        OTCSelectCoinTypePopWindow OTCSelectCoinTypePopWindow = new OTCSelectCoinTypePopWindow(mCtx);
        OTCSelectCoinTypePopWindow.showPopWindowForData(coinBeanList, 1, mOTCCoinListBean);
        OTCSelectCoinTypePopWindow.setTitle(getString(R.string.str_otc_select_withdraw_curr));
        OTCSelectCoinTypePopWindow.setOnItemClickListener(((bean, position) -> {
            GlideUtil.loadImageView(mCtx, bean.getLogo(), mImgSelect);
            coinId = bean.getId();//币种id
            mTvSelect.setText(bean.getCoinCode().toUpperCase());
            mTvCurrTypeOne.setText(bean.getCoinCode().toUpperCase());
            mTvWithdrawBalance.setText(Utils.toSubStringDegist(bean.getDepositBanlance(), 4) + " " + bean.getCoinCode().toUpperCase());
            newBalance = bean.getDepositBanlance();
            mOTCCoinListBean = bean;
            refreshPageUi();
        }));
    }

    private void refreshPageUi() {
        mTvSelectCurrHint.setVisibility(View.GONE);
        mLlCurrAndName.setVisibility(View.VISIBLE);
        ed_amount_down_hint.setVisibility(View.GONE);
        mTvCurrTypeOne.setVisibility(View.VISIBLE);
        mEdUpInput.setVisibility(View.VISIBLE);
        ll_withdraw_deposit_layout.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mEdUpInput.getText().toString())) {
            mEdUpInput.getText().clear();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mBtnAcceptBuyCurr.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        mBtnAcceptBuyCurr.setTextColor(getResources().getColor(R.color.color_a0a2b1));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().substring(0).equals(".")) {
            mEdUpInput.setText("0" + s);
            mEdUpInput.setSelection(2);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(mEdUpInput.getText().toString().equals("")||mEdAccount.getText().toString().equals(""))) {
            mBtnAcceptBuyCurr.setEnabled(true);
            mBtnAcceptBuyCurr.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onComplete(String inputPass) {
        LoginStatus bean = LoginUtils.getLoginBean(this);
        if (bean != null) {//coinId, mEdUpInput.getText().toString().trim(), mTvRechargeAddress.getText().toString().trim(), inputPass, bean.getDevice_id()
            requestWithdrawDeposit(coinId, Double.parseDouble(mEdUpInput.getText().toString().trim()), mEdAccount.getText().toString().trim(), inputPass, bean.getDevice_id());
        }
    }

    private void requestWithdrawDeposit(int CoinId, double Amount, String Address, String PIN, String DeviceId) {
        otcMoneyBagListProviderServices.requestWithdrawDepositProvider
                (new OTCWithdrawDepositParam(CoinId, Amount, Address, PIN, DeviceId))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<OTCWithdrawResultBean>(this) {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        if (ex.getErrorCode().equals("407")) {
                            ToastUtil.show("PIN错误次数太多");
                        } else if (ex.getErrorCode().equals("628")) {
                            mOtcRechargeDepositDialog.checkPinErrorHint(OTCWithdrawDepositActivity.this);
                            ToastUtil.show(getString(R.string.pay_pass_word_error));
                        } else if (ex.getErrorCode().equals("602")) {
                            mOtcRechargeDepositDialog.checkPinErrorHint(OTCWithdrawDepositActivity.this);
                            ToastUtil.show(getString(R.string.str_otc_assener_can_withdraw_not));
                        } else {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.str_otc_assener_withdrawt_error));
                        }
                    }

                    @Override
                    public void onSuccess(OTCWithdrawResultBean otcRechargeResultBean) {
                        if (0 == otcRechargeResultBean.getStatusCode()) {
                            if (null != mOtcRechargeDepositDialog)
                                mOtcRechargeDepositDialog.showResultPage();
                        } else if (6 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.address_not_true));
                        } else if (102 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.balance_enough));
                        } else if (108 == otcRechargeResultBean.getStatusCode() || 3 == otcRechargeResultBean.getStatusCode() ||
                                102 == otcRechargeResultBean.getStatusCode() || 107 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.trade_text_send));
                        } else if (10 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.checkPinErrorHint(OTCWithdrawDepositActivity.this);
                            ToastUtil.show(getString(R.string.pay_pass_word_error));
                        } else if (963800 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (963900 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (961600 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (973300 == otcRechargeResultBean.getStatusCode() || 973400 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (962900 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (964200 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (960200 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (611 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        }else if (751 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        }else if (961300 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.str_otc_assener_withdrawt_error));
                        }
                    }
                });
    }

    @Override
    public void onConfirmBtnClick() {
        setResult(RESULT_OK, new Intent());
        ARouter.getInstance().build(OTCConstant.DEPOSITWATER)
                .withInt("waterTag", 3)
                .withString("depositWater", mTvSelect.getText().toString().toLowerCase())
                .withInt("ID", coinId)
                .navigation(mCtx);
        finish();
    }
}
