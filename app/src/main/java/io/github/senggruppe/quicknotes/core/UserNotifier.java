package io.github.senggruppe.quicknotes.core;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.activities.MainActivity;


public class UserNotifier extends Service {
    String note;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        note = intent.getExtras().getString("Note");
        if (note == null) {
            note = "";
        }
        createNotification(this, note);
        return START_STICKY;
    }

    private static void createNotification(Context context, String content) {
        String[] split = content.split(".", 1);
        String smallText = "";
        String bigText = "";
        if (split.length == 2) {
            if (split[0] != null) {
                smallText = split[0];
            }
            if (split[1] != null) {
                bigText = split[1];
            }
        } else if (split.length == 1) {
            if (split[0] != null) {
                smallText = split[0];
            }
        }


        Intent intent = new Intent(context, MainActivity.class);
        long[] pattern = {0, 300, 0};
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "Reminder")
                .setContentTitle(context.getString(R.string.NotificationReminderTitle))
                .setSmallIcon(R.drawable.ic_note)
                .setContentText(smallText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .setVibrate(pattern)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

}
