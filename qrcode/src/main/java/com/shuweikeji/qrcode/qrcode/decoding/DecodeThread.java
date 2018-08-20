package com.shuweikeji.qrcode.qrcode.decoding;

import android.os.Handler;
import android.os.Looper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.shuweikeji.qrcode.activity.CaptureActivity;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description: 解码线程DecodeThread，这里主要初始化二维码解析配置参数，并创建生成和解析二维码图像的处理类Handler。
 * 当解码操作完成时，DecodeHandler会产生消息，向CaptureActivity反馈二维码图像处理结果。
 */
final class DecodeThread extends Thread {

	public static final String BARCODE_BITMAP = "barcode_bitmap";
	/** 二维码扫描对应的CaptureActivity */
	private final CaptureActivity activity;
	private final Hashtable<DecodeHintType, Object> hints;
	/** 解码处理类Handler */
	private Handler handler;
	/** 同步处理辅助工具 */
	private final CountDownLatch handlerInitLatch;
    
	/**
	 * 解码线程构造方法
	 * @param activity 二维码扫描CaptureActivity
	 * @param decodeFormats 解码格式
	 * @param characterSet 
	 * @param resultPointCallback 结果回调
	 */
	DecodeThread(CaptureActivity activity, Vector<BarcodeFormat> decodeFormats,
			String characterSet, ResultPointCallback resultPointCallback) {
		this.activity = activity;
		//初始化同步CountDownLatch
		handlerInitLatch = new CountDownLatch(1);
		//---------------------配置解码参数开始----------------------------//
		hints = new Hashtable<DecodeHintType, Object>(3);
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}
		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
		//---------------------配置解码参数结束----------------------------//
	}
    
	/**
	 * 获取解码处理类Handler，一次只能被一个线程访问获取，其他线程将处于等待状态。
	 * @return
	 */
	Handler getHandler() {
		try {
			//进入同步状态，让其他线程等待，因为此时解码处理类Handler正在被一个线程访问
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		//初始化解码处理Handler
		handler = new DecodeHandler(activity, hints);
		//解码处理Handler初始化完成，解除同步操作
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
