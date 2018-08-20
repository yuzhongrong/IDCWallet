package com.shuweikeji.qrcode.qrcode.decoding;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description:完成操作监听器，在一些情况下退出应用
 */
public final class FinishListener implements DialogInterface.OnClickListener,
		DialogInterface.OnCancelListener, Runnable {
    /** 退出的Activity */
	private final Activity activityToFinish;
    /**
     * 监听器构造方法
     * @param activityToFinish 退出的Activity
     */
	public FinishListener(Activity activityToFinish) {
		this.activityToFinish = activityToFinish;
	}

	public void onCancel(DialogInterface dialogInterface) {
		run();
	}

	public void onClick(DialogInterface dialogInterface, int i) {
		run();
	}
    
	public void run() {
		//退出Activity
		activityToFinish.finish();
	}

}
