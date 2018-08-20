package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 12:00
 **/

public class SendPhoneCodeReqParam  extends  LanguageReqParam implements Serializable {

    private String mobile;
    private String verifyCodeType;

    public SendPhoneCodeReqParam() {


    }

    public SendPhoneCodeReqParam(String mobile, String verifyCodeType, String language) {
        this.mobile = mobile;
        this.verifyCodeType = verifyCodeType;
        this.language = language;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCodeType() {
        return verifyCodeType;
    }

    public void setVerifyCodeType(String verifyCodeType) {
        this.verifyCodeType = verifyCodeType;
    }

}
