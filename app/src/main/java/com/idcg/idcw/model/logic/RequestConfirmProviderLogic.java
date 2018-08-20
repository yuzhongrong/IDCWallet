package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.RequestConfirmProviderServices;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */
@Route(path = ProviderPath.path_GetCoinProviderServicesLogic,name = "暴露获取已阅状态")
public class RequestConfirmProviderLogic implements RequestConfirmProviderServices {
    @Override
    public Flowable<Object> getConfirmStatus(String id) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .RequestConfirmStatus(id)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public void init(Context context) {

    }
}
