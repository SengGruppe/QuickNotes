package io.github.senggruppe.quicknotes.core;

import android.content.Context;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A label. Text should be unique.
 * Might also contain information for styling etc.
 */
public class Label implements Serializable {
    public final String text;
    public final int color;
    public final Set<Note> notes;

    protected Label(Context ctx, String text, int color) throws IOException, ClassNotFoundException {
        this.text = text;
        this.color = color;
        notes = new HashSet<>();
        if (LabelStorage.get(ctx).getLabels().contains(this)) {
            throw new IllegalStateException("Label already exists");
        }
        LabelStorage.get(ctx).addLabel(ctx, this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Label && text.equals(((Label) obj).text);
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
