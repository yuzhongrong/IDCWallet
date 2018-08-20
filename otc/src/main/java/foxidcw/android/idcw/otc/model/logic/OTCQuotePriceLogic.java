package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCQuotePriceServices;
import foxidcw.android.idcw.otc.model.params.OTCQuotePriceReqParams;
import io.reactivex.Flowable;
import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCQuotePriceServices;

@Route(path = path_OTCQuotePriceServices,name = "承兑商报价接口")
public class OTCQuotePriceLogic implements OTCQuotePriceServices {
    @Override
    public Flowable<OTCQuotePriceReqParams> quotePrice(OTCQuotePriceReqParams reqParams) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .quotePrice(reqParams)
                .compose(new DefaultTransformer<OTCQuotePriceReqParams>());
    }

    @Override
    public void init(Context context) {

    }
}
