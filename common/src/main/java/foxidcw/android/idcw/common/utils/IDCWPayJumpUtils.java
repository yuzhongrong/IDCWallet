package foxidcw.android.idcw.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import com.cjwsc.idcm.Utils.LogUtil;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.google.gson.Gson;

import java.nio.charset.Charset;

import foxidcw.android.idcw.common.model.bean.IDCWDataBean;
import foxidcw.android.idcw.foxcommon.Constants.Constants;

import static foxidcw.android.idcw.foxcommon.Constants.Constants.ACACHE_PARAM_PAY;
import static foxidcw.android.idcw.foxcommon.Constants.Constants.IDCW_PAY;
import static foxidcw.android.idcw.foxcommon.Constants.Constants.IDCW_WITH_DRAW;

public class IDCWPayJumpUtils {

    public static final String RSH_ORDER_ID_PREFIX = "RSH";
    public static final String RSH_NOTIFY_URL = "api.stst.com";
    public static final String SCHEME = "idcwwallet";
    public static final String JUMP_FROM_WELCOME_ACTIVITY = "from_wel";
    public static final int JUMP_STATE_NONE = -1;
    public static final int JUMP_STATE_PAY = 0;
    public static final int JUMP_STATE_WITH_DRAW = 1;

    /**
     * 0 支付 / 充值
     * 1 提现
     * 默认为查看MainActivity是否打开
     *
     * @param type
     */
    public static void IDCWPayJumpWithType(Context context, int type, boolean isFromWel) {
        ARouter router = ARouter.getInstance();
        Postcard postcard = null;
        switch (type) {
            case JUMP_STATE_PAY: // 支付 充值
                postcard = router.build(IDCW_PAY);
                break;
            case JUMP_STATE_WITH_DRAW: // 提现
                postcard = router.build(IDCW_WITH_DRAW);
                break;
            default:
                break;
        }
        if (null != postcard) {
            postcard.withBoolean(JUMP_FROM_WELCOME_ACTIVITY, isFromWel);
            postcard.navigation(context, new NavigationCallback() {
                @Override
                public void onFound(Postcard postcard) {
                    LogUtil.e("IDCWPayJumpWithType", "onFound");
                }

                @Override
                public void onLost(Postcard postcard) {
                    LogUtil.e("IDCWPayJumpWithType", "onLost");
                }

                @Override
                public void onArrival(Postcard postcard) {
                    LogUtil.e("IDCWPayJumpWithType", "onArrival");
                }

                @Override
                public void onInterrupt(Postcard postcard) {
                    LogUtil.e("IDCWPayJumpWithType", "onInterrupt");
                }
            });
        }
    }

    /**
     * 缓存三方吊起传递参数
     *
     * @param intent WelComeActivity  intent
     */
    public static void cacheThirdPartyDataToSpIfHasScheme(Intent intent) {
        if (intent != null
                && Intent.ACTION_VIEW.equals(intent.getAction())
                && SCHEME.equals(intent.getScheme())) {
            LogUtil.e("-------------->   ", "添加参数");
            Uri uri = intent.getData();
            if (uri != null) { // 必须判空  防止崩溃
                String data = uri.getQueryParameter("data");
                LogUtil.e("third data  ----> ", data);
                LogUtil.e("data decode  ----> ", new String(Base64.decode(data.getBytes(), Base64.DEFAULT), Charset.defaultCharset()));
                SpUtils.setStringData(ACACHE_PARAM_PAY, new String(Base64.decode(data.getBytes(), Base64.DEFAULT), Charset.defaultCharset()));
            }
        }
    }

    /**
     * 判断是否需要进行三方跳转
     * 不需要返回-1
     * 需要返回需要跳转的类型
     */
    public static int checkNeedJumpWithLogin(Context context) {
        String data = getCacheParamData();
        LoginStatus loginStatus = LoginUtils.getLoginBean(context);
        if (!TextUtils.isEmpty(data) && loginStatus != null &&
                ACacheUtil.get(context).getAsBoolean(loginStatus.getDevice_id() + AcacheKeys.SAVEISVAILDPIN, false)) {
            IDCWDataBean bean = new Gson().fromJson(data, IDCWDataBean.class);
            return bean.getType();
        }
        return JUMP_STATE_NONE;
    }

    /**
     * 将获取到的参数转换为bean
     *
     * @param cacheData
     * @return
     */
    public static IDCWDataBean getCacheParamBean(String cacheData) {
        if (!TextUtils.isEmpty(cacheData)) {
            return new Gson().fromJson(cacheData, IDCWDataBean.class);
        }
        return null;
    }

    /**
     * 获取缓存的跳转state
     *
     * @return
     */
    public static int getCacheParamType() {
        String data = getCacheParamData();
        if (!TextUtils.isEmpty(data)) {
            IDCWDataBean bean = new Gson().fromJson(data, IDCWDataBean.class);
            return bean.getType();
        }
        return JUMP_STATE_NONE;
    }

    /**
     * 判断缓存的参数是否为空
     *
     * @return
     */
    public static boolean checkHasCacheParam() {
        return !TextUtils.isEmpty(getCacheParamData());
    }

    /**
     * 获取缓存的参数
     *
     * @return
     */
    public static String getCacheParamData() {
        return SpUtils.getStringData(ACACHE_PARAM_PAY, "");
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean checkHasLogin() {
        LoginStatus loginStatus = LoginUtils.getLoginBean(BaseApplication.getContext());
        return loginStatus != null;
    }

    /**
     * 判断是否已经登录并且有缓存参数
     *
     * @return
     */
    public static boolean checkHasCacheParamAndLogined() {
        return checkHasCacheParam() && checkHasLogin();
    }

    /**
     * 清除缓存key
     */
    public static void clearCacheParam() {
        SpUtils.remove(Constants.ACACHE_PARAM_PAY);
    }

    /**
     * 判断是不是由瑞士会吊起的
     */
    public static boolean isNeedBackToThirdParty() {
        try {
            // 1 如果是瑞士会的返回false
            // 2 如果不是瑞士会的并且appid为空
            IDCWDataBean dataBean = getCacheParamBean(getCacheParamData());
            if (!TextUtils.isEmpty(dataBean.getNotify_url()) && dataBean.getNotify_url().contains(RSH_NOTIFY_URL) &&
                    !TextUtils.isEmpty(dataBean.getTrans_id()) && dataBean.getTrans_id().contains(RSH_ORDER_ID_PREFIX)) {
                return false;
            } else {
                return TextUtils.isEmpty(dataBean.getAppId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
