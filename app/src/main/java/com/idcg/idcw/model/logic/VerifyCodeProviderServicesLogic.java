package com.idcg.idcw.model.logic;

import android.content.Context;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.VerifyCodeAndEMailProviderServices;
import com.idcg.idcw.model.params.SendEmailCodeReqParam;
import com.idcg.idcw.model.params.SendPhoneCodeReqParam;
import com.idcg.idcw.model.params.VerifyCodeReqParam;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;
import static com.cjwsc.idcm.constant.ProviderPath.path_VerifyCodeAndEMailProviderServices;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 11:58
 **/
@Route(path = path_VerifyCodeAndEMailProviderServices,name = "暴露验证码校验接口")
public class VerifyCodeProviderServicesLogic implements VerifyCodeAndEMailProviderServices {
    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<Object> requestPhoneCode(SendPhoneCodeReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestPhoneCode(reqParam)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<Object> requestEmailCode(SendEmailCodeReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestEmailCode(reqParam)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<Boolean> requestVerifyCode(VerifyCodeReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestVerifyCode(reqParam)
                .compose(new DefaultTransformer<Boolean>());
    }
}
