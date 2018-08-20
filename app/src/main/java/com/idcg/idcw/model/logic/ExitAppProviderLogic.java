package com.idcg.idcw.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.api.HttpApi;
import com.idcg.idcw.app.WalletApplication;
import com.idcg.idcw.iprovider.ExitAppProviderServices;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import io.reactivex.Flowable;

import static com.cjwsc.idcm.constant.ProviderPath.path_ExitAppProviderServices;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.logic
 * 备注消息：
 * 修改时间：2018/3/16 17:13
 **/
@Route(path = path_ExitAppProviderServices, name = "登出服务接口")
public class ExitAppProviderLogic implements ExitAppProviderServices {
    @Override
    public Flowable<String> requestLoginOutProvider() {
        return HttpUtils.getInstance(WalletApplication.getContext())
                .getRetrofitClient()
                .builder(HttpApi.class)
                .requestLoginOut()
                .compose(new DefaultTransformer<String>());
    }
    @Override
    public void init(Context context) {

    }
}
