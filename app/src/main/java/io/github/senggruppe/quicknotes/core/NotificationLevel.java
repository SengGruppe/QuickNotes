package io.github.senggruppe.quicknotes.core;

import android.content.Context;
import android.graphics.Color;

import java.io.File;

import java.time.Period;
import java.util.Date;
import java.util.List;

import io.github.senggruppe.quicknotes.notifications.UserNotifier;

import static java.lang.Thread.sleep;

//TODO implement
class NotificationLevel {
    boolean vibrate;
    String notification;
    File sound;
    List<Label> showLabels;
    Color blink;
    Period loop;

    NotificationLevel(boolean vibrate, String notification, File sound, List<Label> showLabels, Color blink, Period loop){
        this.vibrate = vibrate;
        this.notification = notification;
        this.sound = sound;
        this.showLabels = showLabels;
        this.blink = blink;
        this.loop = loop;
    }

    public void trigger(Context ctx){
        UserNotifier.createNotification(ctx, notification);
    }
}
