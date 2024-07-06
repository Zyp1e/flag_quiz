package com.example.flagquiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    private Spinner optionsSpinner;
    private Spinner regionSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        optionsSpinner = view.findViewById(R.id.options_spinner);
        regionSpinner = view.findViewById(R.id.region_spinner);

        ArrayAdapter<CharSequence> optionsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.option_numbers, R.layout.spinner_item);
        optionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        optionsSpinner.setAdapter(optionsAdapter);

        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.regions, R.layout.spinner_item);
        regionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);

        loadSettings();

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> saveSettings());

        return view;
    }

    private void saveSettings() {
        int numberOfOptions = Integer.parseInt(optionsSpinner.getSelectedItem().toString());
        String region = regionSpinner.getSelectedItem().toString();

        SharedPreferences preferences = getActivity().getSharedPreferences("GameSettings", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("NumberOfOptions", numberOfOptions);
        editor.putString("Region", region);
        editor.apply();

        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void loadSettings() {
        SharedPreferences preferences = getActivity().getSharedPreferences("GameSettings", getActivity().MODE_PRIVATE);
        int numberOfOptions = preferences.getInt("NumberOfOptions", 3);
        String region = preferences.getString("Region", "All");

        ArrayAdapter<CharSequence> optionsAdapter = (ArrayAdapter<CharSequence>) optionsSpinner.getAdapter();
        int optionsPosition = optionsAdapter.getPosition(String.valueOf(numberOfOptions));
        optionsSpinner.setSelection(optionsPosition);

        ArrayAdapter<CharSequence> regionAdapter = (ArrayAdapter<CharSequence>) regionSpinner.getAdapter();
        int regionPosition = regionAdapter.getPosition(region);
        regionSpinner.setSelection(regionPosition);
    }
}
