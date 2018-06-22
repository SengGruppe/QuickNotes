package io.github.senggruppe.quicknotes.core;

import java.io.Serializable;

public interface Condition extends Serializable {
    String getType();

    String getDescription();
}
