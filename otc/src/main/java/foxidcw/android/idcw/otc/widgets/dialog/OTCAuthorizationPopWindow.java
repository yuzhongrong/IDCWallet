package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import foxidcw.android.idcw.otc.R;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hpz on 2018/5/22.
 */

public class OTCAuthorizationPopWindow extends BasePopupWindow implements View.OnClickListener{

    private TextView mIvClose;
    private TextView mTvContent;
    private TextView btn_go_set;
    private TextView tv_user;
    private ImageView iv_game;
    private TextView tv_game;

    public OTCAuthorizationPopWindow(Context context) {
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

    public TextView getSkipSureUser() {
        return tv_user;
    }

    public ImageView setImageRes() {
        return iv_game;
    }

    public TextView getTvGameName() {
        return tv_game;
    }

    @Override
    public View onCreatePopupView() {
        View root = createPopupById(R.layout.pop_authorization_sure_type);
        mIvClose = (TextView) root.findViewById(R.id.btn_back_close);
        mTvContent = (TextView) root.findViewById(R.id.tv_device_content);
        btn_go_set = (TextView) root.findViewById(R.id.btn_go_set);
        tv_user = (TextView) root.findViewById(R.id.tv_user);
        iv_game = (ImageView)root.findViewById(R.id.iv_game);
        tv_game = (TextView) root.findViewById(R.id.tv_game);
        mIvClose.setOnClickListener(this);
        btn_go_set.setOnClickListener(this);
        tv_user.setOnClickListener(this);
        return root;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public OTCAuthorizationPopWindow setTitle(String title) {
        mTvContent.setText(title);
        return this;
    }

    public OTCAuthorizationPopWindow setConfirmContent(String confirmContent) {
        btn_go_set.setText(confirmContent);
        return this;
    }

    public OTCAuthorizationPopWindow setCancelContent(String cancelContent) {
        mIvClose.setText(cancelContent);
        return this;
    }

    public OTCAuthorizationPopWindow setContentTextSize(float size) {
        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        return this;
    }

    private OTCAuthorizationPopWindow.BtnClickListener clickListener;

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

    public OTCAuthorizationPopWindow setClickListener(OTCAuthorizationPopWindow.BtnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }
}
