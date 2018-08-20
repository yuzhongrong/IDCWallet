package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.SetFindPassReqParam;
import com.idcg.idcw.presenter.interfaces.SetFindPassContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * Created by admin-2 on 2018/3/15.
 */

public class SetFindPassPresenterImpl extends SetFindPassContract.SetFindPassPreseter {
    @Override
    public void SetFindPass(SetFindPassReqParam reqParam) {
        addSubscribe(mModel.SetFindPass(reqParam).subscribeWith(new RxSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.updateSetFindPass(result);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
