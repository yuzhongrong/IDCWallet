package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.LoginReqParam;
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
 * 修改时间：2018/3/15 14:42
 **/

public interface LoginWalletContract {
    interface View extends BaseView {
        void updateRequestPhoneLogin(LoginStatus loginStatus);
    }
    interface Model extends BaseModel {
        Flowable<LoginStatus> requestPhoneLogin(LoginReqParam reqParam);
    }
    abstract class LoginWalletPresenter extends BasePresenter<View,Model> {
        public abstract void requestPhoneLogin(LoginReqParam reqParam);
    }
}
