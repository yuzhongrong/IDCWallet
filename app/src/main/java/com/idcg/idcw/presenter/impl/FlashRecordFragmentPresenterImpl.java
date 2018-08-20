package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.ExchangeDataBean;
import com.idcg.idcw.model.params.FlashRecordListReqParam;
import com.idcg.idcw.presenter.interfaces.FlashRecordFragmentContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import java.util.List;

/**
 *
 * @author yiyang
 */
public class FlashRecordFragmentPresenterImpl extends FlashRecordFragmentContract.Presenter {
    @Override
    public void getExchangeDataList(FlashRecordListReqParam reqParam) {
        addSubscribe(mModel.requestExchangeDataList(reqParam).subscribeWith(new RxSubscriber<List<ExchangeDataBean>>() {
            @Override
            public void onSuccess(List<ExchangeDataBean> result) {
                mView.updateExchangeDataList(result);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
