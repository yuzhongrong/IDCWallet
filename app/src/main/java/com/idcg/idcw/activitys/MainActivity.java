package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.fragments.FoxAssetsFragment;
import com.idcg.idcw.fragments.FoxFlashFragment;
import com.idcg.idcw.fragments.FoxSettingsFragment;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.sound.SoundPlayUtils;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.BaseSignalConstants;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.cjwsc.idcm.signarl.impl.HubOnDataCallBackImpl;
import com.cjwsc.idcm.widget.navigatetab.NavigateTabBar;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.OTCDotShowBean;
import foxidcw.android.idcw.common.utils.DialogVersionUtil;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.common.utils.SpUtils;
import foxidcw.android.idcw.otc.fragment.OTCFoxDiscoveryFragment;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.iprovider.OTCGetOfferListServices;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCConfirmQuoteOrderMessageBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceListBean;
import foxidcw.android.idcw.otc.model.beans.OTCNewOrderNoticeAcceptantBean;
import foxidcw.android.idcw.otc.model.beans.OTCStateBean;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOfficeListParam;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by admin-2 on 2018/3/13.
 */
@Route(path = ArouterConstants.MAIN, name = "主页", extras = ArouterConstants.LOGIN)
public class MainActivity extends BaseWalletActivity {

    private        NavigateTabBar     mNavigateTabBar;
    public static boolean isOpen = false;
    private String languageTag = "";
    private final String IS_SHOW_DOT= "IS_SHOW_DOT";

