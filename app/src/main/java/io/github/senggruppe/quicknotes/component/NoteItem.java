package io.github.senggruppe.quicknotes.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.databinding.NoteItemBinding;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;

public class NoteItem extends RecyclerAdapter.ViewHolder<Note> {
    private final NoteItemBinding binding;
    private boolean isExpanded;

    private NoteItem(NoteItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static NoteItem create(ViewGroup parent) {
        return new NoteItem(NoteItemBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void bind(Note el) {
        binding.setController(this);
        binding.setNote(el);
        binding.noteItemLabels.setChipSpacing(2);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public Note getNote() {
        return binding.getNote();
    }

    public void expand() {
        isExpanded = true;
        binding.noteItemContent.setMaxHeight(Integer.MAX_VALUE);
        binding.noteItemVanishgradient.setVisibility(View.GONE);
        binding.noteItemExpandButton.setImageResource(R.drawable.ic_collapse);
    }

    public void collapse() {
        isExpanded = false;
        binding.noteItemContent.setMaxHeight((int) (55 * ctx.getResources().getDisplayMetrics().density));
        binding.noteItemVanishgradient.setVisibility(View.VISIBLE);
        binding.noteItemExpandButton.setImageResource(R.drawable.ic_expand);
    }
}
