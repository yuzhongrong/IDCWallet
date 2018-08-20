package com.idcg.idcw.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.iprovider.GetExchangeDetailProviderServices;
import com.idcg.idcw.model.bean.ExchangeDetailBean;
import com.idcg.idcw.model.params.EditCommentReqParam;
import com.idcg.idcw.utils.Utils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.base.ui.pagestatemanager.PageManager;
import com.cjwsc.idcm.event.BiBiRecordRefreshEvent;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.simple.eventbus.EventBus;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.common.widget.ClearableEditText;
import io.reactivex.functions.Consumer;

@Route(path = ArouterConstants.FlashRecordDetail, name = "闪兑详情界面")
public class FlashRecordDetailActivity extends BaseWalletActivity {
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.mr_back_layout)
    LinearLayout mMrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView mTvSetName;
    @BindView(R.id.img_code)
    ImageView mImgCode;
    @BindView(R.id.title_bar)
    RelativeLayout mTitleBar;
    @BindView(R.id.tv_out_amount)
    TextView mTvOutAmount;
    @BindView(R.id.tv_out_status)
    TextView mTvOutStatus;
    @BindView(R.id.tv_out_tx_id)
    TextView mTvOutTxId;
    @BindView(R.id.btn_out_block)
    TextView mBtnOutBlock;
    @BindView(R.id.btn_out_copy)
    TextView mBtnOutCopy;
    @BindView(R.id.tv_in_amount)
    TextView mTvInAmount;
    @BindView(R.id.tv_in_status)
    TextView mTvInStatus;
    @BindView(R.id.tv_in_tx_id)
    TextView mTvInTxId;
    @BindView(R.id.btn_in_block)
    TextView mBtnInBlock;
    @BindView(R.id.btn_in_copy)
    TextView mBtnInCopy;
    @BindView(R.id.tv_fee)
    TextView mTvFee;
    @BindView(R.id.tv_rate)
    TextView mTvRate;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.ed_back_up)
    ClearableEditText mEdBackUp;
    @BindView(R.id.btn_finish)
    TextView mBtnFinish;
    @BindView(R.id.ll_edit)
    LinearLayout mLlEdit;
    @BindView(R.id.ll_send)
    LinearLayout mLlSend;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout mSmartrefreshlayout;
    @BindView(R.id.layout_out)
    LinearLayout mLayoutOut;
    @BindView(R.id.layout_in)
    LinearLayout mLayoutIn;
    private ExchangeDetailBean mData;
    private PageManager mPageManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flash_record_detail;
    }

    @Autowired
    GetExchangeDetailProviderServices mGetExchangeDetailProviderServices;
    @Autowired
    String id;

    private static final int degist = 8;

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        autoScrollView(mLlSend, mLlEdit);
        mPageManager = PageManager.init(mSmartrefreshlayout, false, new Runnable() {
            @Override
            public void run() {
                loadData(true);
            }
        });
        mTvSetName.setText(R.string.exchange_detail);
        RxTextView.textChanges(mEdBackUp)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence s) throws Exception {
                        if (mEdBackUp.hasFocus()) {
                            if (s.toString().length() == 50) {
                                ToastUtil.show(getString(R.string.content_not_50));
                            }
                        }
                    }
                });
        mEdBackUp.setOnEditorActionListener((v, actionId, event) -> {
                /*判断是否是“GO”键*/
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
        mEdBackUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //ToastUtils.showToast(SendCurrencyActivity.this,"聚焦");
                    mBtnFinish.setVisibility(View.VISIBLE);
                    mEdBackUp.setSelection(mEdBackUp.getText().toString().length());
                } else {
                    // 此处为失去焦点时的处理内容
                    //ToastUtils.showToast(SendCurrencyActivity.this,"失焦");
                }
            }
        });
        mSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData(false);
            }
        });
        mSmartrefreshlayout.setEnableLoadmore(false);

        loadData(true);
    }

    private void loadData(boolean isShow) {
        if(!isShow)
            mSmartrefreshlayout.finishRefresh();
        addSubscribe(mGetExchangeDetailProviderServices.GetExchangeDetail(id).subscribeWith(new RxProgressSubscriber<ExchangeDetailBean>(this, isShow) {
            @Override
            public void onSuccess(ExchangeDetailBean data) {
                if(data ==null) {
                    mPageManager.showError();
                    return;
                }
                mPageManager.showContent();
                mData = data;
                /*
                 * 0是兑入中。
                 * 1是兑出中，兑入完成。
                 * 2是兑出完成，兑入完成
                 * 3是兑出完成，兑入完成
                 * 4是兑出失败，兑入失败
                 * 5是兑出失败，兑入完成
                 * */
                /*if (status == 4) {
                    mLayoutOut.setVisibility(View.GONE);
                    mTvInStatus.setText(R.string.exchange_faild);
                } else if (status == 0) {
                    mLayoutOut.setVisibility(View.GONE);
                    mTvInStatus.setText(String.format(getString(R.string.exchange_doing),data.getConfirmCount(),data.getMinCount()));
                }else if(status == 1){
                    mTvOutStatus.setText(String.format(getString(R.string.exchange_doing), data.getToConfirmCount(),data.getToMinCount()));
                    mTvInStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getConfirmCount())));
                }else if(status == 2 || status == 3){
                    mTvOutStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getToConfirmCount())));
                    mTvInStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getConfirmCount())));
                }else if(status == 5){
                    mTvOutStatus.setText(R.string.exchange_faild);
                    mTvInStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getConfirmCount())));
                }*/
                /*
                 * 0是兑出中。
                 * 1是兑入中，兑出完成。
                 * 2是兑入完成，兑出完成
                 * 3是兑入完成，兑出完成
                 * 4是兑入失败，兑出失败
                 * 5是兑入失败，兑出完成
                 * */
                /*int status = data.getStatus();
                if (status == 4) {
                    mLayoutIn.setVisibility(View.GONE);
                    mTvOutStatus.setText(R.string.exchange_faild);
                } else if (status == 0) {
                    mLayoutIn.setVisibility(View.GONE);
                    mTvOutStatus.setText(String.format(getString(R.string.exchange_doing),data.getConfirmCount(),data.getMinCount()));
                }else if(status == 1){
                    mTvInStatus.setText(String.format(getString(R.string.exchange_doing), data.getToConfirmCount(),data.getToMinCount()));
                    mTvOutStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getConfirmCount())));
                }else if(status == 2 || status == 3){
                    mTvInStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getToConfirmCount())));
                    mTvOutStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getConfirmCount())));
                }else if(status == 5){
                    mTvInStatus.setText(R.string.exchange_faild);
                    mTvOutStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getConfirmCount())));
                }*/
                int inStatus = data.getInStatus();
                if(inStatus ==0 || inStatus == 3){//兑入中
                    if(data.getConfirmCount()>=data.getMinCount()&&data.getConfirmCount()!=0){
                        mTvOutStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getConfirmCount())));
                    }else {
                        mTvOutStatus.setText(String.format(getString(R.string.exchange_doing), data.getConfirmCount(), data.getMinCount()));
                    }
                }else if(inStatus == 1){//兑入确认
                    mTvOutStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getConfirmCount())));
                }else if(inStatus == 2){//兑入失败
                    mTvOutStatus.setText(R.string.exchange_faild);
                }
                int outStatus = data.getOutStatus();
                if(outStatus ==0 || outStatus == 3){//兑出中
                    if(data.getToConfirmCount()>=data.getToMinCount()&&data.getToConfirmCount()!=0){
                        mTvInStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getToConfirmCount())));
                    }else {
                        mTvInStatus.setText(String.format(getString(R.string.exchange_doing), data.getToConfirmCount(), data.getToMinCount()));
                    }
                }else if(outStatus == 1){//兑出确认
                    mTvInStatus.setText(String.format(getString(R.string.exchange_complete_with_count), getCountString(data.getToConfirmCount())));
                }else if(outStatus == 2){//兑出失败
                    mTvInStatus.setText(R.string.exchange_faild);
                }
                mTvOutAmount.setText(""+ StringUtils.subZeroAndDot(BigDecimal.valueOf(data.getAmount()).toString()) + " " + data.getUpperCaseCurrency());
                mTvInAmount.setText(""+StringUtils.subZeroAndDot(BigDecimal.valueOf(data.getToAmount()).toString()) + " " + data.getUpperCaseToCurrency());
                mTvOutTxId.setText(data.getTxId());
                mTvInTxId.setText(data.getToTxId());
                mTvFee.setText(""+BigDecimal.valueOf(data.getFee()) + " " + data.getUpperCaseUnit());
                mTvRate.setText(String.format("1 %s = %s %s", data.getUpperCaseCurrency(), Utils
                        .toNoPointSubStringDegistIfDegistIsZero(data.getExchangeRate(), data.getRateDigit()), data.getUpperCaseToCurrency()));
                mTvTime.setText(data.getCreateTime());
                mEdBackUp.setText(data.getComment());
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mPageManager.showError();
            }
        }));
    }

    private String getCountString(int count) {
        return count>1000?"1000+": String.valueOf(count);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    private int scrollToPosition = 0;

    private void autoScrollView(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被遮挡的高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于150，则键盘显示d
//                if (rootInvisibleHeight > 150) {
                //3.29修复，有些下边有导航栏的会把导航栏认为是输入框，这里把值调高一点
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
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().post(new BiBiRecordRefreshEvent());
    }

    public static String doubleTrans(double d){
        if(Math.round(d)-d==0){
            return String.valueOf((long)d);
        }
        return String.valueOf(d);
    }
    @OnClick({R.id.mr_back_layout, R.id.btn_out_block, R.id.btn_out_copy, R.id.btn_in_block, R.id.btn_in_copy, R.id
            .btn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                finish();
                break;
            case R.id.btn_out_block:
                if (mData == null)
                    return;
                String blockViewUrl = mData.getBlockViewUrl();
                if (TextUtils.isEmpty(blockViewUrl))
                    return;
                toUrl(blockViewUrl);
                break;
            case R.id.btn_in_block:
                if (mData == null)
                    return;
                String toBlockViewUrl = mData.getToBlockViewUrl();
                if (TextUtils.isEmpty(toBlockViewUrl))
                    return;
                toUrl(toBlockViewUrl);
                break;
            case R.id.btn_finish:
                hideSoftKeyBoard(view.getWindowToken());
                mBtnFinish.setVisibility(View.GONE);
                mEdBackUp.clearFocus();
                requestEditComment();
                break;
            case R.id.btn_out_copy:
                if(TextUtils.isEmpty(mTvOutTxId.getText().toString().trim()))
                    return;
                ClipboardManager clipboard1 = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData1 = ClipData.newPlainText(null, mTvOutTxId.getText().toString().trim());
                clipboard1.setPrimaryClip(clipData1);
                ToastUtil.show(getString(R.string.dialog_receive));
                break;
            case R.id.btn_in_copy:
                if(TextUtils.isEmpty(mTvInTxId.getText().toString().trim()))
                    return;
                ClipboardManager clipboard2 = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData2 = ClipData.newPlainText(null, mTvInTxId.getText().toString().trim());
                clipboard2.setPrimaryClip(clipData2);
                ToastUtil.show(getString(R.string.dialog_receive));
                break;
        }
    }

    private void requestEditComment() {
        if(mData==null)
            return;
        String comment = mEdBackUp.getText().toString().trim();
        EditCommentReqParam param = new EditCommentReqParam();
        param.setComment(comment);
        param.setId(mData.getId());
        addSubscribe(mGetExchangeDetailProviderServices.editExchangeComment(param).subscribeWith(new RxProgressSubscriber<Boolean>(this) {
            @Override
            public void onSuccess(Boolean data) {
                if(data){
//                    ToastUtil.show(getString(R.string.edit_exchange_comment_success));
                    mData.setComment(comment);
                }else {
                    ToastUtil.show(getString(R.string.server_connection_error));
//                    ToastUtil.show(getString(R.string.edit_exchange_comment_faild));
                    mEdBackUp.setText(mData.getComment());
                }
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                ToastUtil.show(ex.getErrorMsg());
            }
        }));
    }

    private void toUrl(String blockViewUrl) {
        Uri uri = Uri.parse(blockViewUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void hideSoftKeyBoard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }
}
