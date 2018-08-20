package com.shuweikeji.qrcode.qrcode.util;

import com.cjwsc.idcm.Utils.LogUtil;

/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class L {
	public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
	private static final String TAG = "way";

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (isDebug)
			LogUtil.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			LogUtil.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			LogUtil.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			LogUtil.v(TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (isDebug)
			LogUtil.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			LogUtil.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			LogUtil.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			LogUtil.i(tag, msg);
	}
}
