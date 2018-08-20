package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetExchangeBalanceProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCNewBalanceBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetExchangeBalanceProviderServices;

/**
 * 获取币币闪兑余额，该余额为扣除了矿工费之类的费用后剩下可用余额
 * @author yiyang
 */
@Route(path = path_OTCGetExchangeBalanceProviderServices)
public class OTCGetExchangeBalanceLogic
        implements OTCGetExchangeBalanceProviderServices
{
    @Override
    public Flowable<OTCNewBalanceBean> requestNewBalanceProvider(String currency) {
        return HttpUtils.getInstance(BaseApplication.getContext())
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestExchangeBalance(currency)
                .compose(new DefaultTransformer<OTCNewBalanceBean>());
    }

    @Override
    public void init(Context context) {

    }
}
