package io.github.senggruppe.quicknotes.core;

import android.content.Context;
import android.databinding.ObservableList;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataStore {
    private static Notes notes;

    public static Notes getNotes(Context ctx) throws IOException, ClassNotFoundException {
        if (notes == null) {
            notes = new Notes();
            try {
                notes.readFromFile(ctx);
            } catch (FileNotFoundException ignored) {
                // then do not load...
            }
        }
        return notes;
    }

    public static void addNote(Note n){
        notes.add(n);
    }

    public static void addLabel(Label l){
        notes.labels.add(l);
    }

    public static ObservableList<Label> getLabels() {
        return notes.labels;
    }
}
