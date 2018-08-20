package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.VerifyPhraseReqParam;
import com.idcg.idcw.presenter.interfaces.VerifyPhraseContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/16 14:51
 **/
public class VerifyPhrasePresenterImpl extends VerifyPhraseContract.Preseter {
    @Override
    public void requestVerifyPhrase(VerifyPhraseReqParam reqParam) {
        addSubscribe(mModel.requestVerifyPhrase(reqParam).subscribeWith(new RxSubscriber<Object>() {
            @Override
            public void onSuccess(Object object) {
                mView.updateRequestVerifyPhrase(object);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
