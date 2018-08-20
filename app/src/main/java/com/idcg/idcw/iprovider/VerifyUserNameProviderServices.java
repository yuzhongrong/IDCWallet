package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.params.VerfityNameReqParam;

import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/4/9.
 */

public interface VerifyUserNameProviderServices extends IProvider {
    Flowable<Object> requestCheckUserName(VerfityNameReqParam reqParam);
}
