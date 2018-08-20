package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetOtcTradeSettingServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcTradeSettingBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_GetOtcTradeSettingServices;


@Route(path = path_GetOtcTradeSettingServices, name = "获取OTC交易相关设置")
public class OTCGetOtcTradeSettingLogic
        implements OTCGetOtcTradeSettingServices
{

    private Context mContext;


    @Override
    public void init(Context context) {
        mContext = context;
    }

    @Override
    public Flowable<OTCGetOtcTradeSettingBean> getOtcTradeSetting() {
        return HttpUtils.getInstance(mContext)
                        .getRetrofitClient()
                        .builder(OTCHttpApi.class)
                        .getOtcTradeSetting()
                        .compose(new DefaultTransformer<OTCGetOtcTradeSettingBean>());
    }
}
