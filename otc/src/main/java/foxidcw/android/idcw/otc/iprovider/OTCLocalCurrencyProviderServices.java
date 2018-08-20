package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;
import java.util.List;

import foxidcw.android.idcw.otc.model.beans.OTCLocalCurrencyResBean;
import io.reactivex.Flowable;

public interface OTCLocalCurrencyProviderServices extends IProvider {
    // 获取法币币种列表
    Flowable<List<OTCLocalCurrencyResBean>> getOTCLocalCurrencyList();
}
