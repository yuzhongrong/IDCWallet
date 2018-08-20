package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCLocalCurrencyProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCLocalCurrencyResBean;
import io.reactivex.Flowable;
import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCLocalCurrencyProviderServices;


@Route(path = path_OTCLocalCurrencyProviderServices)
public class OTCLocalCurrencyLogic implements OTCLocalCurrencyProviderServices {

    private Context mContext;

    @Override
    public Flowable<List<OTCLocalCurrencyResBean>> getOTCLocalCurrencyList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .getOTCLocalCurrencyList()
                .compose(new DefaultTransformer<List<OTCLocalCurrencyResBean>>());
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
