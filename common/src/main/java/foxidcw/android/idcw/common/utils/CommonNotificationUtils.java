package foxidcw.android.idcw.common.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.AppManager;

import java.lang.ref.WeakReference;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by hpz on 2018/7/6.
 */

public class CommonNotificationUtils {

    private WeakReference<Context> mWeakReferenceCtx;
    private String contentTitle,contentText,channelFinalId,channelFinalName;
    private int icon;
    private int largeIcon;

    public CommonNotificationUtils(@NonNull Context context,
                                   String contentTitle,
                                   String contentText,
                                   int icon,
                                   int largeIcon,
                                   String channelFinalId,
                                   String channelFinalName) {
        super();
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        this.icon = icon;
        this.largeIcon = largeIcon;
        this.channelFinalId = channelFinalId;
        this.channelFinalName = channelFinalName;
        mWeakReferenceCtx = new WeakReference<>(context);
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = channelFinalId;
            String channelName = channelFinalName;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager manager = (NotificationManager) mWeakReferenceCtx.get().getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        openNotification();
    }

    private void openNotification() {
        NotificationManager manager = (NotificationManager) mWeakReferenceCtx.get().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = null;
            if (manager != null) {
                channel = manager.getNotificationChannel(channelFinalId);
            }
            if (channel != null && channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, mWeakReferenceCtx.get().getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                mWeakReferenceCtx.get().startActivity(intent);
                ToastUtil.show("请手动将通知打开");//8.0以上会有此判断
            }
        }

        //点击通知栏跳转
        Intent notificationIntent = new Intent(mWeakReferenceCtx.get(), AppManager.getInstance().currentActivity().getClass());
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(
                mWeakReferenceCtx.get(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(mWeakReferenceCtx.get(), channelFinalId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(mWeakReferenceCtx.get().getResources(), largeIcon))
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .build();
        if (manager != null) {
            manager.notify(1, notification);
        }
    }
}
