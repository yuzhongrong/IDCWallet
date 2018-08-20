package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.CheckUserReqParam;
import com.idcg.idcw.presenter.interfaces.FindSureMoneyContract;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import java.util.List;

/**
 * Created by admin-2 on 2018/3/15.
 */

public class FindSureMoneyImpl extends FindSureMoneyContract.FindSureMoneyPreseter {

    @Override
    public void ReqCheckUser(List<CheckUserReqParam> reqParam) {
       addSubscribe(mModel.ReqCheckUser(reqParam).subscribeWith(new RxProgressSubscriber<LoginStatus>(mView) {
           @Override
           protected void onError(ResponseThrowable ex) {
            mView.showErrorWithStatus(ex);
           }

           @Override
           public void onSuccess(LoginStatus loginStatus) {
               mView.updateReqCheckUser(loginStatus);
           }
       }));
    }
}
