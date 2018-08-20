package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.bean.ExchangeResultBean;
import com.idcg.idcw.model.params.ExchangeInParam;

import io.reactivex.Flowable;

/**
 *
 * @author yiyang
 */
public interface ExchangeInProviderServices extends IProvider {
    Flowable<ExchangeResultBean> exchangeIn(ExchangeInParam param);
}
