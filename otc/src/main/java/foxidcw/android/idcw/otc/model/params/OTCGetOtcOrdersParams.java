package foxidcw.android.idcw.otc.model.params;

import java.io.Serializable;

import io.reactivex.annotations.Nullable;

/**
 *
 * @project name : FoxIDCW
 * @class name :   OTCGetOtcOrdersParams
 * @package name : foxidcw.android.idcw.otc.model.params
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/7 20:56
 * @describe :     获取OTC订单信息
 *
 */
public class OTCGetOtcOrdersParams implements Serializable {

    /**
     * OrderNO : string
     * TradeDirection : 1
     * CoinId : 0
     * StartDate : 2018-05-07T07:34:04.512Z
     * EndDate : 2018-05-07T07:34:04.512Z
     * Status : 0
     * PageSize : 0
     * PageIndex : 0
     */
    @Nullable
    private String OrderNO = null;
    @Nullable
    private String    TradeDirection = null;
    @Nullable
    private String    CoinId = null;
    @Nullable
    private String StartDate = null;
    @Nullable
    private String EndDate = null;
    @Nullable
    private String    Status = null;
    private int    PageSize;
    private int    PageIndex;

    public String getOrderNO() { return OrderNO;}

    public void setOrderNO(String OrderNO) { this.OrderNO = OrderNO;}

    public String getTradeDirection() { return TradeDirection;}

    public void setTradeDirection(String TradeDirection) { this.TradeDirection = TradeDirection;}

    public String getCoinId() { return CoinId;}

    public void setCoinId(String CoinId) { this.CoinId = CoinId;}

    public String getStartDate() { return StartDate;}

    public void setStartDate(String StartDate) { this.StartDate = StartDate;}

    public String getEndDate() { return EndDate;}

    public void setEndDate(String EndDate) { this.EndDate = EndDate;}

    public String getStatus() { return Status;}

    public void setStatus(String Status) { this.Status = Status;}

    public int getPageSize() { return PageSize;}

    public void setPageSize(int PageSize) { this.PageSize = PageSize;}

    public int getPageIndex() { return PageIndex;}

    public void setPageIndex(int PageIndex) { this.PageIndex = PageIndex;}

    @Override
    public String toString() {
        return "OTCGetOtcOrdersParams{" + "OrderNO='" + OrderNO + '\'' + ", TradeDirection=" + TradeDirection + ", CoinId=" + CoinId + ", StartDate='" + StartDate + '\'' + ", EndDate='" + EndDate + '\'' + ", Status=" + Status + ", PageSize=" + PageSize + ", PageIndex=" + PageIndex + '}';
    }
}
