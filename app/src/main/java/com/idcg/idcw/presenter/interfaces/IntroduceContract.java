package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.VersionListBean;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public interface IntroduceContract {

    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<VersionListBean> RequestVersionList(String lang, String pageIndex);

    }
    //-------------------------V---------------------
    interface View extends BaseView {
        void updateRequestVersionList(VersionListBean result);

    }
    //---------------------P--------------------------
    abstract class Preseter extends BasePresenter<View,Model> {
        public abstract void RequestVersionList(String lang, String pageIndex);

    }
}
