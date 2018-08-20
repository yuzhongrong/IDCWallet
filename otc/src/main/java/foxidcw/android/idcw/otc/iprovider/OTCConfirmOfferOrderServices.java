package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCConfirmOrderResBean;
import foxidcw.android.idcw.otc.model.params.OTCOrderIdQuoteIdReqParams;
import io.reactivex.Flowable;

public interface OTCConfirmOfferOrderServices extends IProvider {
    Flowable<OTCConfirmOrderResBean> confirmOfferOrder(OTCOrderIdQuoteIdReqParams reqParams);
}
