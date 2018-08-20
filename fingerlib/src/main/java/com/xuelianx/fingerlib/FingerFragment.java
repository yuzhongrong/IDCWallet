package com.xuelianx.fingerlib;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjwsc.idcm.Utils.ACacheUtil;
import com.xuelianx.fingerlib.base.BaseFingerprint;
import com.xuelianx.fingerlib.bean.EventBean;

import org.simple.eventbus.EventBus;

import foxidcw.android.idcw.common.model.bean.PosInfo;


/**
 * Title: FingerFragment
 * Description:
 * Copyright (c) 传化物流版权所有 2016
 * Created DateTime: 2017/9/14 16:28
 * Created by xxl.
 */
public class FingerFragment extends DialogFragment {

    Dialog mDialog;
    LinearLayout ll_btn;
    TextView tv_msg;
    ImageView iv;
    TextView verify_touch_id;
    Button btn_confirm;

    private Callback mCallback;

    private FingerprintIdentify mFingerprintIdentify;

    private int MAX_AVAILABLE_TIMES = 3;//private static final int MAX_AVAILABLE_TIMES = 3;
    private int tagVerify = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.petgirls_dialog);
            mDialog.setContentView(R.layout.fragment_finger);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog.getWindow().setGravity(Gravity.CENTER);
            View view = mDialog.getWindow().getDecorView();
            tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            tv_msg.setText("\"IDCW\"" + getString(R.string.touch_pay));
            verify_touch_id = (TextView) view.findViewById(R.id.verify_touch_id);
            btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
            ll_btn = (LinearLayout) view.findViewById(R.id.ll_btn);
            iv = (ImageView) view.findViewById(R.id.iv);
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    EventBus.getDefault().post(new EventBean("finger"));
                }
            });

            view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onError();
                        dismiss();
                        EventBus.getDefault().post(new EventBean("fingerConfirm"));
                    }
                }
            });
        }

        return mDialog;
    }


    public void setmFragmentCallBack(Callback mFragmentCallBack) {
        this.mCallback = mFragmentCallBack;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFingerprintIdentify != null) {

            mFingerprintIdentify.resumeIdentify();//切换回来重新开启指纹识别
        } else {
            mFingerprintIdentify = new FingerprintIdentify(getActivity().getApplicationContext(), new BaseFingerprint.FingerprintIdentifyExceptionListener() {
                @Override
                public void onCatchException(Throwable exception) {
                    //Toast.makeText(getActivity(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            });
            if (mFingerprintIdentify != null && mFingerprintIdentify.isHardwareEnable()) {
                if (!mFingerprintIdentify.isRegisteredFingerprint()) {
                    Toast.makeText(getActivity(), getString(R.string.set_touch), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.device_not_support), Toast.LENGTH_SHORT).show();
                dismiss();
            }

            start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mFingerprintIdentify.cancelIdentify();//退出到后台的时候不能验证，暂停指纹识别
    }


    public void start() {

        mFingerprintIdentify.startIdentify(MAX_AVAILABLE_TIMES, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                if (mCallback != null) {
                    mCallback.onSuccess();
                }
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onNotMatch(int availableTimes) {
                tagVerify = 1;
                tv_msg.setTextColor(getResources().getColor(R.color.color_FB544B));
                if (availableTimes == 0) {
                    //tv_msg.setText(R.string.verify_error_text);
                    //btn_confirm.setVisibility(View.VISIBLE);
                } else {
                    tv_msg.setText(R.string.verify_failed);
                    tv_msg.setTextColor(getResources().getColor(R.color.color_0000));
                }
                shake(iv);
                shake(tv_msg);
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                //不知道为什么锁屏的时候就会走这个onFailed方法，所以我在后台保存的标志拿出来判断
                if ((TextUtils.isEmpty(ACacheUtil.get(mDialog.getContext()).getAsString("pinPass")) && tagVerify == 0) ||
                        ACacheUtil.get(mDialog.getContext()).getAsString("pinPass") != null && (ACacheUtil.get(mDialog.getContext()).getAsString("pinPass").equals("pinPass") && tagVerify == 0)) {
                    mDialog.dismiss();
                    EventBus.getDefault().post(new PosInfo(333));
                    return;
                }
                if ((ACacheUtil.get(mDialog.getContext()).getAsString("pinPass") != null && ACacheUtil.get(mDialog.getContext()).getAsString("pinPass").equals("pinPass") && tagVerify == 1) ||
                        (TextUtils.isEmpty(ACacheUtil.get(mDialog.getContext()).getAsString("pinPass")) && tagVerify == 1)) {

                    LogUtil.e("pinPass==", "走了这里");
                    //在验证错误一次的时候就给一个标志,说明是从验证那里走过来就不走上面那个判断
//                    if (isDeviceLocked) {
//                        tv_msg.setText(R.string.verify_error);
//                    } else {
//                        tv_msg.setText(R.string.verify_error);
//                    }

                    tv_msg.setText(R.string.verify_error);

                    tv_msg.setTextColor(getResources().getColor(R.color.color_FB544B));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mDialog != null && mDialog.isShowing()) {
                                mDialog.dismiss();
                                mCallback.onError();
                            }
                        }
                    }, 1000);
                }
            }


            @Override
            public void onStartFailedByDeviceLocked() {
                tv_msg.setText(R.string.after_try);
                verify_touch_id.setText("");
                tv_msg.setTextColor(getResources().getColor(R.color.color_FB544B));
                btn_confirm.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                            mCallback.onError();
                        }
                    }
                }, 5000);
            }
        });
    }

    private void shake(View v) {
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        v.startAnimation(shake);
    }


    public interface Callback {

        void onSuccess();

        void onError();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mFingerprintIdentify.cancelIdentify();
        //mCallback = null;
    }
}
