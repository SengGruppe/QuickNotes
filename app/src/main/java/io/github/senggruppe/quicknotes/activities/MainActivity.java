package io.github.senggruppe.quicknotes.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        // Inflate (w/o data binding)
        setContentView(R.layout.activity_main);

        // Configure bottom navbar
        BottomNavigationView bnv = findViewById(R.id.bottom_nav);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == currentMenu) {
                return true;
            }
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
                    Crashlytics.logException(new Exception("No menu found!"));
                    return true;
                }
                fragmentCache.put(item.getItemId(), f);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(item.getItemId() == R.id.btnNotes);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((FragmentNotes) fragmentCache.get(R.id.btnNotes)).openDrawer();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
