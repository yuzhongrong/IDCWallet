package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import foxidcw.android.idcw.otc.R;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hpz on 2018/4/28.
 */

public class OTCTwoBtnTitlePopWindow
        extends BasePopupWindow implements View.OnClickListener {
    private TextView mIvClose;
    private TextView mTvContent;
    private TextView btn_go_set;

    public OTCTwoBtnTitlePopWindow(Context context) {
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
        View root = createPopupById(R.layout.pop_two_btn_title);
        mIvClose = (TextView) root.findViewById(R.id.btn_back_close);
        mTvContent = (TextView) root.findViewById(R.id.tv_device_content);
        btn_go_set = (TextView) root.findViewById(R.id.btn_go_set);
        mIvClose.setOnClickListener(this);
        btn_go_set.setOnClickListener(this);
        return root;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public OTCTwoBtnTitlePopWindow setTitle(String title) {
        mTvContent.setText(title);
        return this;
    }

    public OTCTwoBtnTitlePopWindow setConfirmContent(String confirmContent) {
        btn_go_set.setText(confirmContent);
        return this;
    }

    public OTCTwoBtnTitlePopWindow setCancelContent(String cancelContent) {
        mIvClose.setText(cancelContent);
        return this;
    }

    public OTCTwoBtnTitlePopWindow setContentTextSize(float size) {
        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        return this;
    }

    private BtnClickListener clickListener;

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

    public OTCTwoBtnTitlePopWindow setClickListener(BtnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
