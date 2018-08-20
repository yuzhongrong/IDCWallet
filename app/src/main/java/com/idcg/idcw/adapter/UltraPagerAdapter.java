package com.idcg.idcw.adapter;

import android.support.v4.view.PagerAdapter;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.WalletAssetBean;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by hpz on 2018/3/14.
 */

public class UltraPagerAdapter extends PagerAdapter {
    private boolean isMultiScr;
    private List<WalletAssetBean> mDatas;
    private int mCurrentSelectPosition;

    public int getmCurrentSelectPosition() {
        return mCurrentSelectPosition;
    }

    public void setmCurrentSelectPosition(int mCurrentSelectPosition) {
        this.mCurrentSelectPosition = mCurrentSelectPosition;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int positiong);
    }

    public void setOnItemListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }


    public UltraPagerAdapter(boolean isMultiScr, List<WalletAssetBean> mDatas) {
        this.isMultiScr = isMultiScr;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        RelativeLayout linearLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.recyc_item, null);
        //new LinearLayout(container.getContext());
        WalletAssetBean itemBean = mDatas.get(position);
        RelativeLayout relativeLayout = (RelativeLayout) linearLayout.findViewById(R.id.item_rootviw);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.loop_img);
        Glide.with(container.getContext()).load(itemBean.getLogo_url()).into(imageView);
        TextView textView = (TextView) linearLayout.findViewById(R.id.loop_str);
        textView.setText(itemBean.getCurrency().toUpperCase());

        //relativeLayout.setBackgroundColor(Color.parseColor("#FFFF00"));
        LogUtil.e("----->", " mCurrentSelectPosition ===  " + mCurrentSelectPosition);
        LogUtil.e("----->", itemBean.getCurrency().toUpperCase() + "    position ===  " + position);

        View lineView = linearLayout.findViewById(R.id.trade_d_line);
        if (mDatas.get(position).isSelect()) {//mCurrentSelectPosition - 1 == position
            relativeLayout.setBackgroundResource(R.drawable.selected_trad_bg);
            imageView.setAlpha(1f);
            lineView.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setBackgroundResource(0);
            imageView.setAlpha(0.5f);
            lineView.setVisibility(View.GONE);
        }
        container.addView(linearLayout);
        linearLayout.setOnClickListener(v -> {
            if (onItemClickListener != null) onItemClickListener.onItemClick(position);
        });

        return linearLayout;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //   LinearLayout view = (v) object;
        container.removeView((View) object);
    }

    public void setNewData(List<WalletAssetBean> data) {
        mDatas = data;
        notifyDataSetChanged();
    }
//
//
//    public List<T> getmDatas(){
//
//        return mDatas;
//    }
}
