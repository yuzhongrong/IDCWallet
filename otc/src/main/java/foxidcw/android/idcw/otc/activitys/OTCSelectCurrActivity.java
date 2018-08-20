package foxidcw.android.idcw.otc.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.cjwsc.idcm.Utils.LogUtil;
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
import com.blankj.utilcode.util.RegexUtils;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCAddCurrParam;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectCoinTypePopWindow;
import foxidcw.android.idcw.otc.widgets.widget.AddOrReduceView;

/**
 * Created by hpz on 2018/4/25.
 */

@Route(path = OTCConstant.SELECTCURR, name = "添加承兑换币选择币种页面")
public class OTCSelectCurrActivity extends BaseWalletActivity implements View.OnClickListener, TextWatcher {
    private LinearLayout mMrBackLayout;
    private TextView mTvSetName;
    private ImageView mImgSelect;
    private TextView mTvSelect;
    private ImageView mIvRightArrow;
    private RelativeLayout mLlSelectCurr;
    private EditText mEdUpInput;
    private TextView mTvCurrTypeOne;
    private EditText mEdUpDown;
    private TextView mTvCurrType;
    private TextView mBtnAcceptBuyCurr;
    private LinearLayout mLlSelectCurrName;
    private TextView mSelectCurrHint;
    private OTCCoinListBean mOTCCoinListBean = null; //币种选中的item
    private int coinId;
    private EditText mEditUpHint;
    private EditText mEditDownHint;
    private EditText mEditSelectPrice;
    private AddOrReduceView addOrReduceView;
    private TextView tv_str_apply_content;
    private List<OTCCoinListBean> coinBeanList = new ArrayList<>();
    private double mSelectPrice;

    @Autowired(name = "buyOrSell")
    int buyOrSell = 1;//跳转页面标志
    @Autowired(name = "buyOrSellId")
    int buyOrSellId = 3;//币种Id
    @Autowired(name = "buyOrSellCoinId")
    int buyOrSellCoinId = 1001;//币种coinId
    @Autowired(name = "buyOrSellCoinCode")
    String buyOrSellCoinCode = "VHKD";//币种code
    @Autowired(name = "Max")
    double Max = 0.01;
    @Autowired(name = "Min")
    double Min = 0.01;
    @Autowired(name = "Premium")
    double Premium = 0.01;

    @Autowired//服务请求网络
            OTCGetCoinListProviderServices mGetExchangeCoinListProviderServices;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_curr;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mImgSelect = (ImageView) findViewById(R.id.img_select);
        mTvSelect = (TextView) findViewById(R.id.tv_select);
        mIvRightArrow = (ImageView) findViewById(R.id.iv_right_arrow);
        mLlSelectCurr = (RelativeLayout) findViewById(R.id.ll_select_curr);
        mEdUpInput = (EditText) findViewById(R.id.ed_up_input);
        mTvCurrTypeOne = (TextView) findViewById(R.id.tv_curr_type_one);
        mEdUpDown = (EditText) findViewById(R.id.ed_up_down);
        mTvCurrType = (TextView) findViewById(R.id.tv_curr_type);
        mBtnAcceptBuyCurr = (TextView) findViewById(R.id.btn_accept_buy_curr);
        mLlSelectCurrName = (LinearLayout) findViewById(R.id.ll_curr_and_name);
        mSelectCurrHint = (TextView) findViewById(R.id.tv_select_curr_hint);
        mEditUpHint = (EditText) findViewById(R.id.ed_amount_up_hint);
        mEditDownHint = (EditText) findViewById(R.id.ed_amount_down_hint);
        mEditSelectPrice = (EditText) findViewById(R.id.ed_select_price);
        addOrReduceView = (AddOrReduceView) findViewById(R.id.add_reduce_view);
        tv_str_apply_content = (TextView) findViewById(R.id.tv_str_apply_content);
        if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("0")) {
            tv_str_apply_content.setText(String.format(getString(R.string.str_otc_add_curr_price), " " + "-10% and 10%"));
        } else {
            tv_str_apply_content.setText(String.format(getString(R.string.str_otc_add_curr_price), " " + "-10%～+10%"));
        }
        //mEditSelectPrice.setFilters(new InputFilter[]{new Test()});
        addOrReduceView.setOnValueChangeListene(value -> {
            mSelectPrice = value;
        });
        mEdUpDown.addTextChangedListener(this);
        mEdUpInput.addTextChangedListener(this);
        mEditSelectPrice.addTextChangedListener(this);
        String etext = mEditSelectPrice.getText().toString();
        mEditSelectPrice.setSelection(etext.length());
//        mEdUpInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().trim().substring(0).equals(".")) {
//                    mEdUpInput.setText("0" + s);
//                    mEdUpInput.setSelection(2);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        mEdUpInput.addTextChangedListener(new CustomTextWatcher(mEdUpInput, false, 4));
        mEdUpDown.addTextChangedListener(new CustomTextWatcher(mEdUpDown, false, 4));
