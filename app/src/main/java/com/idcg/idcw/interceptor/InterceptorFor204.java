package com.idcg.idcw.interceptor;

import com.idcg.idcw.R;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.net.exception.ServerException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 *
 * @author yiyang
 */
public class InterceptorFor204 implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if(response.code() == 204){
            throw new ServerException(BaseApplication.getContext().getString(R.string.server_error), "204");
        }
        return response;
    }
}
