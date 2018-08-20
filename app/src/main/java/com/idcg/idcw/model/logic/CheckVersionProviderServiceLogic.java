package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.common.iprovider.CheckVersionProviderServices;
import foxidcw.android.idcw.common.model.bean.CheckAppVersionBean;
import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

@Route(path = ProviderPath.path_CheckVersionProviderServiceLogic,name = "检测版本更新接口暴露")
public class CheckVersionProviderServiceLogic implements CheckVersionProviderServices {
    @Override
    public Flowable<CheckAppVersionBean> CheckVersionProvider() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .checkAppVersion()
                .compose(new DefaultTransformer<CheckAppVersionBean>());
    }

    @Override
    public void init(Context context) {

    }
}
