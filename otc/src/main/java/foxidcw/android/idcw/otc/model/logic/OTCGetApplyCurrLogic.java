package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCCurrentStepBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceListBean;
import foxidcw.android.idcw.otc.model.beans.OTCRechargeDepositParam;
import foxidcw.android.idcw.otc.model.beans.OTCRechargeResultBean;
import foxidcw.android.idcw.otc.model.beans.OTCWithdrawResultBean;
import foxidcw.android.idcw.otc.model.beans.OTCWalletAssetBean;
import foxidcw.android.idcw.otc.model.params.OTCDepositSortParams;
import foxidcw.android.idcw.otc.model.params.OTCReqSyncAddressParams;
import foxidcw.android.idcw.otc.model.params.OTCWithdrawDepositParam;
import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_GetApplyCoinCurrListProviderServices;

/**
 * Created by hpz on 2018/5/2.
 */

@Route(path = path_GetApplyCoinCurrListProviderServices)
public class OTCGetApplyCurrLogic implements OTCMoneyBagListProviderServices {//

    private Context mContext;

    @Override
    public Flowable<List<OTCWalletAssetBean>> getWalletListProvider() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .getWalletList()
                .compose(new DefaultTransformer<List<OTCWalletAssetBean>>());
    }

    //requestGetDepositBalanceList

    @Override
    public Flowable<OTCDepositBalanceListBean> requestDepositBalanceList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestGetDepositBalanceList()
                .compose(new DefaultTransformer<OTCDepositBalanceListBean>());
    }

    @Override
    public Flowable<OTCDepositBalanceBean> requestNewDepositBalance(String currency) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestNewBalance(currency)
                .compose(new DefaultTransformer<OTCDepositBalanceBean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    @Override
    public Flowable<OTCRechargeResultBean> requestRechargeDepositProvider(OTCRechargeDepositParam reqParam) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestRechargeDeposit(reqParam)
                .compose(new DefaultTransformer<OTCRechargeResultBean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    //requestWithdrawDeposit
    @Override
    public Flowable<OTCWithdrawResultBean> requestWithdrawDepositProvider(OTCWithdrawDepositParam reqParam) {
        HttpUtils.getInstance(BaseApplication.getContext())
                .updateTimeOut(60);
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestWithdrawDeposit(reqParam)
                .compose(new DefaultTransformer<OTCWithdrawResultBean>())
                .doOnTerminate(() -> HttpUtils.getInstance(BaseApplication.getContext()).resetTimeOut());
    }

    //requestSetSortDepositList
    //requestDepositSortProvider
    @Override
    public Flowable<Object> requestDepositSortProvider(List<OTCDepositSortParams> otcDepositSortParams) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestSetSortDepositList(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        JSON.toJSONString(otcDepositSortParams)
                ))
                .compose(new DefaultTransformer<Object>());
    }

    //requestWithdrawDeposit
    @Override
    public Flowable<OTCCurrentStepBean> requestGetCurrentStepProvider() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestGetCurrentStep()
                .compose(new DefaultTransformer<OTCCurrentStepBean>());
    }

    @Override
    public Flowable<String> requestSyncAddressProvider(OTCReqSyncAddressParams reqParam) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestSyncAddress(reqParam)
                .compose(new DefaultTransformer<String>());
    }

    @Override
    public Flowable<Boolean> requestCheckBtnAddressProvider(OTCReqSyncAddressParams reqParam) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestCheckAddress(reqParam)
                .compose(new DefaultTransformer<Boolean>());
    }

    //

    @Override
    public void init(Context context) {
        mContext = context;
    }


}
