package com.shuweikeji.qrcode.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;


import com.google.zxing.ResultPoint;
import com.shuweikeji.qrcode.R;
import com.shuweikeji.qrcode.qrcode.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * Author: dfl 
 * Date : 2015-08-03 
 * Description: 二维码图像解析取景框
 */
public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	/** 二维码取景框内，横线向下移动间隔时间(每隔0.1秒，横线向下移动一次) */
	private static final long ANIMATION_DELAY = 100L;

	private static final int OPAQUE = 0xFF;
	/** 二维码取景框内的横线 */
	private final Bitmap mLineBitmap;
	/** 中间那条线每次刷新移动的距离 */
	private final int SPEEN_DISTANCE;// 中间那条线每次刷新移动的距离
	/** 四个边角对应的宽度 */
	private final int LINE_WIDTH;// 四个边角对应的宽度
	private final int LINE_PADDING;
	/** 四个边角对应的长度 */
	private int LINE_LENGTH;// 四个边角对应的长度

	/**
	 * 中间滑动线的最顶端位置
	 */
	private int mSlideTop;
	/**
	 * 中间滑动线的最底端位置
	 */
	private static boolean mIsFirst;

	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		Resources resources = getResources();// 定义一个Resources类型的变量resources,并给它赋值
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);

		mLineBitmap = BitmapFactory.decodeResource(resources,
				R.drawable.qb_scan_light);

		float density = context.getResources().getDisplayMetrics().density;
		// 将像素转换成dp
		LogUtil.i("liweiping", "density = " + density);
		SPEEN_DISTANCE = (int) (2 * density);
		LINE_LENGTH = (int) (20 * density);
		LINE_WIDTH = (int) (5 * density);
		LINE_PADDING = -LINE_WIDTH;

		mIsFirst = true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// 中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
		// 获取中央位置的二维码扫描框
		Rect frame = CameraManager.get().getFramingRect();// Rect是获取矩形框
		if (frame == null) {
			return;
		}
		// 初始化中间线滑动的最上边和最下边
		if (mIsFirst) {
			mIsFirst = false;
			mSlideTop = frame.top;
		}
		// ---------------------------二维码扫描框内样式绘制开始-------------------------//
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		// 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (resultBitmap != null) {
			// 在扫描矩形绘制不透明位图结果
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {
			paint.setColor(Color.WHITE);
			// 画边框的四条细线
			canvas.drawLine(frame.left, frame.top - 1, frame.right,
					frame.top - 1, paint);// 上，向上偏移一个像素
			canvas.drawLine(frame.right, frame.top, frame.right, frame.bottom,
					paint);// 右，右边不用偏移
			canvas.drawLine(frame.left, frame.bottom, frame.right,
					frame.bottom, paint);// 下，下面不用偏移，
			canvas.drawLine(frame.left - 1, frame.top, frame.left - 1,
					frame.bottom, paint);// 左，左边偏移一个像素

			// draw rect //画扫描框边上的角，总共8个部分
			paint.setColor(frameColor);
			canvas.drawRect(LINE_PADDING + frame.left,
					LINE_PADDING + frame.top, LINE_PADDING
							+ (LINE_WIDTH + frame.left), LINE_PADDING
							+ (LINE_LENGTH + frame.top), paint);// 左-上
			canvas.drawRect(LINE_PADDING + frame.left,
					LINE_PADDING + frame.top, LINE_PADDING
							+ (LINE_LENGTH + frame.left), LINE_PADDING
							+ (LINE_WIDTH + frame.top), paint);// 上-左
			canvas.drawRect(-LINE_PADDING + ((-1 - LINE_WIDTH) + frame.right),
					LINE_PADDING + frame.top, -LINE_PADDING + frame.right - 1,
					LINE_PADDING + (LINE_LENGTH + frame.top), paint);// 右-上
			canvas.drawRect(-LINE_PADDING + (-LINE_LENGTH + frame.right - 1),
					LINE_PADDING + frame.top, -LINE_PADDING + frame.right - 1,
					LINE_PADDING + (LINE_WIDTH + frame.top), paint);// 上-右
			canvas.drawRect(LINE_PADDING + frame.left, -LINE_PADDING
					+ (-(LINE_LENGTH - 1) - 1 + frame.bottom), LINE_PADDING
					+ (LINE_WIDTH + frame.left), -LINE_PADDING + frame.bottom,
					paint);// 左-下
			canvas.drawRect(LINE_PADDING + frame.left, -LINE_PADDING
					+ ((0 - LINE_WIDTH) + frame.bottom), LINE_PADDING
					+ (LINE_LENGTH + frame.left), -LINE_PADDING + frame.bottom,
					paint);// 下-左
			canvas.drawRect(-LINE_PADDING + ((-1 - LINE_WIDTH) + frame.right),
					-LINE_PADDING + (-(LINE_LENGTH - 1) + frame.bottom),
					-LINE_PADDING + frame.right - 1, -LINE_PADDING
							+ frame.bottom, paint);// 右-下
			canvas.drawRect(-LINE_PADDING + (-LINE_LENGTH + frame.right - 1),
					-LINE_PADDING + ((0 - LINE_WIDTH) + frame.bottom),
					-LINE_PADDING + frame.right - 1, -LINE_PADDING
							+ (LINE_WIDTH - LINE_WIDTH + frame.bottom), paint);// 下-右

			// -------------------------二维码扫描框内，横线的处理回执开始-------------------------//
			// 绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE（抖动刷新）
			mSlideTop += SPEEN_DISTANCE;
			if (mSlideTop >= frame.bottom - SPEEN_DISTANCE - 18) {
				mSlideTop = frame.top + SPEEN_DISTANCE;
			}
			Rect lineRect = new Rect(frame.left, mSlideTop, frame.right,
					mSlideTop + 18);
			canvas.drawBitmap(mLineBitmap, null, lineRect, paint);
			// -------------------------二维码扫描框内，横线的处理回执开始-------------------------//
			// 二维码扫描框内小圆点显示，目前不需要
			// Collection<ResultPoint> currentPossible = possibleResultPoints;
			// Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			// if (currentPossible.isEmpty()) {
			// lastPossibleResultPoints = null;
			// } else {
			// possibleResultPoints = new HashSet<ResultPoint>(5);
			// lastPossibleResultPoints = currentPossible;
			// paint.setAlpha(OPAQUE);
			// paint.setColor(resultPointColor);
			// for (ResultPoint point : currentPossible) {
			// canvas.drawCircle(frame.left + point.getX(), frame.top
			// + point.getY(), 6.0f, paint);
			// }
			// }
			// if (currentLast != null) {
			// paint.setAlpha(OPAQUE / 2);
			// paint.setColor(resultPointColor);
			// for (ResultPoint point : currentLast) {
			// canvas.drawCircle(frame.left + point.getX(), frame.top
			// + point.getY(), 3.0f, paint);
			// }
			// }
			// ---------------------------二维码扫描框内样式绘制结束-------------------------//
			// Request another update at the animation interval, but only
			// repaint the laser line,
			// not the entire viewfinder mask.
			// 只刷新扫描框的内容，其他地方不刷新,延时100毫秒
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
		}
	}

	/**
	 * 刷新视图，每一帧状态下的刷新
	 */
	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
