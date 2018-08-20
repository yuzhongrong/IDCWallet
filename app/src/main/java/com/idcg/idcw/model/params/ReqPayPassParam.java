package com.idcg.idcw.model.params;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class ReqPayPassParam  {
    String originalPayPwd;
    String newPayPwd;
    String affirmPayPwd;
    String type;
    String verifyCode;
    String verifyUser;
    private String device_id;
    private boolean newVersion;

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
