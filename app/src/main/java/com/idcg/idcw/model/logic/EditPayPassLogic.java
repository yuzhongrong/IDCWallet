package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.CheckOriginalPwdProviderServices;
import com.idcg.idcw.iprovider.EditPayPassProviderServices;
import com.idcg.idcw.model.bean.CheckNewPinBean;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.model.params.ReqPayPassParam;
import com.idcg.idcw.presenter.interfaces.EditPayPassContract;
import com.cjwsc.idcm.constant.ProviderPath;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */
@Route(path = ProviderPath.path_EditPayPassProviderServices_CheckOriginalPwdProviderServices,name = "edit_pay_pass接口暴露")
public class EditPayPassLogic implements EditPayPassContract.Model,
        EditPayPassProviderServices,
        CheckOriginalPwdProviderServices {
    @Override
    public Flowable<Object> EditPayPass(ReqPayPassParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .reqPayPass(reqParam)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<CheckNewPinBean> requestCheckOriginalPwd(CheckOriginalPwdReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestCheckOriginalPwd(reqParam)
                .compose(new DefaultTransformer<CheckNewPinBean>());
    }

    //requestOldCheckOriginalPwd
    @Override
    public Flowable<Boolean> requestOldCheckOriginalPwd(CheckOriginalPwdReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestOldCheckOriginalPwd(reqParam)
                .compose(new DefaultTransformer<Boolean>());
    }

    @Override
    public Flowable<Object> EditPayPassProvider(ReqPayPassParam param) {
        return EditPayPass(param);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<CheckNewPinBean> requestCheckOriginalPwdProvider(CheckOriginalPwdReqParam reqParam) {
        return requestCheckOriginalPwd(reqParam);
    }

    @Override
    public Flowable<Boolean> requestOldCheckOriginalPwdProvider(CheckOriginalPwdReqParam reqParam) {
        return requestOldCheckOriginalPwd(reqParam);
    }

    //requestOldCheckOriginalPwd
}
