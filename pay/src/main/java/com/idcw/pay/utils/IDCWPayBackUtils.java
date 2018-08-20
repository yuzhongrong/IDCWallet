package com.idcw.pay.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.List;

import foxidcw.android.idcw.foxcommon.Constants.Constants;

public final class IDCWPayBackUtils {

    public static final String FROM_IDCW_PAY = "from_idcw";
    public static final String IDCW_PAY_APPID = "appid";
    public static final String IDCW_DEFAULT_APPID = "idc2454654387";
    public static final String IDCW_DEFAULT_HOST = "idcwpay";

    private String mMerchantId;
    private Context context;

    public IDCWPayBackUtils(Context context, String appId) {
        this.context = context;
        this.mMerchantId = TextUtils.isEmpty(appId) ? IDCW_DEFAULT_APPID : appId;
    }

    /**
     * 瑞时会默认商户ID idc2454654387
     */
    public void iDCWPayBackToMerchantsWithId() {
        Intent intent = new Intent();
        //  +
        Uri testUriMix = Uri.parse(String.valueOf(mMerchantId + "://" + IDCW_DEFAULT_HOST));
        intent.setData(testUriMix);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // 判断是否有能打开的
        if (isIntentAvailable(intent)) {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
            } catch (Exception e) {
            }
        }
    }

    private boolean isIntentAvailable(Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> infoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return infoList.size() > 0;
    }

    /**
     * pin界面处理回调逻辑
     *
     * @param context
     * @param intent
     */
    public static void checkToMerchantsWithIntent(Context context, Intent intent) {
        String appid = null;
        if (intent.hasExtra(IDCWPayBackUtils.IDCW_PAY_APPID)) {
            appid = intent.getStringExtra(IDCWPayBackUtils.IDCW_PAY_APPID);
        }
        if (!TextUtils.isEmpty(appid)) {
            new IDCWPayBackUtils(context, appid).iDCWPayBackToMerchantsWithId();
        }
    }

    /**
     * 返回到pin界面
     *
     * @param context
     */
    public void IDCWPayBackToPin(Context context) {
        /**
         * 这里需要传过去Appid 和 host
         */
        ARouter.getInstance().build(Constants.PIN_START)
                .withBoolean(FROM_IDCW_PAY, true)
                .withString(IDCW_PAY_APPID, mMerchantId)
                .navigation(context, new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {
                        LogUtil.e("IDCWPayBackToPin", "onFound");
                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        LogUtil.e("IDCWPayBackToPin", "onLost");
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                        LogUtil.e("IDCWPayBackToPin", "onArrival");
                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {
                        LogUtil.e("IDCWPayBackToPin", "onInterrupt");
                    }
                });
    }
}
