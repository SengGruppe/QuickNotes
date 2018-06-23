package io.github.senggruppe.quicknotes.component;

import android.support.design.chip.Chip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.core.Label;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.databinding.NoteItemBinding;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;
import io.github.senggruppe.quicknotes.util.Utils;

public class NoteItem extends RecyclerAdapter.ViewHolder<Note> {
    private final NoteItemBinding binding;
    private AudioPlayer player;
    private boolean isExpanded;
    private final Map<Label, Chip> labelMapping = new HashMap<>();

    private NoteItem(NoteItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        binding.noteItemLabels.setChipSpacing(2);
    }

    public static NoteItem create(ViewGroup parent) {
        return new NoteItem(NoteItemBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void bind(Note el) {
        binding.setController(this);
        binding.setNote(el);

        labelMapping.clear();
        binding.noteItemLabels.removeAllViews();
        for (Label l : el.getLabels()) {
            addLabel(l);
        }

        // Player
        if (el.audioFile != null) {
            if (player == null) {
                binding.noteItemContents.addView(player = new AudioPlayer(ctx), 0);
            }
            try {
                player.setAudioFile(el.audioFile);
            } catch (IOException e) {
                Crashlytics.logException(e);
                binding.noteItemContents.removeView(player);
                TextView replacement = new TextView(ctx);
                replacement.setText("Whoops... Could not init audio player");
                binding.noteItemContents.addView(replacement, 0);
            }
        } else if (player != null) {
            binding.noteItemContents.removeView(player);
            player.release();
            player = null;
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public Note getNote() {
        return binding.getNote();
    }

    public void expand() {
        isExpanded = true;
        binding.noteItemTextContent.setMaxHeight(Integer.MAX_VALUE);
        binding.noteItemVanishgradient.setVisibility(View.GONE);
        binding.noteItemExpandButton.setImageResource(R.drawable.ic_collapse);
    }

    public void collapse() {
        isExpanded = false;
        binding.noteItemTextContent.setMaxHeight(Utils.dpToPx(ctx, 55));
        binding.noteItemVanishgradient.setVisibility(View.VISIBLE);
        binding.noteItemExpandButton.setImageResource(R.drawable.ic_expand);
    }

    public void addLabel(Label l) {
        if (labelMapping.containsKey(l)) {
            return;
        }
        Chip c = new Chip(ctx);
        c.setText(l.text);
        c.setBackgroundColor(l.color);
        labelMapping.put(l, c);
        binding.noteItemLabels.addView(c);
    }

    public void removeLabel(Label l) {
        Chip c = labelMapping.get(l);
        if (c != null) {
            binding.noteItemLabels.removeView(c);
        }
    }
}
