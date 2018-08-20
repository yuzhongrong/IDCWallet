package foxidcw.android.idcw.otc.api;

import com.cjwsc.idcm.net.response.HttpResponse;

import java.util.List;

import foxidcw.android.idcw.otc.activitys.OTCAddCurrencyActivity;
import foxidcw.android.idcw.otc.model.beans.OTCCoinCurrExchangeBean;
import foxidcw.android.idcw.otc.model.beans.OTCAddAmountParam;
import foxidcw.android.idcw.otc.model.beans.OTCAddCurrParam;
import foxidcw.android.idcw.otc.model.beans.OTCChatHistoryBean;
import foxidcw.android.idcw.otc.model.beans.OTCCoinListBean;
import foxidcw.android.idcw.otc.model.beans.OTCConfirmOrderResBean;
import foxidcw.android.idcw.otc.model.beans.OTCCurrentStepBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositBalanceListBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositMgListBean;
import foxidcw.android.idcw.otc.model.beans.OTCDepositWaterBean;
import foxidcw.android.idcw.otc.model.beans.OTCDiscoveryBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeBuyBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetExchangeSellBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcSettingBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcTradeSettingBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import foxidcw.android.idcw.otc.model.beans.OTCLocalCurrencyResBean;
import foxidcw.android.idcw.otc.model.beans.OTCNewBalanceBean;
import foxidcw.android.idcw.otc.model.beans.OTCNewOrderNoticeAcceptantBean;
import foxidcw.android.idcw.otc.model.beans.OTCOrderQuotePriceBean;
import foxidcw.android.idcw.otc.model.beans.OTCRechargeDepositParam;
import foxidcw.android.idcw.otc.model.beans.OTCRechargeResultBean;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.beans.OTCSelectPayListBean;
import foxidcw.android.idcw.otc.model.beans.OTCWithdrawResultBean;
import foxidcw.android.idcw.otc.model.beans.OTCSendOrderDTOBean;
import foxidcw.android.idcw.otc.model.beans.OTCWalletAssetBean;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import foxidcw.android.idcw.otc.model.params.OTCCurrentStepParams;
import foxidcw.android.idcw.otc.model.params.OTCDepositWaterParams;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOfficeListParam;
import foxidcw.android.idcw.otc.model.params.OTCGetOtcOrdersParams;
import foxidcw.android.idcw.otc.model.params.OTCOrderIdQuoteIdReqParams;
import foxidcw.android.idcw.otc.model.params.OTCQuotePriceReqParams;
import foxidcw.android.idcw.otc.model.params.OTCReqSyncAddressParams;
import foxidcw.android.idcw.otc.model.params.OTCSendOrderParams;
import foxidcw.android.idcw.otc.model.params.OTCWithdrawDepositParam;
import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hpz on 2018/5/2.
 */

public interface OTCHttpApi {

    //------------------------承兑商----------------------------->
    String get_otc_coin_list = "/api/OtcAcceptant/GetOtcCoinList";//获取币种列表
    String get_otc_add_coin_list = "/api/OtcAcceptant/ExchangeCoinChange";//添加承兑币种上限
    String get_otc_legal_curr_list = "/api/OtcAcceptant/GetOtcLocalCurrencyList";//获取法币列表
    String get_otc_buy_curr_list = "/api/OtcAcceptant/GetExchangeBuyList";//获取承兑买币信息列表
    String get_otc_sell_curr_list = "/api/OtcAcceptant/GetExchangeSellList";//获取承兑卖币信息列表
    String get_otc_add_amount_list = "/api/OtcAcceptant/ExchangeLocalCurrencyChange";//添加或编辑币种及资金量
    String check_acceptstance_pay = "/api/OtcAcceptant/CheckAcceptant";//校验承兑商是否有设置支付方式
    String remove_otc_add_curr_list = "/api/OtcAcceptant/ExchangeCoinRemove";//移除币种及限额
    String get_otc_pay_type_list = "/api/OtcAcceptant/GetExchangePayModeList";//获取支付方式列表
    String remove_otc_legal_list = "/api/OtcAcceptant/ExchangeLocalCurrencyRemove";//移除法币资金量
    String get_otc_add_pay_list = "/api/OtcAcceptant/ExchangePayModeAdd";//添加法币及支付方式列表
    String remove_otc_add_pay_list = "/api/OtcAcceptant/ExchangePayModeRemove";//移除法币及支付方式列表
    //------------------------承兑商----------------------------->

