package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.GetCoinProviderServices;
import com.idcg.idcw.model.bean.UserBean;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.google.gson.Gson;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by admin-2 on 2018/3/16.
 */

@Route(path = ProviderPath.ACCOUNT_LOGIN,name = "暴露获取赠币接口")
public class GetCoinProviderServicesLogic implements GetCoinProviderServices {


    @Override
    public Flowable<Object> getCoinProvider(UserBean bean) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient().builder(HttpApi.class)
                .requestGetCoin(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(bean)))
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public void init(Context context) {

    }
}
