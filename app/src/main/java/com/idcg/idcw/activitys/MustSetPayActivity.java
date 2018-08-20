package com.idcg.idcw.activitys;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;

import com.idcg.idcw.iprovider.SetPayPassInfoProviderServices;
import com.idcg.idcw.widget.NewKeyBoard;
import com.idcg.idcw.widget.NewPasswordView;
import com.idcg.idcw.widget.NewPasswordViewAgain;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;

import butterknife.BindView;
import butterknife.OnClick;

import static foxidcw.android.idcw.common.constant.ArouterConstants.MustSetPay;

/**
 * Created by hpz on 2018/1/28.
 */
@Route(path = MustSetPay)
public class MustSetPayActivity extends BaseWalletActivity implements NewPasswordView.PasswordListener, NewPasswordViewAgain.PasswordListener1 {
    @BindView(R.id.passwordView)
    NewPasswordView passwordView;
    @BindView(R.id.btn_set_pass_sure)
    Button btnSetPassSure;
    @BindView(R.id.passwordView1)
    NewPasswordViewAgain passwordView1;
    @BindView(R.id.btn_set_pass_sure_two)
    Button btnSetPassSureTwo;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.ll_send)
    RelativeLayout llSend;
    @BindView(R.id.pin_keyboard)
    NewKeyBoard pinKeyboard;
    @BindView(R.id.pin_keyboard1)
    NewKeyBoard pinKeyboard1;

    private String value;
    private String payPin;
    private String payTwoPin;

    @Autowired
    SetPayPassInfoProviderServices setPayPassInfoProviderLogic;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            " ", "0", "ok"
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_must_set_pay;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        //autoScrollView(llSend, btnSetPassSure);
        ARouter.getInstance().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setSubView();
        setSubView1();
        pinKeyboard.Show();
        LoginStatus bean = LoginUtils.getLoginBean(this);
        if (bean != null) {
            value = bean.getTicket();
        }
        passwordView.setPasswordListener(this);
        passwordView.setKeyBoard(pinKeyboard);
        passwordView1.setKeyBoard(pinKeyboard1);
        passwordView1.setPasswordListener1(this);
        btnSetPassSure.setEnabled(false);
        btnSetPassSureTwo.setEnabled(false);

        pinKeyboard.setOnClickKeyboardListener((position, value1) ->  {
                if (position < 11 && position != 9) {
                    passwordView.addData(value1);
                } else if (position == 9) {

                } else if (position == 11) {
                    //点击完成 隐藏输入键盘
                    passwordView.delData(value1);
                    btnSetPassSure.setEnabled(false);
                    btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
                }
        });

        pinKeyboard1.setOnClickKeyboardListener((position, value1) ->  {
                if (position < 11 && position != 9) {
                    passwordView1.addData(value1);
                } else if (position == 9) {

                } else if (position == 11) {
                    //点击完成 隐藏输入键盘
                    passwordView1.delData(value1);
                    btnSetPassSureTwo.setEnabled(false);
                    btnSetPassSureTwo.setBackgroundResource(R.drawable.item_gray_black);
            }
        });
    }

    @Override
    protected void onEvent() {
        llSend.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            passwordView.onWindowFocusChanged(false);
            passwordView1.onWindowFocusChanged(false);
            return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        });
    }

    @Override
    protected BaseView getView() {
        return null;
    }



    private int scrollToPosition = 0;

    private void autoScrollView(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect rect = new Rect();

                        //获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);

                        //获取root在窗体的不可视区域高度(被遮挡的高度)
                        int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;

                        //若不可视区域高度大于150，则键盘显示
                        if (rootInvisibleHeight > 150) {

                            //获取scrollToView在窗体的坐标,location[0]为x坐标，location[1]为y坐标
                            int[] location = new int[2];
                            scrollToView.getLocationInWindow(location);

                            //计算root滚动高度，使scrollToView在可见区域的底部
                            int scrollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;

                            //注意，scrollHeight是一个相对移动距离，而scrollToPosition是一个绝对移动距离
                            scrollToPosition += scrollHeight;

                        } else {
                            //键盘隐藏
                            scrollToPosition = 0;
                        }
                        root.scrollTo(0, scrollToPosition);

                    }
                });
    }


    @Override
    public void passwordChange(String changeText) {
//        if (changeText.equals("1")) {
//            btnSetPassSure.setEnabled(false);
//            btnSetPassSure.setBackgroundResource(R.drawable.item_gray_black);
//        }
        LogUtil.e("passwordChange:===>", changeText);
    }

    @Override
    public void passwordComplete() {
        btnSetPassSure.setEnabled(true);
        btnSetPassSure.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
    }

    @Override
    public void keyEnterPress(String password, boolean isComplete) {

    }

    @Override
    public void getPass(String pass, boolean isCom) {
        payPin = pass;
    }

    private void RequestSetPass() {
//      addSubscribe(setPayPassInfoProviderLogic.requestSetPassProvider(new SetPayPassInfoReqParam("",
//              payPin,payTwoPin,"",""
//      )).subscribeWith(new RxSubscriber<String>() {
//          @Override
//          public void onSuccess(String s) {
//
//              //intentFActivity(SetPwSuccessActivity.class);
//              //ACacheUtil.get(MustSetPayActivity.this).put(ACacheKeys.finger, payTwoPin);
//              //ClientConfig.Instance().setSavePayPass(payTwoPin);
//              ClientConfig.Instance().setExecPopup(true);
//              EventBus.getDefault().post(new PosInfo(1111));
//              LoginStatus loginStatus = LoginUtils.getLoginBean(MustSetPayActivity.this);
//              loginStatus.setPayPasswordFlag(true);
//              ACacheUtil.get(BaseApplication.getContext()).put(AcacheKeys.loginbean, loginStatus);
//              ToastUtil.show(getString(R.string.pass_set_success));
//              finish();
//          }
//
//          @Override
//          protected void onError(ResponseThrowable ex) {
//              if(ex.getErrorCode().equals("108")){
//
//                  ToastUtil.show(getString(R.string.pay_pass_not_match));
//                  finish();
//                  navigation(MustSetPay);
//              }else{
//                  ToastUtil.show(getString(R.string.server_connection_error));
//              }
//
//          }
//      }));

    }


    @Override
    public void passwordChange1(String changeText) {
//        if (changeText.equals("1")) {
//            btnSetPassSureTwo.setEnabled(false);
//            btnSetPassSureTwo.setBackgroundResource(R.drawable.item_gray_black);
//        }
    }

    @Override
    public void passwordComplete1() {
        btnSetPassSureTwo.setEnabled(true);
        btnSetPassSureTwo.setBackgroundResource(R.drawable.sw_tipper_blue_bg);
        //if (pinKeyboard1 != null) pinKeyboard1.hide();
    }

    @Override
    public void keyEnterPress1(String password, boolean isComplete) {

    }

    @Override
    public void getPass1(String pass, boolean isCom) {
        payTwoPin = pass;
    }

    @OnClick({R.id.btn_set_pass_sure, R.id.btn_set_pass_sure_two})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set_pass_sure:
                passwordView.setVisibility(View.GONE);
                passwordView1.setVisibility(View.VISIBLE);
                btnSetPassSure.setVisibility(View.GONE);
                btnSetPassSureTwo.setVisibility(View.VISIBLE);
                tvStart.setText(R.string.tv_sure_pin_content);
                pinKeyboard1.Show();
                break;
            case R.id.btn_set_pass_sure_two:
                RequestSetPass();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void setSubView() {
        //设置键盘
        pinKeyboard.setKeyboardKeys(KEY);
    }

    private void setSubView1() {
        //设置键盘
        pinKeyboard1.setKeyboardKeys(KEY);
    }
}
