package io.github.senggruppe.quicknotes.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import io.github.senggruppe.quicknotes.activities.PopActivity;
import io.github.senggruppe.quicknotes.component.NoteItem;
import io.github.senggruppe.quicknotes.core.DataStore;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.Notes;
import io.github.senggruppe.quicknotes.databinding.FragmentNotesBinding;
import io.github.senggruppe.quicknotes.util.SimpleRecyclerAdapter;

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
            b.notelist.setAdapter(new SimpleRecyclerAdapter<>(notes, NoteItem::create));
        }catch(Exception e){
            Crashlytics.logException(e);
        }

        b.AddButton.setOnClickListener(view -> {
            Intent i = new Intent(getActivity().getApplicationContext(), PopActivity.class);
            startActivity(i);
        });

        return b.getRoot();
    }
}
