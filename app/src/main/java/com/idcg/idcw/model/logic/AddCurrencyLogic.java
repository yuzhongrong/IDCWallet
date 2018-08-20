package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.GetAddAllCurrencyProviderServices;
import com.idcg.idcw.model.bean.AddDataBean;
import com.idcg.idcw.model.params.AddCurrencyIsShow;
import com.idcg.idcw.presenter.interfaces.AddCurrencyContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.cjwsc.idcm.constant.ProviderPath.path_GetAddAllCurrencyProviderServices;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 16:56
 **/
@Route(path = path_GetAddAllCurrencyProviderServices, name = "暴露添加币种数据")
public class AddCurrencyLogic implements AddCurrencyContract.Model, GetAddAllCurrencyProviderServices {
    @Override
    public Flowable<List<AddDataBean>> requestAddData() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestAddData()
                .compose(new DefaultTransformer<List<AddDataBean>>());
    }

    @Override
    public Flowable<Object> requestAddCurrency(List<AddCurrencyIsShow> addCurrencyIsShows) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestAddCurrency(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JSON.toJSONString(addCurrencyIsShows)))
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<List<AddDataBean>> requestAddAllData() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestAllCurrency()
                .compose(new DefaultTransformer<List<AddDataBean>>());
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<Object> requestAddCurrencyProvider(List<AddCurrencyIsShow> addCurrencyIsShows) {
        return requestAddCurrency(addCurrencyIsShows);
    }
}
