package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.params ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/15 14:24
 **/

public class BaseReqParam implements Serializable {

    protected String client = "app";

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
