package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.bean.NoticeResBean;
import com.idcg.idcw.model.params.NoticeListReqParam;
import com.idcg.idcw.presenter.interfaces.NoticeContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/18 18:17
 **/

public class NoticeLogic implements NoticeContract.Model {
    @Override
    public Flowable<NoticeResBean> getNoticeList(NoticeListReqParam reqParam) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .getNoticeList(reqParam.getLang(),reqParam.getClient(),reqParam.getPageIndex(),reqParam.getPageSize())
                .compose(new DefaultTransformer<NoticeResBean>());
    }

    @Override
    public Flowable<Boolean> getNoticeDel(String jsonString) {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .getNoticeDel(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString))
                .compose(new DefaultTransformer<Boolean>());
    }
}
