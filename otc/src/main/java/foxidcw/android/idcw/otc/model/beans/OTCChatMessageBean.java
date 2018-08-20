package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

/**
 * Created by yuzhongrong on 2018/5/5.
 */

public class OTCChatMessageBean implements Serializable{

    private int UserID;
    private String SendUserID;
    private String GroupName;
    private int ChatObjectCategory;
    private String Message;
    private String FileUrl;
    private Object CreateTime;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getSendUserID() {
        return SendUserID;
    }

    public void setSendUserID(String sendUserID) {
        SendUserID = sendUserID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public int getChatObjectCategory() {
        return ChatObjectCategory;
    }

    public void setChatObjectCategory(int chatObjectCategory) {
        ChatObjectCategory = chatObjectCategory;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    public Object getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Object createTime) {
        CreateTime = createTime;
    }
}
