package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCNewBalanceBean;
import io.reactivex.Flowable;

/**
 *
 * @author yiyang
 */
public interface OTCGetExchangeBalanceProviderServices
        extends IProvider{
    Flowable<OTCNewBalanceBean> requestNewBalanceProvider(String currency);
}
