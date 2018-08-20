package com.idcg.idcw.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;

import com.idcg.idcw.model.logic.FindSureMoneyLogic;
import com.idcg.idcw.model.params.CheckUserReqParam;
import com.idcg.idcw.presenter.impl.FindSureMoneyImpl;
import com.idcg.idcw.presenter.interfaces.FindSureMoneyContract;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpz on 2018/2/3.
 */
public class FindSureMoneyActivity extends BaseWalletActivity<FindSureMoneyLogic,FindSureMoneyImpl> implements TextWatcher,FindSureMoneyContract.View {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.view_onen)
    View viewOnen;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.ed_find_seven)
    EditText edFindSeven;
    @BindView(R.id.view_seven)
    View viewSeven;
    @BindView(R.id.ll_find_seven)
    LinearLayout llFindSeven;
    @BindView(R.id.ed_find_eight)
    EditText edFindEight;
    @BindView(R.id.view_eight)
    View viewEight;
    @BindView(R.id.ll_find_eight)
    LinearLayout llFindEight;
    @BindView(R.id.ed_find_nine)
    EditText edFindNine;
    @BindView(R.id.view_nine)
    View viewNine;
    @BindView(R.id.ll_find_nine)
    LinearLayout llFindNine;
    @BindView(R.id.ed_find_ten)
    EditText edFindTen;
    @BindView(R.id.view_ten)
    View viewTen;
    @BindView(R.id.ll_find_ten)
    LinearLayout llFindTen;
    @BindView(R.id.ed_find_eleven)
    EditText edFindEleven;
    @BindView(R.id.view_eleven)
    View viewEleven;
    @BindView(R.id.ll_find_eleven)
    LinearLayout llFindEleven;
    @BindView(R.id.ed_find_twelve)
    EditText edFindTwelve;
    @BindView(R.id.view_twelve)
    View viewTwelve;
    @BindView(R.id.ll_find_twelve)
    LinearLayout llFindTwelve;
    @BindView(R.id.btn_find_next_verify)
    Button btnFindNextVerify;
    @BindView(R.id.ll_find_next)
    LinearLayout llFindNext;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    private String phraseOne;
    private String phraseTwo;
    private String phraseThree;
    private String phraseFour;
    private String phraseFive;
    private String phraseSix;
    List<CheckUserReqParam> phraseList = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sure_money;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tvSetName.setText(R.string.tv_find_money);
        Intent intent = getIntent();
        Bundle bundle1 = intent.getBundleExtra("phrase");
        phraseOne = bundle1.getString("phraseOne");
        phraseTwo = bundle1.getString("phraseTwo");
        phraseThree = bundle1.getString("phraseThree");
        phraseFour = bundle1.getString("phraseFour");
        phraseFive = bundle1.getString("phraseFive");
        phraseSix = bundle1.getString("phraseSix");

        edFindSeven.addTextChangedListener(this);
        edFindEight.addTextChangedListener(this);
        edFindNine.addTextChangedListener(this);
        edFindTen.addTextChangedListener(this);
        edFindEleven.addTextChangedListener(this);
        edFindTwelve.addTextChangedListener(this);
        btnFindNextVerify.setEnabled(false);//初始化按钮不能点击

        edFindSeven.setFilters(new InputFilter[]{filter});
        edFindEight.setFilters(new InputFilter[]{filter});
        edFindNine.setFilters(new InputFilter[]{filter});
        edFindTen.setFilters(new InputFilter[]{filter});
        edFindEleven.setFilters(new InputFilter[]{filter});
        edFindTwelve.setFilters(new InputFilter[]{filter});
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };

    @OnClick({R.id.img_back, R.id.btn_find_next_verify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.finish();
                break;
            case R.id.btn_find_next_verify:
                CheckUserReqParam phraseInfo1 = new CheckUserReqParam();
                phraseInfo1.setSerial_number(1);
                phraseInfo1.setPhrase(phraseOne);
                CheckUserReqParam phraseInfo2 = new CheckUserReqParam();
                phraseInfo2.setSerial_number(2);
                phraseInfo2.setPhrase(phraseTwo);
                CheckUserReqParam phraseInfo3 = new CheckUserReqParam();
                phraseInfo3.setSerial_number(3);
                phraseInfo3.setPhrase(phraseThree);
                CheckUserReqParam phraseInfo4 = new CheckUserReqParam();
                phraseInfo4.setSerial_number(4);
                phraseInfo4.setPhrase(phraseFour);
                CheckUserReqParam phraseInfo5 = new CheckUserReqParam();
                phraseInfo5.setSerial_number(5);
                phraseInfo5.setPhrase(phraseFive);
                CheckUserReqParam phraseInfo6 = new CheckUserReqParam();
                phraseInfo6.setSerial_number(6);
                phraseInfo6.setPhrase(phraseSix);
                CheckUserReqParam phraseInfo7 = new CheckUserReqParam();
                phraseInfo7.setSerial_number(7);
                phraseInfo7.setPhrase(edFindSeven.getText().toString().trim());
                CheckUserReqParam phraseInfo8 = new CheckUserReqParam();
                phraseInfo8.setSerial_number(8);
                phraseInfo8.setPhrase(edFindEight.getText().toString().trim());
                CheckUserReqParam phraseInfo9 = new CheckUserReqParam();
                phraseInfo9.setSerial_number(9);
                phraseInfo9.setPhrase(edFindNine.getText().toString().trim());
                CheckUserReqParam phraseInfo10 = new CheckUserReqParam();
                phraseInfo10.setSerial_number(10);
                phraseInfo10.setPhrase(edFindTen.getText().toString().trim());
                CheckUserReqParam phraseInfo11 = new CheckUserReqParam();
                phraseInfo11.setSerial_number(11);
                phraseInfo11.setPhrase(edFindEleven.getText().toString().trim());
                CheckUserReqParam phraseInfo12 = new CheckUserReqParam();
                phraseInfo12.setSerial_number(12);
                phraseInfo12.setPhrase(edFindTwelve.getText().toString().trim());
                phraseList.add(phraseInfo1);
                phraseList.add(phraseInfo2);
                phraseList.add(phraseInfo3);
                phraseList.add(phraseInfo4);
                phraseList.add(phraseInfo5);
                phraseList.add(phraseInfo6);
                phraseList.add(phraseInfo7);
                phraseList.add(phraseInfo8);
                phraseList.add(phraseInfo9);
                phraseList.add(phraseInfo10);
                phraseList.add(phraseInfo11);
                phraseList.add(phraseInfo12);
                RequestCheckUser();
                break;
        }
    }

    private void RequestCheckUser() {
        showDialog();
    mPresenter.ReqCheckUser(phraseList);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnFindNextVerify.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnFindNextVerify.setBackgroundResource(R.drawable.item_gray_black);
        btnFindNextVerify.setTextColor(getResources().getColor(R.color.gray_90));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edFindSeven.getText().toString().equals("") || edFindEight.getText().toString().equals("") || edFindNine.getText().toString().equals("") || edFindTen.getText().toString().equals("")
                || edFindEleven.getText().toString().equals("") || edFindTwelve.getText().toString().equals(""))) {
            btnFindNextVerify.setEnabled(true);
            btnFindNextVerify.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
            btnFindNextVerify.setTextColor(getResources().getColor(R.color.white));
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



    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        if(throwable.getErrorCode().equals("105")){
            ToastUtil.show(getString(R.string.tv_error_enter));
            phraseList.clear();
        }else{
            ToastUtil.show(getString(R.string.server_connection_error));

        }
    }

    @Override
    public void updateReqCheckUser(LoginStatus result) {
        dismissDialog();
//        if(TextUtils.isEmpty(result))return;
//        ClientConfig.Instance().setSaveName(result);
//        EventBus.getDefault().post(new PosInfo(654));
        navigation( ArouterConstants.FINDMONEYSUCCESS);
     //   intentFActivity(FindMoneySuccessActivity.class);
        this.finish();

    }
}
