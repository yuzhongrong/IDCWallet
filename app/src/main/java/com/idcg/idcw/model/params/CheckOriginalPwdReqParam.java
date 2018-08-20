package com.idcg.idcw.model.params;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 17:49
 **/

public class CheckOriginalPwdReqParam implements Serializable {
    private String type;
    private String password;
    private String device_id;
    private boolean newVersion;

    public CheckOriginalPwdReqParam(String type, String password,String device_id,boolean newVersion) {
        this.type = type;
        this.password = password;
        this.device_id = device_id;
        this.newVersion = newVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
