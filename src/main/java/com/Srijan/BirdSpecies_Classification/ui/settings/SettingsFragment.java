package com.Srijan.BirdSpecies_Classification.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.Srijan.BirdSpecies_Classification.R;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.settings_activity, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);


        if (useDarkTheme) {
            getContext().setTheme(R.style.AppTheme_Dark_NoActionBar);
            final TextView textView = root.findViewById(R.id.text_settings);

            textView.setText("Dark Theme Set!");
        }

        super.onCreate(savedInstanceState);


        Switch toggle = root.findViewById(R.id.switch1);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });
        return root;
    }


    public void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        Intent intent = ((Activity) getContext()).getIntent();
        getActivity().finish();

        startActivity(intent);
    }

}
