package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCCustomerOptionsServices;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCConfirmPayServices;


/**
 * Created by yuzhongrong on 2018/5/11.
 */

@Route(path = path_OTCConfirmPayServices)
public class OTCCustomerOptionLogic implements OTCCustomerOptionsServices{
    @Override
    public Flowable<Object> requestConfirmPayService(String orderId) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestConfirmPay(orderId)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<Object> requestDelayActionService(String orderId) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestDelayTrade(orderId)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<Object> requestConfirmMoneyActionService(String orderId) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(90);

        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestConfirmMoneyTrade(orderId)
                .compose(new DefaultTransformer<Object>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public Flowable<Object> requestApplyAppealActionService(String orderId) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestApplyappealTrade(orderId)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<Object> requestAppeal2UploadCerfiticate(String orderId) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestAppeal2UploadCerfiticate(orderId)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public void init(Context context) {

    }
}
