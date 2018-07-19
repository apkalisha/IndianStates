package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QuizMainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        textView = findViewById(R.id.text);
        Intent intent = getIntent();
        textView.setText(intent.getStringExtra("categoryName"));
    }
}
