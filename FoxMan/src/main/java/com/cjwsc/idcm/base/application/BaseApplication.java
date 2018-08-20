package com.cjwsc.idcm.base.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.caption.netmonitorlibrary.netStateLib.NetStateReceiver;
import com.cjwsc.idcm.BuildConfig;
import com.cjwsc.idcm.R;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.UIUtils;
import com.cjwsc.idcm.api.NetWorkApi;
import com.cjwsc.idcm.base.ui.pagestatemanager.PageManager;
import com.cjwsc.idcm.net.config.NetWorkConfiguration;
import com.cjwsc.idcm.net.http.HttpUtils;
import com.lzy.ninegrid.NineGridView;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by:hzp on 2017/8/10.
 * 这个类不让修改 请继承 谢谢！
 */

public class BaseApplication extends Application {

    private static Context context;
    private static String mActivityName;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //        Buy收集
        String packageName = context.getPackageName();
        String processName = getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //CrashReport.initCrashReport(context, "9ce7bbd668", false);
        // 网络库初始化
        initOkHttpUtils();
        //无敌LOG
        //initPrettyFormatStrategy();
        //初始化路由
        initARouter();
        initNineGridView();
        initNetListener();
        UIUtils.init(this);


        PageManager.initInApp(context);//暂时不用

    }


    private void initNetListener() {
        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);
    }


//    /**
//     * 初始化无敌log
//     */
//    private void initPrettyFormatStrategy() {
//        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
//                .tag("IDCW_WALLET")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
//                .build();
//        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
//            @Override
//            public boolean isLoggable(int priority, String tag) {
//                return true;
//            }
//        });
//    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    protected void initOkHttpUtils() {
        /**
         *  网络配置
         */
        InputStream cert_stream = getContext().getResources().openRawResource(R.raw.idcm);
        NetWorkConfiguration configuration = new NetWorkConfiguration(this)
                .baseUrl(NetWorkApi.baseUrl)
                .isCache(true)
                .isDiskCache(true)
                .isMemoryCache(true)
                .certificates(cert_stream);
        HttpUtils.setConFiguration(configuration);


        if (getItercepors() != null) HttpUtils.setInterceptoers(getItercepors());
    }


    public List<Interceptor> getItercepors() {
        return null;
    }


    //初始化
    private void initNineGridView() {
        //初始化NineGridView的图片加载器
        NineGridView.setImageLoader(new NineGridView.ImageLoader() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String url) {
                GlideUtil.loadImageView(context, url, imageView);
            }

            @Override
            public Bitmap getCacheImage(String url) {
                return null;
            }
        });
    }


    private void initARouter() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
        //初始化无敌log打印
    }

    @Override
    public void onLowMemory() {
        if (this != null) {
            NetStateReceiver.unRegisterNetworkStateReceiver(this);
            android.os.Process.killProcess(android.os.Process.myPid());
//            exitApp();
        }

        super.onLowMemory();
    }

    public static Context getContext() {
        return context;
    }

    public static String getCurrencyActivity() {
        return mActivityName;
    }

    public static void setCurrencyActivity(String activityName) {
        mActivityName = activityName;
    }


}
