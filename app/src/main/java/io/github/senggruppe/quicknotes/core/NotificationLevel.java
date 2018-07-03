package io.github.senggruppe.quicknotes.core;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.io.File;
import java.io.Serializable;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.activities.MainActivity;

@SuppressWarnings("WeakerAccess") // TODO change this
public class NotificationLevel implements Serializable {
    public static final NotificationLevel DEFAULT = new NotificationLevel(true, null, -1);

    public boolean vibrate;
    public File sound;
    public int blink;

    NotificationLevel(boolean vibrate, File sound, int blink) {
        this.vibrate = vibrate;
        this.sound = sound;
        this.blink = blink;
    }

    public void execute(Context context, Note workData) {
        String[] split = workData.getContent().split(".", 1);
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

        StringBuilder labels = new StringBuilder();
        if (workData.getLabels() != null) {
            for (Label l : workData.getLabels()) {
                labels.append(" ").append(l);
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

        if (vibrate) {
            long[] pattern = {0, 300, 0};
            mBuilder.setVibrate(pattern);
        }

        if (sound != null) {
            mBuilder.setSound(Uri.fromFile(sound));
        }

        if (blink != -1) {
            mBuilder.setLights(blink, 500, 500);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }
}
