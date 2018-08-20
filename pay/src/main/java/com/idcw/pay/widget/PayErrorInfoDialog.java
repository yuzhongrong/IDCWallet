package com.idcw.pay.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.idcw.pay.R;

public class PayErrorInfoDialog extends Dialog implements View.OnClickListener {

    public static final int ERROR_INFO_STATE_LEFT = -1;
    public static final int ERROR_INFO_STATE_RIGHT = 1;

    // 缓存上下文
    private Context context;
    // 标题
    private TextView mTitleTv;
    // 左按钮
    private TextView mLeftBtn;
    // 右按钮
    private TextView mRightBtn;
    // 回调函数
    private OnPayErrorInfoListener mOnPayErrorInfoListener;
    // 错误类型
    private int type;


    public PayErrorInfoDialog(@NonNull Context context) {
        this(context, R.style.dialog);
    }

    public PayErrorInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
    }

    /**
     * 创建View
     */
    private void initView() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setOnDismissListener(dialog -> {
        });

        View rootView = LayoutInflater.from(context).inflate(R.layout.activity_third_party_pay_state, null);
        mTitleTv = rootView.findViewById(R.id.id_tv_third_pay_state_info); // 标题
        mLeftBtn = rootView.findViewById(R.id.id_btn_third_pay_cancel); // 左边按钮
        mLeftBtn.setOnClickListener(this);
        mRightBtn = rootView.findViewById(R.id.id_btn_third_pay_confirm); // 右边按钮
        mRightBtn.setOnClickListener(this);
        setContentView(rootView);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.id_btn_third_pay_cancel) {
            dismiss();
            if (mOnPayErrorInfoListener != null) {
                mOnPayErrorInfoListener.onButtonClick(ERROR_INFO_STATE_LEFT, type);
            }
        } else if (viewId == R.id.id_btn_third_pay_confirm) {
            dismiss();
            if (mOnPayErrorInfoListener != null) {
                mOnPayErrorInfoListener.onButtonClick(ERROR_INFO_STATE_RIGHT, type);
            }
        }
    }

    public PayErrorInfoDialog setType(int type) {
        this.type = type;
        return this;
    }

    public PayErrorInfoDialog setTitleText(String title) {
        mTitleTv.setText(title);
        return this;
    }

    public PayErrorInfoDialog setTitleWithRes(int title) {
        mTitleTv.setText(context.getResources().getString(title));
        return this;
    }

    public PayErrorInfoDialog setPositiveButtonText(String text) {
        mLeftBtn.setText(text);
        return this;
    }

    public PayErrorInfoDialog setPositiveButtonTextWithId(int textId) {
        mLeftBtn.setText(context.getResources().getString(textId));
        return this;
    }

    public PayErrorInfoDialog setNegativeButtonText(String text) {
        mRightBtn.setText(text);
        return this;
    }

    public PayErrorInfoDialog setNegativeButtonTextWithId(int textId) {
        if (textId == 0) {
            mRightBtn.setVisibility(View.GONE);
        } else {
            mRightBtn.setVisibility(View.VISIBLE);
            mRightBtn.setText(context.getResources().getString(textId));
        }
        return this;
    }

    public PayErrorInfoDialog setOnPayErrorInfoListener(OnPayErrorInfoListener mOnPayErrorInfoListener) {
        this.mOnPayErrorInfoListener = mOnPayErrorInfoListener;
        return this;
    }

    public interface OnPayErrorInfoListener {
        void onButtonClick(int state, int type);
    }

}
