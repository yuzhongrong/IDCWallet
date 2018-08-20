package com.idcw.pay.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import io.reactivex.Flowable;

public interface GetAccountAddressServices extends IProvider {
    // 获取币种对应的个人的地址
    Flowable<String> getAccountAddress(String currency);
}
