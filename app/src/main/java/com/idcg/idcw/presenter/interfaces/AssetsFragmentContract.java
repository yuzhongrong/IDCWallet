package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.CheckStatusBean;
import com.idcg.idcw.model.bean.HistoryAmountBean;
import com.idcg.idcw.model.bean.NoticeTopBean;
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
 * 修改时间：2018/3/16 14:08
 **/

public interface AssetsFragmentContract {
    interface View extends BaseView {
        void updateRequestTopNotice(NoticeTopBean param);

        void updateRequestReadNotice(Object object);

        void updateRequestHistoryAmountNew(HistoryAmountBean param);

        void updateRequestCheckStatus(CheckStatusBean param);
    }

    interface Model extends BaseModel {
        Flowable<NoticeTopBean> requestTopNotice(String lang);

        Flowable<Object> requestReadNotice(int msgId);

        Flowable<HistoryAmountBean> requestHistoryAmountNew(boolean isShow);

        Flowable<CheckStatusBean> requestCheckStatus(String device_id, boolean newVersion);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestTopNotice(String lang);

        public abstract void requestReadNotice(int msgId);

        public abstract void requestHistoryAmountNew(boolean isShow);

        public abstract void requestCheckStatus(String device_id, boolean newVersion);
    }
}
