package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetPaymentModeManagementServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_GetPaymentModeManagementServices;

@Route(path = path_GetPaymentModeManagementServices)
public class OTCGetPaymentModeManagementLogic implements OTCGetPaymentModeManagementServices {

    private Context mContext;

    @Override
    public Flowable<OTCGetPaymentModeManagementResBean> getPaymentModeManagement() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .getPaymentModeManagement()
                .compose(new DefaultTransformer<OTCGetPaymentModeManagementResBean>());
    }

    @Override
    public Flowable<Object> paymentModeChange(OTCAddOrEditReqParams reqParams) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .paymentModeChange(reqParams)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
