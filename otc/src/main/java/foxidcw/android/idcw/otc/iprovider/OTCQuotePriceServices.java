package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.params.OTCQuotePriceReqParams;
import io.reactivex.Flowable;

public interface OTCQuotePriceServices extends IProvider {
    Flowable<OTCQuotePriceReqParams> quotePrice( OTCQuotePriceReqParams reqParams);
}
