package com.idcw.pay.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.idcw.pay.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hpz on 2018/5/18.
 */

public class PayOpenPhotoPopWindow extends BasePopupWindow implements View.OnClickListener{
    private TextView mIvClose;
    private TextView mTvContent;
    private TextView btn_go_set;

    public PayOpenPhotoPopWindow(Context context) {
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

    public TextView getSkipSureDelete() {
        return btn_go_set;
    }

    @Override
    public View onCreatePopupView() {
        View root = createPopupById(R.layout.pay_sao_qr_dialog);
        mIvClose = root.findViewById(R.id.tv_activity_cancel);
        mTvContent = root.findViewById(R.id.tv_activity_content);
        btn_go_set = root.findViewById(R.id.tv_activity_update);
        mIvClose.setOnClickListener(this);
        btn_go_set.setOnClickListener(this);
        return root;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public PayOpenPhotoPopWindow setTitle(String title) {
        mTvContent.setText(title);
        return this;
    }

    public PayOpenPhotoPopWindow setConfirmContent(String confirmContent) {
        btn_go_set.setText(confirmContent);
        return this;
    }

    public PayOpenPhotoPopWindow setCancelContent(String cancelContent) {
        mIvClose.setText(cancelContent);
        return this;
    }

    public PayOpenPhotoPopWindow setContentTextSize(float size) {
        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        return this;
    }

    private PayOpenPhotoPopWindow.BtnClickListener clickListener;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_activity_cancel) {
            if (null != clickListener) {
                dismiss();
                clickListener.onBtnClick(-1);
            }
        } else if (v.getId() == R.id.tv_activity_update) {
            if (null != clickListener) {
                dismiss();
                clickListener.onBtnClick(1);
            }
        }
    }

    public interface BtnClickListener {
        void onBtnClick(int type);
    }

    public PayOpenPhotoPopWindow setClickListener(PayOpenPhotoPopWindow.BtnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }
}
