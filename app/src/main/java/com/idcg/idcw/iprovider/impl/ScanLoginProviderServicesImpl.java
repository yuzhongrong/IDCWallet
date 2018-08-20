package com.idcg.idcw.iprovider.impl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.foxcommon.ProviderPath;
import foxidcw.android.idcw.foxcommon.provider.bean.ScanLoginBean;
import foxidcw.android.idcw.foxcommon.provider.params.ScanLoginReqParam;
import foxidcw.android.idcw.foxcommon.provider.services.ScanLoginProviderServices;
import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/31.
 */
@Route(path = ProviderPath.path_ScanLoginProviderServices,name = "扫码登录接口暴露")
public class ScanLoginProviderServicesImpl implements ScanLoginProviderServices{
    @Override
    public Flowable<ScanLoginBean> requestScanLoginProvider(ScanLoginReqParam param) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .scanLogin(param)
                .compose(new DefaultTransformer<ScanLoginBean>());
    }

    @Override
    public void init(Context context) {

    }
}
