package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetBuyListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeBuyBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeSellBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetBuyListProviderServices;


/**
 * Created by hpz on 2018/5/5.
 */

@Route(path = path_OTCGetBuyListProviderServices)
public class OTCGetBuyListLogic implements OTCGetBuyListProviderServices {

    private Context mContext;

    @Override
    public Flowable<OTCGetExchangeBuyBean> requestGetBuyListProvider() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestPhraseData()
                .compose(new DefaultTransformer<OTCGetExchangeBuyBean>());
    }

    //requestSellCurrData
    @Override
    public Flowable<OTCGetExchangeSellBean> requestGetSellListProvider() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestSellCurrData()
                .compose(new DefaultTransformer<OTCGetExchangeSellBean>());
    }

    @Override
    public void init(Context context) {
        mContext=context;
    }
}
