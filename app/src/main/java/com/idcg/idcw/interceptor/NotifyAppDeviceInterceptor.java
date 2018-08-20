package com.idcg.idcw.interceptor;

import android.os.Handler;
import android.os.Looper;

import com.idcg.idcw.widget.dialog.DeviceInterceptorPopWindow;
import com.cjwsc.idcm.base.AppManager;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hpz on 2018/4/8.
 */

public class NotifyAppDeviceInterceptor implements Interceptor {

    private WeakReference<DeviceInterceptorPopWindow> deviceInterceptorPopWindow;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Headers headers = response.headers();
        String multidevice = headers.get("multidevice");
        if (String.valueOf(Boolean.TRUE).equals(multidevice)) {
            if (AppManager.getInstance().currentActivity() != null) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    //BaseWalletActivity.getCurrentActivity().showDeviceDialog();
                    if (deviceInterceptorPopWindow == null || deviceInterceptorPopWindow.get() == null || !deviceInterceptorPopWindow.get().isShowing()) {
                        deviceInterceptorPopWindow = new WeakReference<DeviceInterceptorPopWindow>(new DeviceInterceptorPopWindow(AppManager.getInstance().currentActivity()));
                        deviceInterceptorPopWindow.get().showPopupWindow();
                    }
                    //if (deviceInterceptorPopWindow == null || deviceInterceptorPopWindow.get() == null) {
                    //}

                    //deviceInterceptorPopWindow.showWithText(BaseWalletActivity.getCurrentActivity().getResources().getString(R.string.str_device_hint));
                });
            }

        }
        return response;
    }

}