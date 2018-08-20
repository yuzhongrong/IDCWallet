package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCPaymentMethodManagerPageServices;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import io.reactivex.Flowable;
import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCPaymentMethodManagerPageServices;

@Route(path = path_OTCPaymentMethodManagerPageServices,name = "支付方式管理界面列表和删除")
public class OTCPaymentMethodManagerPageLogic implements OTCPaymentMethodManagerPageServices {

    private Context mContext;

    @Override
    public Flowable<Object> paymentModeRemove(OTCRemoveBean reqBean) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .paymentModeRemove(reqBean)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<List<OTCAddOrEditReqParams>> getPaymentModeList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .getPaymentModeList()
                .compose(new DefaultTransformer<List<OTCAddOrEditReqParams>>());
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
