package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.params.CreateSetPassReqParam;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/17.
 */

public interface ReqRegisterIproviderServices extends IProvider {
   public Flowable<LoginStatus> requestRegisterProvider(CreateSetPassReqParam reqParam);
}
