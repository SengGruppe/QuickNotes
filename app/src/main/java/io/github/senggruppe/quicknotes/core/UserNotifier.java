package io.github.senggruppe.quicknotes.core;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.activities.MainActivity;


public class UserNotifier extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CreateNotification(this);
    }

    private static void CreateNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        long[] pattern = {0,300,0};
        PendingIntent pi = PendingIntent.getActivity(context, 0,intent,0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "Reminder")
                .setContentTitle(context.getString(R.string.NotificationReminderTitle))
                .setSmallIcon(R.drawable.ic_note)
                .setContentText("Debug")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("DebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebugDebug"))
                .setVibrate(pattern)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int)System.currentTimeMillis(),mBuilder.build());
    }

}
