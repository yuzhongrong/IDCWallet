package foxidcw.android.idcw.otc.widgets.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.ToastUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.iprovider.OTCGetPaymentModeManagementServices;
import foxidcw.android.idcw.otc.model.OTCOrderStatus;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import foxidcw.android.idcw.otc.model.beans.OTCSelectPayListBean;
import foxidcw.android.idcw.otc.utils.OTCPayCodeWithLanguageCodeUtils;
import foxidcw.android.idcw.otc.widgets.dialog.OTCQrCodePopWindow;
import foxidcw.android.idcw.otc.widgets.dialog.OTCSelectPaymentMethodPopup;


/**
 * 设计理念：垃圾自回收模式
 * Created by yuzhongrong on 2018/5/4.
 * 此页面是交易详情顶部状态页面，状态由自身管理，避免activity代码逻辑量过大
 */

public class OTCTradeDetailPayTypeLayout extends LinearLayout implements View.OnClickListener {
    private View root;

    private TextView reference_number;//参考号
    private TextView paytype;
    private ImageButton right_click;
    private LinearLayout rl_select_pay_type;

    private List<OTCSelectPayListBean> mAllPayTypeLists = new ArrayList<>();
    private List<OTCSelectPayListBean> mCacheAllPayTypeLists = new ArrayList<>();
    private List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> mCurrentPaymentMethod; // 当前选中的支付方式

    @Autowired
    OTCGetPaymentModeManagementServices mGetPaymentModeManagementServices;
    private OTCGetOtcOrdersBean mBean;
    private ImageView mIvType;
    private TextView mTvName;
    private TextView mTvAccount;
    private ImageView mIvQrCode;
    private int mQrCodeSize;
    private String mQrCode;
    private LinearLayout mLayoutAccount;
    private int mLineCount=-1;

    public OTCTradeDetailPayTypeLayout(Context context) {
        super(context);
        EventBus.getDefault().register(this);
        initView();
    }

    public OTCTradeDetailPayTypeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mQrCodeSize = getContext().getResources().getDimensionPixelSize(R.dimen.dp30);
        ARouter.getInstance().inject(this);
        root = LayoutInflater.from(getContext()).inflate(R.layout.activity_otc_trade_detail_paytype_layout, this, true);
        mLayoutAccount = $(R.id.layout_account);
        reference_number = $(R.id.reference_number);
        right_click = $(R.id.right_click);
        paytype = $(R.id.paytype);
        rl_select_pay_type = $(R.id.rl_select_pay_type);
        mIvType = $(R.id.iv_type);
        mTvAccount = $(R.id.tv_account);
        mTvName = $(R.id.tv_name);
        mIvQrCode = $(R.id.iv_qr_code);
        right_click.setOnClickListener(this);
        rl_select_pay_type.setOnClickListener(this);
reference_number.setOnClickListener(this);
mTvAccount.setOnClickListener(this);
mIvQrCode.setOnClickListener(this);
        requestPayList();


//        RxTextView.textChanges(mTvAccount)
//                .subscribe(new Consumer<CharSequence>() {
//                    @Override
//                    public void accept(CharSequence charSequence) throws Exception {
//                        int lineCount = mTvAccount.getLineCount();
//                        LayoutParams layoutParams;
//                        if(lineCount >1){
//                            layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//                            mTvAccount.setSingleLine(true);
//                            mTvAccount.setLines(1);
//                            mTvAccount.setEllipsize(TextUtils.TruncateAt.END);
//                        }else {
//                            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                            mTvAccount.setSingleLine(false);
//                        }
//                        mLayoutAccount.setLayoutParams(layoutParams);
//                    }
//                });

    }

    private void requestPayList() {

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscriber(tag = EventTags.TAGS_STATUS_CHANGE)
    public void onOrderChanged(OTCGetOtcOrdersBean bean){
//        boolean isMine = bean.getCreateUserId() == myId;
//        boolean isBuy = isMine == (bean.getDirection() ==1);
        mBean = bean;
        /**
         * 处理银行卡四位空格
         */
        modifyBankCardNo();

        int status = bean.getStatus();
        if(bean.getDirection()!=1){
            setVisibility(GONE);
            return;
        }
        refreshUi();
        switch (status){
            case OTCOrderStatus.WaitPay:
                //可点击
                if(bean.getDirection()==1){//买家
                    setVisibility(VISIBLE);
                    right_click.setVisibility(VISIBLE);
                    rl_select_pay_type.setClickable(true);

                }else if(bean.getDirection()==2){//卖家
                    setVisibility(GONE);
                }

                break;
            case OTCOrderStatus.Finish:
            case OTCOrderStatus.Cancel:
            case OTCOrderStatus.CustomerRefund:
            case OTCOrderStatus.CustomerPayCoin:
                setVisibility(GONE);
                break;
            default:
                setVisibility(VISIBLE);
                right_click.setVisibility(GONE);
                rl_select_pay_type.setClickable(false);
                break;
        }

    }

    private void modifyBankCardNo() {
        for (OTCGetOtcOrdersBean.PaymentModeResponse paymentModeResponse : mBean.getPayments()) {
            if (OTCPayCodeWithLanguageCodeUtils.checkIsBankcard(paymentModeResponse.getPayTypeCode())) {
                String bankNo = paymentModeResponse.getPayAttributes().getAccountNo();
                String newText = "";
                for (int j = 0, length = bankNo.length(); j < length; j++) {
                    newText = newText + bankNo.substring(j, j + 1);
                    if ((j + 1) % 4 == 0 && j != length - 1) {
                        newText = newText + " ";
                    }
                }
                paymentModeResponse.getPayAttributes().setAccountNo(newText);
            }
        }
    }

    private void refreshUi() {
        reference_number.setText(mBean.getPayCertificateNO());
        for (OTCGetOtcOrdersBean.PaymentModeResponse paymentModeResponse : mBean.getPayments()) {
            if(paymentModeResponse.getID().equalsIgnoreCase(String.valueOf(mBean.getPaymentModeId()))){
                GlideUtil.loadCircleImage(getContext(), paymentModeResponse.getPayTypeLogo(), mIvType);
                /*if("AliPay".equalsIgnoreCase(paymentModeResponse.getPayTypeCode())){
                    paytype.setVisibility(GONE);
                    mTvAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp14));
                }else if("Bankcard".equalsIgnoreCase(paymentModeResponse.getPayTypeCode())){
                    paytype.setVisibility(VISIBLE);
                    paytype.setText(paymentModeResponse.getPayAttributes().getBankName());
                    mTvAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp12));
                }*/
                //这里处理成Bankcard时候才显示银行卡，其他情况一路不显示
                if("Bankcard".equalsIgnoreCase(paymentModeResponse.getPayTypeCode())){
                    paytype.setVisibility(VISIBLE);
                    paytype.setText(paymentModeResponse.getPayAttributes().getBankName());
                    mTvAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp12));
                }else {
                    paytype.setVisibility(GONE);
                    mTvAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp14));
                }
                /*Observable.interval(3, 3, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                mTvAccount.setText(new Random().nextBoolean()?"ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss":"sss");
//                                mTvAccount.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//
//                                    @Override
//                                    public boolean onPreDraw() {
//                                        mTvAccount.setSingleLine(false);
//                                        int lineCount = mTvAccount.getLineCount();
//                                        mLineCount = lineCount;
//                                        LayoutParams layoutParams;
//
//                                        if(mLineCount != 1){
//                                            layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//                                            mTvAccount.setSingleLine(true);
//                                            mTvAccount.setEllipsize(TextUtils.TruncateAt.END);
//                                        }else {
//                                            layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                        }
//                                        mLayoutAccount.setLayoutParams(layoutParams);
//                                        mTvAccount.getViewTreeObserver().removeOnPreDrawListener(this);
//                                        return true;
//                                    }
//                                });
                            }
                        });*/
                mTvAccount.setText(paymentModeResponse.getPayAttributes().getAccountNo());
                mTvName.setText(paymentModeResponse.getPayAttributes().getUserName());

                mQrCode = paymentModeResponse.getPayAttributes().getQRCode();
                if(TextUtils.isEmpty(mQrCode)){
                   mIvQrCode.setVisibility(GONE);
                }else {
                    mIvQrCode.setVisibility(VISIBLE);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        //构造收款方式bean对象传给对话框 然后拿结果
        // showPayPopWindow(new Object());
        int id = v.getId();
        if (id == R.id.rl_select_pay_type) {
            OTCSelectPaymentMethodPopup otcSelectPaymentMethodPopup = new OTCSelectPaymentMethodPopup(
                    getContext());
            ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> datas = new ArrayList<>();
            List<OTCGetOtcOrdersBean.PaymentModeResponse> payments = mBean.getPayments();
            for (OTCGetOtcOrdersBean.PaymentModeResponse m  : payments) {
                if(m.getLocalCurrencyId()!=mBean.getLocalCurrencyId()){
                    continue;
                }
                OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse e = new OTCGetPaymentModeManagementResBean
                        .PaytypeLinkageResponse();
                e.setPayTypeLogo(m.getPayTypeLogo());
                try {
                    e.setID(Integer.parseInt(m.getID()));
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                }
                e.setAccountNo(m.getPayAttributes().getAccountNo());
                e.setPayTypeCode(m.getPayAttributes().getUserName());
                datas.add(e);
            }
            if(datas.size()==0){
                return;
            }
            HashSet<Integer> selects = new HashSet<>();
            int payTypeId = mBean.getPaymentModeId();
            selects.add(payTypeId);
            otcSelectPaymentMethodPopup.showSelectPaymentWithData(datas,getContext().getString(R.string.otc_choose_opposite_type),
                    selects);
            otcSelectPaymentMethodPopup.setOnPaymentMethodSelectListener(new OTCSelectPaymentMethodPopup.OnPaymentMethodSelectListener() {
                @Override
                public void onPaymentMethodSelected(ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> selectItems) {
                    mBean.setPaymentModeId(selectItems.get(0).getID());
                    refreshUi();
                }
            });
        }else if(id == R.id.reference_number){
            if(TextUtils.isEmpty(reference_number.getText().toString().trim())){
                return;
            }
            ClipboardManager clipboard1 = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData1 = ClipData.newPlainText(null, reference_number.getText().toString().trim());
            clipboard1.setPrimaryClip(clipData1);
            ToastUtil.show(getResources().getString(R.string.dialog_receive));
        }else if(id == R.id.tv_account){
            if(TextUtils.isEmpty(mTvAccount.getText().toString().trim())){
                return;
            }
            ClipboardManager clipboard2 = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData2 = ClipData.newPlainText(null, mTvAccount.getText().toString().trim());
            clipboard2.setPrimaryClip(clipData2);
            ToastUtil.show(getResources().getString(R.string.dialog_receive));
        }else if(id == R.id.iv_qr_code){
            new OTCQrCodePopWindow(getContext()).showPop(mQrCode);
        }
    }


    private void showPayPopWindow(Object bean) {
    }


    public <T extends View> T $(@IdRes int resId) {
        return (T) root.findViewById(resId);
    }

}
