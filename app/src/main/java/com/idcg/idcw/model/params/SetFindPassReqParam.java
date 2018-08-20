package com.idcg.idcw.model.params;

/**
 * Created by admin-2 on 2018/3/15.
 */

public class SetFindPassReqParam extends LanguageReqParam {

    private String userName;
    private String mobile;
    private String email;
    private String newPasssword;
    private String affirmPassword;
    private String verifyCode;

    public SetFindPassReqParam(String userName, String mobile, String email, String newPasssword, String affirmPassword, String verifyCode,String language) {
        this.userName = userName;
        this.mobile = mobile;
        this.email = email;
        this.newPasssword = newPasssword;
        this.affirmPassword = affirmPassword;
        this.verifyCode = verifyCode;
        this.language=language;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPasssword() {
        return newPasssword;
    }

    public void setNewPasssword(String newPasssword) {
        this.newPasssword = newPasssword;
    }

    public String getAffirmPassword() {
        return affirmPassword;
    }

    public void setAffirmPassword(String affirmPassword) {
        this.affirmPassword = affirmPassword;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
