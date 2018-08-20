package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.TxParmInfoBean;
import com.idcg.idcw.model.params.RequestCommonParam;
import com.idcg.idcw.presenter.interfaces.TradeDetailContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class RequestCommonImpl extends TradeDetailContract.Preseter {
    @Override
    public void RequestCommon(RequestCommonParam reqParam) {
        addSubscribe(mModel.RequestCommon(reqParam).subscribeWith(new RxSubscriber<Object>() {
            @Override
            protected void onError(ResponseThrowable ex) {

                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(Object o) {
                mView.updateRequestCommon(o);

            }
        }));
    }

    @Override
    public void RequestTxIdData(RequestCommonParam reqParam) {
        addSubscribe(mModel.RequestTxIdData(reqParam).subscribeWith(new RxSubscriber<TxParmInfoBean>() {
            @Override
            protected void onError(ResponseThrowable ex) {

                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(TxParmInfoBean o) {
                mView.updateRequestTxIdData(o);

            }
        }));
    }
}
