package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/5.
 */

public interface OTCCustomerOptionsServices extends IProvider{
    Flowable<Object> requestConfirmPayService(String orderId);//买家已转账

    Flowable<Object> requestDelayActionService(String orderId);//卖家点击延时

    Flowable<Object> requestConfirmMoneyActionService(String orderId);//卖家点击延时

    Flowable<Object> requestApplyAppealActionService(String orderId);//申请申诉


    Flowable<Object> requestAppeal2UploadCerfiticate(String orderId);//等待上传凭证



}
