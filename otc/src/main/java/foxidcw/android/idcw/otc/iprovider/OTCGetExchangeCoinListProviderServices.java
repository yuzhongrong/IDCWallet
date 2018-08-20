package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCCoinCurrExchangeBean;
import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/2.
 */

public interface OTCGetExchangeCoinListProviderServices extends IProvider {//path_GetApplyCoinListProviderServices
    Flowable<OTCCoinCurrExchangeBean> requestCoinList() ;
}
