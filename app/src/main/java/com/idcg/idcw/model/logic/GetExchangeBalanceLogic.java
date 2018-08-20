package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.GetExchangeBalanceProviderServices;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * 获取币币闪兑余额，该余额为扣除了矿工费之类的费用后剩下可用余额
 * @author yiyang
 */
@Route(path = ProviderPath.path_GetExchangeBalanceProviderServices)
public class GetExchangeBalanceLogic implements GetExchangeBalanceProviderServices {
    @Override
    public Flowable<NewBalanceBean> requestNewBalanceProvider(String currency) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestExchangeBalance(currency)
                .compose(new DefaultTransformer<NewBalanceBean>());
    }

    @Override
    public void init(Context context) {

    }
}
