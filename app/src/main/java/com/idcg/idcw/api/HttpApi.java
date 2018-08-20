package com.idcg.idcw.api;

import com.idcg.idcw.model.bean.AboutUsBean;
import com.idcg.idcw.model.bean.AddDataBean;
import com.idcg.idcw.model.bean.CheckNewPinBean;
import com.idcg.idcw.model.bean.CheckStatusBean;
import com.idcg.idcw.model.bean.CoinExchangeBean;
import com.idcg.idcw.model.bean.CoinExchangeRateBean;
import com.idcg.idcw.model.bean.CoinPairBean;
import com.idcg.idcw.model.bean.ExchangeDataBean;
import com.idcg.idcw.model.bean.ExchangeDetailBean;
import com.idcg.idcw.model.bean.ExchangeResultBean;
import com.idcg.idcw.model.bean.HistoryAmountBean;
import com.idcg.idcw.model.bean.NewBalanceBean;
import com.idcg.idcw.model.bean.NoticeResBean;
import com.idcg.idcw.model.bean.NoticeTopBean;
import com.idcg.idcw.model.bean.NotifyMessageBean;
import com.idcg.idcw.model.bean.ReFeeBean;
import com.idcg.idcw.model.bean.ReqSyncAddressParam;
import com.idcg.idcw.model.bean.RunChartDataBean;
import com.idcg.idcw.model.bean.SendTradeBean;
import com.idcg.idcw.model.bean.ShareConfigBean;
import com.idcg.idcw.model.bean.TotalAssetsAmountBean;
import com.idcg.idcw.model.bean.TransMsgBean;
import com.idcg.idcw.model.bean.TxParmInfoBean;
import com.idcg.idcw.model.bean.VersionListBean;
import com.idcg.idcw.model.bean.WalletAssetBean;
import com.idcg.idcw.model.params.CheckAddressReqParam;
import com.idcg.idcw.model.params.CheckOriginalPwdReqParam;
import com.idcg.idcw.model.params.CreateSetPassReqParam;
import com.idcg.idcw.model.params.LocalCurrencyReqParam;
import com.idcg.idcw.model.params.LoginReqParam;
import com.idcg.idcw.model.params.ModifyPassReqParam;
import com.idcg.idcw.model.params.NotifyMessageReqParam;
import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.idcg.idcw.model.params.ReFeeReqParam;
import com.idcg.idcw.model.params.ReqPayPassParam;
import com.idcg.idcw.model.params.RequestCommonParam;
import com.idcg.idcw.model.params.RequestCommonReqParam;
import com.idcg.idcw.model.params.SendEmailCodeReqParam;
import com.idcg.idcw.model.params.SendFormReqAndResParam;
import com.idcg.idcw.model.params.SendPhoneCodeReqParam;
import com.idcg.idcw.model.params.SendTradeReqParam;
import com.idcg.idcw.model.params.SetFindPassReqParam;
import com.idcg.idcw.model.params.SetPayPassInfoReqParam;
import com.idcg.idcw.model.params.SettingConfigReqParam;
import com.idcg.idcw.model.params.TransMsgReqParam;
import com.idcg.idcw.model.params.VerfityNameReqParam;
import com.idcg.idcw.model.params.VerifyCodeReqParam;
import com.idcg.idcw.model.params.VerifyPhraseReqParam;
import com.idcg.idcw.net.HttpAppResponse;
import com.cjwsc.idcm.api.NetWorkApi;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.response.HttpResponse;

import java.util.List;

import foxidcw.android.idcw.common.model.bean.CheckAppVersionBean;
import foxidcw.android.idcw.foxcommon.provider.bean.ScanLoginBean;
import foxidcw.android.idcw.foxcommon.provider.params.ScanLoginReqParam;
import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 作者：hxy
 * 电话：13891436532
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 类描述：FoxIDCW com.idcg.idcw.api ${CLASS_NAME}
 * 备注消息：
 * 修改时间：2018/3/15 14:30
 **/

public interface HttpApi {

