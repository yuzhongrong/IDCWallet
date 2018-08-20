package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.bean.UserBean;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public interface GetCoinProviderServices extends IProvider {

    public Flowable<Object> getCoinProvider(UserBean bean);
}
