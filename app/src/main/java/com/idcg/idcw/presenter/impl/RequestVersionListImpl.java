package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.VersionListBean;
import com.idcg.idcw.presenter.interfaces.IntroduceContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class RequestVersionListImpl extends IntroduceContract.Preseter {
    @Override
    public void RequestVersionList(String lang, String pageIndex) {
        addSubscribe(mModel.RequestVersionList(lang, pageIndex).subscribeWith(new RxSubscriber<VersionListBean>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(VersionListBean versionListBean) {
                mView.updateRequestVersionList(versionListBean);
            }
        }));
    }
}
