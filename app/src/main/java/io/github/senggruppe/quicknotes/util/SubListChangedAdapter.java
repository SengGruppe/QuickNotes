package io.github.senggruppe.quicknotes.util;

import android.databinding.ObservableList;

public class SubListChangedAdapter<T, S> extends ListChangedAdapter<T> {
    public SubListChangedAdapter(SublistExtractor<T, ObservableList<S>> sublistExtractor, ObservableList.OnListChangedCallback<ObservableList<S>> listener, boolean fireCurrent) {
        super(item -> {
                    if (fireCurrent)
                        ListChangedAdapter.addAndFire(sublistExtractor.extract(item), listener);
                    else sublistExtractor.extract(item).addOnListChangedCallback(listener);
                },
                item -> sublistExtractor.extract(item).removeOnListChangedCallback(listener));
    }

    public interface SublistExtractor<K, V> {
        V extract(K list);
    }
}
