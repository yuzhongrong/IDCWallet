package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.params.LoginReqParam;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/17.
 */

public interface LoginWalletProviderServices extends IProvider {

    public Flowable<LoginStatus> reqLogin(LoginReqParam reqParam);
}
