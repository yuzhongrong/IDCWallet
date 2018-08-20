package com.idcg.idcw.utils;//package com.cjwsc.baseweplus.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;

import org.simple.eventbus.EventBus;

import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.telegram.Telegram;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.foxcommon.provider.services.MobShareProviderServices2;

import static foxidcw.android.idcw.foxcommon.Constants.Constants.MOB_SHARE2;

/**
 * 通过shareSdk分享
 * Created by xz on 2016/6/16.
 */
@Route(path = MOB_SHARE2, name = "mob分享服务2")
public class MobShareUtil2 implements MobShareProviderServices2 {


    private static MobShareUtil2 mInstance;
    // private static Context mContext;


    /**
     * 获取络例
     *
     * @return
     */
    public static MobShareUtil2 getInstance() {
        if (mInstance == null) {
            synchronized (MobShareUtil2.class) {
                if (mInstance == null) {
                    mInstance = new MobShareUtil2();
                }
            }
        }
        return mInstance;
    }

    /**
     * 分享监听器
     */
    private static PlatformActionListener listener = new PlatformActionListener() {

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            LogUtil.e("-------onError-------->", "错误");
            if (arg0.getName().equals("Twitter")){
                EventBus.getDefault().post(new PosInfo(6322));//推特重复分享错误
            }

        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
            LogUtil.e("-------share-------->", "完成"+arg0.getName()+arg1);
            if (arg0.getName().equals("Twitter")||arg0.getName().equals("SinaWeibo")||arg0.getName().equals("Facebook")){
                EventBus.getDefault().post(new PosInfo(6321));
            }

        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
            LogUtil.e("-------share-------->", "取消");


        }
    };

    /**
     * 是否安装了手机QQ
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isQQAppInstalledAndSupported(Context context, String packageName) {
        boolean sIsQQAppInstalledAndSupported = Utils.isInstalled(context, packageName);
        if (!sIsQQAppInstalledAndSupported) {
            Toast.makeText(context, "QQ客户端未安装，请确认", Toast.LENGTH_SHORT).show();
        }
        return sIsQQAppInstalledAndSupported;
    }

    /**
     * 是否安装了新浪微博
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isWeiboAppInstalledAndSupported(Context context, String packageName) {
        boolean sIsWeiboAppInstalledAndSupported = Utils.isInstalled(context, packageName);
        if (!sIsWeiboAppInstalledAndSupported) {
            Toast.makeText(context, "新浪微博客户端未安装，请确认", Toast.LENGTH_SHORT).show();
        }
        return sIsWeiboAppInstalledAndSupported;
    }

    /**
     * 判断当前手机是否安装了微信
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isWXAppInstalledAndSupported(Context context, String packageName) {
        boolean sIsWXAppInstalledAndSupported = Utils.isInstalled(context, packageName);
        if (!sIsWXAppInstalledAndSupported) {
            Toast.makeText(context, "微信客户端未安装，请确认", Toast.LENGTH_SHORT).show();
        }
        return sIsWXAppInstalledAndSupported;
    }

    /**
     * 分享图文给QQ好友
     *
     * @param imgUrl 分享时的图片链接，支持本地图片
     */
    public static void shareToQQByMob(String shareTitle,
                                      String shareSummary, String url, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            //  imgUrl = Const.APP_IMG_URL;

        }
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(shareTitle);
        sp.setTitleUrl(url);
        sp.setText(shareSummary);
        sp.setImageUrl(imgUrl);
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(listener);
        qq.share(sp);
    }

    /**
     * 分享图片给QQ好友
     *
     * @param imgUrl 分享时的图片链接，支持本地图片
     */
    public static void shareImgToQQByMob(String imgUrl, String picPath) {
        if (TextUtils.isEmpty(imgUrl)) {
            // imgUrl = Const.APP_IMG_URL;
        }
        QQ.ShareParams sp = new QQ.ShareParams();
        if (picPath == null) {
            sp.setImageUrl(imgUrl);
        } else {
            sp.setImagePath(picPath);
        }
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(listener);
        qq.share(sp);
    }

    /**
     * 分享图文给QQ空间
     *
     * @param imgUrl 分享时的图片链接
     */
    public static void shareToQzoneByMob(String shareTitle, String shareSummary,
                                         String url, String imgUrl, String picPath) {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(shareTitle);
        sp.setTitleUrl(url);
        sp.setText(shareSummary);
        if (TextUtils.isEmpty(imgUrl)) {
            //   imgUrl = Const.APP_IMG_URL;
        }
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        sp.setImageUrl(imgUrl);
        qzone.setPlatformActionListener(listener);
        qzone.share(sp);
    }

    /**
     * 设置分享图片的大小
     *
     * @param imgUrl
     * @return
     */
    private static String setImgUrl(String imgUrl) {
        String string = "http://static.subsite.cjwsc.com//image/ce/69/32/ce693241b38fb717c7f72e89c83aea2ethumb452_452.jpg";
        int start = imgUrl.lastIndexOf("thumb");
        String ed = string.substring(start, string.length());
        String result = string.replace(ed, "100_100.jpg");
        return result;
    }

    /**
     * 分享图文给微信好友
     *
     * @param imgUrl 分享时的图片链接
     */
    public static void shareToWechatFriendsByMob(Context context,String shareTitle,
                                                 String shareSummary, String url, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            //    imgUrl = Const.APP_IMG_URL;
        }
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Wechat.SHARE_WEBPAGE);
        sp.setTitle(shareTitle);
        sp.setText(shareSummary);
        sp.setImageUrl(imgUrl);
        sp.setUrl(url);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (wechat.isClientValid()) {
            wechat.setPlatformActionListener(listener);
            wechat.share(sp);
        } else {
            ToastUtil.show(context.getString(R.string.str_app_no_install));
        }
    }

    /**
     * 分享图片给微信好友
     *
     * @param imgUrl 分享时的图片链接
     */
    public static void shareImgToWechatFriendsByMob(String shareTitle,
                                                    String shareSummary, String imgUrl, Bitmap sharePic) {
        if (TextUtils.isEmpty(imgUrl)) {
            // imgUrl = Const.APP_IMG_URL;
        }
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Wechat.SHARE_IMAGE);
        sp.setTitle(shareTitle);
        sp.setText(shareSummary);
        if (sharePic == null) {
            sp.setImageUrl(imgUrl);
        } else {
            sp.setImageData(sharePic);
        }
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(listener);
        wechat.share(sp);
    }

    /**
     * 分享图文给微信朋友圈
     *
     * @param imgUrl 分享时的图片链接
     */
    public static void shareToWechatByMob(Context context,String shareTitle,
                                          String shareSummary, String url, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            //imgUrl = Const.APP_IMG_URL;
        }
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(WechatMoments.SHARE_WEBPAGE);
        sp.setTitle(shareTitle);
        sp.setText(shareSummary);
        sp.setImageUrl(imgUrl);
        sp.setUrl(url);
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        if (wechat.isClientValid()) {
            wechat.setPlatformActionListener(listener);
            wechat.share(sp);
        } else {
            ToastUtil.show(context.getString(R.string.str_app_no_install));
        }
    }


    /**
     * 分享图片给微信朋友圈
     *
     * @param imgUrl 分享时的图片链接
     */
    public static void shareImgToWechatByMob(String shareTitle,
                                             String shareSummary, String imgUrl, Bitmap sharePic) {
        if (TextUtils.isEmpty(imgUrl)) {
            // imgUrl = Const.APP_IMG_URL;
        }
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Wechat.SHARE_IMAGE);
        sp.setTitle(shareTitle);
        sp.setText(shareSummary);
        if (sharePic == null) {
            sp.setImageUrl(imgUrl);
        } else {
            sp.setImageData(sharePic);
        }
        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
        if (wechatMoments.isClientValid()) {
            wechatMoments.setPlatformActionListener(listener);
            wechatMoments.share(sp);
        } else {

        }
    }

    /**
     * 分享图文给新浪微博
     *
     * @param imgUrl 分享时的图片链接
     */
    public static void shareToWeiboByMob(String shareTitle, String shareSummary,
                                         String url, String imgUrl, Bitmap sharePic) {
        if (TextUtils.isEmpty(imgUrl)) {
            // imgUrl = Const.APP_IMG_URL;
        }
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(shareTitle + "\n" + shareSummary + " " + url);
        if (sharePic == null) {
            sp.setImageUrl(imgUrl);
        } else {
            sp.setImageData(sharePic);
        }
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(listener); // 设置分享事件回调
        weibo.share(sp);
    }


    /**
     * @param shareTitle
     * @param shareSummary
     * @param url
     * @param imgUrl
     * @param sharePic
     */
    public static void shareToFacebookByMob(String shareTitle, String shareSummary,
                                            String url, String imgUrl, Bitmap sharePic) {
        if (TextUtils.isEmpty(imgUrl)) {
            // imgUrl = Const.APP_IMG_URL;
        }
        Facebook.ShareParams sp = new Facebook.ShareParams();
        sp.setUrl(url);
        sp.setImageData(sharePic);
        sp.setTitle(shareTitle);
        sp.setVenueDescription(shareSummary);
        /*sp.setText(shareTitle + "\n" + shareSummary + " " + url);
        if (sharePic == null) {
            sp.setImageUrl(imgUrl);
        } else {
            sp.setImageData(sharePic);
        }*/
        Platform faceBook = ShareSDK.getPlatform(Facebook.NAME);
        faceBook.setPlatformActionListener(listener); // 设置分享事件回调
        faceBook.share(sp);

    }


    public static void shareToTwitterByMob(String shareTitle, String shareSummary,
                                           String url, String imgUrl, Bitmap sharePic) {
        if (TextUtils.isEmpty(imgUrl)) {
            // imgUrl = Const.APP_IMG_URL;
        }

        Twitter.ShareParams sp = new Twitter.ShareParams();
        sp.setText(shareTitle + "\n" + shareSummary + "\n" + url);
        sp.setImageUrl(imgUrl);//如果推特需要图片的话就加上sharePic字段
        /*if (sharePic == null) {
            sp.setImageUrl(imgUrl);
        } else {
            sp.setImageData(sharePic);
        }*/
        Platform twitter = ShareSDK.getPlatform(Twitter.NAME);
        twitter.setPlatformActionListener(listener); // 设置分享事件回调
        twitter.share(sp);
    }

    public static void shareToTelegramByMob(Context context,String shareTitle, String shareSummary,
                                            String url, String imgUrl, Bitmap sharePic) {
        if (TextUtils.isEmpty(imgUrl)) {
            // imgUrl = Const.APP_IMG_URL;
        }
        Telegram.ShareParams sp = new Telegram.ShareParams();
        sp.setText(shareTitle + "\n" + shareSummary + "\n" + url);

//        sp.setImagePath(imgUrl);
        /*if (sharePic == null) {
            sp.setImageUrl(imgUrl);
        } else {
            sp.setImageData(sharePic);
        }*/
        Platform telgram = ShareSDK.getPlatform(Telegram.NAME);
        if (telgram.isClientValid()) {
            telgram.setPlatformActionListener(listener); // 设置分享事件回调
            telgram.share(sp);
        } else {
            ToastUtil.show(context.getString(R.string.str_app_no_tele__install));
        }

    }


    @Override
    public void shareToQQByMobProvider(String shareTitle, String shareSummary, String url, String imgUrl) {
        shareToQQByMob(shareTitle, shareSummary, url, imgUrl);
    }

    @Override
    public void shareImgToQQByMobProvider(String imgUrl, String picPath) {
        shareImgToQQByMob(imgUrl, picPath);
    }

    @Override
    public void shareToQzoneByMobProvider(String shareTitle, String shareSummary, String url, String imgUrl, String picPath) {
        shareToQzoneByMob(shareTitle, shareSummary, url, imgUrl, picPath);
    }

    @Override
    public void shareToWechatFriendsByMobProvider(Context context,String shareTitle, String shareSummary, String url, String imgUrl) {
        shareToWechatFriendsByMob(context,shareTitle, shareSummary, url, imgUrl);
    }

    @Override
    public void shareImgToWechatFriendsByMobProvider(String shareTitle, String shareSummary, String imgUrl, Bitmap sharePic) {
        shareImgToWechatFriendsByMob(shareTitle, shareSummary, imgUrl, sharePic);
    }

    @Override
    public void shareToWechatByMobProvider(Context context,String shareTitle, String shareSummary, String url, String imgUrl) {
        shareToWechatByMob(context,shareTitle, shareSummary, url, imgUrl);
    }

    @Override
    public void shareImgToWechatByMobProvider(String shareTitle, String shareSummary, String imgUrl, Bitmap sharePic) {
        shareImgToWechatByMob(shareTitle, shareSummary, imgUrl, sharePic);
    }

    @Override
    public void shareToWeiboByMobProvider(String shareTitle, String shareSummary, String url, String imgUrl, Bitmap sharePic) {
        shareToWeiboByMob(shareTitle, shareSummary, url, imgUrl, sharePic);
    }

    @Override
    public void shareToFacebookByMobProvider(String shareTitle, String shareSummary, String url, String imgUrl, Bitmap sharePic) {
        shareToFacebookByMob(shareTitle, shareSummary, url, imgUrl, sharePic);
    }

    @Override
    public void shareToTwitterByMobProvider(String shareTitle, String shareSummary, String url, String imgUrl, Bitmap sharePic) {
        shareToTwitterByMob(shareTitle, shareSummary, url, imgUrl, sharePic);
    }

    @Override
    public void shareToTelegramByMobProvider(Context context,String shareTitle, String shareSummary, String url, String imgUrl, Bitmap sharePic) {
        shareToTelegramByMob(context,shareTitle, shareSummary, url, imgUrl, sharePic);
    }

    @Override
    public void init(Context context) {

    }
}
