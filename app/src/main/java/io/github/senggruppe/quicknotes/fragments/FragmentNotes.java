package io.github.senggruppe.quicknotes.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import io.github.senggruppe.quicknotes.activities.MainActivity;
import io.github.senggruppe.quicknotes.activities.PopActivity;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.Notes;
import io.github.senggruppe.quicknotes.databinding.FragmentNotesBinding;

public class FragmentNotes extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FragmentNotesBinding b = FragmentNotesBinding.inflate(inflater);

        Notes notes = new Notes();

        Note n = new Note(new Date());
        n.index = 0;
        n.content = "Dies ist eine Testnotiz";
        notes.add(n);

        b.setNotes(notes);
        b.AddButton.setOnClickListener(view -> {
            Intent i = new Intent(getActivity().getApplicationContext(), PopActivity.class);
            startActivity(i);
        });

        return b.getRoot();
    }
}
