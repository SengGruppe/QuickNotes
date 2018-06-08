package io.github.senggruppe.quicknotes.core;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A single note. TODO implement
 */
public class Note implements Serializable {
    public final ObservableList<Label> labels = new ObservableArrayList<>();
    public String content;
    public Date creationDate;
    public NotificationLevel level;
    public int index;
    public List<Condition> conditions;


    public Note(String content) {

        this.content = content;
        creationDate = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        return String.valueOf(content);
    }
}
