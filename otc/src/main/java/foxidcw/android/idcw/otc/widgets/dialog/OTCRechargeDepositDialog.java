package foxidcw.android.idcw.otc.widgets.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjwsc.idcm.Utils.ScreenUtil;

import java.lang.ref.WeakReference;

import cn.com.epsoft.keyboard.basic.NewKeyBoard;
import cn.com.epsoft.keyboard.basic.NewPasswordView;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.otc.R;

/**
 * Created by hpz on 2018/5/9.
 */

public class OTCRechargeDepositDialog extends Dialog implements View.OnClickListener, NewPasswordView.PasswordListener {
    public static final int STATE_PIN_PAGE = 0; // 默认只显示输入密码
    public static final int STATE_ALL_PAGE = 1; // 按步骤输入密码 然后切换
    public static final int STATE_ORDER_PAGE = 3; //
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
    private TextView mPinInputResultBtn; // 确认按钮

    private String mCachePassword; // 缓存的输入密码
    private String mResultMessage; // 成功显示文字
    private String mResultBtnMessage; // 成功显示文字
    private int mType = STATE_PIN_PAGE; // 当前的状态
    private int mScreenWidth; // 动画用屏幕宽度
    private ProgressBar mProgressBar;

    private OTCRechargeDepositDialog.OnOTCPinInputCompleteListener mOnOTCPinInputCompleteListener; // 输入完pin回调参数
    private LinearLayout mr_back_to_layout;
    private TextView tv_now_address;
    private TextView btn_now_recharge;
    private RelativeLayout rl_recharge_layout;
    private TextView tv_set_Name;
    private TextView tv_amount_currency;
    private Context mContext;
    private TextView tv_check_query_record;

    public OTCRechargeDepositDialog(@NonNull Context context) {
        this(context, 0);
    }

    public OTCRechargeDepositDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BottomDialog);
        mWeakReferenceCtx = new WeakReference<>(context);
        mContext = context;
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(mWeakReferenceCtx.get()).inflate(R.layout.otc_deposit_pin_dialog, null);
        setCancelable(false); // 不能点击取消
        setCanceledOnTouchOutside(false);// 外部点击取消

        mr_back_to_layout = mRootView.findViewById(R.id.mr_back_to_layout);
        tv_now_address = mRootView.findViewById(R.id.tv_now_address);
        btn_now_recharge = mRootView.findViewById(R.id.btn_now_recharge);
        rl_recharge_layout = mRootView.findViewById(R.id.rl_recharge_layout);
        tv_set_Name = mRootView.findViewById(R.id.tv_set_Name);
        tv_amount_currency = mRootView.findViewById(R.id.tv_amount_currency);
        tv_check_query_record = mRootView.findViewById(R.id.tv_check_query_record);
        mr_back_to_layout.setOnClickListener(this);
        btn_now_recharge.setOnClickListener(this);

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
                rl_recharge_layout.setVisibility(View.VISIBLE);
                mPinInputPassRl.setVisibility(View.GONE);
                mPinInputResultCl.setVisibility(View.GONE);
                break;
            case STATE_RESULT_PAGE:
                mPinInputPassRl.setVisibility(View.VISIBLE);
                rl_recharge_layout.setVisibility(View.GONE);
                mPinInputResultCl.setVisibility(View.GONE);
                // 设置成功文字
                mPinInputResultTV.setText(mResultMessage);
                //充值成功
                mPinInputResultBtn.setText(mResultBtnMessage);
                break;
            default:
                break;
        }
    }

    public OTCRechargeDepositDialog setTitle(String title, String address, String amount, String btnText) {
        tv_set_Name.setText(title);
        tv_now_address.setText(address);
        btn_now_recharge.setText(btnText);
        tv_amount_currency.setText(amount);
        return this;
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
        //if (mType != STATE_ALL_PAGE) return;
        mPinInputResultTV.setText(mResultMessage);
        mPinInputResultBtn.setText(mResultBtnMessage);
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

    /**
     * 左边出右边进
     *
     * @param exitView
     * @param enterView
     */
    private void exitToLastEnter(final View exitView, final View enterView) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator exitTransX = ObjectAnimator.ofFloat(exitView, "translationX", 0f, -mScreenWidth);
        ObjectAnimator exitAlpha = ObjectAnimator.ofFloat(exitView, "alpha", 1f, 0.5f);

        ObjectAnimator enterTransX = ObjectAnimator.ofFloat(enterView, "translationX", mScreenWidth, 0f);//mScreenWidth, 0f
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

    /**
     * 左边出右边进
     *
     * @param exitView
     * @param enterView
     */
    private void exitToRightEnter(final View exitView, final View enterView) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator exitTransX = ObjectAnimator.ofFloat(exitView, "translationX", 0f, mScreenWidth);
        ObjectAnimator exitAlpha = ObjectAnimator.ofFloat(exitView, "alpha", 1f, 0.5f);

        ObjectAnimator enterTransX = ObjectAnimator.ofFloat(enterView, "translationX", -mScreenWidth, 0f);//mScreenWidth, 0f
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
            exitToRightEnter(mPinInputPassRl, rl_recharge_layout);
            //dismiss();
        } else if (id == R.id.otc_pin_input_result_confirm_btn) {
            dismiss();
            if (mOnOTCPinInputCompleteListener != null) {
                mOnOTCPinInputCompleteListener.onConfirmBtnClick();
            }
        } else if (id == R.id.mr_back_to_layout) {
            dismiss();
        } else if (id == R.id.btn_now_recharge) {
            //exitToEnter(mPinInputPassRl, mPinInputResultCl);
            //mPinInputPassRl.setVisibility(View.VISIBLE);
            exitToLastEnter(rl_recharge_layout, mPinInputPassRl);
        }
    }

    public void checkPinErrorHint(BaseWalletActivity mActivity) {//输入错误的震动提示
        Vibrator vibrate = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400}; // 停止 开启 停止 开启
        //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次
        vibrate.vibrate(pattern, -1);
        mPasswordViewNV.clearText();
        mProgressBar.setVisibility(View.GONE);
        Animation shake = AnimationUtils.loadAnimation(mActivity, R.anim.otc_shake);
        mPasswordViewNV.startAnimation(shake);
    }

    public void setTextViewVisible() {
        tv_check_query_record.setVisibility(View.VISIBLE);
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

    public OTCRechargeDepositDialog setType(int type) {
        this.mType = type;
        return this;
    }

    public OTCRechargeDepositDialog setResultText(String resultInfo, String resultBtnInfo) {
        this.mResultMessage = resultInfo;
        this.mResultBtnMessage = resultBtnInfo;
        return this;
    }

    public OTCRechargeDepositDialog setOnOTCPinInputCompleteListener(OTCRechargeDepositDialog.OnOTCPinInputCompleteListener listener) {
        this.mOnOTCPinInputCompleteListener = listener;
        return this;
    }

    public interface OnOTCPinInputCompleteListener {
        void onComplete(String inputPass);

        void onConfirmBtnClick();
    }

    public abstract class AbsOTCPinListener implements OTCRechargeDepositDialog.OnOTCPinInputCompleteListener {
        @Override
        public void onConfirmBtnClick() {
        }
    }
}
