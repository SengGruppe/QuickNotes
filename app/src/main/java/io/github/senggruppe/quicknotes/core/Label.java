package io.github.senggruppe.quicknotes.core;

/**
 * A label. Text should be unique.
 * Might also contain information for styling etc.
 */
public class Label {
    public final String text;
    public final int color;

    public Label(String text, int color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Label && text.equals(((Label) obj).text);
    }
}
