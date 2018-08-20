package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.params.TransMsgReqParam;
import com.idcg.idcw.model.bean.TransMsgBean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.iprovider
 * 备注消息：
 * 修改时间：2018/3/16 15:31
 **/

public interface TransMsgProviderServices extends IProvider {
    Flowable<List<TransMsgBean>> requestTransMsgProvider(TransMsgReqParam reqParam);
}
