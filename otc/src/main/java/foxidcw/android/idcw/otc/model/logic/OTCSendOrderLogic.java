package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCSendOrderServices;
import foxidcw.android.idcw.otc.model.beans.OTCSendOrderDTOBean;
import foxidcw.android.idcw.otc.model.params.OTCSendOrderParams;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_SendOrderServices;

@Route(path = path_SendOrderServices, name = "下OTC订单")
public class OTCSendOrderLogic
        implements OTCSendOrderServices
{

    private Context mContext;


    @Override
    public void init(Context context) {
        mContext = context;
    }

    //买入1 卖出2
    @Override
    public Flowable<OTCSendOrderDTOBean> sendOrder(OTCSendOrderParams OTCSendOrderParams) {
        HttpUtils instance = HttpUtils.getInstance(mContext);
        HttpUtils.getInstance(mContext)
                        .updateTimeOut(86400); //超时时间1天

        return           instance.getRetrofitClient()
                        .builder(OTCHttpApi.class)
                        .sendOrder(OTCSendOrderParams)
                        .compose(new DefaultTransformer<OTCSendOrderDTOBean>());
    }
}
