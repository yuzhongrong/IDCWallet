package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.VerifyUserNameProviderServices;
import com.idcg.idcw.model.params.VerfityNameReqParam;
import com.idcg.idcw.presenter.interfaces.CreateWalletContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.verfityUserName;

/**
 * Created by admin-2 on 2018/3/16.
 */

@Route(path = verfityUserName, name = "检查用户名是否已注册暴露")
public class VerfityNameLogic implements CreateWalletContract.Model, VerifyUserNameProviderServices {
    @Override
    public Flowable<Object> verfityName(VerfityNameReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .verfityName(reqParam)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<Object> requestCheckUserName(VerfityNameReqParam reqParam) {
        return verfityName(reqParam);
    }

    @Override
    public void init(Context context) {

    }
}
