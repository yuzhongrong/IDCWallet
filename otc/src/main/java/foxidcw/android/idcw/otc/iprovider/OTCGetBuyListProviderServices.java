package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeBuyBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeSellBean;
import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/5.
 */


public interface OTCGetBuyListProviderServices extends IProvider {
    Flowable<OTCGetExchangeBuyBean> requestGetBuyListProvider();//
    Flowable<OTCGetExchangeSellBean> requestGetSellListProvider();
}
