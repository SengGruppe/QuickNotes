package io.github.senggruppe.quicknotes.util.function;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}
