package io.github.senggruppe.quicknotes.util;

public class Utils {
    @SafeVarargs
    public static <T> T orDefault(T... v) {
        for (T e : v) if (e != null) return e;
        throw new NullPointerException("All values null!");
    }
}
