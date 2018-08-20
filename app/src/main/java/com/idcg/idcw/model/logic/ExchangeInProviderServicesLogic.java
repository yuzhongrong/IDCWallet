package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.ExchangeInProviderServices;
import com.idcg.idcw.model.bean.ExchangeResultBean;
import com.idcg.idcw.model.params.ExchangeInParam;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.google.gson.Gson;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author yiyang
 */
@Route(path = ProviderPath.path_ExchangeInProviderServices,name = "币币闪兑兑入接口暴露")
public class ExchangeInProviderServicesLogic implements ExchangeInProviderServices {

    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<ExchangeResultBean> exchangeIn(ExchangeInParam param) {
        HttpUtils.getInstance(WalletApplication.getContext())
                .updateTimeOut(90);
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient().builder(HttpApi.class)
                .requestExchangeIn(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(param)))
                .compose(new DefaultTransformer<>())
                .doOnTerminate(() -> HttpUtils.getInstance(WalletApplication.getContext()).resetTimeOut());
    }
}
