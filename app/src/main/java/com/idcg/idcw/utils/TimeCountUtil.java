package com.idcg.idcw.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.idcg.idcw.R;
import com.idcg.idcw.app.WalletApplication;
import foxidcw.android.idcw.common.model.bean.PosInfo;


import org.simple.eventbus.EventBus;


/**
 * Created by Administrator on 2017/11/29.
 */

public class TimeCountUtil extends CountDownTimer {
    private TextView mButton;

    public TimeCountUtil(TextView button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mButton = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // 按钮不可用
        mButton.setEnabled(false);
        String showText = WalletApplication.getContext().getString(R.string.review_send) + "("+millisUntilFinished / 1000+")";
        mButton.setText(showText);
        mButton.setTextColor(WalletApplication.getContext().getResources().getColor(R.color.tip_gray));
        //EventBus.getDefault().post(new PosInfo(12));
    }

    @Override
    public void onFinish() {
        // 按钮设置可用
        mButton.setEnabled(true);
        mButton.setText(R.string.tv_get_code);
        EventBus.getDefault().post(new PosInfo(11));
    }
}