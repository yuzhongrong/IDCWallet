package com.shuweikeji.qrcode.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.language.LanguageUtil;
import com.cjwsc.idcm.widget.LoadDialog.LoadingPopWindow;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.shuweikeji.qrcode.R;
import com.shuweikeji.qrcode.qrcode.camera.CameraManager;
import com.shuweikeji.qrcode.qrcode.decoding.CaptureActivityHandler;
import com.shuweikeji.qrcode.qrcode.decoding.InactivityTimer;
import com.shuweikeji.qrcode.qrcode.decoding.RGBLuminanceSource;
import com.shuweikeji.qrcode.utils.GetImagePath;
import com.shuweikeji.qrcode.utils.StatusBarUtils;
import com.shuweikeji.qrcode.view.ViewfinderView;
import com.tbruyelle.rxpermissions2.RxPermissions;


import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;

/**
 * Author:   dfl
 * Date :    2015-08-03
 * Description: 二维码扫描操作界面。接收和解码二维码图像。
 * 1.扫描二维码
 * 2.进入相册，根据相册中的二维码图像，执行扫描，并生成Url地址及链接
 * 3.控制相机闪光灯
 * 4.生成自己的二维码图像
 */

@Route(path = ArouterConstants.SAOCODE, name = "qrcode的扫描页面")
public class CaptureActivity extends BaseWalletActivity implements
        Callback, OnClickListener, BaseProgressView {
    private static final int REQUEST_CODE = 100;
    /**
     * 浏览扫描相册中的二维码图像处理成功
     */
    private static final int PARSE_BARCODE_SUC = 300;
    /**
     * 浏览扫描相册中的二维码图像处理失败
     */
    private static final int PARSE_BARCODE_FAIL = 303;
    private ProgressDialog mProgress;
    /**
     * 图片路径
     */
    private String photo_path;
    private Bitmap scanBitmap;
    /**
     * 二维码扫描结果、相机控制处理类CaptureActivityHandler
     */
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    /**
     * SurfaceView是否就绪
     */
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    /**
     * 延迟0.3秒执行的异步任务
     */
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private TextView mFlashBtn;
    private TextView mAlubmBtn;
    private LinearLayout img_back;
    private RxPermissions rxPermissions;
    //	private TextView mMyQrcodeBtn;
    private static final int REQUEST_CODE_GALLER = 100;
    private boolean mFinish = true;

    private LoadingPopWindow dialog;


    @Override
    protected int getLayoutId() {
        return R.layout.qr_code_scan;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        StatusBarUtils.with(this)
                .init();
        rxPermissions = new RxPermissions(this);
        //初始化CameraManager相机管理服务
        CameraManager.init(this);
        //初始化视图
        initControl();

        initView();

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        dialog = new LoadingPopWindow(this);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    private void initView() {
        EventBus.getDefault().register(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mFinish = getIntent().getBooleanExtra("isfinish", true);
        }
    }

    /**
     * 初始化视图
     */
    private void initControl() {
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        mFlashBtn = (TextView) findViewById(R.id.light_on_button);
        mAlubmBtn = (TextView) findViewById(R.id.select_qrcode_button);
        img_back = (LinearLayout) findViewById(R.id.mr_back_layout);
//		mMyQrcodeBtn = (TextView) findViewById(R.id.go_qrcode_card_button);
        mFlashBtn.setOnClickListener(this);
        mAlubmBtn.setOnClickListener(this);
        img_back.setOnClickListener(this);
//		mMyQrcodeBtn.setOnClickListener(this);
    }

//    /**
//     * 连续按两次返回键就退出
//     */
//    private long firstTime;
//
//    /**
//     * Back返回按键处理
//     */
//    public void onBackPressed() {
//        if (System.currentTimeMillis() - firstTime < 3000) {
//            finish();
//        } else {
//            firstTime = System.currentTimeMillis();
//            T.showShort(this, R.string.press_again_exit);
//        }
//    }

    /**
     * 扫描二维码功能CaptureActivity对用户可见时，初始化相机，将surfaceHolder绑定到相机上，同时启动相机设备、启动取景器，并向CaptureActivity返回相机实时捕获的画面。
     */
    protected void onResume() {
        super.onResume();
        //初始化SurfaceView
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        //从SurfaceView中得到surfaceHolder
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            //初始化相机，打开相机设备
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
//		initBeepSound();
        vibrate = true;
    }

    /**
     * 扫描二维码处于后台，或程序即将退出时。
     */
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            //退出操作，关闭相机取景器
            handler.quitSynchronously();
            handler = null;
        }
        //关闭相机设备
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 跳转到下一个界面，进入Url地址浏览界面，该界面用于显示二维码图像扫描解析结果
     *
     * @param result  二维码解析返回数据
     * @param barcode 返回的二维码图像
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        final String resultString = result.getText();
//        Intent resultIntent = new Intent(CaptureActivity.this, ResultActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("result", resultString);
//        // bundle.putParcelable("bitmap", barcode);
//        resultIntent.putExtras(bundle);
//        startActivity(resultIntent);
//        Intent intent = new Intent();
//        intent.putExtra("result", resultString);
//        //intent.putExtra("num", mCodeList.get(position).getCode());
//        setResult(3, intent);
//        //关闭当前activity
//        finish();
        // EventBus.getDefault().post(new EventBean(resultString));
        ;

        setResult(1, new Intent().putExtra("result", resultString));
        onScanSuccess(resultString);
        if (mFinish) this.finish();

        //Toast.makeText(this, resultString, Toast.LENGTH_LONG).show();
    }

    //告诉子类扫码成功
    protected void onScanSuccess(String success) {
    }

    /**
     * 初始化相机
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        //如果CaptureActivityHandler处理尚未初始化，在相机启动后，初始化CaptureActivityHandler
        if (handler == null) {
            handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, characterSet);
        }
    }

    /**
     * surfaceView发生更新时调用
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    /**
     * surfaceView被创建时调用
     */
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    /**
     * surfaceView销毁时调用
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    /**
     * 获取二维码扫描结果、相机控制处理类CaptureActivityHandler
     *
     * @return
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * 绘制Activity界面，这里主要针对相机返回的帧图像进行实时绘制处理。
     */
    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    //    --------------------------------------------------------------------------目前没用--------------------------------------------------------------------
