package com.idcw.pay.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.idcw.pay.api.CommonApi;
import com.idcw.pay.iprovider.GetAccountAddressServices;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_GetAccountAddressServicer;

@Route(path = path_GetAccountAddressServicer, name = "支付暴露接口")
public class GetAccountAddressLogic implements GetAccountAddressServices {
    @Override
    public Flowable<String> getAccountAddress(String currency) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(CommonApi.class)
                .getAccountAddress(currency)
                .compose(new DefaultTransformer<String>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public void init(Context context) {

    }
}
