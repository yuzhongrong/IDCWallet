package com.shuweikeji.qrcode.qrcode.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import com.cjwsc.idcm.Utils.LogUtil;

/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description: 相机自动对焦回调处理
 */
final class AutoFocusCallback implements Camera.AutoFocusCallback {

	private static final String TAG = AutoFocusCallback.class.getSimpleName();

	private static final long AUTOFOCUS_INTERVAL_MS = 1500L;

	private Handler autoFocusHandler;
	private int autoFocusMessage;

	void setHandler(Handler autoFocusHandler, int autoFocusMessage) {
		this.autoFocusHandler = autoFocusHandler;
		this.autoFocusMessage = autoFocusMessage;
	}

	public void onAutoFocus(boolean success, Camera camera) {
		if (autoFocusHandler != null) {
			Message message = autoFocusHandler.obtainMessage(autoFocusMessage,
					success);
			autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
			autoFocusHandler = null;
		} else {
			LogUtil.d(TAG, "Got auto-focus callback, but no handler for it");
		}
	}

}
