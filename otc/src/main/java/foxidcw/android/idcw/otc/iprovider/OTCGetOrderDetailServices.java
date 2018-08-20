package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/5.
 */

public interface OTCGetOrderDetailServices extends IProvider{
    Flowable<OTCGetOtcOrdersBean> requestOrderDetailService(String orderId);

}
