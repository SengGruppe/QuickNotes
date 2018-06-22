package io.github.senggruppe.quicknotes.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent toUserNotifier = new Intent(context, UserNotifier.class);
        String n = intent.getExtras().getString("Note");
        toUserNotifier.setAction("actionstring" + System.currentTimeMillis());
        toUserNotifier.putExtra("Note",n);
        context.startService(toUserNotifier);
    }
}
