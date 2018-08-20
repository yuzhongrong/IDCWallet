package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.ReFeeBean;
import com.idcg.idcw.model.bean.ReqSyncAddressParam;
import com.idcg.idcw.model.params.CheckAddressReqParam;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.params.ReFeeReqParam;
import com.idcg.idcw.model.params.SendFormReqAndResParam;
import com.idcg.idcw.model.params.SendTradeReqParam;
import com.idcg.idcw.model.bean.SendTradeBean;
import com.idcg.idcw.model.bean.WalletAssetBean;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.presenter.interfaces ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 10:18
 **/

public interface SendCurrencyContract {
    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<SendFormReqAndResParam> requestSendFrom(SendFormReqAndResParam reqParam);

        Flowable<NewBalanceBean> requestNewBalance(String currency);

        Flowable<List<WalletAssetBean>> getWalletList();

        Flowable<Boolean> requestCheckBtnAddress(CheckAddressReqParam reqParam);

        Flowable<SendTradeBean> requestSendTrade(SendTradeReqParam reqParam);

        Flowable<ReFeeBean> requestReFree(ReFeeReqParam reqParam);
        Flowable<ReFeeBean> requestNewReFree(ReFeeReqParam reqParam);
        Flowable<String> RequestSyncAddress(ReqSyncAddressParam reqParam);

    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateRequestSendFrom(SendFormReqAndResParam param);

        void updateRequestNewBalance(NewBalanceBean param);

        void updateGetWalletList(List<WalletAssetBean> params);

        void updateRequestCheckBtnAddress(Boolean isOk);

        void updateRequestSendTrade(SendTradeBean param);

        void updateRequestReFree(ReFeeBean param);

        void updateRequestNewReFree(ReFeeBean param);
    }

    //---------------------P--------------------------
    abstract class SendCurrencyPresenter extends BasePresenter<View, Model> {
        public abstract void requestSendFrom(SendFormReqAndResParam reqParam);

        public abstract void requestNewBalance(String currency);

        public abstract void getWalletList();

        public abstract void requestCheckBtnAddress(CheckAddressReqParam reqParam);

        public abstract void requestSendTrade(SendTradeReqParam reqParam);

        public abstract void requestReFree(ReFeeReqParam reqParam);

        public abstract void requestNewReFree(ReFeeReqParam reqParam);
    }
}
