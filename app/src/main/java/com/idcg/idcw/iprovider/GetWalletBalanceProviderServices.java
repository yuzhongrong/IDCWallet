package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.bean.ReqSyncAddressParam;
import com.idcg.idcw.model.bean.SendTradeBean;
import com.idcg.idcw.model.bean.ShareConfigBean;
import com.idcg.idcw.model.params.CheckAddressReqParam;
import com.idcg.idcw.model.params.SendTradeReqParam;

import io.reactivex.Flowable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.iprovider
 * 备注消息：
 * 修改时间：2018/3/16 16:44
 **/

public interface GetWalletBalanceProviderServices extends IProvider {
    Flowable<NewBalanceBean> requestNewBalanceProvider(String currency);

    Flowable<Boolean> requestCheckBtnAddressProvider(CheckAddressReqParam reqParam);
    Flowable<SendTradeBean> requestSendTradeProvider(SendTradeReqParam reqParam);
    Flowable<String> requestSyncAddressProvider(ReqSyncAddressParam reqParam);

    Flowable<ShareConfigBean> requestShareConfigProvider(String lang, String client);



}
