package foxidcw.android.idcw.common.utils;

import android.content.Context;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.utils
 * 备注消息：
 * 修改时间：2018/3/29 16:14
 **/
public class AppLanguageUtils {

    // 获取本地语言对应的code码
    public static String getLanguageLocalCode(Context context) {
        String languageCode;
        String localLanguage = context.getResources().getConfiguration().locale.getLanguage();
        switch (localLanguage) {
            case "en":
                languageCode = "0";
                break;
            case "cn":
                languageCode = "1";
                break;
            case "fi":
                languageCode = "8";
                break;
            case "ja":
                languageCode = "2";
                break;
            case "ko":
                languageCode = "4";
                break;
            case "fr":
                languageCode = "5";
                break;
            case "nl":
                languageCode = "3";
                break;
            case "es":
                languageCode = "7";
                break;
            case "vi":
                languageCode = "6";
                break;
            default:
                if (localLanguage.equals("zh")) {
                    if (context.getResources().getConfiguration().locale.toString().equals("zh_TW") ||
                            context.getResources().getConfiguration().locale.toString().equals("zh_HK_#Hant") ||
                            context.getResources().getConfiguration().locale.toString().equals("zh_MO_#Hant") ||
                            context.getResources().getConfiguration().locale.toString().equals("zh_TW_#Hant") ||
                            context.getResources().getConfiguration().locale.toString().equals("zh_HK") ||
                            context.getResources().getConfiguration().locale.toString().equals("zh_MO")) {
                        languageCode = "8";
                        return languageCode;
                    }
                    languageCode = "1";
                } else {
                    languageCode = "0";
                }
                break;
        }
        return languageCode;
    }
}
