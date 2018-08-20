package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;

import java.util.List;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.utils.OTCGridBottomInAnimUtils;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hpz on 2018/4/28.
 */

public class OTCSelectCoinTypePopWindow extends BasePopupWindow {

    private CommonAdapter<OTCCoinListBean> adapter;
    private OnCoinTypeClickListener mListener;
    private int selectType;

    public OTCSelectCoinTypePopWindow(Context context) {
        super(context);
    }

    private ImageView mIvClose;
    private TextView mTvTitle;
    private RecyclerView mRvAsset;
    private OTCCoinListBean mCurrentSelect;
    private static final String BTC = "btc";

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return mIvClose;
    }

    @Override
    public View onCreatePopupView() {
        View root = createPopupById(R.layout.pop_select_wallet_type);

        mIvClose = (ImageView) root.findViewById(R.id.iv_close);
        mTvTitle = (TextView) root.findViewById(R.id.tv_title);
        mRvAsset = (RecyclerView) root.findViewById(R.id.rv_asset);

        mRvAsset.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter = new CommonAdapter<OTCCoinListBean>(R.layout.item_select_pop_type) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCCoinListBean item) {
                View view = helper.getView(R.id.item_root);
                //如果当前条目与已经选择的是同一个则，显示为select状态
                if (selectType == 1) {
                    if (mCurrentSelect != null && item.getCoinCode().equals(mCurrentSelect.getCoinCode())) {
                        view.setSelected(true);
                    } else {
                        view.setSelected(false);
                    }
                } else {
                    if (mCurrentSelect != null && item.getLocalCurrencyCode().equals(mCurrentSelect.getLocalCurrencyCode())) {
                        view.setSelected(true);
                    } else {
                        view.setSelected(false);
                    }
                }

//                //逻辑1：如果当前条目是btc并且另外一个是选择了btc则不让选择
//                if (BTC.equals(item.getCurrency()) && anotherSelect != null && BTC.equals(anotherSelect.getCurrency()
//                )) {
//                    view.setEnabled(false);
//                } else {
//                    view.setEnabled(true);
//                }
                //逻辑2：如果当前是btc,那当前只能选择Btc
//                if(select!=null && BTC.equals(select.getCurrency()) && !item.getCurrency().equals(BTC)){
//                    view.setEnabled(false);
//                }else {
//                    view.setEnabled(true);
//                }

                if (view.isEnabled())
                    helper.addOnClickListener(R.id.item_root);
                if (selectType == 1) {
                    helper.setText(R.id.tv_type, item.getCoinCode().toUpperCase());
                } else if (selectType == 2) {
                    helper.setText(R.id.tv_type, item.getLocalCurrencyCode().toUpperCase());
                }

                GlideUtil.loadImageView(mContext, item.getLogo(), (ImageView) helper.getView
                        (R.id.iv_type));
            }
        };
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                dismiss();
                if (view.isSelected())
                    return;
                if (null != mListener)
                    mListener.onClick(OTCSelectCoinTypePopWindow.this.adapter.getItem(position), position);
            }
        });
        mRvAsset.setAdapter(adapter);
        return root;
    }

    public void showPopWindowForData(List<OTCCoinListBean> datas, int type) {
        //mTvTitle.setText(title);
        selectType = type;
        //超过四行，限制高度
        if (datas.size() > 16)
            //320是由item_layout计算出来 10*6(四排中间的间隔)+10*2(上下间隔)+62*4(四排高度)
            mRvAsset.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelSize(R.dimen.dp1) * 328));
        adapter.setNewData(datas);
        showPopupWindow();
    }

    public void showPopWindowForData(List<OTCCoinListBean> datas, int type, OTCCoinListBean select) {
        this.mCurrentSelect = select;
        selectType = type;
        //超过四行，限制高度
        if (datas.size() > 16)
            //320是由item_layout计算出来 10*6(四排中间的间隔)+10*2(上下间隔)+62*4(四排高度)
            mRvAsset.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelSize(R.dimen.dp1) * 328));
        adapter.setNewData(datas);
        showPopupWindow();
        // 设置简单动画
        mRvAsset.setLayoutAnimation(OTCGridBottomInAnimUtils.initAnimationController());
    }

    public void setTitle(String title) {
        mTvTitle.setText(TextUtils.isEmpty(title) ? "" : title);
    }

    public void setOnItemClickListener(OnCoinTypeClickListener listener) {
        mListener = listener;
    }

    public interface OnCoinTypeClickListener {
        void onClick(OTCCoinListBean bean, int position);
    }

    @Override
    public View initAnimaView() {
        return null;
    }

}
