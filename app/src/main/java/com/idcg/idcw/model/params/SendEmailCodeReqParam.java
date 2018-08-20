package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 12:01
 **/

public class SendEmailCodeReqParam extends  LanguageReqParam implements Serializable {
    private String MailAddress;
    private String verifyCodeType;


    public SendEmailCodeReqParam(String mailAddress, String language, String verifyCodeType) {
        MailAddress = mailAddress;
        this.language = language;
        this.verifyCodeType = verifyCodeType;
    }

    public SendEmailCodeReqParam() {

    }

    public String getMailAddress() {
        return MailAddress;
    }

    public void setMailAddress(String mailAddress) {
        MailAddress = mailAddress;
    }

    public String getVerifyCodeType() {
        return verifyCodeType;
    }

    public void setVerifyCodeType(String verifyCodeType) {
        this.verifyCodeType = verifyCodeType;
    }
}
