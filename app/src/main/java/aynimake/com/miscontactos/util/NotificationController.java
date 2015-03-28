package aynimake.com.miscontactos.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;

import aynimake.com.miscontactos.MainActivity;
import aynimake.com.miscontactos.R;

/**
 * Created by Toshiba on 27/03/2015.
 */
public class NotificationController {

    private static Context context = ApplicationContextProvider.getContext();

    public static void notify(String title, String message, int notifID, int currentProgress, int maxProgress) {
        Bitmap iconLarge = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_agenda)
                .setLargeIcon(iconLarge)
                .setContentTitle(title)
                .setTicker(message)
                .setNumber(currentProgress)
                .setAutoCancel(true)
                .setProgress(maxProgress, currentProgress, false)
                .setContentText(message);

        if (currentProgress == 1) builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = builder.build(); // builder.setContentIntent(pendingIntent).build();
        manager.notify(notifID, notification);
    }

    public static void notify(String title, String message, int notifID) {
        Bitmap iconLarge = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_agenda)
                .setLargeIcon(iconLarge)
                .setContentTitle(title)
                .setTicker(message)
                .setAutoCancel(true)
                .setContentText(message)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = builder.build(); // builder.setContentIntent(pendingIntent).build();
        manager.notify(notifID, notification);
    }

}
