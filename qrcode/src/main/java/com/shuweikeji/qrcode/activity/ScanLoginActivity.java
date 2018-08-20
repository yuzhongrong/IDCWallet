package com.shuweikeji.qrcode.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.shuweikeji.qrcode.R;

import foxidcw.android.idcw.foxcommon.Constants.Constants;
import foxidcw.android.idcw.foxcommon.provider.bean.ScanLoginBean;
import foxidcw.android.idcw.foxcommon.provider.params.ScanLoginReqParam;
import foxidcw.android.idcw.foxcommon.provider.services.ScanLoginProviderServices;

/**
 * Created by admin-2 on 2018/3/31.
 */
@Route(path = Constants.SCANLOGIN,name = "扫码页")
public class ScanLoginActivity extends CaptureActivity {

    @Autowired
    ScanLoginProviderServices scanLoginProviderServices;
    TextView light_on_button;
    LinearLayout img_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_scan_login);
        light_on_button=findViewById(R.id.light_on_button);
        img_back=findViewById(R.id.mr_back_layout);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        light_on_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CameraManager.get().switchFlashLight();
            }
        });

    }


    @Override
    protected void onScanSuccess(String success) {
        if(!TextUtils.isEmpty(success)){


      //      http://www.idcw.io?type=idcw_login&clientId=850fab93723f4a0faa7baac7e53cd3ee
            String result=success.substring(success.indexOf("?")+1);
            String[] splits=result.split("&");
           String type= splits[0].substring(splits[0].indexOf("=")+1);
           if(!TextUtils.isEmpty(type)&&type.equals("idcw_login")){//正常情况

               String clientId= splits[1].substring(splits[1].indexOf("=")+1);
               reqScanLogin(new ScanLoginReqParam(clientId,0, LoginUtils.getLoginBean(mCtx).getDevice_id()));
           }else{

               ToastUtil.show(getResources().getString(R.string.str_code_fail));
               finish();
           }

        }
    }

    //扫码成功请求网络把扫码内容传过去
    private void  reqScanLogin(final ScanLoginReqParam param){
        scanLoginProviderServices.requestScanLoginProvider(param).subscribeWith(
                new RxProgressSubscriber<ScanLoginBean>(this) {
                    @Override
                    public void onSuccess(ScanLoginBean data) {

                        //模拟数据
                        if(data==null)return;
                        if(data.getStatus().equals("1")){//扫码成功跳转到登录授权页面
                            ARouter.getInstance().build(Constants.AUTHLOGIN)
                                    .withSerializable("reqparam",param)
                                    .navigation();

                        }else{
                            ToastUtil.show(getResources().getString(R.string.str_code_fail));
                        }
                        finish();//关闭此页
                    }
                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(ex.getErrorCode());

                    }
                }
        );

    }


}
