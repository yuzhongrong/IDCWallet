package foxidcw.android.idcw.otc.activitys.pay;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjwsc.idcm.Utils.ScreenUtil;

import java.lang.ref.WeakReference;

import cn.com.epsoft.keyboard.basic.NewKeyBoard;
import cn.com.epsoft.keyboard.basic.NewPasswordView;
import foxidcw.android.idcw.otc.R;

public class OTCPayPinDialog extends Dialog implements View.OnClickListener, NewPasswordView.PasswordListener {

    public static final int STATE_PIN_PAGE = 0; // 默认只显示输入密码
    public static final int STATE_ALL_PAGE = 1; // 按步骤输入密码 然后切换
    public static final int STATE_RESULT_PAGE = 2; // 只显示成功

    private WeakReference<Context> mWeakReferenceCtx;
    private View mRootView;
    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "ok"
    };
    private ImageButton mCloseIb;
    private TextView mOTCPinTitleTv;
    private NewKeyBoard mKeyboardNB;
    private NewPasswordView mPasswordViewNV;
    private RelativeLayout mPinInputPassRl; // 输入密码界面

    private ConstraintLayout mPinInputResultCl; // 校验结果界面
    private TextView mPinInputResultTV; // 成功文字
    private Button mPinInputResultBtn; // 确认按钮

    private String mCachePassword; // 缓存的输入密码
    private String mResultMessage; // 成功显示文字
    private int mType = STATE_PIN_PAGE; // 当前的状态
    private int mScreenWidth; // 动画用屏幕宽度
    private ProgressBar mProgressBar;

    private OnOTCPinInputCompleteListener mOnOTCPinInputCompleteListener; // 输入完pin回调参数

    public OTCPayPinDialog(@NonNull Context context) {
        this(context, 0);
    }

    public OTCPayPinDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BottomDialog);
        mWeakReferenceCtx = new WeakReference<>(context);
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(mWeakReferenceCtx.get()).inflate(R.layout.otc_pin_dialog, null);
        setCancelable(false); // 不能点击取消
        setCanceledOnTouchOutside(false);// 外部点击取消

        // 屏幕宽度
        mScreenWidth = ScreenUtil.getScreenWidth(mWeakReferenceCtx.get());
        /**
         * 输入pin码部分
         */
        mPinInputPassRl = mRootView.findViewById(R.id.otc_pin_input_pass_container_rl);
        mCloseIb = mRootView.findViewById(R.id.otc_pin_input_pass_close_ib);
        mCloseIb.setOnClickListener(this);
        mOTCPinTitleTv = mRootView.findViewById(R.id.otc_pin_input_pass_title_tv);
        mPasswordViewNV = mRootView.findViewById(R.id.otc_pin_input_pass_password_np);
        mKeyboardNB = mRootView.findViewById(R.id.otc_pin_input_pass_key_nb);
        mKeyboardNB.setOnClickKeyboardListener((position, value1) -> {
            if (position < 11 && position != 9) {
                mPasswordViewNV.addData(value1);
            } else if (position == 9) {
                //passwordView.delData(value);
            } else if (position == 11) {
                //点击完成 隐藏输入键盘
                mPasswordViewNV.delData(value1);
            }
        });
        mPasswordViewNV.setKeyBoard(mKeyboardNB);
        mPasswordViewNV.setPasswordListener(this);
        /**
         * 结果区域
         */
        mPinInputResultCl = mRootView.findViewById(R.id.otc_pin_input_result_container_cl);
        mPinInputResultTV = mRootView.findViewById(R.id.otc_pin_input_result_text_tv);
        mPinInputResultBtn = mRootView.findViewById(R.id.otc_pin_input_result_confirm_btn);
        mPinInputResultBtn.setOnClickListener(this);

        mProgressBar = mRootView.findViewById(R.id.progress_bar);

        setContentView(mRootView);

        Window dialogWindow = getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(cn.com.epsoft.keyboard.R.style.AnimBottom);
        final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = dialogWindow.getWindowManager().getDefaultDisplay().getWidth(); // 宽度持平
        lp.height = dialogWindow.getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void show() {
        //设置键盘
        mKeyboardNB.setKeyboardKeys(KEY);
        mKeyboardNB.Show();
        clearPassword();
        checkStateUI();
        super.show();
    }

    /**
     * 根据设置的state显示UI
     */
    private void checkStateUI() {
        switch (mType) {
            case STATE_PIN_PAGE:
            case STATE_ALL_PAGE:
                mPinInputPassRl.setVisibility(View.VISIBLE);
                mPinInputResultCl.setVisibility(View.GONE);
                break;
            case STATE_RESULT_PAGE:
                mPinInputPassRl.setVisibility(View.GONE);
                mPinInputResultCl.setVisibility(View.VISIBLE);
                // 设置成功文字
                mPinInputResultTV.setText(mResultMessage);
                break;
            default:
                break;
        }
    }

    /**
     * 清除密码
     */
    private void clearPassword() {
        if (null != mPasswordViewNV) {
            mPasswordViewNV.clearText();
        }
    }

    /**
     * 显示成功界面
     */
    public void showResultPage() {
        if (mType != STATE_ALL_PAGE) return;
        mPinInputResultTV.setText(mResultMessage);
        exitToEnter(mPinInputPassRl, mPinInputResultCl);
    }

    /**
     * 左边出右边进
     *
     * @param exitView
     * @param enterView
     */
    private void exitToEnter(final View exitView, final View enterView) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator exitTransX = ObjectAnimator.ofFloat(exitView, "translationX", 0f, -mScreenWidth);
        ObjectAnimator exitAlpha = ObjectAnimator.ofFloat(exitView, "alpha", 1f, 0.5f);

        ObjectAnimator enterTransX = ObjectAnimator.ofFloat(enterView, "translationX", mScreenWidth, 0f);
        ObjectAnimator enterAlpha = ObjectAnimator.ofFloat(enterView, "alpha", 0.5f, 1f);

        set.setDuration(300);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                enterView.setVisibility(View.VISIBLE);
                exitView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.playTogether(exitTransX, exitAlpha, enterTransX, enterAlpha);
        set.start();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        // 如果是点击关闭或者点击确认
        if (id == R.id.otc_pin_input_pass_close_ib) {
            dismiss();
        } else if (id == R.id.otc_pin_input_result_confirm_btn) {
            dismiss();
            if (mOnOTCPinInputCompleteListener != null) {
                mOnOTCPinInputCompleteListener.onConfirmBtnClick();
            }
        }
    }

    @Override
    public void passwordChange(String changeText) {
    }

    @Override
    public void passwordComplete() {
        if (mOnOTCPinInputCompleteListener != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mOnOTCPinInputCompleteListener.onComplete(mCachePassword);
        }
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {
    }

    @Override
    public void getPass(String pass, boolean isCom) {
        mCachePassword = pass;
    }

    public OTCPayPinDialog setType(int type) {
        this.mType = type;
        return this;
    }

    public OTCPayPinDialog setResultText(String resultInfo) {
        this.mResultMessage = resultInfo;
        return this;
    }

    public OTCPayPinDialog setOnOTCPinInputCompleteListener(OnOTCPinInputCompleteListener listener) {
        this.mOnOTCPinInputCompleteListener = listener;
        return this;
    }

    public interface OnOTCPinInputCompleteListener {
        void onComplete(String inputPass);

        void onConfirmBtnClick();
    }

    public abstract class AbsOTCPinListener implements OnOTCPinInputCompleteListener {
        @Override
        public void onConfirmBtnClick() {
        }
    }
}
