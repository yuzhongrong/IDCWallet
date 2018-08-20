package foxidcw.android.idcw.common.controller.client;

import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.google.gson.Gson;

import foxidcw.android.idcw.common.model.bean.TradeConfigPriceBean;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

/**
 * Created by hpz on 2018/7/21.
 */

public class ServiceClient {

    private HubConnection mHubConnection;
    private HubProxy mHubProxy;

    public microsoft.aspnet.signalr.client.Logger hubLogger = (s, logLevel) -> {
        LogUtil.e("ServiceClient->Tag", s);
    };

    public void ConnectToServer() {
        try {
            //连接服务器
            mHubConnection = new HubConnection("https://push3.idcm.io:8310", "UserID="+"", true, hubLogger);

            // 创建hub端口
            mHubProxy = mHubConnection.createHubProxy("ExchangesHub");

            // 订阅错误事件
            mHubConnection.error(error -> error.printStackTrace());

            mHubConnection.received(jsonElement -> {//_DSQ3BmslE-cS-HPJPOlnA
                LogUtil.e(jsonElement.toString());
                LogUtil.e("$$received size = " + jsonElement.toString().getBytes().length);
            });

            mHubConnection.stateChanged((oldState, newState) -> {
                LogUtil.e("Trader Server Connection Changed : " + oldState + "->" + newState);
                if (newState == ConnectionState.Connected) return;

                //ClientServiceController.Instance().InnerChannel_StateChanged(newState);
            });

            //HubConnection.connected(() -> SiseServiceController.getInstance().InnerChannel_StateChanged(ConnectionState.Connected));
            //HubConnection.connectionSlow(() -> LogUtil.d("Trader Server connectionSlow"));

            // 订阅回调事件
            mHubProxy.subscribe(ClientServiceController.Instance());

            // 开始连接
            mHubConnection.start();
                    //.onError(throwable -> SiseServiceController.getInstance().InnerChannel_StateChanged(null));
        } catch (Exception ex) {
            LogUtil.e("Exception Occurs " + ex.getMessage());
            //SiseServiceController.getInstance().InnerChannel_StateChanged(null);
        }
    }

    void Sub(String itemCode) {
        try {
            if (mHubProxy != null)
                mHubProxy.invoke("Sub", itemCode);
        } catch (Exception ex) {
            LogUtil.e("Exception Occurs " + ex.getMessage());
        }
    }
}
