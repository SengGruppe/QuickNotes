package io.github.senggruppe.quicknotes.util;

import android.content.Context;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class SimpleRecyclerAdapter<T, VH extends SimpleRecyclerAdapter.ViewHolder<T>> extends RecyclerView.Adapter<VH> {
    private final ObservableList<T> list;
    private final ViewHolderProvider<VH> provider;

    public SimpleRecyclerAdapter(ObservableList<T> list, ViewHolderProvider<VH> provider) {
        this.list = list;
        this.provider = provider;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return provider.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ViewHolderProvider<VH> {
        VH create(ViewGroup parent);
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        protected Context ctx;

        public ViewHolder(View itemView) {
            super(itemView);
            ctx = itemView.getContext();
        }

        public abstract void bind(T el);
    }
}
