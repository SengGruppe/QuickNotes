package io.github.senggruppe.quicknotes.core;

import android.media.MediaPlayer;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A single note.
 */
public class Note implements Serializable {
    public final List<Condition> conditions = new ArrayList<>();
    public String content;
    public Date creationDate;
    public NotificationLevel level;
    private final Set<Label> labels;
    public File audioFile;
    private MediaPlayer player;

    public Note(String content) {
        this.content = content;
        labels = new HashSet<>();
        creationDate = new Date();
    }

    public Note(String content, @Nullable Set<Label> labels, File audioFile) {
        this.audioFile = audioFile;
        this.content = content;
        creationDate = new Date();
        Note self = this;
        this.labels = labels == null ? new HashSet<>() : labels;
    }

    public void addLabel(Label l) {
        labels.add(l);
    }

    public void removeLabel(Label l) {
        labels.remove(l);
    }

    public Set<Label> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    @Override
    public String toString() {
        return String.valueOf(content);
    }

    public void startAudio(){
        player = new MediaPlayer();
        try {
            player.setDataSource(audioFile.getAbsolutePath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopAudio(){
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
