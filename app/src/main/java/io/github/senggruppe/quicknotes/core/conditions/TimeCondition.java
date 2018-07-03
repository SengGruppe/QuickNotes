package io.github.senggruppe.quicknotes.core.conditions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.notifications.NotificationReceiver;
import io.github.senggruppe.quicknotes.util.Constants;

public class TimeCondition implements Condition {
    private final Date aim;
    private Note boundNote;
    private String intentActionString;

    public TimeCondition(Date aim, Note note) {
        this.aim = new Date((aim.getTime() / 1000) * 1000); // set seconds to 0
        this.boundNote = Objects.requireNonNull(note);
    }

    @Override
    public String getType() {
        return "TimeCondition";
    }

    @Override // FIXME: Use JodaTime for localization
    public String getDescription() {
        Calendar time = Calendar.getInstance();
        time.setTime(aim);
        return time.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.GERMAN) + " "
                + time.get(Calendar.DAY_OF_MONTH) + " "
                + time.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.GERMAN) + " " + time.get(Calendar.HOUR_OF_DAY)
                + ":" + (time.get(Calendar.MINUTE) < 10 ? "0" + time.get(Calendar.MINUTE) : time.get(Calendar.MINUTE))
                + " " + time.get(Calendar.YEAR);
    }

    @Override
    public void cancelCondition(Context ctx) {
        Intent intent = new Intent(ctx, NotificationReceiver.class);
        intent.setAction(intentActionString);
        intent.putExtra("note", boundNote);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (am != null && pendingIntent != null) {
            am.cancel(pendingIntent);
        }
    }

    @Override
    public void startCondition(Context ctx) {
        cancelCondition(ctx); // if already running

        Bundle b = new Bundle();
        b.putSerializable(Constants.KEY_NOTE, boundNote);
        Intent intent = new Intent(ctx, NotificationReceiver.class)
                .setAction(intentActionString = "notificationIntent:" + System.currentTimeMillis())
                .putExtra(Constants.KEY_NOTE, b);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        AlarmManager am = Objects.requireNonNull((AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE));
        am.setExact(AlarmManager.RTC_WAKEUP, aim.getTime(), pendingIntent);
    }
}
