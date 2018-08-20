package com.idcg.idcw.widget.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.idcg.idcw.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hpz on 2018/4/9.
 */

public class DeviceInterceptorPopWindow extends BasePopupWindow {
    private TextView mIvClose;
    private TextView mTvContent;

    public DeviceInterceptorPopWindow(Context context) {
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

    @Override
    public View onCreatePopupView() {
        View root = createPopupById(R.layout.activity_device_interceptor);
        mIvClose = (TextView) root.findViewById(R.id.btn_back_close);
        mTvContent = (TextView) root.findViewById(R.id.tv_device_content);
        return root;
    }

    public TextView getClickSetting() {
        return mIvClose;
    }

    public void showWithText(String content){
        mTvContent.setText(content);
        showPopupWindow();
    }
    @Override
    public View initAnimaView() {
        return null;
    }
}
