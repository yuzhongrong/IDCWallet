package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.params.ModifyPassReqParam;
import com.idcg.idcw.presenter.interfaces.ModifySurePayPassContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/15.
 */

public class ModifySurePayPassLogic implements ModifySurePayPassContract.Model {
    @Override
    public Flowable<String> ReqModifyPass(ModifyPassReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .modifyPass(reqParam)
                .compose(new DefaultTransformer<String>());
    }
}
