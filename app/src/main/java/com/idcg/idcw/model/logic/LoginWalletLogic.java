package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.LoginWalletProviderServices;
import com.idcg.idcw.model.params.LoginReqParam;
import com.idcg.idcw.presenter.interfaces.LoginWalletContract;
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
 * 修改时间：2018/3/15 14:45
 **/

@Route(path = ProviderPath.path_LoginWalletProviderServices,name = "登录接口暴露邮箱登录or手机登录")
public class LoginWalletLogic implements LoginWalletContract.Model,LoginWalletProviderServices {
    @Override
    public Flowable<LoginStatus> requestPhoneLogin(LoginReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestPhoneLogin(reqParam)
                .compose(new DefaultTransformer<LoginStatus>());
    }

    @Override
    public Flowable<LoginStatus> reqLogin(LoginReqParam reqParam) {
        return requestPhoneLogin(reqParam);
    }

    @Override
    public void init(Context context) {

    }
}
