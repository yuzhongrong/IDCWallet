package com.idcg.idcw.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;

import com.idcg.idcw.R;
import com.idcg.idcw.fragments.AddAssetFragment;
import com.idcg.idcw.fragments.OptionalCurrFragment;
import com.idcg.idcw.fragments.RunChartFragment;

import foxidcw.android.idcw.common.model.bean.PosInfo;

import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.base.BaseView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/18 19:40
 **/
@Route(path = ArouterConstants.ADD_NEW_CURRENCY, name = "添加新币")
public class AddNewCurrencyActivity extends BaseWalletActivity {

    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tablayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.fragmentviewpager)
    ViewPager pager;

    private String[] mTitles = null;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_add_currency;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        mTitles = new String[]{getString(R.string.str_your_select),getString(R.string.tv_add_asset), getString(R.string.tv_market_price)};
        mFragments.add(OptionalCurrFragment.getInstance("OPTIONAL"));
        mFragments.add(AddAssetFragment.getInstance("ADDASET"));
        mFragments.add(RunChartFragment.getInstance("RUNCHART"));
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {//选中tab相应的请求数据
                switch (position){
                    case 0:
                        EventBus.getDefault().post(new PosInfo(170));
                        ACacheUtil.get(mCtx).put("refreshSelect","refreshSelect");
                        break;
                    case 2:
                        EventBus.getDefault().post(new PosInfo(168));
                        break;
                        default:
                            break;
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        tabLayout.setViewPager(pager, mTitles, this, mFragments);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {//左右切换滑动页面相应请求数据
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        EventBus.getDefault().post(new PosInfo(170));
                        ACacheUtil.get(mCtx).put("refreshSelect","refreshSelect");
                        break;
                    case 2:
                        EventBus.getDefault().post(new PosInfo(168));
                        break;
                        default:
                            break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        pager.setCurrentItem(0);//默认进入页面显示第一个fragment
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onEvent() {

    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @OnClick({R.id.mr_back_layout, R.id.tablayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                EventBus.getDefault().post(new PosInfo(998));
                this.finish();
                break;
            case R.id.tablayout:
                break;
        }
    }

    @Subscriber
    public void onRefreshAssetsInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 7654) {
                this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new PosInfo(998));
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }

    @Override
    protected BaseView getView() {
        return null;
    }
}
