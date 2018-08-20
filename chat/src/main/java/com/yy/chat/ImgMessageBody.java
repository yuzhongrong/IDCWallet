package com.yy.chat;

/**
 *
 * @author yiyang
 */
public class ImgMessageBody extends MessageBody {
    private String url;

    public ImgMessageBody(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
