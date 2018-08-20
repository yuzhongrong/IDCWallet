package com.shuweikeji.qrcode.qrcode.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.cjwsc.idcm.Utils.LogUtil;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.shuweikeji.qrcode.R;
import com.shuweikeji.qrcode.activity.CaptureActivity;
import com.shuweikeji.qrcode.qrcode.camera.CameraManager;
import com.shuweikeji.qrcode.qrcode.camera.PlanarYUVLuminanceSource;

import java.util.Hashtable;

/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description:二维码图像解码处理类Handler，当解析二维码图像成功或失败后，将与CaptureActivity中的Handler进行交互，将解析二维码的结果报告给CaptureActivity。
 * CaptureActivity可以得到DecodeHandler在解码过程中生成的二维码图像返回结果。
 */
final class DecodeHandler extends Handler {

	private static final String TAG = DecodeHandler.class.getSimpleName();
    /** 二维码CaptureActivity */
	private final CaptureActivity activity;
	/** 多格式二维码图像解码器 */
	private final MultiFormatReader multiFormatReader;
    /**
     * 图像解码处理构造方法
     * @param activity 二维码
     * @param hints 解码参数
     */
	DecodeHandler(CaptureActivity activity,
			Hashtable<DecodeHintType, Object> hints) {
		//初始化多格式二维码图像解码器
		multiFormatReader = new MultiFormatReader();
		//设置解码配置参数
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		if (message.what == R.id.decode) {// LogUtil.d(TAG, "Got decode message");
			decode((byte[]) message.obj, message.arg1, message.arg2);

		} else if (message.what == R.id.quit) {
			Looper.myLooper().quit();

		}
	}

	/**
	 * 对二维码取景器矩形内的图像进行解码，为了提升效率，这里重用了现有的MultiFormatReader解码器
	 * 
	 * @param data The YUV preview frame.
	 * @param width 取景器宽度
	 * @param height 取景器高度
	 */
	private void decode(byte[] data, int width, int height) {
		long start = System.currentTimeMillis();
		Result rawResult = null;

		//创建数据
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				rotatedData[x * height + height - y - 1] = data[x + y * width];
		}
		int tmp = width; // Here we are swapping, that's the difference to #11
		width = height;
		height = tmp;
        //根据相机以及取景器大小，创建并得到数据源YUV或RGB
		PlanarYUVLuminanceSource source = CameraManager.get()
				.buildLuminanceSource(rotatedData, width, height);
		//根据数据源得到二维码图像
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try {
			//根据二维码图像，解码并生成返回结果
			rawResult = multiFormatReader.decodeWithState(bitmap);
		} catch (ReaderException re) {
			// continue
		} finally {
			//释放解码器
			multiFormatReader.reset();
		}
        //检测解码以及返回结果是否成功生成
		if (rawResult != null) {
			long end = System.currentTimeMillis();
			LogUtil.d(TAG, "Found barcode (" + (end - start) + " ms):\n"
					+ rawResult.toString());
			//通过CaptureActivity中的Handler发送Message，通知CaptureActivity，此时二维码图像已经成功解码并生成，接下来CaptureActivity将进行下一步处理。
			Message message = Message.obtain(activity.getHandler(),
					R.id.decode_succeeded, rawResult);
			Bundle bundle = new Bundle();
			bundle.putParcelable(DecodeThread.BARCODE_BITMAP,
					source.renderCroppedGreyscaleBitmap());
			message.setData(bundle);
			// LogUtil.d(TAG, "Sending decode succeeded message...");
			message.sendToTarget();
		} else {
			//通过CaptureActivity中的Handler发送Message，通知CaptureActivity，此时二维码图像解码生成失败，接下来CaptureActivity将进行下一步处理
			Message message = Message.obtain(activity.getHandler(),
					R.id.decode_failed);
			message.sendToTarget();
		}
	}

}
