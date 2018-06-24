package io.github.senggruppe.quicknotes.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class RecyclerAdapter<T, VH extends RecyclerAdapter.ViewHolder<T>> extends RecyclerView.Adapter<VH> {
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createView(parent);
    }

    public abstract VH createView(ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getItemAt(holder.ctx, position));
    }

    public abstract T getItemAt(Context ctx, int position);

    public abstract static class ViewHolder<T> extends RecyclerView.ViewHolder {
        protected Context ctx;

        public ViewHolder(View itemView) {
            super(itemView);
            ctx = itemView.getContext();
        }

        public abstract void bind(T el);
    }
}
