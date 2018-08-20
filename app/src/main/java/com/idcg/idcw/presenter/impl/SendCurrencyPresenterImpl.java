package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.ReFeeBean;
import com.idcg.idcw.model.params.CheckAddressReqParam;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.params.ReFeeReqParam;
import com.idcg.idcw.model.params.SendFormReqAndResParam;
import com.idcg.idcw.model.params.SendTradeReqParam;
import com.idcg.idcw.model.bean.SendTradeBean;
import com.idcg.idcw.model.bean.WalletAssetBean;
import com.idcg.idcw.presenter.interfaces.SendCurrencyContract;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.presenter.impl ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 11:02
 **/

public class SendCurrencyPresenterImpl extends SendCurrencyContract.SendCurrencyPresenter {
    @Override
    public void requestSendFrom(SendFormReqAndResParam reqParam) {
        addSubscribe(mModel.requestSendFrom(reqParam).subscribeWith(new RxSubscriber<SendFormReqAndResParam>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(SendFormReqAndResParam param) {
                mView.updateRequestSendFrom(param);
            }
        }));
    }

    @Override
    public void requestNewBalance(String currency) {
        addSubscribe(mModel.requestNewBalance(currency).subscribeWith(new RxProgressSubscriber<NewBalanceBean>(mView) {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(NewBalanceBean param) {
                mView.updateRequestNewBalance(param);
            }
        }));
    }

    @Override
    public void getWalletList() {
        addSubscribe(mModel.getWalletList().subscribeWith(new RxSubscriber<List<WalletAssetBean>>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(List<WalletAssetBean> params) {
                mView.updateGetWalletList(params);
            }
        }));
    }

    @Override
    public void requestCheckBtnAddress(CheckAddressReqParam reqParam) {
        addSubscribe(mModel.requestCheckBtnAddress(reqParam).subscribeWith(new RxSubscriber<Boolean>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(Boolean param) {
                mView.updateRequestCheckBtnAddress(param);
            }
        }));
    }

    @Override
    public void requestSendTrade(SendTradeReqParam reqParam) {
        addSubscribe(mModel.requestSendTrade(reqParam).subscribeWith(new RxSubscriber<SendTradeBean>() {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(SendTradeBean param) {
                mView.updateRequestSendTrade(param);
            }
        }));
    }

    @Override
    public void requestReFree(ReFeeReqParam reqParam) {
        addSubscribe(mModel.requestReFree(reqParam).subscribeWith(new RxProgressSubscriber<ReFeeBean>(mView) {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(ReFeeBean param) {
                mView.updateRequestReFree(param);
            }
        }));
    }

    @Override
    public void requestNewReFree(ReFeeReqParam reqParam) {
        addSubscribe(mModel.requestNewReFree(reqParam).subscribeWith(new RxProgressSubscriber<ReFeeBean>(mView) {
            @Override
            protected void onError(ResponseThrowable ex) {
                mView.showErrorWithStatus(ex);
            }

            @Override
            public void onSuccess(ReFeeBean param) {
                mView.updateRequestNewReFree(param);
            }
        }));
    }
}
