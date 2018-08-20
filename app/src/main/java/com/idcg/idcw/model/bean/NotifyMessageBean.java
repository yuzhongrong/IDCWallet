package com.idcg.idcw.model.bean;

/**
 * Created by admin-2 on 2018/3/16.
 */

public class NotifyMessageBean {


    /**
     * id : 3
     * msgType : 1
     * msgTitle : english
     * msgContent : english
     * leftButtonTxt :
     * rightButtonTxt :
     * leftButtonJump : false
     * rightButtonJump : false
     * leftButtonUrl :
     * rightButtonUrl :
     * msgTerminal : all
     * startTime : 2018-01-13T11:32:00
     * origCurrency :
     * receiveCurrency :
     */

    private int id;
    private int msgType;
    private String msgTitle;
    private String msgContent;
    private String leftButtonTxt;
    private String rightButtonTxt;
    private boolean leftButtonJump;
    private boolean rightButtonJump;
    private String leftButtonUrl;
    private String rightButtonUrl;
    private String msgTerminal;
    private String startTime;
    private String origCurrency;
    private String receiveCurrency;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
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

    public String getMsgTerminal() {
        return msgTerminal;
    }

    public void setMsgTerminal(String msgTerminal) {
        this.msgTerminal = msgTerminal;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOrigCurrency() {
        return origCurrency;
    }

    public void setOrigCurrency(String origCurrency) {
        this.origCurrency = origCurrency;
    }

    public String getReceiveCurrency() {
        return receiveCurrency;
    }

    public void setReceiveCurrency(String receiveCurrency) {
        this.receiveCurrency = receiveCurrency;
    }
}
