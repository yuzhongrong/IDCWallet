package com.idcg.idcw.model.bean;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.bean
 * 备注消息：
 * 修改时间：2018/3/19 9:41
 **/

public class CurrencyIsShowBean {
    /**
     * id : 0
     * isShow : true
     */

    private int id;
    private int sortIndex;
    private boolean isShow;

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }


}
