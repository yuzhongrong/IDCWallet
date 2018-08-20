package foxidcw.android.idcw.otc.model.beans;

/**
 * Created by yuzhongrong on 2018/5/4.
 * 订单info
 */

public class OTCOrderInfoBean {
    private int type;//买单还是卖单
    private int state;//订单状态

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
