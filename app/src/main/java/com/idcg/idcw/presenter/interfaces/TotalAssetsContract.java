package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.TotalAssetsAmountBean;
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
 * 修改时间：2018/3/16 17:08
 **/

public interface TotalAssetsContract {
    interface View extends BaseView {
        void updateRequestHistoryAmount(List<TotalAssetsAmountBean> params);
    }

    interface Model extends BaseModel {
        Flowable<List<TotalAssetsAmountBean>> requestHistoryAmount();
    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void requestHistoryAmount();
    }
}
