package foxidcw.android.idcw.otc.model;

/**
 *承兑商报单状态
 * @author yiyang
 */
public class OfferOrderStatus {
    /// <summary>
    /// 待确认
    /// </summary>
    public static final int WaitConfirm = 1;
            /// <summary>
            /// 取消
            /// </summary>
    public static final int Cancel = 2;
            /// <summary>
            /// 待转账
            /// </summary>
    public static final int WaitTransfer = 3;
            /// <summary>
            /// 完成
            /// </summary>
    public static final int Finish = 4;
            /// <summary>
            /// 无效
            /// </summary>
    public static final int Invalid = 5;
            /// <summary>
            /// 报价被选中
            /// </summary>
    public static final int Confirm = 6;
            /// <summary>
            /// 已经支付
            /// </summary>
    public static final int Pay = 7;
            /// <summary>
            /// 通知承兑商
            /// </summary>
    public static final int NoticeAcceptant = 8;

}
