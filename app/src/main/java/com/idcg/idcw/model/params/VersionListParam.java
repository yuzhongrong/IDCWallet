package com.idcg.idcw.model.params;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class VersionListParam {
    String clientName;
    String lang;
    String pageIndex;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
