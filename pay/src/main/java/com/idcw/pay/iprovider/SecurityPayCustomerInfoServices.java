package com.idcw.pay.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcw.pay.model.bean.CustomerInfoBean;

import io.reactivex.Flowable;

// 获取公司信息
public interface SecurityPayCustomerInfoServices extends IProvider {
    Flowable<CustomerInfoBean> getCustomerInfo(String appId, String langCode);
}
