package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.SetFindPassReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/15.
 */

public interface SetFindPassContract {
    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<Object> SetFindPass(SetFindPassReqParam reqParam);
    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateSetFindPass(Object result);
    }

    //---------------------P--------------------------
    abstract class SetFindPassPreseter extends BasePresenter<View,Model> {
        public abstract void SetFindPass(SetFindPassReqParam reqParam);
    }

}
