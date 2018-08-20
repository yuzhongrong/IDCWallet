package foxidcw.android.idcw.foxcommon.provider.services;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface MobShareProviderServices2 extends IProvider{

      void shareToQQByMobProvider(String shareTitle,
                          String shareSummary, String url, String imgUrl);

      void shareImgToQQByMobProvider(String imgUrl, String picPath);


      void shareToQzoneByMobProvider( String shareTitle, String shareSummary,
                              String url, String imgUrl, String picPath);

      void shareToWechatFriendsByMobProvider( Context context,String shareTitle,
                                      String shareSummary, String url, String imgUrl);

      void shareImgToWechatFriendsByMobProvider( String shareTitle,
                                         String shareSummary, String imgUrl, Bitmap sharePic);

      void shareToWechatByMobProvider(Context context, String shareTitle,
                               String shareSummary, String url, String imgUrl);

      void shareImgToWechatByMobProvider( String shareTitle,
                                  String shareSummary, String imgUrl, Bitmap sharePic);

      void shareToWeiboByMobProvider( String shareTitle, String shareSummary,
                              String url, String imgUrl, Bitmap sharePic);


      void shareToFacebookByMobProvider( String shareTitle, String shareSummary,
                                         String url, String imgUrl, Bitmap sharePic);

      void shareToTwitterByMobProvider( String shareTitle, String shareSummary,
                                String url, String imgUrl, Bitmap sharePic);

    void shareToTelegramByMobProvider(Context context,String shareTitle, String shareSummary,
                                      String url, String imgUrl, Bitmap sharePic);


}
