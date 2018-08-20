package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import java.util.List;

import foxidcw.android.idcw.otc.model.beans.OTCOrderQuotePriceBean;
import io.reactivex.Flowable;

public interface OTCGetQuoteOrdersServices extends IProvider {
    Flowable<List<OTCOrderQuotePriceBean>> getQuoteOrders(String orderId);

    Flowable<Object> reqCancelQuoteOrder(int orderId);
}
