package com.idcg.idcw.app;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.idcg.idcw.BuildConfig;
import com.idcg.idcw.R;
import com.idcg.idcw.configs.ClientConfig;
import com.idcg.idcw.interceptor.InterceptorFor204;
import com.idcg.idcw.interceptor.InterceptorForHeader;
import com.idcg.idcw.interceptor.NotifyAppDeviceInterceptor;
import com.idcg.idcw.utils.UIUtils;
import com.blankj.utilcode.util.Utils;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.TakePhotoUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.sound.SoundPlayUtils;
import com.cjwsc.idcm.api.NetWorkApi;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.base.ui.pagestatemanager.PageManager;
import com.cjwsc.idcm.language.LanguageUtil;
import com.mob.MobSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.common.utils.IDCWPayJumpUtils;
import okhttp3.Interceptor;

/**
 * Created by admin-2 on 2018/3/13.
 * 这里用于定制化自己的Application
 */

public class WalletApplication extends BaseApplication {

    //smartrefresh 初始化
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                ClassicsHeader header = new ClassicsHeader(context);
                header.setEnableLastTime(false);
                header.setArrowDrawable(null);
                header.setAccentColor(context.getResources().getColor(R.color.color_footer));
                header.setDrawableMarginRightPx(-context.getResources().getDimensionPixelSize(R.dimen.dp10));
                header.setTextSizeTitle(14);
                return header;
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter footer = new ClassicsFooter(context);//color_a0a2b1
                footer.setDrawableSize(20);
                footer.setAccentColor(getContext().getResources().getColor(R.color.color_footer));
                footer.setTextSizeTitle(14);
                footer.setDrawableMarginRightPx(-context.getResources().getDimensionPixelSize(R.dimen.dp10));
                return footer;
            }
        });
    }
    //end

    public static WalletApplication app;

    public static WalletApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // 清除缓存的支付代码
        IDCWPayJumpUtils.clearCacheParam();
        LanguageUtil.initAppLanguage(this);
        // 初始化错误码文件
        ErrorCodeUtils.initErrorCode(this);
        if (NetWorkApi.API_STATE == 4) {
            CrashReport.initCrashReport(getApplicationContext(), "de510649ce", false);//线上腾讯bugly上报
        } else {
            CrashReport.initCrashReport(getApplicationContext(), "181a6c4722", false);//测试环境的腾讯bugly上报
        }
        UIUtils.init(this);
        ClientConfig.Instance().Init();
        ToastUtil.init(this);
        Utils.init(this);
        //定义了默认的错误页面,如果需要默认的empty，loading界面再去重新定义
        PageManager.initInApp(getApplicationContext(), 0, 0, R.layout.pager_layout_error);
        initSoundPlay();
        TakePhotoUtils.init(R.layout.dialog_photo_choose, () -> ToastUtil.show(getString(R.string.permission_deny)));
        LogUtil.DEBUG = BuildConfig.DEBUG; // 是否打印日志
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

    private void initSoundPlay() {
        SoundPlayUtils.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtil.e("MyApplication", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        LanguageUtil.initAppLanguage(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {//8.0+支持多国语言
        Context newContext = LanguageUtil.initAppLanguage(newBase);
        super.attachBaseContext(newContext);
        MultiDex.install(this);
    }

    @Override
    public List<Interceptor> getItercepors() {
        List<Interceptor> temp = new ArrayList<>();
        temp.add(new NotifyAppDeviceInterceptor());
        temp.add(new InterceptorFor204());
        temp.add(new InterceptorForHeader());
        return temp;
    }
}
