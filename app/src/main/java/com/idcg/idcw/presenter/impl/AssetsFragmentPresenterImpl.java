package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.CheckStatusBean;
import com.idcg.idcw.model.bean.HistoryAmountBean;
import com.idcg.idcw.model.bean.NoticeTopBean;
import com.idcg.idcw.presenter.interfaces.AssetsFragmentContract;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/16 14:24
 **/

public class AssetsFragmentPresenterImpl extends AssetsFragmentContract.Presenter {
    @Override
    public void requestTopNotice(String lang) {
        addSubscribe(mModel.requestTopNotice(lang).subscribeWith(new RxSubscriber<NoticeTopBean>() {
            @Override
            public void onSuccess(NoticeTopBean NoticeTopResParam) {
                mView.updateRequestTopNotice(NoticeTopResParam);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestReadNotice(int msgId) {
        addSubscribe(mModel.requestReadNotice(msgId).subscribeWith(new RxSubscriber<Object>() {
            @Override
            public void onSuccess(Object phraseDataReqAndResBean) {
                mView.updateRequestReadNotice(phraseDataReqAndResBean);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestHistoryAmountNew(boolean isShowProgress) {
        addSubscribe(mModel.requestHistoryAmountNew(isShowProgress).subscribeWith(new RxProgressSubscriber<HistoryAmountBean>(mView,isShowProgress) {
            @Override
            public void onSuccess(HistoryAmountBean phraseDataReqAndResBean) {
                mView.updateRequestHistoryAmountNew(phraseDataReqAndResBean);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestCheckStatus(String device_id, boolean newVersion) {
        addSubscribe(mModel.requestCheckStatus(device_id, newVersion).subscribeWith(new RxSubscriber<CheckStatusBean>() {
            @Override
            public void onSuccess(CheckStatusBean phraseDataReqAndResBean) {
                mView.updateRequestCheckStatus(phraseDataReqAndResBean);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
