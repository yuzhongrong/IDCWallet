package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCCancelOrderServices;
import foxidcw.android.idcw.otc.model.beans.OTCConfirmOrderResBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCCancelOrderServices;

@Route(path = path_OTCCancelOrderServices, name = "撤销订单接口")
public class OTCCancelOrderLogic implements OTCCancelOrderServices {

    @Override
    public Flowable<OTCConfirmOrderResBean> cancelOrder(String orderId) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .cancelOrder(orderId)
                .compose(new DefaultTransformer<OTCConfirmOrderResBean>());
    }

    @Override
    public Flowable<Object> agreeRefund(String orderId) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(75);
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .agreeRefund(orderId)
                .compose(new DefaultTransformer<Object>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public void init(Context context) {

    }
}
