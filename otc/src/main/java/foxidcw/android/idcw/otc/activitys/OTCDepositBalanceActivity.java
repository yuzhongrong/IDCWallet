package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.fragment.OTCAcceptBuyFragment;
import foxidcw.android.idcw.otc.fragment.OTCAcceptSellFragment;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceListBean;
import foxidcw.android.idcw.otc.widgets.dialog.OTCRechargeDepositPopWindow;

/**
 * Created by hpz on 2018/5/4.
 */

@Route(path = OTCConstant.DEPOSITBALANCE, name = "承兑商保证金余额列表页面")
public class OTCDepositBalanceActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    /**
     * title
     */
    private TextView mTvSetName;
    private RecyclerView mRvBalance;
    private LinearLayout mBtnRechargeDeposit;
    private LinearLayout mBtnDepositManager;
    private SlidingTabLayout mTablayout;
    private ViewPager mFragmentviewpager;

    private String[] mTitles = null;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private CommonAdapter commonAdapter;

    @Autowired
    OTCMoneyBagListProviderServices mWalletListServices;
    private ImageView mIvType;
    /**
     * BTC
     */
    private TextView mTvType;
    /**
     * 2.2123
     */
    private TextView mTvBalance;
    private FrameLayout mCdDepositView;
    private View view2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_balance;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mRvBalance = (RecyclerView) findViewById(R.id.rv_balance);
        mTablayout = (SlidingTabLayout) findViewById(R.id.tablayout);
        mFragmentviewpager = (ViewPager) findViewById(R.id.fragmentviewpager);
        mBtnRechargeDeposit = (LinearLayout) findViewById(R.id.btn_recharge_deposit);
        mBtnDepositManager = (LinearLayout) findViewById(R.id.btn_deposit_manager);
        mIvType = (ImageView) findViewById(R.id.iv_type);
        mTvType = (TextView) findViewById(R.id.tv_type);
        mTvBalance = (TextView) findViewById(R.id.tv_balance);
        mCdDepositView = (FrameLayout) findViewById(R.id.cd_deposit_view);
        mBtnDepositManager.setOnClickListener(this);
        mBtnRechargeDeposit.setOnClickListener(this);
        mMrBackLayout.setOnClickListener(this);
        mTvSetName.setText(getString(R.string.str_otc_set_assurer));

        mTitles = new String[]{getString(R.string.str_otc_assurer_buy), getString(R.string.str_otc_assurer_sell)};
        mFragments.add(OTCAcceptBuyFragment.getInstance("FLASHSCORD"));
        mFragments.add(OTCAcceptSellFragment.getInstance("OTCSCORD"));

        mTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mFragmentviewpager.setCurrentItem(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });


//        mFragmentviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                mTablayout.setCurrentTab(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        mTablayout.setViewPager(mFragmentviewpager, mTitles, this, mFragments);//不需要设置adapter
        mFragmentviewpager.setCurrentItem(0);

        if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("1")||AppLanguageUtils.getLanguageLocalCode(mCtx).equals("8")){
            mTablayout.setTabPadding(40);
        }

        initCurrRecycler();
    }

    private void initCurrRecycler() {
        //view2 = View.inflate(mCtx, R.layout.tv_deposit_text_layout, null);
        commonAdapter = new CommonAdapter<OTCDepositBalanceListBean.DepositListBean>(R.layout.item_deposit_balance) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCDepositBalanceListBean.DepositListBean item) {
                GlideUtil.loadImageView(mContext, item.getLogo(), (ImageView) helper.getView(R.id.iv_type));
                ((TextView) helper.getView(R.id.tv_type)).setText(item.getCoinCode().toUpperCase());
                ((TextView) helper.getView(R.id.tv_balance)).setText(Utils.toSubStringDegist(item.getUseNum(), 4));

                helper.getConvertView().setOnClickListener(v -> {

                });
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRvBalance);
        mRvBalance.setLayoutManager(linearLayoutManager);
        mRvBalance.setAdapter(commonAdapter);
        //setFooter(mRvBalance);
    }

    //添加底部的方法
    private void setFooter(RecyclerView view) {
        commonAdapter.setFooterView(view2);
    }

    @Override
    protected void onEvent() {
        requestWalletList(true);//网络请求数据
    }

    private void requestWalletList(boolean isShowProgress) {
        mWalletListServices.requestDepositBalanceList()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<OTCDepositBalanceListBean>(this, isShowProgress) {
                    @Override
                    public void onSuccess(OTCDepositBalanceListBean bean) {

                        commonAdapter.setNewData(bean.getDepositList());

                        if (bean.getDepositList() != null && bean.getDepositList().size() == 1) {
                            mRvBalance.setVisibility(View.GONE);
                            mCdDepositView.setVisibility(View.VISIBLE);
                            GlideUtil.loadImageView(mCtx, bean.getDepositList().get(0).getLogo(), mIvType);
                            mTvType.setText(bean.getDepositList().get(0).getCoinCode().toUpperCase());
                            mTvBalance.setText(Utils.toSubStringDegist(bean.getDepositList().get(0).getUseNum(), 4));
                        }

                        if (bean.getStatus() == 4) {
                            OTCRechargeDepositPopWindow deleteCurrPayPopWindow = new OTCRechargeDepositPopWindow(mCtx);
                            deleteCurrPayPopWindow.showPopupWindow();
                            deleteCurrPayPopWindow.getSkipSureDelete().setOnClickListener(v -> {
                                deleteCurrPayPopWindow.dismiss();
                                navigation(OTCConstant.RECHARGEBALANCE);
                            });
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            EventBus.getDefault().post(new PosInfo(603));
            this.finish();
        } else if (i == R.id.btn_recharge_deposit) {
            navigation(OTCConstant.RECHARGEBALANCE);
        } else if (i == R.id.btn_deposit_manager) {
            navigation(OTCConstant.DEPOSITMANAGER);
        } else {
        }
    }

    @Subscriber
    public void refreshBalanceList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 167) {
            requestWalletList(true);//网络请求数据
        }
    }
}
