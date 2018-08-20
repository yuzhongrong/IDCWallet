package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.bean.ExchangeDataBean;
import com.idcg.idcw.model.params.FlashRecordListReqParam;
import com.idcg.idcw.presenter.interfaces.FlashRecordFragmentContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author yiyang
 */
public class FlashRecordFragmentLogic implements FlashRecordFragmentContract.Model {

    @Override
    public Flowable<List<ExchangeDataBean>> requestExchangeDataList(FlashRecordListReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestExchangeDataList(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(reqParam)))
                .retry(2)
                .compose(new DefaultTransformer<List<ExchangeDataBean>>());
    }
}
