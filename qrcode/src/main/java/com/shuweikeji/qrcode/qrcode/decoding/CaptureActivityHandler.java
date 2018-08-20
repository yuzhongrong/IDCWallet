package com.shuweikeji.qrcode.qrcode.decoding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.cjwsc.idcm.Utils.LogUtil;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.shuweikeji.qrcode.R;
import com.shuweikeji.qrcode.activity.CaptureActivity;
import com.shuweikeji.qrcode.qrcode.camera.CameraManager;
import com.shuweikeji.qrcode.view.ViewfinderResultPointCallback;

import java.util.Vector;

/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description: 二维码扫描结果、相机控制处理类CaptureActivityHandler。
 * 该类主要产生操作，相机相关操作最终会分发到CameraManager，由CameraManager执行相机的不同操作。
 */
public final class CaptureActivityHandler extends Handler {

	private static final String TAG = CaptureActivityHandler.class
			.getSimpleName();
    /** 二维码扫描CaptureActivity */
	private final CaptureActivity activity;
	/** 解析二维码图像的线程 */
	private final DecodeThread decodeThread;
	/** 状态 */
	private State state;
    
	/**
	 * Author:   dfl
	 * Date :    2015-08-03
	 * Description: 状态枚举(预览、成功、完成)
	 */
	private enum State {
		/**
		 * 预览
		 */
		PREVIEW, 
		/**
		 * 成功
		 */
		SUCCESS, 
		/**
		 * 完成
		 */
		DONE
	}
    
	/**
	 * CaptureActivity处理类构造方法
	 * @param activity CaptureActivity实例
	 * @param decodeFormats 图像解码格式
	 * @param characterSet
	 */
	public CaptureActivityHandler(CaptureActivity activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet) {
		this.activity = activity;
		//创建解码线程
		decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
				new ViewfinderResultPointCallback(activity.getViewfinderView()));
		//启动线程
		decodeThread.start();
		state = State.SUCCESS;
		// Start ourselves capturing previews and decoding.
		//通过相机管理器CameraManager开始捕获图像，并进行解码。
		CameraManager.get().startPreview();
		//重新启动预览和解码，此时返回每一帧的预览图像，并调整摄像头进行自动对焦，然后重新绘制显示的图像，确保每一帧图像与当前实施同步
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {
		if (message.what == R.id.auto_focus) {// LogUtil.d(TAG, "Got auto-focus message");
			// When one auto focus pass finishes, start another. This is the
			// closest thing to
			// continuous AF. It does seem to hunt a bit, but I'm not sure
			// what else to do.
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}

		} else if (message.what == R.id.restart_preview) {
			LogUtil.d(TAG, "Got restart preview message");
			//重新启动预览和解码
			//1.向取景器返回每一帧的预览图像
			//2.请求相机自动对焦，通知相机硬件进行自动对焦，并注册相机自动对焦的回调监听
			//3.重新绘制取景器中的图像内容，确保图像与当前预览同步
			restartPreviewAndDecode();

		} else if (message.what == R.id.decode_succeeded) {
			LogUtil.d(TAG, "Got decode succeeded message");
			state = State.SUCCESS;
			Bundle bundle = message.getData();
			/***********************************************************************/
			Bitmap barcode = bundle == null ? null : (Bitmap) bundle
					.getParcelable(DecodeThread.BARCODE_BITMAP);
			//activity.getViewfinderView().drawResultBitmap(barcode);//add by liweiping 绘制在预览界面上
			activity.handleDecode((Result) message.obj, barcode);// 解析成功，回调
			/***********************************************************************/

		} else if (message.what == R.id.decode_failed) {// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);

		} else if (message.what == R.id.return_scan_result) {
			LogUtil.d(TAG, "Got return scan result message");
			activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			activity.finish();

		} else if (message.what == R.id.launch_product_query) {
			LogUtil.d(TAG, "Got product query message");
			String url = (String) message.obj;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			activity.startActivity(intent);

		}
	}
    
	/**
	 * 执行退出
	 */
	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
		quit.sendToTarget();
		try {
			decodeThread.join();
		} catch (InterruptedException e) {
			// continue
		}
		// Be absolutely sure we don't send any queued up messages
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
	}
	
    /**
     * 重新启动预览和解码
     */
	public void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			//返回帧预览图像
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			//请求相机自动对焦，通知相机硬件进行自动对焦，并注册相机自动对焦的回调监听
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			//重新启动预览和编码后，对取景器中的图像进行重新绘制，否则当帧发生变化时，取景器中的图像会与当前图像不同步。
			activity.drawViewfinder();
		}
	}

}
