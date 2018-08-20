package foxidcw.android.idcw.otc.model.beans;

import java.io.Serializable;

/**
 * Created by yuzhongrong on 2018/5/7.
 */

public class OTCChatHistoryBean implements Serializable {


    /**
     * SendUserID : string
     * UserID : string
     * GroupName : string
     * ChatObjectCategory : 0
     * Message : string
     * FileUrl : string
     * TimeStamp : 0
     * Id : {"Timestamp":0,"Machine":0,"Pid":0,"Increment":0,"CreationTime":"2018-05-07T14:22:25.096Z"}
     * State : string
     * CreateTime : 2018-05-07T14:22:25.096Z
     * UpdateTime : 2018-05-07T14:22:25.096Z
     */

    private String SendUserID;
    private String UserID;
    private String GroupName;
    private int ChatObjectCategory;
    private String Message;
    private String FileUrl;
    private long TimeStamp;
    private IdBean Id;
    private String State;
    private String CreateTime;
    private String UpdateTime;

    public String getSendUserID() {
        return SendUserID;
    }

    public void setSendUserID(String SendUserID) {
        this.SendUserID = SendUserID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public int getChatObjectCategory() {
        return ChatObjectCategory;
    }

    public void setChatObjectCategory(int ChatObjectCategory) {
        this.ChatObjectCategory = ChatObjectCategory;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String FileUrl) {
        this.FileUrl = FileUrl;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    public IdBean getId() {
        return Id;
    }

    public void setId(IdBean Id) {
        this.Id = Id;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public static class IdBean {
        /**
         * Timestamp : 0
         * Machine : 0
         * Pid : 0
         * Increment : 0
         * CreationTime : 2018-05-07T14:22:25.096Z
         */

        private int Timestamp;
        private int Machine;
        private int Pid;
        private int Increment;
        private String CreationTime;

        public int getTimestamp() {
            return Timestamp;
        }

        public void setTimestamp(int Timestamp) {
            this.Timestamp = Timestamp;
        }

        public int getMachine() {
            return Machine;
        }

        public void setMachine(int Machine) {
            this.Machine = Machine;
        }

        public int getPid() {
            return Pid;
        }

        public void setPid(int Pid) {
            this.Pid = Pid;
        }

        public int getIncrement() {
            return Increment;
        }

        public void setIncrement(int Increment) {
            this.Increment = Increment;
        }

        public String getCreationTime() {
            return CreationTime;
        }

        public void setCreationTime(String CreationTime) {
            this.CreationTime = CreationTime;
        }
    }
}
