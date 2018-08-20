package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.params.ReqPayPassParam;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/16.
 *  this provider for app moudel ,if other model need this provider  please move this  to FoxMan
 */

public interface EditPayPassProviderServices extends IProvider {

    Flowable<Object> EditPayPassProvider(ReqPayPassParam param);


}
