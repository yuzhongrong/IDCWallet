package foxidcw.android.idcw.otc.model.beans;

import java.util.List;

/**
 * Created by hpz on 2018/5/5.
 */

public class OTCCoinExchangeBean {

    private List<OTCCoinListBean> SortList;

    public List<OTCCoinListBean> getSortList() {
        return SortList;
    }

    public void setSortList(List<OTCCoinListBean> sortList) {
        SortList = sortList;
    }
}
