package io.github.senggruppe.quicknotes.core;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.senggruppe.quicknotes.util.ListChangedAdapter;

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
}
