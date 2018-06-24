package io.github.senggruppe.quicknotes.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import io.github.senggruppe.quicknotes.core.Label;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;

public class LabelSelector extends RecyclerView {
    public Collection<Label> labels = Collections.emptyList();

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
        setAdapter(new RecyclerAdapter<Label, LabelItem>() {
            @Override
            public int getItemCount() {
                return labels.size();
            }

            @Override
            public LabelItem createView(ViewGroup parent) {
                return LabelItem.create(parent);
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

    private static class LabelItem extends RecyclerAdapter.ViewHolder<Label> {
        private final CheckBox cb;

        private LabelItem(LinearLayout itemView) {
            super(itemView);
            itemView.setOrientation(LinearLayout.HORIZONTAL);
            itemView.addView(cb = new CheckBox(ctx));
        }

        static LabelItem create(ViewGroup parent) {
            return new LabelItem(new LinearLayout(parent.getContext()));
        }

        @Override
        public void bind(Label el) {
            cb.setText(el.text);
            cb.setBackgroundColor(el.color);
        }
    }
}
