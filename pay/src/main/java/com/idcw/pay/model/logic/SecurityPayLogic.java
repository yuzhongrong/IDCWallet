package com.idcw.pay.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcw.pay.api.CommonApi;
import com.idcw.pay.iprovider.SecurityPayServices;
import com.idcw.pay.model.bean.SecurityPayBean;

import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import com.idcw.pay.model.param.SecurityPayReqParam;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_SecurityPayServices;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/26 18:11
 **/
@Route(path = path_SecurityPayServices, name = "支付暴露接口")
public class SecurityPayLogic implements SecurityPayServices {

    @Override
    public Flowable<SecurityPayBean> securityPay(SecurityPayReqParam reqParam) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(CommonApi.class)
                .securityPay(reqParam)
                .compose(new DefaultTransformer<SecurityPayBean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public void init(Context context) {

    }
}
