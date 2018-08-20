package foxidcw.android.idcw.common.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;

import foxidcw.android.idcw.common.R;

public final class ErrorCodeUtils {

    private static HashMap<String, String> ERROR_CODE_MAP = new HashMap<String, String>();
    private static String sDefaultError;

    public static void initErrorCode(Context context) {
        ERROR_CODE_MAP.clear();
        String[] errorCodeItems = context.getResources().getStringArray(R.array.wallet_error_code);
        for (String codeItem : errorCodeItems) {
            String errorKeyMap[] = codeItem.split("=");
            if (errorCodeItems.length >= 2) {
                ERROR_CODE_MAP.put(errorKeyMap[0], errorKeyMap[1]);
            }
        }
        sDefaultError = context.getResources().getString(R.string.server_connection_error);
    }

    public static String getErrorMessage(String errorCode) {
        if (TextUtils.isEmpty(errorCode)) return "";
        String errorMessage = ERROR_CODE_MAP.get(errorCode);
        return TextUtils.isEmpty(errorMessage) ? sDefaultError : errorMessage;
    }

}
