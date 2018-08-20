package foxidcw.android.idcw.otc.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCAddAmountParam;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectCoinTypePopWindow;

/**
 * Created by hpz on 2018/5/5.
 */

@Route(path = OTCConstant.LEGALTENDER, name = "添加法币资金页面")
public class OTCLegalTenderActivity extends BaseWalletActivity implements View.OnClickListener, TextWatcher {
    private LinearLayout mMrBackLayout;
    private TextView mTvSetName;
    private ImageView mImgSelect;
    private TextView mTvSelect;
    private ImageView mIvRightArrow;
    private RelativeLayout mLlSelectMoney;
    private EditText mEdUpInput;
    private TextView mTvCurrTypeOne;
    private TextView mBtnAcceptSaveMoney;

    @Autowired
    OTCGetCoinListProviderServices mGetExchangeCoinListProviderServices;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;

    @Autowired(name = "legal")
    int legal = 1;
    @Autowired(name = "legalId")
    String legalId = "";
    @Autowired(name = "legalCodeId")
    int legalCodeId = 1001;
    @Autowired(name = "Amount")
    double Amount = 0.01;


    private List<OTCCoinListBean> coinBeanList = new ArrayList<>();
    private int coinId;
    private LinearLayout mLlSelectCurrName;
    private TextView mSelectCurrHint;
    private OTCCoinListBean mOTCCoinListBean = null; //币种选中的item
    private EditText mEditDownHint;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_legal_tender;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mImgSelect = (ImageView) findViewById(R.id.img_select);
        mTvSelect = (TextView) findViewById(R.id.tv_select);
        mIvRightArrow = (ImageView) findViewById(R.id.iv_right_arrow);
        mLlSelectMoney = (RelativeLayout) findViewById(R.id.ll_select_money);
        mEdUpInput = (EditText) findViewById(R.id.ed_up_input);
        mTvCurrTypeOne = (TextView) findViewById(R.id.tv_curr_type_one);
        mBtnAcceptSaveMoney = (TextView) findViewById(R.id.btn_accept_save_money);
        mLlSelectCurrName = (LinearLayout) findViewById(R.id.ll_curr_and_name);
        mSelectCurrHint = (TextView) findViewById(R.id.tv_select_curr_hint);
        mEditDownHint = (EditText) findViewById(R.id.ed_amount_down_hint);
        mLlSelectMoney.setOnClickListener(this);
        mMrBackLayout.setOnClickListener(this);
        mBtnAcceptSaveMoney.setOnClickListener(this);
        mEdUpInput.addTextChangedListener(this);
        mBtnAcceptSaveMoney.setEnabled(false);
        mTvSetName.setText(getString(R.string.str_otc_assener_add_sell_amount));
    }

    @Override
    protected void onEvent() {
        requestCoinList(true);//请求法币列表数据
    }

    private void requestCoinList(boolean showDialog) {
        mGetExchangeCoinListProviderServices.requestOTCCoinLegalList()
                .compose(bindToLifecycle())
                .subscribe(new RxProgressSubscriber<List<OTCCoinListBean>>((BaseProgressView) mCtx, showDialog) {
                    @Override
                    public void onSuccess(List<OTCCoinListBean> data) {
                        coinBeanList.addAll(data);
                        if (legal == 2) {
                            mTvSetName.setText(getString(R.string.str_otc_assener_edit_sell_amount));
                            for (int i = 0; i < data.size(); i++) {
                                if (legalCodeId == data.get(i).getId()) {
                                    GlideUtil.loadImageView(mCtx, data.get(i).getLogo(), mImgSelect);
                                    mTvSelect.setText(data.get(i).getLocalCurrencyCode().toUpperCase());
                                    mTvCurrTypeOne.setText(data.get(i).getLocalCurrencyCode().toUpperCase());
                                }
                            }
                            //编辑进来更新UI，选择币种按钮不可点击
                            setAmountRefreshUi();
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    private void setAmountRefreshUi() {
        mLlSelectMoney.setEnabled(false);
        mSelectCurrHint.setVisibility(View.GONE);
        mEditDownHint.setVisibility(View.GONE);
        mTvCurrTypeOne.setVisibility(View.VISIBLE);
        mEdUpInput.setVisibility(View.VISIBLE);
        mLlSelectCurrName.setVisibility(View.VISIBLE);
        mLlSelectCurrName.setVisibility(View.VISIBLE);
        if (Utils.toSubStringDegist(Amount,2).contains(",")){
            String strSplite = Utils.toSubStringDegist(Amount,2).replace(",","");
            mEdUpInput.setText(strSplite);
        }else {
            mEdUpInput.setText(Utils.toSubStringDegist(Amount,2));
        }
        showSoftInputFromWindow(OTCLegalTenderActivity.this,mEdUpInput);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        } else if (i == R.id.btn_accept_save_money) {
            if (legal == 1) {
                requestExChangeLegal("",coinId);
            } else if (legal == 2) {
                requestExChangeLegal(legalId,legalCodeId);
            }
        } else if (i == R.id.ll_select_money) {
            OTCSelectCoinTypePopWindow OTCSelectCoinTypePopWindow = new OTCSelectCoinTypePopWindow(mCtx);
            OTCSelectCoinTypePopWindow.showPopWindowForData(coinBeanList, 2, mOTCCoinListBean); //币种选中的item;
            OTCSelectCoinTypePopWindow.setTitle(getString(R.string.str_otc_select_currency));
            OTCSelectCoinTypePopWindow.setOnItemClickListener(((bean, position) -> {
                GlideUtil.loadImageView(mCtx, bean.getLogo(), mImgSelect);
                mTvSelect.setText(bean.getLocalCurrencyCode());
                mTvCurrTypeOne.setText(bean.getLocalCurrencyCode());
                mOTCCoinListBean = bean;
                coinId = bean.getId();
                refreshPageUi();//选择完币种刷新页面
            }));
        } else {
        }
    }

    private void refreshPageUi() {
        mSelectCurrHint.setVisibility(View.GONE);
        mEditDownHint.setVisibility(View.GONE);
        mTvCurrTypeOne.setVisibility(View.VISIBLE);
        mEdUpInput.setVisibility(View.VISIBLE);
        mLlSelectCurrName.setVisibility(View.VISIBLE);
        mLlSelectCurrName.setVisibility(View.VISIBLE);
    }

    private void requestExChangeLegal(String id,int coinId) {
        OTCAddAmountParam otcAddAmountParam = new OTCAddAmountParam(id, coinId, Double.parseDouble(mEdUpInput.getText().toString().trim()));
        otcAddBuyCurrProviderServices.requestAddAmountProvider(otcAddAmountParam)
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<Boolean>(this) {
            @Override
            protected void onError(ResponseThrowable ex) {
                if (ex.getErrorCode().equals("622")){
                    ToastUtil.show(getString(R.string.str_otc_assener_not_retry_add));
                }else if (ex.getErrorCode().equals("603")){
                    ToastUtil.show(getString(R.string.str_otc_apply_ge_error));
                } else{
                    ToastUtil.show(getString(R.string.server_connection_error));
                }
            }

            @Override
            public void onSuccess(Boolean checkNewPinBean) {
                EventBus.getDefault().post(new PosInfo(101));
                setResult(2, new Intent().putExtra("result","refreshList"));
                OTCLegalTenderActivity.this.finish();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mBtnAcceptSaveMoney.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        mBtnAcceptSaveMoney.setTextColor(getResources().getColor(R.color.gray_90));
        mBtnAcceptSaveMoney.setBackgroundResource(R.drawable.item_gray_black);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(mEdUpInput.getText().toString().equals("")) && mSelectCurrHint.getVisibility() == View.GONE) {
            mBtnAcceptSaveMoney.setEnabled(true);
            mBtnAcceptSaveMoney.setTextColor(getResources().getColor(R.color.white));
            mBtnAcceptSaveMoney.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
        }
    }
}
