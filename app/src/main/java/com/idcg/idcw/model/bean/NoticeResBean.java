package com.idcg.idcw.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.bean
 * 备注消息：
 * 修改时间：2018/3/18 18:14
 **/

public class NoticeResBean {

    /**
     * status : 1
     * msg : success
     * ex : null
     * data : {"count":28,"msgData":[{"id":1608,"msId":1595,"msgTitle":"NO3：测试Android关于没有男人节的中文推送公告","msgType":1,"msgContent":"NO3：测试Android关于没有男人节的中文推送公告","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-03-08T20:23:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1606,"msId":1593,"msgTitle":"NO1：测试Android关于女神节推送公告","msgType":1,"msgContent":"NO1：测试Android关于女神节推送公告NO1：测试Android关于女神节推送公告NO1：测试Android关于女神节推送公告NO1：测试Android关于女神节推送公告NO1：测试Android关于女神节推送公告","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-03-08T20:08:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1604,"msId":1591,"msgTitle":"android公告测试","msgType":1,"msgContent":"android公告测试111android公告测试111v","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-03-08T19:32:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1603,"msId":1590,"msgTitle":"NO1：测试Android关于三八妇女节推送公告","msgType":1,"msgContent":"Android关于三八妇女节推送公告","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-03-08T19:25:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1597,"msId":1584,"msgTitle":"测试Android的中文公告","msgType":1,"msgContent":"测试Android的中文公告测试Android的中文公告","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-03-06T21:44:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1593,"msId":1580,"msgTitle":"NO2：测试Android的推送中文公告","msgType":1,"msgContent":"NO2：测试Android的推送中文公告","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-03-03T15:49:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1591,"msId":1578,"msgTitle":"NO1：测试Android的推送中文公告","msgType":1,"msgContent":"NO1：测试Android的推送中文公告","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-03-03T15:33:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1569,"msId":1558,"msgTitle":"NO8：测试PC的推送中文公告⑧","msgType":1,"msgContent":"NO8：测试PC的推送中文公告⑧","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-02-06T23:09:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1568,"msId":1557,"msgTitle":"NO7：测试PC的推送中文公告⑦","msgType":1,"msgContent":"NO7：测试PC的推送中文公告⑦","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-02-06T22:25:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""},{"id":1566,"msId":1555,"msgTitle":"NO5：测试PC的推送中文公告⑤","msgType":1,"msgContent":"NO5：测试PC的推送中文公告⑤","leftButtonTxt":"取消","rightButtonTxt":"确定","leftButtonJump":false,"rightButtonJump":false,"leftButtonUrl":"","rightButtonUrl":"","startTime":"2018-02-06T22:19:00","readed":1,"logo":"","secondaryTitle":"","newMsgContent":"","contentUrl":""}]}
     */

    private int count;
    private List<MsgDataBean> msgData;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MsgDataBean> getMsgData() {
        return msgData;
    }

    public void setMsgData(List<MsgDataBean> msgData) {
        this.msgData = msgData;
    }

    public static class MsgDataBean implements Serializable {
        /**
         * id : 1608
         * msId : 1595
         * msgTitle : NO3：测试Android关于没有男人节的中文推送公告
         * msgType : 1
         * msgContent : NO3：测试Android关于没有男人节的中文推送公告
         * leftButtonTxt : 取消
         * rightButtonTxt : 确定
         * leftButtonJump : false
         * rightButtonJump : false
         * leftButtonUrl :
         * rightButtonUrl :
         * startTime : 2018-03-08T20:23:00
         * readed : 1
         * logo :
         * secondaryTitle :
         * newMsgContent :
         * contentUrl :
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
}
