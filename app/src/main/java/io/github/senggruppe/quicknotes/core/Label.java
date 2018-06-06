package io.github.senggruppe.quicknotes.core;

/**
 * A label. Text should be unique.
 * Might also contain information for styling etc.
 * <p>
 * At current state might be replaced by an ordinary String or Spannable
 */
public class Label {
    public final String text;

    public Label(String text) {
        this.text = text;
    }
}
