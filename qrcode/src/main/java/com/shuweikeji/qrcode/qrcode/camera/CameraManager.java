package com.shuweikeji.qrcode.qrcode.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Handler;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description: 相机，摄像头管理类，该类封装了相机操作的相关服务。
 */
public final class CameraManager {

	private static final String TAG = CameraManager.class.getSimpleName();
    /** 框架最小宽度  */
	private final int MIN_FRAME_WIDTH;
	/** 框架最小高度  */
	private final int MIN_FRAME_HEIGHT;
	/** 框架最大宽度  */
	private final int MAX_FRAME_WIDTH;
	/** 框架最大高度  */
	private final int MAX_FRAME_HEIGHT;
    /** 相机管理服务Manager */
	private static CameraManager cameraManager;
	/** Activity活动上下文context */
	private final Activity context;
	private final CameraConfigurationManager configManager;// 相机配置管理
	/** 相机设备HardWare */
	private Camera camera;
	private Rect framingRect;
	private Rect framingRectInPreview;
	private boolean initialized;
	/** 相机取景、预览标识状态(当相机启用取景预览操作时，标识状态为true，关闭时，标识状态为false) */
	private boolean previewing;
	private final boolean useOneShotPreviewCallback;
	/**
	 * 相机预览回调
	 */
	private final PreviewCallback previewCallback;
	/** 自动对焦回调处理，发出请求处理 */
	private final AutoFocusCallback autoFocusCallback;
	
	static final int SDK_INT; // Later we can use Build.VERSION.SDK_INT
	
	static {
		int sdkInt;
		try {
			sdkInt = Integer.parseInt(Build.VERSION.SDK);
		} catch (NumberFormatException nfe) {
			// Just to be safe
			sdkInt = 10000;
		}
		//获取当前设备SDK版本
		SDK_INT = sdkInt;
	}
    

	/**
	 * 根据Activity上下文，初始化相机管理服务CameraManager
	 * @param context 调用使用相机操作的Activity      
	 */
	public static void init(Activity context) {
		if (cameraManager == null) {
			cameraManager = new CameraManager(context);
		}
	}

	/**
	 * 获取相机管理器实例CameraManager
	 * 
	 * @return 相机管理器单例
	 */
	public static CameraManager get() {
		return cameraManager;
	}
    
	/**
	 * 相机管理服务构造方法，创建相机管理CameraManager
	 * @param context Activity活动的上下文
	 */
	private CameraManager(Activity context) {
		//初始化上下文context
		this.context = context;
		this.configManager = new CameraConfigurationManager(context);

		// Camera.setOneShotPreviewCallback() has a race condition in Cupcake,
		// so we use the older
		// Camera.setPreviewCallback() on 1.5 and earlier. For Donut and later,
		// we need to use
		// the more efficient one shot callback, as the older one can swamp the
		// system and cause it
		// to run out of memory. We can't use SDK_INT because it was introduced
		// in the Donut SDK.
		// useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) >
		// Build.VERSION_CODES.CUPCAKE;
		useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > 3; // 3
																				// =
																				// Cupcake

		previewCallback = new PreviewCallback(configManager,
				useOneShotPreviewCallback);
		autoFocusCallback = new AutoFocusCallback();

		WindowManager windowManager = context.getWindowManager();
		Display display = windowManager.getDefaultDisplay();//获取屏幕当前分辨率
		LogUtil.i("liweiping", display.toString());
		int min = Math.min(display.getWidth(), display.getHeight());
		MIN_FRAME_WIDTH = min / 2;
		MIN_FRAME_HEIGHT = MIN_FRAME_WIDTH;

		MAX_FRAME_WIDTH = min * 3 / 4;
		MAX_FRAME_HEIGHT = MAX_FRAME_WIDTH;
	}

