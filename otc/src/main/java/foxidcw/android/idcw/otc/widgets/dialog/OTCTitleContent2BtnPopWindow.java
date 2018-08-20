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

public class OTCTitleContent2BtnPopWindow
        extends BasePopupWindow implements View.OnClickListener {
    private TextView mIvClose;
    private TextView mTvContent;
    private TextView mTvTitle;
    private TextView btn_go_set;

    public OTCTitleContent2BtnPopWindow(Context context) {
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
        View root = createPopupById(R.layout.pop_title_content_2_btn);
        mIvClose = (TextView) root.findViewById(R.id.btn_back_close);
        mTvContent = (TextView) root.findViewById(R.id.tv_device_content);
        mTvTitle = (TextView) root.findViewById(R.id.tv_device_title);
        btn_go_set = (TextView) root.findViewById(R.id.btn_go_set);
        mIvClose.setOnClickListener(this);
        btn_go_set.setOnClickListener(this);
        return root;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public OTCTitleContent2BtnPopWindow setContent(String content) {
        mTvContent.setText(content);
        return this;
    }

    public OTCTitleContent2BtnPopWindow setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public OTCTitleContent2BtnPopWindow setConfirmContent(String confirmContent) {
        btn_go_set.setText(confirmContent);
        return this;
    }

    public OTCTitleContent2BtnPopWindow setCancelContent(String cancelContent) {
        mIvClose.setText(cancelContent);
        return this;
    }

    public OTCTitleContent2BtnPopWindow setContentTextSize(float size) {
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

    public OTCTitleContent2BtnPopWindow setClickListener(BtnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }
}