    //请求登录
    String login_url = "/api/Account/login";
    //请求注册
    String register_url = "/api/Account/register_new";
    // 登出接口
    String exit_url = "/api/Account/exit";
    //获取短语列表数据
    String phrase_list_url = "/api/SecuritySettings/GetRandomList";
    //保存用户助记词
    String save_phrase_url = "/api/SecuritySettings/SavePhrases";
    //获取钱包列表
    String money_bag_list_url = "/api/Wallet/GetWalletList?added=true";
    //校验表单
    String valid_send_from = "/api/Wallet/ValidSendFrom";
    //获取最新余额
    String get_wallet_balance = "/api/Wallet/GetBalanceByCoin";
    //获取币币兑换余额
    String get_exchange_balance = "/api/Exchange/GetBalanceByCoin";
    //获取推荐费用
    String recommend_free_list = "/api/Wallet/RecommendedFeeList";
    //校验转账地址是否正确
    String check_address_url = "/api/Wallet/ValidAddress";
    //钱包转账
    String bag_send_url = "/api/Wallet/SendFrom";
    //检查验证码
    String verify_code_true_false_url = "/api/Tools/CheckVerifyCode";
    // 发送手机验证码
    String verify_code_url = "/api/Tools/SendSMS";
    // 发送邮箱验证码
    String verify_code_mail_url = "/api/Tools/SendMail";
    //获取最新公告
    String get_notice_top = "/api/Message/GetNewMessage_New";
    //阅读公告
    String get_notice_read = "/api/Message/ConfirmRead";
    //获取首页数据
    String get_home_data = "/api/TrendChart/GetTrendChartData";
    //获取设置状态
    String setting_state_url = "/api/SecuritySettings/GetSetingsState";
    //检查用户助记词
    String VerifyPhrase = "/api/SecuritySettings/GetUserInfoByPhrase";
    //请求币种对应的数据
    String wallet_opt_url = "/api/Wallet/ReceivedWalletOpt";
    //获取钱包地址
    String get_account_address_info = "/api/Wallet/GetAccountAddress";
    //请求钱包历史数据
    String wallet_history_url = "/api/Wallet/GetWalletHistories";
    //添加币种数据
    String get_add_currency = "/api/Wallet/SetCurrencyIsShow";
    //获取添加全部的列表数据
    String get_add_all_data = "/api/Wallet/GetUserCurrency";
    //添加走势数据
    String get_history_amount_list = "/api/Wallet/GetHistoryAmount";
    //修改用户信息
    String modify_user_info_url = "/api/Account/ModifyUserInfo";
    // 设置支付密码
    String set_pay_pass_info = "/api/SecuritySettings/SetPayPasssword";
    // 检查原始密码
    String check_old_original_pass = "/api/SecuritySettings/CheckOriginalPwd";
    // 检查新原始密码
    String check_original_pass = "/api/SecuritySettings/CheckOriginalPwd_New";
    //获取用户设置配置
    String get_user_settings = "/api/TrendChart/GetConfig";
    //用户设置配置
    String set_user_settings = "/api/TrendChart/SettingConfig";
    //提交用户反馈
    String commint_feed_back = "/api/Tools/FeedBack";
    //重置你的密码
    String re_set_pass = "/api/SecuritySettings/ResetPassword";
    //修改支付密码
    String edit_pay_pass = "/api/SecuritySettings/RestPayPassword";
    //根据助记词查找用户
    String phrase_check_user = "/api/SecuritySettings/GetUserInfoByPhrase";
    //找回我的钱包
    String find_my_wallet = "/api/SecuritySettings/FindMyWallet";
    //获取所有同志
    String get_notice_list = "/api/Message/GetMessageList";
    //删除通知
    String get_notice_del = "/api/Message/MessageBatchSetting";

