package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.LoginReqParam;
import com.idcg.idcw.presenter.interfaces.LoginWalletContract;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.presenter.impl ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/15 14:48
 **/

public class LoginWalletPresenterImpl extends LoginWalletContract.LoginWalletPresenter {
    @Override
    public void requestPhoneLogin(LoginReqParam reqParam) {
        addSubscribe(mModel.requestPhoneLogin(reqParam).subscribeWith(new RxSubscriber<LoginStatus>() {
            @Override
            public void onSuccess(LoginStatus loginStatus) {
                mView.updateRequestPhoneLogin(loginStatus);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