    String money_bag_list_url = "/api/Wallet/GetWalletList?added=true";//获取钱包列表
    String GetNewCoinList = "/api/Exchange/GetNewCoinList";//获取币列表
    String OTC_GET_ALL_CURRENCY_LIST = "/api/OtcAcceptant/GetOtcLocalCurrencyList"; // 法币币种列表
    String GET_PAYMENT_MODES = "/api/Payment/GetPaymentModeManagement"; // 添加支付方式获取联动属性
    String PAYMENT_MODE_CHANGE = "/api/Payment/PaymentModeChange"; // 添加或编辑支付方式
    String PAYMENT_MODE_REMOVE = "/api/Payment/PaymentModeRemove";//删除支付方式
    String GET_PAYMENT_MODE_LIST = "/api/Payment/GetPaymentModeList"; // 获取列表

    String GET_CHAT_HISTORY = "/api/OtcChat/GetChatHistory";//获取聊天历史
    String GET_QUOTE_ORDERS = "/api/OtcOfferOrder/GetQuoteOrders"; // 获取订单报价单
    String GET_OFFER_LIST = "/api/OtcOfferOrder/GetOtcOfferList"; // 获取承兑商报价list

    String check_otc_if_withdraw = "/api/OtcAcceptant/CheckWithdraw";//校验能否提取保证金
    String get_otc_deposit_manager_list = "/api/OtcAcceptant/GetDepositList";//保证金管理列表
    String get_otc_deposit_balance_list = "/api/OtcAcceptant/GetAcceptantInfo";//获取承兑商状态和保证金列表
    String get_otc_deposit_water_list = "/api/OtcAcceptant/GetDepositWastebookList";//获取保证金流水列表
    String get_wallet_balance = "/api/Wallet/GetBalanceByCoin";//获取保证金最新余额
    String otc_recharge_deposit = "/api/OtcAcceptant/Recharge";//充值保证金
    String otc_withdraw_deposit = "/api/OtcAcceptant/Withdraw";//提取保证金
    String otc_get_water_curr_deposit = "/api/OtcAcceptant/GetWithdrawCoinList";//提取保证金的获取币种余额和列表

    String otc_order_detail = "/api/OtcTrade/GetOtcOrder";//获取订单详情
    String otc_get_current_step = "/api/OtcAcceptant/GetCurrentStep";//获取当前申请步骤
    String otc_set_current_step = "/api/OtcAcceptant/SetCurrentStep";//设置当前申请步骤
    String otc_verify_sync_address = "/api/Wallet/ValidComplicatedAddressAsync";//校验特殊字符的地址
    String otc_check_address_url = "/api/Wallet/ValidAddress";//校验转账地址是否正确

    /*-----------------------交易详情所有操作[取消交易，已转账，上传凭证，申诉，交易完成，等等]------------------------------------*/

    /*-----------------------买方-----------------------------*/
    String otc_confirmpay_option = "/api/OtcTrade/SetTransfer";//已转账

    String otc_certificate_switch_option = "/api/OtcTrade/SetAppealing";//申诉----->上传凭证


    /*-----------------------卖方-----------------------------*/
    String otc_delay_option = "/api/OtcTrade/SetDelayConfirm";//延时交易
    String otc_applyappeal_option = "/api/OtcTrade/ApplyAppeal";//卖家提交申诉
    String AGREE_REFUND = "/api/OtcTrade/AgreeRefund"; // 同意退币


    String otc_confirm_money_option = "/api/OtcTrade/ConfirmArrived";//确认收款


