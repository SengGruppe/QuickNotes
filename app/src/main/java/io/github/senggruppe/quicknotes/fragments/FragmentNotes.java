package io.github.senggruppe.quicknotes.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import io.github.senggruppe.quicknotes.activities.PopActivity;
import io.github.senggruppe.quicknotes.component.NoteItem;
import io.github.senggruppe.quicknotes.core.DataStore;
import io.github.senggruppe.quicknotes.core.Label;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.Notes;
import io.github.senggruppe.quicknotes.databinding.FragmentNotesBinding;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;

public class FragmentNotes extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FragmentNotesBinding b = FragmentNotesBinding.inflate(inflater);
        Notes notes;
        try {
             notes = DataStore.getNotes(getActivity());
            Note n = new Note("Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz Dies ist eine Testnotiz");
            n.index = 0;

            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);
            notes.add(n);

            b.setNotes(notes);
            b.notelist.setLayoutManager(new LinearLayoutManager(getActivity()));
            b.notelist.setAdapter(new RecyclerAdapter<>(notes, NoteItem::create));

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return true;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    ((NoteItem) viewHolder).getNote().labels.add(new Label("Archived"));
                    // TODO implement better handling (-> labels)
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    getDefaultUIUtil().onDraw(c, recyclerView, ((NoteItem) viewHolder).itemView, dX, dY, actionState, isCurrentlyActive);
                }
            }).attachToRecyclerView(b.notelist);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }

        b.AddButton.setOnClickListener(view -> {
            Intent i = new Intent(getActivity().getApplicationContext(), PopActivity.class);
            startActivity(i);
        });

        return b.getRoot();
    }
}
