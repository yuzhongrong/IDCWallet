package com.cjwsc.idcm.signarl;

import android.app.ActivityManager;
import android.content.Context;
import java.util.Stack;
import microsoft.aspnet.signalr.client.SignalRFuture;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * Created by xz on 2016/4/8.
 */
public  class SignalrConnectionManager {

    protected static Stack<FoxHubConnection> connectionStack;
    private static SignalrConnectionManager instance;



    /**
     * 单一实例
     */
    public static SignalrConnectionManager getInstance() {
        if (instance == null) {
            instance = new SignalrConnectionManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addConnection( FoxHubConnection connection) {
        if (connectionStack == null) {
            connectionStack = new Stack<>();
        }
        connectionStack.add(connection);
    }



    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public FoxHubConnection currentConnection() {

        return connectionStack.lastElement();
    }



    /**
     * 结束所有Activity
     */
    public void finishAllConnection() {



        if(connectionStack==null)return;//初始化的时候为空

        for (int i = 0, size = connectionStack.size(); i < size; i++) {
            if (null != connectionStack.get(i)) {
                connectionStack.get(i).disconnect();
            }
        }
        connectionStack.clear();
    }

    /**
     * 结束指定con
     */
    public void finishConnection(FoxHubConnection connection) {
        if (connection != null) {
            try {
                connection.disconnect();
                connectionStack.remove(connection);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }











    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllConnection();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
