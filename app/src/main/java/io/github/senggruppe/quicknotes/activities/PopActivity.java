package io.github.senggruppe.quicknotes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;

import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.core.DataStore;
import io.github.senggruppe.quicknotes.core.Note;

public class PopActivity extends AppCompatActivity {


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

        saveButton.setOnClickListener(View-> {
            try {
                DataStore.getNotes(this).add(new Note(editNote.getText().toString()));
            }catch(Exception e){
                Crashlytics.logException(e);
            }
            this.finish();

        });

    }
}
