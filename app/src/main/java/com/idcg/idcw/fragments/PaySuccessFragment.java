package com.idcg.idcw.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.idcg.idcw.R;
import com.idcg.idcw.configs.ClientConfig;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.xuelianx.fingerlib.bean.EventBean;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Created by hpz on 2017/12/29.
 */

public class PaySuccessFragment extends DialogFragment {

    private RelativeLayout rePayWay, rePayDetail, reBalance;
    private LinearLayout LinPayWay, linPass;
    private ListView lv;
    private Button btnPay;
    private EditText gridPasswordView;
    private ImageView imageCloseOne, imageCloseTwo, imageCloseThree;

    private TextView img_close;
    private TextView tv_time_count;
    private String msg;
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_pay_success);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.AnimRightBottom);
            final WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
            window.setAttributes(lp);
        }
        initView(dialog);


        if (getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface anInterface, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (!TextUtils.isEmpty(gridPasswordView.getText().toString().trim())) {

                        }
                    } else {
                        ToastUtil.show(getString(R.string.pass_not_null));
                    }
                    return false;
                }
            });

        }


        return dialog;
    }

    private void initView(Dialog dialog) {
        img_close = (TextView) dialog.findViewById(R.id.img_close);
        tv_time_count = (TextView) dialog.findViewById(R.id.tv_time_count);
        img_close.setOnClickListener(listener);
        countDownTimer = new CountDownTimer(4000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                LogUtil.e("PafSuccessFragment:", "seconds remaining: " + millisUntilFinished / 1000);
                tv_time_count.setText(millisUntilFinished / 1000 + getString(R.string.miao)+getString(R.string.go_back_to_assets) + " " + ClientConfig.Instance().getSaveBtc() + " " + getString(R.string.assets_page) + " ");
            }

            public void onFinish() {
                getDialog().dismiss();
                EventBus.getDefault().post(new PosInfo(10));
                LogUtil.e("PafSuccessFragment:", "done!");
            }
        }.start();
    }

    View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.img_close://
                countDownTimer.cancel();
                getDialog().dismiss();
                EventBus.getDefault().post(new PosInfo(10));
                break;
            default:
                break;
        }
    };

    @Subscriber
    public void onEventInfo(EventBean eventBean) {
        try {
            if (eventBean == null) return;
            msg = eventBean.getMsg();
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
