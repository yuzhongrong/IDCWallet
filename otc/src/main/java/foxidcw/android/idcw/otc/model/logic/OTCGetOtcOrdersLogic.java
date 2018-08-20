package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetOtcOrdersServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOrdersParams;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetOtcOrders;

@Route(path = path_OTCGetOtcOrders, name = "获取OTC订单信息")
public class OTCGetOtcOrdersLogic
        implements OTCGetOtcOrdersServices
{

    private Context mContext;


    @Override
    public void init(Context context) {
        mContext = context;
    }

    @Override
    public Flowable<List<OTCGetOtcOrdersBean>> getOtcOrders(OTCGetOtcOrdersParams otcGetOtcOrdersParams) {
        return HttpUtils.getInstance(mContext)
                        .getRetrofitClient()
                        .builder(OTCHttpApi.class)
                        .getOtcOrders(otcGetOtcOrdersParams)
                        .compose(new DefaultTransformer<List<OTCGetOtcOrdersBean>>());
    }
}
