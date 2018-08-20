package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.params
 * 备注消息：
 * 修改时间：2018/3/16 14:11
 **/

public class NoticeTopBean implements Serializable {
    /**
     * id : 1432
     * msId : 1424
     * msgTitle : 这是一个andriod的中文公告
     * msgType : 1
     * msgContent : 这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告
     * leftButtonTxt : 取消
     * rightButtonTxt : 确认
     * leftButtonJump : false
     * rightButtonJump : false
     * leftButtonUrl :
     * rightButtonUrl :
     * startTime : 2018-02-01T21:26:00
     * readed : 1
     * logo : /upload/logo/active.png
     * secondaryTitle : 这是一个andriod的中文公告这是一个andriod的中文公告
     * newMsgContent : <p>这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告这是一个andriod的中文公告</p>
     * contentUrl : www.idcw.io/mobileActivity/index.html
     */

    private int id;
    private int msId;
    private String msgTitle;
    private int msgType;
    private String msgContent;
    private String leftButtonTxt;
    private String rightButtonTxt;
    private boolean leftButtonJump;
    private boolean rightButtonJump;
    private String leftButtonUrl;
    private String rightButtonUrl;
    private String startTime;
    private int readed;
    private String logo;
    private String secondaryTitle;
    private String newMsgContent;
    private String contentUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsId() {
        return msId;
    }

    public void setMsId(int msId) {
        this.msId = msId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getLeftButtonTxt() {
        return leftButtonTxt;
    }

    public void setLeftButtonTxt(String leftButtonTxt) {
        this.leftButtonTxt = leftButtonTxt;
    }

    public String getRightButtonTxt() {
        return rightButtonTxt;
    }

    public void setRightButtonTxt(String rightButtonTxt) {
        this.rightButtonTxt = rightButtonTxt;
    }

    public boolean isLeftButtonJump() {
        return leftButtonJump;
    }

    public void setLeftButtonJump(boolean leftButtonJump) {
        this.leftButtonJump = leftButtonJump;
    }

    public boolean isRightButtonJump() {
        return rightButtonJump;
    }

    public void setRightButtonJump(boolean rightButtonJump) {
        this.rightButtonJump = rightButtonJump;
    }

    public String getLeftButtonUrl() {
        return leftButtonUrl;
    }

    public void setLeftButtonUrl(String leftButtonUrl) {
        this.leftButtonUrl = leftButtonUrl;
    }

    public String getRightButtonUrl() {
        return rightButtonUrl;
    }

    public void setRightButtonUrl(String rightButtonUrl) {
        this.rightButtonUrl = rightButtonUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public void setSecondaryTitle(String secondaryTitle) {
        this.secondaryTitle = secondaryTitle;
    }

    public String getNewMsgContent() {
        return newMsgContent;
    }

    public void setNewMsgContent(String newMsgContent) {
        this.newMsgContent = newMsgContent;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
