package io.github.senggruppe.quicknotes.core.conditions;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import java.sql.Time;
import java.util.Calendar;

import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.NotificationReceiver;

public class TimeCondition implements Condition {
    private String conditionTime ="";
    private PendingIntent associatedIntend;

    private TimeCondition(String time,PendingIntent pi){
        conditionTime = time;
        associatedIntend =pi;
    }

    @Override
    public String getType() {
        return "TimeCondition";
    }

    @Override
    public String getDescription() {
        return conditionTime;
    }

    public void cancleCondition(Context caller){
        AlarmManager am = (AlarmManager) caller.getSystemService(Context.ALARM_SERVICE);
        if(am != null && associatedIntend!= null)
            am.cancel(associatedIntend);
    }

    public static TimeCondition SetupTimedNotification(Context caller, Calendar time){

        Intent intent = new Intent(caller,NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(caller, 0,intent,0);
        AlarmManager am = (AlarmManager)caller.getSystemService(Context.ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP,time.getTimeInMillis(),pendingIntent);

        return  new TimeCondition(time.getTime().toString(),pendingIntent);
    }

}
