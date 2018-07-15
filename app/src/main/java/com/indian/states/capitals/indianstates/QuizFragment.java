package com.indian.states.capitals.indianstates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuizFragment extends Fragment{

    View quizFragment;

    public static QuizFragment newInstance() {

        return new QuizFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        quizFragment = inflater.inflate(R.layout.fragment_quiz,container,false);

        return quizFragment;
    }
}
