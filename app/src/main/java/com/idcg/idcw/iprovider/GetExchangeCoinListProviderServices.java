package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.bean.CoinExchangeBean;

import io.reactivex.Flowable;

/**
 *
 * @author yiyang
 */

public interface GetExchangeCoinListProviderServices extends IProvider {
    Flowable<CoinExchangeBean> requestCoinList() ;
}
