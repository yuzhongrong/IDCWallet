package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.params.SendPhoneCodeReqParam;
import com.idcg.idcw.model.params.SendEmailCodeReqParam;
import com.idcg.idcw.model.params.VerifyCodeReqParam;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.iprovider
 * 备注消息：
 * 修改时间：2018/3/16 11:54
 **/

public interface VerifyCodeAndEMailProviderServices extends IProvider {
    Flowable<Object> requestPhoneCode(SendPhoneCodeReqParam reqParam);

    Flowable<Object> requestEmailCode(SendEmailCodeReqParam reqParam);

    Flowable<Boolean> requestVerifyCode(VerifyCodeReqParam reqParam);
}
