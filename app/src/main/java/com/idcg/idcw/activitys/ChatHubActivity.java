package com.idcg.idcw.activitys;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.ChatHubBean;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.signarl.BaseSignalrActivity;
import com.cjwsc.idcm.signarl.impl.HubOnDataCallBackImpl;


import java.util.List;


/**
 * Created by admin-2 on 2018/3/29.
 */

public class ChatHubActivity extends BaseSignalrActivity implements View.OnClickListener {


    private final String host="http://push1.idcm.io:8319";
    private final String groupid="UserID=&GroupID=_DSQ3BmslE-cS-HP3POlnA";
    private final String groupid1="UserID=&GroupID=nIBO7Gvy10CNr9uGUVX24A";
    private final String btc_group="_DSQ3BmslE-cS-HP3POlnA";
    private final String eth_group="_DSQ3BmslE-cS-HP4POlnA";




    private Button button;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chathub_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        button=findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    @Override
    protected void onEvent() {
        subscribe("RealTrendCallback", new HubOnDataCallBackImpl<List<ChatHubBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void convert(List<ChatHubBean> chatHubBeans) {
                LogUtil.d("------chatHubBeans------->"+chatHubBeans.size());
                chatHubBeans.stream().forEach(x->{
                    if(x.getTradingConfigID().equals(btc_group))
                        LogUtil.d("------btc最新行情------->"+x.getNewest());
                    if(x.getTradingConfigID().equals(eth_group))LogUtil.d("------eth最新行情------->"+x.getNewest());
                });

            }
        });

//        subscribe("KLinesDataCallback", new HubOnDataCallBackImpl<Object>() {
//            @Override
//            public void convert(Object chatHubBeans) {
//
//                com.orhanobut.logger.LogUtil.d("------KLinesDataCallback------->"+chatHubBeans);
//            }
//        });
//
//        subscribe("OrderBookCallback", new HubOnDataCallBackImpl<Object>() {
//            @Override
//            public void convert(Object chatHubBeans) {
//
//                com.orhanobut.logger.LogUtil.d("------OrderBookCallback------->"+chatHubBeans);
//            }
//        });





    }


    @Override
    protected BaseView getView() {
        return null;
    }
    @Override
    public void onClick(View view) {

//        switchGroupID(groupid1, new SwitchGroupIdCallBack() {
//            @Override
//            public void OnSeccess() {
//                LogUtil.d("------switch success------->");
//            }
//
//            @Override
//            public void OnFail() {
//                LogUtil.d("------switch fail------->");
//            }
//        });


    }
    @Override
    public boolean isAutoCreateSignalrConnection() {
        return true;
    }




}
