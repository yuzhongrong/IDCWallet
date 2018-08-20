package foxidcw.android.idcw.otc.model.params;

/**
 * Created by hpz on 2018/5/10.
 */

public class OTCDepositWaterParams {
    /**
     * PageIndex : 0
     * PageSize : 0
     * CoinId : 0
     * UserId : 0
     */

    private int PageIndex;
    private int PageSize;
    private int CoinId;
    private int UserId;

    public OTCDepositWaterParams(int PageIndex,int PageSize, int CoinId, int UserId) {
        this.PageIndex = PageIndex;
        this.PageSize = PageSize;
        this.CoinId = CoinId;
        this.UserId = UserId;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int PageIndex) {
        this.PageIndex = PageIndex;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getCoinId() {
        return CoinId;
    }

    public void setCoinId(int CoinId) {
        this.CoinId = CoinId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }
}
