package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import foxidcw.android.idcw.otc.model.beans.OTCSendOrderDTOBean;
import foxidcw.android.idcw.otc.model.params.OTCSendOrderParams;
import io.reactivex.Flowable;
import retrofit2.http.Body;

// 下OTC单
public interface OTCSendOrderServices
        extends IProvider {
    //买入1 卖出2
    Flowable<OTCSendOrderDTOBean> sendOrder(@Body OTCSendOrderParams OTCSendOrderParams);
}
