package foxidcw.android.idcw.otc.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceBean;
import foxidcw.android.idcw.otc.model.beans.OTCRechargeDepositParam;
import foxidcw.android.idcw.otc.model.beans.OTCRechargeResultBean;
import foxidcw.android.idcw.otc.model.params.OTCCurrentStepParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCRechargeDepositDialog;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectCoinTypePopWindow;

/**
 * Created by hpz on 2018/5/2.
 */

@Route(path = OTCConstant.RECHARGEDEPOSIT, name = "申请承兑商充值保证金页面")
public class OTCRechargeDepositActivity extends BaseWalletActivity implements View.OnClickListener, OTCRechargeDepositDialog.OnOTCPinInputCompleteListener, TextWatcher {
    private LinearLayout mMrBackLayout;
    private TextView mTvSetName;
    private ImageView mImgOneStep;
    private View mViewOneStep;
    private ImageView mImgTwoStep;
    private View mViewTwoStep;
    private ImageView mImgThreeStep;
    private View mViewThreeStep;
    private ImageView mImgFourStep;
    private TextView mTvTwoSellText;
    private TextView mTvThreeSellText;
    private TextView mTvFourSellText;
    private ImageView mImgSelect;
    private TextView mTvSelect;
    private ImageView mIvRightArrow;
    private RelativeLayout mLlSelectCurr;
    private EditText mEdUpInput;
    private TextView mTvRechargeAddress;
    private TextView mTvSeekCurrency;
    private TextView mTvSeekBalance;
    private BubbleSeekBar mSeekBar;
    private LinearLayout mLlFreeSeekbar;
    private LinearLayout mLlSeekFree;
    private LinearLayout mLlCurrAndName;
    private TextView mBtnAcceptRechargeDeposit;
    private TextView mTvRechargeBalance;
    private TextView mTvSelectCurrHint;
    private LinearLayout mr_back_layout;
    private TextView tv_now_address;
    private TextView btn_now_recharge;
    private int coinId;
    private TextView tv_amount_currency;
    private TextView tv_curr_type_one;
    private EditText ed_amount_down_hint;
    private LinearLayout ll_withdraw_balance_layout;
    private List<OTCCoinListBean> coinBeanList = new ArrayList<>();
    private OTCCoinListBean mOTCCoinListBean = null; //币种选中的item
    private OTCRechargeDepositDialog mOtcRechargeDepositDialog;//pin验证的弹框
    private double MinAmount;//获取最小充值数量

    @Autowired
    OTCGetCoinListProviderServices mGetExchangeCoinListProviderServices;

