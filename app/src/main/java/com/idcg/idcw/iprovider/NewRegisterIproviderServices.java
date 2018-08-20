package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.params.CreateSetPassReqParam;

import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/4/1.
 */

public interface NewRegisterIproviderServices extends IProvider {
    public Flowable requestNewRegisterProvider(CreateSetPassReqParam reqParam);
}
