package io.github.senggruppe.quicknotes.core;

import android.content.Context;

import java.io.Serializable;

public interface Condition extends Serializable {
    String getType();

    String getDescription();
    void cancleCondition(Context ctx);
}
