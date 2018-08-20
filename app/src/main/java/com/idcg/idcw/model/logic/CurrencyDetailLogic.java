package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.AccountAddressInfoProviderServices;
import com.idcg.idcw.iprovider.TransMsgProviderServices;
import com.idcg.idcw.model.params.TransMsgReqParam;
import com.idcg.idcw.model.bean.TransMsgBean;
import com.idcg.idcw.presenter.interfaces.CurrencyDetailContract;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_AccountAddressInfoProviderServices_TransMsgProviderServices;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 15:10
 **/
@Route(path = path_AccountAddressInfoProviderServices_TransMsgProviderServices, name = "暴露用户地址详情以及钱包历史数据")
public class CurrencyDetailLogic implements CurrencyDetailContract.Model,
        AccountAddressInfoProviderServices, TransMsgProviderServices {
    @Override
    public Flowable<Boolean> requestOpt(String currency, boolean isShow) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestOpt(currency, isShow)
                .compose(new DefaultTransformer<Boolean>());
    }

    @Override
    public Flowable<String> requestAccountAddress(String currency) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestAccountAddress(currency)
                .compose(new DefaultTransformer<String>());
    }

    @Override
    public Flowable<List<TransMsgBean>> requestTransMsg(TransMsgReqParam reqParam) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestTransMsg(reqParam)
                .compose(new DefaultTransformer<List<TransMsgBean>>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public Flowable<String> requestAccountAddressProvider(String currency) {
        return requestAccountAddress(currency);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<List<TransMsgBean>> requestTransMsgProvider(TransMsgReqParam reqParam) {
        return requestTransMsg(reqParam);
    }
}
