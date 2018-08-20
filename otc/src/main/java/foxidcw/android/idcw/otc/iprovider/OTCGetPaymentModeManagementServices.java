package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import io.reactivex.Flowable;
import retrofit2.http.Body;

public interface OTCGetPaymentModeManagementServices extends IProvider {
    // 获取联动属性
    Flowable<OTCGetPaymentModeManagementResBean> getPaymentModeManagement();

    // 添加或者修改支付方式
    Flowable<Object> paymentModeChange(@Body OTCAddOrEditReqParams reqParams);
}
