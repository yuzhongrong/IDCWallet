package com.idcg.idcw.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin-2 on 2018/3/29.
 */

public class ChatHubList implements Serializable{
    List<ChatHubBean> datas;

    public List<ChatHubBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ChatHubBean> datas) {
        this.datas = datas;
    }
}
