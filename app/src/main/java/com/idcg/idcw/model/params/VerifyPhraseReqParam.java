package com.idcg.idcw.model.params;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 14:48
 **/

public class VerifyPhraseReqParam implements Serializable {
    /**
     * type : 0
     * userName : string
     * PhraseInfo : [{"serial_number":0,"phrase":"string"}]
     */

    private int type;
    private String userName;
    private List<PhraseInfoBean> PhraseInfo;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<PhraseInfoBean> getPhraseInfo() {
        return PhraseInfo;
    }

    public void setPhraseInfo(List<PhraseInfoBean> PhraseInfo) {
        this.PhraseInfo = PhraseInfo;
    }

    public static class PhraseInfoBean {
        /**
         * serial_number : 0
         * phrase : string
         */

        private int serial_number;
        private String phrase;

        public int getSerial_number() {
            return serial_number;
        }

        public void setSerial_number(int serial_number) {
            this.serial_number = serial_number;
        }

        public String getPhrase() {
            return phrase;
        }

        public void setPhrase(String phrase) {
            this.phrase = phrase;
        }
    }
}