    String QUOTE_PRICE = "/api/OtcOfferOrder/QuotePrice";// 承兑商报价
    String otc_deposit_manager_sort_list = "/api/OtcAcceptant/SetPaySequence";//保证金管理列表排序

    String get_exchange_balance = "/api/Exchange/GetBalanceByCoin";//获取币币兑换余额
    String CONFIRM_OFFER_ORDER = "/api/OtcTrade/ConfirmOfferOrder"; // 確認报价信息
    String CANCEL_ORDER = "/api/OtcTrade/CancelOrder"; // 撤销订单

    String get_discovery_list = "/api/Discover/ShowAsync";//发现页面
    String get_dapp_is_read = "/api/Discover/DappIsRead";//判断Dapp是否已读
    String set_dapp_is_read = "/api/Discover/MarkDappAsRead";//设置Dapp已读
    String get_signalr_url = "/api/OtcChat/GetSignalrUrl";//获取signalr地址

    String get_deposit_otc_coin_list = "/api/OtcAcceptant/GetOtcMarginCoinList";//获取保证金币种列表

    String  request_cancel_quoteorder="/api/OtcOfferOrder/CancelQuoteOrder";//取消申报订单


    //------------------------承兑商----------------------------->
    @POST(get_otc_coin_list)
    Flowable<HttpResponse<List<OTCCoinListBean>>> requestAcceptstantCoinList();//获取承兑币种

    @POST(get_otc_add_coin_list)
    Flowable<HttpResponse<Boolean>> requestAddCurrList(@Body OTCAddCurrParam otcAddCurrParam);

    @POST(get_otc_legal_curr_list)
    Flowable<HttpResponse<List<OTCCoinListBean>>> requestAcceptstantLegalList();//获取法币列表

    @POST(get_otc_buy_curr_list)
    Flowable<HttpResponse<OTCGetExchangeBuyBean>> requestPhraseData();

    @POST(get_otc_add_amount_list)
    Flowable<HttpResponse<Boolean>> requestAddAmountList(@Body OTCAddAmountParam otcAddAmountParam);//添加或编辑币种及资金量

    @POST(get_otc_sell_curr_list)
    Flowable<HttpResponse<OTCGetExchangeSellBean>> requestSellCurrData();//获取承兑商卖币信息

    @POST(check_acceptstance_pay)
    Flowable<HttpResponse<Boolean>> requestCheckPay();//校验承兑商是否有设置支付方式

    //@FormUrlEncoded
    @POST(remove_otc_add_curr_list)
    Flowable<HttpResponse<Object>> requestRemoveCurr(@Body OTCRemoveBean otcRemoveBean);//移除币种及限额

    @POST(get_otc_pay_type_list)
    Flowable<HttpResponse<List<OTCSelectPayListBean>>> requestPayTypeList();//获取支付方式列表

    @POST(remove_otc_legal_list)
    Flowable<HttpResponse<Object>> requestRemoveLegal(@Body OTCRemoveBean otcRemoveBean);//移除法币

    @POST(get_otc_add_pay_list)
    Flowable<HttpResponse<Boolean>> requestAddPayList(@Body OTCAddCurrencyActivity.CacheBean otcRemoveBean);

    @POST(remove_otc_add_pay_list)
    Flowable<HttpResponse<Object>> requestRemovePay(@Body OTCRemoveBean otcRemoveBean);//移除支付方式
    //------------------------承兑商----------------------------->

    @POST(GetNewCoinList)
    Flowable<HttpResponse<OTCCoinCurrExchangeBean>> requestNewCoinList();

    //获取钱包列表
    @GET(money_bag_list_url)
    Flowable<HttpResponse<List<OTCWalletAssetBean>>> getWalletList();

    // 获取法币币种列表
    @POST(OTC_GET_ALL_CURRENCY_LIST)
    Flowable<HttpResponse<List<OTCLocalCurrencyResBean>>> getOTCLocalCurrencyList();

    // 獲取支付方式获取联动属性
    @POST(GET_PAYMENT_MODES)
    Flowable<HttpResponse<OTCGetPaymentModeManagementResBean>> getPaymentModeManagement();

