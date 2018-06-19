package io.github.senggruppe.quicknotes.core;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


//TODO: Talk about Replace Serialiable with Parcelable. Seems to work faster on Android.

/**
 * A single note.
 */
public class Note implements Serializable {
    public final List<Condition> conditions = new ArrayList<>();
    private final Set<Label> labels;
    public String content;
    public Date creationDate;
    public NotificationLevel level;
    public File audioFile;

    public Note(String content) {
        this(content, null, null);
    }

    public Note(String content, @Nullable Set<Label> labels, File audioFile) {
        this.audioFile = audioFile;
        this.content = content;
        creationDate = new Date();
        Note self = this;
        this.labels = labels == null ? new HashSet<>() : labels;
        Iterator<Label> it= this.labels.iterator();
        while (it.hasNext())
            it.next().notes.add(this);
    }

    public void addLabel(Label l) {
        labels.add(l);
        l.notes.add(this);
    }

    public void removeLabel(Label l) {
        labels.remove(l);
        l.notes.remove(this);
    }

    public Set<Label> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    @Override
    public String toString() {
        return String.valueOf(content);
    }
}
