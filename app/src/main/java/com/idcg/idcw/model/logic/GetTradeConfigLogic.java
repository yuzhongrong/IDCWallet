package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.cjwsc.idcm.iprovider.GetTradeConfigServices;
import com.cjwsc.idcm.net.http.HttpUtils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import static com.cjwsc.idcm.constant.ProviderPath.path_GetTradeConfigServices;

@Route(path = path_GetTradeConfigServices, name = "获取交易所推送地址")
public class GetTradeConfigLogic implements GetTradeConfigServices {
    @Override
    public Flowable<String> getTradeConfig() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .getTradeConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void init(Context context) {

    }
}
