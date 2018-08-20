package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetExchangeCoinListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCCoinCurrExchangeBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_GetApplyCoinListProviderServices;

/**
 * Created by hpz on 2018/5/2.
 */

@Route(path = path_GetApplyCoinListProviderServices)
public class OTCGetCoinCurrListLogic implements OTCGetExchangeCoinListProviderServices {
    private Context mContext;
    @Override
    public Flowable<OTCCoinCurrExchangeBean> requestCoinList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestNewCoinList()
                .compose(new DefaultTransformer<OTCCoinCurrExchangeBean>());
    }

    @Override
    public void init(Context context) {
        mContext=context;
    }
}
