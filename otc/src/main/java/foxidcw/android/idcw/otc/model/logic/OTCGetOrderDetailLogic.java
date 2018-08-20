package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetOrderDetailServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCOrderDetailServices;


/**
 * Created by yuzhongrong on 2018/5/11.
 * 获取
 */

@Route(path = path_OTCOrderDetailServices)
public class OTCGetOrderDetailLogic implements OTCGetOrderDetailServices{

    @Override
    public void init(Context context) {

    }

    @Override
    public Flowable<OTCGetOtcOrdersBean> requestOrderDetailService(String orderId) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestOrderDetailrDetail(orderId)
                .compose(new DefaultTransformer<OTCGetOtcOrdersBean>());
    }
}