    private final int TRADE_PAGE_POSITION = 1; //显示小红点的页面-交易页面
    private boolean mIsAcceptor = false;
    private QBadgeView badgeView;
    @Autowired
    OTCMoneyBagListProviderServices mOTCMoneyBagListProviderServices; //获取承兑商状态和保证金列表
    @Autowired
    OTCGetOfferListServices         mOTCGetOfferListServices; //获取OTC承兑信息
    private boolean mIsReciveDot = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BaseSignalConstants.isAddGroup=false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);

        isOpen = true;

        badgeView=new QBadgeView(this);
        initNavigeteTabBar();

        Intent intent = getIntent();
        Bundle bundleString = intent.getBundleExtra("Language");
        if (bundleString != null) {
            languageTag = bundleString.getString("language");
        }
        // 启动signalr
        initSignalr(BaseSignalConstants.SIGNAL_HOST, "", BaseSignalConstants.SIGNAL_HUB_NAME);

        initBadgeView(mNavigateTabBar);
    }

    @Override
    public void subScribes() {
        // 有新的报价单推送
        subscribe("otcNewestOrder", new HubOnDataCallBackImpl<OTCNewOrderNoticeAcceptantBean>() {
            @Override
            public void convert(OTCNewOrderNoticeAcceptantBean o) {
//                ToastUtil.show(o.toString());
                LogUtil.e(o.toString());
                try {
                    if (null == o) return;
                    if (o.getNum() <= 0) return;
                    EventBus.getDefault().post(o);
                    if(o.getUserId() != LoginUtils.getLoginBean(MainActivity.this).getId() && mIsAcceptor){
                        SpUtils.setBooleanData(IS_SHOW_DOT, true);
                        EventBus.getDefault().post(new OTCDotShowBean(true), EventTags.TAGS_OTC_SHOW_DOT);
                        badgeView.setBadgeNumber(-1);
                        SoundPlayUtils.play(1); //推送声音
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 用户确认报价之后推送
        subscribe("confirmQuoteOrderMessage", new HubOnDataCallBackImpl<OTCConfirmQuoteOrderMessageBean>() {
            @Override
            public void convert(OTCConfirmQuoteOrderMessageBean o) {
                LogUtil.e("用户确认了  ---> " + o.toString());
                try {
                    if (null == o) return;
                    EventBus.getDefault().post(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 订阅订单状态变更通知
        subscribe("otcOrderStatusChange", new HubOnDataCallBackImpl<OTCStateBean>() {
            @Override
            public void convert(OTCStateBean bean) {
                try {
                    if (null == bean) return;
                    EventBus.getDefault().post(bean, EventTags.TAGS_STATUS_CHANGE_MAIN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected String getUserId() {//解决有时候重连报错问题
        if (LoginUtils.getLoginBean(this) != null)
            return String.valueOf(LoginUtils.getLoginBean(this).getId());
        return "";

    }

    @Override
    protected void onEvent() {
        getAcceptantInfo();
        if (!TextUtils.isEmpty(languageTag) && languageTag.equals("language")) {
            return;
        }
        if (dialogVersionUtil == null) dialogVersionUtil = new DialogVersionUtil();
        dialogVersionUtil.checkVersion(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //既然第一次进来和从后台进来 都要更新何不放在这里更新呢
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    private void initNavigeteTabBar() {
        mNavigateTabBar = (NavigateTabBar) findViewById(R.id.home_navigate);

        String QUOTE_PAGE = getString(R.string.icon_asset);
        mNavigateTabBar.addTab(FoxAssetsFragment.class,
                new NavigateTabBar.TabParam(
                        R.drawable.icon_unselect_asset,
                        R.drawable.icon_select_asset,
                        QUOTE_PAGE));
        String TRADE_PAGE = getString(R.string.icon_market);
        mNavigateTabBar.addTab(FoxFlashFragment.class,
                new NavigateTabBar.TabParam(
                        R.drawable.icon_unselect_market,
                        R.drawable.icon_select_market,
                        TRADE_PAGE));
        String DISCOVERY_PAGE = getString(R.string.str_otc_discovery);
        mNavigateTabBar.addTab(OTCFoxDiscoveryFragment.class,
                new NavigateTabBar.TabParam(
                        R.drawable.icon_unselect_discovery,
                        R.drawable.icon_select_discovery,
                        DISCOVERY_PAGE));
        String SETTING_PAGE = getString(R.string.str_mine);
        mNavigateTabBar.addTab(FoxSettingsFragment.class,
                new NavigateTabBar.TabParam(
                        R.drawable.icon_unselect_me,
                        R.drawable.icon_select_me,
                        SETTING_PAGE));

        mNavigateTabBar.setTabSelectListener(holder ->  {
                if (QUOTE_PAGE.equals(holder.tag.toString())) {
                    mNavigateTabBar.showFragment(holder);
                } else if (TRADE_PAGE.equals(holder.tag.toString())) {
                    mNavigateTabBar.showFragment(holder);
                } else if (DISCOVERY_PAGE.equals(holder.tag.toString())) {
                    mNavigateTabBar.showFragment(holder);
                } else if (SETTING_PAGE.equals(holder.tag.toString())) {
                    mNavigateTabBar.showFragment(holder);
            }
        });
    }


    /**
     * 点击手机返回键finish app
     */
    @Override
    public void onBackPressed() {
        exitApp();
    }

    /**
     * 退出应用
     */

    private long exitTime = 0;

    public void exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.show(getString(R.string.tv_again_click_out));
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.getInstance().finishAllActivity();
            finish();
            System.exit(0);
        }
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    //获取承兑商信息, 判断用户是不是承兑商
    @SuppressLint("CheckResult")
    public void getAcceptantInfo() {
        if (mOTCMoneyBagListProviderServices != null) {
            mOTCMoneyBagListProviderServices.requestDepositBalanceList()
                                            .compose(bindUntilEvent(ActivityEvent.DESTROY))
                                            .subscribeWith(new RxProgressSubscriber<OTCDepositBalanceListBean>(this) {
                                                @Override
                                                public void onSuccess(OTCDepositBalanceListBean otcDepositBalanceListBean) {
                                                    if (otcDepositBalanceListBean != null) {
                                                        if (otcDepositBalanceListBean.getStatus() == 3) { //已开通承兑商
                                                            mIsAcceptor = true;
                                                            getOfferOrderList();
                                                        }
                                                    }
                                                }

                                                @Override
                                                protected void onError(ResponseThrowable ex) {
                                                    ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                                }
                                            });
        }

    }

    //获取承兑商报价信息
    @SuppressLint("CheckResult")
    private void getOfferOrderList() {
        mIsReciveDot = true;
        mOTCGetOfferListServices.getOTCOfferList(new OTCGetOtcOfficeListParam())
                                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                                .subscribeWith(new RxSubscriber<List<OTCNewOrderNoticeAcceptantBean>>() {
                                    @Override
                                    public void onSuccess(List<OTCNewOrderNoticeAcceptantBean> otcNewOrderNoticeAcceptantBeans) {
                                        if (otcNewOrderNoticeAcceptantBeans != null && otcNewOrderNoticeAcceptantBeans.size() > 0) {
                                            //取5条 过滤倒计时为0
                                            for (int i = 0; i < otcNewOrderNoticeAcceptantBeans.size(); i++) {
                                                OTCNewOrderNoticeAcceptantBean item = otcNewOrderNoticeAcceptantBeans.get(
                                                        i);

                                                LoginStatus loginBean = LoginUtils.getLoginBean( mCtx);

                                                //只有未报价的并且倒计时大于0秒
                                                if (item != null && (item.getDeadLineSeconds() > 0 && item.getStatus() == 8)) {
                                                    if (loginBean != null && loginBean.getId() != item.getUserId()) { // 下单的不是承兑商
                                                        badgeView.setBadgeNumber(-1);
                                                        EventBus.getDefault().postSticky(new OTCDotShowBean(true), EventTags.TAGS_OTC_SHOW_DOT);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    protected void onError(ResponseThrowable ex) {
                                        LogUtil.e("MainActivity:" + ex.ErrorMsg);
                                    }
                                });
    }

    //显示和隐藏小红点
    @Subscriber(tag =  EventTags.TAGS_OTC_SHOW_DOT)
    public void reciveOTCShowDot(OTCDotShowBean otcDotShowBean){
        if(otcDotShowBean!=null){//隐藏小红点
            if(!mIsReciveDot){ //首页未请求小红点状态不接受eventbus
                return;
            }
            if(otcDotShowBean.isShow()){
                badgeView.setBadgeNumber(-1);
            }else{
                badgeView.setBadgeNumber(0);
            }
        }
    }


    //初始化badgeview
    private void initBadgeView(NavigateTabBar mNavigateTabBar){
        ImageView img = mNavigateTabBar.getViewHolderList(TRADE_PAGE_POSITION).tabIcon;
        badgeView=new QBadgeView(this);
        badgeView.bindTarget((ViewGroup)img.getParent())
                 .setBadgeGravity(Gravity.END|Gravity.TOP)
                 .setGravityOffset(getResources().getDimensionPixelSize(R.dimen.dp30),getResources().getDimensionPixelSize(R.dimen.dp1),false)
                 .setBadgeTextSize(getResources().getDimensionPixelSize(R.dimen.dp3),false)
                 .setShowShadow(false)
                 .setBadgeNumber(0);//初始化默认设置0
    }
}
