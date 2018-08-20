package foxidcw.android.idcw.otc.model.beans;

import java.util.List;

/**
 * Created by hpz on 2018/5/2.
 */

public class OTCCoinCurrExchangeBean {

    //private List<CoinPairBean> CoinList;
    private List<OTCCoinBean> SortList;

//    public List<CoinPairBean> getCoinList() {
//        return CoinList;
//    }
//
//    public void setCoinList(List<CoinPairBean> coinList) {
//        CoinList = coinList;
//    }

    public List<OTCCoinBean> getSortList() {
        return SortList;
    }

    public void setSortList(List<OTCCoinBean> sortList) {
        SortList = sortList;
    }
}
