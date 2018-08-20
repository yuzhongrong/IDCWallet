package com.yy.chat.widget;

import com.yy.chat.IMessage;

/**
 *
 * @author yiyang
 */
public interface OnItemChildActionCallBack {
    void onResendClick(IMessage message);

    void onBubbleClick(IMessage message, int position);

    void onUserAvatarClick(IMessage message);
}
