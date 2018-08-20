package com.idcg.idcw.model.logic;

import com.alibaba.fastjson.JSON;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.idcg.idcw.presenter.interfaces.BackPhraseContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.logic ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 9:53
 **/

public class BackPhraseLogic implements BackPhraseContract.Model {
    @Override
    public Flowable<PhraseDataReqAndResParam> requestPhraseData(String lang) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestPhraseData(lang)
                .compose(new DefaultTransformer<PhraseDataReqAndResParam>());
    }

    @Override
    public Flowable<Object> requestSavePhrase(PhraseDataReqAndResParam reqAndResBean) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestSavePhrase(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JSON.toJSONString(reqAndResBean)
                ))
                .compose(new DefaultTransformer<Object>());
    }
}
