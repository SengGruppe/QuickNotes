package io.github.senggruppe.quicknotes.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.github.senggruppe.quicknotes.R;
import io.github.senggruppe.quicknotes.fragments.FragmentNotes;
import io.github.senggruppe.quicknotes.fragments.FragmentNotificationLevels;
import io.github.senggruppe.quicknotes.fragments.FragmentSettings;

public class MainActivity extends AppCompatActivity {
    private final SparseArray<Fragment> fragmentCache = new SparseArray<>();
    int currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        // inflate (w/o data binding)
        setContentView(R.layout.activity_main);

        BottomNavigationView bnv = findViewById(R.id.bottom_nav);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == currentMenu) return true;
            Fragment f = fragmentCache.get(item.getItemId());
            if (f == null) {
                switch (item.getItemId()) {
                    case R.id.btnNotLvls:
                        f = new FragmentNotificationLevels();
                        break;
                    case R.id.btnNotes:
                        f = new FragmentNotes();
                        break;
                    case R.id.btnSettings:
                        f = new FragmentSettings();
                        break;
                }
                if (f == null) {
                    //Crashlytics.logException(new Exception("No menu found!"));
                    return true;
                }
                fragmentCache.put(item.getItemId(), f);
            }
            getFragmentManager().beginTransaction().replace(R.id.content, f).commit();
            currentMenu = item.getItemId();
            return true;
        });
        bnv.setSelectedItemId(R.id.btnNotes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
}
