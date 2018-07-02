package io.github.senggruppe.quicknotes.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.os.Bundle;
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

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.component.AudioPlayer;
import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.NoteStorage;
import io.github.senggruppe.quicknotes.core.conditions.TimeCondition;
import io.github.senggruppe.quicknotes.fragments.DatePickerFragment;
import io.github.senggruppe.quicknotes.fragments.FragmentNotes;
import io.github.senggruppe.quicknotes.fragments.TimePickerFragment;
import io.github.senggruppe.quicknotes.util.Utils;


public class PopActivity extends AppCompatActivity implements Utils.PermissionResultHandler, OnMenuItemClickListener {
    Calendar calendar = null;
    private MediaRecorder recorder;
    private File audioMessage;
    private AudioPlayer player;

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
        Button specificationButton = findViewById(R.id.SpecifyButton);
        Button addLabelButton = findViewById(R.id.AddLabelButton);
        Switch notifySwitch = findViewById(R.id.notifySwitch);


        /*datePickButton.setOnClickListener((View View) -> {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
        timePickButton.setOnClickListener(View -> {
            TimePickerFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });*/
        saveButton.setOnClickListener(View -> {
            try {
                Note addedNote = new Note(editNote.getText().toString(), null, audioMessage);
                if (calendar == null) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, 1);
                }
                if (notifySwitch.isChecked()) {
                    Condition timeCondition = TimeCondition.setupTimedNotification(this, addedNote, calendar);
                    addedNote.conditions.add(timeCondition);
                }
                NoteStorage.get(this).addNote(this, addedNote);
                FragmentNotes.notifyDataSetChanged();




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
    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.specification_popup);
        popup.show();
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
