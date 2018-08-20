package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.params.AddCurrencyIsShow;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.iprovider
 * 备注消息：
 * 修改时间：2018/3/16 17:02
 **/

public interface GetAddAllCurrencyProviderServices extends IProvider{
    Flowable<Object> requestAddCurrencyProvider(List<AddCurrencyIsShow> addCurrencyIsShows);
}
