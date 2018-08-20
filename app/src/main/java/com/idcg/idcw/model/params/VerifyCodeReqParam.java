package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 13:41
 **/

public class VerifyCodeReqParam implements Serializable {
    private String verifyUser;
    private String verifyCodeType;
    private String VerifyCode;

    public VerifyCodeReqParam(String verifyUser, String verifyCodeType, String verifyCode) {
        this.verifyUser = verifyUser;
        this.verifyCodeType = verifyCodeType;
        VerifyCode = verifyCode;
    }

    public String getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getVerifyCodeType() {
        return verifyCodeType;
    }

    public void setVerifyCodeType(String verifyCodeType) {
        this.verifyCodeType = verifyCodeType;
    }

    public String getVerifyCode() {
        return VerifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        VerifyCode = verifyCode;
    }
}
