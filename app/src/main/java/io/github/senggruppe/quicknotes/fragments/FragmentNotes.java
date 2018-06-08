package io.github.senggruppe.quicknotes.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import io.github.senggruppe.quicknotes.activities.PopActivity;
import io.github.senggruppe.quicknotes.core.DataStore;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.Notes;
import io.github.senggruppe.quicknotes.databinding.FragmentNotesBinding;

public class FragmentNotes extends Fragment {
    Notes notes;

    public FragmentNotes() throws IOException, ClassNotFoundException {
        notes = DataStore.getNotes(getActivity());
        Note n = new Note("");
        n.index = 0;
        n.content = "";
        for (int i = 0; i < 100; i++) {
            n.content = n.content + "Dies ist eine Testnotiz\n";
        }
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
        notes.add(n);
        notes.add(n);
        notes.add(n);
        notes.add(n);
        notes.add(n);
        notes.add(n);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FragmentNotesBinding b = FragmentNotesBinding.inflate(inflater);

        b.setNotes(notes);
        b.AddButton.setOnClickListener(view -> {
            Intent i = new Intent(getActivity().getApplicationContext(), PopActivity.class);
            startActivity(i);
        });

        return b.getRoot();
    }
}
