package com.idcg.idcw.model.params;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class VerfityNameReqParam {
    String content;
    String validType;

    public VerfityNameReqParam(String content, String validType) {
        this.content = content;
        this.validType = validType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValidType() {
        return validType;
    }

    public void setValidType(String validType) {
        this.validType = validType;
    }
}
