package com.idcw.pay.api;

import com.cjwsc.idcm.net.response.HttpResponse;

import com.idcw.pay.model.bean.CustomerInfoBean;
import com.idcw.pay.model.bean.SecurityPayBean;
import com.idcw.pay.model.param.PayValidAddressReqParam;
import com.idcw.pay.model.param.SecurityPayReqParam;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CommonApi {

    //支付
    String SECURITY_PAY_SEND_FORM = "/api/SecurityPay/SecurityPaySendFrom";

    //支付
    String GET_CUSTOMER_INFO = "/api/SecurityPay/GetCustomerInfo";

    //检测地址是否合法
    String VALID_ADDRESS = "/api/Wallet/ValidAddress";

    //获取钱包地址
    String get_account_address_info = "/api/Wallet/GetAccountAddress";

    //如果是切割地址那么再判断合法性
    String VALID_COMPLICATED_ADDRESS_ASYNC = "/api/Wallet/ValidComplicatedAddressAsync";

    // 支付
    @POST(SECURITY_PAY_SEND_FORM)
    Flowable<HttpResponse<SecurityPayBean>> securityPay(@Body SecurityPayReqParam reqParam);

    // 获取公司信息
    @GET(GET_CUSTOMER_INFO)
    Flowable<HttpResponse<CustomerInfoBean>> getCustomerInfo(@QueryMap Map<String,String> map);

    // 校验地址
    @POST(VALID_ADDRESS)
    Flowable<HttpResponse<Boolean>> validAddress(@Body PayValidAddressReqParam reqParam);

    // 校验切割地址
    @POST(VALID_COMPLICATED_ADDRESS_ASYNC)
    Flowable<HttpResponse<String>> validComplicatedAddress(@Body PayValidAddressReqParam reqParam);

    //获取钱包地址
    @GET(get_account_address_info)
    Flowable<HttpResponse<String>> getAccountAddress(@Query("currency") String currency);
}
