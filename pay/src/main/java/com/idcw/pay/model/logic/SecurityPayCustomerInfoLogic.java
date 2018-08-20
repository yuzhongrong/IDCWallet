package com.idcw.pay.model.logic;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.idcw.pay.api.CommonApi;
import com.idcw.pay.iprovider.SecurityPayCustomerInfoServices;
import com.idcw.pay.model.bean.CustomerInfoBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_SecurityPayCustomerInfoServices;

@Route(path = path_SecurityPayCustomerInfoServices, name = "支付暴露接口")
public class SecurityPayCustomerInfoLogic implements SecurityPayCustomerInfoServices {
    @Override
    public Flowable<CustomerInfoBean> getCustomerInfo(String appId, String langCode) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        Map<String, String> params = new HashMap<>();
        // 如果是空 默认为瑞士会
        params.put("appid", TextUtils.isEmpty(appId) ? "idc3830728052018" : appId);
        params.put("lang", langCode);
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(CommonApi.class)
                .getCustomerInfo(params)
                .compose(new DefaultTransformer<CustomerInfoBean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public void init(Context context) {

    }
}
