package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.bean.NewBalanceBean;

import io.reactivex.Flowable;

/**
 *
 * @author yiyang
 */
public interface GetExchangeBalanceProviderServices extends IProvider{
    Flowable<NewBalanceBean> requestNewBalanceProvider(String currency);
}
