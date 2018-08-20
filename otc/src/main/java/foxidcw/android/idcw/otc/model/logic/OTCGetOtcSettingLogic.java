package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetOtcSettingServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcSettingBean;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetOtcSetting;

@Route(path = path_OTCGetOtcSetting,
       name = "获取OTC配置信息")
public class OTCGetOtcSettingLogic
        implements OTCGetOtcSettingServices
{

    private Context mContext;


    @Override
    public void init(Context context) {
        mContext = context;
    }


    @Override
    public Flowable<OTCGetOtcSettingBean> getOtcSetting() {
        return HttpUtils.getInstance(mContext)
                        .getRetrofitClient()
                        .builder(OTCHttpApi.class)
                        .getOtcSetting()
                        .compose(new DefaultTransformer<OTCGetOtcSettingBean>());
    }
}
