package io.github.senggruppe.quicknotes.core.conditions;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
        intent.putExtra("note", dataForNotes);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        AlarmManager am = Objects.requireNonNull((AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE));
        am.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

        String t = "" + time.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.GERMAN) + " "
                + time.get(Calendar.DAY_OF_MONTH) + " "
                + time.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.GERMAN) + " " + time.get(Calendar.HOUR)
                + ":" + time.get(Calendar.MINUTE) + " " + time.get(Calendar.YEAR);

        return new TimeCondition(t, intentActionString, noteContent);
    }

    @Override
    public String getType() {
        return "TimeCondition";
    }

    @Override
    public String getDescription() {
        return conditionTime;
    }

    @Override
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
