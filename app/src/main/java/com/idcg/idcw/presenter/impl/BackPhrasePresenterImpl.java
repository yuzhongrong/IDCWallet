package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.idcg.idcw.presenter.interfaces.BackPhraseContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.presenter.impl ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 9:54
 **/

public class BackPhrasePresenterImpl extends BackPhraseContract.BackPhrasePresenter {
    @Override
    public void requestPhraseData(String lang) {
        addSubscribe(mModel.requestPhraseData(lang).subscribeWith(new RxSubscriber<PhraseDataReqAndResParam>() {
            @Override
            public void onSuccess(PhraseDataReqAndResParam phraseDataReqAndResBean) {
                mView.updateRequestPhraseData(phraseDataReqAndResBean);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestSavePhrase(PhraseDataReqAndResParam reqAndResBean) {
        addSubscribe(mModel.requestSavePhrase(reqAndResBean).subscribeWith(new RxSubscriber<Object>() {
            @Override
            public void onSuccess(Object object) {
                mView.updateRequestSavePhrase(object);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
