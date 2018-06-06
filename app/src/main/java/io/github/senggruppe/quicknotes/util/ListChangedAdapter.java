package io.github.senggruppe.quicknotes.util;

import android.databinding.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ListChangedAdapter<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private final ItemAcceptor<T> itemAdded;
    private final ItemAcceptor<T> itemRemoved;
    private List<T> old;

    public ListChangedAdapter(ItemAcceptor<T> itemAdded, ItemAcceptor<T> itemRemoved) {
        this.itemAdded = itemAdded;
        this.itemRemoved = itemRemoved;
    }

    public static <T> void addAndFire(ObservableList<T> list, ObservableList.OnListChangedCallback<ObservableList<T>> lcc) {
        list.addOnListChangedCallback(lcc);
        for (T el : list) lcc.onItemRangeInserted(list, 0, list.size());
    }

    @Override
    public void onChanged(ObservableList<T> sender) {
        throw new RuntimeException("How did this happen?!");
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            itemRemoved.accept(old.get(positionStart + i));
            itemAdded.accept(sender.get(positionStart + i));
        }
        old = new ArrayList<>(sender);
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            itemAdded.accept(sender.get(positionStart + i));
        }
        old = new ArrayList<>(sender);
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
        old = new ArrayList<>(sender);
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            itemRemoved.accept(old.get(positionStart + i));
        }
        old = new ArrayList<>(sender);
    }

    public interface ItemAcceptor<T> {
        void accept(T item);
    }
}