    @Autowired
    OTCMoneyBagListProviderServices mWalletListServices;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;
    private double newBalance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_deposit;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mBtnAcceptRechargeDeposit = (TextView) findViewById(R.id.btn_accept_recharge_deposit);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mImgOneStep = (ImageView) findViewById(R.id.img_one_step);
        mViewOneStep = (View) findViewById(R.id.view_one_step);
        mImgTwoStep = (ImageView) findViewById(R.id.img_two_step);
        mViewTwoStep = (View) findViewById(R.id.view_two_step);
        mImgThreeStep = (ImageView) findViewById(R.id.img_three_step);
        mViewThreeStep = (View) findViewById(R.id.view_three_step);
        mImgFourStep = (ImageView) findViewById(R.id.img_four_step);
        mTvTwoSellText = (TextView) findViewById(R.id.tv_two_sell_text);
        mTvThreeSellText = (TextView) findViewById(R.id.tv_three_sell_text);
        mTvFourSellText = (TextView) findViewById(R.id.tv_four_sell_text);
        mImgSelect = (ImageView) findViewById(R.id.img_select);
        mTvSelect = (TextView) findViewById(R.id.tv_select);
        mIvRightArrow = (ImageView) findViewById(R.id.iv_right_arrow);
        mLlSelectCurr = (RelativeLayout) findViewById(R.id.ll_select_curr);
        mEdUpInput = (EditText) findViewById(R.id.ed_up_input);
        mTvRechargeAddress = (TextView) findViewById(R.id.tv_recharge_address);
        mTvSeekCurrency = (TextView) findViewById(R.id.tv_seek_currency);
        mTvSeekBalance = (TextView) findViewById(R.id.tv_seek_balance);
        mSeekBar = (BubbleSeekBar) findViewById(R.id.seekBar);
        mLlFreeSeekbar = (LinearLayout) findViewById(R.id.ll_free_seekbar);
        mLlSeekFree = (LinearLayout) findViewById(R.id.ll_seek_free);
        mBtnAcceptRechargeDeposit = (TextView) findViewById(R.id.btn_accept_recharge_deposit);
        mTvRechargeBalance = (TextView) findViewById(R.id.tv_recharge_deposit);
        mLlCurrAndName = (LinearLayout) findViewById(R.id.ll_curr_and_name);
        mTvSelectCurrHint = (TextView) findViewById(R.id.tv_select_curr_hint);
        tv_curr_type_one = (TextView) findViewById(R.id.tv_curr_type_one);//
        ed_amount_down_hint = (EditText) findViewById(R.id.ed_amount_down_hint);
        ll_withdraw_balance_layout = (LinearLayout) findViewById(R.id.ll_withdraw_balance_layout);
        mLlSelectCurr.setOnClickListener(this);
        mMrBackLayout.setOnClickListener(this);
        mBtnAcceptRechargeDeposit.setOnClickListener(this);
        mBtnAcceptRechargeDeposit.setOnClickListener(this);
        mEdUpInput.addTextChangedListener(this);
        mTvSetName.setText(getString(R.string.str_otc_apply_assurer));
        mBtnAcceptRechargeDeposit.setEnabled(false);
        mViewOneStep.setBackgroundColor(getResources().getColor(R.color.tipper_blue_color));
        mViewTwoStep.setBackgroundColor(getResources().getColor(R.color.tipper_blue_color));
        mImgTwoStep.setImageResource(R.mipmap.otc_icon_two_select);
        mImgThreeStep.setImageResource(R.mipmap.otc_icon_three_select);
        mTvTwoSellText.setTextColor(getResources().getColor(R.color.tipper_blue_color));
        mTvThreeSellText.setTextColor(getResources().getColor(R.color.tipper_blue_color));
    }

    @Override
    protected void onEvent() {
        requestCoinList(true);//请求添加保证金币种列表
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

    @Override
    protected BaseView getView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            this.finish();
        } else if (i == R.id.ll_select_curr) {
            showChooseCoinPopWin();//点击按钮弹出选择保证金币种的列表
        } else if (i == R.id.btn_accept_recharge_deposit) {//充值保证金的弹框
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
                        } else if (ex.getErrorCode().equals("407")) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show("PIN已锁定");
                        } else if (ex.getErrorCode().equals("630")) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.str_otc_assener_recharge_error));
                        } else if (ex.getErrorCode().equals("121")) {
                            mOtcRechargeDepositDialog.checkPinErrorHint(OTCRechargeDepositActivity.this);
                            ToastUtil.show(getString(R.string.pay_pass_word_error));
                        } else if (ex.getErrorCode().equals("733")) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.balance_enough));
                        } else if (ex.getErrorCode().equals("628")) {
                            mOtcRechargeDepositDialog.checkPinErrorHint(OTCRechargeDepositActivity.this);
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
                            requestSetFourthStep();//设置承兑商设置步骤4
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
                            mOtcRechargeDepositDialog.checkPinErrorHint(OTCRechargeDepositActivity.this);
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
                        } else if (961700 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (611 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (751 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(ErrorCodeUtils.getErrorMessage(String.valueOf(otcRechargeResultBean.getStatusCode())));
                        } else if (963200 == otcRechargeResultBean.getStatusCode()) {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.str_otc_assener_recharge_error));
                        } else {
                            mOtcRechargeDepositDialog.dismiss();
                            ToastUtil.show(getString(R.string.str_otc_assener_recharge_error));
                        }
                    }
                });
    }

    private void showChooseCoinPopWin() {
        OTCSelectCoinTypePopWindow OTCSelectCoinTypePopWindow = new OTCSelectCoinTypePopWindow(mCtx);
        OTCSelectCoinTypePopWindow.showPopWindowForData(coinBeanList, 1, mOTCCoinListBean);
        OTCSelectCoinTypePopWindow.setTitle(getString(R.string.str_otc_select_withdraw_curr));
        OTCSelectCoinTypePopWindow.setOnItemClickListener(((bean, position) -> {
            mTvRechargeAddress.setText(bean.getSysWalletAddress());
            coinId = bean.getId();//币种id
            mOTCCoinListBean = bean;
            GlideUtil.loadImageView(mCtx, bean.getLogo(), mImgSelect);
            tv_curr_type_one.setText(bean.getCoinCode().toUpperCase());
            mTvSelect.setText(bean.getCoinCode().toUpperCase());
            mEdUpInput.setHint(String.format(getString(R.string.str_otc_assener_add_amount_limit), StringUtils.subZeroAndDot(Utils.toSubStringDegist(bean.getMinAmount(), 1))));
            MinAmount = (double) (bean.getMinAmount());
            refreshPageUi();//选择完币种之后刷新页面
            RequestNewDepositBalance(bean.getCoinCode(), true);//根据选择的币种请求最新的余额
        }));
    }

    private void refreshPageUi() {
        mTvSelectCurrHint.setVisibility(View.GONE);
        mEdUpInput.setVisibility(View.VISIBLE);
        tv_curr_type_one.setVisibility(View.VISIBLE);
        ed_amount_down_hint.setVisibility(View.GONE);
        mLlCurrAndName.setVisibility(View.VISIBLE);
        ll_withdraw_balance_layout.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mEdUpInput.getText().toString())) {
            mEdUpInput.getText().clear();
        }
    }

    private void RequestNewDepositBalance(String currency, boolean isShowProgress) {
        addSubscribe(mWalletListServices.requestNewDepositBalance(currency).subscribeWith(
                new RxProgressSubscriber<OTCDepositBalanceBean>(this, isShowProgress) {
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_connection_error));
                    }

                    @Override
                    public void onSuccess(OTCDepositBalanceBean newBalanceBean) {
                        mTvRechargeBalance.setText(Utils.toSubStringDegist(newBalanceBean.getCurrentBalance(), 4) + " " + currency.toUpperCase());
                        newBalance = newBalanceBean.getCurrentBalance();
                    }
                }));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onComplete(String inputPass) {//输入完PIN的回调事件
        LoginStatus bean = LoginUtils.getLoginBean(this);
        if (bean != null) {
            requestRechargeDeposit(coinId, mEdUpInput.getText().toString().trim(), mTvRechargeAddress.getText().toString().trim(), inputPass, bean.getDevice_id());
        }
    }

    @Override
    public void onConfirmBtnClick() {
        //requestSetFourthStep();//设置承兑商设置步骤4
        EventBus.getDefault().post(new PosInfo(166));
        setResult(RESULT_OK, new Intent());
        navigation(OTCConstant.DEPOSITSUCCESS);//充值成功跳转的页面
        finish();
    }

    private void requestSetFourthStep() {
        OTCCurrentStepParams otcCurrentStepParams = new OTCCurrentStepParams(4);
        otcAddBuyCurrProviderServices.requestSetCurrentStepProvider(otcCurrentStepParams)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<Object>((BaseProgressView) mCtx, true) {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_error));
                    }
                });
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
