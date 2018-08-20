package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.VerifyPhraseReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.interfaces
 * 备注消息：
 * 修改时间：2018/3/16 14:48
 **/

public interface VerifyPhraseContract {
    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<Object> requestVerifyPhrase(VerifyPhraseReqParam reqParam);
    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateRequestVerifyPhrase(Object result);
    }

    //---------------------P--------------------------
    abstract class Preseter extends BasePresenter<View,Model> {
        public abstract void requestVerifyPhrase(VerifyPhraseReqParam reqParam);
    }
}
