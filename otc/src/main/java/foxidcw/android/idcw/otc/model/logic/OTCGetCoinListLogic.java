package foxidcw.android.idcw.otc.model.logic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.cjwsc.idcm.net.transformer.DefaultTransformer;

import java.util.List;

import foxidcw.android.idcw.otc.api.OTCHttpApi;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositMgListBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositWaterBean;
import foxidcw.android.idcw.otc.model.beans.OTCSelectPayListBean;
import foxidcw.android.idcw.otc.model.params.OTCDepositWaterParams;
import io.reactivex.Flowable;

import static foxidcw.android.idcw.otc.iprovider.constants.OTCProviderPath.path_OTCGetCoinListProviderServices;


/**
 * Created by hpz on 2018/5/5.
 */

@Route(path = path_OTCGetCoinListProviderServices)
public class OTCGetCoinListLogic implements OTCGetCoinListProviderServices{
    private Context mContext;
    @Override
    public Flowable<List<OTCCoinListBean>> requestOTCCoinList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestAcceptstantCoinList()
                .compose(new DefaultTransformer<List<OTCCoinListBean>>());
    }

    @Override
    public Flowable<List<OTCCoinListBean>> requestOTCCoinLegalList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestAcceptstantLegalList()
                .compose(new DefaultTransformer<List<OTCCoinListBean>>());
    }

    //requestPayTypeList

    @Override
    public Flowable<List<OTCSelectPayListBean>> requestOTCPayTypeList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestPayTypeList()
                .compose(new DefaultTransformer<List<OTCSelectPayListBean>>());
    }

    //requestGetDepositManagerList
    @Override
    public Flowable<List<OTCDepositMgListBean>> requestOTCDepositMgList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestGetDepositManagerList()
                .compose(new DefaultTransformer<List<OTCDepositMgListBean>>());
    }


    @Override
    public Flowable<List<OTCDepositWaterBean>> requestOTCDepositWaterList(OTCDepositWaterParams otcDepositWaterParams) {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestGetDepositWaterList(otcDepositWaterParams)
                .compose(new DefaultTransformer<List<OTCDepositWaterBean>>());
    }

    //requestAcceptstantgetCurrWaterList
    @Override
    public Flowable<List<OTCCoinListBean>> requestOTCCurrWaterList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestAcceptstantgetCurrWaterList()
                .compose(new DefaultTransformer<List<OTCCoinListBean>>());
    }


    @Override
    public Flowable<List<OTCCoinListBean>> requestOTCDepositCoinList() {
        return HttpUtils.getInstance(mContext)
                .getRetrofitClient()
                .builder(OTCHttpApi.class)
                .requestDepositCoinList()
                .compose(new DefaultTransformer<List<OTCCoinListBean>>());
    }

    @Override
    public void init(Context context) {
        mContext=context;
    }
}
