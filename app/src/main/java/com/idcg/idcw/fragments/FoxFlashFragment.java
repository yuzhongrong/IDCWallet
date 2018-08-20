package com.idcg.idcw.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.OTCCannotInBean;
import com.idcg.idcw.utils.EasyBlur;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.TabEntity;
import com.cjwsc.idcm.widget.NoScrollViewPager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.OTCDotShowBean;
import foxidcw.android.idcw.common.utils.SpUtils;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTwoBtnTitlePopWindow;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin-2 on 2018/3/14.
 */

public class FoxFlashFragment extends BaseWalletFragment {
    @BindView(R.id.tablayout)
    CommonTabLayout   tabLayout;
    @BindView(R.id.fragmentviewpager)
    NoScrollViewPager pager;
    Unbinder unbinder;
    @BindView(R.id.ll_flash_layout)
    LinearLayout llFlashLayout;
    @BindView(R.id.ll_otc_layout)
    LinearLayout llOtcLayout;

    private String[] mTitles = null;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private OTCTwoBtnTitlePopWindow mOTCTwoBtnTitlePopWindow;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[]        mMIconUnselectIds;
    private int[]        mMIconSelectIds;
    private final int OTC_PAGE_POSITION = 1; //显示小红点的页面-法币OTC
    private final String IS_SHOW_DOT= "IS_SHOW_DOT";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fox_flash;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        EventBus.getDefault().registerSticky(this);
        mTitles = new String[]{getString(R.string.bibi_exchange), getString(R.string.fabi_otc)};
        mMIconUnselectIds = new int[]{R.drawable.icon_bibi_unselected, R.drawable.icon_otc_unselected};
        mMIconSelectIds = new int[]{R.drawable.icon_bibi_selected, R.drawable.bibi_otc_selected};
        initViewPager(); 
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                pager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }



        });
//        tabLayout.setViewPager(pager,mTitles,getActivity(),mFragments); //2018/06/13
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshTabState(position);
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setCurrentItem(0);
        refreshTabState(0);

        MsgView msgView        = tabLayout.getMsgView(0);
        MsgView msgViewOtc = tabLayout.getMsgView(OTC_PAGE_POSITION);
        if (msgView != null) {
            msgView.setStrokeWidth(0);
        }
        if (msgViewOtc != null) {
            msgViewOtc.setStrokeWidth(0);
        }
    }


    private void refreshTabState(int position) {
        TextView titleView1 = tabLayout.getTitleView(1);
        TextView titleView0 = tabLayout.getTitleView(0);
        if(position == 0) {
            titleView0.setText(new SpanUtils().appendImage(R.drawable.icon_bibi_selected,SpanUtils.ALIGN_CENTER).append(" "+mTitles[0]).create());
            titleView1.setText(new SpanUtils().appendImage(R.drawable.icon_otc_unselected, SpanUtils.ALIGN_CENTER).append(" "+mTitles[1]).create());
            tabLayout.setIndicatorWidth(SizeUtils.px2dp(Layout.getDesiredWidth(titleView0.getText(), titleView0.getPaint())));
        }else if(position ==1) {
            titleView0.setText(new SpanUtils().appendImage(R.drawable.icon_bibi_unselected,SpanUtils.ALIGN_CENTER).append(" "+mTitles[0]).create());
            titleView1.setText(new SpanUtils().appendImage(R.drawable.bibi_otc_selected,SpanUtils.ALIGN_CENTER).append(" "+mTitles[1]).create());
            tabLayout.setIndicatorWidth(SizeUtils.px2dp(Layout.getDesiredWidth(titleView1.getText(), titleView0.getPaint())));
        }
    }

    @Override
    protected void onEvent() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SpUtils.getBooleanData(IS_SHOW_DOT, false)){ //用于交易页面未创建的小红点显示
            tabLayout.showMsg(OTC_PAGE_POSITION, 0);
            setOTCDot();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SpUtils.setBooleanData(IS_SHOW_DOT, false); //只使用一次小红点的sp
    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tablayout, R.id.ll_flash_layout, R.id.ll_otc_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tablayout:
                break;
            case R.id.ll_flash_layout:
//                navigation(EXCHANGEDETAIL);
                Observable.just(shotActivity(getActivity()))
                        .doOnNext(bitmap -> {
                            //保存pin页面需要的高斯模糊背景
                            ACacheUtil.get(mContext).put("bibiflashbg", EasyBlur.with(mContext)
                                    .bitmap(bitmap) //要模糊的图片
                                    .policy(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? EasyBlur
                                            .BlurPolicy.RS_BLUR : EasyBlur.BlurPolicy.FAST_BLUR)
                                    .radius(25)//模糊半径
                                    .blur());
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            ARouter.getInstance()
                                    .build(ArouterConstants.CheckFlashPin)
                                    .navigation();
                        });
                break;
            case R.id.ll_otc_layout:
                navigation(ArouterConstants.OTC);
                break;
        }
    }

    /**
     * 根据指定的Activity截图（带空白的状态栏）
     *
     * @param context 要截图的Activity
     * @return Bitmap
     */
    public static Bitmap shotActivity(Activity context) {
        View view = context.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view
                .getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    @Subscriber
    public void switch2Flash(OTCCannotInBean otcCannotInBean){
        pager.setCurrentItem(0);
    }

    private void initViewPager() {
        mFragments.clear();
        mTabEntities.clear();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mMIconSelectIds[i], mMIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        mFragments.add(FlashRecordFragment.getInstance("FLASHSCORD"));
        mFragments.add(OTCRecordFragment.getInstance("OTCSCORD"));
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    //设置小红点
    public void setOTCDot(){
        MsgView otcMsgView = tabLayout.getMsgView(OTC_PAGE_POSITION);
        if(otcMsgView != null){
            UnreadMsgUtils.setSize(otcMsgView, SpUtils.dp2px(9f));
            otcMsgView.setStrokeWidth(0);
            otcMsgView.setBackgroundColor(getResources().getColor(R.color.c_FF3F3F));
        }
        tabLayout.setMsgMargin(OTC_PAGE_POSITION, -1.5f, 2);
    }

    @Subscriber(tag =  EventTags.TAGS_OTC_SHOW_DOT)
    public void reciveOTCShowDot(OTCDotShowBean otcDotShowBean){
        if(otcDotShowBean!=null){//显示小红点
            if(otcDotShowBean.isShow()){
                if(tabLayout.getMsgView(OTC_PAGE_POSITION) != null && tabLayout.getMsgView(OTC_PAGE_POSITION).getVisibility() == View.VISIBLE) return;
                tabLayout.showMsg(OTC_PAGE_POSITION, 0);
                setOTCDot();
                EventBus.getDefault().removeStickyEvent(otcDotShowBean.getClass());
            }else{
                if(tabLayout.getMsgView(OTC_PAGE_POSITION) != null && tabLayout.getMsgView(OTC_PAGE_POSITION).getVisibility() == View.GONE) return;
                tabLayout.hideMsg(OTC_PAGE_POSITION);

            }
        }
    }

}
