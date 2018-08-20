package com.idcg.idcw.screen;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by hpz on 2018/1/23.
 */

public class ScreenshotContentObserver extends ContentObserver {
    public static final String SCREENSHOT_ACTION = "screenShotAction";
    public static final String SCREENSHOT_PATH = "screenShotPath";
    private Context mContext;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public ScreenshotContentObserver(Handler handler, Context context) {
        super(handler);
        mContext = context;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        String externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
        if (uri.toString().matches(externalContentUri + "/[0-9]+")) {
            Cursor cursor = null;
            try {
                cursor = mContext.getContentResolver().query(uri, new String[]{
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                }, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Intent screenShotIntent = new Intent();
                    screenShotIntent.setAction(SCREENSHOT_ACTION);
                    screenShotIntent.putExtra(SCREENSHOT_PATH, path);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(screenShotIntent);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        super.onChange(selfChange, uri);
    }
}

