package foxidcw.android.idcw.otc.model.params;

import java.io.Serializable;

/**
 *
 * @project name : FoxIDCW
 * @class name :   OTCGetOtcOfficeListParam
 * @package name : foxidcw.android.idcw.otc.model.params
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/10 17:59
 * @describe :     OTC报价信息请求体
 *
 */
public class OTCGetOtcOfficeListParam implements Serializable {
    private String Status = null;
    private String PageSize  = null;
    private String PageIndex  = null;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPageSize() {
        return PageSize;
    }

    public void setPageSize(String pageSize) {
        PageSize = pageSize;
    }

    public String getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(String pageIndex) {
        PageIndex = pageIndex;
    }
}
