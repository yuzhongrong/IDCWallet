package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.NotifyMessageBean;
import com.idcg.idcw.model.params.NotifyMessageReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public interface TotalAssetContract {

    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<NotifyMessageBean> RequestNotifyMessage(NotifyMessageReqParam reqParam);
    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateRequestNotifyMessage(NotifyMessageBean result);
    }

    //---------------------P--------------------------
    abstract class Preseter extends BasePresenter<View,Model> {
        public abstract void RequestNotifyMessage(NotifyMessageReqParam reqParam);
    }
}
