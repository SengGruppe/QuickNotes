package io.github.senggruppe.quicknotes.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.util.Constants;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Note n = (Note) Objects.requireNonNull(intent.getBundleExtra(Constants.KEY_NOTE).getSerializable(Constants.KEY_NOTE));
        if (n.getNotificationLevel() != null) {
            n.getNotificationLevel().execute(context, n);
        }
    }
}