//	/**
//	 * 扫描正确后的震动声音,如果感觉apk大了,可以删除
//	 * 
//	 * 这个方法目前没用
//	 */
//	
//	private void initBeepSound() {
//		if (playBeep && mediaPlayer == null) {
//			// The volume on STREAM_SYSTEM is not adjustable, and users found it
//			// too loud,
//			// so we now play on the music stream.
//			setVolumeControlStream(AudioManager.STREAM_MUSIC);
//			mediaPlayer = new MediaPlayer();
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			mediaPlayer.setOnCompletionListener(beepListener);
//
//			AssetFileDescriptor file = getResources().openRawResourceFd(
//					R.raw.qrcode_beep);
//			try {
//				mediaPlayer.setDataSource(file.getFileDescriptor(),
//						file.getStartOffset(), file.getLength());
//				file.close();
//				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//				mediaPlayer.prepare();
//			} catch (IOException e) {
//				mediaPlayer = null;
//			}
//		}
//	}
//     -----------------------------------------------------------------------------------------------------------------------------------------------------
    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 相册返回图像处理
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLER://相册
                    //Uri newUri = Uri.parse(GetImagePath.getPath(this, data.getData()));
                    File imgUri = new File(GetImagePath.getPath(this, data.getData()));
                    //Uri dataUri = FileProvider.getUriForFile(this, "com.renwohua.conch.fileprovider", imgUri);
                    photo_path = imgUri.getPath();

                    mProgress = new ProgressDialog(CaptureActivity.this);
                    mProgress.setMessage(getString(R.string.doing_sao));
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //根据选择的二维码图像对应的路径，扫描该二维码图像，并得到返回数据
                            Result result = scanningImage(photo_path);
                            //设备相册中二维码图像扫描成功处理
                            if (result != null) {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = result.getText();
                                mHandler.sendMessage(m);
                            }
                            //相册中二维码图像扫描失败处理
                            else {
                                Message m = mHandler.obtainMessage();
                                m.what = PARSE_BARCODE_FAIL;
                                m.obj = getString(R.string.error_restart_select);
                                mHandler.sendMessage(m);
                            }
                        }
                    }).start();
                    break;
            }
        }
    }

    /**
     * 根据图像路径，扫描二维码图像，此操作为选择相册中的二维码图像，返回后的扫描处理
     *
     * @param path 相册中二维码图像的路径
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        //以下是解析二维码图像操作
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 选择相册中的二维码图像进行解析(子线程中完成)，解析返回结果处理类Handler。
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mProgress.dismiss();
            switch (msg.what) {
                case PARSE_BARCODE_SUC://解析相册中二维码图像成功
                    onResultHandler((String) msg.obj, scanBitmap);
                    break;
                case PARSE_BARCODE_FAIL://解析相册中二维码图像失败
                    Toast.makeText(CaptureActivity.this, (String) msg.obj,
                            Toast.LENGTH_LONG).show();
                    break;

            }
        }

    };

    /**
     * 跳转到下一个界面，进入Url地址浏览界面，该界面用于显示二维码图像扫描解析结果
     *
     * @param resultString 扫描二维码图像后得到的Url路径
     * @param bitmap
     */
    private void onResultHandler(String resultString, Bitmap bitmap) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
//        EventBus.getDefault().post(new EventBean(resultString));
//        this.finish();
        setResult(1, new Intent().putExtra("result", resultString));
        onScanSuccess(resultString);
        if (mFinish) this.finish();

//        Intent resultIntent = new Intent(CaptureActivity.this, ResultActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("result", resultString);
//        bundle.putParcelable("bitmap", bitmap);
//        resultIntent.putExtras(bundle);
//        startActivity(resultIntent);
        //Toast.makeText(this, resultString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.select_qrcode_button) {// 打开手机中的相册
            Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
            innerIntent.setType("image/*");
            Intent wrapperIntent = Intent.createChooser(innerIntent, getString(R.string.select_sao_picture));
            this.startActivityForResult(wrapperIntent, REQUEST_CODE);

        } else if (i == R.id.light_on_button) {
            CameraManager.get().switchFlashLight();

//		case R.id.go_qrcode_card_button://创建我的二维码
//			Intent intent = new Intent(CaptureActivity.this,
//					CreateQRActivity.class);
//			startActivity(intent);
//			break;
        } else if (i == R.id.mr_back_layout) {
            CaptureActivity.this.finish();
        } else {

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtil.e("MyApplication", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        LanguageUtil.initAppLanguage(this);
    }

    @Override
    public void showDialog() {

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {

                if (!dialog.isShowing()) {

                    dialog.showPopupWindow();

                }

            }
        });
    }

    @Override
    public void dismissDialog() {
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {

                if (dialog.isShowing()) {

                    dialog.dismiss();

                }

            }
        });
    }
}
