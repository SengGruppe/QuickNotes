package io.github.senggruppe.quicknotes.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A label. Text should be unique.
 * Might also contain information for styling etc.
 */
public class Label {
    public final String text;
    public final int color;
    public final Set<Note> notes;

    public Label(String text, int color) {
        this.text = text;
        this.color = color;
        notes = new HashSet<Note>();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Label && text.equals(((Label) obj).text);
    }
}
