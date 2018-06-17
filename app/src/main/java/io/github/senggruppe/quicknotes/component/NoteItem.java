package io.github.senggruppe.quicknotes.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.databinding.NoteItemBinding;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;
import io.github.senggruppe.quicknotes.util.Utils;

public class NoteItem extends RecyclerAdapter.ViewHolder<Note> {
    private AudioPlayer player;
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
        if (el.audioFile != null) {
            if (player == null) binding.noteItemContents.addView(player = new AudioPlayer(ctx), 0);
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
}
