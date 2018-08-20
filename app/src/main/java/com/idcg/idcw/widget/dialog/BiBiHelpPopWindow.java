package com.idcg.idcw.widget.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.idcg.idcw.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by admin on 2018/3/22.
 */

public class BiBiHelpPopWindow extends BasePopupWindow {

    private ImageView mIvClose;
    private TextView mTvContent;
    private ImageView mTvTitle;

    public BiBiHelpPopWindow(Context context) {
        super(context);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return mIvClose;
    }

//    public TextView getmTvTitle() {
//        return mTvTitle;
//    }

    @Override
    public View onCreatePopupView() {
        View root = createPopupById(R.layout.pop_bibi_help);
        mIvClose = (ImageView) root.findViewById(R.id.iv_close);
        mTvContent = (TextView) root.findViewById(R.id.tv_content);
        mTvTitle = (ImageView) root.findViewById(R.id.tv_title);
        return root;
    }

    public void showWithText(String title, String content){
        //mTvTitle.setText(title);
        mTvContent.setText(content);
        showPopupWindow();
    }
    @Override
    public View initAnimaView() {
        return null;
    }
}
