package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.TxParmInfoBean;
import com.idcg.idcw.model.params.RequestCommonParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public interface TradeDetailContract {
    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<Object> RequestCommon(RequestCommonParam reqParam);
        Flowable<TxParmInfoBean> RequestTxIdData(RequestCommonParam reqParam);
    }
    //-------------------------V---------------------
    interface View extends BaseView {
        void updateRequestCommon(Object result);
        void updateRequestTxIdData(TxParmInfoBean result);
    }
    //---------------------P--------------------------
    abstract class Preseter extends BasePresenter<View,Model> {
        public abstract void RequestCommon(RequestCommonParam reqParam);
        public abstract void RequestTxIdData(RequestCommonParam reqParam);
    }

}
