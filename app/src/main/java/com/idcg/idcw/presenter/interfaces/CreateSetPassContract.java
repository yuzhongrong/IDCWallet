package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.CreateSetPassReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.presenter.interfaces ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/15 16:56
 **/

public interface CreateSetPassContract {
    interface View extends BaseView {
        void updateRequestRegister(LoginStatus loginStatus);
    }

    interface Model extends BaseModel {
        Flowable<LoginStatus> requestRegister(CreateSetPassReqParam reqParam);
    }

    abstract class CreateSetPassPresenter extends BasePresenter<View, Model> {
        public abstract void requestRegister(CreateSetPassReqParam reqParam);
    }
}
