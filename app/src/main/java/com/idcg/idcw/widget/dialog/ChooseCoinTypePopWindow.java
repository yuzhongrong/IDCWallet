package com.idcg.idcw.widget.dialog;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.CoinBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;

import java.util.Collections;
import java.util.List;

import foxidcw.android.idcw.otc.utils.OTCGridBottomInAnimUtils;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by admin on 2018/3/22.
 */

public class ChooseCoinTypePopWindow extends BasePopupWindow {
    private CommonAdapter<CoinBean> adapter;
    private OnCoinTypeClickListener mListener;

    public ChooseCoinTypePopWindow(Context context) {
        super(context);
    }

    private ImageView mIvClose;
    private TextView mTvTitle;
    private RecyclerView mRvAsset;

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
        View root = createPopupById(R.layout.pop_choose_wallet_type);

        mIvClose = (ImageView) root.findViewById(R.id.iv_close);
        mTvTitle = (TextView) root.findViewById(R.id.tv_title);
        mRvAsset = (RecyclerView) root.findViewById(R.id.rv_asset);

        mRvAsset.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter = new CommonAdapter<CoinBean>(R.layout.item_pop_wallet_type) {
            @Override
            public void commonconvert(BaseViewHolder helper, CoinBean item) {
                View view = helper.getView(R.id.item_root);
                //如果当前条目与已经选择的是同一个则，显示为select状态
                if (select != null && item.getCoin().equals(select.getCoin())) {
                    view.setSelected(true);
                } else {
                    view.setSelected(false);
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
                helper.setText(R.id.tv_type, item.getUpperCaseCoin());
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
                    mListener.onClick(ChooseCoinTypePopWindow.this.adapter.getItem(position));
            }
        });
        mRvAsset.setAdapter(adapter);


        return root;
    }

    public void showPopWindowForData(List<CoinBean> datas, String title) {
        mTvTitle.setText(title);
        //超过四行，限制高度
        if(datas.size()>16)
            //320是由item_layout计算出来 10*6(四排中间的间隔)+10*2(上下间隔)+62*4(四排高度)
            mRvAsset.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelSize(R.dimen.dp1)*328));
        adapter.setNewData(datas);
        showPopupWindow();
    }

    private CoinBean select;
    private CoinBean anotherSelect;

    public void showPopWindowForData(List<CoinBean> datas, String title, CoinBean select, CoinBean anotherSelect) {
        mTvTitle.setText(title);
        if(!datas.contains(anotherSelect))
            datas.add(0,anotherSelect);
        Collections.sort(datas);
        if(datas.size()>16)
            //320是由item_layout计算出来 10*6(四排中间的间隔)+10*2(上下间隔)+62*4(四排高度)
            mRvAsset.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelSize(R.dimen.dp1)*328));
        adapter.setNewData(datas);
        this.select = select;
        this.anotherSelect = anotherSelect;
        showPopupWindow();
        mRvAsset.setLayoutAnimation(OTCGridBottomInAnimUtils.initAnimationController());
    }

    public void setOnItemClickListener(OnCoinTypeClickListener listener) {
        mListener = listener;
    }

    public interface OnCoinTypeClickListener {
        void onClick(CoinBean bean);
    }

    @Override
    public View initAnimaView() {
        return null;
    }
}
