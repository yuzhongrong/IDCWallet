package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import foxidcw.android.idcw.otc.R;
import razerdp.basepopup.BasePopupWindow;

//一个按钮
public class OTCOneBtnPopWindow
        extends BasePopupWindow implements View.OnClickListener {
    private TextView mTvContent;
    private TextView btn_go_set;

    public OTCOneBtnPopWindow(Context context) {
        super(context);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }


    public TextView getSkipSureDelete() {
        return btn_go_set;
    }

    @Override
    public View onCreatePopupView() {
        View root = createPopupById(R.layout.pop_otc_one_btn);
        mTvContent = (TextView) root.findViewById(R.id.tv_device_content);
        btn_go_set = (TextView) root.findViewById(R.id.btn_go_set);
        btn_go_set.setOnClickListener(this);
        return root;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public OTCOneBtnPopWindow setTitle(String title) {
        mTvContent.setText(title);
        return this;
    }

    public OTCOneBtnPopWindow setConfirmContent(String confirmContent) {
        btn_go_set.setText(confirmContent);
        return this;
    }

    public TextView getSkipSureDismiss() {
        return btn_go_set;
    }

    private BtnClickListener clickListener;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_go_set) {
            if (null != clickListener) {
                dismiss();
                clickListener.onBtnClick(1);
            }
        }
    }

    public interface BtnClickListener {
        void onBtnClick(int type);
    }

    public OTCOneBtnPopWindow setClickListener(BtnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }
}
