package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.bean.CoinExchangeRateBean;
import com.idcg.idcw.model.bean.CoinPairBean;
import com.idcg.idcw.model.params.BIBIHelpReqParam;
import com.idcg.idcw.model.params.CoinRateReqParam;
import com.idcg.idcw.presenter.interfaces.BiBiFlashContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author yiyang
 */
public class BiBiFlashLogic implements BiBiFlashContract.Model {

    @Override
    public Flowable<List<CoinPairBean>> requestCoinList() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestCoinList()
                .compose(new DefaultTransformer<List<CoinPairBean>>());
    }

    @Override
    public Flowable<CoinExchangeRateBean> requestCoinRate(CoinRateReqParam param) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestCoinRate(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(param)))
                .compose(new DefaultTransformer<CoinExchangeRateBean>());
    }

    @Override
    public Flowable<String> requestHelpString(BIBIHelpReqParam param) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestHelpInfo(param.getLanguage())
                .compose(new DefaultTransformer<String>());
    }
}
