package com.example.flagquiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class GameFragment extends Fragment {
    private ImageView flagImage;
    private List<Button> optionButtons;
    private GameLogic gameLogic;
    private LinearLayout buttonsLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        flagImage = view.findViewById(R.id.flag_image);
        buttonsLayout = view.findViewById(R.id.buttons_layout);
        optionButtons = new ArrayList<>();

        SharedPreferences preferences = getActivity().getSharedPreferences("GameSettings", getActivity().MODE_PRIVATE);
        int numberOfOptions = preferences.getInt("NumberOfOptions", 3);
        createOptionButtons(numberOfOptions);

        List<CountryFlag> flags = getFlags();
        gameLogic = new GameLogic(getActivity(), flagImage, optionButtons, flags);

        if (savedInstanceState != null) {
            gameLogic.restoreState(savedInstanceState);
        } else {
            gameLogic.startGame();
        }

        return view;
    }

    private void createOptionButtons(int numberOfOptions) {
        buttonsLayout.removeAllViews();
        optionButtons.clear();

        for (int i = 0; i < numberOfOptions; i++) {
            Button button = new Button(getContext(), null, 0, R.style.AppButton);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setVisibility(View.VISIBLE);
            button.setText("Option " + (i + 1));
            button.setOnClickListener(v -> gameLogic.handleAnswer(button));
            buttonsLayout.addView(button);
            optionButtons.add(button);
        }
    }

    private List<CountryFlag> getFlags() {
        List<CountryFlag> flags = new ArrayList<>();
        flags.add(new CountryFlag("USA", R.drawable.flag_usa, "Americas"));
        flags.add(new CountryFlag("Germany", R.drawable.flag_germany, "Europe"));
        flags.add(new CountryFlag("France", R.drawable.flag_france, "Europe"));
        flags.add(new CountryFlag("Japan", R.drawable.flag_japan, "Asia"));
        flags.add(new CountryFlag("Australia", R.drawable.flag_australia, "Oceania"));
        flags.add(new CountryFlag("China", R.drawable.flag_china, "Asia"));
        flags.add(new CountryFlag("England", R.drawable.flag_england, "Europe"));
        flags.add(new CountryFlag("Russia", R.drawable.flag_russia, "Europe"));
        flags.add(new CountryFlag("Korea", R.drawable.flag_korea, "Asia"));
        flags.add(new CountryFlag("Canada", R.drawable.flag_canada, "Americas"));
        flags.add(new CountryFlag("India", R.drawable.flag_india, "Asia"));
        return flags;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        gameLogic.saveState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            gameLogic.restoreState(savedInstanceState);
        }
    }
}
