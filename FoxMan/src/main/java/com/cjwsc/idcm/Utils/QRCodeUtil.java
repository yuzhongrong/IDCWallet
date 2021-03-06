package com.cjwsc.idcm.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码生成工具类
 * Created by xz on 2015/11/23.
 */
public class QRCodeUtil {
    /**
     * 文件存储根目录
     *
     * @param context
     * @return
     */
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static Bitmap createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm, String filePath) {
        try {
            if (content == null || "".equals(content)) {
                return null;
            }

            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 1); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            deleteWhite(bitMatrix);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, new FileOutputStream(filePath));
                return bitmap;
            }
            //return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/7
        float scaleFactor = srcWidth * 1.0f / 6 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            paint.setColor(Color.WHITE);
            canvas.drawARGB(0, 0, 0, 0);

            /** logo内矩形和logo的交集 **/
            RectF rectF = new RectF();
            rectF.left = (srcWidth - logoWidth) / 2 + 5;
            rectF.top = (srcHeight - logoHeight) / 2 + 5;
            rectF.right = srcWidth - (srcWidth - logoWidth) / 2 - 5;
            rectF.bottom = srcHeight - (srcHeight - logoHeight) / 2 - 5;
            canvas.drawRoundRect(rectF, 10, 10, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, paint);

            /** logo外矩形与（logo内矩形和logo的交集）的并集，并让外矩形放在底部 **/
            RectF rectF2 = new RectF();
            rectF2.left = (srcWidth - logoWidth) / 2;
            rectF2.top = (srcHeight - logoHeight) / 2;
            rectF2.right = srcWidth - (srcWidth - logoWidth) / 2;
            rectF2.bottom = srcHeight - (srcHeight - logoHeight) / 2;
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            canvas.drawRoundRect(rectF2, 10, 10, paint);

            /** 二维码图片与以上图片的并集，并让二维码图片放在底部 **/
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            canvas.scale(1 / scaleFactor, 1 / scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(src, 0, 0, paint);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    /**
     * 保存图片
     *
     * @param context 上下文
     * @param bmp     图片
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "b2b_qr");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // insertImage(context, file, fileName, bmp);
            savePicInfoToMediaStore(context, file.getAbsolutePath());
        }
    }


    /**
     * 把图片插入到系统图库
     *
     * @param context  上下文
     * @param file
     * @param fileName
     * @param bmp      图片
     */
    private static void insertImage(Context context, File file, String fileName, Bitmap bmp) {
        /** 把文件插入到系统图库 **/
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /** 删除原文件 **/
        if (file.exists()) {
            file.delete();
        }

        /** 更新系统图库 **/
        // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getParent())));

        if (!bmp.isRecycled()) {
            bmp.recycle(); //当存储大图片时，为避免出现OOM ，及时回收Bitmap
            //System.gc(); // 通知系统回收
        }
        // Toast.makeText(context, "图片保存为" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }


    /**
     * 把图片插入到系统图库
     * @param context
     * @param originPath
     */
    public static void savePicInfoToMediaStore(Context context, String originPath) {
        File file = new File(originPath);
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues newValues = new ContentValues(6);
        newValues.put(MediaStore.Images.Media.TITLE, file.getName());
        newValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
        newValues.put(MediaStore.Images.Media.DATA, file.getPath());
        newValues.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
        newValues.put(MediaStore.Images.Media.SIZE, file.length());
        newValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        int i = contentResolver.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                newValues, MediaStore.Images.Media.DATA + " =?", new String[]{originPath});
        if (i == 0) {//更新失败
            LogUtil.e( "更新失败" + i + "保存图片信息");
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newValues);
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getParent())));
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

}
