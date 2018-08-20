package foxidcw.android.idcw.otc.widgets.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import foxidcw.android.idcw.otc.R;
import io.reactivex.functions.Action;

public class OTCPromptConfirmCancelPopup extends Dialog implements View.OnClickListener {

    private Button mBtnPrompt;
    private Button mBtnConfirm;
    private Button mBtnCancel;
    private Action mConfirmAction;
    private View mRootView;
    
    public OTCPromptConfirmCancelPopup(Activity activity, String prompt, String confirm, String cancel, Action confirmAction){
        super(activity,0);
        mRootView = activity.getLayoutInflater().inflate(R.layout.dialog_select_prompt, null);
        mBtnPrompt = (Button) mRootView.findViewById(R.id.btn_prompt);
        mBtnConfirm = (Button) mRootView.findViewById(R.id.btn_confirm);
        mBtnCancel = (Button) mRootView.findViewById(R.id.btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mBtnPrompt.setText(prompt);
        mBtnConfirm.setText(confirm);
        mBtnCancel.setText(cancel);
        mConfirmAction = confirmAction;

        setContentView(mRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = getWindow();
        // 设置显示动画
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setWindowAnimations(android.R.style.Animation_InputMethod);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);
    }

    public void onClick(View v) {
        if(mConfirmAction == null)return;
        int id = v.getId();
        if(id==R.id.btn_confirm){
            dismiss();
            try {
                mConfirmAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(id == R.id.btn_cancel){
            dismiss();
        }
    }
}
