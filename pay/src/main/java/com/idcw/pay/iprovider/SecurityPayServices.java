package com.idcw.pay.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcw.pay.model.bean.SecurityPayBean;
import com.idcw.pay.model.param.SecurityPayReqParam;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.iprovider
 * 备注消息：
 * 修改时间：2018/3/26 17:59
 **/
public interface SecurityPayServices extends IProvider {
    Flowable<SecurityPayBean> securityPay(SecurityPayReqParam reqParam);
}
