package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.LocalCurrencyReqParam;
import com.idcg.idcw.presenter.interfaces.LocalCurrencyContract;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/16 17:31
 **/

public class LocalCurrencyPresenterImpl extends LocalCurrencyContract.Presenter {
    @Override
    public void requestModifyCurrency(LocalCurrencyReqParam reqParam) {
        addSubscribe(mModel.requestModifyCurrency(reqParam).subscribeWith(new RxProgressSubscriber<String>(mView) {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(String result) {
                mView.updateRequestModifyCurrency(result);
            }
        }));
    }
}
