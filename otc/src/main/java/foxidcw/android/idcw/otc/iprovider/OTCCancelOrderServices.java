package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCConfirmOrderResBean;
import io.reactivex.Flowable;

public interface OTCCancelOrderServices extends IProvider {
    Flowable<OTCConfirmOrderResBean> cancelOrder(String orderId);
    Flowable<Object> agreeRefund(String orderId);
}
