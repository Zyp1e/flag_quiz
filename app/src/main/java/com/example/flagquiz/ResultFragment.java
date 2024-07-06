package com.example.flagquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        int score = getArguments().getInt("score", 0);
        int attempts = getArguments().getInt("attempts", 0);
        double accuracy = getArguments().getDouble("accuracy", 0);

        TextView scoreView = view.findViewById(R.id.score);
        scoreView.setText("答对数/总尝试数: " + score + " / " + attempts + "\nAccuracy: " + String.format("%.2f%%", accuracy));

        Button restartButton = view.findViewById(R.id.restart_button);
        restartButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new GameFragment())
                    .commit();
        });

        Button exitButton = view.findViewById(R.id.exit_button);
        exitButton.setOnClickListener(v -> getActivity().finishAffinity());

        return view;
    }
}
