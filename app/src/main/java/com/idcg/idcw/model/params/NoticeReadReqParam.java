package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 14:15
 **/

public class NoticeReadReqParam implements Serializable {
    private String msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
