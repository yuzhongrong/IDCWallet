package com.yy.chat;


import android.content.Context;
import android.support.annotation.IntDef;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yy.chat.widget.MessageList;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yiyang
 */
public interface IMessage<MessageBody> extends MultiItemEntity {
    /**发送者的id*/
    public String getFrom();

    /**消息体，在Chatrow的onSetUpView(MessageBody)会回调*/
    public MessageBody getBody();

    /**接收者的id*/
    public String getTo();

    /**消息id*/
    public String getId();

    /**消息的方向，true为发送，false为接收*/
    public boolean isDirction();

    /**显示在chatrow的时间*/
    public String getTime();

    /**消息创建的时间戳*/
    public long timeStamp();

    /**该条消息对应的头像*/
    public String getAvatar();

    /**昵称*/
    public String getNickname();

    /**消息状态，具体参考{@link Status}*/
    @Status
    int getStatus();

    /**
     * 获取chatrow type，如果返回0则默认根据消息体类型来显示已经定义好的文本，图片类型
     * 如果需要自定义chatrow, 必须返回从1开始有序排列，并在代码中设置{@link MessageList#setChatRowProvider(com.yy.chat.widget.chatrow.CustomChatRowProvider)}
     * @return
     */
    int getItemType();

    public static final int DEFAULT_ITEM_TYPE_COUNT = 5;
    /**新类型*/
    public static final int TYPE_NEW = -1;
    /**文本类型*/
    public static final int TYPE_TEXT_SEND = 1;
    public static final int TYPE_TEXT_RECV = 2;
    /**图片类型*/
    public static final int TYPE_IMG_SEND = 3;
    public static final int TYPE_IMG_RECV = 4;

    @IntDef({TYPE_NEW,
            TYPE_TEXT_SEND,
            TYPE_TEXT_RECV,
            TYPE_IMG_SEND,
            TYPE_IMG_RECV,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageType {
    }


    /**创建*/
    public static final int STATUS_CREATE = 0;
    /**发送中或者加载中*/
    public static final int STATUS_INPROGRESS = 1;
    /**成功*/
    public static final int STATUS_SUCCESS = 2;
    /**失败*/
    public static final int STATUS_FAIL = 3;

    @IntDef({STATUS_INPROGRESS,
            STATUS_SUCCESS,
            STATUS_CREATE,
            STATUS_FAIL,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }

    /**在消息状态为创建状态下，会回调*/
    public void handleMsg(Context context, MessageListAdapter messageListAdapter);

    class StatusHelper {
        public interface StatusCallBack {
            void onSuccess();

            void onError();

            void onProgress(int progress);
        }

        private static final Map<String, StatusHelper> mHelpers = new HashMap();

        public static StatusHelper get(IMessage message) {
            StatusHelper statusHelper = mHelpers.get(message.toString());
            if (statusHelper == null) {
                statusHelper = new StatusHelper();
                mHelpers.put(message.toString(), statusHelper);
            }
            return statusHelper;
        }
        public static void clear(){
            mHelpers.clear();
        }

        private StatusCallBack mStatusCallBack;

        public void setStatusCallBack(StatusCallBack statusCallBack) {
            mStatusCallBack = statusCallBack;
        }

        public StatusCallBack getStatusCallBack(){
            return mStatusCallBack;
        }
    }
}
