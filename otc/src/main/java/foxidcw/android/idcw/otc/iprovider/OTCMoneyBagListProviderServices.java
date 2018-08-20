package foxidcw.android.idcw.otc.iprovider;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.List;

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

/**
 * Created by hpz on 2018/5/2.
 */

public interface OTCMoneyBagListProviderServices extends IProvider {
    Flowable<List<OTCWalletAssetBean>> getWalletListProvider();
    Flowable<OTCDepositBalanceListBean> requestDepositBalanceList();//获取承兑商状态和保证金列表
    Flowable<OTCDepositBalanceBean> requestNewDepositBalance(String currency);
    Flowable<OTCRechargeResultBean> requestRechargeDepositProvider(OTCRechargeDepositParam otcRechargeDepositParam);//充值保证金
    Flowable<OTCWithdrawResultBean> requestWithdrawDepositProvider(OTCWithdrawDepositParam otcWithdrawDepositParam);//提取保证金
    Flowable<Object> requestDepositSortProvider(List<OTCDepositSortParams> otcDepositSortParams);//排序保证金列表
    Flowable<OTCCurrentStepBean> requestGetCurrentStepProvider();//获取当前设置步骤
    Flowable<String> requestSyncAddressProvider(OTCReqSyncAddressParams otcReqSyncAddressParams);//校验地址
    Flowable<Boolean> requestCheckBtnAddressProvider(OTCReqSyncAddressParams otcReqSyncAddressParams);//校验地址

}