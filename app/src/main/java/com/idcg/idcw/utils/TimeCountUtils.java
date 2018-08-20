package com.idcg.idcw.utils;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hpz on 2018/3/27.
 */

public class TimeCountUtils extends CountDownTimer {
    private TextView button;
    private String title;

    public TimeCountUtils(long millisInFuture, long countDownInterval, TextView button, String title) {
        super(millisInFuture, countDownInterval);
        this.button = button;
        this.title = title;
    }

    public TimeCountUtils(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    @Override
    public void onTick(long l) {
        //button.setBackgroundResource(R.drawable.hx_ripple_gray_bg);
        button.setEnabled(false);
    }

    @Override
    public void onFinish() {
        //button.setText(title);
        //button.setBackgroundResource(R.drawable.hx_ripple_btn_bg);
        button.setEnabled(true);
    }
}

