package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCDiscoveryBean;
import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/23.
 */

public interface OTCDiscoveryListProviderServices extends IProvider {
    Flowable<OTCDiscoveryBean> requestDiscoveryListProvider(String lang);
    Flowable<Boolean> requestDiscoveryIfReadProvider(String dappId);
    Flowable<Boolean> requestDiscoverySetReadProvider(String dappId);

}
