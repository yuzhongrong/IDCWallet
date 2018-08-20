package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.GetExchangeCoinListProviderServices;
import com.idcg.idcw.model.bean.CoinExchangeBean;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_GetCoinListProviderServices;

/**
 *
 * @author yiyang
 */
@Route(path = path_GetCoinListProviderServices)
public class GetCoinListLogic implements GetExchangeCoinListProviderServices {
    @Override
    public Flowable<CoinExchangeBean> requestCoinList() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestNewCoinList()
                .compose(new DefaultTransformer<CoinExchangeBean>());
    }

    @Override
    public void init(Context context) {

    }
}
