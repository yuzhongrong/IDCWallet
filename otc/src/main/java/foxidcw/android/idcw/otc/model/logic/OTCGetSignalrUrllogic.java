package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;
import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetSignalrUrlServices;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetSignalUrlServices;

/**
 * Created by hpz on 2018/5/5.
 */

@Route(path = path_OTCGetSignalUrlServices)
public class OTCGetSignalrUrllogic implements OTCGetSignalrUrlServices {

    private Context mContext;

    @Override
    public Flowable<Object> getSignalrUrl() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestDiscoverySetRead()
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public void init(Context context) {

        this.mContext=context;
    }
}
