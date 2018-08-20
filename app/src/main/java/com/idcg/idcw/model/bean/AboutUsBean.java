package com.idcg.idcw.model.bean;

import java.io.Serializable;

/**
 * Created by hpz on 2018/5/30.
 */

public class AboutUsBean implements Serializable {
    private int id;
    private String title;
    private String icon;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
