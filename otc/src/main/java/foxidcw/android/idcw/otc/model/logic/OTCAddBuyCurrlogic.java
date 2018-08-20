package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import foxidcw.android.idcw.otc.activitys.OTCAddCurrencyActivity;
import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCAddAmountParam;
import foxidcw.android.idcw.otc.model.beans.OTCAddCurrParam;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.params.OTCCurrentStepParams;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCAddBuyCurrProviderServices;

/**
 * Created by hpz on 2018/5/5.
 */

@Route(path = path_OTCAddBuyCurrProviderServices)
public class OTCAddBuyCurrlogic implements OTCAddBuyCurrProviderServices {
    private Context mContext;
    @Override
    public Flowable<Boolean> requestAddBuyCurrProvider(OTCAddCurrParam reqParam) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestAddCurrList(reqParam)
                .compose(new DefaultTransformer<Boolean>());
    }

    //requestAddAmountList
    @Override
    public Flowable<Boolean> requestAddAmountProvider(OTCAddAmountParam reqParam) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestAddAmountList(reqParam)
                .compose(new DefaultTransformer<Boolean>());
    }

    //requestCheckPay

    @Override
    public Flowable<Boolean> requestCheckPayProvider() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestCheckPay()
                .compose(new DefaultTransformer<Boolean>());
    }

    //requestRemoveCurr

    @Override
    public Flowable<Object> requesRemoveProvider(OTCRemoveBean otcRemoveBean) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestRemoveCurr(otcRemoveBean)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public Flowable<Object> requesRemoveLegalProvider(OTCRemoveBean otcRemoveBean) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestRemoveLegal(otcRemoveBean)
                .compose(new DefaultTransformer<Object>());
    }

    //requestAddPayList
    @Override
    public Flowable<Boolean> requestAddPayProvider(OTCAddCurrencyActivity.CacheBean reqParam) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestAddPayList(reqParam)
                .compose(new DefaultTransformer<Boolean>());
    }

    //
    @Override
    public Flowable<Object> requesRemovePayProvider(OTCRemoveBean otcRemoveBean) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestRemovePay(otcRemoveBean)
                .compose(new DefaultTransformer<Object>());
    }

    //requestCheckWithdraw
    @Override
    public Flowable<Boolean> requestCheckWithdrawProvider() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestCheckWithdraw()
                .compose(new DefaultTransformer<Boolean>());
    }

    //requestSetCurrentStep
    @Override
    public Flowable<Object> requestSetCurrentStepProvider(OTCCurrentStepParams reqParam) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestSetCurrentStep(reqParam)
                .compose(new DefaultTransformer<Object>());
    }

    @Override
    public void init(Context context) {
        mContext=context;
    }

}
