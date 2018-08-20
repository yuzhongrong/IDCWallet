package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.AddDataBean;
import com.idcg.idcw.model.params.AddCurrencyIsShow;
import com.idcg.idcw.presenter.interfaces.AddCurrencyContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/16 16:57
 **/

public class AddCurrencyPresenterImpl extends AddCurrencyContract.Presenter {
    @Override
    public void requestAddData() {
        addSubscribe(mModel.requestAddData().subscribeWith(new RxSubscriber<List<AddDataBean>>() {
            @Override
            public void onSuccess(List<AddDataBean> NoticeTopResParam) {
                mView.updateRequestAddData(NoticeTopResParam);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestAddCurrency(List<AddCurrencyIsShow> addCurrencyIsShows) {
        addSubscribe(mModel.requestAddCurrency(addCurrencyIsShows).subscribeWith(new RxSubscriber<Object>() {
            @Override
            public void onSuccess(Object NoticeTopResParam) {
                mView.updateRequestAddCurrency(NoticeTopResParam);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestAddAllData() {
        addSubscribe(mModel.requestAddAllData().subscribeWith(new RxSubscriber<List<AddDataBean>>() {
            @Override
            public void onSuccess(List<AddDataBean> NoticeTopResParam) {
                mView.updateRequestAddData(NoticeTopResParam);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
