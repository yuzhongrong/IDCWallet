package foxidcw.android.idcw.otc.utils;

import android.content.Context;

import foxidcw.android.idcw.common.utils.AppLanguageUtils;

public final class OTCUtils {
    public static String getCancelCountStringWithLanguage(Context context, int count) {
        if (AppLanguageUtils.getLanguageLocalCode(context).equals("0")) {
            String result;
            switch (count) {
                case 1:
                    result = "1st";
                    break;
                case 2:
                    result = "2nd";
                    break;
                case 3:
                    result = "3rd";
                    break;
                default:
                    result = String.valueOf(count + "th");
                    break;
            }
            return result;
        }
        return String.valueOf(count);
    }
}
