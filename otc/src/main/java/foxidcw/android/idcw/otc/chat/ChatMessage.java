package foxidcw.android.idcw.otc.chat;

import android.content.Context;

import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.signarl.GroupsUtils;
import com.cjwsc.idcm.signarl.SwitchGroupIdCallBack;

import com.yy.chat.IMessage;
import com.yy.chat.ImgMessageBody;
import com.yy.chat.MessageBody;
import com.yy.chat.MessageListAdapter;
import com.yy.chat.TextMessageBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author yiyang
 */

public class ChatMessage implements IMessage<MessageBody> {
    private String from;
    private String to;
    private MessageBody body;
    private String id;
    /**为true表示为发送*/
    private boolean dirction;
    private int status;
    private int type;
    private long timeStamp;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDirction() {
        return dirction;
    }

    public void setDirction(boolean dirction) {
        this.dirction = dirction;
    }

    public String getTime() {
        return getFormatTime(timeStamp());
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public long timeStamp() {
        return timeStamp;
    }

    @Override
    public String getAvatar() {

        if(type==-1){//系统头像

            return "file:///android_asset/sys_avatar.png";

        }else{//接受或者发送消息
            if(dirction){//本人头像
               return   "file:///android_asset/me_avatar.png";
            }else{//对方头像
                return   "file:///android_asset/side_avatar.png";
            }
        }
    }

    @Override
    public String getNickname() {
        return null;
    }

    public void setStatus(@Status int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public int getItemType() {
//        if (body instanceof TextMessageBody) {
//            return isDirction() ? TYPE_TEXT_SEND : TYPE_TEXT_RECV;
//        }
//        if (body instanceof ImgMessageBody) {
//            return isDirction() ? TYPE_IMG_SEND : TYPE_IMG_RECV;
//        }
        return 0;
    }

    public void setType(int type){
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public static ChatMessage createSysMessage(String text){
        ChatMessage receiveMessage = createTextReceiveMessage(text,"");
        receiveMessage.setType(-1);
        return receiveMessage;
    }

    public static ChatMessage createSysImgMessage(String path){
        ChatMessage receiveMessage = createImgReceiveMessage(path,"");
        receiveMessage.setType(-1);
        return receiveMessage;
    }

    public static ChatMessage createTextSendMessage(String text, String to) {
        ChatMessage sendMessage = createSendMessage(to);
        sendMessage.setBody(new TextMessageBody(text));
        return sendMessage;
    }

    public static ChatMessage createTextReceiveMessage(String text, String from) {
        ChatMessage receiveMessage = createReceiveMessage(from);
        receiveMessage.setBody(new TextMessageBody(text));
        return receiveMessage;
    }

    public static ChatMessage createImgSendMessage(String path, String to) {
        ChatMessage sendMessage = createSendMessage(to);
        sendMessage.setBody(new ImgMessageBody(path));
        return sendMessage;
    }

    public static ChatMessage createImgReceiveMessage(String text, String from) {
        ChatMessage receiveMessage = createReceiveMessage(from);
        receiveMessage.setBody(new ImgMessageBody(text));
        return receiveMessage;
    }


//    public static ChatMessage createImgReceiveMessage(String text, String from,boolean issystem,boolean dirction) {
//        ChatMessage receiveMessage = createReceiveMessage(from);
//        if(issystem){//系统的图片一定是-1，false
//            receiveMessage.setType(-1);
//            receiveMessage.setDirction(false);
//        }else{//接收和发送方
//            receiveMessage.setDirction(dirction);
//        }
//        receiveMessage.setBody(new ImgMessageBody(text));
//        return receiveMessage;
//    }

    public static ChatMessage createSendMessage(String to) {
        ChatMessage message = new ChatMessage();
        message.setDirction(true);
        message.setFrom(self());
        message.setTo(to);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static ChatMessage createReceiveMessage(String from) {
        ChatMessage message = new ChatMessage();
        message.setDirction(false);
        message.setFrom(from);
        message.setTo(self());
        return message;
    }

    private static final DateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String getFormatTime(long timeStamp){
        return DATE_FORMAT.format(new Date(timeStamp));
    }

    static String self() {
        LoginStatus loginBean = LoginUtils.getLoginBean(BaseApplication.getContext());
        if(loginBean==null)
            return "";
        return loginBean.getId()+"";
    }



    @Override
    public void handleMsg(Context context, MessageListAdapter messageListAdapter) {
        if(!isDirction())
            return;

        boolean finalIsReSend = status == STATUS_FAIL;

        setStatus(STATUS_INPROGRESS);
        StatusHelper.get(this).getStatusCallBack().onProgress(0);

        SendMsgCallBack sendMsgCallBack = new SendMsgCallBack() {
            @Override
            public void onSuccess(ChatMessage message) {
                setStatus(STATUS_SUCCESS);
                message.setTimeStamp(System.currentTimeMillis());
                if(finalIsReSend){
                    int curPos = messageListAdapter.getData().indexOf(message);
                    int tarPos = messageListAdapter.getData().size() - 1;
                    if(curPos != tarPos) {
                        Collections.swap(messageListAdapter.getData(), curPos, tarPos);
                        messageListAdapter.notifyItemMoved(curPos, tarPos);
                    }
                }
                StatusHelper.get(message).getStatusCallBack().onSuccess();
            }

            @Override
            public void onFailed(ChatMessage message) {
                setStatus(STATUS_FAIL);
                StatusHelper.get(message).getStatusCallBack().onError();
            }
        };
        if (getBody() instanceof TextMessageBody) {//发送文字和接受文字
            String msg = ((TextMessageBody) getBody()).getText();
            sendMsg(context, msg, sendMsgCallBack);
        } else if(getBody() instanceof ImgMessageBody) {
            String msg = ((ImgMessageBody) getBody()).getUrl();
            sendImgMsg(context, msg, sendMsgCallBack);
        }
    }

    private interface SendMsgCallBack{
        void onSuccess(ChatMessage message);
        void onFailed(ChatMessage message);
    }
    //发送文字消息
    public void sendMsg(Context context, String msg, final SendMsgCallBack callBack) {
        final ChatMessage message = this;
        //由于后台垃圾导致上传流程变更 先上传服务器成功后拿url 再上传给signalr 恶心
        GroupsUtils.senMessageChat(context, msg, new SwitchGroupIdCallBack() {
            @Override
            public void OnSeccess() {
                callBack.onSuccess(message);
                //更新自身 去掉菊花
                LogUtil.d("----OnSeccess--发送文字成功--->");
            }

            @Override
            public void OnFail() {
                callBack.onFailed(message);
                //更新自身 显示感叹号
                LogUtil.d("----OnFail--发送文字失败--->");


            }
        });

    }


    public void sendImgMsg(Context context, String path, final SendMsgCallBack callBack) {
        final ChatMessage message = this;
        new GroupsUtils().sendImgMessageChat2(context, path, new SwitchGroupIdCallBack() {
            @Override
            public void OnSeccess() {
                callBack.onSuccess(message);
                //更新自身 ui
                LogUtil.d("----OnSeccess--发送图片成功--->");
            }

            @Override
            public void OnFail() {
                callBack.onFailed(message);
                //更新自身 ui
                LogUtil.d("----OnFail--发送图片失败--->");
            }
        });

    }


}
