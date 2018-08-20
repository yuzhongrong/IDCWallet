package com.idcg.idcw.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.bean
 * 备注消息：
 * 修改时间：2018/3/18 18:25
 **/

public class NoticeOptionBean implements Serializable {
    /**
     * type : 0
     * uid : 0
     * msgId : [0]
     */
    private int type;
    private int uid;
    private List<Integer> msgId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public List<Integer> getMsgId() {
        return msgId;
    }

    public void setMsgId(List<Integer> msgId) {
        this.msgId = msgId;
    }
}
