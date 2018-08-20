package com.idcg.idcw.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class VersionListBean implements Serializable {
    /**
     * mobileVersionList : [{"id":1009,"version":"2.0","version_name":"V2.0","client":"2","modifyTime":"2018-02-08T20:01:16","modifyBy":"admin","createTime":"2018-02-08T20:01:16","createBy":"admin","isItUpdate":true,"app_update_url":"http://www.idcw.io/mobile/mobileindex.html","version_introduction":"","version_introduction_url":"?id=1009&lang=1"},{"id":1010,"version":"2.1.2","version_name":"V2.1.2","client":"2","modifyTime":"2018-02-08T16:59:51","modifyBy":"admin","createTime":"2018-02-08T16:59:51","createBy":"admin","isItUpdate":true,"app_update_url":"http://www.idcw.io/mobile/mobileindex.html","version_introduction":"","version_introduction_url":"?id=1010&lang=1"}]
     * total : 2
     */

    private int total;
    private List<MobileVersionListBean> mobileVersionList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MobileVersionListBean> getMobileVersionList() {
        return mobileVersionList;
    }

    public void setMobileVersionList(List<MobileVersionListBean> mobileVersionList) {
        this.mobileVersionList = mobileVersionList;
    }

    public static class MobileVersionListBean implements Serializable{
        /**
         * id : 1009
         * version : 2.0
         * version_name : V2.0
         * client : 2
         * modifyTime : 2018-02-08T20:01:16
         * modifyBy : admin
         * createTime : 2018-02-08T20:01:16
         * createBy : admin
         * isItUpdate : true
         * app_update_url : http://www.idcw.io/mobile/mobileindex.html
         * version_introduction :
         * version_introduction_url : ?id=1009&lang=1
         */

        private int id;
        private String version;
        private String version_name;
        private String client;
        private String modifyTime;
        private String modifyBy;
        private String createTime;
        private String createBy;
        private boolean isItUpdate;
        private String app_update_url;
        private String version_introduction;
        private String version_introduction_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getModifyBy() {
            return modifyBy;
        }

        public void setModifyBy(String modifyBy) {
            this.modifyBy = modifyBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public boolean isIsItUpdate() {
            return isItUpdate;
        }

        public void setIsItUpdate(boolean isItUpdate) {
            this.isItUpdate = isItUpdate;
        }

        public String getApp_update_url() {
            return app_update_url;
        }

        public void setApp_update_url(String app_update_url) {
            this.app_update_url = app_update_url;
        }

        public String getVersion_introduction() {
            return version_introduction;
        }

        public void setVersion_introduction(String version_introduction) {
            this.version_introduction = version_introduction;
        }

        public String getVersion_introduction_url() {
            return version_introduction_url;
        }

        public void setVersion_introduction_url(String version_introduction_url) {
            this.version_introduction_url = version_introduction_url;
        }
    }

}
