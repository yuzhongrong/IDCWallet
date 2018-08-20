package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.GetExchangeDetailProviderServices;
import com.idcg.idcw.model.bean.ExchangeDetailBean;
import com.idcg.idcw.model.params.EditCommentReqParam;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author yiyang
 */
@Route(path = ProviderPath.path_GetExchangeDetailProviderServices,name = "币币闪兑接口暴露")
public class GetExchangeDetailProviderServicesLogic implements GetExchangeDetailProviderServices {

    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<ExchangeDetailBean> GetExchangeDetail(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestExchangeDetail(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(params)))
                .compose(new DefaultTransformer<ExchangeDetailBean>());
    }

    @Override
    public Flowable<Boolean> editExchangeComment(EditCommentReqParam param) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestEditComment(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(param)))
                .compose(new DefaultTransformer<Boolean>());
    }
}
