package foxidcw.android.idcw.otc.model.beans;

/**
 *
 * @project name : FoxIDCW
 * @class name :   OTCGetOtcOrdersBean
 * @package name : foxidcw.android.idcw.otc.model.beans
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/7 21:11
 * @describe :     实体类
 *
 */
public class OTCGetOtcSettingBean {


    /**
     * AllowCancelOrderCount : 0
     * AllowCancelOrderDuration : 0
     * CancelOrderForbidTradeDuration : 0
     * AppealFailForbidTradeDuration : 0
     * AllowQuotePriceDuration : 0
     * ConfirmTransferDuration : 0
     * AllowDelayDuration : 0
     * ConfirmReceivablesDuration : 0
     * HandlerAppealDuration : 0
     * UploadCertificateDuration : 0
     * MaxHandlerOrderCount : 0
     */

    private int AllowCancelOrderCount;
    private int AllowCancelOrderDuration;
    private int CancelOrderForbidTradeDuration;
    private int AppealFailForbidTradeDuration;
    private int AllowQuotePriceDuration;
    private int ConfirmTransferDuration;
    private int AllowDelayDuration;
    private int ConfirmReceivablesDuration;
    private int HandlerAppealDuration;
    private int UploadCertificateDuration;
    private int MaxHandlerOrderCount;

    public int getAllowCancelOrderCount() { return AllowCancelOrderCount;}

    public void setAllowCancelOrderCount(int AllowCancelOrderCount) { this.AllowCancelOrderCount = AllowCancelOrderCount;}

    public int getAllowCancelOrderDuration() { return AllowCancelOrderDuration;}

    public void setAllowCancelOrderDuration(int AllowCancelOrderDuration) { this.AllowCancelOrderDuration = AllowCancelOrderDuration;}

    public int getCancelOrderForbidTradeDuration() { return CancelOrderForbidTradeDuration;}

    public void setCancelOrderForbidTradeDuration(int CancelOrderForbidTradeDuration) { this.CancelOrderForbidTradeDuration = CancelOrderForbidTradeDuration;}

    public int getAppealFailForbidTradeDuration() { return AppealFailForbidTradeDuration;}

    public void setAppealFailForbidTradeDuration(int AppealFailForbidTradeDuration) { this.AppealFailForbidTradeDuration = AppealFailForbidTradeDuration;}

    public int getAllowQuotePriceDuration() { return AllowQuotePriceDuration;}

    public void setAllowQuotePriceDuration(int AllowQuotePriceDuration) { this.AllowQuotePriceDuration = AllowQuotePriceDuration;}

    public int getConfirmTransferDuration() { return ConfirmTransferDuration;}

    public void setConfirmTransferDuration(int ConfirmTransferDuration) { this.ConfirmTransferDuration = ConfirmTransferDuration;}

    public int getAllowDelayDuration() { return AllowDelayDuration;}

    public void setAllowDelayDuration(int AllowDelayDuration) { this.AllowDelayDuration = AllowDelayDuration;}

    public int getConfirmReceivablesDuration() { return ConfirmReceivablesDuration;}

    public void setConfirmReceivablesDuration(int ConfirmReceivablesDuration) { this.ConfirmReceivablesDuration = ConfirmReceivablesDuration;}

    public int getHandlerAppealDuration() { return HandlerAppealDuration;}

    public void setHandlerAppealDuration(int HandlerAppealDuration) { this.HandlerAppealDuration = HandlerAppealDuration;}

    public int getUploadCertificateDuration() { return UploadCertificateDuration;}

    public void setUploadCertificateDuration(int UploadCertificateDuration) { this.UploadCertificateDuration = UploadCertificateDuration;}

    public int getMaxHandlerOrderCount() { return MaxHandlerOrderCount;}

    public void setMaxHandlerOrderCount(int MaxHandlerOrderCount) { this.MaxHandlerOrderCount = MaxHandlerOrderCount;}
}