    //------------------------分界----------------------------->
    String verfity_user_name = "/api/Account/validUserInfo";//验证用户名 or邮箱用户名
    String check_version = "/api/Tools/CheckVersion?clientName=0";//检查版本更新
    String get_activity_notify = "/api/Message/GetNewMessage?";//得到通知消息
    String get_new_coin = "/api/Message/GetCoin";//获取赠送币
    String get_new_confirmread = "/api/Message/ConfirmRead?";//获取已阅状态
    String modify_trade_des = "/api/Wallet/ModifyTradeDescription";//提交转汇描述
    String get_tx_trade = "/api/Wallet/GetTxByTxId";//txid

    String get_version_list = "/api/Tools/GetVersionList";//获取版本列表

    String verify_sync_address = "/api/Wallet/ValidComplicatedAddressAsync";//校验特殊字符的地址


    String GetExchangeDataList = "/api/Exchange/GetExchangeDataList";//获取交易列表
    String GetExchangeDetail = "/api/Exchange/GetExchangeDetail";//获取交易详情
    String ExchangeIn = "/api/Exchange/ExchangeIn";//兑入
    String GetCoinList = "/api/Exchange/GetCoinList";//获取闪兑币列表
    String GetNewCoinList = "/api/Exchange/GetNewCoinList";//获取闪兑币列表
    String GetCoinRate = "/api/Exchange/GetCoinRate";//获取闪兑币汇率信息
    String AddExchangeCoin = "/api/Exchange/AddExchangeCoin";//添加币币闪兑虚拟币
    String EditComment = "/api/Exchange/EditComment";//修改币币兑换备注
    String GetHelpInfo = "/api/Exchange/GetHelpInfo";//获取币币兑换帮助

    String GetLoginScanResult = "/api/AuthorizedLogin/QrCodeAuthorized";
    //请求新的注册
    String register_new_url = "/api/Account/register";
    //获取扫码结果

    String get_about_us_list = "/api/Tools/GetAboutUs";
    //获取关于我们页面列表

    //获取系统币种的全部数据
    String get_add_all_curr = "/api/Wallet/GetAllCurrency";

    //邀请链入口显示隐藏
    String share_config_url = "/api/Tools/GetInviteConfig";

    // 登录接口
    @POST(login_url)
    Flowable<HttpResponse<LoginStatus>> requestPhoneLogin(@Body LoginReqParam reqParam);

    // 注册接口
    @POST(register_url)
    Flowable<HttpResponse<LoginStatus>> requestRegister(@Body CreateSetPassReqParam reqParam);

    // 登出接口
    @POST(exit_url)
    Flowable<HttpResponse<String>> requestLoginOut();

    //获取短语列表数据
    @GET(phrase_list_url)
    Flowable<HttpResponse<PhraseDataReqAndResParam>> requestPhraseData(@Query("lang") String lang);

    //保存用户助记词
    @POST(save_phrase_url)
    Flowable<HttpResponse<Object>> requestSavePhrase(@Body RequestBody requestBody);

    //校验表单
    @POST(valid_send_from)
    Flowable<HttpResponse<SendFormReqAndResParam>> requestSendFrom(@Body SendFormReqAndResParam reqParam);

    //获取最新余额
    @GET(get_wallet_balance)
    Flowable<HttpResponse<NewBalanceBean>> requestNewBalance(@Query("currency") String currency);

    //获取币币闪兑余额
    @Headers("Cache-Control:FORCE_NETWORK")
    @GET(get_exchange_balance)
    Flowable<HttpResponse<NewBalanceBean>> requestExchangeBalance(@Query("currency") String currency);

    //获取钱包列表
    @GET(money_bag_list_url)
    Flowable<HttpResponse<List<WalletAssetBean>>> getWalletList();

    //校验转账地址是否正确
    @POST(check_address_url)
    Flowable<HttpResponse<Boolean>> requestCheckBtnAddress(@Body CheckAddressReqParam reqParam);

    //钱包转账
    @POST(bag_send_url)
    Flowable<HttpResponse<SendTradeBean>> requestSendTrade(@Body SendTradeReqParam reqParam);

    //获取推荐费用
    @POST(recommend_free_list)
    Flowable<HttpResponse<ReFeeBean>> requestReFree(@Body ReFeeReqParam reqParam);

