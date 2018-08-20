package com.idcg.idcw.model.params;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.model.params ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/15 16:50
 **/

public class CreateSetPassReqParam extends LanguageReqParam {
    private String user_name;
    private String localCurrencyName;
    private String registerSoucre;
    private String ip;
    private String device_id;
    private String invite_code;
    //private PhraseDataReqAndResParam phrase;

    public CreateSetPassReqParam(String user_name,
                                 String localCurrencyName,
                                 String registerSoucre,
                                 String language,
                                 String language_url,
                                 String ip,
                                 String device_id,
                                 String invite_code) {
        //PhraseDataReqAndResParam phrase) {
        this.user_name = user_name;
        this.localCurrencyName = localCurrencyName;
        this.registerSoucre = registerSoucre;
        this.language = language;
        this.language_url = language_url;
        this.ip = ip;
        this.device_id = device_id;
        this.invite_code = invite_code;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLocalCurrencyName() {
        return localCurrencyName;
    }

    public void setLocalCurrencyName(String localCurrencyName) {
        this.localCurrencyName = localCurrencyName;
    }

    public String getRegisterSoucre() {
        return registerSoucre;
    }

    public void setRegisterSoucre(String registerSoucre) {
        this.registerSoucre = registerSoucre;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    //    public PhraseDataReqAndResParam getPhrase() {
//        return phrase;
//    }
//
//    public void setPhrase(PhraseDataReqAndResParam phrase) {
//        this.phrase = phrase;
//    }
}
