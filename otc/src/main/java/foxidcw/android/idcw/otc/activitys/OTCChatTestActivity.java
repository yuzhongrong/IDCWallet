package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.signarl.impl.HubOnDataCallBackImpl;


import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.model.beans.OTCChatMessageBean;

/**
 * Created by yuzhongrong on 2018/5/6.
 */

public class OTCChatTestActivity extends BaseWalletActivity implements View.OnClickListener {
    private TextView test;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_test_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        test= (TextView) $(R.id.test);
        test.setOnClickListener(this);

        initSignalr("http://192.168.1.251:8100","123456789","WalletHub");

    }

    @Override
    protected void onEvent() {
        //订阅聊天消息
        subscribe("otcChatMessage", new HubOnDataCallBackImpl<OTCChatMessageBean>() {
            @Override
            public void convert(OTCChatMessageBean o) {

                LogUtil.d("------雄兵连测试消息发送------>"+o.getMessage());

            }
        });

    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
//        GroupsUtils.senMessageChat(hubProxy,groupid, GroupsUtils.send, new SwitchGroupIdCallBack() {
//            @Override
//            public void OnSeccess() {
//               LogUtil.d("---OnSeccess--发送成功->");
//            }
//            @Override
//            public void OnFail() {
//                LogUtil.d("---OnSeccess--发送失败->");
//            }
//        },"这个聊天室有人吗？");
    }
}
