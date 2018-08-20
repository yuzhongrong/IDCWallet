package com.idcg.idcw.interceptor;

import com.idcg.idcw.app.WalletApplication;

import java.io.IOException;

import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author yiyang
 */
public class InterceptorForHeader implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request =  chain.request().newBuilder()
                .addHeader("language-code", AppLanguageUtils.getLanguageLocalCode(WalletApplication.getContext()))
                .build();

        return chain.proceed(request);

    }
}
