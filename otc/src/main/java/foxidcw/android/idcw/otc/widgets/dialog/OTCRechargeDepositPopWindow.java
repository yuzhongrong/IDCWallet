package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import foxidcw.android.idcw.otc.R;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hpz on 2018/5/2.
 */

public class OTCRechargeDepositPopWindow extends BasePopupWindow {
    private TextView mIvClose;
    private TextView mTvContent;
    private TextView btn_go_set;

    public OTCRechargeDepositPopWindow(Context context) {
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
        View root = createPopupById(R.layout.pop_recharge_deposit);
        mIvClose = (TextView) root.findViewById(R.id.btn_back_close);
        mTvContent = (TextView) root.findViewById(R.id.tv_device_content);
        btn_go_set = (TextView) root.findViewById(R.id.btn_go_set);
        return root;
    }

    public OTCRechargeDepositPopWindow setTitle(String title) {
        mTvContent.setText(title);
        return this;
    }

    @Override
    public View initAnimaView() {
        return null;
    }
}
