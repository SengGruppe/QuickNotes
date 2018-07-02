package io.github.senggruppe.quicknotes.core.conditions;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Objects;

import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.notifications.NotificationReceiver;

public class TimeCondition implements Condition {
    private String conditionTime;
    private String intentActionString;
    private String noteContent;

    private TimeCondition(String time, String intentActionString, String noteContent) {
        conditionTime = time;
        this.intentActionString = intentActionString;
        this.noteContent = noteContent;
    }

    public static TimeCondition setupTimedNotification(Context ctx, Note dataForNotes, Calendar time) {
        String intentActionString = "notificationIntent:" + System.currentTimeMillis();
        String noteContent = dataForNotes.getContent();
        time.set(Calendar.SECOND, 0);
        Intent intent = new Intent(ctx, NotificationReceiver.class);
        intent.setAction(intentActionString);
        intent.putExtra("Note", noteContent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        AlarmManager am = Objects.requireNonNull((AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE));
        am.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

        return new TimeCondition(time.getTime().toString(), intentActionString, noteContent);
    }

    @Override
    public String getType() {
        return "TimeCondition";
    }

    @Override
    public String getDescription() {
        return conditionTime;
    }

    public void cancleCondition(Context ctx) {
        Intent intent = new Intent(ctx, NotificationReceiver.class);
        intent.setAction(intentActionString);
        intent.putExtra("Note", noteContent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (am != null && pendingIntent != null) {
            am.cancel(pendingIntent);
        }
    }

}