	/**
	 * 打开摄像头驱动程序和初始化硬件参数。
	 * 
	 * @param holder
	 *            The surface object which the camera will draw preview frames
	 *            into.
	 * @throws IOException
	 *             Indicates the camera driver failed to open.
	 */
	public void openDriver(SurfaceHolder holder) throws IOException {
		if (camera == null) {
			//打开相机设备
			camera = Camera.open();
			if (camera == null) {
				throw new IOException();
			}
			//设置预览显示
			camera.setPreviewDisplay(holder);
			if (!initialized) {
				initialized = true;
				//初始化相机配置参数
				configManager.initFromCameraParameters(camera);
			}
			configManager.setDesiredCameraParameters(camera);

			// FIXME
			// SharedPreferences prefs =
			// PreferenceManager.getDefaultSharedPreferences(context);
			// if (prefs.getBoolean(PreferencesActivity.KEY_FRONT_LIGHT, false))
			// {
			// FlashlightManager.enableFlashlight();
			// }
			//闪光灯控制相关
			FlashlightManager.enableFlashlight();
		}
	}

	/**
	 * 关闭相机
	 */
	public void closeDriver() {
		if (camera != null) {
			//关闭闪光灯
			FlashlightManager.disableFlashlight();
			Parameters parameters = camera.getParameters();
			if (parameters != null) {
				//关闭闪光灯
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameters);
			}
			//释放相机
			camera.release();
			camera = null;
		}
	}
    
	/**
	 * 闪光灯控制，控制相机闪关灯开启或关闭。
	 */
	public void switchFlashLight() {
		if (camera != null) {
			Parameters parameters = camera.getParameters();
			if (!parameters.getFlashMode().equals(Parameters.FLASH_MODE_TORCH))
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
			else
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
		}
	}

	/**
	 * 开始捕获图像，启动相机预览，同时将图像绘制到取景器中
	 */
	public void startPreview() {
		if (camera != null && !previewing) {
			//开始捕获图像
			camera.startPreview();
			//将取景标识状态更新为true，此时相机取景器已经启动。
			previewing = true;
		}
	}

	/**
	 * 停止图像捕获，关闭预览，停止取景器中图像绘制。
	 */
	public void stopPreview() {
		if (camera != null && previewing) {
			if (!useOneShotPreviewCallback) {
				//解除已经注册的取景回调监听器
				camera.setPreviewCallback(null);
			}
			//关闭取景器
			camera.stopPreview();
			//解除预览回调与Handler之间的绑定
			previewCallback.setHandler(null, 0);
			autoFocusCallback.setHandler(null, 0);
			//将取景标识状态更新为false
			previewing = false;
		}
	}

	/**
	 * 请求预览框，返回帧预览，数据类型为byte[]的消息，Obj中将包含宽度和高度
	 * 
	 * @param handler
	 *            The handler to send the message to.
	 * @param message
	 *            The what field of the message to be sent.
	 */
	public void requestPreviewFrame(Handler handler, int message) {
		if (camera != null && previewing) {
			previewCallback.setHandler(handler, message);
			if (useOneShotPreviewCallback) {
				camera.setOneShotPreviewCallback(previewCallback);
			} else {
				camera.setPreviewCallback(previewCallback);
			}
		}
	}

	/**
	 * 请求相机自动对焦，通知相机硬件进行自动对焦
	 * 
	 * @param handler
	 *            The Handler to notify when the autofocus completes.
	 * @param message
	 *            The message to deliver.
	 */
	public void requestAutoFocus(Handler handler, int message) {
		if (camera != null && previewing) {
			autoFocusCallback.setHandler(handler, message);
			// LogUtil.d(TAG, "Requesting auto-focus callback");
			//注册自动对焦回调监听
			camera.autoFocus(autoFocusCallback);
		}
	}

	/**
	 * 
	 * 计算框架矩形UI应该显示用户条形码的位置。
	 * 是为了目标对齐以及空间扭动有助于用户离得设备距离足够远,确保能捕获到图像
	 * 
	 * @return The rectangle to draw on screen in window coordinates.
	 */
	public Rect getFramingRect() {
		Point screenResolution = configManager.getScreenResolution();
		if (framingRect == null) {
			if (camera == null) {
				return null;
			}
			int width = screenResolution.x * 2 / 3;
			if (width < MIN_FRAME_WIDTH) {
				width = MIN_FRAME_WIDTH;
			} else if (width > MAX_FRAME_WIDTH) {
				width = MAX_FRAME_WIDTH;
			}
			int height = screenResolution.y * 2 / 3;
			if (height < MIN_FRAME_HEIGHT) {
				height = MIN_FRAME_HEIGHT;
			} else if (height > MAX_FRAME_HEIGHT) {
				height = MAX_FRAME_HEIGHT;
			}
			// fix by liweiping for scanle width = height
			if (width > height)
				width = height;
			else
				height = width;
			// end by liweiping 20131126
			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 3;
			framingRect = new Rect(leftOffset, topOffset, leftOffset + width,
					topOffset + height);
			LogUtil.d(TAG, "Calculated framing rect: " + framingRect);
		}
		return framingRect;
	}

	/**
	 * Like {@link #getFramingRect} but coordinates are in terms of the preview
	 * frame, not UI / screen.
	 */
	public Rect getFramingRectInPreview() {
		if (framingRectInPreview == null) {
			Rect rect = new Rect(getFramingRect());
			Point cameraResolution = configManager.getCameraResolution();
			Point screenResolution = configManager.getScreenResolution();
			// modify here
			// rect.left = rect.left * cameraResolution.x / screenResolution.x;
			// rect.right = rect.right * cameraResolution.x /
			// screenResolution.x;
			// rect.top = rect.top * cameraResolution.y / screenResolution.y;
			// rect.bottom = rect.bottom * cameraResolution.y /
			// screenResolution.y;
			rect.left = rect.left * cameraResolution.y / screenResolution.x;
			rect.right = rect.right * cameraResolution.y / screenResolution.x;
			rect.top = rect.top * cameraResolution.x / screenResolution.y;
			rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
			framingRectInPreview = rect;
		}
		return framingRectInPreview;
	}

	/**
	 * Converts the result points from still resolution coordinates to screen
	 * coordinates.
	 * 
	 * @param points
	 *            The points returned by the Reader subclass through
	 *            Result.getResultPoints().
	 * @return An array of Points scaled to the size of the framing rect and
	 *         offset appropriately so they can be drawn in screen coordinates.
	 */
	/*
	 * public Point[] convertResultPoints(ResultPoint[] points) { Rect frame =
	 * getFramingRectInPreview(); int count = points.length; Point[] output =
	 * new Point[count]; for (int x = 0; x < count; x++) { output[x] = new
	 * Point(); output[x].x = frame.left + (int) (points[x].getX() + 0.5f);
	 * output[x].y = frame.top + (int) (points[x].getY() + 0.5f); } return
	 * output; }
	 */

	/**
	 * A factory method to build the appropriate LuminanceSource object based on
	 * the format of the preview buffers, as described by Camera.Parameters.
	 * 
	 * @param data
	 *            A preview frame.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @return A PlanarYUVLuminanceSource instance.
	 */
	public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data,
			int width, int height) {
		Rect rect = getFramingRectInPreview();
		int previewFormat = configManager.getPreviewFormat();
		String previewFormatString = configManager.getPreviewFormatString();
		switch (previewFormat) {
		// This is the standard Android format which all devices are REQUIRED to
		// support.
		// In theory, it's the only one we should ever care about.
		case PixelFormat.YCbCr_420_SP:
			// This format has never been seen in the wild, but is compatible as
			// we only care
			// about the Y channel, so allow it.
		case PixelFormat.YCbCr_422_SP:
			return new PlanarYUVLuminanceSource(data, width, height, rect.left,
					rect.top, rect.width(), rect.height());
		default:
			// The Samsung Moment incorrectly uses this variant instead of the
			// 'sp' version.
			// Fortunately, it too has all the Y data up front, so we can read
			// it.
			if ("yuv420p".equals(previewFormatString)) {
				return new PlanarYUVLuminanceSource(data, width, height,
						rect.left, rect.top, rect.width(), rect.height());
			}
		}
		throw new IllegalArgumentException("Unsupported picture format: "
				+ previewFormat + '/' + previewFormatString);
	}

	public Context getContext() {
		return context;
	}

}
