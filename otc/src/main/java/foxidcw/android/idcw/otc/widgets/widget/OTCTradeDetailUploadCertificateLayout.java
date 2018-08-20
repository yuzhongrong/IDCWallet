package foxidcw.android.idcw.otc.widgets.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.ui.view.roundimge.RoundImageView;
import com.cjwsc.idcm.iprovider.UploadImgProviderServices;
import com.cjwsc.idcm.iprovider.impl.UploadImgProviderImpl;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.activitys.OTCPreviewImgListActivity;
import com.cjwsc.idcm.Utils.TakePhotoUtils;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;

import static foxidcw.android.idcw.otc.model.OTCOrderStatus.*;

/**
 *
 * @author yiyang
 */
public class OTCTradeDetailUploadCertificateLayout extends LinearLayout implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private CommonAdapter<String> mAdapter;
    private View mTvUpload;
    private View mIvAddCert;
    private LinearLayout mContainer;
    private TextView mTvPrompt;
    private int mSize;
    private int mMargin;

    private int status;
    private OTCGetOtcOrdersBean mBean;


    public OTCTradeDetailUploadCertificateLayout(Context context) {
        this(context, null);
    }

    public OTCTradeDetailUploadCertificateLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    UploadImgProviderServices mUploadImgProviderServices;
    private void init(Context context) {
        mUploadImgProviderServices = new UploadImgProviderImpl();
        mSize = getResources().getDimensionPixelSize(R.dimen.dp56);
        mMargin = getResources().getDimensionPixelSize(R.dimen.dp12);


        LayoutInflater.from(context).inflate(R.layout.activity_otc_tradedetail_upload_certificate, this);
        mTvUpload = findViewById(R.id.tv_upload);
        mTvUpload.setOnClickListener(this);
        /*mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        mAdapter = new CommonAdapter<String>(R.layout.item_upload_certificate) {
            @Override
            public void commonconvert(BaseViewHolder helper, String item) {
                ImageView view = helper.getView(R.id.iv_content);
                if(helper.getAdapterPosition() == 0 && canEdit){
                        view.setImageResource(R.drawable.otc_add_cert_selector);
                        //                    GlideUtil.loadImageView(context, R.drawable.otc_add_cert_selector, view);
                        if (getData().size() > 3) {
                            view.setEnabled(false);
                        } else {
                            view.setEnabled(true);
                        }
                }else {
                    GlideUtil.loadImageView(context, item, view);
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position == 0){
                    TakePhotoUtils.showDialog((FragmentActivity) context, new TakePhotoUtils.CallBack() {
                        @Override
                        public void onActivityResult(String path) {
                            mAdapter.addData(path);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }else {
                    List<String> data = new ArrayList<>(mAdapter.getData());
                    data.remove(0);
                    OTCPreviewImgListActivity.preview((Activity) context, true, position-1, data, new OTCPreviewImgListActivity.CallBack() {
                        @Override
                        public void onListChanged(List<String> paths) {
                            paths.add(0,"");
                            mAdapter.setNewData(paths);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        //第一个按钮
        mAdapter.addData("");*/
        mIvAddCert = findViewById(R.id.iv_add_cert);
        mIvAddCert.setOnClickListener(this);
        mContainer = (LinearLayout) findViewById(R.id.layout_cert_container);
        refreshAddHintTextView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
//        OTCGetOtcOrdersBean otcGetOtcOrdersBean = new OTCGetOtcOrdersBean();
//        otcGetOtcOrdersBean.setStatus(UploadPayCertficate);
//        ArrayList<String> certificateImages = new ArrayList<>();
//        certificateImages.add("http://ws3.sinaimg.cn/mw600/006ZKreuly1fr77s50bqzj30jg0ltjsr.jpg");
//        certificateImages.add("http://ws3.sinaimg.cn/mw600/00727KmKgy1fr77rgs5zej30rs15o7wh.jpg");
//        otcGetOtcOrdersBean.setCertificateImages(certificateImages);
//        otcGetOtcOrdersBean.setDirection(1);
//        onOrderChanged(otcGetOtcOrdersBean);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscriber(tag = EventTags.TAGS_STATUS_CHANGE)
    public void onOrderChanged(OTCGetOtcOrdersBean bean){

        mBean = bean;
        updateStatus(bean.getStatus(), bean.getCertificateImages(), bean.getDirection()==1);

    }

    private void updateStatus(int status, List<String> certificateImages, boolean isBuy){
        if(status == WaitApproval || status == CustomerRefund || status == CustomerPayCoin){
            setVisibility(VISIBLE);
            setCertViews(certificateImages);
            unEdit();
        }else if(status == UploadPayCertficate && isBuy){
            //等待上传并且是买方才可编辑
            setVisibility(VISIBLE);
        }else {
            setVisibility(GONE);
        }
    }

    private void refreshAddHintTextView(){
        mTvUpload.setEnabled(true);
        if(mContainer.getChildCount()==0){
            if(mTvPrompt == null) {
                mTvPrompt = new TextView(getContext());
                mTvPrompt.setTextColor(ContextCompat.getColor(getContext(), R.color.color_999999));
                mTvPrompt.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp12));
                mTvPrompt.setText(R.string.add_cert_prompt);
                MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.MATCH_PARENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dp12);
                mTvPrompt.setLayoutParams(params);
                mTvPrompt.setGravity(Gravity.CENTER_VERTICAL);
            }
            mContainer.addView(mTvPrompt);
            mTvUpload.setEnabled(false);
        }else if(mContainer.getChildCount()>1){
            mContainer.removeView(mTvPrompt);
        }
    }

    private void setCertViews(List<String> paths){
        mDatas.clear();
        mContainer.removeAllViews();
        if(paths!=null)
            for (String path  : paths) {
                View view = addCertView(path);
            }
        refreshCanAddCert();
    }

    private List<String> mDatas = new ArrayList<>();
    @NonNull
    private View addCertView(String path) {
        mDatas.add(path);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_upload_certificate, null);
        ImageView imageView = (RoundImageView) view.findViewById(R.id.iv_content);
        MarginLayoutParams params = new MarginLayoutParams(mSize, mSize);
        params.leftMargin = mMargin;
        imageView.setLayoutParams(params);
        GlideUtil.loadImageView(getContext(), path, imageView);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OTCPreviewImgListActivity.preview((Activity) getContext(), canEdit, mContainer.indexOfChild(view), mDatas, new OTCPreviewImgListActivity.CallBack() {
                    @Override
                    public void onListChanged(List<String> paths) {
                        setCertViews(paths);
                        refreshAddHintTextView();
                    }
                });
            }
        });
        mContainer.addView(view);
        refreshAddHintTextView();
        return view;
    }

    private boolean canEdit = true;
    @Override
    public void onClick(View v) {
        /*canEdit = false;
        mAdapter.remove(0);
        mAdapter.notifyDataSetChanged();
        mTvUpload.setVisibility(GONE);*/
        int id = v.getId();
        if(id ==R.id.iv_add_cert){
            TakePhotoUtils.showDialog((FragmentActivity) getContext(), new TakePhotoUtils.CallBack() {
                @Override
                public void onActivityResult(String path) {
                    addCertView(path);
                    refreshCanAddCert();
                }
            });
        }else if(id == R.id.tv_upload){
            if(mDatas.size()==0)return;
            if(getContext() instanceof BaseWalletActivity){
                HashMap<String, String> params = new HashMap<>();
                params.put("orderId", String.valueOf(mBean.getId()));
                mUploadImgProviderServices.requestUpload(params, mDatas)
                        .compose(((BaseWalletActivity) getContext()).bindToLifecycle())
                        .subscribe(new RxProgressSubscriber<List<String>>((BaseProgressView) getContext()) {
                            @Override
                            public void onSuccess(List<String> data) {
//                                Toast.makeText(getContext(), "data:" + data, Toast.LENGTH_SHORT).show();
                                setCertViews(data);
                                unEdit();
                                ToastUtil.show(getContext().getString(R.string.otc_upload_success));
                            }

                            @Override
                            protected void onError(ResponseThrowable ex) {
                                ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                            }
                        });

            }
        }
    }

    private void unEdit() {
        mIvAddCert.setVisibility(GONE);
        mTvUpload.setVisibility(GONE);
        canEdit = false;
        if(mContainer.getChildCount()>0){
            View childAt = mContainer.getChildAt(0);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childAt.getLayoutParams();
            layoutParams.leftMargin = 0;
            childAt.setLayoutParams(layoutParams);
        }
        mContainer.setGravity(Gravity.CENTER);
    }

    private void refreshCanAddCert() {
        if(mContainer.getChildCount()>2){
            mIvAddCert.setEnabled(false);
        }else {
            mIvAddCert.setEnabled(true);
        }
    }
}
