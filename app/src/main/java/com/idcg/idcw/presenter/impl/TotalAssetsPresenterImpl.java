package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.TotalAssetsAmountBean;
import com.idcg.idcw.presenter.interfaces.TotalAssetsContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/16 17:20
 **/

public class TotalAssetsPresenterImpl extends TotalAssetsContract.Presenter {
    @Override
    public void requestHistoryAmount() {
        addSubscribe(mModel.requestHistoryAmount().subscribeWith(new RxSubscriber<List<TotalAssetsAmountBean>>() {
            @Override
            public void onSuccess(List<TotalAssetsAmountBean> resParamList) {
                mView.updateRequestHistoryAmount(resParamList);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
