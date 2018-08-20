package com.idcw.pay.model.bean;

public class CustomerInfoBean {

    /**
     * id : 0
     * uid : 0
     * companyName : string
     */

    private int id; // 公司id
    private int uid;// 公司钱包uid
    private String companyName; // 公司名称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
