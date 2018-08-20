package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.ExchangeDataBean;
import com.idcg.idcw.model.params.FlashRecordListReqParam;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.base.BaseView;

import java.util.List;

import io.reactivex.Flowable;


public interface FlashRecordFragmentContract {
    interface View extends BaseView {
        void updateExchangeDataList(List<ExchangeDataBean> result);
    }

    interface Model extends BaseModel {
        Flowable<List<ExchangeDataBean>> requestExchangeDataList(FlashRecordListReqParam reqParam);
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void getExchangeDataList(FlashRecordListReqParam reqParam);
    }
}
