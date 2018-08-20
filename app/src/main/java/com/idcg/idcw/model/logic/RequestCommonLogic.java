package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.bean.TxParmInfoBean;
import com.idcg.idcw.model.params.RequestCommonParam;
import com.idcg.idcw.presenter.interfaces.TradeDetailContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class RequestCommonLogic implements TradeDetailContract.Model {
    @Override
    public Flowable<Object> RequestCommon(RequestCommonParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .RequestCommon(reqParam)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<TxParmInfoBean> RequestTxIdData(RequestCommonParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .RequestTxIdData(reqParam)
                .compose(new DefaultTransformer<TxParmInfoBean>());
    }
}
