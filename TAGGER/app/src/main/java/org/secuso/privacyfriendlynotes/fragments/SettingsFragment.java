package org.secuso.privacyfriendlynotes.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.secuso.privacyfriendlynotes.R;

/**
 * Created by Robin on 11.09.2016.
 */
public class SettingsFragment extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
    }
}