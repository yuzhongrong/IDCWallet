package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.LocalCurrencyReqParam;
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
 * 修改时间：2018/3/16 17:27
 **/

public interface LocalCurrencyContract {
    interface View extends BaseView {
        void updateRequestModifyCurrency(String result);
    }

    interface Model extends BaseModel {
        Flowable<String> requestModifyCurrency(LocalCurrencyReqParam reqParam);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestModifyCurrency(LocalCurrencyReqParam reqParam);
    }
}
