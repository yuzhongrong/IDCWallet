package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.ModifyPassReqParam;
import com.idcg.idcw.presenter.interfaces.ModifySurePayPassContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * Created by admin-2 on 2018/3/15.
 */

public class ModifySurePayPassImpl extends ModifySurePayPassContract.ReqModifyPreseter {
    @Override
    public void ReqModifyPass(ModifyPassReqParam reqParam) {
        addSubscribe(mModel.ReqModifyPass(reqParam).subscribeWith(new RxSubscriber<String>() {
            @Override
            public void onSuccess(String result) {
                mView.updateReqModifyPass(result);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
