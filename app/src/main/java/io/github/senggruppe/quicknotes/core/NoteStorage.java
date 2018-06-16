package io.github.senggruppe.quicknotes.core;

import android.content.Context;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 */
public class NoteStorage {
    private static NoteStorage published;
    private final List<Note> notes = new ArrayList<>();

    private NoteStorage() {

    }

    public static NoteStorage get(Context ctx) throws IOException, ClassNotFoundException {
        if (published == null) {
            published = new NoteStorage();
            try {
                published.readFromFile(ctx);
            } catch (FileNotFoundException ignored) {
                // then do not load...
            }
        }
        return published;
    }

    public void addNote(Note n) {
        notes.add(n);
    }

    public void removeNote(Note n) {
        if (n.audioFile != null) n.audioFile.delete();
        notes.remove(n);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void saveToFile(Context ctx) throws IOException {
        try (OutputStream out = new FileOutputStream(new File(ctx.getFilesDir(), "notes.ser"))) {
            write(out);
        }
    }

    public void write(OutputStream out) throws IOException {
        try (ObjectOutput oo = new ObjectOutputStream(out)) {
            for (Note note : notes) oo.writeObject(note);
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
        notes.clear();
        notes.addAll(tmp);
    }
}
