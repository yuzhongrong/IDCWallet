package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.activitys.OTCAddCurrencyActivity;
import foxidcw.android.idcw.otc.model.beans.OTCAddAmountParam;
import foxidcw.android.idcw.otc.model.beans.OTCAddCurrParam;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.params.OTCCurrentStepParams;
import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/5.
 */

public interface OTCAddBuyCurrProviderServices extends IProvider{
    Flowable<Boolean> requestAddBuyCurrProvider(OTCAddCurrParam otcAddCurrParam);
    Flowable<Boolean> requestAddAmountProvider(OTCAddAmountParam otcAddAmountParam);
    Flowable<Boolean> requestCheckPayProvider();
    Flowable<Object> requesRemoveProvider(OTCRemoveBean otcRemoveBean);//移除币种及限额
    Flowable<Object> requesRemoveLegalProvider(OTCRemoveBean otcRemoveBean);//移除法币资金量
    Flowable<Boolean> requestAddPayProvider(OTCAddCurrencyActivity.CacheBean otcRemoveBean);//添加法币及支付方式
    Flowable<Object> requesRemovePayProvider(OTCRemoveBean otcRemoveBean);//移除支付方式
    Flowable<Boolean> requestCheckWithdrawProvider();//校验能否提取保证金
    Flowable<Object> requestSetCurrentStepProvider(OTCCurrentStepParams otcCurrentStepParams);//设置步骤

}
