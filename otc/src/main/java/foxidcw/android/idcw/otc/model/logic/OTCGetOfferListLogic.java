package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetOfferListServices;
import foxidcw.android.idcw.otc.model.beans.OTCNewOrderNoticeAcceptantBean;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOfficeListParam;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetOfferListServices;

@Route(path = path_OTCGetOfferListServices, name = "获取需要承兑商报价集合")
public class OTCGetOfferListLogic implements OTCGetOfferListServices {
    @Override
    public Flowable<List<OTCNewOrderNoticeAcceptantBean>> getOTCOfferList(OTCGetOtcOfficeListParam otcGetOtcOfficeListParam) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .getOTCOfferList(otcGetOtcOfficeListParam)
                .compose(new DefaultTransformer<List<OTCNewOrderNoticeAcceptantBean>>());
    }

    @Override
    public void init(Context context) {

    }
}
