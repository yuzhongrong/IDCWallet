package cn.com.epsoft.keyboard.basic;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import cn.com.epsoft.keyboard.R;
import foxidcw.android.idcw.foxcommon.provider.services.PinDialogProviderServices;

public class NewPinDialog extends Dialog implements View.OnClickListener, NewPasswordView.PasswordListener {

    private WeakReference<Context> mWeakReferenceCtx;
    private View mRootView;
    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "ok"
    };
    private NewKeyBoard pin_keyboard;
    private NewPasswordView passwordView;
    private PinDialogProviderServices.PinResultStr mNewPassWordListener;
    private String mCachePassword;

    public NewPinDialog(@NonNull Context context) {
        this(context,0);
    }

    public NewPinDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BottomDialog);
        mWeakReferenceCtx = new WeakReference<>(context);
        initView();
    }

    public void setNewPassWordListener(PinDialogProviderServices.PinResultStr mNewPassWordListener) {
        this.mNewPassWordListener = mNewPassWordListener;
    }

    /**
     * 初始化
     */
    private void initView() {
        mRootView = LayoutInflater.from(mWeakReferenceCtx.get()).inflate(R.layout.board_item_new_please_send_pin, null);
        setCancelable(false);
        setCanceledOnTouchOutside(false);// 外部点击取消
        //初始化控件
        pin_keyboard = mRootView.findViewById(R.id.pin_keyboard);
        passwordView = mRootView.findViewById(R.id.passwordView);
        mRootView.findViewById(R.id.new_mr_back_layout).setOnClickListener(this);
        //操作键盘
        pin_keyboard.setOnClickKeyboardListener((position, value1) -> {
            if (position < 11 && position != 9) {
                passwordView.addData(value1);
            } else if (position == 9) {
                //passwordView.delData(value);
            } else if (position == 11) {
                //点击完成 隐藏输入键盘
                passwordView.delData(value1);
            }
        });
        passwordView.setKeyBoard(pin_keyboard);
        passwordView.setPasswordListener(this);

        setContentView(mRootView);

        Window dialogWindow = getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = dialogWindow.getWindowManager().getDefaultDisplay().getWidth(); // 宽度持平
        lp.height = dialogWindow.getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void show() {
        //设置键盘
        pin_keyboard.setKeyboardKeys(KEY);
        pin_keyboard.Show();
        clearPassword();
        super.show();
    }


    public void showForProvider(PinDialogProviderServices.PinResultStr resultStr){
        setNewPassWordListener(resultStr);
        show();
    }

    /**
     * 清除密码
     */
    public void clearPassword() {
        if (null != passwordView) {
            passwordView.clearText();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_mr_back_layout) {
            PinDialogManager.getInstance(getContext()).dismissPinDialog();
            dismiss();
        }
    }

    @Override
    public void passwordChange(String changeText) {
    }

    @Override
    public void passwordComplete() {
        if (null != mNewPassWordListener) {
            mNewPassWordListener.onPinResultStr(mCachePassword);
        }
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {
    }

    @Override
    public void getPass(String pass, boolean isCom) {
        mCachePassword = pass;
    }


}
