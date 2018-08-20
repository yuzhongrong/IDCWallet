package com.idcg.idcw.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.idcg.idcw.model.bean.PhraseBean;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.widget.KeyboardLayout;
import com.idcg.idcw.widget.SoftKeyInputHidWidget;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.base.BaseView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hpz on 2017/12/25.
 */
@Route(path = ArouterConstants.FINDMONEY,name = "找回资产页面")
public class FindMoneyActivity extends BaseWalletActivity implements TextWatcher {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.ed_find_one)
    EditText edFindOne;
    @BindView(R.id.ed_find_two)
    EditText edFindTwo;
    @BindView(R.id.ed_find_three)
    EditText edFindThree;
    @BindView(R.id.ed_find_four)
    EditText edFindFour;
    @BindView(R.id.btn_find_next_step)
    Button btnFindNextStep;
    @BindView(R.id.view_one)
    View viewOne;
    @BindView(R.id.view_two)
    View viewTwo;
    @BindView(R.id.view_three)
    View viewThree;
    @BindView(R.id.view_four)
    View viewFour;
    @BindView(R.id.ll_find_one)
    LinearLayout llFindOne;
    @BindView(R.id.ll_find_two)
    LinearLayout llFindTwo;
    @BindView(R.id.ll_find_three)
    LinearLayout llFindThree;
    @BindView(R.id.ll_find_four)
    LinearLayout llFindFour;
    @BindView(R.id.ed_find_five)
    EditText edFindFive;
    @BindView(R.id.ll_find_five)
    LinearLayout llFindFive;
    @BindView(R.id.ed_find_six)
    EditText edFindSix;
    @BindView(R.id.ll_find_six)
    LinearLayout llFindSix;
    @BindView(R.id.view1)
    View view1;

    List<PhraseBean> phraseList = new ArrayList<>();
    @BindView(R.id.view_onen)
    View viewOnen;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.ll_find_last)
    LinearLayout llFindLast;
    @BindView(R.id.ll_scro)
    ScrollView llScro;
    @BindView(R.id.main)
    KeyboardLayout main;
    private int tag;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_money;
    }

    @Override
    protected void onInitView(Bundle bundle) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //addLayoutListener();

        //AndroidBug5497Workaround.assistActivity(FindMoneyActivity.this);
        tvSetName.setText(R.string.tv_find_money);
        edFindOne.addTextChangedListener(this);
        edFindTwo.addTextChangedListener(this);
        edFindThree.addTextChangedListener(this);
        edFindFour.addTextChangedListener(this);
        edFindFive.addTextChangedListener(this);
        edFindSix.addTextChangedListener(this);

        edFindOne.setFilters(new InputFilter[]{filter});
        edFindTwo.setFilters(new InputFilter[]{filter});
        edFindThree.setFilters(new InputFilter[]{filter});
        edFindFour.setFilters(new InputFilter[]{filter});
        edFindFive.setFilters(new InputFilter[]{filter});
        edFindSix.setFilters(new InputFilter[]{filter});

    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return null;
    }


    /**
     * 监听键盘状态，布局有变化时，靠scrollView去滚动界面
     */
    public void addLayoutListener() {
        main.setKeyboardListener(new KeyboardLayout.KeyboardLayoutListener() {
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                LogUtil.e("onKeyboardStateChanged", "isActive:" + isActive + " keyboardHeight:" + keyboardHeight);
                if (isActive) {
                    scrollToBottom();
                }
            }
        });
    }

    /**
     * 弹出软键盘时将SVContainer滑到底
     */
    private void scrollToBottom() {

        llScro.postDelayed(new Runnable() {

            @Override
            public void run() {
                llScro.smoothScrollTo(0, llScro.getBottom() + SoftKeyInputHidWidget.getStatusBarHeight(FindMoneyActivity.this));
            }
        }, 100);

    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };

    @OnClick({R.id.img_back, R.id.ed_find_one, R.id.ed_find_two, R.id.ed_find_three, R.id.ed_find_four, R.id.btn_find_next_step, R.id.ll_find_one, R.id.ll_find_two, R.id.ll_find_three, R.id.ll_find_four})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
//                if (tag==1){
//                    llFindLast.setVisibility(View.VISIBLE);
//                    llFindNext.setVisibility(View.GONE);
//                    view1.setVisibility(View.GONE);
//                    viewOnen.setVisibility(View.VISIBLE);
//                }else {
                this.finish();
                //}

                break;
            case R.id.ed_find_one:
                break;
            case R.id.ed_find_two:
                break;
            case R.id.ed_find_three:
                break;
            case R.id.ed_find_four:
                break;
            case R.id.btn_find_next_step:
                Intent intent = new Intent(FindMoneyActivity.this, FindSureMoneyActivity.class);
                //用数据捆传递数据
                Bundle bundle = new Bundle();
                bundle.putString("phraseOne", edFindOne.getText().toString().trim());
                bundle.putString("phraseTwo", edFindTwo.getText().toString().trim());
                bundle.putString("phraseThree", edFindThree.getText().toString().trim());
                bundle.putString("phraseFour", edFindFour.getText().toString().trim());
                bundle.putString("phraseFive", edFindFive.getText().toString().trim());
                bundle.putString("phraseSix", edFindSix.getText().toString().trim());
                intent.putExtra("phrase", bundle);
                startActivity(intent);

                break;
            case R.id.ll_find_one:
                break;
            case R.id.ll_find_two:
                break;
            case R.id.ll_find_three:
                break;
            case R.id.ll_find_four:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnFindNextStep.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnFindNextStep.setBackgroundResource(R.drawable.item_gray_black);
        btnFindNextStep.setTextColor(getResources().getColor(R.color.gray_90));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edFindOne.getText().toString().equals("") || edFindTwo.getText().toString().equals("") || edFindThree.getText().toString().equals("") || edFindFour.getText().toString().equals("")
                || edFindFive.getText().toString().equals("") || edFindSix.getText().toString().equals(""))) {
            btnFindNextStep.setEnabled(true);
            btnFindNextStep.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
            btnFindNextStep.setTextColor(getResources().getColor(R.color.white));
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return super.dispatchTouchEvent(ev);
            }
            // 必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }

        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
        return onTouchEvent(ev);
    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        try {
            if (v != null && (v instanceof EditText)) {
                int[] leftTop = {0, 0};
                //获取输入框当前的location位置
                v.getLocationInWindow(leftTop);
                int left = leftTop[0];
                int top = leftTop[1];
                int bottom = top + v.getHeight();
                int right = left + v.getWidth();
                if (event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom) {
                    // 点击的是输入框区域，保留点击EditText的事件
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }

        return false;
    }

    @Subscriber
    public void oneRefreshInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 654) {
                FindMoneyActivity.this.finish();

            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
}
