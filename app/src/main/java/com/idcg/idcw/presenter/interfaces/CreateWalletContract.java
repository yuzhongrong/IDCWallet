package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.VerfityNameReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public interface CreateWalletContract {
    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<Object> verfityName(VerfityNameReqParam reqParam);
    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateVerfityName(Object result);
    }

    //---------------------P--------------------------
    abstract class Preseter extends BasePresenter<View,Model> {
        public abstract void verfityName(VerfityNameReqParam reqParam);
    }

}
