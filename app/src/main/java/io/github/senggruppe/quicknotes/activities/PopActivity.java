package io.github.senggruppe.quicknotes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.sql.Time;
import java.util.Calendar;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.core.Condition;
import io.github.senggruppe.quicknotes.core.conditions.TimeCondition;
import io.github.senggruppe.quicknotes.core.Note;
import io.github.senggruppe.quicknotes.core.NoteStorage;
import io.github.senggruppe.quicknotes.core.UserNotifier;
import io.github.senggruppe.quicknotes.fragments.DatePickerFragment;

public class PopActivity extends AppCompatActivity {

    Calendar calendar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height= dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-20;

        getWindow().setAttributes(params);

        EditText editNote = findViewById(R.id.noteText);
        Button saveButton = findViewById(R.id.saveButton);
        Button datePickButton = findViewById(R.id.datePickButton);

        datePickButton.setOnClickListener((View View) ->{
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");


        });
        saveButton.setOnClickListener(View-> {
            try {
                Note addedNote = new Note(editNote.getText().toString());
                if(calendar == null){
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE,1);
                }
                Condition timeCondition = TimeCondition.SetupTimedNotification(this,calendar);
                addedNote.conditions.add(timeCondition);
                NoteStorage.get(this).addNote(addedNote);

            }catch(Exception e){
                Crashlytics.logException(e);
            }
            this.finish();

        });
    }
    public void updateCalendar(Calendar c){
        calendar = c;
    }
}
