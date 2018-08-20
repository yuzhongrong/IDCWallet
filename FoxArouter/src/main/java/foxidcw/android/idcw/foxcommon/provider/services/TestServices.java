package foxidcw.android.idcw.foxcommon.provider.services;

import com.alibaba.android.arouter.facade.template.IProvider;

import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/23.
 */

public interface TestServices extends IProvider {
    Flowable<String> requestAccountAddressProvider(String currency);


}
