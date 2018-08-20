package com.idcw.pay.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.idcw.pay.api.CommonApi;
import com.idcw.pay.iprovider.SecurityPayValidAddressServices;
import com.idcw.pay.model.param.PayValidAddressReqParam;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_SecurityPayValidAddressServices;

@Route(path = path_SecurityPayValidAddressServices, name = "校验地址暴露接口")
public class SecurityPayValidAddressLogic implements SecurityPayValidAddressServices {
    @Override
    public Flowable<Boolean> validAddress(PayValidAddressReqParam reqParam) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(CommonApi.class)
                .validAddress(reqParam)
                .compose(new DefaultTransformer<Boolean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public Flowable<String> validComplicatedAddress(PayValidAddressReqParam reqParam) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(CommonApi.class)
                .validComplicatedAddress(reqParam)
                .compose(new DefaultTransformer<String>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public void init(Context context) {

    }
}
