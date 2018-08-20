package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.params.TransMsgReqParam;
import com.idcg.idcw.model.bean.TransMsgBean;
import com.idcg.idcw.presenter.interfaces.CurrencyDetailContract;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/16 15:11
 **/

public class CurrencyDetailPresenterImpl extends CurrencyDetailContract.Presenter {
    @Override
    public void requestOpt(String currency,boolean isShow) {
        addSubscribe(mModel.requestOpt(currency,isShow).subscribeWith(new RxProgressSubscriber<Boolean>(mView,isShow) {
            @Override
            public void onSuccess(Boolean result) {
                mView.updateRequestOpt(result);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestAccountAddress(String currency) {
        addSubscribe(mModel.requestAccountAddress(currency).subscribeWith(new RxProgressSubscriber<String>(mView) {
            @Override
            public void onSuccess(String result) {
                mView.updateRequestAccountAddress(result);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestTransMsg(TransMsgReqParam reqParam) {
        addSubscribe(mModel.requestTransMsg(reqParam).subscribeWith(new RxProgressSubscriber<List<TransMsgBean>>(mView) {
            @Override
            public void onSuccess(List<TransMsgBean> result) {
                mView.updateRequestTransMsg(result);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
