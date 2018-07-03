package io.github.senggruppe.quicknotes.core;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.senggruppe.quicknotes.component.NoteItem;


//TODO: Talk about Replace Serialiable with Parcelable. Seems to work faster on Android.

/**
 * A single note.
 */
public class Note implements Serializable {
    private final List<Condition> conditions = new ArrayList<>();
    private final Set<Label> labels = new HashSet<>();
    private String content = "";
    private Date creationDate = new Date();
    private NotificationLevel level;
    private File audioFile;
    private transient NoteItem boundView;

    public void bindToView(NoteItem view) {
        boundView = view;
    }

    public void addLabel(Label l) {
        labels.add(l);
        l.notes.add(this);
        if (boundView != null) {
            boundView.addLabel(l);
        }
    }

    public void removeLabel(Label l) {
        labels.remove(l);
        l.notes.remove(this);
        if (boundView != null) {
            boundView.removeLabel(l);
        }
    }

    public Set<Label> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    @Override
    public String toString() {
        return String.valueOf(content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String con) {
        this.content = con;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public NotificationLevel getNotificationLevel() {
        return level;
    }

    public void setNotificationLevel(NotificationLevel notificationLevel) {
        this.level = notificationLevel;
    }
}
