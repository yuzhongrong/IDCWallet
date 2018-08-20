package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.AddDataBean;
import com.idcg.idcw.model.params.AddCurrencyIsShow;
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
 * 修改时间：2018/3/16 16:50
 **/

public interface AddCurrencyContract {
    interface View extends BaseView {
        void updateRequestAddData(List<AddDataBean> params);

        void updateRequestAddCurrency(Object object);
    }

    interface Model extends BaseModel {
        Flowable<List<AddDataBean>> requestAddData();
        Flowable<List<AddDataBean>> requestAddAllData();
        Flowable<Object> requestAddCurrency(List<AddCurrencyIsShow> addCurrencyIsShows);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestAddData();
        public abstract void requestAddAllData();
        public abstract void requestAddCurrency(List<AddCurrencyIsShow> addCurrencyIsShows);
    }
}
