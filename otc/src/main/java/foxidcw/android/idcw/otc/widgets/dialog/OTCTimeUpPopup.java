package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import foxidcw.android.idcw.otc.R;
import razerdp.basepopup.BasePopupWindow;

// 时间到界面
public class OTCTimeUpPopup extends BasePopupWindow {

    private TextView mTimesUpInfo;
    private Button mTimesUpKnowBtn;

    public OTCTimeUpPopup(Context context) {
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

    @Override
    public View onCreatePopupView() {
        View mRootView = createPopupById(R.layout.popup_otc_time_up);
        setDismissWhenTouchOutside(false); // 点击外部不消失
        setBackPressEnable(false); // 不能点击返回
        mTimesUpInfo = mRootView.findViewById(R.id.otc_time_up_info_tv);
        mTimesUpKnowBtn = mRootView.findViewById(R.id.otc_time_up_btn);
        mTimesUpKnowBtn.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onClick();
            }
        });
        return mRootView;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public interface OnKnowClickListener {
        void onClick();
    }

    private OnKnowClickListener listener;

    public OTCTimeUpPopup setListener(OnKnowClickListener listener) {
        this.listener = listener;
        return this;
    }

    public OTCTimeUpPopup setTimesUpInfo(@NonNull String message) {
        mTimesUpInfo.setText(message);
        return this;
    }

    public OTCTimeUpPopup setTimesUpInfo(int message) {
        mTimesUpInfo.setText(message);
        return this;
    }

    @Override
    public View initAnimaView() {


        return null;
    }
}