    // 添加或编辑支付方式
    @POST(PAYMENT_MODE_CHANGE)
    Flowable<HttpResponse<Object>> paymentModeChange(@Body OTCAddOrEditReqParams reqParams);

    // 删除支付方式
    @POST(PAYMENT_MODE_REMOVE)
    Flowable<HttpResponse<Object>> paymentModeRemove(@Body OTCRemoveBean reqBean);

    // 获取支付方式列表
    @POST(GET_PAYMENT_MODE_LIST)
    Flowable<HttpResponse<List<OTCAddOrEditReqParams>>> getPaymentModeList();

    // 获取OTC交易相关设置
    @Headers("Cache-Control:FORCE_NETWORK")
    @POST("/api/OtcTradeSetting/GetOtcTradeSetting")
    Flowable<HttpResponse<OTCGetOtcTradeSettingBean>> getOtcTradeSetting();

    // 下OTC订单
    @POST("/api/OtcTrade/SendOrder")
    Flowable<HttpResponse<OTCSendOrderDTOBean>> sendOrder(@Body OTCSendOrderParams reqParam);

    //获取聊天历史
    @GET(GET_CHAT_HISTORY)
    Flowable<HttpResponse<List<OTCChatHistoryBean>>> getChatHistory(@Query("groupName") String groupid);

    // 获取订单报价单
    @FormUrlEncoded
    @POST(GET_QUOTE_ORDERS)
    Flowable<HttpResponse<List<OTCOrderQuotePriceBean>>> getQuoteOrders(@Field("orderId") String body);

    //获取OTC订单信息
    @POST("/api/OtcTrade/GetOtcOrders")
    Flowable<HttpResponse<List<OTCGetOtcOrdersBean>>> getOtcOrders(@Body OTCGetOtcOrdersParams otcGetOtcOrdersParams);

    //获取OTC订单信息
    @Headers("Cache-Control:FORCE_NETWORK")
    @GET("/api/OtcSysSetting/GetOtcSetting")
    Flowable<HttpResponse<OTCGetOtcSettingBean>> getOtcSetting();

    @POST(check_otc_if_withdraw)
    Flowable<HttpResponse<Boolean>> requestCheckWithdraw();//校验能否提取保证金

    @POST(get_otc_deposit_manager_list)
    Flowable<HttpResponse<List<OTCDepositMgListBean>>> requestGetDepositManagerList();//获取保证金管理列表

    @POST(get_otc_deposit_balance_list)
    Flowable<HttpResponse<OTCDepositBalanceListBean>> requestGetDepositBalanceList();//获取承兑商状态和保证金列表

    @POST(get_otc_deposit_water_list)
    Flowable<HttpResponse<List<OTCDepositWaterBean>>> requestGetDepositWaterList(@Body OTCDepositWaterParams otcRemoveBean);//移除法币

    @GET(get_wallet_balance)
    Flowable<HttpResponse<OTCDepositBalanceBean>> requestNewBalance(@Query("currency") String currency);//获取最新余额

    @POST(otc_recharge_deposit)
    Flowable<HttpResponse<OTCRechargeResultBean>> requestRechargeDeposit(@Body OTCRechargeDepositParam otcRechargeDepositParam);//充值保证金

    @POST(otc_withdraw_deposit)
    Flowable<HttpResponse<OTCWithdrawResultBean>> requestWithdrawDeposit(@Body OTCWithdrawDepositParam otcWithdrawDepositParam);//提取保证金

    // 获取承兑商历史记录单据
    @POST(GET_OFFER_LIST)
    Flowable<HttpResponse<List<OTCNewOrderNoticeAcceptantBean>>> getOTCOfferList(@Body OTCGetOtcOfficeListParam otcGetOtcOfficeListParam);

    //确认已付款
    @POST(otc_confirmpay_option)
    Flowable<HttpResponse<Object>> requestConfirmPay(@Query("orderId") String orderId);


