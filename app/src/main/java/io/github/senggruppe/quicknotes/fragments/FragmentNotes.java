package io.github.senggruppe.quicknotes.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.senggruppe.quicknotes.core.Notes;
import io.github.senggruppe.quicknotes.databinding.FragmentNotesBinding;

public class FragmentNotes extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FragmentNotesBinding b = FragmentNotesBinding.inflate(inflater);

        Notes notes = new Notes();
        b.setNotes(notes);

        return b.getRoot();
    }
}
