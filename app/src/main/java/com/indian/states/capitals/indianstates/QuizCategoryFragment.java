package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class QuizCategoryFragment extends Fragment {

    View quizFragment;
    CardView gk, capitals, area, population, literacy, sex_ratio;
    String GK, CAPITALS, AREA, POPULATION, LITERACY, SEXRATIO;


    public static QuizCategoryFragment newInstance() {

        return new QuizCategoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        quizFragment = inflater.inflate(R.layout.fragment_quiz, container, false);
        gk = quizFragment.findViewById(R.id.gk_cardview);
        capitals = quizFragment.findViewById(R.id.capitals_cardview);
        area = quizFragment.findViewById(R.id.area_cardview);
        population = quizFragment.findViewById(R.id.population_cardview);
        literacy = quizFragment.findViewById(R.id.lieracyrate_cardview);
        sex_ratio = quizFragment.findViewById(R.id.sexRatio_cardview);

        GK = getString(R.string.generalKnowledge_fire);
        AREA = getString(R.string.area_fire);
        CAPITALS = getString(R.string.capitals_fire);
        POPULATION = getString(R.string.population_fire);
        LITERACY = getString(R.string.literacy_fire);
        SEXRATIO = getString(R.string.sexRatio_fire);
        gk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callQuizActivity(0);
            }
        });
        capitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callQuizActivity(1);
            }
        });
        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callQuizActivity(2);
            }
        });

        population.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callQuizActivity(3);
            }
        });
        literacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callQuizActivity(4);
            }
        });
        sex_ratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callQuizActivity(5);
            }
        });

        return quizFragment;
    }

    public void callQuizActivity(int category) {
        String CategoryName = null;
        switch (category) {
            case 0:
                CategoryName = GK;
                break;
            case 1:
                CategoryName = CAPITALS;
                break;
            case 2:
                CategoryName = AREA;
                break;
            case 3:
                CategoryName = POPULATION;
                break;
            case 4:
               CategoryName = LITERACY;
                break;
            case 5:
                CategoryName = SEXRATIO;
                break;
        }
        Intent intent = new Intent(getContext(), HighScoreActivity.class);
        Questions.category = CategoryName;
        startActivity(intent);
    }

}
