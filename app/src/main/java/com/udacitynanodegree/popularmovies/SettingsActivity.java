package com.udacitynanodegree.popularmovies;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {
        public static final String KEY_SORTBY_PREF = "sortBy";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(KEY_SORTBY_PREF)) {
                Preference sortByPref = findPreference(key);
                String valueSelected = sharedPreferences.getString(key, "");
                //Couldn't figure out a better way of doing this
                //Setting summaries manually based on currently selected option
                if (valueSelected.equalsIgnoreCase("popularity.desc")) {
                    sortByPref.setSummary("Most Popular");
                } else if (valueSelected.equalsIgnoreCase("vote_average.desc")) {
                    sortByPref.setSummary("Highest Rating");
                }
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
