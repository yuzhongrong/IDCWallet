package com.idcg.idcw.net;

import com.idcg.idcw.model.bean.CurrencyDetailInfo;
import com.cjwsc.idcm.net.response.HttpResponse;

/**
 * Created by hpz on 2018/4/2.
 */

public class HttpAppResponse<T> extends HttpResponse<T> {
    private CurrencyDetailInfo.PageBean page;

    public CurrencyDetailInfo.PageBean getPage() {
        return page;
    }

    public void setPage(CurrencyDetailInfo.PageBean page) {
        this.page = page;
    }


}
