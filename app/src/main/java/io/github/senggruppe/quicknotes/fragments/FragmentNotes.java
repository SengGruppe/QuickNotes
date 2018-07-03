package io.github.senggruppe.quicknotes.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import io.github.senggruppe.quicknotes.activities.MainActivity;
import io.github.senggruppe.quicknotes.activities.PopActivity;
import io.github.senggruppe.quicknotes.component.NoteItem;
import io.github.senggruppe.quicknotes.core.LabelStorage;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.NoteStorage;
import io.github.senggruppe.quicknotes.databinding.FragmentNotesBinding;
import io.github.senggruppe.quicknotes.util.FilteredList;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;
import io.github.senggruppe.quicknotes.util.Utils;

public class FragmentNotes extends Fragment {
    private static Runnable notifyDataSetChanged;
    private FragmentNotesBinding b;

    public static void notifyDataSetChanged() {
        if (notifyDataSetChanged != null) {
            notifyDataSetChanged.run();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        b = FragmentNotesBinding.inflate(inflater);
        try {
            List<Note> filtered = new FilteredList<Note>(NoteStorage.get(getActivity()).getNotes()) {
                @Override
                public boolean allow(Note el) {
                    return el.getContent().contains(((MainActivity) getActivity()).getSearchText());
                }
            };

            b.notelist.setLayoutManager(new LinearLayoutManager(getActivity()));
            b.notelist.setAdapter(new RecyclerAdapter<Note, NoteItem>() {
                @NonNull
                @Override
                public NoteItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    return NoteItem.create(viewGroup);
                }

                @Override
                public int getItemCount() {
                    return filtered.size();
                }

                @Override
                public Note getItemAt(Context ctx, int position) {
                    return filtered.get(position);
                }
            });
            notifyDataSetChanged = Objects.requireNonNull(b.notelist.getAdapter())::notifyDataSetChanged;

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull RecyclerView.ViewHolder target) {
                    return true;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    if (direction == ItemTouchHelper.LEFT) {
                        Utils.showMessage(getActivity(), "Archived");
                        try {
                            ((NoteItem) viewHolder).getNote().addLabel(LabelStorage.get(getActivity()).getOrCreate(getActivity(), "Archived"));
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    } else {
                        try {
                            NoteStorage.get(getActivity()).removeNote(getActivity(), ((NoteItem) viewHolder).getNote());
                            Utils.showMessage(getActivity(), "Removed");
                        } catch (IOException | ClassNotFoundException e) {
                            new AlertDialog.Builder(getActivity()).setTitle("Could not remove note: " + e.getMessage()).show();
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    }
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                        float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    getDefaultUIUtil().onDraw(c, recyclerView, ((NoteItem) viewHolder).itemView, dX, dY, actionState, isCurrentlyActive);
                }
            }).attachToRecyclerView(b.notelist);

            b.labelSelector.labels = LabelStorage.get(getActivity()).getLabels();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }

        b.AddButton.setOnClickListener(view -> {
            Intent i = new Intent(getActivity().getApplicationContext(), PopActivity.class).putExtra("note", new Note());
            Utils.startIntentForResult(getActivity(), i, (resultCode, data) -> {
                if (resultCode.equals(Activity.RESULT_OK)) {
                    try {
                        NoteStorage.get(getActivity()).addNote(getActivity(), (Note) data.getSerializableExtra("note"));
                    } catch (IOException | ClassNotFoundException e) {
                        Crashlytics.logException(e);
                    }
                }
            });
        });

        return b.getRoot();
    }

    public void openDrawer() {
        b.labelDrawer.openDrawer(Gravity.START);
    }
}
