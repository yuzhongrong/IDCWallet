package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.SettingStateProviderServices;
import com.idcg.idcw.model.bean.CheckStatusBean;
import com.idcg.idcw.model.bean.HistoryAmountBean;
import com.idcg.idcw.model.bean.NoticeTopBean;
import com.idcg.idcw.presenter.interfaces.AssetsFragmentContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_SettingStateProviderServices;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 14:24
 **/
@Route(path = path_SettingStateProviderServices, name = "暴露检测设置state的")
public class AssetsFragmentLogic implements AssetsFragmentContract.Model, SettingStateProviderServices {
    @Override
    public Flowable<NoticeTopBean> requestTopNotice(String lang) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestTopNotice(lang, "android")
                .compose(new DefaultTransformer<NoticeTopBean>());
    }

    @Override
    public Flowable<Object> requestReadNotice(int msgId) {
//        NoticeReadReqParam reqParam = new NoticeReadReqParam();
//        reqParam.setMsgId(String.valueOf(msgId));
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestReadNotice(msgId)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<HistoryAmountBean> requestHistoryAmountNew(boolean isShow) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestHistoryAmountNew()
                .compose(new DefaultTransformer<HistoryAmountBean>());
    }

    @Override
    public Flowable<CheckStatusBean> requestCheckStatus(String device_id, boolean newVersion) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestCheckStatus(device_id, newVersion)
                .compose(new DefaultTransformer<CheckStatusBean>());
    }

    @Override
    public Flowable<CheckStatusBean> requestCheckStatusProvider(String device_id, boolean newVersion) {
        return requestCheckStatus(device_id, newVersion);
    }

    @Override
    public void init(Context context) {

    }
}
