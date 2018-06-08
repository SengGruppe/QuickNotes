package io.github.senggruppe.quicknotes.core;

import android.content.Context;

import java.io.IOException;

public class DataStore {
    private static Notes notes;

    public static Notes getNotes(Context ctx) throws IOException, ClassNotFoundException {
        if (notes == null) {
            notes = new Notes();
            notes.readFromFile(ctx);
        }
        return notes;
    }
}
