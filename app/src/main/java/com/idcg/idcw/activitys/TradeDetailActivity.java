package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.idcg.idcw.model.bean.TxParmInfoBean;
import com.idcg.idcw.model.logic.RequestCommonLogic;
import com.idcg.idcw.model.params.RequestCommonParam;
import com.idcg.idcw.presenter.impl.RequestCommonImpl;
import com.idcg.idcw.presenter.interfaces.TradeDetailContract;
import com.idcg.idcw.utils.SmartRefreshUtil;

import foxidcw.android.idcw.common.utils.StringUtils;

import com.idcg.idcw.utils.Utils;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ScreenUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.widget.ClearableEditText;

/**
 * Created by hpz on 2017/12/20.
 */
@Route(path = ArouterConstants.TradeDetail)
public class TradeDetailActivity extends BaseWalletActivity<RequestCommonLogic, RequestCommonImpl> implements TextWatcher, View.OnClickListener, TradeDetailContract.View {
    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_receive_address)
    TextView tvReceiveAddress;
    @BindView(R.id.tv_send_address)
    TextView tvSendAddress;
    @BindView(R.id.tx_id)
    TextView txId;
    @BindView(R.id.btn_copy)
    TextView btnCopy;
    @BindView(R.id.tv_free)
    TextView tvFree;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ed_back_up)
    ClearableEditText edBackUp;
    @BindView(R.id.img_send_receive)
    ImageView imgSendReceive;
    @BindView(R.id.ll_send)
    LinearLayout llSend;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.btn_block)
    TextView btnBlock;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.str_zhuan_catch)
    TextView strZhuanCatch;
    private int type;
    private String txIds;
    private String currency;
    private String des;
    private String value = "";
    private List<TxParmInfoBean> txDetailList = new ArrayList<>();
    private static final int INFO_AL_WHAT_UNM = 0x2432;
    private int tradeId;
    private int tradeType;
    private String newTxID;
    private boolean isJump;
    private String urlTx;
    private Dialog dialog;
    private View inflater;
    private LinearLayout tv_sure;
    private TextView tv_catch_url;
    private boolean isToken;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trade_detail;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        autoScrollView(llSend, llEdit);
        tvSetName.setText(R.string.trade_deatil);
        btnFinish.setVisibility(View.GONE);
        LoginStatus bean = LoginUtils.getLoginBean(this);
        if (bean != null) {
            value = bean.getTicket();
        }
        Intent intent = getIntent();
        Bundle bundle1 = intent.getBundleExtra("trade");
        type = bundle1.getInt("tradeTypes");
        currency = bundle1.getString("currencys");
        txIds = bundle1.getString("ids");
        des = bundle1.getString("des");
        isToken = bundle1.getBoolean("istoken");
        edBackUp.addTextChangedListener(this);

        //initCatchDialog();

        edBackUp.setOnEditorActionListener((v, actionId, event) -> {
                /*判断是否是“GO”键*/
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        edBackUp.setOnFocusChangeListener((v,hasFocus)-> {
                if (hasFocus) {
                    //ToastUtils.showToast(SendCurrencyActivity.this,"聚焦");
                    btnFinish.setVisibility(View.VISIBLE);
                    edBackUp.setSelection(edBackUp.getText().toString().length());
                } else {
                    // 此处为失去焦点时的处理内容
                    //ToastUtils.showToast(SendCurrencyActivity.this,"失焦");
                }
        });

        smartrefreshlayout.setOnRefreshListener(refreshlayout ->  {
                RequestTxIdData();
        });
        smartrefreshlayout.setEnableLoadmore(false);

    }

    @Override
    protected void onEvent() {
        RequestTxIdData();
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    private void initCatchDialog() {
        dialog = new Dialog(this, R.style.shuweiDialog);
        inflater = LayoutInflater.from(this).inflate(R.layout.init_catch_dialog, null);
        dialog.setContentView(inflater);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setAttributes(lp);
        tv_sure = (LinearLayout) inflater.findViewById(R.id.mr_back_layout);
        tv_catch_url = (TextView) inflater.findViewById(R.id.catch_url);
        if (!TextUtils.isEmpty(urlTx)) {
            String finalUrl = urlTx.replace("{idcw_txid}", newTxID);
            tv_catch_url.setText(finalUrl);
        }
        tv_sure.setOnClickListener(this);
        dialog.show();
    }

    private int scrollToPosition = 0;

    private void autoScrollView(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(()-> {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被遮挡的高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于150，则键盘显示
                if (rootInvisibleHeight > 200) {
                    //获取scrollToView在窗体的坐标,location[0]为x坐标，location[1]为y坐标
                    int[] location = new int[2];
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域的底部
                    int scrollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    //注意，scrollHeight是一个相对移动距离，而scrollToPosition是一个绝对移动距离
                    scrollToPosition += scrollHeight;
                } else {
                    //键盘隐藏
                    scrollToPosition = 0;
                }
                root.scrollTo(0, scrollToPosition);
        });
    }

    private void RequestCommon() {
        showDialog();
        mPresenter.RequestCommon(new RequestCommonParam(tradeId + "", tradeType + "", currency, edBackUp.getText().toString()));
    }

    private void RequestTxIdData() {
        if (!smartrefreshlayout.isRefreshing()) {
            showDialog();
        }
        mPresenter.RequestTxIdData(new RequestCommonParam(type + "", currency, txIds));
    }

    @OnClick({R.id.mr_back_layout, R.id.btn_copy, R.id.btn_block, R.id.btn_finish, R.id.str_zhuan_catch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.str_zhuan_catch:
                //dialog.show();
                initCatchDialog();
                break;
            case R.id.mr_back_layout:
                EventBus.getDefault().post(new PosInfo(2020));
                this.finish();
                break;
            case R.id.btn_finish:
                hideSoftKeyBoard(view.getWindowToken());
                btnFinish.setVisibility(View.GONE);
                edBackUp.clearFocus();
                RequestCommon();
                break;
            case R.id.btn_block:
                if (isJump && !TextUtils.isEmpty(urlTx)) {
                    String sureUrl = urlTx.replace("{idcw_txid}", newTxID);
                    Uri uri = Uri.parse(sureUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (!isJump && TextUtils.isEmpty(urlTx)) {
                    return;
                }
                break;
            case R.id.btn_copy:
                if (TextUtils.isEmpty(txId.getText().toString().trim())) return;
                ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, txId.getText().toString().trim());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clipData);
                }
                ToastUtil.show(getString(R.string.dialog_receive));
                break;
        }
    }

    private void hideSoftKeyBoard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new PosInfo(2020));
        this.finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (edBackUp.hasFocus()) {
            if (s.toString().length() == 50) {
                ToastUtil.show(getString(R.string.content_not_50));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        SmartRefreshUtil.onUpdateSmartRefresh(smartrefreshlayout);
        if (throwable.getErrorCode().equals("100")) {

            ToastUtil.show(getString(R.string.tv_request_overtime));
        } else {
            ToastUtil.show(getString(R.string.server_connection_error));

        }
    }

    @Override
    public void updateRequestCommon(Object result) {
        dismissDialog();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updateRequestTxIdData(TxParmInfoBean txParmInfo) {
        dismissDialog();
        SmartRefreshUtil.onUpdateSmartRefresh(smartrefreshlayout);
        txDetailList.add(txParmInfo);
        tradeId = txParmInfo.getId();
        tradeType = txParmInfo.getTrade_type();
        isJump = txParmInfo.isIsJump();
        urlTx = txParmInfo.getUrl();

        if (txParmInfo.isTxReceiptStatus()) {
            tvType.setVisibility(View.VISIBLE);
            strZhuanCatch.setVisibility(View.GONE);
            if (txParmInfo.getConfirmations() < txParmInfo.getTotal_confirmations()) {
                tvType.setText(getString(R.string.doing) + "(" + txParmInfo.getConfirmations() + "/" + txParmInfo.getTotal_confirmations() + getString(R.string.one) + ")");
            } else {
                if (txParmInfo.getConfirmations() > 1000) {
                    tvType.setText(getString(R.string.complite) + "(" + "1,000+" + getString(R.string.one) + ")");
                } else {
                    tvType.setText(getString(R.string.complite) + "(" + StringUtils.formatChangeInt(txParmInfo.getConfirmations()) + getString(R.string.one) + ")");
                }
            }
        } else {
            tvType.setVisibility(View.GONE);
            strZhuanCatch.setVisibility(View.VISIBLE);
            tvType.setText(getString(R.string.doing) + "(" + txParmInfo.getConfirmations() + "/" + txParmInfo.getTotal_confirmations() + getString(R.string.one) + ")");
        }
        if (txParmInfo.getTrade_type() == 1) {
            tvCount.setTextColor(getResources().getColor(R.color.tian_tip_blue));
            imgSendReceive.setBackground(getResources().getDrawable(R.mipmap.tag_send));
            if (txParmInfo.isIsToken()) {
                String total = StringUtils.subZeroAndDot(Utils.toSubStringDegist(txParmInfo.getAmount(), 8));
                tvCount.setText("-" + total + " " + txParmInfo.getCurrency().toUpperCase());
            } else {
                String total = StringUtils.subZeroAndDot(Utils.toSubStringDegist(Double.parseDouble(Utils.add(txParmInfo.getAmount(), txParmInfo.getTx_fee())), 8));
                tvCount.setText("-" + total + " " + txParmInfo.getCurrency().toUpperCase());
            }

        } else if (txParmInfo.getTrade_type() == 0) {
            tvCount.setTextColor(getResources().getColor(R.color.main_color));
            imgSendReceive.setBackground(getResources().getDrawable(R.mipmap.tag_receive));
            String totalRec = StringUtils.subZeroAndDot(Utils.toSubStringDegist(txParmInfo.getAmount(), 8));
            tvCount.setText("+" + totalRec + " " + txParmInfo.getCurrency().toUpperCase() + "");
        }
        if (txParmInfo.getTrade_type() == 1) {
            tvReceiveAddress.setText(txParmInfo.getSend_address());
            tvSendAddress.setText(txParmInfo.getReceiver_address());
        } else if (txParmInfo.getTrade_type() == 0) {
            tvReceiveAddress.setText(txParmInfo.getSend_address());
            tvSendAddress.setText(txParmInfo.getReceiver_address());
        }
        txId.setText(txParmInfo.getTxhash());
        txId.setPadding(0, ScreenUtil.dp2px(13, TradeDetailActivity.this), 0, 0);
        newTxID = txParmInfo.getTxhash();
        if (txParmInfo.isIsToken()) {
            if (txParmInfo.getTx_fee() == 0.0) {
                tvFree.setText("-" + " " + "-" + " ");
            } else {
                String totalRec = StringUtils.subZeroAndDot(Utils.toSubStringDegist(txParmInfo.getTx_fee(), 20));
                String free = totalRec;
                if (free.contains("-")) {
                    String s = free.substring(1, free.length());
                    tvFree.setText(s + " " + txParmInfo.getCategoryUnit().toUpperCase() + "");
                } else {
                    tvFree.setText(free + " " + txParmInfo.getCategoryUnit().toUpperCase() + "");
                }
            }
        } else {
            if (txParmInfo.getTx_fee() == 0.0) {
                tvFree.setText("-" + " " + "-" + " ");
            } else {
                String totalRec = StringUtils.subZeroAndDot(Utils.toSubStringDegist(txParmInfo.getTx_fee(), 20));
                String free = totalRec;
                if (free.contains("-")) {
                    String s = free.substring(1, free.length());
                    tvFree.setText(s + " " + txParmInfo.getCurrency().toUpperCase() + "");
                } else {
                    tvFree.setText(free + " " + txParmInfo.getCurrency().toUpperCase() + "");
                }
            }
        }
        tvArea.setText(txParmInfo.getBlockhash());
        if (txParmInfo.getConfirmations() > 1000) {
            tvConfirm.setText("1,000+");
        } else {
            String aaa = StringUtils.formatChangeInt(txParmInfo.getConfirmations());
            tvConfirm.setText(aaa);
        }
        String data = txParmInfo.getCreate_time();
        String confirm = data.replace("T", " ");
        tvTime.setText(confirm);
        edBackUp.setText(txParmInfo.getDescription());

    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
