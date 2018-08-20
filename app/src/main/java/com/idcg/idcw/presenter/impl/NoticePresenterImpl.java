package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.NoticeResBean;
import com.idcg.idcw.model.params.NoticeListReqParam;
import com.idcg.idcw.presenter.interfaces.NoticeContract;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/18 18:17
 **/

public class NoticePresenterImpl extends NoticeContract.Preseter {
    @Override
    public void getNoticeList(NoticeListReqParam reqParam, boolean isShowDialog) {
        addSubscribe(mModel.getNoticeList(reqParam).subscribeWith(new RxProgressSubscriber<NoticeResBean>(mView, isShowDialog) {
            @Override
            public void onSuccess(NoticeResBean result) {
                mView.updateNoticeList(result);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void getNoticeDel(String jsonString) {
        addSubscribe(mModel.getNoticeDel(jsonString).subscribeWith(new RxProgressSubscriber<Boolean>(mView) {
            @Override
            public void onSuccess(Boolean result) {
                mView.updateNoticeDel(result);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
