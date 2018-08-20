package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.params.CheckUserReqParam;
import com.idcg.idcw.presenter.interfaces.FindSureMoneyContract;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by admin-2 on 2018/3/15.
 */

public class FindSureMoneyLogic implements FindSureMoneyContract.Model {
    @Override
    public Flowable<LoginStatus> ReqCheckUser(List<CheckUserReqParam> reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestCheckUser(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(reqParam)))
                .compose(new DefaultTransformer<LoginStatus>());
    }
}
