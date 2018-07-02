package io.github.senggruppe.quicknotes.core;

import java.io.File;
import java.util.List;

import org.joda.time.Period;


//TODO implement
public class NotificationLevel {
    public boolean vibrate;
    public String notification;
    public File sound;
    public List<Label> showLabels;
    public int blink;
    public Period loop;
    public int howOften;

    NotificationLevel(boolean vibrate, String notification, File sound, List<Label> showLabels, int blink, Period loop) {
        this.vibrate = vibrate;
        this.notification = notification;
        this.sound = sound;
        this.showLabels = showLabels;
        this.blink = blink;
        this.loop = loop;
        howOften = 1;
    }

    NotificationLevel(boolean vibrate, String notification, File sound, List<Label> showLabels, int blink, Period loop, int howOften) {
        this.vibrate = vibrate;
        this.notification = notification;
        this.sound = sound;
        this.showLabels = showLabels;
        this.blink = blink;
        this.loop = loop;
        this.howOften = howOften;
    }
}