//        mEdUpDown.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().trim().substring(0).equals(".")) {
//                    mEdUpDown.setText("0" + s);
//                    mEdUpDown.setSelection(2);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        mEditSelectPrice.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().trim().substring(0).equals(".")) {
//                    mEditSelectPrice.setText("0" + s);
//                    mEditSelectPrice.setSelection(2);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        mEditSelectPrice.addTextChangedListener(new CustomTextWatcher(mEditSelectPrice, true, 2));

        mMrBackLayout.setOnClickListener(this);
        mLlSelectCurr.setOnClickListener(this);
        mBtnAcceptBuyCurr.setOnClickListener(this);
        mBtnAcceptBuyCurr.setEnabled(false);

        if (buyOrSell == 2 || buyOrSell == 4) {
            mTvSetName.setText(getString(R.string.otc_str_add_curr));
        } else if (buyOrSell == 1 || buyOrSell == 3) {
            mTvSetName.setText(getString(R.string.otc_str_add_curr));
        }

        //StringUtils.setEditTextInhibitInputSpeChat(mEditSelectPrice);
    }

    private class CustomTextWatcher implements TextWatcher {

        private String mLastStr;
        String regex = "(^%1$s[1-9]+\\d*\\.?[0-9]{0,%2$d}$)|(^%1$s[0]\\.[0-9]{0,%2$d}$)|(^%1$s[0]?$)";
        private EditText mEditText;

        public CustomTextWatcher(EditText mEditText, boolean hasNegative, int digest) {
            this.mEditText = mEditText;
            regex = String.format(regex, hasNegative ? "-?" : "", digest);
            LogUtil.e("regex  --->" + regex);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().equals(".")) {
                mEditText.setText("0" + s);
                mEditText.setSelection(2);
                return;
            }
            if (RegexUtils.isMatch(regex, s) || TextUtils.isEmpty(s)) {
                this.mLastStr = s.toString();
            } else {
                if (mEditText.getText().toString().equals(mLastStr)) return;
                int selection = Math.max(mEditText.getSelectionStart() - 1, 0);
                mEditText.setText(mLastStr);
                mEditText.setSelection(Math.min(selection, mLastStr.length()));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    protected void onEvent() {
        requestCoinList(true);//请求可选币种列表
    }

    private void requestCoinList(boolean showDialog) {
        mGetExchangeCoinListProviderServices.requestOTCCoinList()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new RxProgressSubscriber<List<OTCCoinListBean>>((BaseProgressView) mCtx, showDialog) {
                    @Override
                    public void onSuccess(List<OTCCoinListBean> data) {
                        coinBeanList.addAll(data);
                        if (buyOrSell == 3 || buyOrSell == 4) {
                            mLlSelectCurr.setEnabled(false);
                            if (buyOrSell == 3) {
                                mTvSetName.setText(getString(R.string.str_otc_assener_edit_buy));
                            }
                            if (buyOrSell == 4) {
                                mTvSetName.setText(getString(R.string.str_otc_assener_edit_sell));
                            }

                            for (int i = 0; i < data.size(); i++) {
                                if (buyOrSellCoinId == data.get(i).getId()) {
                                    GlideUtil.loadImageView(mCtx, data.get(i).getLogo(), mImgSelect);
                                }
                            }
                            setSelectCurrUi();//选择完币种之后需要更新页面UI
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    private void setSelectCurrUi() {
        mEditUpHint.setVisibility(View.GONE);
        mEditDownHint.setVisibility(View.GONE);
        mSelectCurrHint.setVisibility(View.GONE);
        mLlSelectCurrName.setVisibility(View.VISIBLE);
        mTvSelect.setText(buyOrSellCoinCode.toUpperCase());
        mTvCurrType.setText(buyOrSellCoinCode.toUpperCase());
        mTvCurrTypeOne.setText(buyOrSellCoinCode.toUpperCase());
        mEdUpInput.setVisibility(View.VISIBLE);
        mTvCurrTypeOne.setVisibility(View.VISIBLE);
        mEdUpDown.setVisibility(View.VISIBLE);
        mTvCurrType.setVisibility(View.VISIBLE);
        if (Utils.toSubStringDegist(Max, 4).contains(",")) {
            String strSpliteMax = StringUtils.subZeroAndDot(Utils.toSubStringDegist(Max, 4).replace(",", ""));
            mEdUpInput.setText(strSpliteMax);
        } else {
            mEdUpInput.setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(Max, 4)));
        }
        if (Utils.toSubStringDegist(Min, 4).contains(",")) {
            String strSpliteMin = StringUtils.subZeroAndDot(Utils.toSubStringDegist(Min, 4).replace(",", ""));
            mEdUpDown.setText(strSpliteMin);
        } else {
            mEdUpDown.setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(Min, 4)));
        }
        //mEditSelectPrice.setText(Premium * 100 + "");
        addOrReduceView.setValue(Double.parseDouble(StringUtils.formatChangeOne(Premium * 100)));
        addOrReduceView.setButtonColor().setTextColor(getResources().getColor(R.color.tip_black));

        showSoftInputFromWindow(OTCSelectCurrActivity.this, mEdUpInput);

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
        } else if (i == R.id.ll_select_curr) {
            if (buyOrSell == 3) {
                mLlSelectCurr.setEnabled(false);
                return;
            }
            showChooseCoinPopWin();
        } else if (i == R.id.btn_accept_buy_curr) {
//            RxTextView.textChanges(mEditSelectPrice).map(new Function<CharSequence, Double>() {
//                @Override
//                public Double apply(CharSequence charSequence) throws Exception {
//                    return TextUtils.isEmpty(charSequence) ? 0 : Double.parseDouble(charSequence.toString());
//                }
//            }).subscribe(new Consumer<Double>() {
//                @Override
//                public void accept(Double aDouble) throws Exception {
//
//                }
//            });
            if (mEditSelectPrice.getText().toString().equals("-")) {
                ToastUtil.show(getString(R.string.str_otc_apply_ge_error));
                return;
            }
//            if (Double.parseDouble(mEditSelectPrice.getText().toString()) > 50 || Double.parseDouble(mEditSelectPrice.getText().toString()) < (-50)) {
//                ToastUtil.show(getString(R.string.str_otc_assener_over_price));
//                return;
//            }
            if (Double.parseDouble(mEdUpInput.getText().toString().trim()) <= Double.parseDouble(mEdUpDown.getText().toString().trim())) {
                ToastUtil.show(getString(R.string.str_otc_assener_up_must_down));
                return;
            }
            if (buyOrSell == 3) {
                requestExchangeBuyOrSell(String.valueOf(buyOrSellId), buyOrSellCoinId, 1);
                return;
            } else if (buyOrSell == 4) {
                requestExchangeBuyOrSell(String.valueOf(buyOrSellId), buyOrSellCoinId, 2);
                return;
            }
            requestExchangeBuyOrSell("", coinId, buyOrSell);

        } else {
        }
    }

    private void requestExchangeBuyOrSell(String id, int coinCodeId, int buyOrSellDire) {
        OTCAddCurrParam otcAddCurrParam = new OTCAddCurrParam(id, coinCodeId, Double.parseDouble(mEdUpInput.getText().toString().trim()), Double.parseDouble(mEdUpDown.getText().toString().trim()), buyOrSellDire, mSelectPrice);
        addSubscribe(otcAddBuyCurrProviderServices.requestAddBuyCurrProvider(otcAddCurrParam).subscribeWith(new RxProgressSubscriber<Boolean>(this) {
            @Override
            protected void onError(ResponseThrowable ex) {
                if (ex.getErrorCode().equals("622")) {
                    ToastUtil.show(getString(R.string.str_otc_assener_not_retry_add));//
                } else {
                    ToastUtil.show(getString(R.string.server_connection_error));
                }
            }

            @Override
            public void onSuccess(Boolean checkNewPinBean) {
                EventBus.getDefault().post(new PosInfo(101));
                setResult(2, new Intent().putExtra("result", "refreshList"));
                OTCSelectCurrActivity.this.finish();
            }
        }));
    }

    private void showChooseCoinPopWin() {
        OTCSelectCoinTypePopWindow OTCSelectCoinTypePopWindow = new OTCSelectCoinTypePopWindow(mCtx);
        OTCSelectCoinTypePopWindow.showPopWindowForData(coinBeanList, 1, mOTCCoinListBean);
        OTCSelectCoinTypePopWindow.setOnItemClickListener(((bean, position) -> {
            GlideUtil.loadImageView(mCtx, bean.getLogo(), mImgSelect);
            mOTCCoinListBean = bean;//保存选中的item
            coinId = bean.getId();
            mTvSelect.setText(bean.getCoinCode().toUpperCase());
            mTvCurrType.setText(bean.getCoinCode().toUpperCase());
            mTvCurrTypeOne.setText(bean.getCoinCode().toUpperCase());
            //点击item之后的操作
            refreshClickItemUi();
        }));
    }

    private void refreshClickItemUi() {
        mEditUpHint.setVisibility(View.GONE);
        mEditDownHint.setVisibility(View.GONE);
        mSelectCurrHint.setVisibility(View.GONE);
        mLlSelectCurrName.setVisibility(View.VISIBLE);
        mEdUpInput.setVisibility(View.VISIBLE);
        mTvCurrTypeOne.setVisibility(View.VISIBLE);
        mEdUpDown.setVisibility(View.VISIBLE);
        mTvCurrType.setVisibility(View.VISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mBtnAcceptBuyCurr.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        mBtnAcceptBuyCurr.setTextColor(getResources().getColor(R.color.gray_90));
        mBtnAcceptBuyCurr.setBackgroundResource(R.drawable.item_gray_black);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(mEdUpInput.getText().toString().equals("") || mEdUpDown.getText().toString().equals("")) && mSelectCurrHint.getVisibility() == View.GONE) {
            mBtnAcceptBuyCurr.setEnabled(true);
            mBtnAcceptBuyCurr.setTextColor(getResources().getColor(R.color.white));
            mBtnAcceptBuyCurr.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
        }
    }
}
