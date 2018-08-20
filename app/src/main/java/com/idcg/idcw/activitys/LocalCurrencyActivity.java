package com.idcg.idcw.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import com.idcg.idcw.configs.ClientConfig;
import com.idcg.idcw.model.bean.LanguageBean;
import com.idcg.idcw.model.logic.LocalCurrencyLogic;
import com.idcg.idcw.model.params.LocalCurrencyReqParam;
import com.idcg.idcw.presenter.impl.LocalCurrencyPresenterImpl;
import com.idcg.idcw.presenter.interfaces.LocalCurrencyContract;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/17 17:20
 **/
@Route(path = ArouterConstants.LOCAL_CURRENCY, name = "本地货币")
public class LocalCurrencyActivity extends BaseWalletActivity<LocalCurrencyLogic, LocalCurrencyPresenterImpl> implements LocalCurrencyContract.View {

    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.rv_currency_list)
    RecyclerView rvCurrencyList;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;

    private static final String[] CURRENCY_LIST = {"USD", "EUR", "CNY", "HKD", "JPY", "KRW", "GBP", "VND"};
    private static final int[] CURRENCY_NAMES = {
            R.string.tv_usd,
            R.string.tv_ouyuan,
            R.string.tv_rmb,
            R.string.gangbi,
            R.string.tv_japan,
            R.string.tv_kor,
            R.string.tv_yingbang,
            R.string.tv_yuenanyu};
    private static final int[] CURRENCY_ICONS = {
            R.mipmap.img_usd,
            R.mipmap.img_ouyuan,
            R.mipmap.img_rmb,
            R.mipmap.img_hk,
            R.mipmap.img_japan,
            R.mipmap.img_hanyuan,
            R.mipmap.img_yingbang,
            R.mipmap.img_yuenan};


    private BaseQuickAdapter<LanguageBean,BaseViewHolder> currencyCommonAdapter;
    private List<LanguageBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_local_currency;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        tvSetName.setText(R.string.title_local_currency);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadmore(false);
        initRecycler();
    }

    private void initRecycler() {
        try {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvCurrencyList.setLayoutManager(linearLayoutManager);
            rvCurrencyList.setAdapter(currencyCommonAdapter = new BaseQuickAdapter<LanguageBean, BaseViewHolder>(R.layout.item_language, getCurrencyInfo()) {
                @Override
                protected void convert(BaseViewHolder helper, LanguageBean item) {
                    if (item == null) return;
                    helper.setImageResource(R.id.img_tag, item.getImg());
                    helper.setText(R.id.img_name, item.getName());
                    if (item.isSelect()) {
                        helper.getView(R.id.ra_btn).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ra_btn_select).setVisibility(View.GONE);
                    } else {
                        helper.getView(R.id.ra_btn).setVisibility(View.GONE);
                        helper.getView(R.id.ra_btn_select).setVisibility(View.VISIBLE);
                    }
                }
            });

            rvCurrencyList.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == position) {
                            list.get(i).setSelect(true);
                        } else {
                            list.get(i).setSelect(false);
                        }
                    }
                    currencyCommonAdapter.notifyDataSetChanged();
                    LocalCurrencyReqParam reqParam = new LocalCurrencyReqParam();
                    reqParam.setContent(CURRENCY_LIST[position]);
                    reqParam.setVerifyCode("");
                    reqParam.setType("4");
                    showDialog();
                    mPresenter.requestModifyCurrency(reqParam);
                }
            });
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    private List<LanguageBean> getCurrencyInfo() {
        String baseLocalCurrency = ClientConfig.Instance().getSaveLocal();
        for (int i = 0; i < CURRENCY_NAMES.length; i++) {
            LanguageBean itemBean = new LanguageBean();
            itemBean.setName(getString(CURRENCY_NAMES[i]));
            itemBean.setImg(CURRENCY_ICONS[i]);
            itemBean.setSelect(baseLocalCurrency.equals(CURRENCY_LIST[i]));
            list.add(itemBean);
        }
        return list;
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        ToastUtil.show(getString(R.string.server_connection_error));
    }

    @OnClick({R.id.mr_back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
        }
    }

    @Override
    public void updateRequestModifyCurrency(String result) {
        dismissDialog();
        AppManager.getInstance().finishAllActivity();
        Intent intent = new Intent(LocalCurrencyActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("language", "language");
        intent.putExtra("Language", bundle);
        startActivity(intent);
    }

    @Override
    protected void checkAppVersion() {
    }
}
