package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetQuoteOrdersServices;
import foxidcw.android.idcw.otc.model.beans.OTCOrderQuotePriceBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetQuoteOrdersServices;

@Route(path = path_OTCGetQuoteOrdersServices, name = "获取承兑商报价单")
public class OTCGetQuoteOrdersLogic implements OTCGetQuoteOrdersServices {
    @Override
    public Flowable<List<OTCOrderQuotePriceBean>> getQuoteOrders(String orderId) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .getQuoteOrders(orderId)
                .compose(new DefaultTransformer<List<OTCOrderQuotePriceBean>>());
    }

    @Override
    public Flowable<Object> reqCancelQuoteOrder(int orderId) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestCancelQuoteorder(orderId)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public void init(Context context) {

    }




}
