package io.github.senggruppe.quicknotes.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.component.AudioPlayer;
import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.Label;
import io.github.senggruppe.quicknotes.core.LabelStorage;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.NotificationLevel;
import io.github.senggruppe.quicknotes.core.conditions.TimeCondition;
import io.github.senggruppe.quicknotes.fragments.DatePickerFragment;
import io.github.senggruppe.quicknotes.fragments.TimePickerFragment;
import io.github.senggruppe.quicknotes.util.Utils;


public class PopActivity extends AppCompatActivity implements Utils.PermissionResultHandler, OnMenuItemClickListener {
    Calendar calendar;
    private MediaRecorder recorder;
    private File audioFile;
    private AudioPlayer player;
    private Note note;
    private EditText editNote;
    private Switch notifySwitch;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        // Set activity appearance
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int) (dm.widthPixels * .8), (int) (dm.heightPixels * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        // components
        note = Objects.requireNonNull((Note) getIntent().getSerializableExtra("note"));
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        editNote = findViewById(R.id.noteText);
        Button saveButton = findViewById(R.id.saveButton);
        FloatingActionButton btnRecord = findViewById(R.id.btnRecord);
        player = findViewById(R.id.activity_pop_player);
        notifySwitch = findViewById(R.id.notifySwitch);
        // Button specificationButton = findViewById(R.id.SpecifyButton);
        //Button addLabelButton = findViewById(R.id.AddLabelButton);

        // fill
        editNote.setText(note.getContent());
        notifySwitch.setChecked(note.getConditions().size() > 0); // temporary as we don't have more notification settings

        // actions
        saveButton.setOnClickListener(v -> saveAndExit());
        btnRecord.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                startRecord();
            } else {
                stopRecord();
            }
            return true;
        });
    }

    private void saveAndExit() {
        note.setContent(editNote.getText().toString());
        note.setAudioFile(audioFile);

        for (Condition c : note.getConditions()) {
            c.cancelCondition(this);
        }
        note.getConditions().clear();

        note.setNotificationLevel(NotificationLevel.DEFAULT);


        if (notifySwitch.isChecked()) {
            Condition timeCondition = new TimeCondition(calendar.getTime(), note);
            timeCondition.startCondition(this);
            note.getConditions().add(timeCondition);
        }


        setResult(RESULT_OK, new Intent().putExtra("note", note));
        finish();
    }

    public void showPopUpConditions(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.specification_popup_condition);
        popup.show();
    }
    public void showPopUpLabels(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        try {
            for (Label l : LabelStorage.get(this).getLabels()) {
                popup.getMenu().add(l.toString());
                popup.show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.PopUpDateButton:
                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getSupportFragmentManager(), "datePicker");
                return true;

            case R.id.PopUpTimeButton:
                TimePickerFragment timeFragment = new TimePickerFragment();
                timeFragment.show(getSupportFragmentManager(), "timePicker");
                return true;

            default:
                return false;
        }
    }

    private void stopRecord() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            try {
                player.setAudioFile(audioFile);
            } catch (IOException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }
    }

    private void startRecord() {
        Utils.requestPermission(this, Manifest.permission.RECORD_AUDIO, () -> {
            try {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                if (audioFile != null) {
                    audioFile.delete();
                }
                audioFile = new File(this.getFilesDir().getAbsolutePath(), System.currentTimeMillis() + ".3gp");
                recorder.setOutputFile(new FileOutputStream(audioFile).getFD());
                recorder.prepare();
                recorder.start();
            } catch (IOException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
                recorder = null;
            }
        }, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    public void updateDate(Calendar c) {
        calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public void updateTime(Calendar c) {
        calendar.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Utils.onActivityResult(requestCode, resultCode, data);
    }
}
