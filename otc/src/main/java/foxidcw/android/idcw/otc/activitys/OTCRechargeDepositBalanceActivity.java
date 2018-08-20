package foxidcw.android.idcw.otc.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceBean;
import foxidcw.android.idcw.otc.model.beans.OTCRechargeDepositParam;
import foxidcw.android.idcw.otc.model.beans.OTCRechargeResultBean;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCRechargeDepositDialog;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectCoinTypePopWindow;

/**
 * Created by hpz on 2018/5/9.
 */

@Route(path = OTCConstant.RECHARGEBALANCE, name = "充值保证金")
public class OTCRechargeDepositBalanceActivity extends BaseWalletActivity implements View.OnClickListener, OTCRechargeDepositDialog.OnOTCPinInputCompleteListener, TextWatcher {
    private LinearLayout mMrBackLayout;
    private TextView mTvSetName;
    private ImageView mImgSelect;
    private TextView mTvSelect;
    private LinearLayout mLlCurrAndName;
    private TextView mTvSelectCurrHint;
    private ImageView mIvRightArrow;
    private RelativeLayout mLlSelectCurr;
    private EditText mEdUpInput;
    private TextView mTvDepositBalance;
    private TextView mTvRechargeAddress;
    private TextView mTvSeekCurrency;
    private TextView mTvSeekBalance;
    private BubbleSeekBar mSeekBar;
    private LinearLayout mLlFreeSeekbar;
    private LinearLayout mLlSeekFree;
    private TextView mBtnAcceptRechargeDeposit;
    private OTCRechargeDepositDialog mOtcRechargeDepositDialog;
    private List<OTCCoinListBean> coinBeanList = new ArrayList<>();
    private OTCCoinListBean mOTCCoinListBean = null; //币种选中的item

    @Autowired
    OTCGetCoinListProviderServices mGetExchangeCoinListProviderServices;

    @Autowired
    OTCMoneyBagListProviderServices mWalletListServices;

    /**
     * pin验证接口
     */

