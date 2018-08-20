package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCConfirmOfferOrderServices;
import foxidcw.android.idcw.otc.model.beans.OTCConfirmOrderResBean;
import foxidcw.android.idcw.otc.model.params.OTCOrderIdQuoteIdReqParams;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCConfirmOfferOrderServices;

@Route(path = path_OTCConfirmOfferOrderServices, name = "用户确认交易")
public class OTCConfirmOfferOrderLogic implements OTCConfirmOfferOrderServices {
    @Override
    public Flowable<OTCConfirmOrderResBean> confirmOfferOrder(OTCOrderIdQuoteIdReqParams reqParams) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(120);
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .confirmOfferOrder(reqParams)
                .compose(new DefaultTransformer<OTCConfirmOrderResBean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public void init(Context context) {

    }
}
