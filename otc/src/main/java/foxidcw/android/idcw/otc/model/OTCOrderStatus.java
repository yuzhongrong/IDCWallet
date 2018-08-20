package foxidcw.android.idcw.otc.model;

/**
 *订单状态
 * @author yiyang
 */
public class   OTCOrderStatus {
    /// <summary>
    /// 待报价
    /// </summary>
    public static final int WaitQuotePrice = 0;
            /// <summary>
            /// 待转账
            /// </summary>
    public static final int WaitPay = 1;
            /// <summary>
            /// 已转账
            /// </summary>
    public static final int Transfered = 2;
            /// <summary>
            /// 已支付
            /// </summary>
    public static final int Paied = 3;
            /// <summary>
            /// 申诉中
            /// </summary>
    public static final int Appeal = 4;
            /// <summary>
            /// 待上传支付凭证
            /// </summary>
    public static final int UploadPayCertficate = 5;
            /// <summary>
            /// 待审核
            /// </summary>
    public static final int WaitApproval = 6;
            /// <summary>
            /// 客服退回
            /// </summary>
    public static final int CustomerRefund = 7;
            /// <summary>
            /// 客服放币
            /// </summary>
    public static final int CustomerPayCoin = 8;
            /// <summary>
            /// 完成
            /// </summary>
    public static final int Finish = 12;
            /// <summary>
            /// 取消
            /// </summary>
    public static final int Cancel = 9;
            /// <summary>
            /// 待退币
            /// </summary>
    public static final int WaitRefundCoin = 10;

            /// <summary>
            /// 同意退还
            /// </summary>
    public static final int AgreeRefund = 11;

    public static final int delay = 13;



}
