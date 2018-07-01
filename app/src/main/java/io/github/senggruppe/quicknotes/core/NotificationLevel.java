package io.github.senggruppe.quicknotes.core;

import android.graphics.Color;

import java.io.File;
import java.time.Period;
import java.util.List;


//TODO implement
class NotificationLevel {
    boolean vibrate;
    String notification;
    File sound;
    List<Label> showLabels;
    Color blink;
    Period loop;
    int howOften;

    NotificationLevel(boolean vibrate, String notification, File sound, List<Label> showLabels, Color blink, Period loop) {
        this.vibrate = vibrate;
        this.notification = notification;
        this.sound = sound;
        this.showLabels = showLabels;
        this.blink = blink;
        this.loop = loop;
        howOften = 1;
    }

    NotificationLevel(boolean vibrate, String notification, File sound, List<Label> showLabels, Color blink, Period loop, int howOften) {
        this.vibrate = vibrate;
        this.notification = notification;
        this.sound = sound;
        this.showLabels = showLabels;
        this.blink = blink;
        this.loop = loop;
        this.howOften = howOften;
    }
}
