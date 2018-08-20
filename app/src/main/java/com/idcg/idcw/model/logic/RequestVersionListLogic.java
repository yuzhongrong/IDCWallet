package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.bean.VersionListBean;
import com.idcg.idcw.presenter.interfaces.IntroduceContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class RequestVersionListLogic implements IntroduceContract.Model {
    @Override
    public Flowable<VersionListBean> RequestVersionList(String lang, String pageIndex) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .RequestVersionList(lang, "0", pageIndex)
                .compose(new DefaultTransformer<VersionListBean>());
    }
}
