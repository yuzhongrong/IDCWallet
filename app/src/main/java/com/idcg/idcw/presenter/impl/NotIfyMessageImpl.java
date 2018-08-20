package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.NotifyMessageBean;
import com.idcg.idcw.model.params.NotifyMessageReqParam;
import com.idcg.idcw.presenter.interfaces.TotalAssetContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class NotIfyMessageImpl extends TotalAssetContract.Preseter {
    @Override
    public void RequestNotifyMessage(NotifyMessageReqParam reqParam) {
        addSubscribe(mModel.RequestNotifyMessage(reqParam).subscribeWith(new RxSubscriber<NotifyMessageBean>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(NotifyMessageBean notifyMessageBean) {
                mView.updateRequestNotifyMessage(notifyMessageBean);
            }
        }));
    }
}
