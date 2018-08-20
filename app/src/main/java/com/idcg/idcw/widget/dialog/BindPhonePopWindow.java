package com.idcg.idcw.widget.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.idcg.idcw.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hpz on 2018/4/20.
 */

public class BindPhonePopWindow extends BasePopupWindow {

    private TextView mIvClose;
    private TextView mTvContent;
    private TextView btn_go_set;

    public BindPhonePopWindow(Context context) {
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

    public TextView getSkipBindPhone() {
        return btn_go_set;
    }

    @Override
    public View onCreatePopupView() {
        View root = createPopupById(R.layout.activity_bind_phone);
        mIvClose = (TextView) root.findViewById(R.id.btn_back_close);
        mTvContent = (TextView) root.findViewById(R.id.tv_device_content);
        btn_go_set = (TextView) root.findViewById(R.id.btn_go_set);
        return root;
    }

    @Override
    public View initAnimaView() {
        return null;
    }
}
