package com.idcg.idcw.activitys;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import com.idcg.idcw.configs.ClientConfig;

import foxidcw.android.idcw.common.constant.ArouterConstants;

import com.idcg.idcw.iprovider.CheckOriginalPwdProviderServices;

import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.idcg.idcw.model.bean.PositionBean;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.utils.UIUtils;
import com.idcg.idcw.widget.NewKeyBoard;
import com.idcg.idcw.widget.NewPasswordView;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.AESUtil;
import com.cjwsc.idcm.Utils.FingerPrintHelper;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.xuelianx.fingerlib.FingerFragment;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.epsoft.keyboard.widget.PayKeyboardView;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/17 19:25
 **/
@Route(path = ArouterConstants.SET_PAY_PASS, name = "设置支付密码")
public class SetPayPassActivity extends BaseWalletActivity implements PayKeyboardView.OnKeyboardListener, NewPasswordView.PasswordListener, View.OnClickListener {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.tv_set_pay_pass)
    TextView tvSetPayPass;
    @BindView(R.id.img_touch_open)
    ImageView imgTouchOpen;
    @BindView(R.id.img_touch_close)
    ImageView imgTouchClose;
    @BindView(R.id.img_face_open)
    ImageView imgFaceOpen;
    @BindView(R.id.img_face_close)
    ImageView imgFaceClose;
    @BindView(R.id.tv_not_set)
    TextView tvNotSet;
    @BindView(R.id.tv_forget_pay_pass)
    TextView tvForgetPayPass;
    @BindView(R.id.view)
    View view;
    private AppCompatDialog mDialog;
    PayKeyboardView panel = null;

    private boolean isPay;
    private String payPass;
    private String value;
    private LoginStatus loginStatus;
    private Dialog dialogNewPin;
    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "ok"
    };
    private NewKeyBoard pin_keyboard;
    private static final int INTERVAL = 90;

    @Autowired
    CheckOriginalPwdProviderServices mCheckOriginalPwdServices;

    @Autowired(name = "mIsBindPay")
    boolean isBind = false;
    private NewPasswordView passwordView;
    private ImageView icon;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pay_pass;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        try {
            tvSetName.setText(getString(R.string.str_pin_managle));
//            loginStatus = LoginUtils.getLoginBean(this);
//            if (loginStatus != null) {
//                value = loginStatus.getTicket();
//                isPay = loginStatus.isPayPasswordFlag();
//            }
//
//            if (isPay || ClientConfig.Instance().getExecPopup()) {
//                tvNotSet.setText(getString(R.string.pin_set));
//                tvSetPayPass.setText(getString(R.string.tv_modify));
//                tvForgetPayPass.setVisibility(View.VISIBLE);
//            } else {
//                tvNotSet.setText(getString(R.string.not_setting));
//                tvSetPayPass.setText(getString(R.string.not_setting));
//                tvForgetPayPass.setVisibility(View.INVISIBLE);
//            }
            int titleId;
            isBind = true;
            if (isBind) {
                titleId = R.string.tv_modify;
            } else {
                titleId = R.string.set;
            }
            tvSetPayPass.setText(titleId);
            loginStatus = LoginUtils.getLoginBean(this);
            String isopen = ACacheUtil.get(SetPayPassActivity.this).getAsString(loginStatus.getId() + "");
            if (isopen == null || isopen.equals("0")) {
                showOnOffVisibility(imgTouchOpen, imgTouchClose);
            } else if (isopen.equals("1")) {
                showOnOffVisibility(imgTouchClose, imgTouchOpen);
            }

            if (ClientConfig.Instance().getFacePlay()) {
                showOnOffVisibility(imgFaceClose, imgFaceOpen);
            } else {
                showOnOffVisibility(imgFaceOpen, imgFaceClose);
            }

        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private void showOnOffVisibility(ImageView img1, ImageView img2) {
        img1.setVisibility(View.INVISIBLE);
        img2.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_forget_pay_pass, R.id.mr_back_layout, R.id.tv_set_pay_pass, R.id.img_touch_open, R.id.img_touch_close, R.id.img_face_open, R.id.img_face_close, R.id.tv_not_set})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.tv_set_pay_pass:
                if (getString(R.string.set).equals(tvSetPayPass.getText().toString())) {
                    navigation(ArouterConstants.SET_PAY_PW);
                } else if (getString(R.string.tv_modify).equals(tvSetPayPass.getText().toString())) {
                    navigation(ArouterConstants.EDIT_PAY_PASS);
                }
                break;
            case R.id.img_touch_open:
                showOnOffVisibility(imgTouchOpen, imgTouchClose);
                // ClientConfig.Instance().setTouchPlay(false);
                ACacheUtil.get(SetPayPassActivity.this).put(loginStatus.getId() + "", "0");
                break;
            case R.id.img_touch_close:
                if (FingerPrintHelper.isHardWareDetected(this) && FingerPrintHelper.hasEnrolledFingerPrint(this)) {
                    if (isBind) {
                        if (panel != null) {
                            panel.clear();
                        }
                        initNewPinDialog();
                    } else {
                        ToastUtil.show(getString(R.string.please_set_pay_pass));
                    }
                } else {
                    ToastUtil.show(getString(R.string.set_touch_id));
                }

                break;
            case R.id.img_face_open:
                showOnOffVisibility(imgFaceOpen, imgFaceClose);
                ClientConfig.Instance().setFacePlay(false);
                break;
            case R.id.img_face_close:
                showOnOffVisibility(imgFaceClose, imgFaceOpen);
                ClientConfig.Instance().setFacePlay(true);
                break;
            case R.id.tv_not_set:
                break;
            case R.id.tv_forget_pay_pass:
                navigation(ArouterConstants.FORGET_PASS);
                break;
        }
    }

    AnimationDrawable mAnimation;

    private void initNewPinDialog() {

        dialogNewPin = new Dialog(this, R.style.BottomDialog);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_new_please_send_pin, null);
        dialogNewPin.setCancelable(false);
        //初始化控件
        pin_keyboard = (NewKeyBoard) inflate.findViewById(R.id.pin_keyboard);
        LinearLayout new_mr_back_layout = (LinearLayout) inflate.findViewById(R.id.new_mr_back_layout);
        passwordView = (NewPasswordView) inflate.findViewById(R.id.passwordView);
        icon = (ImageView) inflate.findViewById(R.id.icon);
        startAnimation();
        new_mr_back_layout.setOnClickListener(this);
        passwordView.setPasswordListener(this);
        passwordView.setKeyBoard(pin_keyboard);
        setSubView();
        pin_keyboard.Show();

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
        //将布局设置给Dialog
        dialogNewPin.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialogNewPin.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        //将属性设置给窗体
        dialogNewPin.setCanceledOnTouchOutside(false); // 外部点击取消
        //设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialogNewPin.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = this.getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);
        dialogNewPin.show();
    }

    private void startAnimation() {
        mAnimation = new AnimationDrawable();
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_01), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_02), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_03), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_04), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_05), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_06), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_07), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_08), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_09), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_10), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_11), INTERVAL);
        mAnimation.addFrame(getResources().getDrawable(com.cjwsc.idcm.R.drawable.loading_12), INTERVAL);
        mAnimation.setOneShot(false);
        icon.setBackground(mAnimation);
        if (mAnimation != null && !mAnimation.isRunning()) {
            mAnimation.start();
        }
    }

    @Override
    protected void onEvent() {

    }

    private void showFragment() {
        FingerFragment fingerFragment = new FingerFragment();
        fingerFragment.show(getFragmentManager(), "fingerFragment");
        fingerFragment.setCancelable(false);
        fingerFragment.setmFragmentCallBack(new FingerFragment.Callback() {
            @Override
            public void onSuccess() {
                LoginStatus bean = LoginUtils.getLoginBean(SetPayPassActivity.this);
                showOnOffVisibility(imgTouchClose, imgTouchOpen);
                ToastUtil.show(getString(R.string.figer_set_success));
                //   ClientConfig.Instance().setTouchPlay(true);
                ACacheUtil.get(SetPayPassActivity.this).put(bean.getId() + "", "1");
                try {
                    String mdAESstring = new AESUtil().encrypt(payPass);
                    LogUtil.d("--测试加密touchid-->", mdAESstring);
                    ACacheUtil.get(SetPayPassActivity.this).put(bean.getUser_name(), mdAESstring);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
            }
        });
    }

    @Subscriber
    public void onPosInfo(PositionBean positionState) {
        try {
            if (positionState == null) return;
            if (positionState.getPos() == 8) {
                tvNotSet.setText(R.string.already_set);
                tvSetPayPass.setText(R.string.tv_modify);
                tvForgetPayPass.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Subscriber
    public void onStcikyInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 1111) {
                tvNotSet.setText(getString(R.string.already_set));
                tvSetPayPass.setText(getString(R.string.tv_modify));
                tvForgetPayPass.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onComplete(String one, String two, String three, String four, String five, String six) {
        payPass = one + two + three + four + five + six;
        try {
            String mdAESstring = new AESUtil().encrypt(payPass);
            ClientConfig.Instance().setSavePayPass(mdAESstring);
            String pass = new AESUtil().decrypt(mdAESstring);
            LoginStatus bean1 = LoginUtils.getLoginBean(mCtx);
            CheckOriginalPwdReqParam reqParam = new CheckOriginalPwdReqParam("1", pass, bean1.getDevice_id(), true);
            mCheckOriginalPwdServices.requestOldCheckOriginalPwdProvider(reqParam)
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeWith(new RxSubscriber<Boolean>() {
                        @Override
                        public void onSuccess(Boolean checkNewPinBean) {
                            mDialog.cancel();
                            showFragment();
                        }

                        @Override
                        protected void onError(ResponseThrowable ex) {
                            if (ex.getErrorCode().equals("121")) {
                                mDialog.cancel();
                                ToastUtil.show(getString(R.string.pay_pass_word_error));
                            } else {
                                ToastUtil.show(getString(R.string.server_connection_error));
                            }
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBack() {
        mDialog.dismiss();
    }

    @Override
    public void onSetPay() {
        navigation(ArouterConstants.FORGET_PASS);
        if (mDialog.isShowing() && mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void passwordChange(String changeText) {

    }

    @Override
    public void passwordComplete() {
        UIUtils.postDelayed(() -> {
            try {
                icon.setVisibility(View.VISIBLE);
                String mdAESstring = new AESUtil().encrypt(payPass);
                ClientConfig.Instance().setSavePayPass(mdAESstring);
                String pass = new AESUtil().decrypt(mdAESstring);
                LoginStatus bean1 = LoginUtils.getLoginBean(mCtx);
                CheckOriginalPwdReqParam reqParam = new CheckOriginalPwdReqParam("1", pass, bean1.getDevice_id(), true);
                mCheckOriginalPwdServices.requestOldCheckOriginalPwdProvider(reqParam)
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeWith(new RxSubscriber<Boolean>() {
                            @Override
                            public void onSuccess(Boolean checkNewPinBean) {
                                if (icon != null) {
                                    icon.setVisibility(View.INVISIBLE);
                                }
                                dialogNewPin.dismiss();
                                showFragment();
                            }

                            @Override
                            protected void onError(ResponseThrowable ex) {
                                if (ex.getErrorCode().equals("121")) {
                                    icon.setVisibility(View.INVISIBLE);
                                    checkPinErrorHint();
                                    ToastUtil.show(getString(R.string.pay_pass_word_error));
                                } else {
                                    if (icon != null) {
                                        icon.setVisibility(View.INVISIBLE);
                                    }
                                    ToastUtil.show(getString(R.string.server_connection_error));
                                }
                            }
                        });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 500);
    }

    private void checkPinErrorHint() {//输入错误的震动提示
        Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400}; // 停止 开启 停止 开启
        //第二个参数表示使用pattern第几个参数作为震动时间重复震动，如果是-1就震动一次
        vibrate.vibrate(pattern, -1);
        passwordView.clearText();
        Animation shake = AnimationUtils.loadAnimation(SetPayPassActivity.this, R.anim.shake);
        passwordView.startAnimation(shake);
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {

    }

    @Override
    public void getPass(String pass, boolean isCom) {
        payPass = pass;
    }

    private void setSubView() {
        //设置键盘
        pin_keyboard.setKeyboardKeys(KEY);
    }

    @Override
    public void onClick(View v) {
        dialogNewPin.dismiss();
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }

    @Subscriber
    public void refrestrfhpinBuyList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 333) {
            showFragment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnimation != null) {
            mAnimation.stop();
        }
    }
}
