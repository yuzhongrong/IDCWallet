package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.VerfityNameReqParam;
import com.idcg.idcw.presenter.interfaces.CreateWalletContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class VerfityNameImpl extends CreateWalletContract.Preseter {
    @Override
    public void verfityName(VerfityNameReqParam reqParam) {
        addSubscribe(mModel.verfityName(reqParam).subscribeWith(new RxSubscriber<Object>() {
            @Override
            protected void onError(ResponseThrowable ex) {
              mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(Object o) {
                mView.updateVerfityName(o);
            }
        }));
    }
}
