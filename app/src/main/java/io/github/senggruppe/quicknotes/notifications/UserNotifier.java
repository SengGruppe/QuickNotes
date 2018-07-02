package io.github.senggruppe.quicknotes.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.crashlytics.android.Crashlytics;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.activities.MainActivity;
import io.github.senggruppe.quicknotes.core.Label;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.NotificationLevel;
import io.github.senggruppe.quicknotes.core.conditions.TimeCondition;


public class UserNotifier extends Service {

    public static void createNotification(Context context, @NotNull Note n) {
        NotificationLevel nl = n.getLevel();
        String[] split = nl.notification.split(".", 1);
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

        String labels = "";
        if (nl.showLabels != null) {
            for (Label l : nl.showLabels) {
                labels += " " + l;
            }
        }
        Intent intent = new Intent(context, MainActivity.class);


        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "Reminder")
                .setContentTitle("Erinnerung" + labels)
                .setSmallIcon(R.drawable.ic_note)
                .setContentText(smallText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        if (nl.vibrate) {
            long[] pattern = {0, 300, 0};
            mBuilder.setVibrate(pattern);
        }

        if (nl.sound != null) {
            mBuilder.setSound(Uri.fromFile(nl.sound));
        }

        if (nl.blink != -1) {
            mBuilder.setLights(nl.blink, 500, 500);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());

        if (nl.howOften > 0) {
            nl.howOften--;
            Calendar c = Calendar.getInstance();
            c.add(Calendar.YEAR, nl.loop.getYears());
            c.add(Calendar.MONTH, nl.loop.getMonths());
            c.add(Calendar.DAY_OF_MONTH, nl.loop.getDays());
            c.add(Calendar.HOUR, nl.loop.getHours());
            c.add(Calendar.MINUTE, nl.loop.getMinutes());

            TimeCondition.setupTimedNotification(context, n, c);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Note note = (Note) intent.getExtras().getSerializable("note");
            if (note == null) {
                new NullPointerException();
            }
            createNotification(this, note);
        } catch (NullPointerException e) {
            Crashlytics.logException(e);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
