package io.github.senggruppe.quicknotes.core;

import java.util.LinkedList;

/**
 * A label. Text should be unique.
 * Might also contain information for styling etc.
 * <p>
 * At current state might be replaced by an ordinary String or Spannable
 */
public class Label {
    public final String text;
    public final LinkedList<Note> notes;

    public Label(String text) {
        this.text = text;
        notes=new LinkedList<Note>();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Label)
            return text.equals((Label)obj);
        return false;
    }
}
