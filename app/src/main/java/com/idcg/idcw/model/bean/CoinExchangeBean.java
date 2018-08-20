package com.idcg.idcw.model.bean;

import java.util.List;

/**
 *
 * @author yiyang
 */
public class CoinExchangeBean {
    private List<CoinPairBean> CoinList;
    private List<CoinBean>     SortList;

    public List<CoinPairBean> getCoinList() {
        return CoinList;
    }

    public void setCoinList(List<CoinPairBean> coinList) {
        CoinList = coinList;
    }

    public List<CoinBean> getSortList() {
        return SortList;
    }

    public void setSortList(List<CoinBean> sortList) {
        SortList = sortList;
    }
}
