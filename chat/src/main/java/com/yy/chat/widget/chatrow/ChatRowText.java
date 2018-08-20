package com.yy.chat.widget.chatrow;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.yy.chat.R;
import com.yy.chat.TextMessageBody;


/**
 *
 * @author yiyang
 */
public class ChatRowText extends ChatRow<TextMessageBody> {


    private TextView mTvContent;

    public ChatRowText(Context context, @LayoutRes int res) {
        super(context, res);
    }

    @Override
    protected void onFindViewById() {
        mTvContent = mView.findViewById(R.id.tv_chatcontent);
    }

    @Override
    public void onSetUpView(TextMessageBody textMessageBody) {
        mTvContent.setText(textMessageBody.getText());
    }
}
