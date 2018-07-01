package io.github.senggruppe.quicknotes.util;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class FilteredList<T> extends AbstractList<T> {
    private List<T> data;

    public FilteredList(@NotNull List<T> data) {
        this.data = data;
    }

    @Override
    public boolean add(T t) {
        return data.add(t);
    }

    @Override
    public T get(int i) {
        Iterator<T> it = iterator();
        try {
            for (int j = 0; j < i; j++) {
                it.next();
            }
            return it.next();
        } catch (NoSuchElementException e) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
    }

    @Override
    public boolean remove(Object o) {
        return data.remove(o);
    }

    public abstract boolean allow(T el);

    @NonNull
    @Override
    public Iterator<T> iterator() {
        List<T> toIterate = new ArrayList<>();
        for (T el : data) {
            if (allow(el)) {
                toIterate.add(el);
            }
        }
        return Collections.unmodifiableList(toIterate).iterator();
    }

    @Override
    public int size() {
        int result = 0;
        for (T ignored : this) {
            result++;
        }
        return result;
    }
}
