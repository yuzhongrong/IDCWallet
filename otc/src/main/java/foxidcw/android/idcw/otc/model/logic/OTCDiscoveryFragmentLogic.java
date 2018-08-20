package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCDiscoveryListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCDiscoveryBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCDiscoveryListServices;

/**
 * Created by hpz on 2018/5/23.
 */

@Route(path = path_OTCDiscoveryListServices)
public class OTCDiscoveryFragmentLogic implements OTCDiscoveryListProviderServices{

    private Context mContext;

    @Override
    public Flowable<OTCDiscoveryBean> requestDiscoveryListProvider(String lang) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestDiscoveryList(lang, "1")
                .compose(new DefaultTransformer<OTCDiscoveryBean>());
    }

    @Override
    public Flowable<Boolean> requestDiscoveryIfReadProvider(String dappId) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestDiscoveryIfRead(dappId)
                .compose(new DefaultTransformer<Boolean>());
    }


    @Override
    public Flowable<Boolean> requestDiscoverySetReadProvider(String dappId) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestDiscoverySetRead(dappId)
                .compose(new DefaultTransformer<Boolean>());
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
