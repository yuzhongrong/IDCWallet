package com.shuweikeji.qrcode.qrcode.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * 取景像素类
 */
public class SaveImageInBackgroundData {
	public Context context;
	public Bitmap image;
	public Uri imageUri;
	public Runnable finisher;
	public int iconSize;
	public int result;
}
