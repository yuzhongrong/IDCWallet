package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.GetWalletBalanceProviderServices;
import com.idcg.idcw.model.bean.ReFeeBean;
import com.idcg.idcw.model.bean.ReqSyncAddressParam;
import com.idcg.idcw.model.bean.ShareConfigBean;
import com.idcg.idcw.model.params.CheckAddressReqParam;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.params.ReFeeReqParam;
import com.idcg.idcw.model.params.SendFormReqAndResParam;
import com.idcg.idcw.model.params.SendTradeReqParam;
import com.idcg.idcw.model.bean.SendTradeBean;
import com.idcg.idcw.iprovider.MoneyBagListProviderServices;
import com.idcg.idcw.model.bean.WalletAssetBean;
import com.idcg.idcw.presenter.interfaces.SendCurrencyContract;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_MoneyBagListProviderServices_GetWalletBalanceProviderServices;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.logic ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 11:01
 **/
@Route(path = path_MoneyBagListProviderServices_GetWalletBalanceProviderServices, name = "暴露用户获取所有资产接口")
public class SendCurrencyLogic implements SendCurrencyContract.Model, MoneyBagListProviderServices, GetWalletBalanceProviderServices {
    @Override
    public Flowable<SendFormReqAndResParam> requestSendFrom(SendFormReqAndResParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestSendFrom(reqParam)
                .compose(new DefaultTransformer<SendFormReqAndResParam>());
    }

    @Override
    public Flowable<NewBalanceBean> requestNewBalance(String currency) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestNewBalance(currency)
                .compose(new DefaultTransformer<NewBalanceBean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public Flowable<List<WalletAssetBean>> getWalletList() {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .getWalletList()
                .compose(new DefaultTransformer<List<WalletAssetBean>>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public Flowable<Boolean> requestCheckBtnAddress(CheckAddressReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestCheckBtnAddress(reqParam)
                .compose(new DefaultTransformer<Boolean>());
    }

    @Override
    public Flowable<SendTradeBean> requestSendTrade(SendTradeReqParam reqParam) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestSendTrade(reqParam)
                .compose(new DefaultTransformer<SendTradeBean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }


    @Override
    public Flowable<ReFeeBean> requestReFree(ReFeeReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestReFree(reqParam)
                .compose(new DefaultTransformer<ReFeeBean>());
    }

    @Override
    public Flowable<ReFeeBean> requestNewReFree(ReFeeReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestReFree(reqParam)
                .compose(new DefaultTransformer<ReFeeBean>());
    }

    @Override
    public Flowable<String> RequestSyncAddress(ReqSyncAddressParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .RequestSyncAddress(reqParam)
                .compose(new DefaultTransformer<String>());
    }

    @Override
    public Flowable<ShareConfigBean> requestShareConfigProvider(String lang, String client) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestShareConfig(lang,client)
                .compose(new DefaultTransformer<ShareConfigBean>());
    }


    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<List<WalletAssetBean>> getWalletListProvider() {
        return getWalletList();
    }

    @Override
    public Flowable<NewBalanceBean> requestNewBalanceProvider(String currency) {
        return requestNewBalance(currency);
    }

    @Override
    public Flowable<Boolean> requestCheckBtnAddressProvider(CheckAddressReqParam reqParam) {
        return requestCheckBtnAddress(reqParam);
    }

    @Override
    public Flowable<SendTradeBean> requestSendTradeProvider(SendTradeReqParam reqParam) {
        return requestSendTrade(reqParam);
    }

    @Override
    public Flowable<String> requestSyncAddressProvider(ReqSyncAddressParam reqParam) {
        return RequestSyncAddress(reqParam);
    }


}
