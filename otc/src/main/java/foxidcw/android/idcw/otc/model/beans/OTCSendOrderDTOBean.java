package foxidcw.android.idcw.otc.model.beans;

/**
 *
 * @project name : FoxIDCW
 * @class name :   OTCSendOrderDTOBean
 * @package name : foxidcw.android.idcw.otc.model.beans
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/6 16:28
 * @describe :     OTC订单返回Bean
 *
 */
public class OTCSendOrderDTOBean {
//    OrderId (integer, optional): 订单ID ,
//    OrderNO (string, optional): 订单编号 ,
//    QuoteOrderExpired (string, optional): 承兑商报价到期时间 ,
//    ForbidExpired (string, optional): 解禁下单时间

    private int OrderId;
    private String OrderNO;
    private String QuoteOrderExpired;
    private String ForbidExpired;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getOrderNO() {
        return OrderNO;
    }

    public void setOrderNO(String orderNO) {
        OrderNO = orderNO;
    }

    public String getQuoteOrderExpired() {
        return QuoteOrderExpired;
    }

    public void setQuoteOrderExpired(String quoteOrderExpired) {
        QuoteOrderExpired = quoteOrderExpired;
    }

    public String getForbidExpired() {
        return ForbidExpired;
    }

    public void setForbidExpired(String forbidExpired) {
        ForbidExpired = forbidExpired;
    }

    @Override
    public String toString() {
        return "OTCSendOrderDTOBean{" + "OrderId=" + OrderId + ", OrderNO='" + OrderNO + '\'' + ", QuoteOrderExpired='" + QuoteOrderExpired + '\'' + ", ForbidExpired='" + ForbidExpired + '\'' + '}';
    }
}
