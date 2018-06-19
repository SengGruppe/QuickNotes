package io.github.senggruppe.quicknotes.core.conditions;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.notifications.NotificationReceiver;

public class TimeCondition implements Condition {
    private String conditionTime;
    private PendingIntent associatedIntend;

    private TimeCondition(String time, PendingIntent pi) {
        conditionTime = time;
        associatedIntend = pi;
    }

    public static TimeCondition SetupTimedNotification(Context caller, Note dataForNotes, Calendar time) {
        time.set(Calendar.SECOND, 0);
        Intent intent = new Intent(caller, NotificationReceiver.class);
        intent.setAction("actionstring" + System.currentTimeMillis());
        intent.putExtra("Note", dataForNotes.content);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(caller, 0, intent, 0);

        AlarmManager am = (AlarmManager) caller.getSystemService(Context.ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

        return new TimeCondition(time.getTime().toString(), pendingIntent);
    }

    @Override
    public String getType() {
        return "TimeCondition";
    }

    @Override
    public String getDescription() {
        return conditionTime;
    }

    public void cancleCondition(Context caller) {
        AlarmManager am = (AlarmManager) caller.getSystemService(Context.ALARM_SERVICE);
        if (am != null && associatedIntend != null)
            am.cancel(associatedIntend);
    }

}
