package io.github.senggruppe.quicknotes.core;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.senggruppe.quicknotes.util.ListChangedAdapter;

/**
 * A single note. TODO implement
 */
public class Note implements Serializable {
    public final ObservableList<Label> labels;
    public String content;
    public Date creationDate;
    public NotificationLevel level;
    public int index;
    public List<Condition> conditions;
    public File audioFile;
    private MediaPlayer player;

    public Note(String content) {
        this.labels = new ObservableArrayList<Label>();
        this.content = content;
        creationDate = Calendar.getInstance().getTime();
        Note self = this;
        labels.addOnListChangedCallback(new ListChangedAdapter<>(new ListChangedAdapter.ItemAcceptor<Label>() {
            @Override
            public void accept(Label item) {
                item.notes.add(self);
            }
        }, new ListChangedAdapter.ItemAcceptor<Label>() {
            @Override
            public void accept(Label item) {
                item.notes.remove(self);
            }
        }));
    }

    public Note(String content, ObservableList<Label> labels, File audioFile) {
        this.audioFile = audioFile;
        this.content = content;
        creationDate = Calendar.getInstance().getTime();
        Note self = this;
        if (labels == null){
            this.labels = new ObservableArrayList<Label>();
        } else {
            this.labels = labels;
        }
        labels.addOnListChangedCallback(new ListChangedAdapter<>(new ListChangedAdapter.ItemAcceptor<Label>() {
            @Override
            public void accept(Label item) {
                item.notes.add(self);
            }
        }, new ListChangedAdapter.ItemAcceptor<Label>() {
            @Override
            public void accept(Label item) {
                item.notes.remove(self);
            }
        }));
        for (Label label : labels) {
            label.notes.add(this);
        }
    }

    public void addLabel(String text){
        Label l=new Label(text);
        if(!DataStore.getLabels().contains(l)){
            DataStore.addLabel(l);
        }else{
            l=DataStore.getLabels().get(DataStore.getLabels().indexOf(l));
        }
        l.notes.add(this);
        labels.add(l);
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
