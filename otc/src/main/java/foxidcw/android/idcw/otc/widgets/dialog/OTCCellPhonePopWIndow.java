package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;

import foxidcw.android.idcw.otc.R;
import razerdp.basepopup.BasePopupWindow;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by yuzhongrong on 2018/5/5.
 */

public class OTCCellPhonePopWIndow  extends BasePopupWindow implements View.OnClickListener {
    private View root;

    public OTCCellPhonePopWIndow(Context context) {
        super(context);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return root.findViewById(R.id.cancel);
    }

    @Override
    public View onCreatePopupView() {

         root = createPopupById(R.layout.dialog_otc_phone);
          root.findViewById(R.id.cell).setOnClickListener(this);

        return root;
    }

    @Override
    public View initAnimaView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        cellPhone();
    }

    private void cellPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "18826585609");
        intent.setData(data);
        startActivity(intent);
    }
}
