package io.github.senggruppe.quicknotes.fragments;

import android.app.AlertDialog;
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

import java.io.IOException;
import java.util.Objects;

import io.github.senggruppe.quicknotes.activities.PopActivity;
import io.github.senggruppe.quicknotes.component.NoteItem;
import io.github.senggruppe.quicknotes.core.NoteStorage;
import io.github.senggruppe.quicknotes.databinding.FragmentNotesBinding;
import io.github.senggruppe.quicknotes.util.RecyclerAdapter;
import io.github.senggruppe.quicknotes.util.Utils;

public class FragmentNotes extends Fragment {
    private static Runnable notifyDataSetChanged;

    public static void notifyDataSetChanged() {
        if (notifyDataSetChanged != null) notifyDataSetChanged.run();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FragmentNotesBinding b = FragmentNotesBinding.inflate(inflater);
        try {
            b.notelist.setLayoutManager(new LinearLayoutManager(getActivity()));
            b.notelist.setAdapter(new RecyclerAdapter<>(NoteStorage.get(getActivity()).getNotes(), NoteItem::create));
            notifyDataSetChanged = Objects.requireNonNull(b.notelist.getAdapter())::notifyDataSetChanged;

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return true;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    if (direction == ItemTouchHelper.LEFT) {
                        Utils.showMessage(getActivity(), "Archived");
                    } else {
                        try {
                            NoteStorage.get(getActivity()).removeNote(getActivity(), ((NoteItem) viewHolder).getNote());
                        } catch (IOException | ClassNotFoundException e) {
                            new AlertDialog.Builder(getActivity()).setTitle("Could not remove note: " + e.getMessage()).show();
                            e.printStackTrace();
                            Crashlytics.logException(e);
                        }
                    }
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
