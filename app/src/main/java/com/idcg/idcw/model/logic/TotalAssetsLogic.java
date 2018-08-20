package com.idcg.idcw.model.logic;

import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.model.bean.TotalAssetsAmountBean;
import com.idcg.idcw.presenter.interfaces.TotalAssetsContract;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 17:19
 **/

public class TotalAssetsLogic implements TotalAssetsContract.Model {
    @Override
    public Flowable<List<TotalAssetsAmountBean>> requestHistoryAmount() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestHistoryAmount()
                .compose(new DefaultTransformer<List<TotalAssetsAmountBean>>());
    }
}
