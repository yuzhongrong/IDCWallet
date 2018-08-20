package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 */

public interface RequestConfirmProviderServices extends IProvider{
    public Flowable<Object> getConfirmStatus(String id);

}
