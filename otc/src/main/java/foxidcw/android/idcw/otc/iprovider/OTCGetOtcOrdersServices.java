package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.List;

import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOrdersParams;
import io.reactivex.Flowable;
import retrofit2.http.Body;

// 获取OTC订单信息
public interface OTCGetOtcOrdersServices
        extends IProvider {
    Flowable<List<OTCGetOtcOrdersBean>> getOtcOrders(@Body OTCGetOtcOrdersParams otcGetOtcOrdersParams);
}