    //延长交易时间
    @POST(otc_delay_option)
    Flowable<HttpResponse<Object>> requestDelayTrade(@Query("orderId") String orderId);

    //获取订单详情form格式
    @FormUrlEncoded
    @POST(otc_order_detail)
    Flowable<HttpResponse<OTCGetOtcOrdersBean>> requestOrderDetailrDetail(@Field("orderId") String orderId);


    // 报价界面
    @POST(QUOTE_PRICE)
    Flowable<HttpResponse<OTCQuotePriceReqParams>> quotePrice(@Body OTCQuotePriceReqParams reqParams);

    @POST(otc_get_water_curr_deposit)
    Flowable<HttpResponse<List<OTCCoinListBean>>> requestAcceptstantgetCurrWaterList();//获取保证金的币种及列表余额

    @POST(otc_deposit_manager_sort_list)
    Flowable<HttpResponse<Object>> requestSetSortDepositList(@Body RequestBody requestBody);//设置保证列表顺序

    // 确认报价信息  交易
    @POST(CONFIRM_OFFER_ORDER)
    Flowable<HttpResponse<OTCConfirmOrderResBean>> confirmOfferOrder(@Body OTCOrderIdQuoteIdReqParams reqParams);

    // 取消訂單
    @FormUrlEncoded
    @POST(CANCEL_ORDER)
    Flowable<HttpResponse<OTCConfirmOrderResBean>> cancelOrder(@Field("orderId") String orderId);

    //获取币币闪兑余额
    @Headers("Cache-Control:FORCE_NETWORK")
    @GET(get_exchange_balance)
    Flowable<HttpResponse<OTCNewBalanceBean>> requestExchangeBalance(@Query("currency") String currency);


    //卖家确认收款

    @POST(otc_confirm_money_option)
    Flowable<HttpResponse<Object>> requestConfirmMoneyTrade(@Query("orderId") String orderId);

    @POST(otc_get_current_step)
    Flowable<HttpResponse<OTCCurrentStepBean>> requestGetCurrentStep();//

    @POST(otc_set_current_step)
    Flowable<HttpResponse<Object>> requestSetCurrentStep(@Body OTCCurrentStepParams otcCurrentStepParams);//设置当前步骤

    @POST(otc_applyappeal_option)
    Flowable<HttpResponse<Object>> requestApplyappealTrade(@Query("orderId") String orderId);


    @POST(otc_certificate_switch_option)
    Flowable<HttpResponse<Object>> requestAppeal2UploadCerfiticate(@Query("orderId") String orderId);

    @POST(otc_verify_sync_address)
    Flowable<HttpResponse<String>> requestSyncAddress(@Body OTCReqSyncAddressParams otcReqSyncAddressParams);

    @POST(otc_check_address_url)
    Flowable<HttpResponse<Boolean>> requestCheckAddress(@Body OTCReqSyncAddressParams otcReqSyncAddressParams);

    @POST(AGREE_REFUND)
    Flowable<HttpResponse<Object>> agreeRefund(@Query("orderId") String orderId);

    //发现页面
    @GET(get_discovery_list)
    Flowable<HttpResponse<OTCDiscoveryBean>> requestDiscoveryList(@Query("lang") String lang, @Query("client") String client);

    //判断Dapp是否已读
    @GET(get_dapp_is_read)
    Flowable<HttpResponse<Boolean>> requestDiscoveryIfRead(@Query("dappId") String dappId);

    //设置Dapp已读
    @GET(set_dapp_is_read)
    Flowable<HttpResponse<Boolean>> requestDiscoverySetRead(@Query("dappId") String dappId);

    //设置Dapp已读
    @POST(get_signalr_url)
    Flowable<HttpResponse<Object>> requestDiscoverySetRead();

    @POST(get_deposit_otc_coin_list)
    Flowable<HttpResponse<List<OTCCoinListBean>>> requestDepositCoinList();//获取保证金币种


    @POST(request_cancel_quoteorder)
    Flowable<HttpResponse<Object>> requestCancelQuoteorder(@Query("id") int orderId);

}
