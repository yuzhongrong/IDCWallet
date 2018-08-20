package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 14:22
 **/

public class CheckStatusBean implements Serializable {
    /**
     * password_prompt : {"valid":true,"phrase":"cccccccccccc"}
     * payPassword : {"valid":true}
     * password : true
     * email_valid : {"valid":false,"email":""}
     * mobile_valid : {"valid":true,"mobile":""}
     * wallet_phrase : true
     */

    private PasswordPromptBean password_prompt;
    private PayPasswordBean payPassword;
    private boolean password;
    private EmailValidBean email_valid;
    private MobileValidBean mobile_valid;
    private boolean wallet_phrase;

    public PasswordPromptBean getPassword_prompt() {
        return password_prompt;
    }

    public void setPassword_prompt(PasswordPromptBean password_prompt) {
        this.password_prompt = password_prompt;
    }

    public PayPasswordBean getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(PayPasswordBean payPassword) {
        this.payPassword = payPassword;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public EmailValidBean getEmail_valid() {
        return email_valid;
    }

    public void setEmail_valid(EmailValidBean email_valid) {
        this.email_valid = email_valid;
    }

    public MobileValidBean getMobile_valid() {
        return mobile_valid;
    }

    public void setMobile_valid(MobileValidBean mobile_valid) {
        this.mobile_valid = mobile_valid;
    }

    public boolean isWallet_phrase() {
        return wallet_phrase;
    }

    public void setWallet_phrase(boolean wallet_phrase) {
        this.wallet_phrase = wallet_phrase;
    }

    public static class PasswordPromptBean {
        /**
         * valid : true
         * phrase : cccccccccccc
         */

        private boolean valid;
        private String phrase;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getPhrase() {
            return phrase;
        }

        public void setPhrase(String phrase) {
            this.phrase = phrase;
        }
    }

    public static class PayPasswordBean {
        /**
         * valid : true
         */

        private boolean valid;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }

    public static class EmailValidBean {
        /**
         * valid : false
         * email :
         */

        private boolean valid;
        private String email;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class MobileValidBean {
        /**
         * valid : true
         * mobile :
         */

        private boolean valid;
        private String mobile;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
