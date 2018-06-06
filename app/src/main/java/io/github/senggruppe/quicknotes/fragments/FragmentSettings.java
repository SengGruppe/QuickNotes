package io.github.senggruppe.quicknotes.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import io.github.senggruppe.quicknotes.R;

public class FragmentSettings extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        CheckBoxPreference cb01 = (CheckBoxPreference) findPreference("cb01");
        cb01.setChecked(true);
    }
}
