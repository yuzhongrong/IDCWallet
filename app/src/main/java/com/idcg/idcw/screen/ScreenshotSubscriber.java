package com.idcg.idcw.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

/**
 * Created by hpz on 2018/1/23.
 */

public class ScreenshotSubscriber {
    /**
     * 截屏监听
     */
    private OnScreenshotListener mOnScreenshotListener;
    /**
     * 截屏广播
     */
    private IntentFilter mIntentFilter;

    private Context mContext;
    /**
     * 截屏广播接收器
     */
    private ScreenshotReceiver mScreenshotReceiver;
    /**
     * 内容监听，监听到图片增加之后模拟发送广播
     */
    private ScreenshotContentObserver mScreenshotContentObserver;

    public ScreenshotSubscriber(Context context, OnScreenshotListener onScreenshotListener) {
        mContext = context;
        mOnScreenshotListener = onScreenshotListener;
        final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });
        mScreenshotReceiver = new ScreenshotReceiver();
        mScreenshotContentObserver = new ScreenshotContentObserver(handler, context);
        context.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, mScreenshotContentObserver);
        mIntentFilter = new IntentFilter(ScreenshotContentObserver.SCREENSHOT_ACTION);
    }

    /**
     * 开始监听截屏
     */
    public void subscribe() {
        if (mScreenshotReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).registerReceiver(mScreenshotReceiver, mIntentFilter);
        }
    }

    /**
     * 结束监听截屏
     */
    public void unSubscribe() {
        if (mScreenshotReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mScreenshotReceiver);
        }
    }

    /**
     * 页面关闭的时候调用，取消注册内容监听
     */
    public void onDestroy() {
        if (mScreenshotContentObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mScreenshotContentObserver);
        }
    }


    public interface OnScreenshotListener {
        /**
         * 监听截图事件
         *
         * @param path
         */
        void onScreenshot(String path);
    }

    /**
     * 截屏监听
     */
    private final class ScreenshotReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ScreenshotContentObserver.SCREENSHOT_ACTION)) {
                String screenShotPath = intent.getStringExtra(ScreenshotContentObserver.SCREENSHOT_PATH);
                if (!TextUtils.isEmpty(screenShotPath)) {
                    if (mOnScreenshotListener != null) {
                        mOnScreenshotListener.onScreenshot(screenShotPath);
                    }
                }
            }
        }
    }
}
