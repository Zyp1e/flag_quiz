package com.example.flagquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class GameLogic {
    private Context context;
    private ImageView flagImage;
    private List<Button> optionButtons;
    private List<CountryFlag> flags;
    private CountryFlag currentFlag;
    private int currentScore = 0;
    private int attempts = 0;
    private int totalQuestions = 10;
    private int questionIndex = 0;

    public GameLogic(Context context, ImageView flagImage, List<Button> optionButtons, List<CountryFlag> flags) {
        this.context = context;
        this.flagImage = flagImage;
        this.optionButtons = optionButtons;
        this.flags = new ArrayList<>(flags);
    }

    public void startGame() {
        loadSettings();
        Collections.shuffle(flags);
        nextQuestion();
    }

    private void loadSettings() {
        SharedPreferences preferences = context.getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int numberOfOptions = preferences.getInt("NumberOfOptions", 3);
        String region = preferences.getString("Region", "All");

        filterFlagsBasedOnRegion(region);
        adjustNumberOfOptions(numberOfOptions);
    }

    private void filterFlagsBasedOnRegion(String region) {
        if (!region.equals("All")) {
            flags = flags.stream().filter(flag -> flag.getRegion().equals(region)).collect(Collectors.toList());
        }
    }

    private void adjustNumberOfOptions(int numberOfOptions) {
        for (int i = 0; i < optionButtons.size(); i++) {
            optionButtons.get(i).setVisibility(i < numberOfOptions ? View.VISIBLE : View.GONE);
        }
    }

    public void handleAnswer(final Button button) {
        final String selectedAnswer = button.getText().toString().trim();
        final boolean isCorrect = selectedAnswer.equals(currentFlag.getName().trim());
        attempts++;
        ((MainActivity) context).runOnUiThread(() -> {
            if (isCorrect) {
                currentScore++;
                button.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
                Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show();
                nextQuestion();  // 正确则进入下一题
            } else {
                button.setEnabled(false); // 错误则禁用按钮
                button.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
                Toast.makeText(context, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nextQuestion() {
        if (questionIndex < totalQuestions) {
            int flagIndex = new Random().nextInt(flags.size());
            currentFlag = flags.get(flagIndex);
            flagImage.setImageResource(currentFlag.getImageResourceId());
            Set<String> usedNames = new HashSet<>();
            usedNames.add(currentFlag.getName());
            int correctAnswerPosition = new Random().nextInt(optionButtons.size());
            for (int i = 0; i < optionButtons.size(); i++) {
                Button button = optionButtons.get(i);
                if (i == correctAnswerPosition) {
                    button.setText(currentFlag.getName());
                } else {
                    String alternativeName;
                    do {
                        alternativeName = flags.get(new Random().nextInt(flags.size())).getName();
                    } while (usedNames.contains(alternativeName));
                    button.setText(alternativeName);
                    usedNames.add(alternativeName);
                }
                button.setEnabled(true);
                button.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
            }
            questionIndex++;
        } else {
            endGame();
        }
    }

    private void endGame() {
        double accuracy = 0;
        if (attempts > 0) {
            accuracy = (double) currentScore / attempts * 100;
        }
        ((MainActivity) context).showResults(currentScore, attempts, accuracy);
    }

    public void saveState(Bundle outState) {
        outState.putInt("currentQuestionIndex", questionIndex);
        outState.putInt("currentScore", currentScore);
        outState.putInt("attempts", attempts);
        outState.putString("currentFlagName", currentFlag.getName());
    }

    public void restoreState(Bundle savedInstanceState) {
        questionIndex = savedInstanceState.getInt("currentQuestionIndex");
        currentScore = savedInstanceState.getInt("currentScore");
        attempts = savedInstanceState.getInt("attempts");
        String currentFlagName = savedInstanceState.getString("currentFlagName");

        for (CountryFlag flag : flags) {
            if (flag.getName().equals(currentFlagName)) {
                currentFlag = flag;
                break;
            }
        }

        restoreCurrentQuestion();
    }

    private void restoreCurrentQuestion() {
        if (questionIndex < flags.size()) {
            flagImage.setImageResource(currentFlag.getImageResourceId());
            updateOptionButtons();
        } else {
            endGame();
        }
    }

    private void updateOptionButtons() {
        Set<String> usedNames = new HashSet<>();
        usedNames.add(currentFlag.getName());

        int correctAnswerPosition = new Random().nextInt(optionButtons.size());
        for (int i = 0; i < optionButtons.size(); i++) {
            Button button = optionButtons.get(i);
            if (i == correctAnswerPosition) {
                button.setText(currentFlag.getName());
            } else {
                String alternativeName;
                do {
                    alternativeName = flags.get(new Random().nextInt(flags.size())).getName();
                } while (usedNames.contains(alternativeName));
                button.setText(alternativeName);
                usedNames.add(alternativeName);
            }
            button.setEnabled(true);
            button.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        }
    }

    public void resumeGame() {
        restoreCurrentQuestion();
    }
}
