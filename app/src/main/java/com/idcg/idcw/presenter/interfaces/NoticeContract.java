package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.NoticeResBean;
import com.idcg.idcw.model.params.NoticeListReqParam;
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
 * 修改时间：2018/3/18 18:11
 **/

public interface NoticeContract {
    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<NoticeResBean> getNoticeList(NoticeListReqParam reqParam);

        Flowable<Boolean> getNoticeDel(String jsonString);
    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateNoticeList(NoticeResBean result);
        void updateNoticeDel(Boolean result);
    }

    //---------------------P--------------------------
    abstract class Preseter extends BasePresenter<View, Model> {
        public abstract void getNoticeList(NoticeListReqParam reqParam,boolean isShowDialog);

        public abstract void getNoticeDel(String jsonString);
    }
}
