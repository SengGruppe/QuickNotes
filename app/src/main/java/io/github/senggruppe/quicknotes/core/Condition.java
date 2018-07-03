package io.github.senggruppe.quicknotes.core;

import android.content.Context;

import java.io.Serializable;

public interface Condition extends Serializable {
    String getType();

    String getDescription();

    /*
     * Cancel the (possible running/planned) condition
     */
    void cancelCondition(Context ctx);

    /*
     * Plan the new condition (e.g. setup timer for TimeCondition
     */
    void startCondition(Context ctx);
}
