package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.params ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/16 10:56
 **/

public class ReFeeBean implements Serializable {
    /**
     * slow : 0.0005
     * fastFee : 0.001
     * veryFastFee : 0.003
     */

    private String slow;
    private String fastFee;
    private String veryFastFee;

    public String getSlow() {
        return slow;
    }

    public void setSlow(String slow) {
        this.slow = slow;
    }

    public String getFastFee() {
        return fastFee;
    }

    public void setFastFee(String fastFee) {
        this.fastFee = fastFee;
    }

    public String getVeryFastFee() {
        return veryFastFee;
    }

    public void setVeryFastFee(String veryFastFee) {
        this.veryFastFee = veryFastFee;
    }
}
