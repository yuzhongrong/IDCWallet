package foxidcw.android.idcw.common.controller.client;

import com.cjwsc.idcm.Utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import foxidcw.android.idcw.common.controller.sise.RealTrendCallback;
import foxidcw.android.idcw.common.model.bean.TradeConfigPriceBean;

/**
 * Created by hpz on 2018/7/21.
 */

public class ClientServiceController {

    private static ClientServiceController instance = null;

    public static synchronized ClientServiceController Instance() {
        if (instance == null) {
            synchronized (ClientServiceController.class) {
                if (instance == null) {
                    instance = new ClientServiceController();
                }
            }
        }
        return instance;
    }

    public ServiceClient mClient = new ServiceClient();

    private List<RealTrendCallback> mCallbackInstanceList = Collections.synchronizedList(new ArrayList<RealTrendCallback>());
    private ConcurrentHashMap<String, List<RealTrendCallback>> mSubedTarget = new ConcurrentHashMap<String, List<RealTrendCallback>>();
    public List<String> mSubedItemCodeList = Collections.synchronizedList(new ArrayList<String>());
    private ConcurrentHashMap<String, TradeConfigPriceBean> mLastHogaDic = new ConcurrentHashMap<String, TradeConfigPriceBean>();

    /**
     * 公共的连接接口
     */
    public void Open() {
        MakeProxy();
    }

    private void MakeProxy() {
        mClient.ConnectToServer();
    }

    public void addCallback(RealTrendCallback target) {
        mCallbackInstanceList.add(target);
    }

    public void removeCallBack(RealTrendCallback target) {
        mCallbackInstanceList.remove(target);
    }

    public void Sub(RealTrendCallback target, String itemCode) {
        try {
            if (mSubedTarget.containsKey(itemCode)) {
                // Is already Subed Pass
                if (mSubedTarget.get(itemCode).contains(target)) return;
                else {
                    // Add
                    mSubedTarget.get(itemCode).add(target);
                }
            } else {
                // is first Subed ItemCode
                mSubedTarget.put(itemCode, new ArrayList<RealTrendCallback>() {
                    {
                        add(target);
                    }
                });
            }

            // Check it is subed itemcode to Server
            if (mSubedItemCodeList.contains(itemCode) == false) {
                mSubedItemCodeList.add(itemCode);
                // request to Sise Server
                new Thread(() ->
                {
                    try {
                        mClient.Sub(itemCode);
                    } catch (Exception ex) {
                        LogUtil.e("Sub:Exception Occurs " + ex.getMessage());
                    }
                }).start();

                // request to Order Server
                new Thread(() ->
                {
                    try {
                        ClientServiceController.Instance().mClient.Sub(itemCode);
                    } catch (Exception ex) {
                        LogUtil.e("Sub:Exception Occurs " + ex.getMessage());
                    }
                }).start();

                return;
            }

            if (mCallbackInstanceList.contains(target) == false) return;

                        TradeConfigPriceBean hoga = GetLastHoga(itemCode);
            if (hoga != null) {
                target.RealTrendCallback(hoga);
            }

            // if I have last Hoga callback it only request target instance
//            RealHoga hoga = GetLastHoga(itemCode);
//            if (hoga != null) {
//                target.callbackRealHoga(hoga);
//            }
//
//            // if I have last Exec callback it only request target instance
//            RealExec exec = GetLastExec(itemCode);
//            if (exec != null) {
//                target.callbackRealExec(exec);
//            }
        } catch (Exception ex) {
            LogUtil.e("Sub:Exception Occurs " + ex.getMessage());
        }
    }

    public TradeConfigPriceBean GetLastHoga(String itemCode) {
        try {
            return mLastHogaDic.containsKey(itemCode) ? mLastHogaDic.get(itemCode) : null;
        } catch (Exception ex) {
            LogUtil.e("GetLastHoga:Exception Occurs " + ex.getMessage());
            return null;
        }
    }
}
