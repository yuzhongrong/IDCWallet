package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import foxidcw.android.idcw.otc.R;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hpz on 2018/5/18.
 */

public class OTCOpenPhotoPopWindow extends BasePopupWindow implements View.OnClickListener{
    private TextView mIvClose;
    private TextView mTvContent;
    private TextView btn_go_set;

    public OTCOpenPhotoPopWindow(Context context) {
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
        View root = createPopupById(R.layout.otc_sao_qr_dialog);
        mIvClose = (TextView) root.findViewById(R.id.tv_activity_cancel);
        mTvContent = (TextView) root.findViewById(R.id.tv_device_content);
        btn_go_set = (TextView) root.findViewById(R.id.tv_activity_update);
        mIvClose.setOnClickListener(this);
        btn_go_set.setOnClickListener(this);
        return root;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public OTCOpenPhotoPopWindow setTitle(String title) {
        mTvContent.setText(title);
        return this;
    }

    public OTCOpenPhotoPopWindow setConfirmContent(String confirmContent) {
        btn_go_set.setText(confirmContent);
        return this;
    }

    public OTCOpenPhotoPopWindow setCancelContent(String cancelContent) {
        mIvClose.setText(cancelContent);
        return this;
    }

    public OTCOpenPhotoPopWindow setContentTextSize(float size) {
        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        return this;
    }

    private OTCOpenPhotoPopWindow.BtnClickListener clickListener;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back_close) {
            if (null != clickListener) {
                dismiss();
                clickListener.onBtnClick(-1);
            }
        } else if (v.getId() == R.id.btn_go_set) {
            if (null != clickListener) {
                dismiss();
                clickListener.onBtnClick(1);
            }
        }
    }

    public interface BtnClickListener {
        void onBtnClick(int type);
    }

    public OTCOpenPhotoPopWindow setClickListener(OTCOpenPhotoPopWindow.BtnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }
}
