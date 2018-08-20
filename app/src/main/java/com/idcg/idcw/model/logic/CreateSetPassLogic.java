package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.ReqRegisterIproviderServices;
import com.idcg.idcw.model.params.CreateSetPassReqParam;
import com.idcg.idcw.presenter.interfaces.CreateSetPassContract;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.logic ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/15 17:00
 **/
@Route(path = ProviderPath.path_CreateSetPassProviderServices,name = "注册接口暴露")
public class CreateSetPassLogic implements CreateSetPassContract.Model,ReqRegisterIproviderServices {
    @Override
    public Flowable<LoginStatus> requestRegister(CreateSetPassReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestRegister(reqParam)
                .compose(new DefaultTransformer<LoginStatus>());
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<LoginStatus> requestRegisterProvider(CreateSetPassReqParam reqParam) {
        return requestRegister(reqParam);
    }
}
