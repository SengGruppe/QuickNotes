package io.github.senggruppe.quicknotes.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent toUserNotifier = new Intent(context, UserNotifier.class);
        Serializable n = intent.getExtras().getSerializable("note");
        toUserNotifier.setAction("actionstring" + System.currentTimeMillis());
        toUserNotifier.putExtra("note", n);
        context.startService(toUserNotifier);
    }
}
