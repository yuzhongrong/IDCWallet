package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.RequestCommonReqParam;
import com.idcg.idcw.presenter.interfaces.UserBackContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/17 16:34
 **/

public class UserBackPresenterImpl extends UserBackContract.Preseter {
    @Override
    public void requestCommonToServer(RequestCommonReqParam reqParam) {
        addSubscribe(mModel.requestCommonToServer(reqParam).subscribeWith(new RxSubscriber<Object>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(Object o) {
                mView.updateRequestCommonToServer(o);
            }
        }));
    }
}
