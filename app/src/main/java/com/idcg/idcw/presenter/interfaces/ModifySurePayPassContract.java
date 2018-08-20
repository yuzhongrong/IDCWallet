package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.ModifyPassReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/15.
 */

public interface ModifySurePayPassContract {

    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<String> ReqModifyPass(ModifyPassReqParam reqParam);
    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateReqModifyPass(String result);
    }

    //---------------------P--------------------------
    abstract class ReqModifyPreseter extends BasePresenter<View,Model> {
        public abstract void ReqModifyPass(ModifyPassReqParam reqParam);
    }

}
