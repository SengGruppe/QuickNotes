package io.github.senggruppe.quicknotes.core;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A single note. TODO implement
 */
public class Note implements Serializable {
    public final ObservableList<Label> labels = new ObservableArrayList<>();
    public Serializable content;
    public Date creationDate;
    public NotificationLevel level;
    public int index;
    public List<Condition> conditions;

    public Note(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Note(Serializable content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.valueOf(content);
    }
}
