package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.CheckUserReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/15.
 */

public interface FindSureMoneyContract {

    interface View extends BaseView {
        void updateReqCheckUser(LoginStatus loginStatus);
    }
    interface Model extends BaseModel {
        Flowable<LoginStatus> ReqCheckUser(List<CheckUserReqParam> reqParam);
    }
    abstract class FindSureMoneyPreseter extends BasePresenter<View,Model> {
        public abstract void ReqCheckUser(List<CheckUserReqParam> reqParam);
    }

}
