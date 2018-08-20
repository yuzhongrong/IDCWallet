package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.List;

import foxidcw.android.idcw.otc.model.beans.OTCNewOrderNoticeAcceptantBean;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOfficeListParam;
import io.reactivex.Flowable;

public interface OTCGetOfferListServices extends IProvider {
    Flowable<List<OTCNewOrderNoticeAcceptantBean>> getOTCOfferList(OTCGetOtcOfficeListParam otcGetOtcOfficeListParam);
}
