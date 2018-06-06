package io.github.senggruppe.quicknotes.core;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of notes, including load/save functionality
 * TODO: add fields for stats about the note list (like a label list for the navbar)
 */
public class Notes extends ObservableArrayList<Note> {
    public final ObservableList<Label> labels = new ObservableArrayList<>();

    public Notes() {
        super();
    }

    public void saveToFile(Context ctx) throws IOException {
        try (OutputStream out = new FileOutputStream(new File(ctx.getFilesDir(), "notes.ser"))) {
            write(out);
        }
    }

    public void write(OutputStream out) throws IOException {
        try (ObjectOutput oo = new ObjectOutputStream(out)) {
            for (Note note : this) oo.writeObject(note);
        }
    }

    public void readFromFile(Context ctx) throws IOException, ClassNotFoundException {
        try (InputStream out = new FileInputStream(new File(ctx.getFilesDir(), "notes.ser"))) {
            read(out);
        }
    }

    public void read(InputStream in) throws IOException, ClassNotFoundException {
        List<Note> tmp = new ArrayList<>();
        try (ObjectInput oi = new ObjectInputStream(in)) {
            //noinspection InfiniteLoopStatement - will get broken by EOF
            while (true) tmp.add((Note) oi.readObject());
        } catch (EOFException ignored) {
            // finish
        }
        clear();
        addAll(tmp);
    }
}
