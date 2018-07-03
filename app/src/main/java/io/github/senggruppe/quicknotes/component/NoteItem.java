package io.github.senggruppe.quicknotes.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.chip.Chip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.activities.PopActivity;
import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.Label;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.NoteStorage;
import io.github.senggruppe.quicknotes.databinding.NoteItemBinding;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;
import io.github.senggruppe.quicknotes.util.Utils;

public class NoteItem extends RecyclerAdapter.ViewHolder<Note> {
    private final NoteItemBinding binding;
    private final Map<Label, Chip> labelMapping = new HashMap<>();
    private AudioPlayer player;
    private boolean isExpanded;
    private Note note;

    private NoteItem(NoteItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        binding.setController(this);
        binding.noteItemLabels.setChipSpacing(2);
    }

    public static NoteItem create(ViewGroup parent) {
        return new NoteItem(NoteItemBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void bind(Note el) {
        note = el;
        note.bindToView(this);

        // Text
        binding.noteItemTextContent.setText(el.getContent());

        // Labels
        labelMapping.clear();
        binding.noteItemLabels.removeAllViews();
        for (Label l : el.getLabels()) {
            addLabel(l);
        }

        // Player
        if (el.getAudioFile() != null) {
            if (player == null) {
                binding.noteItemContents.addView(player = new AudioPlayer(ctx), 0);
            }
            try {
                player.setAudioFile(el.getAudioFile());
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

        // Condition
        if (el.getConditions().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Condition c : el.getConditions()) {
                sb.append(c.getDescription()).append(" & ");
            }
            binding.noteItemConditionSummary.setText(sb.substring(0, sb.toString().length() - 3));
        } else {
            binding.noteItemConditionSummary.setText("");
        }

        // timestamp
        binding.noteItemCreation.setText(formatCreationDate(ctx, el.getCreationDate()));
    }

    public boolean isExpanded() {
        return isExpanded;
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

    public void editNote() {
        Context ctx = binding.getRoot().getContext();
        Activity a = Objects.requireNonNull(Utils.getActivity(binding.getRoot()));
        Utils.startIntentForResult(a, new Intent(ctx, PopActivity.class).putExtra("note", note), (resultCode, data) -> {
            if (resultCode.equals(Activity.RESULT_OK)) {
                try {
                    NoteStorage.get(ctx).removeNote(ctx, note);
                    NoteStorage.get(ctx).addNote(ctx, (Note) data.getSerializableExtra("note"));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
        });
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

    private String formatCreationDate(Context ctx, Date date) {
        long dur = System.currentTimeMillis() - date.getTime();
        Period p = new Period(dur);
        if (p.getYears() > 0) {
            p = new Period(dur, PeriodType.years());
        } else if (p.getMonths() > 0) {
            p = new Period(dur, PeriodType.months());
        } else if (p.getWeeks() > 0) {
            p = new Period(dur, PeriodType.weeks());
        } else if (p.getDays() > 0) {
            p = new Period(dur, PeriodType.days());
        } else if (p.getHours() > 0) {
            p = new Period(dur, PeriodType.hours());
        } else if (p.getMinutes() > 0) {
            p = new Period(dur, PeriodType.minutes());
        } else if (p.getSeconds() >= 30) {
            p = new Period(dur, PeriodType.seconds());
        } else {
            p = null;
        }

        return ctx.getString(R.string.note_item_creationdate, p == null
                ? ctx.getString(R.string.time_below_30s)
                : PeriodFormat.wordBased(Locale.getDefault()).print(p));
    }

    public Note getNote() {
        return note;
    }
}
