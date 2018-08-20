package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.CheckNewPinBean;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.model.params.ReqPayPassParam;
import com.idcg.idcw.presenter.interfaces.EditPayPassContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class EditPayPassPresenterImpl extends EditPayPassContract.EditPayPassPreseter {
    @Override
    public void EditPayPass(ReqPayPassParam reqParam) {
        addSubscribe(mModel.EditPayPass(reqParam).subscribeWith(new RxSubscriber<Object>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(Object o) {
                mView.updateEditPayPass(o);
            }
        }));
    }

    @Override
    public void requestCheckOriginalPwd(CheckOriginalPwdReqParam reqParam) {
        addSubscribe(mModel.requestCheckOriginalPwd(reqParam).subscribeWith(new RxSubscriber<CheckNewPinBean>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(CheckNewPinBean checkNewPinBean) {
                mView.updateRequestCheckOriginalPwd(checkNewPinBean);
            }
        }));
    }
}
