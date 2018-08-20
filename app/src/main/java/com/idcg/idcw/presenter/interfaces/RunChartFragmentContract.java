package com.idcg.idcw.presenter.interfaces;

import com.idcg.idcw.model.bean.RunChartDataBean;
import com.idcg.idcw.model.params.SettingConfigReqParam;
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
 * 修改时间：2018/3/16 18:07
 **/

public interface RunChartFragmentContract {
    //------------------------M---------------------
    interface Model extends BaseModel {
        Flowable<RunChartDataBean> requestRunChartData();
        Flowable<Object> requestSettingConfig(SettingConfigReqParam reqParam);
    }

    //-------------------------V---------------------
    interface View extends BaseView {
        void updateRequestRunChartData(RunChartDataBean param);
        void updateRequestSettingConfig(Object object);
    }

    //---------------------P--------------------------
    abstract class Preseter extends BasePresenter<View,Model> {
        public abstract void requestRunChartData();
        public abstract void requestSettingConfig(SettingConfigReqParam reqParam);
    }
}
