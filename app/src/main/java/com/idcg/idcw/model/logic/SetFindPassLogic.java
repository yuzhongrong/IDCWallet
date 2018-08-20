package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.params.SetFindPassReqParam;
import com.idcg.idcw.presenter.interfaces.SetFindPassContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/15.
 */

public class SetFindPassLogic implements SetFindPassContract.Model {
    @Override
    public Flowable<Object> SetFindPass(SetFindPassReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .RequesteRegister(reqParam)
                .compose(new DefaultTransformer<Object>());
    }
}
