package io.github.senggruppe.quicknotes.util.function;

@FunctionalInterface
public interface BiConsumer<A, B> {
    void accept(A a, B b);
}
