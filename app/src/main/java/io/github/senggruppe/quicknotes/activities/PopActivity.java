package io.github.senggruppe.quicknotes.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Calendar;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.component.AudioPlayer;
import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.conditions.TimeCondition;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.NoteStorage;
import io.github.senggruppe.quicknotes.fragments.TimePickerFragment;
import io.github.senggruppe.quicknotes.util.Utils;
import io.github.senggruppe.quicknotes.fragments.DatePickerFragment;

public class PopActivity extends AppCompatActivity implements Utils.PermissionResultHandler {
    private MediaRecorder recorder;
    private File audioMessage;
    private AudioPlayer player;

    Calendar calendar = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        EditText editNote = findViewById(R.id.noteText);
        Button saveButton = findViewById(R.id.saveButton);
        FloatingActionButton btnRecord = findViewById(R.id.btnRecord);

        player = findViewById(R.id.activity_pop_player);
        Button datePickButton = findViewById(R.id.datePickButton);
        Button timePickButton = findViewById(R.id.timePickButton);
        datePickButton.setOnClickListener(view -> {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
        timePickButton.setOnClickListener(view -> {
            TimePickerFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });
        saveButton.setOnClickListener(view-> {
            try {
                Note addedNote = new Note(editNote.getText().toString(), null, audioMessage);
                if (calendar == null) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 1);
                }
                Condition timeCondition = TimeCondition.SetupTimedNotification(this, addedNote, calendar);
                addedNote.conditions.add(timeCondition);
                NoteStorage.get(this).addNote(addedNote);


            } catch (Exception e) {
                Crashlytics.logException(e);
            }
            this.finish();

        });

        btnRecord.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                startRecord();
            } else {
                stopRecord();
            }
            return true;
        });
    }

    private void stopRecord() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            try {
                player.setAudioFile(audioMessage);
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
                if (audioMessage != null) {
                    audioMessage.delete();
                }
                audioMessage = new File(this.getFilesDir().getAbsolutePath(), System.currentTimeMillis() + ".3gp");
                recorder.setOutputFile(new FileOutputStream(audioMessage).getFD());
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
        if (calendar != null) {
            calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        } else {
            calendar = c;
        }
    }

    public void updateTime(Calendar c) {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
        } else {
            calendar = c;
        }
    }
}
