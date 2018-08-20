package com.yy.chat;

/**
 *
 * @author yiyang
 */
public class TextMessageBody extends MessageBody {
    private String text;

    public TextMessageBody(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
