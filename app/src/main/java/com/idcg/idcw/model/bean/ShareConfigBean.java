package com.idcg.idcw.model.bean;

/**
 * Created by hpz on 2018/7/10.
 */

public class ShareConfigBean {

    private int type;
    private String link;
    private boolean is_show;
    private int id;
    private int lang;
    private String lable_text;
    private String label_text2;
    private String icon;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isIs_show() {
        return is_show;
    }

    public void setIs_show(boolean is_show) {
        this.is_show = is_show;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getLable_text() {
        return lable_text;
    }

    public void setLable_text(String lable_text) {
        this.lable_text = lable_text;
    }

    public String getLabel_text2() {
        return label_text2;
    }

    public void setLabel_text2(String label_text2) {
        this.label_text2 = label_text2;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
