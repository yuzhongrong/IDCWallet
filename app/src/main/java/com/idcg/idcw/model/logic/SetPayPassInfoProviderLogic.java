package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.SetPayPassInfoProviderServices;
import com.idcg.idcw.model.params.SetPayPassInfoReqParam;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 17:44
 **/
@Route(path = ProviderPath.path_SetPayPassInfoProviderServices, name = "重置pin码接口暴露")
public class SetPayPassInfoProviderLogic implements SetPayPassInfoProviderServices {
    @Override
    public Flowable<String> requestSetPassProvider(SetPayPassInfoReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestSetPass(reqParam)
                .compose(new DefaultTransformer<String>());
    }

    @Override
    public void init(Context context) {

    }
}
