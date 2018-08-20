package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.List;

import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositMgListBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositWaterBean;
import foxidcw.android.idcw.otc.model.beans.OTCSelectPayListBean;
import foxidcw.android.idcw.otc.model.params.OTCDepositWaterParams;
import io.reactivex.Flowable;

/**
 * Created by hpz on 2018/5/5.
 */

public interface OTCGetCoinListProviderServices extends IProvider {
    Flowable<List<OTCCoinListBean>> requestOTCCoinList() ;
    Flowable<List<OTCCoinListBean>> requestOTCCoinLegalList() ;
    Flowable<List<OTCSelectPayListBean>> requestOTCPayTypeList() ;
    Flowable<List<OTCDepositMgListBean>> requestOTCDepositMgList() ;//获取保证金管理列表
    Flowable<List<OTCDepositWaterBean>> requestOTCDepositWaterList(OTCDepositWaterParams otcDepositWaterParams) ;//获取保证金流水
    Flowable<List<OTCCoinListBean>> requestOTCCurrWaterList() ;//获取保证金币种列表及余额
    Flowable<List<OTCCoinListBean>> requestOTCDepositCoinList() ;//获取保证金列表币种

}
