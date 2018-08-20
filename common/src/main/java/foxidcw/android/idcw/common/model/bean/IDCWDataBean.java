package foxidcw.android.idcw.common.model.bean;

import java.io.Serializable;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.model.bean
 * 备注消息：
 * 修改时间：2018/3/26 17:17
 **/

public class IDCWDataBean implements Serializable {
    private String sign; // 签名
    private String currency; // 币种
    private String notify_url; // 回调地址
    private String amount; // 数量
    private String time_span; // 时间戳
    private String trans_id; // 订单号
    private String appId = "idc2454654387"; // 商户ID  默认为瑞士会
    private int type = 0; // 默认为0 支付和充值

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime_span() {
        return time_span;
    }

    public void setTime_span(String time_span) {
        this.time_span = time_span;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "IDCWDataBean{" +
                "sign='" + sign + '\'' +
                ", currency='" + currency + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", amount='" + amount + '\'' +
                ", time_span='" + time_span + '\'' +
                ", trans_id='" + trans_id + '\'' +
                ", appId='" + appId + '\'' +
                ", type=" + type +
                '}';
    }
}
