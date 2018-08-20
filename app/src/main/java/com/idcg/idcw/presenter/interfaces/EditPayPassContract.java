package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.CheckNewPinBean;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.model.params.ReqPayPassParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public interface EditPayPassContract {

    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<Object> EditPayPass(ReqPayPassParam reqParam);
        Flowable<CheckNewPinBean> requestCheckOriginalPwd(CheckOriginalPwdReqParam reqParam);
        Flowable<Boolean> requestOldCheckOriginalPwd(CheckOriginalPwdReqParam reqParam);
    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateEditPayPass(Object result);
        void updateRequestCheckOriginalPwd(CheckNewPinBean checkNewPinBean);
        void updateRequestCheckOriginalPwd(Boolean isOk);
    }

    //---------------------P--------------------------
    abstract class EditPayPassPreseter extends BasePresenter<View,Model> {
        public abstract void EditPayPass(ReqPayPassParam reqParam);
        public abstract void requestCheckOriginalPwd(CheckOriginalPwdReqParam reqParam);

    }

}
