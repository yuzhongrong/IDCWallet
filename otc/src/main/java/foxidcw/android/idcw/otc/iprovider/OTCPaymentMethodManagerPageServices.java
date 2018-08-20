package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.List;

import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import io.reactivex.Flowable;

public interface OTCPaymentMethodManagerPageServices extends IProvider {

    // 删除支付方式
    Flowable<Object> paymentModeRemove(OTCRemoveBean reqBean);

    // 获取支付方式列表
    Flowable<List<OTCAddOrEditReqParams>> getPaymentModeList();
}
