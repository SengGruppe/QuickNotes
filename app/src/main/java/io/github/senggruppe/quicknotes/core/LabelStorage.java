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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A list of labels, including load/save functionality
 */
public class LabelStorage {
    private static LabelStorage published;
    private final Set<Label> labels = new HashSet<>();

    private LabelStorage() {

    }

    public static LabelStorage get(Context ctx) throws IOException, ClassNotFoundException {
        if (published == null) {
            published = new LabelStorage();
            try {
                published.readFromFile(ctx);
            } catch (FileNotFoundException ignored) {
                // then do not load...
            }
        }
        return published;
    }

    public void addLabel(Context ctx, Label l) throws IOException {
        labels.add(l);
        saveToFile(ctx);
    }

    public void removeLabel(Context ctx, Label l) throws IOException {
        labels.remove(l);
        saveToFile(ctx);
    }

    public Set<Label> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    public void saveToFile(Context ctx) throws IOException {
        try (OutputStream out = new FileOutputStream(new File(ctx.getFilesDir(), "labels.ser"))) {
            write(out);
        }
    }

    public void write(OutputStream out) throws IOException {
        try (ObjectOutput oo = new ObjectOutputStream(out)) {
            for (Label note : labels) oo.writeObject(note);
        }
    }

    public void readFromFile(Context ctx) throws IOException, ClassNotFoundException {
        try (InputStream out = new FileInputStream(new File(ctx.getFilesDir(), "labels.ser"))) {
            read(out);
        }
    }

    public void read(InputStream in) throws IOException, ClassNotFoundException {
        List<Label> tmp = new ArrayList<>();
        try (ObjectInput oi = new ObjectInputStream(in)) {
            //noinspection InfiniteLoopStatement - will get broken by EOF
            while (true) tmp.add((Label) oi.readObject());
        } catch (EOFException ignored) {
            // finish
        }
        labels.clear();
        labels.addAll(tmp);
    }
}