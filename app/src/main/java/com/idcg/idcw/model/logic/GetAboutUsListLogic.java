package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.GetAboutUsListProviderServices;
import com.idcg.idcw.model.bean.AboutUsBean;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/30.
 */

@Route(path = ProviderPath.path_AboutUsServices, name = "关于我们列表接口")
public class GetAboutUsListLogic implements GetAboutUsListProviderServices {
    @Override
    public Flowable<List<AboutUsBean>> requestAboutUsListProvider(String lang) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestAboutUsList(lang)
                .compose(new DefaultTransformer<List<AboutUsBean>>());
    }

    @Override
    public void init(Context context) {

    }
}
