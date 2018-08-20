package com.idcw.pay.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcw.pay.model.param.PayValidAddressReqParam;

import io.reactivex.Flowable;

public interface SecurityPayValidAddressServices extends IProvider {
    // 校验地址
    Flowable<Boolean> validAddress(PayValidAddressReqParam reqParam);

    // 校验切割地址
    Flowable<String> validComplicatedAddress(PayValidAddressReqParam reqParam);
}
