package org.secuso.privacyfriendlynotes.code_old;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.secuso.privacyfriendlynotes.R;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREF_DEL_NOTES = "settings_del_notes";
    public static final String PREF_CUSTOM_FONT = "settings_use_custom_font_size";
    public static final String PREF_CUTSOM_FONT_SIZE = "settings_font_size";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        // TODO Auto-generated method stub
        if(preference.getKey().equals("checkbox")){
            Log.i("itchq", "checkbox");
        }
        return true;
    }
}
