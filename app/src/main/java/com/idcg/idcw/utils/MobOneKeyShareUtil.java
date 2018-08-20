package com.idcg.idcw.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.base.application.BaseApplication;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import foxidcw.android.idcw.foxcommon.provider.services.MobShareProviderServices;

import static foxidcw.android.idcw.foxcommon.Constants.Constants.MOB_SHARE;

/**
 * 通过shareSdk分享
 * Created by xz on 2016/6/16.
 */
@Route(path = MOB_SHARE,name = "mob分享服务")
public class MobOneKeyShareUtil implements MobShareProviderServices {

    private static MobOneKeyShareUtil mInstance;
   // private static Context mContext;


    /**
     * 获取络例
     *
     * @return
     */
    public static MobOneKeyShareUtil getInstance() {
        if (mInstance == null) {
            synchronized (MobOneKeyShareUtil.class) {
                if (mInstance == null) {
                    mInstance = new MobOneKeyShareUtil();
                }
            }
        }
        return mInstance;
    }



    private  OnekeyShare oks;

    /**
     *
     * @param platform
     *
     * 前提：先要去app的build.gradle 中添加分享平台
     *
     * 常用平台：SinaWeibo（新浪微博）、Wechat（微信好友）、WechatMoments（微信朋友圈）、QQ（QQ好友）、Facebook、FacebookMessenger
     * 其他主流：TencentWeibo（腾讯微博）、QZone（QQ空间）、Renren（人人网）、Twitter、Douban（豆瓣）、Tumblr、GooglePlus（Google+）、Pinterest、Line、Instagram、Alipay（支付宝好友）、AlipayMoments（支付宝朋友动态）、Youtube、Meipai（美拍）
     *
     * @return
     */
    public MobOneKeyShareUtil getOneKeyShare(String platform) {
         oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if(!TextUtils.isEmpty(platform))oks.setPlatform(platform);
        oks.setCallback(listener);
        return mInstance;
    }

    /** title标题，微信、QQ和QQ空间等平台使用*/
    public MobOneKeyShareUtil setTitle(String title){
        oks.setTitle(title);
        return mInstance;

    }


    /** titleUrl QQ和QQ空间跳转链接*/
    public MobOneKeyShareUtil setTitleUrl(String titleUrl){

        oks.setTitleUrl(titleUrl);
        return mInstance;

    }

    /**text是分享文本，所有平台都需要这个字段*/
    public MobOneKeyShareUtil setText(String content){
        oks.setText(content);
        return mInstance;

    }

    /**imagePath是图片的本地路径，Linked-In以外的平台都支持此参数*/
    public MobOneKeyShareUtil setImageUrl(String imagePath){
        oks.setImageUrl(imagePath);
        return mInstance;

    }


    /** url在微信、微博，Facebook等平台中使用*/
    public MobOneKeyShareUtil setUrl(String imagePath){
        oks.setUrl(imagePath);
        return mInstance;

    }


    /** comment是我对这条分享的评论，仅在人人网使用*/
    public MobOneKeyShareUtil setComment(String comment){
        oks.setComment(comment);
        return mInstance;

    }


    /**启动分享GUI*/
    public  void show(){
        oks.show(BaseApplication.getContext());
    }

    /**
     * 分享监听器
     */
    private static PlatformActionListener listener = new PlatformActionListener() {

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
          //  DebugLog.e(DebugLog.TAG, "错误");
            LogUtil.e("错误");
        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
          //  DebugLog.e(DebugLog.TAG, "完成");
            LogUtil.e("完成");
        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
          //  DebugLog.e(DebugLog.TAG, "取消");

            LogUtil.e("取消");
        }
    };

    @Override
    public void init(Context context) {

    }

    @Override
    public void share(String platform,String title,String url,String text,String imgpath) {
                getOneKeyShare(platform)
                .setTitle(title)
                .setTitleUrl(url)
                .setText(text)
                .setImageUrl(imgpath)
                .show();
    }




}
