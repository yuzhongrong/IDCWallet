package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.CoinExchangeRateBean;
import com.idcg.idcw.model.bean.CoinPairBean;
import com.idcg.idcw.model.params.BIBIHelpReqParam;
import com.idcg.idcw.model.params.CoinRateReqParam;
import com.idcg.idcw.presenter.interfaces.BiBiFlashContract;
import com.cjwsc.idcm.base.BaseActivity;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

/**
 *
 * @author yiyang
 */
public class BiBiFlashPresenterImpl extends BiBiFlashContract.Presenter {
    @Override
    public void requestCoinList(boolean isShowDialog) {
        mModel.requestCoinList()
                .compose(((BaseActivity)mView).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<List<CoinPairBean>>(mView, isShowDialog) {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(List<CoinPairBean> coinBeans) {
                mView.updateCoinList(coinBeans);
            }
        });
    }

    @Override
    public void requestCoinRate(CoinRateReqParam param, boolean isBackground) {
        mModel.requestCoinRate(param)
                .compose(((BaseActivity)mView).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<CoinExchangeRateBean>(mView, !isBackground) {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(CoinExchangeRateBean coinExchangeRateBean) {
                mView.updateRate(coinExchangeRateBean);
            }
        });
    }

    @Override
    public void requestHelpString(BIBIHelpReqParam param) {
        mModel.requestHelpString(param)
                .compose(((BaseActivity)mView).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<String>(mView) {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(String coinExchangeRateBean) {
                mView.updateHelpString(coinExchangeRateBean);
            }
        });
    }
}
