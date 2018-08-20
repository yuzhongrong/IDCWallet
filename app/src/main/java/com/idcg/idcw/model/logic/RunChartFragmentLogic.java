package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.bean.RunChartDataBean;
import com.idcg.idcw.model.params.SettingConfigReqParam;
import com.idcg.idcw.presenter.interfaces.RunChartFragmentContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 18:12
 **/

public class RunChartFragmentLogic implements RunChartFragmentContract.Model {
    @Override
    public Flowable<RunChartDataBean> requestRunChartData() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestRunChartData()
                .compose(new DefaultTransformer<RunChartDataBean>());
    }

    @Override
    public Flowable<Object> requestSettingConfig(SettingConfigReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestSettingConfig(reqParam)
                .compose(new DefaultTransformer<Object>());
    }
}
