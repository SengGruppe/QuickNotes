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
import java.util.Calendar;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.activities.MainActivity;

public class NotesDateNotifier extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, MainActivity.class);
        long[] pattern = {0,300,0};
        PendingIntent pi = PendingIntent.getActivity(this,01234,intent,0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.NotificationReminderTitle))
                .setContentText("Debug")
                .setVibrate(pattern)
                .setAutoCancel(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManager mNotificationManger = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManger.notify(01234,mBuilder.build());
    }

    public static void SetupNotification(Context caller, Calendar time){
        Intent intent = new Intent(caller,NotesDateNotifier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(caller,001,intent,0);
        AlarmManager am = (AlarmManager)caller.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,time.getTimeInMillis(),pendingIntent);
    }
}
