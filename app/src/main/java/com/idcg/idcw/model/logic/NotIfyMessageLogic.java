package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.bean.NotifyMessageBean;
import com.idcg.idcw.model.params.NotifyMessageReqParam;
import com.idcg.idcw.presenter.interfaces.TotalAssetContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class NotIfyMessageLogic implements TotalAssetContract.Model {
    @Override
    public Flowable<NotifyMessageBean> RequestNotifyMessage(NotifyMessageReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestNotifyMessage(reqParam)
                .compose(new DefaultTransformer<NotifyMessageBean>());
    }
}
