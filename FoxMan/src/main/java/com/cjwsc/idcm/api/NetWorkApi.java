package com.cjwsc.idcm.api;

/**
 * 作者：yzr
 * 电话：18826585609
 * 邮箱：956942189@qq.com
 * 版本号：1.0
 * 类描述：   接口Api
 * 备注消息：
 * 修改时间：2016/12/15 下午4:12
 **/
public class NetWorkApi {

    /**
     * 0 测试
     * 1 活动测试88
     * 2 预发布
     * 3 灰度
     * 4 正式 正式地址必须为4，如果需要改，必须得修改{{@link "com.idcg.idcw.activitys.FindAssetActivity#testEnviromentFill"}}
     */
    public static int API_STATE = 4;

    public static String baseUrl;//测试环境
    public static String TRANSACTION_RULE_URL;
    public static String TRANSACTION_ACCEPT_RULE_URL;
    public static final String TRADE_CONFIG_URL = "http://api.idcm.io:8303/api/Trade/GetTradeConfig";
    public static final String SHARE_COMMON_URL = "http://192.168.1.35:83/idcw/invite/?userid=";

    /**
     * 测试环境
     */
    private static String TEST_URL = "http://192.168.1.35:8203";//测试环境
    private static String TEST_TRANSACTION_RULE_URL = "http://192.168.1.35:81/static/mobile/tradingRules/gzsm.html"; //买卖规则的H5->测试环境
    private static String TEST_TRANSACTION_ACCEPT_RULE_URL = "http://192.168.1.35:81/static/mobile/tradingRules/index.html"; //保证金规则的H5->测试环境

    /**
     * 活动测试环境88
     */
    private static String ACTIVE_URL = "http://192.168.1.88:8203";//活动测试环境88
    private static String ACTIVE_TRANSACTION_RULE_URL = "http://192.168.1.35:81/static/mobile/tradingRules/gzsm.html"; //买卖规则的H5->测试环境
    private static String ACTIVE_TRANSACTION_ACCEPT_RULE_URL = "http://192.168.1.35:81/static/mobile/tradingRules/index.html"; //保证金规则的H5->测试环境

    /**
     * 预发布
     */
    private static String PRE_URL = "http://preapi.idcw.io";//预发布环境
    private static String PRE_TRANSACTION_RULE_URL = "http://prewww.idcw.io/static/mobile/tradingRules/gzsm.html"; //买卖规则的H5->预发布
    private static String PRE_TRANSACTION_ACCEPT_RULE_URL = "http://prewww.idcw.io/static/mobile/tradingRules/index.html"; //保证金规则的H5->预发布

    /**
     * 灰度环境
     */
    private static String GRAY_URL = "https://api.idcw.io:8204";//线上正式环境
    private static String GRAY_TRANSACTION_RULE_URL = "https://www.idcw.io/static/mobile/tradingRules/gzsm.html"; //买卖规则的H5->正式
    private static String GRAY_TRANSACTION_ACCEPT_RULE_URL = "https://www.idcw.io/static/mobile/tradingRules/index.html"; //保证金规则的H5->正式

    /**
     * 正式
     */
    private static String RELEASE_URL = "https://api.idcw.io";//线上正式环境
    private static String RELEASE_TRANSACTION_RULE_URL = "https://www.idcw.io/static/mobile/tradingRules/gzsm.html"; //买卖规则的H5->正式
    private static String RELEASE_TRANSACTION_ACCEPT_RULE_URL = "https://www.idcw.io/static/mobile/tradingRules/index.html"; //保证金规则的H5->正式

//  public static String baseUrl = "http://183.62.193.179:8203";//dapp的测试环境

    private static String[] BASE_URLS = {TEST_URL, ACTIVE_URL,PRE_URL, GRAY_URL, RELEASE_URL}; // 接口url
    private static String[] TRANSACTION_RULE_URLS = {TEST_TRANSACTION_RULE_URL,ACTIVE_TRANSACTION_RULE_URL, PRE_TRANSACTION_RULE_URL, GRAY_TRANSACTION_RULE_URL, RELEASE_TRANSACTION_RULE_URL}; // 买卖规则的H5
    private static String[] TRANSACTION_ACCEPT_URLS = {TEST_TRANSACTION_ACCEPT_RULE_URL, ACTIVE_TRANSACTION_ACCEPT_RULE_URL,PRE_TRANSACTION_ACCEPT_RULE_URL, GRAY_TRANSACTION_ACCEPT_RULE_URL, RELEASE_TRANSACTION_ACCEPT_RULE_URL};//保证金规则的H5

    static {
        baseUrl = BASE_URLS[API_STATE];
        TRANSACTION_RULE_URL = TRANSACTION_RULE_URLS[API_STATE];
        TRANSACTION_ACCEPT_RULE_URL = TRANSACTION_ACCEPT_URLS[API_STATE];
    }
}
