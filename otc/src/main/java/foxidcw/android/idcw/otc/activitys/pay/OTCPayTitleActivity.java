package foxidcw.android.idcw.otc.activitys.pay;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.otc.R;

public abstract class OTCPayTitleActivity<M extends BaseModel, P extends BasePresenter> extends BaseWalletActivity<M, P> implements View.OnClickListener {

    protected FrameLayout mTitleContainer = null;
    protected LinearLayout mTitleBar = null;
    protected ImageButton mLeftBtn = null;
    protected ImageButton mRightBtn = null;
    protected TextView mLeftTv = null;
    protected TextView mTitleTv = null;
    protected TextView mRightTv = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_otc_pay_title);
        mTitleContainer = $(R.id.id_ac_title_base_container);
        mTitleBar = $(R.id.pay_title_otc_ll);
        mLeftBtn = $(R.id.pay_title_otc_left_ib);
        mRightBtn = $(R.id.pay_title_otc_right_ib);
        mLeftTv = $(R.id.pay_title_otc_left_tv);
        mTitleTv = $(R.id.pay_title_otc_middle_tv);
        mRightTv = $(R.id.pay_title_otc_right_tv);
        initTitleBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otc_pay_title;
    }

    @Override
    protected void onInitView(Bundle bundle) {

    }

    private void initTitleBar() {
        String leftText = getLeftText();
        int leftRes = getLeftRes();
        String middle = getTitleText();
        String rightText = getRightText();
        int rightRes = getRightRes();
        int color = getTitleBarColor();

        setTitleBarColor(color);
        setLeftRes(leftRes);
        setLeftText(leftText);
        setRightRes(rightRes);
        setRightText(rightText);
        mTitleTv.setText(middle);
        setLeftClickListener();
        setRightClickListener();
    }

    private void setRightClickListener() {
        if (mRightTv.getVisibility() == View.VISIBLE) {
            viewClicks(mRightTv,false);
            //mRightTv.setOnClickListener(this);
        } else if (mRightBtn.getVisibility() == View.VISIBLE) {
            viewClicks(mRightBtn,false);
            //mRightBtn.setOnClickListener(this);
        }
    }

    @SuppressLint("CheckResult")
    private void viewClicks(View view, boolean isLeft) {
        RxView.clicks(view)
                .throttleFirst(2, TimeUnit.SECONDS)
                .compose(bindToLifecycle())
                .subscribe(aBoolean -> {
                    if (isLeft) onLeftIconClick();
                    else onRightIconClick();
                });
    }

    private void setLeftClickListener() {
        if (mLeftTv.getVisibility() == View.VISIBLE) {
            viewClicks(mLeftTv,true);
            //mLeftTv.setOnClickListener(this);
        } else if (mLeftBtn.getVisibility() == View.VISIBLE) {
            viewClicks(mLeftBtn,true);
            //mLeftBtn.setOnClickListener(this);
        }
    }

    private void setRightText(String text) {
        mRightTv.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        mRightTv.setText(text);
    }

    private void setRightRes(int resId) {
        mRightBtn.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        mRightBtn.setImageResource(resId);
    }

    private void setLeftText(String text) {
        mLeftTv.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        mLeftTv.setText(text);
    }

    private void setLeftRes(int resId) {
        mLeftBtn.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        mLeftBtn.setImageResource(resId);
    }

    private void setTitleBarColor(int color) {
        mTitleBar.setBackgroundColor(color);
    }

    protected int getRightRes() {
        return 0;
    }

    protected String getRightText() {
        return "";
    }

    protected abstract String getTitleText();

    protected int getLeftRes() {
        return R.mipmap.icon_back;
    }

    protected String getLeftText() {
        return "";
    }

    protected int getTitleBarColor() {
        return getResources().getColor(R.color.tipper_blue_color);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, mTitleContainer);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pay_title_otc_left_ib || id == R.id.pay_title_otc_left_tv) {
            onLeftIconClick();
        } else if (id == R.id.pay_title_otc_right_ib || id == R.id.pay_title_otc_right_tv) {
            onRightIconClick();
        }
    }

    protected void onRightIconClick() {

    }

    protected void onLeftIconClick() {
        finish();
    }
}
