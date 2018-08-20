package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.params.TransMsgReqParam;
import com.idcg.idcw.model.bean.TransMsgBean;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.interfaces
 * 备注消息：
 * 修改时间：2018/3/16 14:57
 **/

public interface CurrencyDetailContract {
    interface View extends BaseView {
        void updateRequestOpt(Boolean isOk);

        void updateRequestAccountAddress(String param);

        void updateRequestTransMsg(List<TransMsgBean> params);
    }

    interface Model extends BaseModel {
        Flowable<Boolean> requestOpt(String currency,boolean isShow);

        Flowable<String> requestAccountAddress(String currency);

        Flowable<List<TransMsgBean>> requestTransMsg(TransMsgReqParam reqParam);
    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void requestOpt(String currency,boolean isShow);

        public abstract void requestAccountAddress(String currency);

        public abstract void requestTransMsg(TransMsgReqParam reqParam);
    }
}
