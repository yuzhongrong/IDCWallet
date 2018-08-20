package com.idcg.idcw.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.idcg.idcw.R;
import com.idcg.idcw.app.WalletApplication;
import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import com.idcg.idcw.model.bean.RunChartDataBean;
import com.idcg.idcw.model.logic.RunChartFragmentLogic;
import com.idcg.idcw.model.params.SettingConfigReqParam;
import com.idcg.idcw.presenter.impl.RunChartFragmentPresenterImpl;
import com.idcg.idcw.presenter.interfaces.RunChartFragmentContract;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.fragments
 * 备注消息：
 * 修改时间：2018/3/18 19:44
 **/

public class RunChartFragment extends BaseWalletFragment<RunChartFragmentLogic, RunChartFragmentPresenterImpl>
        implements RunChartFragmentContract.View {

    private String mTitle;
    private int chartId;
    @BindView(R.id.runchart)
    RecyclerView settingrecyclerView;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    CommonAdapter commonAdapter;
    private View view2;

    private HashMap<Integer, Boolean> map1 = new HashMap<>();
    private boolean isAsset = true;
    private String showType;

    public static RunChartFragment getInstance(String title) {
        RunChartFragment self = new RunChartFragment();
        self.mTitle = title;
        return self;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_runchart_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        smartrefreshlayout.setEnableRefresh(false);
        smartrefreshlayout.setEnableLoadmore(false);
        view2 = View.inflate(getContext(), R.layout.chart_run_layout, null);
        commonAdapter = new CommonAdapter<RunChartDataBean.CurrencyListBean>(R.layout.item_runchar_layout) {
            @Override
            public void commonconvert(BaseViewHolder helper, RunChartDataBean.CurrencyListBean item) {
                //helper.setVisible(foxidcw.android.idcw.otc.R.id.view_diver, helper.getAdapterPosition()!=getItemCount()-1);
                LogUtil.e("commonAdapter>>>", commonAdapter.getItemCount() + "");
                LogUtil.d("--------commonconvert_url------>"+item.getLogo_url());
                Glide.with(WalletApplication.getContext()).load(item.getLogo_url()).into((ImageView) helper.getView(R.id.icon));


                if (item.getId() == 0 && item.getCurrency().equals(getString(R.string.total_assets))) {//总资产
                    ((ImageView) helper.getView(R.id.icon)).setBackgroundResource(R.mipmap.icon_assets_total);//不要使用ImageRescouse
                    ((CheckBox) helper.getView(R.id.runcheckboxtwo)).setVisibility(View.INVISIBLE);
                } else {
                    ((CheckBox) helper.getView(R.id.runcheckboxtwo)).setVisibility(View.VISIBLE);
                }

                if (item.getCurrency().equals(getString(R.string.total_assets))) {
                    ((TextView) helper.getView(R.id.btype)).setText(item.getCurrency());
                } else {
                    ((TextView) helper.getView(R.id.btype)).setText(item.getCurrency().toUpperCase());
                }
                LogUtil.e("commonAdapter===>", commonAdapter.getData().size() + "");

                if (item.isIsDefault()) {
                    if (ACacheUtil.get(getActivity()).getAsString(AcacheKeys.switchtype) == null)
                        return;
                    if (ACacheUtil.get(getActivity()).getAsString(AcacheKeys.switchtype).equals("0")) {
                        ((CheckBox) helper.getView(R.id.checkboxone)).setChecked(true);
                        if (((CheckBox) helper.getView(R.id.checkboxone)).isChecked()) {
                            ((CheckBox) helper.getView(R.id.checkboxone)).setClickable(false);
                        }
                        ((CheckBox) helper.getView(R.id.runcheckboxtwo)).setChecked(false);
                    } else if (ACacheUtil.get(getActivity()).getAsString(AcacheKeys.switchtype).equals("1")) {
                        ((CheckBox) helper.getView(R.id.runcheckboxtwo)).setChecked(true);
                        if (((CheckBox) helper.getView(R.id.runcheckboxtwo)).isChecked()) {
                            ((CheckBox) helper.getView(R.id.runcheckboxtwo)).setClickable(false);
                        }
                        ((CheckBox) helper.getView(R.id.checkboxone)).setChecked(false);
                    }
                } else {
                    ((CheckBox) helper.getView(R.id.checkboxone)).setChecked(false);
                    ((CheckBox) helper.getView(R.id.runcheckboxtwo)).setChecked(false);
                }

                helper.getView(R.id.checkboxone).setOnClickListener(v -> {
                    if (helper.getLayoutPosition() == 0) {
                        isAsset = true;
                    }
                    requestSettingConfig(String.valueOf(0), String.valueOf(item.getId()));
                    ((CheckBox) v).setChecked(true);
                });

                helper.getView(R.id.runcheckboxtwo).setOnClickListener(v -> {
                    requestSettingConfig(String.valueOf(1), String.valueOf(item.getId()));
                    ((CheckBox) v).setChecked(true);
                });
                if (commonAdapter.getItemCount() == 1) {
                    ((CheckBox) helper.getView(R.id.checkboxone)).setChecked(true);
                }
                if (helper.getLayoutPosition() == 0 && ((CheckBox) helper.getView(R.id.runcheckboxtwo)).isChecked()) {
                    ((CheckBox) helper.getView(R.id.checkboxone)).setChecked(true);
                }
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        settingrecyclerView.setLayoutManager(linearLayoutManager);
        settingrecyclerView.setAdapter(commonAdapter);
    }

    private void requestSettingConfig(String showType, String currencyId) {
        showDialog();
        this.showType = showType;
        SettingConfigReqParam reqParam = new SettingConfigReqParam();
        reqParam.setId(String.valueOf(chartId));
        reqParam.setCurrencyId(currencyId);
        reqParam.setShowType(String.valueOf(showType));
        mPresenter.requestSettingConfig(reqParam);
    }


    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    protected void lazyFetchData() {
        lazyLoadData();
    }

    private void lazyLoadData() {
        showDialog();
        mPresenter.requestRunChartData();
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        if (throwable.getState() == 1) {
            RunChartDataBean.CurrencyListBean currencyListBean = new RunChartDataBean.CurrencyListBean();
            if (isAdded()) {
                currencyListBean.setCurrency(getResources().getString(R.string.total_assets));
            }
            currencyListBean.setId(0);
            //总资产选中
            if (isAsset == true) {
                currencyListBean.setIsDefault(true);
            }
            ArrayList<RunChartDataBean.CurrencyListBean> list = new ArrayList<>();
            list.add(currencyListBean);
            commonAdapter.setNewData(list);
        } else {
            if (isAdded()) {
                ToastUtil.show(getResources().getString(R.string.server_connection_error));
            }
        }
    }

    @Override
    public void updateRequestRunChartData(RunChartDataBean param) {
        dismissDialog();
        chartId = param.getId();
        RunChartDataBean.CurrencyListBean currencyListBean = new RunChartDataBean.CurrencyListBean();
        if (isAdded()) {
            currencyListBean.setCurrency(getResources().getString(R.string.total_assets));
        }
        //只要请求到的4组数据有一个是true的话 就构造一个false的总资产
        for (RunChartDataBean.CurrencyListBean itembean : param.getCurrencyList()) {
            if (itembean.isIsDefault()) {
                isAsset = false;
                currencyListBean.setIsDefault(false);
            }
        }
        //总资产选中
        if (isAsset == true) {
            currencyListBean.setIsDefault(true);
        }
        currencyListBean.setId(0);
        param.getCurrencyList().add(0, currencyListBean);
        //setHeader(settingrecyclerView);
        commonAdapter.setNewData(param.getCurrencyList());
        LogUtil.e("配置信息===>", param.getCurrencyList().size() + "");
    }

    @Override
    public void updateRequestSettingConfig(Object object) {
        dismissDialog();
        ACacheUtil.get(getActivity()).put(AcacheKeys.switchtype, showType);
        lazyLoadData();
    }

    @Subscriber
    public void onRefreshRunInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 168) {
                isAsset = true;
                lazyLoadData();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }
}
