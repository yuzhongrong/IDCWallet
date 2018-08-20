package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 17:43
 **/

public class SetPayPassInfoReqParam implements Serializable {
    private String originalPayPwd;
    private String newPayPwd;
    private String affirmPayPwd;
    private String type;
    private String verifyCode;
    private String verifyUser;
    private String device_id;
    private boolean newVersion;

    public SetPayPassInfoReqParam(String originalPayPwd, String newPayPwd, String affirmPayPwd, String type, String verifyCode,String verifyUser,String device_id,boolean newVersion) {
        this.originalPayPwd = originalPayPwd;
        this.newPayPwd = newPayPwd;
        this.affirmPayPwd = affirmPayPwd;
        this.type = type;
        this.verifyCode = verifyCode;
        this.verifyUser = verifyUser;
        this.device_id = device_id;
        this.newVersion = newVersion;
    }
    public SetPayPassInfoReqParam(){

    }

    public String getOriginalPayPwd() {
        return originalPayPwd;
    }

    public void setOriginalPayPwd(String originalPayPwd) {
        this.originalPayPwd = originalPayPwd;
    }

    public String getNewPayPwd() {
        return newPayPwd;
    }

    public void setNewPayPwd(String newPayPwd) {
        this.newPayPwd = newPayPwd;
    }

    public String getAffirmPayPwd() {
        return affirmPayPwd;
    }

    public void setAffirmPayPwd(String affirmPayPwd) {
        this.affirmPayPwd = affirmPayPwd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public boolean isNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }
}
