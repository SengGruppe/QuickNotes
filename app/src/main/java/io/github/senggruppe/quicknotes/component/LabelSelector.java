package io.github.senggruppe.quicknotes.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.core.Label;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;

public class LabelSelector extends RecyclerView {
    public Collection<Label> labels = Collections.emptyList();
    private Collection<Label> selected = new ArrayList<>();

    public LabelSelector(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LabelSelector(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LabelSelector(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context ctx) {
        setLayoutManager(new LinearLayoutManager(ctx));
        setAdapter(new RecyclerAdapter<Label, LabelItem>() {
            @NonNull
            @Override
            public LabelItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new LabelItem(new CheckBox(viewGroup.getContext()));
            }

            @Override
            public int getItemCount() {
                return labels.size();
            }

            @Override
            public Label getItemAt(Context ctx, int position) {
                Iterator<Label> it = labels.iterator();
                for (int i = 0; i < position; i++) {
                    it.next();
                }
                return it.next();
            }
        });
    }

    public Collection<Label> getSelected() {
        return Collections.unmodifiableCollection(selected);
    }

    private class LabelItem extends RecyclerAdapter.ViewHolder<Label> {
        private final CheckBox cb;

        private LabelItem(CheckBox itemView) {
            super(itemView);
            cb = itemView;
            cb.setBackground(ctx.getDrawable(R.drawable.labelselector_label_bg));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(Label el) {
            cb.setText(el.text + ' ');
            cb.getBackground().setColorFilter(el.color, PorterDuff.Mode.MULTIPLY);
            cb.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    selected.add(el);
                } else {
                    selected.remove(el);
                }
            });
        }
    }
}
