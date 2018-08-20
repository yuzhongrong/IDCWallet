package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.params.LocalCurrencyReqParam;
import com.idcg.idcw.presenter.interfaces.LocalCurrencyContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 17:30
 **/

public class LocalCurrencyLogic implements LocalCurrencyContract.Model {
    @Override
    public Flowable<String> requestModifyCurrency(LocalCurrencyReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestModifyCurrency(reqParam)
                .compose(new DefaultTransformer<String>());
    }
}
