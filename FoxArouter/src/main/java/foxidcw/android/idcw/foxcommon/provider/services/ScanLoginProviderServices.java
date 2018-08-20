package foxidcw.android.idcw.foxcommon.provider.services;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.foxcommon.provider.bean.ScanLoginBean;
import foxidcw.android.idcw.foxcommon.provider.params.ScanLoginReqParam;
import io.reactivex.Flowable;

/**
 * Created by admin-2 on 2018/3/31.
 */


public interface ScanLoginProviderServices extends IProvider {

    Flowable<ScanLoginBean> requestScanLoginProvider(ScanLoginReqParam currency);
}
