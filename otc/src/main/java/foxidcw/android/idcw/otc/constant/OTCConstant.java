package foxidcw.android.idcw.otc.constant;

/**
 * Created by hpz on 2018/4/21.
 */

public class OTCConstant {

     /*-----------------------------------------------页面传递值的key-格式：key.类型.bean---------------------------------*/

    public static final String NOTICEORDERBEAN = "key.order.noticeorderbean"; //推送到承兑商订单bean






    /*-----------------------------------------------页面路径----------------------------------*/
    public static final String APPLYACCEPT = "/otc/activitys/OTCAcceptanceApplyActivity"; //承兑商申请页面
    public static final String APPLYACCEPTREAD = "/otc/activitys/OTCAcceptanceReadActivity"; //承兑商申请阅读页面
    public static final String DEPOSITRULES = "/otc/activitys/OTCDepositTradeActivity"; //保证金规则页面
    public static final String ADDCURRENCY = "/otc/activitys/OTCAddCurrencyActivity"; //申请承兑商添加币种和支付方式
    public static final String ADDCHANGEXCURR = "/otc/activitys/OTCAddBuyCurrActivity"; //添加承兑换币
    public static final String SELECTCURR = "/otc/activitys/OTCSelectCurrActivity"; //添加承兑换币选择币种页面
    public static final String APPLYSELLCURR = "/otc/activitys/OTCApplySellCurrActivity"; //申请承兑商添加卖币
    public static final String RECHARGEDEPOSIT = "/otc/activitys/OTCRechargeDepositActivity"; //申请承兑商充值保证金页面
    public static final String DEPOSITSUCCESS = "/otc/activitys/OTCOpenSuccessActivity"; //申请承兑商充值保证金成功页面
    public static final String DEPOSITBALANCE = "/otc/activitys/OTCDepositBalanceActivity"; //承兑商保证金余额列表页面
    public static final String DEPOSITMANAGER = "/otc/activitys/OTCDepositManagerActivity"; //保证金管理页面
    public static final String DEPOSITWATER = "/otc/activitys/OTCDepositWaterActivity"; //保证金流水详情页面
    public static final String WITHDRAWDEPOSIT = "/otc/activitys/OTCWithdrawDepositActivity"; //提取保证金页面
    public static final String LEGALTENDER = "/otc/activitys/OTCLegalTenderActivity"; //添加法币资金页面
    public static final String RECHARGEBALANCE = "/otc/activitys/OTCRechargeDepositBalanceActivity"; //充值保证金

    public static final String PAY_METHOD_MANAGER = "/otc/activitys/OTCPayMethodManagerActivity"; //支付管理界面
    public static final String TRANSACTION_RULE = "/otc/activitys/OTCTransactionRuleActivity"; //OTC买卖币规则
    public static final String PAY_METHOD_ADD_OR_EDIT = "/otc/activitys/OTCAddOrEditPayMethodActivity"; //添加或者修改支付方式
    public static final String TRADE_DETAIL = "/otc/activitys/OTCTradeDetailActivity"; //OTC交易详情

    public static final String CONSOLE = "/otc/activitys/OTCConsoleActivity"; //操作台界面
    public static final String QUOTATION = "/otc/activitys/OTCQuotationActivity"; //确认买入卖出界面
    public static final String RULEWEBVIEW = "/otc/activitys/OTCWebviewActivity"; //保证金规则webview
    public static final String USERAGREE = "/otc/activitys/OTCUserAgreementActivity"; //发现的游戏协议页面

}
