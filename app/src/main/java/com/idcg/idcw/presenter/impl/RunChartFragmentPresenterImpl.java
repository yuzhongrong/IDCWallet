package com.idcg.idcw.presenter.impl;

import com.idcg.idcw.model.bean.RunChartDataBean;
import com.idcg.idcw.model.params.SettingConfigReqParam;
import com.idcg.idcw.presenter.interfaces.RunChartFragmentContract;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.presenter.impl
 * 备注消息：
 * 修改时间：2018/3/16 18:12
 **/

public class RunChartFragmentPresenterImpl extends RunChartFragmentContract.Preseter {
    @Override
    public void requestRunChartData() {
        addSubscribe(mModel.requestRunChartData().subscribeWith(new RxSubscriber<RunChartDataBean>() {
            @Override
            public void onSuccess(RunChartDataBean NoticeTopResParam) {
                mView.updateRequestRunChartData(NoticeTopResParam);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                ex.setState(1);
                mView.showErrorWithStatus(ex);
            }
        }));
    }

    @Override
    public void requestSettingConfig(SettingConfigReqParam reqParam) {
        addSubscribe(mModel.requestSettingConfig(reqParam).subscribeWith(new RxSubscriber<Object>() {
            @Override
            public void onSuccess(Object NoticeTopResParam) {
                mView.updateRequestSettingConfig(NoticeTopResParam);
            }

            @Override
            protected void onError(ResponseThrowable ex) {
                ex.state = 2;
                mView.showErrorWithStatus(ex);
            }
        }));
    }
}