    // 发送手机验证码
    @POST(verify_code_url)
    Flowable<HttpResponse<Object>> requestPhoneCode(@Body SendPhoneCodeReqParam reqParam);

    // 发送手机验证码
    @POST(verify_code_mail_url)
    Flowable<HttpResponse<Object>> requestEmailCode(@Body SendEmailCodeReqParam reqParam);

    // 校验验证码
    @POST(verify_code_true_false_url)
    Flowable<HttpResponse<Boolean>> requestVerifyCode(@Body VerifyCodeReqParam reqParam);

    //获取最新公告
    @GET(get_notice_top)
    Flowable<HttpResponse<NoticeTopBean>> requestTopNotice(@Query("lang") String lang, @Query("client") String client);

    //阅读公告
    @GET(get_notice_read)
    Flowable<HttpResponse<Object>> requestReadNotice(@Query("msgId") int msgId);

    //获取首页数据
    @GET(get_home_data)
    Flowable<HttpResponse<HistoryAmountBean>> requestHistoryAmountNew();

    //获取设置状态
    @GET(setting_state_url)
    Flowable<HttpResponse<CheckStatusBean>> requestCheckStatus(@Query("device_id") String device_id, @Query
            ("newVersion") boolean newVersion);

    //检查用户助记词
    @POST(VerifyPhrase)
    Flowable<HttpResponse<Object>> requestVerifyPhrase(@Body VerifyPhraseReqParam reqParam);

    //请求币种对应的数据
    @GET(wallet_opt_url)
    Flowable<HttpResponse<Boolean>> requestOpt(@Query("currency") String currency, @Query("isShow") boolean isShow);

    //获取钱包地址
    @GET(get_account_address_info)
    Flowable<HttpResponse<String>> requestAccountAddress(@Query("currency") String currency);

    //请求钱包历史数据
    @POST(wallet_history_url)
    Flowable<HttpAppResponse<List<TransMsgBean>>> requestTransMsg(@Body TransMsgReqParam reqParam);

    //获取添加全部的列表数据
    @GET(get_add_all_data)
    Flowable<HttpResponse<List<AddDataBean>>> requestAddData();

    //添加币种数据
    @POST(get_add_currency)
    Flowable<HttpResponse<Object>> requestAddCurrency(@Body RequestBody requestBody);

    @GET(get_history_amount_list)
    Flowable<HttpResponse<List<TotalAssetsAmountBean>>> requestHistoryAmount();

    //修改用户信息
    @POST(modify_user_info_url)
    Flowable<HttpResponse<String>> requestModifyCurrency(@Body LocalCurrencyReqParam reqParam);

    //设置支付密码
    @POST(set_pay_pass_info)
    Flowable<HttpResponse<String>> requestSetPass(@Body SetPayPassInfoReqParam reqParam);

    // 检查新原始密码
    @POST(check_original_pass)
    Flowable<HttpResponse<CheckNewPinBean>> requestCheckOriginalPwd(@Body CheckOriginalPwdReqParam reqParam);

    // 检查原始密码
    @POST(check_old_original_pass)
    Flowable<HttpResponse<Boolean>> requestOldCheckOriginalPwd(@Body CheckOriginalPwdReqParam reqParam);

    //获取用户设置配置
    @GET(get_user_settings)
    Flowable<HttpResponse<RunChartDataBean>> requestRunChartData();

    //用户设置配置
    @POST(set_user_settings)
    Flowable<HttpResponse<Object>> requestSettingConfig(@Body SettingConfigReqParam reqParam);

    // 提交反馈
    @POST(commint_feed_back)
    Flowable<HttpResponse<Object>> requestCommonToServer(@Body RequestCommonReqParam reqParam);

    // 获取通知列表
    @GET(get_notice_list)
    Flowable<HttpResponse<NoticeResBean>> getNoticeList
    (@Query("lang") String lang, @Query("client") String client, @Query("pageIndex") String pageIndex, @Query
            ("pageSize") String pageSize);

    // 删除某条通知
    @POST(get_notice_del)
    Flowable<HttpResponse<Boolean>> getNoticeDel(@Body RequestBody requestBody);

