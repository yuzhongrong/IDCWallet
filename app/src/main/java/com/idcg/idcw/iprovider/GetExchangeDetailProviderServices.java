package com.idcg.idcw.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.idcg.idcw.model.bean.ExchangeDetailBean;
import com.idcg.idcw.model.params.EditCommentReqParam;

import io.reactivex.Flowable;

/**
 *
 * @author yiyang
 */
public interface GetExchangeDetailProviderServices extends IProvider {
    Flowable<ExchangeDetailBean> GetExchangeDetail(String id);
    Flowable<Boolean> editExchangeComment(EditCommentReqParam param);
}