    // 请求参数
    private OTCAddOrEditReqParams reqParams = new OTCAddOrEditReqParams();
    private Dialog dialogRecharge;
    private int coinId;
    private TextView mTvRechargeBalance;
    private TextView tv_curr_type_one;
    private EditText ed_amount_down_hint;
    private LinearLayout ll_withdraw_balance_layout;
    private double MinAmount;
    private double newBalance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_balance;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mImgSelect = (ImageView) findViewById(R.id.img_select);
        mTvSelect = (TextView) findViewById(R.id.tv_select);
        mLlCurrAndName = (LinearLayout) findViewById(R.id.ll_curr_and_name);
        mTvSelectCurrHint = (TextView) findViewById(R.id.tv_select_curr_hint);
        mIvRightArrow = (ImageView) findViewById(R.id.iv_right_arrow);
        mLlSelectCurr = (RelativeLayout) findViewById(R.id.ll_select_curr);
        mTvRechargeBalance = (TextView) findViewById(R.id.tv_recharge_deposit);
        mEdUpInput = (EditText) findViewById(R.id.ed_up_input);
        mTvDepositBalance = (TextView) findViewById(R.id.tv_deposit_balance);
        mTvRechargeAddress = (TextView) findViewById(R.id.tv_recharge_address);
        mTvSeekCurrency = (TextView) findViewById(R.id.tv_seek_currency);
        mTvSeekBalance = (TextView) findViewById(R.id.tv_seek_balance);
        mSeekBar = (BubbleSeekBar) findViewById(R.id.seekBar);
        mLlFreeSeekbar = (LinearLayout) findViewById(R.id.ll_free_seekbar);
        mLlSeekFree = (LinearLayout) findViewById(R.id.ll_seek_free);
        ll_withdraw_balance_layout = (LinearLayout) findViewById(R.id.ll_withdraw_balance_layout);
        mBtnAcceptRechargeDeposit = (TextView) findViewById(R.id.btn_accept_recharge_deposit);
        tv_curr_type_one = (TextView) findViewById(R.id.tv_curr_type_one);//
        ed_amount_down_hint = (EditText) findViewById(R.id.ed_amount_down_hint);
        mBtnAcceptRechargeDeposit.setOnClickListener(this);
        mMrBackLayout.setOnClickListener(this);
        mLlSelectCurr.setOnClickListener(this);
        mBtnAcceptRechargeDeposit.setEnabled(false);
        mEdUpInput.addTextChangedListener(this);
        mTvSetName.setText(getString(R.string.str_otc_deposit_money));
        //初始化发送对话框
        initRechargeDialog();
    }

    private void initRechargeDialog() {
        dialogRecharge = new Dialog(this, R.style.BottomDialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_sure_recharge_dialog, null);
        dialogRecharge.setCancelable(false);
        //初始化控件
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
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = this.getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);
    }

    @Override
    protected void onEvent() {
        requestCoinList(true);
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    private void requestCoinList(boolean showDialog) {
        mGetExchangeCoinListProviderServices.requestOTCDepositCoinList()
                .compose(bindToLifecycle())
                .subscribe(new RxProgressSubscriber<List<OTCCoinListBean>>((BaseProgressView) mCtx, showDialog) {
                    @Override
                    public void onSuccess(List<OTCCoinListBean> data) {
                        coinBeanList.addAll(data);
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    public void initView() {
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            this.finish();
        } else if (i == R.id.ll_select_curr) {
            showChooseCoinPopWin();
        } else if (i == R.id.btn_accept_recharge_deposit) {
            if (Double.parseDouble(mEdUpInput.getText().toString().trim()) < MinAmount) {
                ToastUtil.show(String.format(getString(R.string.str_otc_assener_min_amount_limit), StringUtils.subZeroAndDot(Utils.toSubStringDegist(MinAmount, 1))));
                return;
            }
            if (Double.parseDouble(mEdUpInput.getText().toString().trim()) > newBalance) {
                ToastUtil.show(getString(R.string.balance_enough));
                return;
            }
            mOtcRechargeDepositDialog = new OTCRechargeDepositDialog(mCtx);
            mOtcRechargeDepositDialog.show();
            mOtcRechargeDepositDialog.setOnOTCPinInputCompleteListener(this);
            mOtcRechargeDepositDialog.setResultText(getString(R.string.str_otc_assener_recharge_success), getString(R.string.str_otc_recharge_success));
            mOtcRechargeDepositDialog.setTitle(getString(R.string.str_otc_deposit_sure_recharge), mTvRechargeAddress.getText().toString(), mTvSelect.getText().toString() + " " + mEdUpInput.getText().toString().trim(), getString(R.string.str_otc_assener_now_recharge));
        } else {
        }
    }

    private void showChooseCoinPopWin() {
        OTCSelectCoinTypePopWindow OTCSelectCoinTypePopWindow = new OTCSelectCoinTypePopWindow(mCtx);
        OTCSelectCoinTypePopWindow.showPopWindowForData(coinBeanList, 1, mOTCCoinListBean);
        OTCSelectCoinTypePopWindow.setTitle(getString(R.string.str_otc_select_withdraw_curr));
        OTCSelectCoinTypePopWindow.setOnItemClickListener(((bean, position) -> {
            GlideUtil.loadImageView(mCtx, bean.getLogo(), mImgSelect);
            mTvRechargeAddress.setText(bean.getSysWalletAddress());
            tv_curr_type_one.setText(bean.getCoinCode().toUpperCase());
            mTvSelect.setText(bean.getCoinCode().toUpperCase());
            coinId = bean.getId();//币种id
            mOTCCoinListBean = bean;
            mEdUpInput.setHint(String.format(getString(R.string.str_otc_assener_add_amount_limit), StringUtils.subZeroAndDot(Utils.toSubStringDegist(bean.getMinAmount(), 1))));
            MinAmount = (double) (bean.getMinAmount());
            setSelectRefresh();
            RequestNewDepositBalance(bean.getCoinCode(), true);
        }));
    }

    private void setSelectRefresh() {
        mTvSelectCurrHint.setVisibility(View.GONE);
        mEdUpInput.setVisibility(View.VISIBLE);
        mLlCurrAndName.setVisibility(View.VISIBLE);
        tv_curr_type_one.setVisibility(View.VISIBLE);
        ed_amount_down_hint.setVisibility(View.GONE);
        ll_withdraw_balance_layout.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mEdUpInput.getText().toString())) {
            mEdUpInput.getText().clear();
        }
    }

    private void RequestNewDepositBalance(String currency, boolean isShowProgress) {
        mWalletListServices.requestNewDepositBalance(currency)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(
                        new RxProgressSubscriber<OTCDepositBalanceBean>(this, isShowProgress) {
                            @Override
                            protected void onError(ResponseThrowable ex) {
                                ToastUtil.show(getString(R.string.server_connection_error));
                            }

                            @Override
                            public void onSuccess(OTCDepositBalanceBean newBalanceBean) {
                                mTvDepositBalance.setText(Utils.toSubStringDegist(newBalanceBean.getCurrentBalance(), 4) + " " + currency.toUpperCase());
                                newBalance = newBalanceBean.getCurrentBalance();
                            }
                        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onComplete(String inputPass) {
        LoginStatus bean = LoginUtils.getLoginBean(this);
        if (bean != null) {
            requestRechargeDeposit(coinId, mEdUpInput.getText().toString().trim(), mTvRechargeAddress.getText().toString().trim(), inputPass, bean.getDevice_id());
        }
    }

    private void requestRechargeDeposit(int CoinId, String Amount, String SysWalletAddress, String PIN, String DeviceId) {
        OTCRechargeDepositParam otcRechargeDepositParam = new OTCRechargeDepositParam(CoinId, Double.parseDouble(Amount), SysWalletAddress, PIN, DeviceId);
        mWalletListServices.requestRechargeDepositProvider(otcRechargeDepositParam)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<OTCRechargeResultBean>(this) {
            @Override
            protected void onError(ResponseThrowable ex) {
                if (ex.getErrorCode().equals("618")) {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(getString(R.string.str_otc_assener_recharge_error));
                } else if (ex.getErrorCode().equals("630")) {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(getString(R.string.str_otc_assener_recharge_error));
                } else if (ex.getErrorCode().equals("121")) {
                    mOtcRechargeDepositDialog.checkPinErrorHint(OTCRechargeDepositBalanceActivity.this);
                    ToastUtil.show(getString(R.string.pay_pass_word_error));
                } else if (ex.getErrorCode().equals("733")) {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(getString(R.string.balance_enough));
                } else if (ex.getErrorCode().equals("628")) {
                    mOtcRechargeDepositDialog.checkPinErrorHint(OTCRechargeDepositBalanceActivity.this);
                    ToastUtil.show(getString(R.string.pay_pass_word_error));
                } else {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(getString(R.string.str_otc_assener_recharge_error));
                }
            }

            @Override
            public void onSuccess(OTCRechargeResultBean otcRechargeResultBean) {
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
                    mOtcRechargeDepositDialog.checkPinErrorHint(OTCRechargeDepositBalanceActivity.this);
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
                } else if (611 == otcRechargeResultBean.getStatusCode()) {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                } else if (751 == otcRechargeResultBean.getStatusCode()) {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                } else if (961700 == otcRechargeResultBean.getStatusCode()) {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                } else if (963200 == otcRechargeResultBean.getStatusCode()) {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                } else {
                    mOtcRechargeDepositDialog.dismiss();
                    ToastUtil.show(getString(R.string.str_otc_assener_recharge_error));
                }
            }
        });
    }

    @Override
    public void onConfirmBtnClick() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mBtnAcceptRechargeDeposit.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        mBtnAcceptRechargeDeposit.setTextColor(getResources().getColor(R.color.color_a0a2b1));
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
        if (!(mEdUpInput.getText().toString().equals("") || mTvRechargeAddress.getText().toString().equals("")) && mTvSelectCurrHint.getVisibility() == View.GONE) {
            mBtnAcceptRechargeDeposit.setEnabled(true);
            mBtnAcceptRechargeDeposit.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