    //------------------------分界----------------------------->
    @POST(re_set_pass)
    Flowable<HttpResponse<String>> modifyPass(@Body ModifyPassReqParam reqParam);

    @POST(phrase_check_user)
    Flowable<HttpResponse<LoginStatus>> requestCheckUser(@Body RequestBody reqParam);

    @POST(find_my_wallet)
    Flowable<HttpResponse<Object>> RequesteRegister(@Body SetFindPassReqParam reqParam);

    @POST(edit_pay_pass)
    Flowable<HttpResponse<Object>> reqPayPass(@Body ReqPayPassParam reqParam);

    @POST(verfity_user_name)
    Flowable<HttpResponse<Object>> verfityName(@Body VerfityNameReqParam reqParam);

    @GET(check_version)
    Flowable<HttpResponse<CheckAppVersionBean>> checkAppVersion();

    @GET(get_activity_notify)
    Flowable<HttpResponse<NotifyMessageBean>> requestNotifyMessage(@Body NotifyMessageReqParam reqParam);

    @POST(get_new_coin)
    Flowable<HttpResponse<Object>> requestGetCoin(@Body RequestBody requestBody);

    @GET(get_new_confirmread)
    Flowable<HttpResponse<Object>> RequestConfirmStatus(@Query("msgId") String id);

    @POST(modify_trade_des)
    Flowable<HttpResponse<Object>> RequestCommon(@Body RequestCommonParam reqParam);

    @POST(get_tx_trade)
    Flowable<HttpResponse<TxParmInfoBean>> RequestTxIdData(@Body RequestCommonParam reqParam);

    @GET(get_version_list)
    Flowable<HttpResponse<VersionListBean>> RequestVersionList(
            @Query("lang") String lang, @Query("clientName") String clientName, @Query("pageIndex")
            String pageIndex);

    @POST(verify_sync_address)
    Flowable<HttpResponse<String>> RequestSyncAddress(@Body ReqSyncAddressParam reqParam);

    @POST(GetExchangeDataList)
    Flowable<HttpResponse<List<ExchangeDataBean>>> requestExchangeDataList(@Body RequestBody reqParam);

    @POST(GetExchangeDetail)
    Flowable<HttpResponse<ExchangeDetailBean>> requestExchangeDetail(@Body RequestBody reqParam);

    @POST(GetCoinList)
    Flowable<HttpResponse<List<CoinPairBean>>> requestCoinList();

    @POST(GetNewCoinList)
    Flowable<HttpResponse<CoinExchangeBean>> requestNewCoinList();

    @POST(GetCoinRate)
    Flowable<HttpResponse<CoinExchangeRateBean>> requestCoinRate(@Body RequestBody reqParam);

    @POST(ExchangeIn)
    Flowable<HttpResponse<ExchangeResultBean>> requestExchangeIn(@Body RequestBody reqParam);

    @POST(GetHelpInfo)
    Flowable<HttpResponse<String>> requestHelpInfo(@Query("lang") String lang);

    @POST(EditComment)
    Flowable<HttpResponse<Boolean>> requestEditComment(@Body RequestBody reqParam);

    @POST(GetLoginScanResult)
    Flowable<HttpResponse<ScanLoginBean>> scanLogin(@Body ScanLoginReqParam reqParam);

    //关于我们页面
    @GET(get_about_us_list)
    Flowable<HttpResponse<List<AboutUsBean>>> requestAboutUsList(@Query("lang") String lang);

    //获取系统币种全部数据
    @GET(get_add_all_curr)
    Flowable<HttpResponse<List<AddDataBean>>> requestAllCurrency();

    // 获取交易所实时的价格信息
    @POST(NetWorkApi.TRADE_CONFIG_URL)
    Flowable<String> getTradeConfig();

    //获取邀请链入口的配置
    @GET(share_config_url)
    Flowable<HttpResponse<ShareConfigBean>> requestShareConfig(@Query("lang") String lang,@Query("client") String client);
}
